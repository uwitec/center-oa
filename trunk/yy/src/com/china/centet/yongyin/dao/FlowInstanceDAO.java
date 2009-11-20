/**
 *
 */
package com.china.centet.yongyin.dao;


import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.FlowInstanceBean;
import com.china.centet.yongyin.constant.FlowConstant;
import com.china.centet.yongyin.vo.FlowInstanceBeanVO;


/**
 * @author Administrator
 */
public class FlowInstanceDAO extends BaseDAO2<FlowInstanceBean, FlowInstanceBeanVO>
{
    public int countNotEndInstance(String flowId)
    {
        return this.countBycondition("where flowId = ? and status <> ?", flowId,
            FlowConstant.FLOW_INSTANCE_END);
    }

    public int countByFlowId(String flowId)
    {
        return this.countBycondition("where flowId = ?", flowId);
    }

    public boolean updateStatus(String id, int status)
    {
        return this.jdbcOperation.updateField("status", status, id, this.claz) > 0;
    }
}
