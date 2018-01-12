
package com.ctosb.core.util;

import java.util.Collection;

import com.ctosb.core.mybatis.page.Page;
import com.ctosb.core.mybatis.page.PageInfo;
import com.ctosb.core.mybatis.page.PageList;

public class PageUtil {

	/**
	 * pageList convert pageInfo
	 * @date 2018/1/12 17:14
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @param pageList
	 * @return
	 */
	public static PageInfo convertPageInfo(PageList pageList) {
		Page page = pageList.getPage();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setData(pageList).setPageNum(page.getPageNum()).setPageSize(page.getPageSize())
				.setTotalPage(page.getTotalPage()).setTotalRecord(page.getTotalRecord());
		return pageInfo;
	}

	/**
	 * pageList convert pageInfo
	 * @date 2018/1/12 17:15
	 * @author liliangang-1163
	 * @since 1.0.0
	 * @param pageList
	 * @param data
	 * @return
	 */
	public static PageInfo convertPageInfo(PageList pageList, Collection data) {
		Page page = pageList.getPage();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setData(data).setPageNum(page.getPageNum()).setPageSize(page.getPageSize())
				.setTotalPage(page.getTotalPage()).setTotalRecord(page.getTotalRecord());
		return pageInfo;
	}
}
