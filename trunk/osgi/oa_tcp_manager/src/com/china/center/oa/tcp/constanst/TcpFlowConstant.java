/**
 * File Name: TcpFlowConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.constanst;

/**
 * TcpFlowConstant
 * 
 * @author ZHUZHU
 * @version 2011-7-17
 * @see TcpFlowConstant
 * @since 3.0
 */
public interface TcpFlowConstant
{
    /**
     * 5000以内的报销
     */
    String TRAVELAPPLY_0_5000 = "travel-0-5000";

    /**
     * 5000-10000的报销
     */
    String TRAVELAPPLY_5000_10000 = "travel-5000-10000";

    /**
     * 10000-50000的报销
     */
    String TRAVELAPPLY_10000_50000 = "travel-10000-50000";

    /**
     * 50000+的报销
     */
    String TRAVELAPPLY_50000_MAX = "travel-50000+";

    /**
     * 部门经理
     */
    String GROUP_DM = "A220110406000200001";

    /**
     * 大区经理
     */
    String GROUP_AM = "A220110406000200002";

    /**
     * 事业部经理
     */
    String GROUP_SM = "A220110406000200003";

    /**
     * 财务总监
     */
    String GROUP_CFO = "A220110406000200004";

    /**
     * 总裁
     */
    String GROUP_CEO = "A220110406000200005";

    /**
     * 董事长
     */
    String GROUP_TOP = "A220110406000200006";

    /**
     * 稽核
     */
    String GROUP_CHECK = "A220110406000200007";

    /**
     * 财务支付
     */
    String GROUP_PAY = "A220110406000200008";
}
