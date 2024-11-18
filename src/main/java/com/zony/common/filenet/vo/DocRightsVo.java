package com.zony.common.filenet.vo;
/**
 * @Function 文档权限对象
 * @Version 1.0.0.0
 * @Date 2016年10月21日
 * @Author WXZ
 * @Copyright (C) 2016, ShangHai ZonySoft Co .,Ltd  All Rights Reserved.
 */
public class DocRightsVo {
	
	private String userId;//权限所有者用户编号
	private String viewRights;//是否有查看权限
	private String downloadRights;//是否有下载权限
	private String editRights;//是否有编辑权限
	private String printRights;//是否有打印权限
	private String endTime = "forever";//权限失效时间(永远不失效)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getViewRights() {
		return viewRights;
	}
	public void setViewRights(String viewRights) {
		this.viewRights = viewRights;
	}
	public String getDownloadRights() {
		return downloadRights;
	}
	public void setDownloadRights(String downloadRights) {
		this.downloadRights = downloadRights;
	}
	public String getEditRights() {
		return editRights;
	}
	public void setEditRights(String editRights) {
		this.editRights = editRights;
	}
	public String getPrintRights() {
		return printRights;
	}
	public void setPrintRights(String printRights) {
		this.printRights = printRights;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
