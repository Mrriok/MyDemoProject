package com.zony.common.filenet.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentClassVo {
	private String id;
	private String parent;//parent
	private String text;
	private String pId;
	private String symbolicName;
	private String name;//displayName
	private String admini;//是否可以操作
	@JsonProperty("isCreate")
	private boolean isCreate=true;
	@JsonProperty("isSubNood")
	private boolean isSubNood;
	private String icon;
	private List<DocumentClassVo> children = new ArrayList<DocumentClassVo>();
	private List<PropertyDefinitionVo> propTemplateIdList = new ArrayList<PropertyDefinitionVo>();
	public List<PropertyDefinitionVo> getPropTemplateIdList() {
		return propTemplateIdList;
	}

	public void setPropTemplateIdList(List<PropertyDefinitionVo> propTemplateIdList) {
		this.propTemplateIdList = propTemplateIdList;
	}


	public boolean isCreate() {
		return isCreate;
	}

	public void setCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String toString() {
//		return "id:" + id + ";parent:" + parent + ";symbolicName:" + symbolicName + ";text:" + text + ";isSubNood:" + isSubNood
//				+ ";propTemplateIdList Size:" + propTemplateIdList.size();
		return "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	

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

	public String getSymbolicName() {
		return symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	 @JsonIgnore
	public boolean isSubNood() {
		return isSubNood;
	}

	public void setSubNood(boolean isSubNood) {
		this.isSubNood = isSubNood;
	}

	public List<DocumentClassVo> getChildren() {
		return children;
	}

	public void setChildren(List<DocumentClassVo> children) {
		this.children = children;
	}
	
	public String getAdmini() {
		return admini;
	}

	public void setAdmini(String admini) {
		this.admini = admini;
	}
}