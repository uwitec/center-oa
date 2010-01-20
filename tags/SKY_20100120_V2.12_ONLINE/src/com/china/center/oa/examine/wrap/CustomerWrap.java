/**
 * File Name: CustomerWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.wrap;


import com.china.center.oa.customer.bean.CustomerBean;


/**
 * CustomerWrap
 * 
 * @author zhuzhu
 * @version 2009-1-15
 * @see CustomerWrap
 * @since 1.0
 */
public class CustomerWrap extends CustomerBean
{
    private String outId = "";

    /**
     * ¼ÇÂ¼Ê±¼ä
     */
    private String logTime = "";

    public CustomerWrap()
    {}

    /**
     * @return the outId
     */
    public String getOutId()
    {
        return outId;
    }

    /**
     * @param outId
     *            the outId to set
     */
    public void setOutId(String outId)
    {
        this.outId = outId;
    }

    /**
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param logTime
     *            the logTime to set
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }
}
