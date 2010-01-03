/**
 * File Name: ProctExceptionDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-2-16<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.centet.yongyin.bean.ProctExceptionBean;


/**
 * 货物异常处理单的DAO
 * 
 * @author zhuzhu
 * @version 2008-2-16
 * @see
 * @since
 */
public class ProctExceptionDAO
{
    private JdbcOperation jdbcOperation = null;

    /**
     * default constructor
     */
    public ProctExceptionDAO()
    {}

    public boolean addProctException(ProctExceptionBean bean)
    {
        return jdbcOperation.save(bean) > 0;
    }

    public boolean updateProctException(ProctExceptionBean bean)
    {
        return jdbcOperation.update(bean) > 0;
    }

    public boolean updateProctExceptionStatus(String id, int status, String apply)
    {
        return jdbcOperation.update(
            "update T_CENTER_PROCTEXCEPTION set status = ?, apply = ? where id = ?", status,
            apply, id) > 0;
    }

    public boolean delProctException(String id)
    {
        return jdbcOperation.delete(id, ProctExceptionBean.class) > 0;
    }

    public List<ProctExceptionBean> queryProctExceptionByCondition(ConditionParse condition)
    {
        condition.addWhereStr();

        return jdbcOperation.queryForList(condition.toString() + " order by logDate desc",
            ProctExceptionBean.class);
    }

    public ProctExceptionBean findProctExceptionById(String id)
    {
        return jdbcOperation.find(id, ProctExceptionBean.class);
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
