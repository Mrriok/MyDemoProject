package com.zony.common.filenet.os;

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
import com.zony.common.filenet.util.ZonyStringUtil;
import com.zony.common.filenet.vo.RankVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CBR can only be done with <tt>SearchScope.fetchObject</tt>. It does not
 * work with <tt>SearchScope.fetchRows</tt>.
 */
public class Search {

	private static final int DEFAUT_PAGE_SIZE = 100;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ObjectStore os;
	private SearchSQL sql;
	private SearchScope search;
	private PropertyFilter filter;

	private int pageSize;
	private boolean continuable;

	private IndependentObjectSet objects;

	public Search() {
		pageSize = DEFAUT_PAGE_SIZE;
		continuable = true;
	}

	public Search(int pageSize) {
		this.pageSize = pageSize;
		continuable = true;
	}

	public Search(int pageSize, boolean continuable) {
		this.pageSize = pageSize;
		this.continuable = continuable;
	}

	public Search setRowSql(String className, boolean includeSubclasses, String searchExpr) {
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

	public Search setRowSqlWithFullText(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords,
			boolean isFullText) {
		sql = new SearchSQL();
		String alias = "d";
		sql.setSelectList(alias + ".*, cs.Rank");
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (isFullText)
			sql.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch", "cs", "d.This", JoinComparison.EQUAL, "cs.QueriedObject", includeSubclasses);
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
			logger.debug("sql: " + sql.toString());
		}
		return this;
	}

	public Search setObjectSql(String className, boolean includeSubclasses, String whereClause) {
		sql = new SearchSQL();
		String alias = "d";
		sql.setSelectList(alias + ".*");
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (ZonyStringUtil.isNotEmpty(whereClause)) {
			sql.setWhereClause(whereClause);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("sql: " + sql.toString());
		}
		return this;
	}

	public Search setObjectSql(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords) {
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
		if (maxRecords != -1)
			sql.setMaxRecords(maxRecords);
		if (logger.isDebugEnabled()) {
			logger.debug("sql: " + sql.toString());
		}
		return this;
	}

	public Search setObjectSql(String selectClause, String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords) {
		sql = new SearchSQL();
		String alias = "d";
		sql.setSelectList(selectClause);
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
			logger.debug("sql: " + sql.toString());
		}
		return this;
	}

	public Search setObjectSql(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords, boolean isFullText) {
		sql = new SearchSQL();
		String alias = "d";
		sql.setSelectList(alias + ".*");
		sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (isFullText)
			sql.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch", "cs", "d.This", JoinComparison.EQUAL, "cs.QueriedObject", includeSubclasses);
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
			logger.debug("sql: " + sql.toString());
		}
		return this;
	}

	public Search setScope(ObjectStore os) {
		this.os = os;
		search = new SearchScope(os);
		PropertyFilter filter = new PropertyFilter();
		filter.setMaxRecursion(1);
		filter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null));
		return this;
	}

	public Search setPropertyFilter() {
		filter = new PropertyFilter();
		filter.setMaxRecursion(1);
		filter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null));
		return this;
	}

	public IndependentObjectSet fetchIndependentObjectSet() {
		return search.fetchObjects(sql, pageSize, filter, continuable);
	}

	public List<IndependentObject> fetchObjects() {
		IndependentObjectSet objects = search.fetchObjects(sql, pageSize, filter, continuable);
		return EngineCollectionUtils.c(objects, IndependentObject.class);
	}

	public List<IndependentObject> fetchObjects(int pageNum) {
		logger.debug("fetchObjects(), page number=" + pageNum);
		long start = System.currentTimeMillis();
		if (objects == null) {
			objects = search.fetchObjects(sql, pageSize, filter, continuable);
			logger.debug("First Query spent(ms):" + (System.currentTimeMillis() - start));
		}
		logger.debug("begin pageIterator");
		PageIterator pi = objects.pageIterator();
		for (int currentPage = 1; currentPage <= pageNum; currentPage++) {
			logger.debug("currentPage=" + currentPage);
			long pageStart = System.currentTimeMillis();
			pi.nextPage();
			logger.debug("Paging spent(ms):" + (System.currentTimeMillis() - pageStart));
		}
		List independentObjects = Arrays.asList(pi.getCurrentPage());
		logger.debug("Query spent(ms):" + (System.currentTimeMillis() - start));
		logger.debug("getElementCount:" + pi.getElementCount());
		return independentObjects;
	}

	public List<Link> fetchLinks() {
		IndependentObjectSet objects = search.fetchObjects(sql, pageSize, filter, continuable);
		return EngineCollectionUtils.c(objects, Link.class);
	}

	public List<Document> fetchDocuments() {
		IndependentObjectSet objects = search.fetchObjects(sql, pageSize, filter, continuable);
		return EngineCollectionUtils.c(objects, Document.class);
	}

	public List<Folder> fetchFolders() {
		IndependentObjectSet objects = search.fetchObjects(sql, pageSize, filter, continuable);
		return EngineCollectionUtils.c(objects, Folder.class);
	}

	public List<CustomObject> fetchCustomObjects() {
		IndependentObjectSet objects = search.fetchObjects(sql, pageSize, filter, continuable);
		return EngineCollectionUtils.c(objects, CustomObject.class);
	}

	public List<Document> fetchRows() {
		RepositoryRowSet rowSet = search.fetchRows(sql, pageSize, filter, continuable);
		List<Document> documentList = new ArrayList<Document>();
		for (RepositoryRow row : EngineCollectionUtils.c(rowSet, RepositoryRow.class)) {
			Id docId = row.getProperties().get("Id").getIdValue();
			Document document = Factory.Document.fetchInstance(os, docId, null);
			documentList.add(document);
		}
		return documentList;
	}

	public List<RankVo> fetchRowsWithRank() {
		RepositoryRowSet rowSet = search.fetchRows(sql, pageSize, filter, continuable);
		List<RankVo> documentList = new ArrayList<RankVo>();
		RankVo rankVo = null;
		for (RepositoryRow row : EngineCollectionUtils.c(rowSet, RepositoryRow.class)) {
			Id docId = row.getProperties().get("Id").getIdValue();
			Document document = Factory.Document.fetchInstance(os, docId, null);
			double rank = row.getProperties().getFloat64Value("Rank");
			rankVo = new RankVo();
			rankVo.setDocument(document);
			rankVo.setRank(rank);
			documentList.add(rankVo);
		}
		return documentList;
	}
}
