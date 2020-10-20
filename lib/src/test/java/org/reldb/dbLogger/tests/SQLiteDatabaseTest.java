/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.reldb.dbLogger.tests;

import org.junit.jupiter.api.Test;

import org.reldb.dbLogger.Log;
import org.reldb.dbLogger.SQLiteDatabase;
import org.reldb.dbLogger.tools.Logger;

class SQLiteDatabaseTest {

    @Test void testLogBasic() {
        try (var db = new SQLiteDatabase("./testdb.sqlite")) {
            try (var log = new Log(db.getConnection(), "testlog")) {
                for (int i = 0; i < 10; i++) {
                    Logger.log("a", i)
                            .log("b", "" + i)
                            .log("c", "blah")
                            .insert(log);
                }
                for (int i = 11; i < 20; i++) {
                    Logger.log("a", i)
                            .log("b", "" + i)
                            .log("c", "blah")
                            .log("d", (float) i)
                            .insert(log);
                }
            }
        }
    }

    @Test void testLogNested1() {
        try (var db = new SQLiteDatabase("./testdb.sqlite")) {
            try (var log = new Log(db.getConnection(), "testlognested")) {
                for (int i = 0; i < 10; i++) {
                    Logger.log("a", i)
                            .log("b", "" + i)
                            .log("c", "blah")
                            .log("n",
                                    Logger.log("p", i * 10)
                                            .log("q", (double) (i * 20))
                                            .log("a", i + 100))
                            .insert(log);
                }
                for (int i = 11; i < 20; i++) {
                    Logger.log("a", i)
                            .log("b", "" + i)
                            .log("c", "blah")
                            .log("d", (float) i)
                            .insert(log);
                }
            }
        }
    }

}
