package com.ctosb.core.mybatis;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

/**
 * paging bean
 * 
 * @author Alan
 *
 */
public class Page implements Serializable {

	private static final long serialVersionUID = -6153586095524896492L;
	private int pageNum = 1; // current page number,default value 1
	private int pageSize = 50; // the row max count of percent page,default
								// value 50
	private int totalRecord = 0; // the quantity of total record
	private int totalPage = 0; // the quantity of total page

	public Page() {

	}

	public Page(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public Page setPageNum(int pageNum) {
		this.pageNum = pageNum;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Page setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public Page setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		return this;
	}

	public int getTotalPage() {
		if (totalPage == 0) {
			return (int) Math.ceil((totalRecord * 1.0) / pageSize);
		}
		return totalPage;
	}

	public Page setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		return this;
	}

}
