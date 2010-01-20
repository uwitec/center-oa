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

import com.china.centet.yongyin.wokflow.sale.SaleConstant;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class SaveDecision implements DecisionHandler
{
    public String decide(ExecutionContext context)
        throws Exception
    {
        String save = context.getContextInstance().getVariable(SaleConstant.SAVE_OPRRATION).toString();

        if (SaleConstant.FLOW_DECISION_SAVE.equals(save))
        {
            return SaleConstant.FLOW_DECISION_SAVE;
        }
        else
        {
            return SaleConstant.FLOW_DECISION_SUBMIT;
        }
    }
}
