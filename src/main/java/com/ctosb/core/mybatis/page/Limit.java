
package com.ctosb.core.mybatis.page;

import java.io.Serializable;

/**
 * limit bean
 * @author Alan
 */
public class Limit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4686942205685362061L;
	/** offset,default value 0 **/
	private int offset;
	/** count,default value 50 **/
	private int count;

	public Limit() {
		this(50);
	}

	public Limit(int count) {
		this(0, count);
	}

	public Limit(int offset, int count) {
		this.offset = offset;
		this.count = count;
	}

	public int getOffset() {
		return offset;
	}

	public Limit setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	public int getCount() {
		return count;
	}

	public Limit setCount(int count) {
		this.count = count;
		return this;
	}
}
