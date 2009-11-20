/**
 * File Name: Make01Bean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.oa.constant.MakeConstant;


/**
 * Make01Bean
 * 
 * @author ZHUZHU
 * @version 2009-10-8
 * @see Make01Bean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_MAKE_01")
public class Make01Bean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "客户名称", must = true, maxLength = 100, readonly = true)
    private String cname = "";

    private String cid = "";

    @Html(title = "制作背景", type = Element.TEXTAREA, maxLength = 100)
    private String description = "";

    @Html(title = "样品出货期", must = true, type = Element.DATE)
    private String endTime = "";

    @Html(title = "样品物流方式", must = true, maxLength = 100)
    private String flowTypeName = "";

    @Html(title = "大货出货期", must = true, type = Element.DATE)
    private String endTime2 = "";

    @Html(title = "大货物流方式", must = true, maxLength = 100)
    private String flowTypeName2 = "";

    @Html(title = "材质", must = true, maxLength = 100)
    private String charact = "";

    @Html(title = "定作物方概况描述", must = true, type = Element.TEXTAREA)
    private String cdes = "";

    @Html(title = "证书落款", maxLength = 200)
    private String certificate = "";

    @Html(title = "客户需求", must = true, type = Element.SELECT)
    private int request = MakeConstant.REQUEST_TYPE_00;

    @Html(title = "数量", must = true, oncheck = JCheck.ONLY_NUMBER)
    private int amount = 0;

    /**
     * 是否打样
     */
    @Html(title = "是否打样", type = Element.SELECT)
    private int sampleType = MakeConstant.SAMPLETYPE_YES;

    @Html(title = "预期结款方式", type = Element.SELECT)
    private int billType = MakeConstant.BILLTYPE_DIRECT;

    @Html(title = "客户类型", type = Element.SELECT)
    private int customerType = MakeConstant.CUSTOMERTYPE_COMMON;

    @Html(title = "是否估价", type = Element.SELECT)
    private int appraisalType = MakeConstant.APPRAISALTYPE_YES;

    @Html(title = "报价部门", type = Element.SELECT)
    private int gujiaType = MakeConstant.GUJIATYPE_CENTER;

    @Html(title = "设计", type = Element.SELECT)
    private int designType = MakeConstant.DESIGNTYPE_INIT;

    @Html(title = "询价类型", type = Element.SELECT, must = true)
    private int appType = MakeConstant.APP_TYPE_00;

    /**
     * default constructor
     */
    public Make01Bean()
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
     * @return the cname
     */
    public String getCname()
    {
        return cname;
    }

    /**
     * @param cname
     *            the cname to set
     */
    public void setCname(String cname)
    {
        this.cname = cname;
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
     * @return the endTime
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @return the charact
     */
    public String getCharact()
    {
        return charact;
    }

    /**
     * @param charact
     *            the charact to set
     */
    public void setCharact(String charact)
    {
        this.charact = charact;
    }

    /**
     * @return the cdes
     */
    public String getCdes()
    {
        return cdes;
    }

    /**
     * @param cdes
     *            the cdes to set
     */
    public void setCdes(String cdes)
    {
        this.cdes = cdes;
    }

    /**
     * @return the amount
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * @return the sampleType
     */
    public int getSampleType()
    {
        return sampleType;
    }

    /**
     * @param sampleType
     *            the sampleType to set
     */
    public void setSampleType(int sampleType)
    {
        this.sampleType = sampleType;
    }

    /**
     * @return the billType
     */
    public int getBillType()
    {
        return billType;
    }

    /**
     * @param billType
     *            the billType to set
     */
    public void setBillType(int billType)
    {
        this.billType = billType;
    }

    /**
     * @return the customerType
     */
    public int getCustomerType()
    {
        return customerType;
    }

    /**
     * @param customerType
     *            the customerType to set
     */
    public void setCustomerType(int customerType)
    {
        this.customerType = customerType;
    }

    /**
     * @return the appraisalType
     */
    public int getAppraisalType()
    {
        return appraisalType;
    }

    /**
     * @param appraisalType
     *            the appraisalType to set
     */
    public void setAppraisalType(int appraisalType)
    {
        this.appraisalType = appraisalType;
    }

    /**
     * @return the designType
     */
    public int getDesignType()
    {
        return designType;
    }

    /**
     * @param designType
     *            the designType to set
     */
    public void setDesignType(int designType)
    {
        this.designType = designType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( (id == null) ? 0 : id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if ( ! (obj instanceof Make01Bean)) return false;
        final Make01Bean other = (Make01Bean)obj;
        if (id == null)
        {
            if (other.id != null) return false;
        }
        else if ( !id.equals(other.id)) return false;
        return true;
    }

    /**
     * @return the cid
     */
    public String getCid()
    {
        return cid;
    }

    /**
     * @param cid
     *            the cid to set
     */
    public void setCid(String cid)
    {
        this.cid = cid;
    }

    /**
     * @return the certificate
     */
    public String getCertificate()
    {
        return certificate;
    }

    /**
     * @param certificate
     *            the certificate to set
     */
    public void setCertificate(String certificate)
    {
        this.certificate = certificate;
    }

    /**
     * @return the appType
     */
    public int getAppType()
    {
        return appType;
    }

    /**
     * @param appType
     *            the appType to set
     */
    public void setAppType(int appType)
    {
        this.appType = appType;
    }

    /**
     * @return the gujiaType
     */
    public int getGujiaType()
    {
        return gujiaType;
    }

    /**
     * @param gujiaType
     *            the gujiaType to set
     */
    public void setGujiaType(int gujiaType)
    {
        this.gujiaType = gujiaType;
    }

    /**
     * @return the request
     */
    public int getRequest()
    {
        return request;
    }

    /**
     * @param request
     *            the request to set
     */
    public void setRequest(int request)
    {
        this.request = request;
    }

    /**
     * @return the endTime2
     */
    public String getEndTime2()
    {
        return endTime2;
    }

    /**
     * @param endTime2
     *            the endTime2 to set
     */
    public void setEndTime2(String endTime2)
    {
        this.endTime2 = endTime2;
    }

    /**
     * @return the flowTypeName
     */
    public String getFlowTypeName()
    {
        return flowTypeName;
    }

    /**
     * @param flowTypeName
     *            the flowTypeName to set
     */
    public void setFlowTypeName(String flowTypeName)
    {
        this.flowTypeName = flowTypeName;
    }

    /**
     * @return the flowTypeName2
     */
    public String getFlowTypeName2()
    {
        return flowTypeName2;
    }

    /**
     * @param flowTypeName2
     *            the flowTypeName2 to set
     */
    public void setFlowTypeName2(String flowTypeName2)
    {
        this.flowTypeName2 = flowTypeName2;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String tab = ",";

        StringBuilder retValue = new StringBuilder();

        retValue.append("Make01Bean ( ").append(super.toString()).append(tab).append("id = ").append(
            this.id).append(tab).append("cname = ").append(this.cname).append(tab).append("cid = ").append(
            this.cid).append(tab).append("description = ").append(this.description).append(tab).append(
            "endTime = ").append(this.endTime).append(tab).append("flowTypeName = ").append(
            this.flowTypeName).append(tab).append("endTime2 = ").append(this.endTime2).append(tab).append(
            "flowTypeName2 = ").append(this.flowTypeName2).append(tab).append("charact = ").append(
            this.charact).append(tab).append("cdes = ").append(this.cdes).append(tab).append(
            "certificate = ").append(this.certificate).append(tab).append("request = ").append(
            this.request).append(tab).append("amount = ").append(this.amount).append(tab).append(
            "sampleType = ").append(this.sampleType).append(tab).append("billType = ").append(
            this.billType).append(tab).append("customerType = ").append(this.customerType).append(
            tab).append("appraisalType = ").append(this.appraisalType).append(tab).append(
            "gujiaType = ").append(this.gujiaType).append(tab).append("designType = ").append(
            this.designType).append(tab).append("appType = ").append(this.appType).append(tab).append(
            " )");

        return retValue.toString();
    }

}
