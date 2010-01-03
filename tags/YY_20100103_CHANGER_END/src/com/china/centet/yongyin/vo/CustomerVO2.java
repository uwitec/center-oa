/**
 * File Name: CustomerVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-4<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.vo;


import com.china.center.oa.customer.bean.CustomerBean;


/**
 * CustomerVO
 * 
 * @author zhuzhu
 * @version 2009-1-4
 * @see CustomerVO2
 * @since 1.0
 */
public class CustomerVO2 extends CustomerBean
{
    private String stafferName = "";

    public CustomerVO2()
    {}

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }
}
