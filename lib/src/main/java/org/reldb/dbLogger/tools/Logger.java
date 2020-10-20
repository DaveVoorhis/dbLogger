package org.reldb.dbLogger.tools;

import org.reldb.dbLogger.Log;

import java.util.HashMap;
import java.util.Map;

public class Logger extends HashMap<Object, Object> {
    public static Logger log() {
        return new Logger();
    }

    public Logger log(String attribute, Object value) {
        put(attribute, value);
        return this;
    }

    public void insert(Log log) {
        Map<String, Object> logOutput = new HashMap<>();
        Flattener.flatten(this, logOutput);
        log.insert(logOutput);
    }
}
