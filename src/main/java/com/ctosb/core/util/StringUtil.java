package com.ctosb.core.util;

public class StringUtil {

	/**
	 * merge string array
	 * 
	 * @param strings
	 * @param split
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:13:51
	 */
	public static String mergeStringArray(String[] strings, String split) {
		if (strings == null) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strings) {
			stringBuffer.append(split + string);
		}
		return stringBuffer.substring(1);
	}
}
