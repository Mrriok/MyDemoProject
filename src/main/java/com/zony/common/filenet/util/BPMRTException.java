package com.zony.common.filenet.util;

/**
 * Thrown to indicate that an error occurs during the P8 workflow operations.
 */
public class BPMRTException extends RuntimeException {
	/**
	 * Constructs a <code>WorkflowExceptino</code> without detail message.
	 */
	public BPMRTException() {
		super();
	}

	/**
	 * Constructs a <code>WorkflowExceptino</code> with the specified detail
	 * message.
	 * 
	 * @param msg
	 *            The detail message.
	 */
	public BPMRTException(String msg) {
		super(msg);
	}

	public BPMRTException(String firstMsg, Object[] otherMsgs) {
		this(firstMsg, toStrings(otherMsgs));
	}

	/**
	 * Constructs a <code>WorkflowException</code> with the given first
	 * message and other messages.
	 * 
	 * @param firstMsg
	 *            The first message.
	 * @param otherMsgs
	 *            The other message.
	 */
	public BPMRTException(String firstMsg, String[] otherMsgs) {
		super(constructMsg(firstMsg, otherMsgs));
	}

	/**
	 * Constructs a <code>WorkflowExceptino</code> with specified nested
	 * <code>Throwable</code>.
	 * 
	 * @param cause
	 *            The exception or error that caused this exception to be
	 *            thrown.
	 */
	public BPMRTException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a <code>WorkflowExceptino</code> with specified detail
	 * message and nested <code>Throwable</code>.
	 * 
	 * @param msg
	 *            The detail message.
	 * @param cause
	 *            The exception or error that caused this exception to be
	 *            thrown.
	 */
	public BPMRTException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/*
	 * Constructs the message.
	 */
	private static String constructMsg(String firstMsg, String[] otherMsgs) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(firstMsg + "\n");
		for (int i = 0; i < otherMsgs.length; i++) {
			strBuf.append(otherMsgs[i] + "\n");
		}
		return strBuf.toString();
	}

	/*
	 * Creates strings from objects using toString.
	 */
	private static String[] toStrings(Object[] objs) {
		int length = objs.length;
		String[] strs = new String[length];
		for (int i = 0; i < length; i++) {
			strs[i] = objs[i].toString();
		}
		return strs;
	}
}
