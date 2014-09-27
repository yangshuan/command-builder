package com.commander.builder;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class CommandBuilderTest {

	@Test
	public void testUnStrictWithNamesNoArgs() {
		String[] args = { "-key1", "val1", "-key2", "10", "-key3", "5.5" };
		CommandBuilder builder = CommandBuilder.getInstance(false);
		Arguments arguments = builder.build(args);
		assertEquals(null, (String) arguments.get("key1"));
	}

	@Test
	public void testStrictWithNamesNoArgs() {
		String[] args = { "-key1", "val1", "-key2", "10", "-key3", "5.5" };
		CommandBuilder builder = CommandBuilder.getInstance(true);
		boolean exception = false;
		try {
			builder.build(args);
		} catch (Exception e) {
			exception = true;
			assertEquals("Wrong argument: key1", e.getMessage());
		}
		assertTrue("Should exception", exception);
	}

	@Test
	public void testUnStrictWithNamesAndArgs() {
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

	@Test
	public void testStrictWithNamesAndArgs() {
		String[] args = { "-key1", "val1", "-key2", "10", "-key3", "5.5" };
		CommandBuilder builder = CommandBuilder.getInstance(true);
		builder.addArgument("key1", String.class, "it is key1");
		builder.addArgument("key2", Integer.class, "it is key2");
		boolean exception = false;
		try {
			builder.build(args);
		} catch (Exception e) {
			exception = true;
			assertEquals("Wrong argument: key3", e.getMessage());
		}
		assertTrue("Should exception", exception);
	}

	@Test
	public void testUnStrictWithoutNamesNoArgs() {
		String[] args = { "single1", "2" };

		CommandBuilder builder = CommandBuilder.getInstance(false);
		Arguments arguments = builder.build(args);

		Iterator<Object> it = arguments.singleIterator();
		assertFalse("Should has no value", it.hasNext());
	}

	@Test
	public void testStrictWithoutNamesNoArgs() {
		String[] args = { "single1", "2" };

		CommandBuilder builder = CommandBuilder.getInstance(true);
		boolean exception = false;
		try {
			builder.build(args);
		} catch (Exception e) {
			exception = true;
			assertEquals("Wrong argument: single1", e.getMessage());
		}
		assertTrue("Should exception", exception);
	}

	@Test
	public void testUnStrictWithoutNamesAndArgs() {
		String[] args = { "single1", "2" };

		CommandBuilder builder = CommandBuilder.getInstance(false);
		builder.addArgument(String.class);
		builder.addArgument(Integer.class);
		Arguments arguments = builder.build(args);

		Iterator<Object> it = arguments.singleIterator();
		Object single1 = it.next();
		Object single2 = it.next();
		assertEquals("single1", single1);
		assertEquals(new Integer(2), single2);
	}

	@Test
	public void testStrictWithoutNamesAndArgs() {
		String[] args = { "single1", "2" };

		CommandBuilder builder = CommandBuilder.getInstance(true);
		builder.addArgument(String.class);
		boolean exception = false;
		try {
			builder.build(args);
		} catch (Exception e) {
			exception = true;
			assertEquals("Wrong argument: 2", e.getMessage());
		}
		assertTrue("Should exception", exception);
	}

	@Test
	public void testUsage() {
		// -key1 val1 -key2 10 -key3 5.5
		CommandBuilder builder = CommandBuilder.getInstance(true);
		builder.addArgument("key1", String.class, "it is key1");
		builder.addArgument("key2", Integer.class);
		builder.addArgument(Double.class);
		StringBuilder sb = new StringBuilder("Usage: \n");
		sb.append("\t-key1 : java.lang.String\tit is key1\n");
		sb.append("\t-key2 : java.lang.Integer\t\n");
		sb.append("\tjava.lang.Double\t\n");
		assertEquals(sb.toString(), builder.usage());
	}

}
