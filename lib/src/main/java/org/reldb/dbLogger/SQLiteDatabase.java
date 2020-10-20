package org.reldb.dbLogger;

/**
 * A standard JDBC connection to a SQLite database.
 */
public class SQLiteDatabase extends Database {

	/**
	 * Create a connection to a SQLite database. 
	 * 
	 * @see super.getConnection()
	 * 
	 * @param dbPath - path and filename of database file. E.g., "/tmp/mydatabase.sqlite"
	 */
	public SQLiteDatabase(final String dbPath) {
		super("jdbc:sqlite:" + dbPath);
	}
	
}
