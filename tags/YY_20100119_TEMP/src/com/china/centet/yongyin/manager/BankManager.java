/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.manager;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.dao.DataAccessException;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.BankBean;
import com.china.centet.yongyin.bean.StatBean;
import com.china.centet.yongyin.dao.BankDAO;
import com.china.centet.yongyin.dao.StatDAO;


/**
 * 银行的dao
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class BankManager
{
    private BankDAO bankDAO = null;

    private StatDAO statDAO = null;

    /**
     * default constructor
     */
    public BankManager()
    {}

    @Exceptional(sensitiveException = {DataAccessException.class})
    public boolean addBank(BankBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean, bean.getName());

        if (bankDAO.countByName(bean.getName()) > 0)
        {
            throw new MYException("银行已经存在!");
        }

        bankDAO.saveEntityBean(bean);

        addStat(bean);

        return true;
    }

    /**
     * @param bean
     */
    private void addStat(BankBean bean)
    {
        // 获得上个月是否统计
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);

        String lastId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMM");

        StatBean sbean = new StatBean();

        sbean.setStatId(lastId);

        sbean.setBank(bean.getName());

        sbean.setLogDate(TimeTools.now_short());

        statDAO.addStat(sbean);
    }

    public boolean modifyBank(BankBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        JudgeTools.judgeParameterIsNull(bean.getName(), bean.getId());

        BankBean old = bankDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("银行不存在!");
        }

        if ( !old.getName().equals(bean.getName()))
        {
            if (bankDAO.countByName(bean.getName()) > 0)
            {
                throw new MYException("银行已经存在!");
            }
        }

        return bankDAO.updateEntityBean(bean);
    }

    public boolean delBank(String id)
        throws MYException
    {
        return bankDAO.deleteEntityBean(id);
    }

    public List<BankBean> listBank()
    {
        return bankDAO.listEntityBeans();
    }

    public List<BankBean> queryBankByCondition(ConditionParse condition)
    {
        return bankDAO.queryEntityBeansBycondition(condition);
    }

    public List<BankBean> queryBankByLocationId(String locationId)
    {
        ConditionParse condition = new ConditionParse();

        condition.addCondition("locationId", "=", locationId);

        return bankDAO.queryEntityBeansBycondition(condition);
    }

    public BankBean findBankById(String id)
    {
        return bankDAO.find(id);
    }

    public BankBean findBankByName(String name)
    {
        return bankDAO.findBankByName(name);
    }

    /**
     * @return the bankDAO
     */
    public BankDAO getBankDAO()
    {
        return bankDAO;
    }

    /**
     * @param bankDAO
     *            the bankDAO to set
     */
    public void setBankDAO(BankDAO bankDAO)
    {
        this.bankDAO = bankDAO;
    }

    /**
     * @return the statDAO
     */
    public StatDAO getStatDAO()
    {
        return statDAO;
    }

    /**
     * @param statDAO
     *            the statDAO to set
     */
    public void setStatDAO(StatDAO statDAO)
    {
        this.statDAO = statDAO;
    }
}
