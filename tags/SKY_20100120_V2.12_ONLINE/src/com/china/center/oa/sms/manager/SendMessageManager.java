/**
 * File Name: SendMessageManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-8-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sms.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;
import net.sourceforge.sannotations.annotation.Property;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.china.center.common.ConditionParse;
import com.china.center.oa.note.bean.ShortMessageConstant;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.oa.note.dao.ShortMessageTaskDAO;
import com.china.center.oa.sms.helper.SMSHelper;
import com.china.center.tools.TimeTools;


/**
 * SendMessageManager
 * 
 * @author ZHUZHU
 * @version 2009-8-16
 * @see SendMessageManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "sendMessageManager")
public class SendMessageManager
{
    private ShortMessageTaskDAO shortMessageTaskDAO = null;

    private final Log _logger = LogFactory.getLog(getClass());

    private Log SMSLOG = LogFactory.getLog("sms");

    @Property(value = "${sendSMSURL}")
    private String sendSMSURL = "";

    @Property(value = "${exNumber}")
    private String exNumber = "";

    private DataSourceTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public SendMessageManager()
    {}

    /**
     * sendSMSByMAS
     */
    public void sendSMSByMAS()
    {
        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addIntCondition("status", "=", ShortMessageConstant.STATUS_WAIT_SEND);

        condition.addCondition("order by logTime desc");

        List<ShortMessageTaskBean> smsList = shortMessageTaskDAO.queryEntityBeansByLimit(
            condition, 200);

        // 操作在数据库事务中完成
        TransactionTemplate tranTemplate = new TransactionTemplate(transactionManager);

        for (final ShortMessageTaskBean shortMessageTaskBean : smsList)
        {
            sendSMS(shortMessageTaskBean, tranTemplate);
        }
    }

    /**
     * @return the shortMessageTaskDAO
     */
    public ShortMessageTaskDAO getShortMessageTaskDAO()
    {
        return shortMessageTaskDAO;
    }

    /**
     * @param shortMessageTaskDAO
     *            the shortMessageTaskDAO to set
     */
    public void setShortMessageTaskDAO(ShortMessageTaskDAO shortMessageTaskDAO)
    {
        this.shortMessageTaskDAO = shortMessageTaskDAO;
    }

    /**
     * @return the sendSMSURL
     */
    public String getSendSMSURL()
    {
        return sendSMSURL;
    }

    /**
     * @param sendSMSURL
     *            the sendSMSURL to set
     */
    public void setSendSMSURL(String sendSMSURL)
    {
        this.sendSMSURL = sendSMSURL;
    }

    /**
     * @return the exNumber
     */
    public String getExNumber()
    {
        return exNumber;
    }

    /**
     * @param exNumber
     *            the exNumber to set
     */
    public void setExNumber(String exNumber)
    {
        this.exNumber = exNumber;
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
     * sendSMS
     * 
     * @param shortMessageTaskBean
     * @return Boolean.TRUE
     */
    private Object sendSMS(final ShortMessageTaskBean shortMessageTaskBean,
                           TransactionTemplate tranTemplate)
    {
        final boolean isSend = SMSHelper.sendSMSReal(sendSMSURL,
            shortMessageTaskBean.getReceiver().trim(), shortMessageTaskBean.getMessage().trim(),
            shortMessageTaskBean.getHandId(), exNumber);

        try
        {
            // must send each item in Transaction(may be wait)
            tranTemplate.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    if (isSend)
                    {
                        shortMessageTaskBean.setStatus(ShortMessageConstant.STATUS_SEND_SUCCESS);

                        if (shortMessageTaskBean.getMtype() == ShortMessageConstant.MTYPE_ONLY_SEND)
                        {
                            shortMessageTaskDAO.deleteEntityBean(shortMessageTaskBean.getId());
                        }
                        else
                        {
                            shortMessageTaskDAO.updateEntityBean(shortMessageTaskBean);
                        }
                    }
                    else
                    {
                        int fail = shortMessageTaskBean.getFail() + 1;

                        if (fail >= 3)
                        {
                            shortMessageTaskDAO.deleteEntityBean(shortMessageTaskBean.getId());

                            SMSLOG.warn("send faid and delete " + shortMessageTaskBean);

                            return Boolean.TRUE;
                        }

                        shortMessageTaskBean.setStatus(ShortMessageConstant.STATUS_INIT);

                        shortMessageTaskBean.setFail(fail);

                        // 过20分钟
                        shortMessageTaskBean.setSendTime(TimeTools.getDateTimeString(1200000));

                        shortMessageTaskDAO.updateEntityBean(shortMessageTaskBean);
                    }

                    return Boolean.TRUE;
                }
            });
        }
        catch (Throwable e)
        {
            _logger.error(e, e);
        }

        return Boolean.TRUE;
    }
}
