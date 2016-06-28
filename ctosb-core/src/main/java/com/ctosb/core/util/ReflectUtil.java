package com.ctosb.core.util;

import java.lang.reflect.Field;


/**
 * tool class of reflected operation
 * 
 * @author Alan
 *
 */
public class ReflectUtil {
    /**
     * get the specified field value of specified object
     * 
     * @param target
     * @param fieldName
     * @return
     * @author Alan
     * @createTime 2015年12月15日 下午10:04:29
     */
    public static Object getFieldValue(Object target, String fieldName) {
        Object result = null;
        Field field = ReflectUtil.getField(target, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(target);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * set the specified field value of specified object
     * 
     * @param target
     * @param fieldName
     * @param fieldValue
     * @author Alan
     * @createTime 2015年12月15日 下午10:05:18
     */
    public static void setFieldValue(Object target, String fieldName, String fieldValue) {
        Field field = ReflectUtil.getField(target, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(target, fieldValue);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get specified field object of specified object
     * 
     * @param target
     * @param fieldName
     * @return
     * @author Alan
     * @createTime 2015年12月15日 下午10:04:56
     */
    private static Field getField(Object target, String fieldName) {
        Field field = null;
        for (Class<?> clazz = target.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // if have not the field,then continue searched from its parent class
            }
        }
        return field;
    }
}
