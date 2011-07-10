/**
 * File Name: TravelApplyBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-10<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.tcp.constanst.TcpConstanst;


/**
 * 差旅费申请
 * 
 * @author ZHUZHU
 * @version 2011-7-10
 * @see TravelApplyBean
 * @since 3.0
 */
@Entity
@Table(name = "T_CENTER_TRAVELAPPLY")
public class TravelApplyBean implements Serializable
{
    @Id
    private String id = "";

    private String name = "";

    private String flowKey = "";

    @Join(tagClass = StafferBean.class)
    private String stafferId = "";

    @Join(tagClass = PrincipalshipBean.class)
    private String departmentId = "";

    private String logTime = "";

    private String srcCity = "";

    private String destCity = "";

    private String beginDate = "";

    private String endDate = "";

    private String description = "";

    private int status = TcpConstanst.TCP_STATUS_INIT;

    /**
     * 是否借款
     */
    private int borrow = TcpConstanst.TRAVELAPPLY_BORROW_NO;

    private long total = 0L;

    @Ignore
    private List<TravelApplyItemBean> itemList = null;

    @Ignore
    private List<TravelApplyPayBean> payList = null;

    @Ignore
    private List<TcpShareBean> shareList = null;

    /**
     * default constructor
     */
    public TravelApplyBean()
    {
    }

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
     * @return the stafferId
     */
    public String getStafferId()
    {
        return stafferId;
    }

    /**
     * @param stafferId
     *            the stafferId to set
     */
    public void setStafferId(String stafferId)
    {
        this.stafferId = stafferId;
    }

    /**
     * @return the departmentId
     */
    public String getDepartmentId()
    {
        return departmentId;
    }

    /**
     * @param departmentId
     *            the departmentId to set
     */
    public void setDepartmentId(String departmentId)
    {
        this.departmentId = departmentId;
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

    /**
     * @return the srcCity
     */
    public String getSrcCity()
    {
        return srcCity;
    }

    /**
     * @param srcCity
     *            the srcCity to set
     */
    public void setSrcCity(String srcCity)
    {
        this.srcCity = srcCity;
    }

    /**
     * @return the destCity
     */
    public String getDestCity()
    {
        return destCity;
    }

    /**
     * @param destCity
     *            the destCity to set
     */
    public void setDestCity(String destCity)
    {
        this.destCity = destCity;
    }

    /**
     * @return the beginDate
     */
    public String getBeginDate()
    {
        return beginDate;
    }

    /**
     * @param beginDate
     *            the beginDate to set
     */
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate()
    {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    /**
     * @return the borrow
     */
    public int getBorrow()
    {
        return borrow;
    }

    /**
     * @param borrow
     *            the borrow to set
     */
    public void setBorrow(int borrow)
    {
        this.borrow = borrow;
    }

    /**
     * @return the total
     */
    public long getTotal()
    {
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(long total)
    {
        this.total = total;
    }

    /**
     * @return the itemList
     */
    public List<TravelApplyItemBean> getItemList()
    {
        return itemList;
    }

    /**
     * @param itemList
     *            the itemList to set
     */
    public void setItemList(List<TravelApplyItemBean> itemList)
    {
        this.itemList = itemList;
    }

    /**
     * @return the payList
     */
    public List<TravelApplyPayBean> getPayList()
    {
        return payList;
    }

    /**
     * @param payList
     *            the payList to set
     */
    public void setPayList(List<TravelApplyPayBean> payList)
    {
        this.payList = payList;
    }

    /**
     * @return the shareList
     */
    public List<TcpShareBean> getShareList()
    {
        return shareList;
    }

    /**
     * @param shareList
     *            the shareList to set
     */
    public void setShareList(List<TcpShareBean> shareList)
    {
        this.shareList = shareList;
    }

    /**
     * @return the flowKey
     */
    public String getFlowKey()
    {
        return flowKey;
    }

    /**
     * @param flowKey
     *            the flowKey to set
     */
    public void setFlowKey(String flowKey)
    {
        this.flowKey = flowKey;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("TravelApplyBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("name = ")
            .append(this.name)
            .append(TAB)
            .append("flowKey = ")
            .append(this.flowKey)
            .append(TAB)
            .append("stafferId = ")
            .append(this.stafferId)
            .append(TAB)
            .append("departmentId = ")
            .append(this.departmentId)
            .append(TAB)
            .append("logTime = ")
            .append(this.logTime)
            .append(TAB)
            .append("srcCity = ")
            .append(this.srcCity)
            .append(TAB)
            .append("destCity = ")
            .append(this.destCity)
            .append(TAB)
            .append("beginDate = ")
            .append(this.beginDate)
            .append(TAB)
            .append("endDate = ")
            .append(this.endDate)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("borrow = ")
            .append(this.borrow)
            .append(TAB)
            .append("total = ")
            .append(this.total)
            .append(TAB)
            .append("itemList = ")
            .append(this.itemList)
            .append(TAB)
            .append("payList = ")
            .append(this.payList)
            .append(TAB)
            .append("shareList = ")
            .append(this.shareList)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
