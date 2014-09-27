package com.commander.builder;

public class UnsupportedTypeException extends RuntimeException {

	private static final long serialVersionUID = -3050595786520921590L;

	public UnsupportedTypeException(Class<?> type) {
		super(type.getName());
	}

}
