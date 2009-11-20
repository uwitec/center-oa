/*
 * File Name: ParameterDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-16
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.tools.MathTools;


/**
 * ParameterDAO
 * 
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
@Bean(name = "parameterDAO", initMethod = "init")
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
