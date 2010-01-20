/**
 * File Name: DefaultHandleMessageImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-8-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.note.manager;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.china.center.common.ConditionParse;
import com.china.center.oa.note.bean.ShortMessageConstant;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.oa.note.bean.ShortMessageTaskHisBean;
import com.china.center.oa.note.dao.ShortMessageTaskDAO;
import com.china.center.oa.note.dao.ShortMessageTaskHisDAO;
import com.china.center.tools.BeanUtil;


/**
 * DefaultHandleMessageImpl
 * 
 * @author ZHUZHU
 * @version 2009-8-8
 * @see DefaultHandleMessageImpl
 * @since 1.0
 */
public class DefaultHandleMessageImpl implements ApplicationContextAware
{
    private final Log _logger = LogFactory.getLog(getClass());

    /**
     * handleObjectList
     */
    private List<String> handleObjectList = new ArrayList();

    private List<HandleMessage> handleObjectInstanceList = new ArrayList();

    private ShortMessageTaskDAO shortMessageTaskDAO = null;

    private ShortMessageTaskHisDAO shortMessageTaskHisDAO = null;

    /**
     * default constructor
     */
    public DefaultHandleMessageImpl()
    {}

    /**
     * NOTE handle SMS message portal
     */
    public void handleMessage()
    {
        for (HandleMessage eachItem : handleObjectInstanceList)
        {
            ConditionParse con = createCondition(eachItem);
            // query near records
            List<ShortMessageTaskBean> smsList = shortMessageTaskDAO.queryEntityBeansByLimit(con,
                200);

            for (ShortMessageTaskBean shortMessageTaskBean : smsList)
            {
                try
                {
                    eachItem.handleMessage(shortMessageTaskBean);
                }
                catch (Throwable e)
                {
                    _logger.error(e, e);
                }
            }

            // cancel message
            con = createCondition2(eachItem);

            // query near records
            smsList = shortMessageTaskDAO.queryEntityBeansByLimit(con, 200);

            for (ShortMessageTaskBean shortMessageTaskBean : smsList)
            {
                try
                {
                    eachItem.cancelMessage(shortMessageTaskBean);
                }
                catch (Throwable e)
                {
                    _logger.error(e, e);
                }
            }
        }
    }

    /**
     * moveData
     * 
     * @param shortMessageTaskBean
     */
    public void moveDataToHis(ShortMessageTaskBean shortMessageTaskBean)
    {
        ShortMessageTaskHisBean his = new ShortMessageTaskHisBean();

        BeanUtil.copyProperties(his, shortMessageTaskBean);

        his.setStatus(ShortMessageConstant.STATUS_END_COMMON);

        shortMessageTaskDAO.deleteEntityBean(shortMessageTaskBean.getId());

        shortMessageTaskHisDAO.saveEntityBean(his);
    }

    /**
     * createCondition
     * 
     * @param eachItem
     * @return
     */
    private ConditionParse createCondition(HandleMessage eachItem)
    {
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addIntCondition("type", "=", eachItem.getHandleType());

        con.addIntCondition("status", "=", ShortMessageConstant.STATUS_RECEIVE_SUCCESS);

        con.addIntCondition("mtype", "=", ShortMessageConstant.MTYPE_ONLY_SEND_RECEIVE);

        return con;
    }

    /**
     * createCondition
     * 
     * @param eachItem
     * @return
     */
    private ConditionParse createCondition2(HandleMessage eachItem)
    {
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addIntCondition("type", "=", eachItem.getHandleType());

        con.addIntCondition("status", "=", ShortMessageConstant.STATUS_INIT);

        con.addIntCondition("mtype", "=", ShortMessageConstant.MTYPE_ONLY_SEND_RECEIVE);

        return con;
    }

    public void setApplicationContext(ApplicationContext context)
        throws BeansException
    {
        for (String eachItem : handleObjectList)
        {
            Object bean = context.getBean(eachItem);

            if (bean != null)
            {
                if (bean instanceof HandleMessage)
                {
                    handleObjectInstanceList.add((HandleMessage)bean);
                }
            }
        }
    }

    /**
     * @return the handleObjectList
     */
    public List<String> getHandleObjectList()
    {
        return handleObjectList;
    }

    /**
     * @param handleObjectList
     *            the handleObjectList to set
     */
    public void setHandleObjectList(List<String> handleObjectList)
    {
        this.handleObjectList = handleObjectList;
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
     * @return the shortMessageTaskHisDAO
     */
    public ShortMessageTaskHisDAO getShortMessageTaskHisDAO()
    {
        return shortMessageTaskHisDAO;
    }

    /**
     * @param shortMessageTaskHisDAO
     *            the shortMessageTaskHisDAO to set
     */
    public void setShortMessageTaskHisDAO(ShortMessageTaskHisDAO shortMessageTaskHisDAO)
    {
        this.shortMessageTaskHisDAO = shortMessageTaskHisDAO;
    }
}
