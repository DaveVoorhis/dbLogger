package org.reldb.dbLogger;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A standard JDBC connection to a JDBC database.
 */
public class Database implements Closeable {
	private Connection conn = null;

	/**
	 * Return a string with SQL-breaking characters translated to '_'.
	 *  
	 * @param source - A String.
	 * @return - Cleaned string.
	 */
	public static String cleanSqlIdentifier(String source) {
		return source.replaceAll("\\P{Alnum}", "_");
	}
	
	/**
	 * Create a connection to a database.
	 * 
	 * @param dbURL - full JDBC database URL.
	 */
	public Database(final String dbURL) {
		try {
			conn = DriverManager.getConnection(dbURL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the connection to the database.
	 */
	public void close() {
		try {
			conn.close();
			conn = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Obtain a JDBC Connection to this database. NULL if connection failed.
	 * 
	 * @return Connection, or null if failed.
	 */
	public Connection getConnection() {
		return conn;
	}
	
}
