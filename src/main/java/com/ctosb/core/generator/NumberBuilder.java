
package com.ctosb.core.generator;

/**
 * Sequence Number builder
 * @author liliangang-1163
 * @date 2017年8月7日上午11:28:10
 */
public interface NumberBuilder<T> {

	/**
	 * get next sequence number
	 * @date 2017年8月18日下午3:13:52
	 * @author liliangang
	 * @since 1.0.0 
	 * @return
	 */
	T next();
}
