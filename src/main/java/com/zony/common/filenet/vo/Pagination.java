package com.zony.common.filenet.vo;

import com.filenet.api.core.Document;
import com.filenet.apiimpl.query.SearchContext;

import java.util.List;


public class Pagination extends Vo {
	private int currentPageIndex;
	private int pageSize; // 每页显示的记录数
	private int totalNum; // 总记录条数
	private List<Document> items; // 记录集
	private boolean isFirst; // 是否是第一条
	private boolean isLast; // 是否为最后一条
	private int totalPages; // 总页数
	private int pageCount;
	private SearchContext searchContext;// 查询缓存

	/**
	 * 计算分页参数
	 * 
	 * @param items
	 * @param pageSize
	 * @param currentPageIndex
	 * @param totalNum
	 */
	public Pagination(List<Document> items, int pageSize, int currentPageIndex, int totalNum) {
		this.pageSize = pageSize;
		this.currentPageIndex = currentPageIndex;
		this.isFirst = (currentPageIndex == 1);
		this.isLast = ((currentPageIndex * pageSize) >= totalNum);
		this.pageCount = (items == null ? 0 : items.size());
		this.items = items;
		this.totalPages = ((totalNum / pageSize) + (((totalNum % pageSize) == 0) ? 0 : 1));
		this.totalNum = totalNum;
	}

	public Pagination(List<Document> items, int pageSize, int currentPageIndex, boolean isFirst, boolean isLast, int pageCount, int totalPages, int totalNum) {
		this.pageSize = pageSize;
		this.currentPageIndex = currentPageIndex;
		this.isFirst = isFirst;
		this.isLast = isLast;
		this.pageCount = pageCount;
		this.items = items;
		this.totalPages = totalPages;
		this.totalNum = totalNum;
	}

	public Pagination(List<Document> items, int pageSize, int currentPageIndex, boolean isFirst, boolean isLast, int pageCount, int totalPages, int totalNum,
                      SearchContext searchContext) {
		super();
		this.currentPageIndex = currentPageIndex;
		this.pageSize = pageSize;
		this.totalNum = totalNum;
		this.items = items;
		this.isFirst = isFirst;
		this.isLast = isLast;
		this.totalPages = totalPages;
		this.pageCount = pageCount;
		this.searchContext = searchContext;
	}

	public SearchContext getSearchContext() {
		return searchContext;
	}

	public void setSearchContext(SearchContext searchContext) {
		this.searchContext = searchContext;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<Document> getItems() {
		return items;
	}

	public void setItems(List<Document> items) {
		this.items = items;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
}
