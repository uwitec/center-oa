/**
 * File Name: FinanceManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.manager.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.tax.bean.CheckViewBean;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.constanst.CheckConstant;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.dao.CheckViewDAO;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.FinanceItemDAO;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.TimeTools;


/**
 * FinanceManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-2-7
 * @see FinanceManagerImpl
 * @since 1.0
 */
public class FinanceManagerImpl implements FinanceManager
{
    private FinanceDAO financeDAO = null;

    private CommonDAO commonDAO = null;

    private CheckViewDAO checkViewDAO = null;

    private FinanceItemDAO financeItemDAO = null;

    /**
     * default constructor
     */
    public FinanceManagerImpl()
    {
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addFinanceBean(User user, FinanceBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getItemList());

        bean.setId(commonDAO.getSquenceString20());

        bean.setCreaterId(user.getStafferId());

        bean.setLogTime(TimeTools.now());

        List<FinanceItemBean> itemList = bean.getItemList();

        Map<String, List<FinanceItemBean>> pareMap = new HashMap<String, List<FinanceItemBean>>();

        double inTotal = 0.0d;

        double outTotal = 0.0d;

        for (FinanceItemBean financeItemBean : itemList)
        {
            financeItemBean.setId(commonDAO.getSquenceString20());

            financeItemBean.setPid(bean.getId());

            // 纳税实体
            financeItemBean.setDutyId(bean.getDutyId());

            financeItemBean.setLogTime(TimeTools.now());

            String key = financeItemBean.getPareId();

            if (pareMap.get(key) == null)
            {
                pareMap.put(key, new ArrayList<FinanceItemBean>());
            }

            pareMap.get(key).add(financeItemBean);

            if (financeItemBean.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                inTotal += financeItemBean.getInmoney();
            }

            if (financeItemBean.getForward() == TaxConstanst.TAX_FORWARD_OUT)
            {
                outTotal += financeItemBean.getOutmoney();
            }
        }

        bean.setInmoney(inTotal);

        bean.setOutmoney(outTotal);

        // 核对借贷必相等的原则
        Set<String> keySet = pareMap.keySet();

        for (String key : keySet)
        {
            List<FinanceItemBean> pareList = pareMap.get(key);

            double inMoney = 0.0d;

            double outMoney = 0.0d;

            for (FinanceItemBean item : pareList)
            {
                if (item.getForward() == TaxConstanst.TAX_FORWARD_IN)
                {
                    inMoney += item.getInmoney();
                }

                if (item.getForward() == TaxConstanst.TAX_FORWARD_OUT)
                {
                    outMoney += item.getOutmoney();
                }
            }

            if (inMoney != outMoney)
            {
                throw new MYException("借贷不等,凭证增加错误");
            }
        }

        financeDAO.saveEntityBean(bean);

        financeItemDAO.saveAllEntityBeans(itemList);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteFinanceBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        FinanceBean old = financeDAO.find(id);

        if (old == null)
        {
            throw new MYException("数据错误,请重新操作");
        }

        if (old.getCreateType() != TaxConstanst.FINANCE_CREATETYPE_HAND)
        {
            throw new MYException("只能删除手工凭证,请重新操作");
        }

        // 删除凭证
        financeDAO.deleteEntityBean(id);

        // 删除凭证项
        financeItemDAO.deleteEntityBeansByFK(id);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean checks(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        CheckViewBean bean = checkViewDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        String tableName = "";

        if (bean.getType() == CheckConstant.CHECK_TYPE_COMPSE)
        {
            tableName = "T_CENTER_COMPOSE";
        }
        else if (bean.getType() == CheckConstant.CHECK_TYPE_CHANGE)
        {
            tableName = "T_CENTER_PRICE_CHANGE";
        }
        else if (bean.getType() == CheckConstant.CHECK_TYPE_INBILL)
        {
            tableName = "T_CENTER_INBILL";
        }
        else if (bean.getType() == CheckConstant.CHECK_TYPE_OUTBILL)
        {
            tableName = "T_CENTER_OUTBILL";
        }
        else if (bean.getType() == CheckConstant.CHECK_TYPE_STOCK)
        {
            tableName = "T_CENTER_STOCK";
        }
        else if (bean.getType() == CheckConstant.CHECK_TYPE_BUY)
        {
            tableName = "T_CENTER_OUT";
        }
        else
        {
            throw new MYException("数据错误,请确认操作");
        }

        checkViewDAO.updateCheck(tableName, id, reason);

        checkViewDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * @return the financeDAO
     */
    public FinanceDAO getFinanceDAO()
    {
        return financeDAO;
    }

    /**
     * @param financeDAO
     *            the financeDAO to set
     */
    public void setFinanceDAO(FinanceDAO financeDAO)
    {
        this.financeDAO = financeDAO;
    }

    /**
     * @return the financeItemDAO
     */
    public FinanceItemDAO getFinanceItemDAO()
    {
        return financeItemDAO;
    }

    /**
     * @param financeItemDAO
     *            the financeItemDAO to set
     */
    public void setFinanceItemDAO(FinanceItemDAO financeItemDAO)
    {
        this.financeItemDAO = financeItemDAO;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }

    /**
     * @return the checkViewDAO
     */
    public CheckViewDAO getCheckViewDAO()
    {
        return checkViewDAO;
    }

    /**
     * @param checkViewDAO
     *            the checkViewDAO to set
     */
    public void setCheckViewDAO(CheckViewDAO checkViewDAO)
    {
        this.checkViewDAO = checkViewDAO;
    }
}
