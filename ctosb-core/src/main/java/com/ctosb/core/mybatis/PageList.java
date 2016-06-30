package com.ctosb.core.mybatis;

import java.util.ArrayList;
import java.util.Collection;

/**
 * paging result set
 *
 * @param <E>
 * @author Alan
 * @date 2016年6月30日 上午10:00:35
 */
public class PageList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9117684513797366572L;

	/**
	 * paging instance
	 */
	private Page page;

	public PageList() {
		super();
	}

	public PageList(Collection<E> collection) {
		super(collection);
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
