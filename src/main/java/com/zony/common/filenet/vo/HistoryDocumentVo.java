package com.zony.common.filenet.vo;

public class HistoryDocumentVo {

	private String documentTitle;
	private String versionStatus;
	private String majorVersionNumber;
	private String minorVersionNumber;
	private String lastModifier;
	private String mimeType;
	private String dateLastModified;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public String getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(String versionStatus) {
		this.versionStatus = versionStatus;
	}

	public String getMajorVersionNumber() {
		return majorVersionNumber;
	}

	public void setMajorVersionNumber(String majorVersionNumber) {
		this.majorVersionNumber = majorVersionNumber;
	}

	public String getMinorVersionNumber() {
		return minorVersionNumber;
	}

	public void setMinorVersionNumber(String minorVersionNumber) {
		this.minorVersionNumber = minorVersionNumber;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public String getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(String dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
