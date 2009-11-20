/**
 * File Name: FlowInstanceViewBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-9-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;
import com.china.center.oa.constant.FlowConstant;


/**
 * FlowInstanceViewBean
 * 
 * @author zhuzhu
 * @version 2008-9-3
 * @see
 * @since
 */
@Entity(name = "流程实例可视")
@Table(name = "T_CENTER_OAFLOWINSTANCEVIEW")
public class FlowInstanceViewBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK(index = FlowConstant.FK_INDEX_USERID)
    private String viewer = "";
    
    @Join(tagClass = FlowDefineBean.class)
    private String flowId = "";
    
    private int type = FlowConstant.FLOW_PLUGIN_STAFFER;

    @FK
    @Unique(dependFields = "stafferId")
    @Join(tagClass = FlowInstanceBean.class)
    private String instanceId = "";
    
    private String logTime = "";

    /**
     * default constructor
     */
    public FlowInstanceViewBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

   
    /**
     * @return the instanceId
     */
    public String getInstanceId()
    {
        return instanceId;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

   
    /**
     * @param instanceId the instanceId to set
     */
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }


    /**
     * @return the viewer
     */
    public String getViewer()
    {
        return viewer;
    }

    /**
     * @param viewer the viewer to set
     */
    public void setViewer(String viewer)
    {
        this.viewer = viewer;
    }

    /**
     * @return the flowId
     */
    public String getFlowId()
    {
        return flowId;
    }

    /**
     * @param flowId the flowId to set
     */
    public void setFlowId(String flowId)
    {
        this.flowId = flowId;
    }

    /**
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param logTime the logTime to set
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }
}
