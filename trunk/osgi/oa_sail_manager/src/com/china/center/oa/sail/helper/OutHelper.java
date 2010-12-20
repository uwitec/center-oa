/*
 * File Name: OutBeanHelper.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-14
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.helper;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.china.center.common.taglib.DefinedCommon;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * @author ZHUZHU
 * @version 2007-8-14
 * @see
 * @since 1.0
 */
public abstract class OutHelper
{
    public static String createTable(List<BaseBean> list, double tatol)
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<table width='100%' border='0' cellspacing='1'>");
        buffer.append("<tr align='center' class='content0'>");
        buffer.append("<td width='20%' align='center'>品名</td>");
        buffer.append("<td width='5%' align='center'>单位</td>");
        buffer.append("<td width='10%' align='center'>数量</td>");
        buffer.append("<td width='15%' align='center'>单价</td>");
        buffer.append("<td width='20%' align='left'>金额(总计:<span id='total'>"
                      + MathTools.formatNum(tatol) + "</span>)</td>");
        buffer.append("<td width='25%' align='center'>成本</td></tr>");

        int index = 0;
        String cls = null;
        for (BaseBean bean : list)
        {
            if (index % 2 == 0)
            {
                cls = "content1";
            }
            else
            {
                cls = "content2";
            }

            buffer.append("<tr class='" + cls + "'>");

            buffer.append("<td width='20%' align='center'>" + bean.getProductName() + "</td>");
            buffer.append("<td width='5%' align='center'>" + bean.getUnit() + "</td>");
            buffer.append("<td width='10%' align='center'>" + bean.getAmount() + "</td>");
            buffer.append(" <td width='15%' align='center'>" + MathTools.formatNum(bean.getPrice())
                          + "</td>");
            buffer.append("<td width='15%' align='center'>" + MathTools.formatNum(bean.getValue())
                          + "</td>");
            buffer.append("<td width='25%' align='center'>"
                          + StringTools.print(bean.getDescription()) + "</td>");
            index++ ;
        }

        buffer.append("</table>");

        return buffer.toString();
    }

    public static void getBean(ResultSet rst, OutBean outBean)
        throws SQLException
    {
        outBean.setId(rst.getString("id"));
        outBean.setFullId(rst.getString("fullId"));
        outBean.setStafferName(rst.getString("stafferName"));
        outBean.setCustomerName(rst.getString("customerName"));
        outBean.setStatus(rst.getInt("status"));
        outBean.setOutType(rst.getInt("outType"));

        outBean.setOutTime(TimeTools.getStringBySqlDate(rst.getDate("outTime")));

        outBean.setDepartment(rst.getString("department"));
        outBean.setDescription(rst.getString("description"));
        outBean.setTotal(rst.getDouble("total"));
        outBean.setPhone(rst.getString("phone"));
        outBean.setConnector(rst.getString("connector"));
        outBean.setCustomerId(rst.getString("customerId"));
        outBean.setType(rst.getInt("type"));
        outBean.setMarks(rst.getInt("mark"));
        outBean.setPay(rst.getInt("pay"));
        outBean.setChecks(rst.getString("checks"));

        String kk = rst.getString("reday");

        outBean.setReday(MathTools.parseInt(kk));

        outBean.setRedate(rst.getString("redate"));

        outBean.setTempType(rst.getInt("tempType"));

        outBean.setHadPay(rst.getString("hadPay"));
    }

    public static String getStatus(int i)
    {
        return getStatus(i, true);
    }

    /**
     * getStatus
     * 
     * @param i
     * @param color
     * @return
     */
    public static String getStatus(int i, boolean color)
    {
        return DefinedCommon.getValue("outStatus", i);
    }

    public static boolean canDelete(OutBean outBean)
    {
        int status = outBean.getStatus();

        if (status == OutConstant.STATUS_SAVE || status == OutConstant.STATUS_REJECT)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean canSubmit(OutBean outBean)
    {
        if (outBean.getStatus() != OutConstant.STATUS_SAVE
            && outBean.getStatus() != OutConstant.STATUS_REJECT
            && outBean.getStatus() != OutConstant.STATUS_LOCATION_MANAGER_CHECK)
        {
            return false;
        }

        return true;
    }

    public static boolean canReject(OutBean outBean)
    {
        int status = outBean.getStatus();

        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            if (status == OutConstant.STATUS_SAVE || status == OutConstant.STATUS_REJECT
                || status == OutConstant.STATUS_PASS || status == OutConstant.STATUS_SEC_PASS)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL)
        {
            if (outBean.getStatus() == OutConstant.STATUS_LOCATION_MANAGER_CHECK)
            {
                return true;
            }

            if (outBean.getStatus() == OutConstant.STATUS_CEO_CHECK)
            {
                return true;
            }

            if (outBean.getStatus() == OutConstant.STATUS_CHAIRMA_CHECK)
            {
                return true;
            }

            // 调动分为两个单据 一个是源头的调出入库单 一个是目的的调入入库单
            // 源头调出后状态处于提交态,此时可以驳回的
            if (outBean.getStatus() == OutConstant.STATUS_SUBMIT
                && outBean.getType() == OutConstant.OUTTYPE_IN_MOVEOUT)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    /**
     * 是否调入
     * 
     * @param outBean
     * @return
     */
    public static boolean isMoveIn(OutBean outBean)
    {
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT
            && outBean.getReserve1() == OutConstant.MOVEOUT_IN)
        {
            return true;
        }

        return false;
    }

    /**
     * 是否调出
     * 
     * @param outBean
     * @return
     */
    public static boolean isMoveOut(OutBean outBean)
    {
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT
            && outBean.getReserve1() == OutConstant.MOVEOUT_OUT)
        {
            return true;
        }

        return false;
    }
}
