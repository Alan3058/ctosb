
package com.ctosb.core.mybatis.page;

import java.util.Collection;

/**
 * paging info bean，contains page and search result
 * @author Alan
 */
public class PageInfo<T> extends Page {

	private static final long serialVersionUID = -631603728409248490L;
	/** search result **/
	private Collection<T> data;

	public PageInfo() {
	}

	public Collection<T> getData() {
		return data;
	}

	public PageInfo setData(Collection<T> data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return "PageInfo{" + "data=" + data + "pageNum=" + getPageNum() + ", pageSize=" + getPageSize()
				+ ", totalRecord=" + getTotalRecord() + ", totalPage=" + getTotalPage() + '}';
	}
}
