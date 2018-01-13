
package com.ctosb.core.util;

import com.ctosb.core.mybatis.page.Page;
import com.ctosb.core.mybatis.page.PageInfo;
import com.ctosb.core.mybatis.page.PageList;

import java.util.Collection;

/**
 * page util
 * @date 2018/1/12 17:14
 * @author alan
 * @since 1.0.0
 */
public class PageUtil {

	/**
	 * pageList convert pageInfo
	 * @date 2018/1/12 17:14
	 * @author alan
	 * @since 1.0.0
	 * @param pageList
	 * @return
	 */
	public static <T> PageInfo<T> convertPageInfo(PageList<T> pageList) {
		Page page = pageList.getPage();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setData(pageList).setPageNum(page.getPageNum()).setPageSize(page.getPageSize())
				.setTotalPage(page.getTotalPage()).setTotalRecord(page.getTotalRecord());
		return pageInfo;
	}

	/**
	 * pageList convert pageInfo
	 * @date 2018/1/12 17:15
	 * @author alan
	 * @since 1.0.0
	 * @param pageList
	 * @param data
	 * @return
	 */
	public static <T> PageInfo<T> convertPageInfo(PageList pageList, Collection<T> data) {
		Page page = pageList.getPage();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setData(data).setPageNum(page.getPageNum()).setPageSize(page.getPageSize())
				.setTotalPage(page.getTotalPage()).setTotalRecord(page.getTotalRecord());
		return pageInfo;
	}
}
