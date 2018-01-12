
package com.ctosb.core.generator;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * snowflakebuilder测试类
 * @date 2018/1/12 17:32
 * @author liliangang-1163
 * @since 1.0.0
 */
public class SnowflakeBuilderTest {

	/**
	 * 测试重复
	 * @date 2018/1/12 17:31
	 * @author liliangang-1163
	 * @since 1.0.0
	 */
	@Test
	public void testRepeat() {
		SnowflakeBuilder idWorker = new SnowflakeBuilder(0, 0);
		Set<Long> set = new HashSet<Long>();
		for (int i = 0; i < 1000; i++) {
			long id = idWorker.next();
			set.add(id);
		}
		assertEquals(set.size(), 1000);
	}
}
