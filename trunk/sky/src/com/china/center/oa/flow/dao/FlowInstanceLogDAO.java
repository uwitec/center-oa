/**
 * File Name: FlowInstanceLogDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.flow.bean.FlowInstanceLogBean;
import com.china.center.oa.flow.vo.FlowInstanceLogVO;


/**
 * FlowInstanceLogDAO
 * 
 * @author zhuzhu
 * @version 2009-5-3
 * @see FlowInstanceLogDAO
 * @since 1.0
 */
@Bean(name = "flowInstanceLogDAO")
public class FlowInstanceLogDAO extends BaseDAO2<FlowInstanceLogBean, FlowInstanceLogVO>
{
    /**
     * findLastLog
     * 
     * @param instanceId
     * @param tokenId
     * @param nextTokenId
     * @return
     */
    public FlowInstanceLogBean findLastLog(String instanceId, String tokenId, String nextTokenId)
    {
        List<FlowInstanceLogBean> list = this.queryEntityBeansByCondition(
            "where instanceId = ? and tokenId = ? and nextTokenId = ? order by logTime desc",
            instanceId, tokenId, nextTokenId);

        if (list.isEmpty())
        {
            return null;
        }

        return list.get(0);
    }

    public FlowInstanceLogVO findLastLogVO(String instanceId, String tokenId)
    {
        List<FlowInstanceLogVO> list = this.queryEntityVOsByCondition(
            "where FlowInstanceLogBean.instanceId = ? and FlowInstanceLogBean.tokenId = ? order by logTime desc",
            instanceId, tokenId);

        if (list.isEmpty())
        {
            return null;
        }

        return list.get(0);
    }
}
