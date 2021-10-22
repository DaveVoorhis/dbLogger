package org.reldb.dbLogger;

import java.io.Closeable;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

/**
 * A Logger provides facilities to log data to a SQL database table.
 */
public class Logger implements Closeable {
	private final Connection database;
	private final String logName;
 	
	private final Set<String> logAttributes = new HashSet<>();
	private final Map<String, PreparedStatement> preparedStatementCache = new HashMap<>();

	/**
	 * Create a Logger to be able to log data to a SQL database table.
	 * 
	 * @param database - JDBC database Connection
	 * @param logName - Name of table to be created in database
	 */
	public Logger(Connection database, String logName) {
		this.database = database;
		this.logName = logName;
	}

	/**
	 * Close the database connection.
	 */
	public void close() {
		// Close prepared statements.
		try {
			preparedStatementCache.values().forEach(preparedStatement -> {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			});
			preparedStatementCache.clear();
		} catch (Exception e) {
			System.out.println("Log " + logName + " failed to close: " + e);
		}
	}

	// Create the log table if it doesn't exist. Obtain its attributes if it does.
	// Return true if columnSpec has been used by creating the table.
	private boolean ensureTableExists(String columnSpec) throws SQLException {
		if (logAttributes.isEmpty()) {
			try (Statement statement = database.createStatement()) {
				// If the table already exists, get logAttributes from its metadata
				try {
					ResultSet rset = statement.executeQuery("SELECT * FROM " + logName + ";");
					ResultSetMetaData metadata = rset.getMetaData();
					for (int columnIndex = 1; columnIndex <= metadata.getColumnCount(); columnIndex++) {
						logAttributes.add(metadata.getColumnName(columnIndex));
					}
					return false;
				} catch (SQLException sqe) {
					// Otherwise, create table
					statement.execute("CREATE TABLE " + logName + " (" + columnSpec + ");");
					return true;
				}
			}
		}
		return false;
	}

	// Create the log table if it doesn't exist; add a column if it does.
	private void ensureColumnExists(String columnName, String columnType) throws SQLException {
		String columnSpec = columnName + " " + columnType;
		if (ensureTableExists(columnSpec)) {
			return;
		}
		if (!logAttributes.contains(columnName)) {
			try (Statement statement = database.createStatement()) {
				statement.execute("ALTER TABLE " + logName + " ADD " + columnSpec + ";");
				logAttributes.add(columnName);
			}
		}
	}
	
	/** 
	 * Obtain the appropriate SQL type name given the class of a data type we wish to log.
	 * 
	 * @param clazz Class of data type we wish to log.
	 * @return SQL type name to use to record data.
	 */
	protected String getSqlTypeFor(Class<?> clazz) {
		return switch (clazz.getCanonicalName()) {
			case "java.lang.Byte", "java.lang.Integer", "java.lang.Long", "java.lang.Short" -> "INTEGER";
			case "java.lang.Double", "java.lang.Float" -> "NUMERIC";
			default -> "TEXT";
		};
	}
	
	/**
	 * Given a column name provided in the data we wish to log, translate any characters
	 * that are not acceptable SQL.
	 * 
	 * @param rawColumnName Column name provided in log data.
	 * @return SQL-legal column name.
	 */
	protected String cleanColumnName(String rawColumnName) {
		return Database.cleanSqlIdentifier(rawColumnName);
	}
	
	/**
	 * Given a Map of column_name to data we wish to log, insert it into a table in the database, creating or expanding the table as needed.
	 * 
	 * NOTE: Changing the type of a given column is not supported, and attempts may or may not break.
	 * 
	 * @param fullRecord Map representing a record of data we wish to log
	 */
	public void insert(Map<String, Object> fullRecord) {
		try {
			Set<Entry<String, Object>> entries = fullRecord.entrySet();
			Vector<Object> values = new Vector<>();
			StringBuilder columnSpec = new StringBuilder();
			StringBuilder parmSpec = new StringBuilder();
			for (Entry<String, Object> entry: entries) {
				if (columnSpec.length() > 0) {
					columnSpec.append(", ");
					parmSpec.append(", ");
				}
				String columnName = cleanColumnName(entry.getKey());
				Object value = entry.getValue();
				ensureColumnExists(columnName, getSqlTypeFor(value.getClass()));
				columnSpec.append(columnName);
				parmSpec.append("?");
				values.add(value);
			}
			String sql = "INSERT INTO " + logName + " (" + columnSpec + ") VALUES (" + parmSpec + ");";
			PreparedStatement statement = preparedStatementCache.get(sql);
			if (statement == null) {
				statement = database.prepareStatement(sql);
				preparedStatementCache.put(sql, statement);
			}
			for (int index = 0; index < values.size(); index++) {
				statement.setObject(index + 1, values.get(index));
			}
			statement.execute();
		} catch (SQLException sqe) {
			System.out.println("Log " + logName + " failed: " + sqe + ": unable to log " + fullRecord);
		}
	}

}
