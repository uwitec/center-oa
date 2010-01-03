/**
 *
 */
package com.china.centet.yongyin.wokflow.plugin;


import com.china.center.common.MYException;
import com.china.centet.yongyin.bean.FlowViewerBean;
import com.china.centet.yongyin.bean.FlowTokenBean;


/**
 * 角色的Plgin
 * 
 * @author Administrator
 */
public interface CommonPlugin
{
    /**
     * 处理流程所有者
     * 
     * @param instanceId
     * @param token
     * @throws MYException
     */
    void processFlowInstanceBelong(String instanceId, FlowTokenBean token)
        throws MYException;

    /**
     * 获得处理人
     * 
     * @param processerId
     * @return
     */
    String getProcesserName(String processerId);

    /**
     * 处理流程的查阅者
     * 
     * @param instanceId
     * @param viewer
     * @throws MYException
     */
    void processFlowInstanceViewer(String instanceId, FlowViewerBean viewer)
        throws MYException;
}
