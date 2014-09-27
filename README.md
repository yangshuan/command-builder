command-builder [![Build Status](https://travis-ci.org/yangshuan/command-builder.svg?branch=develop)](https://travis-ci.org/yangshuan/command-builder)
===============

This is a Java library to help you to build your command arguments.

## Usage

``` java
public static void main(String[] args) {
  String[] args = { "-key1", "val1", "-key2", "10", "-key3", "5.5" };
	CommandBuilder builder = CommandBuilder.getInstance(false);
	builder.addArgument("key1", String.class, "it is key1");
	builder.addArgument("key2", Integer.class, "it is key2");
	builder.addArgument("key3", Double.class, "it is key3");
	Arguments arguments = builder.build(args);
	assertEquals("val1", (String) arguments.get("key1"));
	assertEquals(new Integer(10), (Integer) arguments.get("key2"));
	assertEquals(new Double(5.5), (Double) arguments.get("key3"));
}
```


## LICENSE
Apache 2

If you want to contribute to this library, please fork, update and send a pull request.
Thank you very much.
