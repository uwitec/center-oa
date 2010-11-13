package com.china.center.oa.sail.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.jdbc.annotation.Column;
import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.JoinType;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.tools.StringTools;


/**
 * 库单(销售单、入库单、出库单)
 * 
 * @author ZHUZHU
 * @version 2007-3-25
 * @see OutBean
 * @since 1.0
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
     * 库单类型 对于出入库都不一样(0:销售单 1:个人领样)
     */
    private int outType = OutConstant.OUTTYPE_OUT_COMMON;

    /**
     * 0:销售单 1:入库单
     */
    private int type = OutConstant.OUT_TYPE_OUTBILL;

    /**
     * 0:保存 1:提交 2:驳回 3:通过 4:会计审核通过 6:总经理审核通过 7:物流管理员通过<br>
     * (一般此通过即是销售单已经OK status in (3, 4))<br>
     * 预占库存 status in (1, 6, 7)
     */
    private int status = OutConstant.STATUS_SAVE;

    private String department = "";

    @Join(tagClass = CustomerBean.class, type = JoinType.LEFT)
    private String customerId = "";

    private String customerName = "";

    /**
     * 库单所在区域
     */
    @Join(tagClass = LocationBean.class)
    private String locationId = "";

    /**
     * 进出产品所在的仓库
     */
    @Join(tagClass = DepotBean.class)
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
    @Join(tagClass = DepotpartBean.class, type = JoinType.LEFT)
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
     * 目的仓库（入库单使用）
     */
    @Join(tagClass = DepotBean.class, alias = "des")
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
    private int reserve2 = OutConstant.OUT_CREDIT_COMMON;

    /**
     * 0:货到收款 1:款到发货(黑名单) 2:使用业务员信用额度
     */
    private int reserve3 = OutConstant.OUT_SAIL_TYPE_COMMON;

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
     * 应收的描述,当销售单结束的时候会填写
     */
    private String reserve7 = "";

    /**
     * 预留
     */
    private String reserve8 = "";

    /**
     * 预留
     */
    private String reserve9 = "";

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

    /**
     * @return the reserve7
     */
    public String getReserve7()
    {
        return reserve7;
    }

    /**
     * @param reserve7
     *            the reserve7 to set
     */
    public void setReserve7(String reserve7)
    {
        this.reserve7 = reserve7;
    }

    /**
     * @return the reserve8
     */
    public String getReserve8()
    {
        return reserve8;
    }

    /**
     * @param reserve8
     *            the reserve8 to set
     */
    public void setReserve8(String reserve8)
    {
        this.reserve8 = reserve8;
    }

    /**
     * @return the reserve9
     */
    public String getReserve9()
    {
        return reserve9;
    }

    /**
     * @param reserve9
     *            the reserve9 to set
     */
    public void setReserve9(String reserve9)
    {
        this.reserve9 = reserve9;
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
            .append("OutBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("fullId = ")
            .append(this.fullId)
            .append(TAB)
            .append("id = ")
            .append(this.id)
            .append(TAB)
            .append("flowId = ")
            .append(this.flowId)
            .append(TAB)
            .append("outTime = ")
            .append(this.outTime)
            .append(TAB)
            .append("managerTime = ")
            .append(this.managerTime)
            .append(TAB)
            .append("outType = ")
            .append(this.outType)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("status = ")
            .append(this.status)
            .append(TAB)
            .append("department = ")
            .append(this.department)
            .append(TAB)
            .append("customerId = ")
            .append(this.customerId)
            .append(TAB)
            .append("customerName = ")
            .append(this.customerName)
            .append(TAB)
            .append("locationId = ")
            .append(this.locationId)
            .append(TAB)
            .append("location = ")
            .append(this.location)
            .append(TAB)
            .append("connector = ")
            .append(this.connector)
            .append(TAB)
            .append("phone = ")
            .append(this.phone)
            .append(TAB)
            .append("stafferName = ")
            .append(this.stafferName)
            .append(TAB)
            .append("stafferId = ")
            .append(this.stafferId)
            .append(TAB)
            .append("total = ")
            .append(this.total)
            .append(TAB)
            .append("description = ")
            .append(this.description)
            .append(TAB)
            .append("checks = ")
            .append(this.checks)
            .append(TAB)
            .append("reday = ")
            .append(this.reday)
            .append(TAB)
            .append("redate = ")
            .append(this.redate)
            .append(TAB)
            .append("depotpartId = ")
            .append(this.depotpartId)
            .append(TAB)
            .append("marks = ")
            .append(this.marks)
            .append(TAB)
            .append("mark = ")
            .append(this.mark)
            .append(TAB)
            .append("consign = ")
            .append(this.consign)
            .append(TAB)
            .append("pay = ")
            .append(this.pay)
            .append(TAB)
            .append("inway = ")
            .append(this.inway)
            .append(TAB)
            .append("tempType = ")
            .append(this.tempType)
            .append(TAB)
            .append("hadPay = ")
            .append(this.hadPay)
            .append(TAB)
            .append("arriveDate = ")
            .append(this.arriveDate)
            .append(TAB)
            .append("destinationId = ")
            .append(this.destinationId)
            .append(TAB)
            .append("refOutFullId = ")
            .append(this.refOutFullId)
            .append(TAB)
            .append("tranNo = ")
            .append(this.tranNo)
            .append(TAB)
            .append("reserve1 = ")
            .append(this.reserve1)
            .append(TAB)
            .append("reserve2 = ")
            .append(this.reserve2)
            .append(TAB)
            .append("reserve3 = ")
            .append(this.reserve3)
            .append(TAB)
            .append("reserve4 = ")
            .append(this.reserve4)
            .append(TAB)
            .append("reserve5 = ")
            .append(this.reserve5)
            .append(TAB)
            .append("reserve6 = ")
            .append(this.reserve6)
            .append(TAB)
            .append("reserve7 = ")
            .append(this.reserve7)
            .append(TAB)
            .append("reserve8 = ")
            .append(this.reserve8)
            .append(TAB)
            .append("reserve9 = ")
            .append(this.reserve9)
            .append(TAB)
            .append("curcredit = ")
            .append(this.curcredit)
            .append(TAB)
            .append("staffcredit = ")
            .append(this.staffcredit)
            .append(TAB)
            .append("baseList = ")
            .append(this.baseList)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}
