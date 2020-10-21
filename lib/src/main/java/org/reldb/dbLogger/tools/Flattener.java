package org.reldb.dbLogger.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copy a hierarchy of Map, List, and scalar value containment -- e.g., an instance/container-instance 
 * hierarchy representing a JSON object, HTML DOM, etc. -- to a flat Map.
 */
public class Flattener {

	@FunctionalInterface
	interface AttributeValuePredicate {
		boolean isTrue(Object name, Object value);
	}
	
	/**
	 * Flatten a datum named attributeName which may be a List, Map, or some scalar value to the target Map.
	 * 
	 * @param attributeName - The name of this attribute as its known in its parent container. Used to construct new attribute names.
	 * @param value - The List, Map to be flattened and stored, or the value to be stored, in the target.
	 * @param target - The Map to receive the flattened data.
	 */
	public static void flatten(String attributeName, Object value, Map<String, Object> target) {
		if (value instanceof List) {
			List<?> list = (List<?>)value;
			for (int index = 0; index < list.size(); index++) {
				Object listValue = list.get(index);
				String listAttributeName = attributeName + "_idx" + index;
				flatten(listAttributeName, listValue, target);
			}
		} else if (value instanceof Map) {
			flatten(attributeName + "_", (Map <?, ?>)value, target);
		} else {
			target.put(attributeName, value);
		}
	}
	
	/**
	 * Flatten a Map named containerName -- which may contain ListS, MapS, or scalar values -- to the target Map, but only where predicate is true.
	 * 
	 * @param containerName - The name of the source Map as an attribute in its parent Map. Used to construct new attribute names.
	 * @param source - The Map to be flattened
	 * @param target - The Map to receive the flattened attributes
	 * @param predicate - If false, exclude this attribute. If null, assume true.
	 */
	public static void flatten(String containerName, Map<?, ?> source, Map<String, Object> target, AttributeValuePredicate predicate) {
		source.forEach((name, value) -> {
			if (predicate == null || predicate.isTrue(name, value)) {
				String attributeName = containerName + name.toString();
				flatten(attributeName, value, target);
			}
		});
	}
	
	/**
	 * Flatten a Map named containerName -- which may contain ListS, MapS, or scalar values -- to the target Map.
	 * 
	 * @param containerName - The name of the source Map as an attribute in its parent Map. Used to construct new attribute names.
	 * @param source - The Map to be flattened
	 * @param target - The Map to receive the flattened attributes
	 */
	public static void flatten(String containerName, Map<?, ?> source, Map<String, Object> target) {
		flatten(containerName, source, target, null);
	}

	/**
	 * Flatten a Map -- which may contain ListS, MapS, or scalar values -- to the target Map, but only where predicate is true.
	 * 
	 * @param source - The Map to be flattened
	 * @param target - The Map to receive the flattened attributes
	 * @param predicate - If false, exclude this attribute. If null, assume true.
	 */
	public static void flatten(Map<?, ?> source, Map<String, Object> target, AttributeValuePredicate predicate) {
		flatten("", source, target, predicate);
	}

	/**
	 * Flatten a Map -- which may contain ListS, MapS, or scalar values -- to a target Map.
	 *
	 * @param source - The Map to be flattened
	 */
	public static Map<String, Object> flatten(Map<?, ?> source) {
		Map<String, Object> target = new HashMap<>();
		flatten(source, target, null);
		return target;
	}
}
