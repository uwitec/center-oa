/**
 * File Name: CurOutManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.china.center.common.MYException;
import com.china.center.oa.constant.CreditConstant;
import com.china.center.oa.credit.bean.CreditItemSecBean;
import com.china.center.oa.credit.bean.CreditItemThrBean;
import com.china.center.oa.credit.bean.CurOutBean;
import com.china.center.oa.credit.dao.CreditItemSecDAO;
import com.china.center.oa.credit.dao.CreditItemThrDAO;
import com.china.center.oa.credit.dao.CreditlogDAO;
import com.china.center.oa.credit.dao.CurOutDAO;
import com.china.center.oa.credit.dao.CustomerCreditDAO;
import com.china.center.oa.credit.dao.OutStatDAO;
import com.china.center.oa.credit.vs.CustomerCreditBean;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.helper.UserHelper;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.OutBean;


/**
 * CurOutManager
 * 
 * @author ZHUZHU
 * @version 2009-12-2
 * @see CurOutManager
 * @since 1.0
 */
@Bean(name = "curOutManager")
public class CurOutManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log _coreLog = LogFactory.getLog("core");

    private final Log triggerLog = LogFactory.getLog("trigger");

    private CurOutDAO curOutDAO = null;

    private OutStatDAO outStatDAO = null;

    private CreditlogDAO creditlogDAO = null;

    private CustomerDAO customerDAO = null;

    private CustomerCreditDAO customerCreditDAO = null;

    private CreditItemSecDAO creditItemSecDAO = null;

    private CreditItemThrDAO creditItemThrDAO = null;

    private CustomerCreditManager customerCreditManager = null;

    private DataSourceTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public CurOutManager()
    {}

    /**
     * 客户统计
     */
    public void statOut()
    {
        triggerLog.info("统计客户信用开始.....");

        List<String> customerIdList = outStatDAO.listCustomerIdList();

        User user = UserHelper.getSystemUser();

        CreditItemSecBean outItem = creditItemSecDAO.find(CreditConstant.OUT_COMMON_ITEM);

        CreditItemSecBean outDelayItem = creditItemSecDAO.find(CreditConstant.OUT_DELAY_ITEM);

        CreditItemThrBean maxDelayThrItem = creditItemThrDAO.findMaxDelayItem();

        if (outItem == null || outDelayItem == null || maxDelayThrItem == null)
        {
            _coreLog.error("miss CreditItemSecBean:" + CreditConstant.OUT_COMMON_ITEM + ".or:"
                           + CreditConstant.OUT_DELAY_ITEM + ".or:maxDelayThrItem");

            triggerLog.info("统计客户信用结束.....");

            return;
        }

        // iterator to handle
        for (String cid : customerIdList)
        {
            try
            {
                handleEachCustomer(user, outItem, maxDelayThrItem, cid);
            }
            catch (Throwable e)
            {
                _logger.error(e, e);
            }
        }

        triggerLog.info("统计客户信用结束.....");
    }

    /**
     * 处理每个客户
     * 
     * @param user
     * @param outItem
     * @param maxDelayThrItem
     * @param customerBean
     */
    private void handleEachCustomer(User user, CreditItemSecBean outItem,
                                    CreditItemThrBean maxDelayThrItem, String cid)
    {
        // 从2009-12-01开始分析客户的行为
        List<OutBean> outList = outStatDAO.queryNoneStatByCid(cid);

        if (outList.size() == 0)
        {
            return;
        }

        // 开始分析
        for (OutBean outBean : outList)
        {
            // 付款且没有延期
            if (outBean.getPay() == CreditConstant.PAY_YES && outBean.getTempType() == 0)
            {
                handleCommon(user, cid, outItem, outBean);
            }

            // 延期的
            if (outBean.getPay() == CreditConstant.PAY_YES && outBean.getTempType() > 0)
            {
                // 减分 记入日志(变成已经处理)
                handleDelay(user, cid, outBean, maxDelayThrItem, true);
            }

            // 还未付款的，看看是否已经超期
            if (outBean.getPay() == CreditConstant.PAY_NOT)
            {
                int delay = TimeTools.cdate(TimeTools.now_short(), outBean.getRedate());

                // 等待延期
                if (delay <= 0)
                {
                    return;
                }

                // 处理延期
                handleDelay(user, cid, outBean, maxDelayThrItem, false);
            }
        }
    }

    /**
     * handleCommon
     * 
     * @param user
     * @param customerBean
     * @param cid
     * @param outItem
     */
    private void handleCommon(final User user, final String cid, final CreditItemSecBean outItem,
                              final OutBean outBean)
    {
        CustomerCreditBean customerCreditBean = customerCreditDAO.findByUnique(cid,
            CreditConstant.OUT_COMMON_ITEM);

        // 加分 记入日志(变成已经处理 reserve1=1)
        if (customerCreditBean == null)
        {
            customerCreditBean = new CustomerCreditBean();

            customerCreditBean.setVal(outItem.getPer());

            customerCreditBean.setLog("销售单正常付款,加分:" + MathTools.formatNum(outItem.getPer())
                                      + ".加分后:" + MathTools.formatNum(outItem.getPer()));
        }
        else
        {
            customerCreditBean.setLog("销售单正常付款,加分:"
                                      + MathTools.formatNum(outItem.getPer())
                                      + ".加分后:"
                                      + MathTools.formatNum(customerCreditBean.getVal()
                                                            + outItem.getPer()));

            customerCreditBean.setVal(outItem.getPer() + customerCreditBean.getVal());
        }

        customerCreditBean.setCid(cid);

        customerCreditBean.setLogTime(TimeTools.now());

        customerCreditBean.setPtype(CreditConstant.CREDIT_TYPE_DYNAMIC);

        customerCreditBean.setItemId(CreditConstant.OUT_COMMON_ITEM);

        customerCreditBean.setPitemId(CreditConstant.OUT_COMMON_ITEM_PARENT);

        customerCreditBean.setValueId("0");

        final CustomerCreditBean fcustomerCreditBean = customerCreditBean;

        // 操作在数据库事务中完成
        TransactionTemplate tranTemplate = new TransactionTemplate(transactionManager);

        try
        {
            // must send each item in Transaction(may be wait)
            tranTemplate.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    try
                    {
                        customerCreditManager.interposeCreditInner(user, cid, fcustomerCreditBean);
                    }
                    catch (MYException e)
                    {
                        _logger.warn(e, e);
                        throw new RuntimeException(e.getErrorContent());
                    }

                    // 更新out里面的状态
                    outStatDAO.updateReserve1ByFullId(outBean.getFullId(),
                        CreditConstant.CREDIT_OUT_END,
                        MathTools.formatNum(fcustomerCreditBean.getVal()));

                    double minus = fcustomerCreditBean.getVal();

                    saveCurLog(cid, outBean, minus);

                    triggerLog.info("handle out stat[" + cid + "]:" + outBean.getFullId());

                    return Boolean.TRUE;
                }
            });
        }
        catch (Throwable e)
        {
            _logger.error(e, e);
        }
    }

    /**
     * 处理延期的销售单
     * 
     * @param user
     * @param customerBean
     * @param cid
     * @param outItem
     * @param outBean
     */
    private void handleDelay(final User user, final String cid, final OutBean outBean,
                             final CreditItemThrBean maxDelayThrItem, boolean isEnd)
    {
        CustomerCreditBean customerCreditBean = customerCreditDAO.findByUnique(cid,
            CreditConstant.OUT_DELAY_ITEM);

        if ( !isEnd)
        {
            int delay = TimeTools.cdate(TimeTools.now_short(), outBean.getRedate());

            outBean.setTempType(delay);
        }

        final CreditItemThrBean delayItemThrBean = creditItemThrDAO.findDelayItemByDays(outBean.getTempType());

        // 设置延期时间
        if ( !isEnd)
        {
            // 如果还是上次的更新就返回(防止重复扣除)
            if (outBean.getReserve5().equals(delayItemThrBean.getId()))
            {
                return;
            }
        }

        // 如果最大的天数已经超过定义的延期天数据全部结束
        if (outBean.getTempType() >= maxDelayThrItem.getIndexPos())
        {
            isEnd = true;
        }

        // 减分 记入日志
        final double currentMinus = handleCurrentMinus(outBean, delayItemThrBean);

        if (customerCreditBean == null)
        {
            customerCreditBean = new CustomerCreditBean();

            // 负向指标
            customerCreditBean.setVal( -currentMinus);

            customerCreditBean.setLog("销售单延期[" + outBean.getTempType() + "天]付款,减分:"
                                      + MathTools.formatNum(currentMinus) + ".减分后:"
                                      + MathTools.formatNum( -currentMinus));
        }
        else
        {
            customerCreditBean.setLog("销售单延期["
                                      + outBean.getTempType()
                                      + "天]付款,减分:"
                                      + MathTools.formatNum(currentMinus)
                                      + ".减分后:"
                                      + MathTools.formatNum( (customerCreditBean.getVal() - currentMinus)));

            customerCreditBean.setVal(customerCreditBean.getVal() - currentMinus);
        }

        customerCreditBean.setCid(cid);

        customerCreditBean.setLogTime(TimeTools.now());

        customerCreditBean.setPtype(CreditConstant.CREDIT_TYPE_DYNAMIC);

        customerCreditBean.setItemId(CreditConstant.OUT_DELAY_ITEM);

        customerCreditBean.setPitemId(CreditConstant.OUT_DELAY_ITEM_PARENT);

        customerCreditBean.setValueId(delayItemThrBean.getId());

        final CustomerCreditBean fcustomerCreditBean = customerCreditBean;

        final boolean fisEnd = isEnd;

        // 操作在数据库事务中完成
        TransactionTemplate tranTemplate = new TransactionTemplate(transactionManager);

        try
        {
            // must send each item in Transaction(may be wait)
            tranTemplate.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    try
                    {
                        customerCreditManager.interposeCreditInner(user, cid, fcustomerCreditBean);
                    }
                    catch (MYException e)
                    {
                        _logger.warn(e, e);

                        throw new RuntimeException(e.getErrorContent());
                    }

                    // 更新out里面的状态
                    if (fisEnd)
                    {
                        outStatDAO.updateReserve1ByFullId(outBean.getFullId(),
                            CreditConstant.CREDIT_OUT_END,
                            MathTools.formatNum(fcustomerCreditBean.getVal()));
                    }
                    else
                    {
                        // 更新当前已经延期级别
                        outStatDAO.updateReserve5ByFullId(outBean.getFullId(),
                            delayItemThrBean.getId());
                    }

                    saveCurLog(cid, outBean, fcustomerCreditBean.getVal());

                    triggerLog.info("handle out stat[" + cid + "]:" + outBean.getFullId());

                    return Boolean.TRUE;
                }
            });
        }
        catch (Throwable e)
        {
            _logger.error(e, e);
        }
    }

    /**
     * handleCurrentMinus
     * 
     * @param outBean
     * @param delayItemThrBean
     * @return
     */
    private double handleCurrentMinus(final OutBean outBean,
                                      final CreditItemThrBean delayItemThrBean)
    {
        boolean isNearestDelay = false;

        OutBean nearestById = outStatDAO.findNearestById(outBean.getId(), outBean.getCustomerId());

        if (nearestById != null && nearestById.getTempType() > 0)
        {
            isNearestDelay = true;
        }

        double currentMinus = delayItemThrBean.getPer();

        // 上次已经扣除了一部分了
        if ( !StringTools.isNullOrNone(outBean.getReserve4()))
        {
            // 这里是正的
            double hasMinus = Math.abs(MathTools.parseDouble(outBean.getReserve4()));

            currentMinus = currentMinus - hasMinus;
        }

        if (isNearestDelay)
        {
            // 扣除双倍的
            return currentMinus * 2;
        }

        return currentMinus;
    }

    /**
     * @return the curOutDAO
     */
    public CurOutDAO getCurOutDAO()
    {
        return curOutDAO;
    }

    /**
     * @param curOutDAO
     *            the curOutDAO to set
     */
    public void setCurOutDAO(CurOutDAO curOutDAO)
    {
        this.curOutDAO = curOutDAO;
    }

    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    /**
     * @param customerDAO
     *            the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
    }

    /**
     * @return the outStatDAO
     */
    public OutStatDAO getOutStatDAO()
    {
        return outStatDAO;
    }

    /**
     * @param outStatDAO
     *            the outStatDAO to set
     */
    public void setOutStatDAO(OutStatDAO outStatDAO)
    {
        this.outStatDAO = outStatDAO;
    }

    /**
     * @return the transactionManager
     */
    public DataSourceTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param transactionManager
     *            the transactionManager to set
     */
    public void setTransactionManager(DataSourceTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    /**
     * @return the creditlogDAO
     */
    public CreditlogDAO getCreditlogDAO()
    {
        return creditlogDAO;
    }

    /**
     * @param creditlogDAO
     *            the creditlogDAO to set
     */
    public void setCreditlogDAO(CreditlogDAO creditlogDAO)
    {
        this.creditlogDAO = creditlogDAO;
    }

    /**
     * @return the creditItemSecDAO
     */
    public CreditItemSecDAO getCreditItemSecDAO()
    {
        return creditItemSecDAO;
    }

    /**
     * @param creditItemSecDAO
     *            the creditItemSecDAO to set
     */
    public void setCreditItemSecDAO(CreditItemSecDAO creditItemSecDAO)
    {
        this.creditItemSecDAO = creditItemSecDAO;
    }

    /**
     * @return the customerCreditManager
     */
    public CustomerCreditManager getCustomerCreditManager()
    {
        return customerCreditManager;
    }

    /**
     * @param customerCreditManager
     *            the customerCreditManager to set
     */
    public void setCustomerCreditManager(CustomerCreditManager customerCreditManager)
    {
        this.customerCreditManager = customerCreditManager;
    }

    /**
     * @return the customerCreditDAO
     */
    public CustomerCreditDAO getCustomerCreditDAO()
    {
        return customerCreditDAO;
    }

    /**
     * @param customerCreditDAO
     *            the customerCreditDAO to set
     */
    public void setCustomerCreditDAO(CustomerCreditDAO customerCreditDAO)
    {
        this.customerCreditDAO = customerCreditDAO;
    }

    /**
     * @return the creditItemThrDAO
     */
    public CreditItemThrDAO getCreditItemThrDAO()
    {
        return creditItemThrDAO;
    }

    /**
     * @param creditItemThrDAO
     *            the creditItemThrDAO to set
     */
    public void setCreditItemThrDAO(CreditItemThrDAO creditItemThrDAO)
    {
        this.creditItemThrDAO = creditItemThrDAO;
    }

    /**
     * saveCurLog
     * 
     * @param customerBean
     * @param outBean
     * @param minus
     */
    private void saveCurLog(final String cid, final OutBean outBean, double minus)
    {
        // add log CurOutBean
        CurOutBean log = new CurOutBean();

        log.setCid(cid);

        log.setDelay(outBean.getTempType());

        log.setLogTime(TimeTools.now());

        log.setOutId(outBean.getFullId());

        log.setVal(minus);

        curOutDAO.saveEntityBean(log);
    }
}
