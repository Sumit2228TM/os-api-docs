package com.krishagni.catissueplus.core.common;

import java.util.Arrays;
import java.util.List;

public class Tuple {
	private Object[] elements;

	private List<Object> elementsList;

	public Tuple(Object ... elements) {
		this.elements = elements;
		this.elementsList = Arrays.asList(elements);
	}

	public <T> T element(int i) {
		if (elements == null || i >= elements.length) {
			throw new IllegalArgumentException("Tuple element index " + i + " out of bounds");
		}

		return (T) elements[i];
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Tuple tuple = (Tuple) o;
		return elementsList.equals(tuple.elementsList);
	}

	@Override
	public int hashCode() {
		return elementsList.hashCode();
	}

	public static Tuple make(Object ... args) {
		return new Tuple(args);
	}
}
