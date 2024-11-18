package com.zony.common.filenet.vo;

public class PermissionsVo extends Vo {

	private String title;
	private boolean allControl;
	private boolean upGradeVersion;
	private boolean updatePath;
	private boolean updateProperties;
	private boolean viewPath;
	private boolean viewProperties;
	private boolean publish;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAllControl() {
		return allControl;
	}

	public void setAllControl(boolean allControl) {
		this.allControl = allControl;
	}

	public boolean isUpGradeVersion() {
		return upGradeVersion;
	}

	public void setUpGradeVersion(boolean upGradeVersion) {
		this.upGradeVersion = upGradeVersion;
	}

	public boolean isUpdatePath() {
		return updatePath;
	}

	public void setUpdatePath(boolean updatePath) {
		this.updatePath = updatePath;
	}

	public boolean isUpdateProperties() {
		return updateProperties;
	}

	public void setUpdateProperties(boolean updateProperties) {
		this.updateProperties = updateProperties;
	}

	public boolean isViewPath() {
		return viewPath;
	}

	public void setViewPath(boolean viewPath) {
		this.viewPath = viewPath;
	}

	public boolean isViewProperties() {
		return viewProperties;
	}

	public void setViewProperties(boolean viewProperties) {
		this.viewProperties = viewProperties;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

}
