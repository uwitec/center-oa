/*
 * File Name: ConsignBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-14
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.centet.yongyin.constant.Constant;


/**
 * 发货单
 * 
 * @author zhuzhu
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

    private int currentStatus = Constant.CONSIGN_INIT;

    /**
     * 发货单的回复类型 0:无回复 1:正常收货 2:异常收货
     */
    private int reprotType = Constant.CONSIGN_REPORT_INIT;

    private String applys = "";

    /**
     * default constructor
     */
    public ConsignBean()
    {}

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

}
