package com.zony.common.filenet.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Function 文档基础信息，用于在文档列表中展示文档信息
 * @Version 1.0.0.0
 * @Date 2016-9-1
 * @Author TanXD
 * @Copyright (C) 2016, ShangHai ZonySoft Co .,Ltd  All Rights Reserved.
 */
public class BdDocBaseInfoVo {
	public String getAttachementtype() {
		return attachementtype;
	}
	public void setAttachementtype(String attachementtype) {
		this.attachementtype = attachementtype;
	}
	private String id;//文档ID
	private String creator;//创建者
	private String creatorName;//创建者姓名
	private String name;//文档名称
	private String majorVersionNumber;//主 版本号
	private String docType;//文档类型(word,excel)
	private String lockStatus="false";//锁定状态
	private String fileName;
	private String attachementtype;//附件类型
	//文件扩展属性信息
		private List<DocExVo> extendList=new ArrayList<DocExVo>();
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getMajorVersionNumber() {
		return majorVersionNumber;
	}
	public void setMajorVersionNumber(String majorVersionNumber) {
		this.majorVersionNumber = majorVersionNumber;
	}
	private String className;//文档类型名称
	private String classNameCN;//文档类型中文名称 前台显示值
	private String docNumber;//文件编码
	public String getClassNameCN() {
		return classNameCN;
	}
	public void setClassNameCN(String classNameCN) {
		this.classNameCN = classNameCN;
	}
	private String dateCreated;//文档创建时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	public List<DocExVo> getExtendList() {
		return extendList;
	}
	public void setExtendList(List<DocExVo> extendList) {
		this.extendList = extendList;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

	
}
