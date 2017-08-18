
package com.ctosb.core.mybatis.sort;

import java.io.Serializable;

/**
 * sort bean
 * @author Alan
 */
public class Sort implements Serializable {

	private static final long serialVersionUID = -1453114764475405942L;
	/** sort field name **/
	private String fieldName;
	/** sort type **/
	private SortType sortType;

	public Sort() {
	}

	public Sort(String fieldName) {
		this(fieldName, SortType.ASC);
	}

	public Sort(String fieldName, SortType sortType) {
		this.fieldName = fieldName;
		this.sortType = sortType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Sort setFieldName(String fieldName) {
		this.fieldName = fieldName;
		return this;
	}

	public SortType getSortType() {
		return sortType;
	}

	public Sort setSortType(SortType sortType) {
		this.sortType = sortType;
		return this;
	}
}
