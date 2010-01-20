/*
 * File Name: LogBeanHelper.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-5-18
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean.helper;


import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.LogBean;
import com.china.centet.yongyin.bean.OutBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-5-18
 * @see
 * @since
 */
public class LogBeanHelper
{
    /**
     * ¼ÇÂ¼ÈÕÖ¾
     * 
     * @param outBean
     * @param baseBean
     * @param oprType
     * @param stafferName
     * @return
     */
    public static LogBean getLogBean(OutBean outBean, BaseBean baseBean, int oprType,
                                     String stafferName)
    {
        LogBean bean = new LogBean();

        bean.setOprType(oprType);

        bean.setOutId(outBean.getFullId());

        bean.setType(outBean.getType());

        bean.setOutType(outBean.getOutType());

        bean.setProductName(baseBean.getProductName());

        bean.setCurrent(baseBean.getAmount());

        bean.setValue(baseBean.getPrice());

        bean.setStaffer(stafferName);

        bean.setRefId(outBean.getRefOutFullId());

        return bean;
    }
}
