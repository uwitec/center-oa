/*
 * File Name: OutLog.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-13
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean.helper;


import com.china.center.tools.BeanUtil;
import com.china.centet.yongyin.bean.FlowLogBean;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.vo.FlowLogBeanVO;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2008-1-13
 * @see
 * @since
 */
public abstract class FlowLogHelper
{
    public static FlowLogBeanVO getOutLogVO(FlowLogBean bean)
    {
        FlowLogBeanVO vo = new FlowLogBeanVO();

        if (bean == null)
        {
            return vo;
        }

        BeanUtil.copyProperties(vo, bean);

        if (bean.getOprMode() == Constant.OPRMODE_PASS)
        {
            vo.setOprModeName("通过");
        }

        if (bean.getOprMode() == Constant.OPRMODE_REJECT)
        {
            vo.setOprModeName("驳回");
        }

        vo.setPreStatusName(OutBeanHelper.getStatus(vo.getPreStatus()));

        vo.setAfterStatusName(OutBeanHelper.getStatus(vo.getAfterStatus()));

        return vo;
    }

    public static FlowLogBeanVO getStockFlowLogVO(FlowLogBean bean)
    {
        FlowLogBeanVO vo = new FlowLogBeanVO();

        if (bean == null)
        {
            return vo;
        }

        BeanUtil.copyProperties(vo, bean);

        if (bean.getOprMode() == Constant.OPRMODE_PASS)
        {
            vo.setOprModeName("通过");
        }

        if (bean.getOprMode() == Constant.OPRMODE_REJECT)
        {
            vo.setOprModeName("驳回");
        }

        vo.setPreStatusName(StockHelper.getStatus(vo.getPreStatus()));

        vo.setAfterStatusName(StockHelper.getStatus(vo.getAfterStatus()));

        return vo;
    }
}
