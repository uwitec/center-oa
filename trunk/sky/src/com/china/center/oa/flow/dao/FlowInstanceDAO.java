/**
 * File Name: FlowInstanceDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.dao;


import java.io.Serializable;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.FlowConstant;
import com.china.center.oa.flow.bean.FlowInstanceBean;
import com.china.center.oa.flow.vo.FlowInstanceVO;


/**
 * FlowInstanceDAO
 * 
 * @author zhuzhu
 * @version 2009-4-26
 * @see FlowInstanceDAO
 * @since 1.0
 */
@Bean(name = "flowInstanceDAO")
public class FlowInstanceDAO extends BaseDAO2<FlowInstanceBean, FlowInstanceVO>
{
    /**
     * count process Instance bt flow id
     * 
     * @param flowId
     * @return
     */
    public int countProcessInstanceByFlowId(Serializable flowId)
    {
        return this.countByCondition("where flowId = ? and status != ?", flowId,
            FlowConstant.FLOW_INSTANCE_END);
    }

    /**
     * updateStatus
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(Serializable id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, this.claz);

        return true;
    }

    /**
     * updateCurrentTokenId
     * 
     * @param id
     * @param currentTokenId
     * @return
     */
    public boolean updateCurrentTokenId(Serializable id, String currentTokenId)
    {
        this.jdbcOperation.updateField("currentTokenId", currentTokenId, id, this.claz);

        return true;
    }

    /**
     * findByParentIdAndTokenId
     * 
     * @param parentId
     * @param tokenId
     * @return
     */
    public FlowInstanceBean findByParentIdAndTokenId(String parentId, String tokenId)
    {
        List<FlowInstanceBean> list = this.queryEntityBeansByCondition(
            "where parentId = ? and parentTokenId = ?", parentId, tokenId);

        if (list.isEmpty())
        {
            return null;
        }

        return list.get(0);
    }

}
