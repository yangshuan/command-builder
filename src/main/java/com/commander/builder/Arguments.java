package com.commander.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Arguments {

	private Map<String, Object> _values = new HashMap<String, Object>();
	private List<Object> _singleValues = new ArrayList<Object>();

	void addArgument(Object value) {
		this.addArgument(null, value);
	}

	void addArgument(String key, Object value) {
		if (key == null) {
			_singleValues.add(value);
		} else {
			this._values.put(key, value);
		}
	}

	/**
	 * Get argument value by argument name
	 * @param argument name
	 * @return argument value
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		Object value = this._values.get(name);
		return value == null ? null : (T) value;
	}

	/**
	 * Get argument value that don't has a name
	 * @return iterator of value that don't has a name
	 */
	public Iterator<Object> singleIterator() {
		return Collections.unmodifiableList(_singleValues).iterator();
	}

}
