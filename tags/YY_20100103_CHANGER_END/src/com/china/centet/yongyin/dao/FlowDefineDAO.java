/**
 *
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.FlowDefineBean;
import com.china.centet.yongyin.constant.FlowConstant;
import com.china.centet.yongyin.vo.FlowDefineBeanVO;


/**
 * @author Administrator
 */
public class FlowDefineDAO extends BaseDAO2<FlowDefineBean, FlowDefineBeanVO>
{
    public int countName(String name)
    {
        return this.countBycondition("where name = ? and status = ?", name,
            FlowConstant.FLOW_STATUS_INIT);
    }

    /**
     * 修改流程定义的状态
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateFlowDefineStatus(String id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, this.claz);

        return true;
    }

    public List<FlowDefineBean> queryUseFlow()
    {
        return this.queryEntityBeansByFK(FlowConstant.FLOW_STATUS_INIT);
    }
}
