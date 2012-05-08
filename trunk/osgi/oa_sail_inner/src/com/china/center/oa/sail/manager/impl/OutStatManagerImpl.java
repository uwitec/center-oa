/**
 * File Name: OutStatManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2012-5-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.manager.impl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;

import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.publics.helper.UserHelper;
import com.china.center.oa.publics.manager.CommonManager;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.sail.manager.OutStatManager;
import com.china.center.tools.TimeTools;


/**
 * OutStatManagerImpl
 * 
 * @author ZHUZHU
 * @version 2012-5-8
 * @see OutStatManagerImpl
 * @since 3.0
 */
public class OutStatManagerImpl implements OutStatManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log triggerLog = LogFactory.getLog("trigger");

    private OutManager outManager = null;

    private OutDAO outDAO = null;

    private CommonManager commonManager = null;

    private PlatformTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public OutStatManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.manager.OutStatManager#statOutPay()
     */
    public void statOutPay()
        throws MYException
    {
        triggerLog.info("begin statOutPay...");

        // 查询最近三个月的付款的销售单
        ConditionParse con = new ConditionParse();
        con.addCondition("outTime", ">=", TimeTools.now_short( -150));
        con.addIntCondition("type", "=", OutConstant.OUT_TYPE_OUTBILL);
        con.addIntCondition("pay", "=", OutConstant.PAY_YES);

        final List<OutBean> outList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : outList)
        {
            ResultBean result = outManager.checkOutPayStatus(UserHelper.getSystemUser(), outBean);

            // 金额不足
            if (result.getResult() == 1)
            {
                try
                {
                    String msg = "严重告警:销售单[" + outBean.getFullId() + "]费用异常,单据是已经付款,但是实际金额不足.具体描述:"
                                 + result.getMessage();

                    if (commonManager.addAlarm(outBean.getFullId(), msg))
                    {
                        triggerLog.warn(msg);
                    }
                }
                catch (Exception e)
                {
                    _logger.error(e, e);
                }
            }
        }

        triggerLog.info("success stat outList:" + outList.size());

        triggerLog.info("end statOutPay...");
    }

    /**
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
    }

    /**
     * @return the commonManager
     */
    public CommonManager getCommonManager()
    {
        return commonManager;
    }

    /**
     * @param commonManager
     *            the commonManager to set
     */
    public void setCommonManager(CommonManager commonManager)
    {
        this.commonManager = commonManager;
    }

    /**
     * @return the transactionManager
     */
    public PlatformTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param transactionManager
     *            the transactionManager to set
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    /**
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

}
