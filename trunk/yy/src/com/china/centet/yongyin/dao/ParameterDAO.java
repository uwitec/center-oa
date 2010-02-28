/*
 * File Name: ParameterDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-16
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.tools.MathTools;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
public class ParameterDAO
{
    private JdbcOperation jdbcOperation = null;

    private Map<String, String> parMap = new HashMap<String, String>();

    /**
     * default constructor
     */
    public ParameterDAO()
    {}

    public void init()
    {
        List<Map> list = jdbcOperation.queryForList("select * from t_center_sysconfig");

        for (Map map : list)
        {
            parMap.put(map.get("CONFIG").toString(), map.get("VALUE").toString());
        }

        // System.out.println("加载系统配置参数...");
    }

    public boolean getBoolean(String key)
    {
        String value = parMap.get(key);

        if ("true".equalsIgnoreCase(value))
        {
            return true;
        }

        return false;
    }

    public String getString(String key)
    {
        String value = parMap.get(key);

        if (value == null)
        {
            return "";
        }

        return parMap.get(key);
    }

    public int getInt(String key)
    {
        String value = parMap.get(key);

        return MathTools.parseInt(value);
    }

    /**
     * @return the jdbcOperation
     */
    public JdbcOperation getJdbcOperation()
    {
        return jdbcOperation;
    }

    /**
     * @param jdbcOperation
     *            the jdbcOperation to set
     */
    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation = jdbcOperation;
    }
}
