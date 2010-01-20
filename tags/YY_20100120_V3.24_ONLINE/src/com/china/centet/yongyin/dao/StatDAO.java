/*
 * File Name: StatDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-2
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.tools.BeanUtil;
import com.china.centet.yongyin.bean.StatBankBean;
import com.china.centet.yongyin.bean.StatBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-9-2
 * @see
 * @since
 */
public class StatDAO
{
    private JdbcOperation jdbcOperation = null;

    /**
     * default constructor
     */
    public StatDAO()
    {}

    public boolean addStat(final StatBean bean)
    {
        return jdbcOperation.save(bean) > 0;
    }

    public boolean addStatBank(final StatBankBean bean)
    {
        return jdbcOperation.save(bean) > 0;
    }

    public StatBankBean findStatBankBean(String statId, String bank)
    {
        List<StatBankBean> list = jdbcOperation.queryForList("where statId = ? and bank = ?",
            StatBankBean.class, statId, bank);

        if (list.isEmpty())
        {
            return null;
        }

        return list.get(0);
    }

    public boolean updateStat(final StatBean bean)
    {
        return jdbcOperation.update(bean) > 0;
    }

    public StatBean findStatBeanById(String id)
    {
        return jdbcOperation.find(id, StatBean.class);
    }

    public StatBean findStatBeanByBank(String statId, String bank)
    {
        List<StatBean> list = jdbcOperation.queryForList("where statId = ? and bank = ?",
            StatBean.class, statId, bank);

        if (list.isEmpty())
        {
            return null;
        }

        return list.get(0);
    }

    public boolean deleteStatBean(String statId, String bank)
    {
        String sql = "delete from t_center_statmoney where statId = ? and bank = ?";

        jdbcOperation.update(sql, new Object[] {statId, bank});

        return true;
    }

    public List<StatBean> queryStatByCondition(ConditionParse condition)
    {
        String sql = "select * from t_center_statmoney " + condition.toString();

        return (List<StatBean>)BeanUtil.getListBean(jdbcOperation.queryForList(sql),
            StatBean.class);
    }

    public int countByStatId(String statId, boolean isFlag)
    {
        String sql = "select count(*) from t_center_statmoney where statid = ?";

        return jdbcOperation.queryForInt(sql, new Object[] {statId});
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
