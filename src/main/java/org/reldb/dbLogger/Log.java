package org.reldb.dbLogger;

/**
 * Start of an ergonomic chain of Log.log(...).log(...).log(...).insert(logdb) invocations.
 */
public class Log {

    /**
     * Obtain a LogChain instance and invoke its log() method with attribute and value.
     *
     * @param attribute - An attribute name
     * @param value - A value associated with the attribute name
     * @return - A new LogChain instance
     */
    public static LogChain log(String attribute, Object value) {
        return new LogChain().log(attribute, value);
    }
}
