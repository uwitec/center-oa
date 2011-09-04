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
import com.china.center.jdbc.annotation.Html;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Join;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.enums.Element;
import com.china.center.oa.publics.bean.AttachmentBean;
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

    @Html(title = "目的", must = true, maxLength = 40)
    private String name = "";

    private String flowKey = "";

    @Html(title = "申请人", name = "stafferName", must = true, maxLength = 40, readonly = true)
    @Join(tagClass = StafferBean.class)
    private String stafferId = "";

    @Html(title = "部门", name = "departmentName", must = true, maxLength = 40, readonly = true)
    @Join(tagClass = PrincipalshipBean.class)
    private String departmentId = "";

    private String logTime = "";

    @Html(title = "出发城市", must = true, readonly = true)
    private String srcCity = "";

    @Html(title = "目的城市", must = true, readonly = true)
    private String destCity = "";

    @Html(title = "开始日期", must = true, type = Element.DATE)
    private String beginDate = "";

    @Html(title = "结束日期", must = true, type = Element.DATE)
    private String endDate = "";

    @Html(title = "申请事由", maxLength = 200, must = true, type = Element.TEXTAREA)
    private String description = "";

    @Html(title = "状态", must = true, type = Element.SELECT)
    private int status = TcpConstanst.TCP_STATUS_INIT;

    /**
     * 是否借款
     */
    @Html(title = "借款", must = true, type = Element.SELECT)
    private int borrow = TcpConstanst.TRAVELAPPLY_BORROW_NO;

    /**
     * 飞机票-其他费用二需要合计到差旅费里面
     */
    @Html(title = "飞机票", must = true, type = Element.DOUBLE)
    private long airplaneCharges = 0L;

    @Html(title = "火车票", must = true, type = Element.DOUBLE)
    private long trainCharges = 0L;

    @Html(title = "汽车票", must = true, type = Element.DOUBLE)
    private long busCharges = 0L;

    @Html(title = "住宿费", must = true, type = Element.DOUBLE)
    private long hotelCharges = 0L;

    @Html(title = "业务招待费", must = true, type = Element.DOUBLE)
    private long entertainCharges = 0L;

    @Html(title = "补贴", must = true, type = Element.DOUBLE)
    private long allowanceCharges = 0L;

    @Html(title = "其他费用一", must = true, type = Element.DOUBLE)
    private long other1Charges = 0L;

    @Html(title = "其他费用二", must = true, type = Element.DOUBLE)
    private long other2Charges = 0L;

    /**
     * 准确到分
     */
    private long total = 0L;

    /**
     * 借款总金额
     */
    private long borrowTotal = 0L;

    /**
     * 费用子项
     */
    @Ignore
    private List<TravelApplyItemBean> itemList = null;

    /**
     * 付款列表
     */
    @Ignore
    private List<TravelApplyPayBean> payList = null;

    /**
     * 分担列表
     */
    @Ignore
    private List<TcpShareBean> shareList = null;

    /**
     * 附件列表
     */
    @Ignore
    private List<AttachmentBean> attachmentList = null;

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
     * @return the attachmentList
     */
    public List<AttachmentBean> getAttachmentList()
    {
        return attachmentList;
    }

    /**
     * @param attachmentList
     *            the attachmentList to set
     */
    public void setAttachmentList(List<AttachmentBean> attachmentList)
    {
        this.attachmentList = attachmentList;
    }

    /**
     * @return the airplaneCharges
     */
    public long getAirplaneCharges()
    {
        return airplaneCharges;
    }

    /**
     * @param airplaneCharges
     *            the airplaneCharges to set
     */
    public void setAirplaneCharges(long airplaneCharges)
    {
        this.airplaneCharges = airplaneCharges;
    }

    /**
     * @return the trainCharges
     */
    public long getTrainCharges()
    {
        return trainCharges;
    }

    /**
     * @param trainCharges
     *            the trainCharges to set
     */
    public void setTrainCharges(long trainCharges)
    {
        this.trainCharges = trainCharges;
    }

    /**
     * @return the busCharges
     */
    public long getBusCharges()
    {
        return busCharges;
    }

    /**
     * @param busCharges
     *            the busCharges to set
     */
    public void setBusCharges(long busCharges)
    {
        this.busCharges = busCharges;
    }

    /**
     * @return the hotelCharges
     */
    public long getHotelCharges()
    {
        return hotelCharges;
    }

    /**
     * @param hotelCharges
     *            the hotelCharges to set
     */
    public void setHotelCharges(long hotelCharges)
    {
        this.hotelCharges = hotelCharges;
    }

    /**
     * @return the entertainCharges
     */
    public long getEntertainCharges()
    {
        return entertainCharges;
    }

    /**
     * @param entertainCharges
     *            the entertainCharges to set
     */
    public void setEntertainCharges(long entertainCharges)
    {
        this.entertainCharges = entertainCharges;
    }

    /**
     * @return the allowanceCharges
     */
    public long getAllowanceCharges()
    {
        return allowanceCharges;
    }

    /**
     * @param allowanceCharges
     *            the allowanceCharges to set
     */
    public void setAllowanceCharges(long allowanceCharges)
    {
        this.allowanceCharges = allowanceCharges;
    }

    /**
     * @return the other1Charges
     */
    public long getOther1Charges()
    {
        return other1Charges;
    }

    /**
     * @param other1Charges
     *            the other1Charges to set
     */
    public void setOther1Charges(long other1Charges)
    {
        this.other1Charges = other1Charges;
    }

    /**
     * @return the other2Charges
     */
    public long getOther2Charges()
    {
        return other2Charges;
    }

    /**
     * @param other2Charges
     *            the other2Charges to set
     */
    public void setOther2Charges(long other2Charges)
    {
        this.other2Charges = other2Charges;
    }

    /**
     * @return the borrowTotal
     */
    public long getBorrowTotal()
    {
        return borrowTotal;
    }

    /**
     * @param borrowTotal
     *            the borrowTotal to set
     */
    public void setBorrowTotal(long borrowTotal)
    {
        this.borrowTotal = borrowTotal;
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
            .append("airplaneCharges = ")
            .append(this.airplaneCharges)
            .append(TAB)
            .append("trainCharges = ")
            .append(this.trainCharges)
            .append(TAB)
            .append("busCharges = ")
            .append(this.busCharges)
            .append(TAB)
            .append("hotelCharges = ")
            .append(this.hotelCharges)
            .append(TAB)
            .append("entertainCharges = ")
            .append(this.entertainCharges)
            .append(TAB)
            .append("allowanceCharges = ")
            .append(this.allowanceCharges)
            .append(TAB)
            .append("other1Charges = ")
            .append(this.other1Charges)
            .append(TAB)
            .append("other2Charges = ")
            .append(this.other2Charges)
            .append(TAB)
            .append("total = ")
            .append(this.total)
            .append(TAB)
            .append("borrowTotal = ")
            .append(this.borrowTotal)
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
            .append("attachmentList = ")
            .append(this.attachmentList)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

}
