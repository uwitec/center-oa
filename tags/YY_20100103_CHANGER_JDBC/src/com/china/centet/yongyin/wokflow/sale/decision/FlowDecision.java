/*
 * File Name: SaveDecision.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wokflow.sale.decision;


import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;

import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.helper.LocationHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.manager.OutManager;
import com.china.centet.yongyin.wokflow.JbpmHelper;
import com.china.centet.yongyin.wokflow.sale.SaleConstant;


/**
 * 是否需要物流审批
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class FlowDecision implements DecisionHandler
{
    private OutManager outManager = null;

    public String decide(ExecutionContext context)
        throws Exception
    {
        JbpmHelper helper = new JbpmHelper(context);

        // 获得request里面的数据
        String fullId = helper.getStringVariable("fullId");

        OutBean out = outManager.findOutById(fullId);

        if (out == null)
        {
            throw new Exception("销售单不存在!");
        }

        if (LocationHelper.isSystemLocation(out.getLocation()))
        {
            return SaleConstant.YES;
        }
        else
        {
            helper.setVariable("nextStatus", Constant.STATUS_FLOW_PASS);

            return SaleConstant.NO;
        }
    }

    /**
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
    }
}
