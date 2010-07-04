/**
 * File Name: QueryConfigResource.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-3<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.query;


import java.util.HashMap;
import java.util.Map;


/**
 * QueryConfigResource
 * 
 * @author ZHUZHU
 * @version 2010-7-3
 * @see QueryConfigResource
 * @since 1.0
 */
public abstract class QueryConfigResource
{
    private static Map<String, QueryItemBean> configMap = new HashMap<String, QueryItemBean>();

    /**
     * @return the configMap
     */
    public static Map<String, QueryItemBean> getConfigMap()
    {
        return configMap;
    }
}
