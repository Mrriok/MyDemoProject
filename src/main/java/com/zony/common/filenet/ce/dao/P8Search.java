package com.zony.common.filenet.ce.dao;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.JoinComparison;
import com.filenet.api.constants.JoinOperator;
import com.filenet.api.core.*;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.zony.common.filenet.util.EngineCollectionUtils;
import com.zony.common.filenet.vo.Pagination;
import com.zony.common.filenet.util.ZonyStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides FileNet Content Engine search related operations
 * 
 * CBR can only be done with <code>SearchScope.fetchObject</code>, which does
 * not work with <code>SearchScope.fetchRows</code>.
 */
public class P8Search {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private ObjectStore os;
	private String userId;
	private SearchSQL sql;
	private SearchScope searchScope;
	private PropertyFilter filter;

	private int pageSize;
	private boolean continuable;
	private int desiredPageIndex;

	public P8Search() {
		pageSize = 100;
		continuable = true;
		desiredPageIndex = 0;
	}

	public boolean isContinuable() {
		return continuable;
	}

	public void setContinuable(boolean continuable) {
		this.continuable = continuable;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ObjectStore getOs() {
		return os;
	}

	public void setOs(ObjectStore os) {
		this.os = os;
	}

	public int getPageSize() {
		return pageSize;
	}

	public P8Search setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public int getDesiredPageIndex() {
		return desiredPageIndex;
	}

	public P8Search setDesiredPageIndex(int desiredPageIndex) {
		this.desiredPageIndex = desiredPageIndex;
		return this;
	}

	/**
	 * Sets row sql
	 * 
	 * @param className
	 * @param includeSubclasses
	 * @param searchExpr
	 * @return this
	 */
	public P8Search setRowSql(String className, boolean includeSubclasses, String searchExpr) {
		sql = new SearchSQL();
		sql.setSelectList("*");
		sql.setFromClauseInitialValue(className, null, includeSubclasses);
		if (ZonyStringUtil.isNotEmpty(searchExpr)) {
			sql.setContainsRestriction(className, searchExpr);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sql: " + sql.toString());
		}
		return this;
	}

	/**
	 * Sets row sql
	 * 
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @param orderByClause
	 * @param maxRecords
	 * @param isFullText
	 * @return this
	 */
	public P8Search setRowSql(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords, boolean isFullText) {
		sql = new SearchSQL();
		String alias = "d";
		
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (isFullText){
			sql.setSelectList(alias + ".*,cs.RANK");
			sql.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch", "cs", "d.This", JoinComparison.EQUAL, "cs.QueriedObject", includeSubclasses);
		}else{
			sql.setSelectList(alias + ".* ");
		}
			
		// String queryString =
		// "SELECT d.* FROM NuclearRecord d INNER JOIN ContentSearch vcs ON d.This = vcs.QueriedObject WHERE CONTAINS(d.*, 'mm')";
		if (ZonyStringUtil.isNotEmpty(whereClause)) {
			sql.setWhereClause(whereClause);
		}
		if (ZonyStringUtil.isNotEmpty(orderByClause)) {
			sql.setOrderByClause(orderByClause);
		}
		// Set Maximum return records
		/*if (maxRecords != -1)
			sql.setMaxRecords(maxRecords);*/
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}

	/**
	 * Sets object sql
	 * 
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @return this
	 */
	public P8Search setObjectSql(String className, boolean includeSubclasses, String whereClause, List<String> selectList) {
		sql = new SearchSQL();

		/*
		 * 设置结果集
		 */
		String alias = "d";
		String selectStr = null;
		if (selectList == null || selectList.size() == 0) {
			selectStr = alias + ".*";
		} else {
			for (String prop : selectList) {
				selectStr = (selectStr == null ? alias + "." + prop : selectStr + "," + alias + "." + prop);
			}
		}
		sql.setSelectList(selectStr);

		sql.setMaxRecords(200);
		// sql.setDistinct();
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (ZonyStringUtil.isNotEmpty(whereClause)) {
			sql.setWhereClause(whereClause);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}

	public P8Search setOrder(String orderByClause) {
		sql.setOrderByClause(orderByClause);
		return this;
	}

	/**
	 * Sets object sql
	 * 
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @param orderByClause
	 * @param maxRecords
	 * @return this
	 */
	public P8Search setObjectSql(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords) {
		sql = new SearchSQL();
		String alias = "d";
		sql.setSelectList(alias + ".*");
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (ZonyStringUtil.isNotEmpty(whereClause)) {
			sql.setWhereClause(whereClause);
		}
		if (ZonyStringUtil.isNotEmpty(orderByClause)) {
			sql.setOrderByClause(orderByClause);
		}
		// Set Maximum return records
		if (maxRecords != -1) {
			sql.setMaxRecords(maxRecords);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}

	/**
	 * Sets object sql
	 * 
	 * @param selectClause
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @param orderByClause
	 * @param maxRecords
	 * @return this
	 */
	public P8Search setObjectSql(String selectClause, String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords) {
		sql = new SearchSQL();
		String alias = "d";
		if (ZonyStringUtil.isEmpty(selectClause)) {
			sql.setSelectList(alias + ".*");
		} else {
			sql.setSelectList(selectClause);
		}
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (ZonyStringUtil.isNotEmpty(whereClause)) {
			sql.setWhereClause(whereClause);
		}
		if (ZonyStringUtil.isNotEmpty(orderByClause)) {
			sql.setOrderByClause(orderByClause);
		}
		// Set Maximum return records
		if (maxRecords != -1)
			sql.setMaxRecords(maxRecords);
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}

	/**
	 * Sets object sql
	 * 
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @param orderByClause
	 * @param maxRecords
	 * @param isFullText
	 * @return this
	 */
	public P8Search setObjectSql(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords, boolean isFullText) {
		sql = new SearchSQL();

		// 配置别名
		String alias = "d";
		sql.setSelectList(alias + ".*");

		// 根据className的数量来决定SQL语句的拼接使用。
		if (className.indexOf(",") > 0) {
			// 如果whereClause不为空，那么加上连接符and
			if (ZonyStringUtil.isNotEmpty(whereClause.replace(" ", "")) && whereClause.replace(" ", "").length() > 0) {
				whereClause = whereClause + " and ";
			}
			whereClause = whereClause + " ( ";
			String[] subDocClasses = className.split(",");
			for (int i = 0; i < subDocClasses.length; i++) {
				whereClause = whereClause + "ISCLASS( " + alias + "," + subDocClasses[i] + " ) ";

				if (i != (subDocClasses.length - 1)) {
					whereClause = whereClause + " or ";
				}

			}
			whereClause = whereClause + " ) ";

			className = "NuclearRecord";
		}
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (isFullText) {
			sql.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch", "cs", "d.This", JoinComparison.EQUAL, "cs.QueriedObject", includeSubclasses);
			sql.setWhereClause(" CONTAINS(d.*, 'com')");
		}
		if (ZonyStringUtil.isNotEmpty(whereClause) || whereClause.length() > 0) {
			sql.setWhereClause(whereClause);
		}
		if (ZonyStringUtil.isNotEmpty(orderByClause)) {
			sql.setOrderByClause(orderByClause);
		}
		// Set Maximum return records
		if (maxRecords != -1)
			sql.setMaxRecords(maxRecords);
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}

	/** 
	 * @Title: setObjectSqlForFullTextSearch 
	 * @Description: 分页检索
	 * @Version 1.0
	 * @Date 2016年10月14日
	 * @Author YYF
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @param orderByClause
	 * @param maxRecords
	 * @param searchTile
	 * @param isFullText
	 * @return 
	*/
	public P8Search setObjectSqlForFullTextSearch(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords,
			String searchTile,boolean isFullText) {
		// sql = new
		// SearchSQL("SELECT d.this,d, cs.ContentSummary  FROM NuclearRecord AS d WITH INCLUDESUBCLASSES INNER JOIN ContentSearch AS cs WITH INCLUDESUBCLASSES ON d.This = cs.QueriedObject WHERE  CONTAINS(*, 'com')");
		sql = new SearchSQL();
		String alias = "d";
		//sql.setSelectList(alias + ".this, d.Id, cs.*");
		//sql.setSelectList(alias +".*");
		if(ZonyStringUtil.isEmpty(searchTile)){
			sql.setSelectList(alias+".*");	
		}else{
			sql.setSelectList(alias +".*,cs.QueriedObject, cs.ContentSummary");			
		}
		
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		
		if(isFullText){
			sql.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch", "cs", "d.This", JoinComparison.EQUAL, "cs.QueriedObject", includeSubclasses);
			//sql.setWhereClause(" CONTAINS(*, '" + searchTile + "') and isCurrentVersion=true");
			// 当全文检索时不需要再附加一些选择性的属性。
			 if (ZonyStringUtil.isNotEmpty(whereClause)) { 
					 whereClause =" CONTAINS("+alias+".*, '" + searchTile + "') and (" + whereClause + ")";
					 sql.setWhereClause(whereClause); 
				 }else if(ZonyStringUtil.isEmpty(searchTile)){
					 sql.setWhereClause(" CONTAINS("+alias+".*, '" + searchTile + "')"
				); 
			 }
		}else{
			 sql.setWhereClause(whereClause); 
		}

		
		if (ZonyStringUtil.isNotEmpty(orderByClause)) {
			sql.setOrderByClause(orderByClause);
		}
		// Set Maximum return records
		if (maxRecords != -1)
			sql.setMaxRecords(maxRecords);
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}

	/**
	 * Sets object sql
	 * 
	 * @param distinct
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @param orderByClause
	 * @return this
	 */
	public P8Search setObjectSql(boolean distinct, String className, boolean includeSubclasses, String whereClause, String orderByClause) {
		sql = new SearchSQL();
		if (distinct) {
			sql.setDistinct();
		}
		String alias = "d";
		sql.setSelectList(alias + ".*");
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (ZonyStringUtil.isNotEmpty(whereClause)) {
			sql.setWhereClause(whereClause);
		}
		if (ZonyStringUtil.isNotEmpty(orderByClause)) {
			sql.setOrderByClause(orderByClause);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}

	/**
	 * Sets object sql
	 * 
	 * @param distinct
	 * @param selectClause
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @param orderByClause
	 * @return this
	 */
	public P8Search setObjectSql(boolean distinct, String selectClause, String className, boolean includeSubclasses, String whereClause, String orderByClause) {
		sql = new SearchSQL();
		if (distinct) {
			sql.setDistinct();
		}
		String alias = "d";
		if (ZonyStringUtil.isEmpty(selectClause)) {
			sql.setSelectList(alias + ".*");
		} else {
			sql.setSelectList(selectClause);
		}
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (ZonyStringUtil.isNotEmpty(whereClause)) {
			sql.setWhereClause(whereClause);
		}
		if (ZonyStringUtil.isNotEmpty(orderByClause)) {
			sql.setOrderByClause(orderByClause);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}

	/**
	 * Sets object sql
	 * 
	 * @param distinct
	 * @param selectClause
	 * @param className
	 * @param includeSubclasses
	 * @param whereClause
	 * @param orderByClause
	 * @param maxRecords
	 * @return this
	 */
	public P8Search setObjectSql(boolean distinct, String selectClause, String className, boolean includeSubclasses, String whereClause, String orderByClause,
			int maxRecords) {
		sql = new SearchSQL();
		if (distinct) {
			sql.setDistinct();
		}
		String alias = "d";
		if (ZonyStringUtil.isEmpty(selectClause)) {
			sql.setSelectList(alias + ".*");
		} else {
			sql.setSelectList(selectClause);
		}
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (ZonyStringUtil.isNotEmpty(whereClause)) {
			sql.setWhereClause(whereClause);
		}
		if (ZonyStringUtil.isNotEmpty(orderByClause)) {
			sql.setOrderByClause(orderByClause);
		}
		// Set Maximum return records
		if (maxRecords != -1)
			sql.setMaxRecords(maxRecords);
		if (logger.isDebugEnabled()) {
			logger.debug("Search SQL: " + sql.toString());
		}
		return this;
	}
	
	public Pagination fetchDocumentsWithPagination() {
		if (desiredPageIndex <= 0) {
			return null;
		}
		
		long sTime1 = System.currentTimeMillis();
		IndependentObjectSet objects = searchScope.fetchObjects(sql, pageSize, filter, continuable);

		PageIterator pageIter = objects.pageIterator();
		pageIter.setPageSize(pageSize);
		System.out.println("查询Document耗时:" + (System.currentTimeMillis() - sTime1) + "ms");
		List<Document> docList = new ArrayList<Document>();
		boolean isLast = false;
		boolean isFirst = false;
		int currentPageIndex = 1;
		int totalPages = 0;
		int totalNum = 0;
		long sTime2 = System.currentTimeMillis();
		while (true) {
			
			if (!pageIter.nextPage())
				break;
			if (currentPageIndex == desiredPageIndex) {

				Object[] docs = pageIter.getCurrentPage();

				for (Object d : docs) {
					Document document = (Document) d;
				
					docList.add(document);
				}
			}
			currentPageIndex++;
			totalPages++;
			totalNum += pageIter.getCurrentPage().length;
			
		}
		System.out.println("获取第" + currentPageIndex + "页耗时:" + (System.currentTimeMillis() - sTime2) + "ms");
		if (desiredPageIndex > totalPages) {
			return null;
		}
		if (desiredPageIndex == 1) {
			isFirst = true;
		}
		if (desiredPageIndex == totalPages) {
			isLast = true;
		}
		return new Pagination(docList, pageSize, desiredPageIndex, isFirst, isLast, docList.size(), totalPages, totalNum, null);
	}
	
	
	/**
	 * Sets search scope
	 * 
	 * @param os
	 * @return this
	 */
	public P8Search setScope(ObjectStore os) {
		this.os = os;
		searchScope = new SearchScope(os);
		return this;
	}

	/**
	 * Sets property filter
	 * 
	 * @return this
	 */
	public P8Search setPropertyFilter() {
		PropertyFilter filter = new PropertyFilter();
		filter.setMaxRecursion(1);
		filter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null));
		return this;
	}

	/**
	 * Sets property filter
	 * 
	 * @param filter
	 * @return this
	 */
	public P8Search setPropertyFilter(PropertyFilter filter) {
		this.filter = filter;
		return this;
	}

	/**
	 * Fetches <code>IndependentObjectSet</code>
	 * 
	 * @return an <code>IndependentObjectSet</code> instance
	 */
	public IndependentObjectSet fetchIndependentObjectSet() {
		return searchScope.fetchObjects(sql, pageSize, filter, continuable);
	}

	/**
	 * Fetches a list of <code>IndependentObject</code>s
	 * 
	 * @return a list of <code>IndependentObject</code>s
	 */
	public List<IndependentObject> fetchObjects() {
		IndependentObjectSet objects = searchScope.fetchObjects(sql, pageSize, filter, continuable);
		List<IndependentObject> independentObjects = new ArrayList<IndependentObject>();
		for (IndependentObject doc : EngineCollectionUtils.c(objects, IndependentObject.class)) {
			independentObjects.add(doc);
		}
		return independentObjects;
	}

	/**
	 * Fetches a list of <code>CustomObject</code>s
	 * 
	 * @return a list of <code>CustomObject</code>s
	 */
	public List<CustomObject> fetchCustomObjects() {
		IndependentObjectSet objects = searchScope.fetchObjects(sql, pageSize, filter, continuable);
		List<CustomObject> customObjects = new ArrayList<CustomObject>();
		for (CustomObject co : EngineCollectionUtils.c(objects, CustomObject.class)) {
			customObjects.add(co);
		}
		return customObjects;
	}

	/**
	 * Fetches a list of <code>Document</code>s
	 * 
	 * @return a list of <code>Document</code>s
	 */
	public List<Document> fetchDocuments() {
		IndependentObjectSet objects = searchScope.fetchObjects(sql, pageSize, filter, continuable);
		List<Document> documents = new ArrayList<Document>();
		for (Document doc : EngineCollectionUtils.c(objects, Document.class)) {
			documents.add(doc);
		}
		return documents;
	}

	/**
	 * Fetches a list of <code>Folder</code>s
	 * 
	 * @return a list of <code>Folder</code>s
	 */
	public List<Folder> fetchFolders() {
		IndependentObjectSet objects = searchScope.fetchObjects(sql, pageSize, filter, continuable);
		List<Folder> folders = new ArrayList<Folder>();
		for (Folder folder : EngineCollectionUtils.c(objects, Folder.class)) {
			folders.add(folder);
		}
		return folders;
	}

	/**
	 * Fetches a list of <code>Link</code>s
	 * 
	 * @return a list of <code>Link</code>s
	 */
	public List<Link> fetchLinks() {
		IndependentObjectSet objects = searchScope.fetchObjects(sql, pageSize, filter, continuable);
		List<Link> links = new ArrayList<Link>();
		for (Link doc : EngineCollectionUtils.c(objects, Link.class)) {
			links.add(doc);
		}
		return links;
	}

	/**
	 * Fetches a list of <code>Document</code>s
	 * 
	 * @return a list of <code>Document</code>s
	 */
	public List<Document> fetchRows() {
		RepositoryRowSet rowSet = searchScope.fetchRows(sql, pageSize, filter, continuable);
		List<Document> docList = new ArrayList<Document>();
		for (RepositoryRow row : EngineCollectionUtils.c(rowSet, RepositoryRow.class)) {
			Id docId = row.getProperties().get("Id").getIdValue();
			Document doc = Factory.Document.fetchInstance(os, docId, null);
			docList.add(doc);
		}
		return docList;
	}
	
	
}
