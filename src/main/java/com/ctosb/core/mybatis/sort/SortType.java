package com.ctosb.core.mybatis.sort;

/**
 * sort type 
 * asc  and  desc
 * @author liliangang
 *
 */
public enum SortType {

	ASC("ASC"), DESC("DESC");
	
	private String value;

	private SortType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
}
