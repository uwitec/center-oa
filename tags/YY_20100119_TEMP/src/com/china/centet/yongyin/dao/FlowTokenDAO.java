/**
 *
 */
package com.china.centet.yongyin.dao;


import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.FlowTokenBean;
import com.china.centet.yongyin.vo.FlowTokenBeanVO;


/**
 * @author Administrator
 */
public class FlowTokenDAO extends BaseDAO2<FlowTokenBean, FlowTokenBeanVO>
{
    public FlowTokenBean findToken(String flowId, int orders)
    {
        return this.findUnique("where flowId = ? and orders = ?", flowId, orders);
    }

    public FlowTokenBean findBeginToken(String flowId)
    {
        return this.findUnique("where flowId = ? and begining = ?", flowId, true);
    }

    public FlowTokenBean findEndToken(String flowId)
    {
        return this.findUnique("where flowId = ? and ending = ?", flowId, true);
    }
}
