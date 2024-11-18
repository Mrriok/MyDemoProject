package com.zony.common.filenet.vo;

import java.util.ArrayList;
import java.util.List;


/**
 * @Function 文档详细信息
 * @Version 1.0.0.0
 * @Date 2016-3-1
 * @Author chenjun
 * @Copyright (C) 2016, ShangHai ZonySoft Co .,Ltd  All Rights Reserved.
 */
public class DocInfoVo implements Comparable<DocInfoVo>{
	private String id;//文档ID
	private String creator;//创建者
	private String creatorName;//创建者姓名
	
	private String name;//文档名称
	private String majorVersionNumber;//主 版本号
	private String className;//文档类型名称
	private String classNameCN;//文档类型中文名称 前台显示值
	private String docNumber;//文件编码
	private String docType;//文档类型（word,excel）
	private String docSuffix;
	private String lockStatus="false";//锁定状态
	private String lockPeople;
   
	private String fileName;//文件名称
    private String docStatus="";//文档状态
    private String	toView;
	private String	downLoad;
	private String	editor;
	private String	print;
    private String docSize;//文档大小
    private String differDocStatus;//用于区分临时或发布
    private String version;//版本号 
    private String contentSummary ;//摘要
    private String dateCreated;//文档创建时间
    private String docAdmin;//文档管理员  1 有权限 0无权限
	private String centerType;//中心级分类
	private String creatDeptName;//部门级分类
	private String datePublish="";//发布时间

	public String getDatePublish() {
		return datePublish;
	}
	public void setDatePublish(String datePublish) {
		this.datePublish = datePublish;
	}
	public String getContentSummary() {
		return contentSummary;
	}
	public String getCenterType() {
		return centerType;
	}
	public void setCenterType(String centerType) {
		this.centerType = centerType;
	}
	public String getCreatDeptName() {
		return creatDeptName;
	}
	public void setCreatDeptName(String creatDeptName) {
		this.creatDeptName = creatDeptName;
	}
	public void setContentSummary(String contentSummary) {
		this.contentSummary = contentSummary;
	}
	
	
	
	public String getDocAdmin() {
		return docAdmin;
	}
	public void setDocAdmin(String docAdmin) {
		this.docAdmin = docAdmin;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLockPeople() {
		return lockPeople;
	}
	public void setLockPeople(String lockPeople) {
		this.lockPeople = lockPeople;
	}
	public String getDocSuffix() {
		return docSuffix;
	}
	public void setDocSuffix(String docSuffix) {
		this.docSuffix = docSuffix;
	}
	public String getDifferDocStatus() {
		return differDocStatus;
	}
	public void setDifferDocStatus(String differDocStatus) {
		this.differDocStatus = differDocStatus;
	}
	public String getDocSize() {
		return docSize;
	}
	public void setDocSize(String docSize) {
		this.docSize = docSize;
	}
	public String getToView() {
		return toView;
	}
	public void setToView(String toView) {
		this.toView = toView;
	}
	public String getDownLoad() {
		return downLoad;
	}
	public void setDownLoad(String downLoad) {
		this.downLoad = downLoad;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getPrint() {
		return print;
	}
	public void setPrint(String print) {
		this.print = print;
	}
	//文件扩展属性信息
	private List<DocExVo> extendList=new ArrayList<DocExVo>();
	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public List<DocExVo> getExtendList() {
		return extendList;
	}
	public void setExtendList(List<DocExVo> extendList) {
		this.extendList = extendList;
	}
	public String getClassNameCN() {
		return classNameCN;
	}
	public void setClassNameCN(String classNameCN) {
		this.classNameCN = classNameCN;
	}
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
	
	public String getMajorVersionNumber() {
		return majorVersionNumber;
	}
	public void setMajorVersionNumber(String majorVersionNumber) {
		this.majorVersionNumber = majorVersionNumber;
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
	
	@Override
	public int compareTo(DocInfoVo o) {		 
	      return o.dateCreated.compareTo(this.dateCreated); 	       
	}
	public String getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDocStatus() {
		return docStatus;
	}
	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}
	
	
}
