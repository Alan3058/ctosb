
package com.ctosb.core.mybatis.page;

import java.util.Collection;

/**
 * paging info bean，contains page and search result
 * @author Alan
 */
public class PageInfo extends Page {

	private static final long serialVersionUID = -631603728409248490L;
	/** search result **/
	private Collection data;

	public PageInfo() {
	}

	public Collection getData() {
		return data;
	}

	public PageInfo setData(Collection data) {
		this.data = data;
		return this;
	}
}
