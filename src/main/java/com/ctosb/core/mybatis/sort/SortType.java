
package com.ctosb.core.mybatis.sort;

/**
 * sort type asc and desc
 * @author liliangang
 */
public enum SortType {
	/** 升序 */
	ASC("ASC"),
	/** 降序 */
	DESC("DESC");

	private String value;

	private SortType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "SortType{" + "value='" + value + '\'' + '}';
	}
}
