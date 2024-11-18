package com.zony.common.filenet.vo;

public class AttachmentVo extends Vo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int index;

	private String retrievalName;

	private String contentType;

	private Number contentSize;

	public int getIndex() {
		return index;
	}

	public void setIndex(int val) {
		this.index = val;
	}

	public String getRetrievalName() {
		return retrievalName;
	}

	public void setRetrievalName(String val) {
		this.retrievalName = val;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String val) {
		this.contentType = val;
	}

	public Number getContentSize() {
		return contentSize;
	}

	public void setContentSize(Number val) {
		this.contentSize = val;
	}
}
