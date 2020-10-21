package org.reldb.dbLogger;

import java.util.HashMap;

/**
 * Start of an ergonomic chain of Logger.log(...).log(...).log(...).insert(logdb) invocations.
 */
public class Logger extends HashMap<Object, Object> {

    /**
     * Obtain a LoggerChained instance and invoke its log() method with attribute and value.
     *
     * @param attribute - An attribute name
     * @param value - A value associated with the attribute name
     * @return - A new LoggerChained instance
     */
    public static LoggerChained log(String attribute, Object value) {
        LoggerChained logger = new LoggerChained();
        logger.log(attribute, value);
        return logger;
    }
}
