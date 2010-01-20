/**
 *
 */
package com.china.centet.yongyin.dao;


import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.FlowConsignBean;


/**
 * @author Administrator
 */
public class FlowConsignDAO extends BaseDAO2<FlowConsignBean, FlowConsignBean>
{
    public boolean hasConsign(String instanceId, String tokenId, String userId)
    {
        return this.countBycondition("where instanceId = ? and tokenId = ? and userId = ?",
            instanceId, tokenId, userId) > 0;
    }
}
