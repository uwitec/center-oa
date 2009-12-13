/**
 * File Name: OutHandleMessage.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-8-10<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.portal;


import net.sourceforge.sannotations.annotation.Bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.china.center.common.MYException;
import com.china.center.oa.note.bean.ShortMessageConstant;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.oa.note.manager.AbstractHandleMessage;
import com.china.center.tools.RandomTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.dao.CommonDAO2;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.manager.OutManager;


/**
 * OutHandleMessage
 * 
 * @author ZHUZHU
 * @version 2009-8-10
 * @see OutHandleMessage
 * @since 1.0
 */
@Exceptional
@Bean(name = "outHandleMessage")
public class OutHandleMessage extends AbstractHandleMessage
{
    private final Log _logger = LogFactory.getLog(getClass());

    private CommonDAO2 commonDAO2 = null;

    private OutManager outManager = null;

    private UserDAO userDAO = null;

    public int getHandleType()
    {
        return TYPE_OUT;
    }

    public void handleMessage(ShortMessageTaskBean task)
    {
        String[] parserMessage = parserMessage(task, 3);

        if ("0".equals(parserMessage[1].trim()))
        {
            try
            {
                handlePass(task);

                moveDataToHis(task);

                sendSMS(task, "OA提示您的短信审批成功:" + task.getHandId(), false);
            }
            catch (MYException e)
            {
                _logger.error(e, e);

                // send SMS and reset message to receive
                sendSMS(task, "OA提示您的短信审批无效(" + task.getHandId() + "):" + e.getErrorContent(),
                    true);
            }
        }

        if ("1".equals(parserMessage[1].trim()))
        {
            try
            {
                handleReject(task);

                moveDataToHis(task);

                sendSMS(task, "OA提示您的短信审批成功:" + task.getHandId(), false);
            }
            catch (MYException e)
            {
                _logger.error(e, e);

                // send SMS and reset message to receive
                sendSMS(task, "OA提示您的短信审批无效(" + task.getHandId() + "):" + e.getErrorContent(),
                    true);
            }
        }
    }

    private void handlePass(ShortMessageTaskBean task)
        throws MYException
    {
        String outId = task.getFk();

        OutBean outBean = outManager.findOutById(outId);

        if (outBean == null)
        {
            throw new MYException("系统内部错误");
        }

        User user = userDAO.findFirstUserByStafferId(task.getStafferId());

        if (user == null)
        {
            throw new MYException("系统内部错误");
        }

        if ( !String.valueOf(outBean.getStatus()).equals(task.getFktoken()))
        {
            throw new MYException("销售单状态错误,无法审批");
        }

        int nextStatus = 6;

        if ("1".equals(task.getFktoken()))
        {
            nextStatus = 6;
        }
        else if ("6".equals(task.getFktoken()) && !"0".equals(outBean.getLocation()))
        {
            nextStatus = 3;
        }
        else
        {
            throw new MYException("销售单状态错误,无法审批");
        }

        outManager.pass(outId, user, nextStatus, "短信审批通过", null);
    }

    /**
     * handleReject
     * 
     * @param task
     * @throws MYException
     */
    private void handleReject(ShortMessageTaskBean task)
        throws MYException
    {
        String[] parserMessage = parserMessage(task, 3);

        String outId = task.getFk();

        OutBean outBean = outManager.findOutById(outId);

        if (outBean == null)
        {
            throw new MYException("系统内部错误");
        }

        if ( !String.valueOf(outBean.getStatus()).equals(task.getFktoken()))
        {
            throw new MYException("销售单状态错误,无法审批");
        }

        User user = userDAO.findFirstUserByStafferId(task.getStafferId());

        if (user == null)
        {
            throw new MYException("系统内部错误");
        }

        outManager.reject(outId, user, parserMessage[2]);
    }

    private void sendSMS(final ShortMessageTaskBean task, String msg, final boolean updateTask)
    {
        if (updateTask)
        {
            task.setReceiveMsg("");

            task.setStatus(ShortMessageConstant.STATUS_SEND_SUCCESS);
        }

        // send short message
        final ShortMessageTaskBean sms = new ShortMessageTaskBean();

        sms.setId(commonDAO2.getSquenceString20());

        sms.setFk(task.getFk());

        sms.setType(getHandleType());

        sms.setStatus(ShortMessageConstant.STATUS_WAIT_SEND);

        sms.setMtype(ShortMessageConstant.MTYPE_ONLY_SEND);

        sms.setFktoken("");

        sms.setMessage(msg);

        sms.setHandId(RandomTools.getRandomMumber(4));

        sms.setReceiver(task.getReceiver());

        sms.setStafferId(task.getStafferId());

        sms.setLogTime(TimeTools.now());

        // 24 hour
        sms.setEndTime(TimeTools.now(1));

        TransactionTemplate tran = new TransactionTemplate(transactionManager);

        tran.execute(new TransactionCallback()
        {
            public Object doInTransaction(TransactionStatus arg0)
            {
                if (updateTask)
                {
                    shortMessageTaskDAO.updateEntityBean(task);
                }

                // add SMS
                return shortMessageTaskDAO.saveEntityBean(sms);
            }
        });
    }

    /**
     * cancleMessage
     */
    public void cancelMessage(ShortMessageTaskBean task)
    {
        OutBean outBean = outManager.findOutById(task.getFk());

        if (outBean == null || !String.valueOf(outBean.getStatus()).equals(task.getFktoken()))
        {
            // delete task
            deleteMsg(task);
        }
    }

    /**
     * @return the commonDAO2
     */
    public CommonDAO2 getCommonDAO2()
    {
        return commonDAO2;
    }

    /**
     * @param commonDAO2
     *            the commonDAO2 to set
     */
    public void setCommonDAO2(CommonDAO2 commonDAO2)
    {
        this.commonDAO2 = commonDAO2;
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
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }
}
