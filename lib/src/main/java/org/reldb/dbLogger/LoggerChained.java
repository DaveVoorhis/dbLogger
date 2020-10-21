package org.reldb.dbLogger;

import java.util.HashMap;

/**
 * A derivative of HashMap for holding log attribute/value pairs and invoking Log.insert().
 */
public class LoggerChained extends HashMap<Object, Object> {

    /**
     * Insert an attribute/value pair.
     *
     * @param attribute - An attribute name
     * @param value - A value associated with the attribute name
     * @return - This LoggerChained instance
     */
    public LoggerChained log(String attribute, Object value) {
        put(attribute, value);
        return this;
    }

    /**
     * Invoke Log's insert() method after using Flattener to flatten this LoggerChained Map.
     *
     * @param log - A Log instancee
     */
    public void insert(Log log) {
        log.insert(Flattener.flatten(this));
    }
}