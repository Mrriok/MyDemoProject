package com.zony.common.filenet.util;

import java.util.Collection;

/**
 * Our own assertion class that is useful for failing fast and for eliminating
 * duplication.
 */
public final class Assert {

	/**
	 * A private constructor to block instantiation.
	 */
	private Assert() {
	}

	/**
	 * Asserts that an object is not <tt>null</tt>.
	 * 
	 * @param o
	 *            An <code>Object</code> to check.
	 * @param message
	 *            Message to display when the assertion fails.
	 * @throws NullPointerException
	 *             If the assertion fails.
	 */
	public static void notNull(Object o, String message) {
		if (null == o) {
			throw new NullPointerException(message);
		}
	}

	/**
	 * Asserts that a <code>String</code> is neither empty (i.e. contains at
	 * least one non-whitespace character) nor <tt>null</tt>.
	 * 
	 * @param s
	 *            A <code>String</code> to check.
	 * @param message
	 *            Message to display when the assertion fails.
	 * @throws IllegalArgumentException
	 *             If <code>s</code> is empty.
	 */
	public static void notEmpty(String s, String message) {
		if (null == s || "".equals(s.trim())) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Asserts that a <code>Collection</code> is neither empty (i.e. contains at
	 * least one element) nor <tt>null</tt>.
	 * 
	 * @param c
	 *            A <code>Collection</code> to check.
	 * @param message
	 *            Message to display when the assertion fails.
	 * @throws IllegalArgumentException
	 *             If <code>c</code> is empty.
	 */
	public static void notEmpty(Collection<?> c, String message) {
		if (null == c || c.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Asserts that a condition is <tt>true</tt>.
	 * 
	 * @param condition
	 *            A condition to check.
	 * @param message
	 *            Message to display when the assertion fails.
	 * @throws IllegalArgumentException
	 *             If the assertion fails.
	 */
	public static void isTrue(boolean condition, String message) {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Asserts that a condition is <tt>true</tt>.
	 * 
	 * @param condition
	 *            A condition to check.
	 * @param message
	 *            Message to display when the assertion fails.
	 * @throws IllegalStateException
	 *             If the assertion fails.
	 */
	public static void validState(boolean condition, String message) {
		if (!condition) {
			throw new IllegalStateException(message);
		}
	}
}
