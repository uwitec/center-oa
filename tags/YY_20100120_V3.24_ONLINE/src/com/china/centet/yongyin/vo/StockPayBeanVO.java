/**
 * File Name: StockPayBeanVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-1-17<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.StockPayBean;


/**
 * StockPayBeanVO
 * 
 * @author ZHUZHU
 * @version 2010-1-17
 * @see StockPayBeanVO
 * @since 1.0
 */
@Entity(inherit = true)
public class StockPayBeanVO extends StockPayBean
{
    @Relationship(relationField = "providerId")
    private String providerName = "";

    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Relationship(relationField = "providerId", tagField = "code")
    private String providerCode = "";

    /**
     * default constructor
     */
    public StockPayBeanVO()
    {}

    /**
     * @return the providerName
     */
    public String getProviderName()
    {
        return providerName;
    }

    /**
     * @param providerName
     *            the providerName to set
     */
    public void setProviderName(String providerName)
    {
        this.providerName = providerName;
    }

    /**
     * @return the providerCode
     */
    public String getProviderCode()
    {
        return providerCode;
    }

    /**
     * @param providerCode
     *            the providerCode to set
     */
    public void setProviderCode(String providerCode)
    {
        this.providerCode = providerCode;
    }

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
