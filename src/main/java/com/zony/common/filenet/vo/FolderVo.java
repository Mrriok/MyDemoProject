package com.zony.common.filenet.vo;

import java.util.Date;


/**
 * @fileName FolderVo.java
 * @package com.zony.filenet.vo
 * @function 文件夹实体类
 * @version 1.0.0
 * @date 2014年8月12日
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class FolderVo implements Comparable<FolderVo>{
	//FolderVo 对应FileNet中自定义基础文件夹（ZonyFolder）
	private String id; // 文件夹ID标识
	private String parent;// 父文件夹ID标识
	private String pId;// 父文件夹ID标识
	private String symbolicName;// 文件夹的class标识名称
	private String text;// 文件夹显示名称
	private String name;// 文件夹显示名称
	private Date createDate;
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String folderPath;// 文件夹全路径
	private String folderRule;//文件夹规则   ,扩展字段，对应FSYS_Rule
	private String folderOrder;//扩展字段，对应FSYS_Order

	public String getFolderOrder() {
		return folderOrder;
	}

	public void setFolderOrder(String folderOrder) {
		this.folderOrder = folderOrder;
	}

	public String getFolderRule() {
		return folderRule;
	}

	public void setFolderRule(String folderRule) {
		this.folderRule = folderRule;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public int compareTo(FolderVo o) {
			int number=0;
			if(o.getCreateDate().getTime()>this.getCreateDate().getTime()){
				number = -1;
			}
		
		
			if(o.getCreateDate().getTime()<this.getCreateDate().getTime()){
				number = 1;
			}
		
		return number;
	}

}
