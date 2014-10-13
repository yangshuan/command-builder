/**
 * Defined a bean which contains all the arguments you want;
 * Then this class will check and build the arguments to the given bean
 * Supported argument type: 
 * 	java.lang.Short,
 * 	java.lang.Integer,
 * 	java.lang.Long,
 * 	java.lang.Float,
 * 	java.lang.Double,
 * 	java.lang.String
 */
package dart.shuayang.tools.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandBuilder {

	private Map<String, Class<?>> _names = Collections.synchronizedMap(new LinkedHashMap<String, Class<?>>());
	private List<Class<?>> _singles = Collections.synchronizedList(new ArrayList<Class<?>>());
	private Map<String, String> _descs = Collections.synchronizedMap(new LinkedHashMap<String, String>());

	private static final String SINGLE_PRE = "__single__";
	private volatile boolean runned = false;
	private static final Object lock = new Object();

	private final boolean strict;

	private CommandBuilder(boolean strict) {
		this.strict = strict;
	}

	/**
	 * @param whether to check undefined name
	 */
	public static CommandBuilder getInstance(boolean strict) {
		return new CommandBuilder(strict);
	}

	/**
	 * Add an argument without name
	 * @param argument type
	 */
	public void addArgument(Class<?> type) {
		this.addArgument(type, null);
	}

	/**
	 * Add an argument with name
	 * @param argument name
	 * @param argument type
	 */
	public void addArgument(String name, Class<?> type) {
		this.addArgument(name, type, null);
	}

	/**
	 * Add an argument without name but has a description 
	 * @param argument type
	 * @param argument description, for usage information
	 */
	public void addArgument(Class<?> type, String description) {
		this.addArgument(null, type, description);
	}

	/**
	 * Add an argument out name and description 
	 * @param argument name
	 * @param argument type
	 * @param argument description, for usage information
	 */
	public void addArgument(String name, Class<?> type, String description) {
		if (description == null) {
			description = "";
		}
		if (!runned) {
			synchronized (lock) {
				if (!runned) {
					if (type != Boolean.class && type != String.class && type != Short.class && type != Integer.class && type != Long.class && type != Float.class && type != Double.class) {
						throw new UnsupportedTypeException(type);
					}

					if (name == null) {
						_singles.add(type);
						_descs.put(SINGLE_PRE + (_singles.size() - 1), description);
					} else {
						_names.put(name, type);
						_descs.put(name, description);
					}
				} else {
					throw new UnsupportedOperationException("Can't add another argument after running.");
				}
			}
		} else {
			throw new UnsupportedOperationException("Can't add another argument after running.");
		}
	}

	/**
	 * Build the argument array to a bean, which can get argument value by name directly
	 * @param input arguments
	 * @return Arguments, get argument value by name
	 */
	public Arguments build(String[] args) {
		synchronized (lock) {
			runned = true;
			return this._build(args);
		}
	}

	/**
	 * @return Usage information, you can show it in command line
	 */
	public String usage() {
		return toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Usage: \n");
		for (String name : _names.keySet()) {
			builder.append("\t-").append(name).append("\t");
			builder.append(_names.get(name).getName());
			builder.append("\t").append(_descs.get(name));
			builder.append("\n");
		}
		for (int i = 0; i < _singles.size(); i++) {
			Class<?> type = _singles.get(i);
			builder.append("\t").append(type.getName());
			builder.append("\t").append(_descs.get(SINGLE_PRE + i));
			builder.append("\n");
		}
		return builder.toString();
	}

	private Arguments _build(String[] args) {
		Arguments arguments = new Arguments();
		int singleIndex = 0;
		for (int i = 0; i < args.length; i++) {
			String name = args[i];
			if (name.startsWith("-")) {
				name = name.substring(1);
				if (_names.get(name) == Boolean.class) {
					arguments.addArgument(name, Boolean.TRUE);
				} else if (_names.containsKey(name) && i < args.length - 1) {
					String value = args[++i];
					Object _value = this.parse(_names.get(name), value);
					arguments.addArgument(name, _value);
				} else {
					if (strict) {
						if (i == args.length - 1) {
							throw new UnsupportedOperationException("Need a value: " + name);
						} else {
							throw new UnsupportedOperationException("Wrong argument: " + name);
						}
					} else {
						continue;
					}
				}
			} else {
				if (singleIndex > _singles.size() - 1) {
					if (strict) {
						throw new UnsupportedOperationException("Wrong argument: " + name);
					} else {
						continue;
					}
				} else {
					Object _value = this.parse(_singles.get(singleIndex++), name);
					arguments.addArgument(_value);
				}
			}
		}
		return arguments;
	}

	private Object parse(Class<?> type, String value) {
		try {
			if (type == Short.class) {
				return Short.parseShort(value);
			} else if (type == Integer.class) {
				return Integer.parseInt(value);
			} else if (type == Long.class) {
				return Long.parseLong(value);
			} else if (type == Float.class) {
				return Float.parseFloat(value);
			} else if (type == Double.class) {
				return Double.parseDouble(value);
			} else if (type == String.class) {
				return value;
			} else if (type == Boolean.class) {
				return Boolean.TRUE;
			} else {
				throw new UnsupportedOperationException(type.getName());
			}
		} catch (NumberFormatException e) {
			throw new UnsupportedOperationException(e.getMessage());
		}
	}

}
