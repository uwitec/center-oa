/*
 * 文件名：Convert.java
 * 版权：Copyright by www.centerchina.com
 * 描述：
 * 修改人：zhuzhu
 * 修改时间：2007-3-3
 *
 */

package com.china.center.jdbc.inter;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * Convert接口(转码)
 * 
 * @author zhuzhu
 * @version 2007-3-3
 * @see Convert
 * @since
 */

public interface Convert
{
    /**
     * Description: 从数据库到程序的解码<br>
     * 
     * @param originStr
     * @return String
     */
    String decode(String originStr);

    /**
     * Description:从程序到数据库的编码 <br>
     * 
     * @param originStr
     * @return String
     */
    String encode(String originStr);

    /**
     * Description: 从List[Map]里面解码<br>
     * 
     * @param list
     * @return List
     */
    List decodeMapInList(List list);

    /**
     * Description:从map里面解码 <br>
     * 
     * @param map
     * @return Map
     */
    Map decodeMap(Map map);

    Object decodeObject(Object obj)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;

    Object encodeObject(Object obj)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;

    List decodeList(List srcList)
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;

}
