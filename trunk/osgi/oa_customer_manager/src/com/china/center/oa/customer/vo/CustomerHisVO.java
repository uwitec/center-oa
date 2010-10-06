/**
 * File Name: CustomerVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.customer.bean.CustomerHisBean;


/**
 * CustomerVO
 * 
 * @author ZHUZHU
 * @version 2008-11-9
 * @see CustomerHisVO
 * @since 1.0
 */
@Entity(inherit = true)
public class CustomerHisVO extends CustomerHisBean
{
    @Relationship(relationField = "updaterId")
    private String updaterName = "";

    /**
     * default constructor
     */
    public CustomerHisVO()
    {
    }

    /**
     * @return the updaterName
     */
    public String getUpdaterName()
    {
        return updaterName;
    }

    /**
     * @param updaterName
     *            the updaterName to set
     */
    public void setUpdaterName(String updaterName)
    {
        this.updaterName = updaterName;
    }
}
