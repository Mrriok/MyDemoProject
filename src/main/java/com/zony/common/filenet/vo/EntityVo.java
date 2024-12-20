package com.zony.common.filenet.vo;

public class EntityVo extends Vo implements Comparable<EntityVo> {

	protected String id;
	protected String versionSeriesId;
	protected String name;
	protected String docSubject;
	protected String className;
	protected String mimeType;
	protected boolean checkedOut;
	protected String path;
	protected String parentId;
	private String rank;

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public int compareTo(EntityVo entityVo) {
		return this.name.compareTo(entityVo.name);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersionSeriesId() {
		return versionSeriesId;
	}

	public void setVersionSeriesId(String versionSeriesId) {
		this.versionSeriesId = versionSeriesId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocSubject() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public boolean isCheckedOut() {
		return checkedOut;
	}

	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFdId() {
		return null;
	}

	public void setFdId(String fdId) {
	}

	public Class<?> getFormClass() {
		return null;
	}

	public void recalculateFields() {
	}
}
