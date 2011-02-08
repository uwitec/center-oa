/*
 * File Name: ConsignBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-14
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.bean;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;
import com.china.center.oa.sail.constanst.SailConstant;


/**
 * 发货单
 * 
 * @author ZHUZHU
 * @version 2008-1-14
 * @see
 * @since
 */
@Entity(name = "发货单")
@Table(name = "T_CENTER_OUTPRODUCT")
public class ConsignBean extends OutBean
{
    @Id
    private String fullId = "";

    private String transport = "";

    private String transportNo = "";

    /**
     * 发货单初始态
     */
    private int currentStatus = SailConstant.CONSIGN_INIT;

    /**
     * 发货单的回复类型 0:无回复 1:正常收货 2:异常收货
     */
    private int reprotType = SailConstant.CONSIGN_REPORT_INIT;

    /**
     * 是否满意 0:初始 1:满意 2:不满意
     */
    private int promitType = SailConstant.CONSIGN_PROMITTYPE_INIT;

    private String applys = "";

    private String arriveDate = "";

    private String checker = "";

    private String packager = "";

    private String packageTime = "";

    private String packageAmount = "";

    private String packageWeight = "";

    private String visitTime = "";

    /**
     * default constructor
     */
    public ConsignBean()
    {
    }

    /**
     * @return the fullId
     */
    public String getFullId()
    {
        return fullId;
    }

    /**
     * @return the transport
     */
    public String getTransport()
    {
        return transport;
    }

    /**
     * @return the currentStatus
     */
    public int getCurrentStatus()
    {
        return currentStatus;
    }

    /**
     * @return the applys
     */
    public String getApplys()
    {
        return applys;
    }

    /**
     * @param fullId
     *            the fullId to set
     */
    public void setFullId(String fullId)
    {
        this.fullId = fullId;
    }

    /**
     * @param transport
     *            the transport to set
     */
    public void setTransport(String transport)
    {
        this.transport = transport;
    }

    /**
     * @param currentStatus
     *            the currentStatus to set
     */
    public void setCurrentStatus(int currentStatus)
    {
        this.currentStatus = currentStatus;
    }

    /**
     * @param applys
     *            the applys to set
     */
    public void setApplys(String applys)
    {
        this.applys = applys;
    }

    /**
     * @return the reprotType
     */
    public int getReprotType()
    {
        return reprotType;
    }

    /**
     * @param reprotType
     *            the reprotType to set
     */
    public void setReprotType(int reprotType)
    {
        this.reprotType = reprotType;
    }

    /**
     * @return the transportNo
     */
    public String getTransportNo()
    {
        return transportNo;
    }

    /**
     * @param transportNo
     *            the transportNo to set
     */
    public void setTransportNo(String transportNo)
    {
        this.transportNo = transportNo;
    }

    /**
     * @return the arriveDate
     */
    public String getArriveDate()
    {
        return arriveDate;
    }

    /**
     * @param arriveDate
     *            the arriveDate to set
     */
    public void setArriveDate(String arriveDate)
    {
        this.arriveDate = arriveDate;
    }

    /**
     * @return the promitType
     */
    public int getPromitType()
    {
        return promitType;
    }

    /**
     * @param promitType
     *            the promitType to set
     */
    public void setPromitType(int promitType)
    {
        this.promitType = promitType;
    }

    /**
     * @return the checker
     */
    public String getChecker()
    {
        return checker;
    }

    /**
     * @param checker
     *            the checker to set
     */
    public void setChecker(String checker)
    {
        this.checker = checker;
    }

    /**
     * @return the packager
     */
    public String getPackager()
    {
        return packager;
    }

    /**
     * @param packager
     *            the packager to set
     */
    public void setPackager(String packager)
    {
        this.packager = packager;
    }

    /**
     * @return the packageTime
     */
    public String getPackageTime()
    {
        return packageTime;
    }

    /**
     * @param packageTime
     *            the packageTime to set
     */
    public void setPackageTime(String packageTime)
    {
        this.packageTime = packageTime;
    }

    /**
     * @return the packageAmount
     */
    public String getPackageAmount()
    {
        return packageAmount;
    }

    /**
     * @param packageAmount
     *            the packageAmount to set
     */
    public void setPackageAmount(String packageAmount)
    {
        this.packageAmount = packageAmount;
    }

    /**
     * @return the packageWeight
     */
    public String getPackageWeight()
    {
        return packageWeight;
    }

    /**
     * @param packageWeight
     *            the packageWeight to set
     */
    public void setPackageWeight(String packageWeight)
    {
        this.packageWeight = packageWeight;
    }

    /**
     * @return the visitTime
     */
    public String getVisitTime()
    {
        return visitTime;
    }

    /**
     * @param visitTime
     *            the visitTime to set
     */
    public void setVisitTime(String visitTime)
    {
        this.visitTime = visitTime;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuffer retValue = new StringBuffer();

        retValue.append("ConsignBean ( ").append(super.toString()).append(TAB).append("fullId = ").append(this.fullId).append(
            TAB).append("transport = ").append(this.transport).append(TAB).append("transportNo = ").append(
            this.transportNo).append(TAB).append("currentStatus = ").append(this.currentStatus).append(TAB).append(
            "reprotType = ").append(this.reprotType).append(TAB).append("promitType = ").append(this.promitType).append(
            TAB).append("applys = ").append(this.applys).append(TAB).append("arriveDate = ").append(this.arriveDate).append(
            TAB).append("checker = ").append(this.checker).append(TAB).append("packager = ").append(this.packager).append(
            TAB).append("packageTime = ").append(this.packageTime).append(TAB).append("packageAmount = ").append(
            this.packageAmount).append(TAB).append("packageWeight = ").append(this.packageWeight).append(TAB).append(
            "visitTime = ").append(this.visitTime).append(TAB).append(" )");

        return retValue.toString();
    }

}
