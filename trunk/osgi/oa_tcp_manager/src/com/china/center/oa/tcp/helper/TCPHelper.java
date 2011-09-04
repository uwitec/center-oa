/**
 * File Name: TCPHlper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.helper;


import java.text.DecimalFormat;

import com.china.center.common.taglib.DefinedCommon;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.vo.FlowLogVO;
import com.china.center.oa.tcp.bean.TravelApplyBean;
import com.china.center.oa.tcp.constanst.TcpConstanst;
import com.china.center.oa.tcp.constanst.TcpFlowConstant;
import com.china.center.oa.tcp.vo.TravelApplyVO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.MathTools;


/**
 * TCPHlper
 * 
 * @author ZHUZHU
 * @version 2011-7-17
 * @see TCPHelper
 * @since 3.0
 */
public abstract class TCPHelper
{
    /**
     * 设置key
     * 
     * @param bean
     */
    public static void setFlowKey(TravelApplyBean bean)
    {
        if (bean.getTotal() <= 500000)
        {
            bean.setFlowKey(TcpFlowConstant.TRAVELAPPLY_0_5000);

            return;
        }

        if (bean.getTotal() > 500000 && bean.getTotal() <= 1000000)
        {
            bean.setFlowKey(TcpFlowConstant.TRAVELAPPLY_5000_10000);

            return;
        }

        if (bean.getTotal() > 1000000 && bean.getTotal() <= 5000000)
        {
            bean.setFlowKey(TcpFlowConstant.TRAVELAPPLY_10000_50000);

            return;
        }

        if (bean.getTotal() > 5000000)
        {
            bean.setFlowKey(TcpFlowConstant.TRAVELAPPLY_50000_MAX);

            return;
        }
    }

    /**
     * doubleToLong(到分)
     * 
     * @param value
     * @return
     */
    public static long doubleToLong2(String value)
    {
        // 先格式转成double
        double parseDouble = MathTools.parseDouble(value);

        return Math.round(MathTools.parseDouble(formatNum2(parseDouble)) * 100);
    }

    public static long doubleToLong2(double value)
    {
        return Math.round(MathTools.parseDouble(formatNum2(value)) * 100);
    }

    public static void chageVO(TravelApplyVO vo)
    {
        vo.setShowTotal(formatNum2(vo.getTotal() / 100.0d));
        vo.setShowBorrowTotal(formatNum2(vo.getBorrowTotal() / 100.0d));

        vo.setShowAirplaneCharges(formatNum2(vo.getAirplaneCharges() / 100.0d));
        vo.setShowTrainCharges(formatNum2(vo.getTrainCharges() / 100.0d));

        vo.setShowBusCharges(formatNum2(vo.getBusCharges() / 100.0d));
        vo.setShowHotelCharges(formatNum2(vo.getHotelCharges() / 100.0d));

        vo.setShowEntertainCharges(formatNum2(vo.getEntertainCharges() / 100.0d));
        vo.setShowAllowanceCharges(formatNum2(vo.getAllowanceCharges() / 100.0d));

        vo.setShowOther1Charges(formatNum2(vo.getOther1Charges() / 100.0d));
        vo.setShowOther2Charges(formatNum2(vo.getOther2Charges() / 100.0d));
    }

    /**
     * 是否可以删除
     * 
     * @param bean
     * @return
     */
    public static boolean canTravelApplyDelete(TravelApplyBean bean)
    {
        if (bean.getStatus() == TcpConstanst.TCP_STATUS_INIT
            || bean.getStatus() == TcpConstanst.TCP_STATUS_REJECT)
        {
            return true;
        }

        return false;
    }

    public static String formatNum2(double d)
    {
        DecimalFormat df = new DecimalFormat("####0.00");

        String result = df.format(d);

        if (".00".equals(result))
        {
            result = "0" + result;
        }

        return result;
    }

    public static FlowLogVO getTCPFlowLogVO(FlowLogBean bean)
    {
        FlowLogVO vo = new FlowLogVO();

        if (bean == null)
        {
            return vo;
        }

        BeanUtil.copyProperties(vo, bean);

        vo.setOprModeName(DefinedCommon.getValue("oprMode", vo.getOprMode()));

        vo.setPreStatusName(DefinedCommon.getValue("tcpStatus", vo.getPreStatus()));

        vo.setAfterStatusName(DefinedCommon.getValue("tcpStatus", vo.getAfterStatus()));

        return vo;
    }
}
