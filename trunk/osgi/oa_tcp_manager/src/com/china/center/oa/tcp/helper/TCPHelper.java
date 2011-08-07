/**
 * File Name: TCPHlper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.helper;


import com.china.center.oa.tcp.bean.TravelApplyBean;
import com.china.center.oa.tcp.constanst.TcpFlowConstant;


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
}
