package org.reldb.dbLogger;

import java.sql.SQLException;

/**
 * A standard JDBC connection to a SQLite database.
 */
public class SQLiteDatabase extends Database {

	/**
	 * Create a connection to a SQLite database. 
	 * 
	 * @param dbPath path and filename of database file. E.g., "/tmp/mydatabase.sqlite"
	 * @throws SQLException error.
	 */
	public SQLiteDatabase(final String dbPath) throws SQLException {
		super("jdbc:sqlite:" + dbPath);
	}
	
}
