package com.zony.common.filenet.vo;

import java.util.List;

public class PrivateFolderTree {
private String id;
private String title;
private List<PrivateDocuments> Data;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public List<PrivateDocuments> getData() {
	return Data;
}
public void setData(List<PrivateDocuments> data) {
	Data = data;
}


}
