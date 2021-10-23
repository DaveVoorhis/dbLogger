# dbLogger - Log to a SQL Database

dbLogger is a simple logging layer that logs to an SQL database rather
than the usual text-based log files.

If you haven't used SQL-based logs, you don't know what 
you've been missing!

Logs in an SQL database can be filtered, queried, sorted, summarised,
and analysed _much_ faster and easier than with text-file logs.

By default, dbLogger is designed to log to SQLite databases. 
For log viewing and analysis, you may want to use something
like DB Browser. See https://sqlitebrowser.org.

## Get dbLogger

Get dbLogger from Maven Central: https://search.maven.org/artifact/org.reldb/dbLogger

## Use dbLogger

*Import dbLogger.*

```
import org.reldb.dbLogger.*;
```

*Create or open a log database, which can contain multiple log tables.*

```
var db = new SQLiteDatabase("mylogs.sqlite");
```

*Create or open a log table.*

```
var logger = new Log(db.getConnection(), "mylog1");
```

*Write an entry to a log table.*

```
Log.log("column_a", 3)
   .log("column_b", 4.3)
   .log("column_c", "blah")
   .log("column_d", 33)
   .insert(logger);
```

*Close everything when done.*

```
logger.close();
db.close();
```

Then open _mylogs.sqlite_ with your favourite SQLite database browser to examine and query your logs.

See _SQLiteDatabaseTest.java_ under _/src/test/java/org/reldb/dbLogger/tests_ for more examples.

This is the source project for the dbLogger library.

To build and test the dbLogger library, and publish to your local Maven repository:

```
gradle clean build test publishToMavenLocal
```

