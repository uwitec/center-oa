/*
 * File Name: InBean.java CopyRight: Copyright by www.center.china Description:
 * Creater: zhuAchen CreateTime: 2007-3-25 Grant: open source to everybody
 */
package com.china.center.oa.credit.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.jdbc.annotation.Column;
import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Table;
import com.china.center.oa.publics.bean.BaseBean;
import com.china.center.tools.StringTools;


/**
 * TODO_OSGI 入库的bean(这里仅仅是为了解耦使用,后面采用其他方式引入)
 * 
 * @author ZHUZHU
 * @version 2007-3-25
 * @see
 * @since
 */
@Entity
@Table(name = "T_CENTER_OUT")
public class OutBean implements Serializable
{
    @Id
    private String fullId = "";

    private String id = "";

    /**
     * 流程ID
     */
    private String flowId = "";

    private String outTime = "";

    /**
     * 总经理通过时间
     */
    private String managerTime = "";

    /**
     * 库单类型 对于出入库都不一样
     */
    private int outType = 0;

    /**
     * 0:出库 1:入库
     */
    private int type = 0;

    /**
     * 0:保存 1:提交 2:驳回 3:通过 4:会计审核通过 6:总经理审核通过 7:物流管理员通过<br>
     * (一般此通过即是销售单已经OK status in (3, 4))<br>
     * 预占库存 status in (1, 6, 7)
     */
    private int status = 0;

    private String department = "";

    private String customerId = "";

    private String customerName = "";

    private String locationId = "";

    /**
     * 进出产品所在的区域
     */
    private String location = "";

    private String connector = "";

    private String phone = "";

    private String stafferName = "";

    private String stafferId = "";

    private double total = 0.0d;

    private String description = "";

    private String checks = "";

    private int reday = 0;

    private String redate = "";

    /**
     * 入库单的入库仓区
     */
    private String depotpartId = "";

    @Column(name = "mark")
    private int marks = 0;

    @Ignore
    private boolean mark = false;

    @Ignore
    private int consign = 0;

    /**
     * 0:没有付款 1:付款
     */
    private int pay = 0;

    /**
     * 0：非在途 1：在途（入库单使用 调出）
     */
    private int inway = 0;

    /**
     * 超期天数 如果未超期就是0
     */
    private int tempType = 0;

    /**
     * 已经支付的金额
     */
    private String hadPay = "0.0";

    private String arriveDate = "";

    /**
     * 目的区域（入库单使用）
     */
    private String destinationId = "0";

    /**
     * 调出的相关OUT
     */
    private String refOutFullId = "";

    /**
     * 调出的时候强制需要运单号
     */
    private String tranNo = "";

    /**
     * 销售单是否处理
     */
    private int reserve1 = 0;

    /**
     * 客户是否超支(0:没有 1:超支)
     */
    private int reserve2 = 0;

    /**
     * 0:货到收款 1:款到发货(黑名单) 2:使用业务员信用额度
     */
    private int reserve3 = 0;

    /**
     * 上次已经扣除了一部分了
     */
    private String reserve4 = "";

    /**
     * 当前已经延期级别
     */
    private String reserve5 = "";

    /**
     * 信用超支日志
     */
    private String reserve6 = "";

    /**
     * 预占客户信用等级金额(优先使用客户的)
     */
    private double curcredit = 0.0d;

    /**
     * 预占职员信用等级金额
     */
    private double staffcredit = 0.0d;

    @Ignore
    private List<BaseBean> baseList = null;

    /**
     * default constructor
     */
    public OutBean()
    {
    }

    /**
     * @return the baseList
     */
    public List<BaseBean> getBaseList()
    {
        return baseList;
    }

    /**
     * @param baseList
     *            the baseList to set
     */
    public void setBaseList(List<BaseBean> baseList)
    {
        this.baseList = baseList;
    }

    /**
     * @return the department
     */
    public String getDepartment()
    {
        return department;
    }

    /**
     * @param department
     *            the department to set
     */
    public void setDepartment(String department)
    {
        this.department = department;
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
     * @return the fullId
     */
    public String getFullId()
    {
        return fullId;
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
     * @return the total
     */
    public double getTotal()
    {
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(double total)
    {
        this.total = total;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     * @param customerName
     *            the customerName to set
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    /**
     * @return the connector
     */
    public String getConnector()
    {
        return connector;
    }

    /**
     * @param connector
     *            the connector to set
     */
    public void setConnector(String connector)
    {
        this.connector = connector;
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
     * @return the outTime
     */
    public String getOutTime()
    {
        return outTime;
    }

    /**
     * @param outTime
     *            the outTime to set
     */
    public void setOutTime(String outTime)
    {
        this.outTime = outTime;
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

    /**
     * @return the outType
     */
    public int getOutType()
    {
        return outType;
    }

    /**
     * @param outType
     *            the outType to set
     */
    public void setOutType(int outType)
    {
        this.outType = outType;
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
     * @return the phone
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * @param phone
     *            the phone to set
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @return the checks
     */
    public String getChecks()
    {
        return checks;
    }

    /**
     * @param checks
     *            the checks to set
     */
    public void setChecks(String checks)
    {
        if (checks != null)
        {
            this.checks = checks;
        }
    }

    /**
     * @return the reday
     */
    public int getReday()
    {
        return reday;
    }

    /**
     * @param reday
     *            the reday to set
     */
    public void setReday(int reday)
    {
        this.reday = reday;
    }

    /**
     * @return the redate
     */
    public String getRedate()
    {
        return redate;
    }

    /**
     * @param redate
     *            the redate to set
     */
    public void setRedate(String redate)
    {
        this.redate = redate;
    }

    /**
     * @return the pay
     */
    public int getPay()
    {
        return pay;
    }

    /**
     * @param pay
     *            the pay to set
     */
    public void setPay(int pay)
    {
        this.pay = pay;
    }

    /**
     * @return the tempType
     */
    public int getTempType()
    {
        return tempType;
    }

    /**
     * @param tempType
     *            the tempType to set
     */
    public void setTempType(int tempType)
    {
        this.tempType = tempType;
    }

    /**
     * @return the hadPay
     */
    public String getHadPay()
    {
        return hadPay;
    }

    /**
     * @param hadPay
     *            the hadPay to set
     */
    public void setHadPay(String hadPay)
    {
        if ( !StringTools.isNullOrNone(hadPay))
        {
            this.hadPay = hadPay;
        }
    }

    public void addPay(double money)
    {
        double value = money + Double.parseDouble(this.hadPay);

        setHadPay(String.valueOf(value));
    }

    /**
     * @return the flowId
     */
    public String getFlowId()
    {
        return flowId;
    }

    /**
     * @param flowId
     *            the flowId to set
     */
    public void setFlowId(String flowId)
    {
        this.flowId = flowId;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param locationId
     *            the locationId to set
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
    }

    /**
     * @return the marks
     */
    public int getMarks()
    {
        return marks;
    }

    /**
     * @param marks
     *            the marks to set
     */
    public void setMarks(int marks)
    {
        if (marks == 0)
        {
            this.setMark(false);
        }
        else
        {
            this.setMark(true);
        }

        this.marks = marks;
    }

    /**
     * @return the mark
     */
    public boolean isMark()
    {
        return mark;
    }

    /**
     * @param mark
     *            the mark to set
     */
    public void setMark(boolean mark)
    {
        this.mark = mark;
    }

    /**
     * @return 返回 arriveDate
     */
    public String getArriveDate()
    {
        return arriveDate;
    }

    /**
     * @param 对arriveDate进行赋值
     */
    public void setArriveDate(String arriveDate)
    {
        this.arriveDate = arriveDate;
    }

    /**
     * @return 返回 location
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * @param 对location进行赋值
     */
    public void setLocation(String location)
    {
        this.location = location;
    }

    /**
     * @return the depotpartId
     */
    public String getDepotpartId()
    {
        return depotpartId;
    }

    /**
     * @param depotpartId
     *            the depotpartId to set
     */
    public void setDepotpartId(String depotpartId)
    {
        this.depotpartId = depotpartId;
    }

    /**
     * @return the destinationId
     */
    public String getDestinationId()
    {
        return destinationId;
    }

    /**
     * @param destinationId
     *            the destinationId to set
     */
    public void setDestinationId(String destinationId)
    {
        this.destinationId = destinationId;
    }

    /**
     * @return the refOutFullId
     */
    public String getRefOutFullId()
    {
        return refOutFullId;
    }

    /**
     * @param refOutFullId
     *            the refOutFullId to set
     */
    public void setRefOutFullId(String refOutFullId)
    {
        this.refOutFullId = refOutFullId;
    }

    /**
     * @return the inway
     */
    public int getInway()
    {
        return inway;
    }

    /**
     * @param inway
     *            the inway to set
     */
    public void setInway(int inway)
    {
        this.inway = inway;
    }

    /**
     * @return the consign
     */
    public int getConsign()
    {
        return consign;
    }

    /**
     * @param consign
     *            the consign to set
     */
    public void setConsign(int consign)
    {
        this.consign = consign;
    }

    /**
     * @return the tranNo
     */
    public String getTranNo()
    {
        return tranNo;
    }

    /**
     * @param tranNo
     *            the tranNo to set
     */
    public void setTranNo(String tranNo)
    {
        this.tranNo = tranNo;
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
     * @return the reserve1
     */
    public int getReserve1()
    {
        return reserve1;
    }

    /**
     * @param reserve1
     *            the reserve1 to set
     */
    public void setReserve1(int reserve1)
    {
        this.reserve1 = reserve1;
    }

    /**
     * @return the reserve2
     */
    public int getReserve2()
    {
        return reserve2;
    }

    /**
     * @param reserve2
     *            the reserve2 to set
     */
    public void setReserve2(int reserve2)
    {
        this.reserve2 = reserve2;
    }

    /**
     * @return the reserve3
     */
    public int getReserve3()
    {
        return reserve3;
    }

    /**
     * @param reserve3
     *            the reserve3 to set
     */
    public void setReserve3(int reserve3)
    {
        this.reserve3 = reserve3;
    }

    /**
     * @return the reserve4
     */
    public String getReserve4()
    {
        return reserve4;
    }

    /**
     * @param reserve4
     *            the reserve4 to set
     */
    public void setReserve4(String reserve4)
    {
        this.reserve4 = reserve4;
    }

    /**
     * @return the reserve5
     */
    public String getReserve5()
    {
        return reserve5;
    }

    /**
     * @param reserve5
     *            the reserve5 to set
     */
    public void setReserve5(String reserve5)
    {
        this.reserve5 = reserve5;
    }

    /**
     * @return the reserve6
     */
    public String getReserve6()
    {
        return reserve6;
    }

    /**
     * @param reserve6
     *            the reserve6 to set
     */
    public void setReserve6(String reserve6)
    {
        this.reserve6 = reserve6;
    }

    /**
     * @return the curcredit
     */
    public double getCurcredit()
    {
        return curcredit;
    }

    /**
     * @param curcredit
     *            the curcredit to set
     */
    public void setCurcredit(double curcredit)
    {
        this.curcredit = curcredit;
    }

    /**
     * @return the staffcredit
     */
    public double getStaffcredit()
    {
        return staffcredit;
    }

    /**
     * @param staffcredit
     *            the staffcredit to set
     */
    public void setStaffcredit(double staffcredit)
    {
        this.staffcredit = staffcredit;
    }

    /**
     * @return the managerTime
     */
    public String getManagerTime()
    {
        return managerTime;
    }

    /**
     * @param managerTime
     *            the managerTime to set
     */
    public void setManagerTime(String managerTime)
    {
        this.managerTime = managerTime;
    }
}
