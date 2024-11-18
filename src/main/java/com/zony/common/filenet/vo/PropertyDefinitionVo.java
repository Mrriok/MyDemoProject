package com.zony.common.filenet.vo;

/**
 * @fileName PropertyTemplateVo.java
 * @package com.zony.filenet.vo
 * @function 文档定义中的属性对象
 * @version 1.0.0
 * @date 2014-8-5
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class PropertyDefinitionVo {
	private String symbolicName; // 属性标识名称
	private String name; // 属性名称
	private String displayName;// 属性显示名称
	private int dataType;// 属性数据类型
	private int cardinalityType;// 属性类型，复杂List类型：2 简单Simple类型：0
	private boolean isHidden; // 字段是否隐藏
	private boolean isChoiceList; // 是否是数据字典类型
	private String choiceListName; // 数据字典类型名称
	private boolean valueRequired;// 是否可空，true为可空，否则为非空
	private int length; // 字段长度
	private String defaultValue=""; // 属性字段的默认值
	private Object choiceListValue;//数据字典值
	private int uiType;//1:不显示，2显示可编辑，3显示不可编辑
	private String value=""; // 属性字段的默认值
	private boolean dateType=false; // 是否是时间控件
	
	public String toString() {
		return "symbolicName : " + symbolicName + " ; name : " + name + " ; displayName : " + displayName + " ; dataType : " + dataType
				+ " ; cardinalityType : " + cardinalityType + " ; isHidden : " + isHidden + " ; isChoiceList : " + isChoiceList + " ; choiceListName : "
				+ choiceListName + " ; valueRequired:" + valueRequired + " ; length:" + length + " ; defaultValue:" + defaultValue;
	}
	public int getUiType() {
		return uiType;
	}

	public void setUiType(int uiType) {
		this.uiType = uiType;
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
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

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getCardinalityType() {
		return cardinalityType;
	}

	public void setCardinalityType(int cardinalityType) {
		this.cardinalityType = cardinalityType;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isChoiceList() {
		return isChoiceList;
	}

	public void setChoiceList(boolean isChoiceList) {
		this.isChoiceList = isChoiceList;
	}

	public String getChoiceListName() {
		return choiceListName;
	}

	public void setChoiceListName(String choiceListName) {
		this.choiceListName = choiceListName;
	}

	public boolean getValueRequired() {
		return valueRequired;
	}

	public void setValueRequired(boolean valueRequired) {
		this.valueRequired = valueRequired;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Object getChoiceListValue() {
		return choiceListValue;
	}

	public void setChoiceListValue(Object choiceListValue) {
		this.choiceListValue = choiceListValue;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isDateType() {
		return dateType;
	}
	public void setDateType(boolean dateType) {
		this.dateType = dateType;
	}
	
	
}
