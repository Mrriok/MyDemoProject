package com.zony.common.filenet.vo;

/**
 * @fileName ChoiseListVo.java
 * @package com.zony.filenet.vo
 * @function 保存CE中ChoiseList对象数据
 * @version 1.0.0
 * @date 2014年8月28日
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class ChoiceListVo {
	private String id;
	private String parentId;
	private String name;
	private String displayName;
	private String itemType;// 1 部门 2 人 3 角色
	private Object value;
	private int dataType;
	private String emailAddress;
	private String userName;
	
	//修改，因前端无法设置子父节点 _by Lity
	private String parent;//父节点  根节点为#
	private String pId;//新父节点  根节点为#
	private String text;//显示字段
	private String type;//String还是Integer类型
	private String userTification;
	
	
	



	@Override
	public String toString() {
		return "ChoiceListVo [id=" + id + ", parentId=" + parentId + ", name=" + name + ", displayName=" + displayName
				+ ", itemType=" + itemType + ", value=" + value + ", dataType=" + dataType + ", parent=" + parent
				+ ", text=" + text + "]";
	}


	
	public String getUserTification() {
		return userTification;
	}



	public void setUserTification(String userTification) {
		this.userTification = userTification;
	}



	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
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
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getpId() {
		return pId;
	}


	public void setpId(String pId) {
		this.pId = pId;
	}
	
	
}
