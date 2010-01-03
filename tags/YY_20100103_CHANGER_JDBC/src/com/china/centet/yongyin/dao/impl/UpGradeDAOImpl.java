/*
 * File Name: UpGradeDAOImpl.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-17
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao.impl;


import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.tools.BeanUtil;
import com.china.centet.yongyin.bean.Version;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.UpGradeDAO;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-8-17
 * @see
 * @since
 */
public class UpGradeDAOImpl implements UpGradeDAO
{
    private JdbcOperation jdbcOperation = null;

    public void initMenuItem()
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.centet.yongyin.dao.UpGradeDAO#getVersion()
     */
    public Version getVersion()
    {

        String sql = "select * from version where index = ?";

        Version version = new Version();

        BeanUtil.getBeanInner(version, jdbcOperation.queryForMap(sql,
            new Object[] {Constant.VERSION_INDEX}));

        return version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.centet.yongyin.dao.UpGradeDAO#updateVersion(com.china.centet.yongyin.bean.Version)
     */
    public boolean updateVersion(Version version)
    {
        String sql = "update version set version = ?, sequences = ?, lastdate = ? where index = ?";

        return jdbcOperation.update(sql, new Object[] {version.getVersion(),
            version.getSequences(), version.getLastdate(), Constant.VERSION_INDEX}) != 0;
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
