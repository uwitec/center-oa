/**
 * File Name: SnapshotDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-9-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.job.dao;


import java.util.HashMap;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.JdbcOperation;


/**
 * SnapshotDAO
 * 
 * @author ZHUZHU
 * @version 2009-9-16
 * @see SnapshotDAO
 * @since 1.0
 */
@Bean(name = "snapshotDAO")
public class SnapshotDAO
{
    private JdbcOperation jdbcOperation = null;

    /**
     * default constructor
     */
    public SnapshotDAO()
    {}

    /**
     * snapshot of productNumber
     * 
     * @param path
     * @param pathCenter
     *            the path for ZB
     */
    public void snapshotProductNumber(String path, String pathCenter)
    {
        Map map = new HashMap();

        map.put("path", path);

        jdbcOperation.getIbatisDaoSupport().update("SnapshotDAO.snapshotProductNumber", map);

        map.clear();

        map.put("path", pathCenter);

        jdbcOperation.getIbatisDaoSupport().update("SnapshotDAO.snapshotProductNumberInCenter",
            map);
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
