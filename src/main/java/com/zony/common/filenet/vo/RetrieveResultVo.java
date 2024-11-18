package com.zony.common.filenet.vo;

import java.util.List;

public class RetrieveResultVo extends Vo {
	private List<?> docsList;
	private Pagination paginationVo;

	public List<?> getDocsList() {
		return docsList;
	}

	public void setDocsList(List<?> docsList) {
		this.docsList = docsList;
	}

	public Pagination getPaginationVo() {
		return paginationVo;
	}

	public void setPaginationVo(Pagination paginationVo) {
		this.paginationVo = paginationVo;
	}
}
