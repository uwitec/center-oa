/**
 * File Name: CustomerHisBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.bean;


import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.JoinType;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * CustomerHisBean
 * 
 * @author zhuzhu
 * @version 2008-11-9
 * @see CustomerHisBean
 * @since 1.0
 */
@Entity(inherit = true)
@Table(name = "T_CENTER_CUSTOMER_HIS")
public class CustomerHisBean extends AbstractBean
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    private String customerId = "";

    @Html(title = "¿Í»§Ãû³Æ", must = true, maxLength = 80)
    private String name = "";

    @Join(tagClass = StafferBean.class, type = JoinType.LEFT)
    private String updaterId = "";
    
    private int checkStatus = CustomerConstant.HIS_CHECK_NO;

    /**
     * default constructor
     */
    public CustomerHisBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId()
    {
        return customerId;
    }

    /**
     * @param customerId
     *            the customerId to set
     */
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    /**
     * @return the updaterId
     */
    public String getUpdaterId()
    {
        return updaterId;
    }

    /**
     * @param updaterId
     *            the updaterId to set
     */
    public void setUpdaterId(String updaterId)
    {
        this.updaterId = updaterId;
    }

    /**
     * @return the checkStatus
     */
    public int getCheckStatus()
    {
        return checkStatus;
    }

    /**
     * @param checkStatus the checkStatus to set
     */
    public void setCheckStatus(int checkStatus)
    {
        this.checkStatus = checkStatus;
    }
}
