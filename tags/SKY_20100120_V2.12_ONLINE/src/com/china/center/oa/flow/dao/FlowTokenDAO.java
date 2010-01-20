/**
 * File Name: FlowTokenDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.flow.bean.FlowTokenBean;
import com.china.center.oa.flow.vo.FlowTokenVO;


/**
 * FlowTokenDAO
 * 
 * @author zhuzhu
 * @version 2009-4-26
 * @see FlowTokenDAO
 * @since 1.0
 */
@Bean(name = "flowTokenDAO")
public class FlowTokenDAO extends BaseDAO2<FlowTokenBean, FlowTokenVO>
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
