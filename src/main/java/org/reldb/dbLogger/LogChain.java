package org.reldb.dbLogger;

import java.util.HashMap;

/**
 * A derivative of HashMap for holding log attribute/value pairs and invoking Log.insert().
 */
public class LogChain extends HashMap<Object, Object> {

    /**
     * Insert an attribute/value pair.
     *
     * @param attribute An attribute name.
     * @param value A value associated with the attribute name.
     * @return This LogChain instance.
     */
    public LogChain log(String attribute, Object value) {
        put(attribute, value);
        return this;
    }

    /**
     * Invoke Logger's insert() method after using Flattener to flatten this LogChain Map.
     *
     * @param logger A Logger instance.
     */
    public void insert(Logger logger) {
        logger.insert(Flattener.flatten(this));
    }
}