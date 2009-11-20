/**
 * File Name: FlowInstanceLogBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.JoinType;
import com.china.center.oa.constant.FlowConstant;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * @author Administrator
 */
@Entity(name = "流程日志")
@Table(name = "T_CENTER_OAFLOWLOG")
public class FlowInstanceLogBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    @Join(tagClass = FlowInstanceBean.class)
    private String instanceId = "";

    @Join(tagClass = FlowDefineBean.class)
    private String flowId = "";

    @Join(tagClass = FlowTokenBean.class)
    private String tokenId = "";

    @Join(tagClass = FlowTokenBean.class, alias = "nextToken")
    private String nextTokenId = "";

    private String logTime = "";

    private int oprMode = FlowConstant.OPERATION_SUBMIT;

    @Join(tagClass = StafferBean.class, type = JoinType.LEFT, alias = "sf1")
    private String stafferId = "";

    @Join(tagClass = StafferBean.class, type = JoinType.LEFT, alias = "sf2")
    private String nextStafferId = "";

    private String opinion = "";

    /**
     *
     */
    public FlowInstanceLogBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the instanceId
     */
    public String getInstanceId()
    {
        return instanceId;
    }

    /**
     * @param instanceId
     *            the instanceId to set
     */
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    /**
     * @return the flowId
     */
    public String getFlowId()
    {
        return flowId;
    }

    /**
     * @param flowId
     *            the flowId to set
     */
    public void setFlowId(String flowId)
    {
        this.flowId = flowId;
    }

    /**
     * @return the tokenId
     */
    public String getTokenId()
    {
        return tokenId;
    }

    /**
     * @param tokenId
     *            the tokenId to set
     */
    public void setTokenId(String tokenId)
    {
        this.tokenId = tokenId;
    }

    /**
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param logTime
     *            the logTime to set
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

    /**
     * @return the oprMode
     */
    public int getOprMode()
    {
        return oprMode;
    }

    /**
     * @param oprMode
     *            the oprMode to set
     */
    public void setOprMode(int oprMode)
    {
        this.oprMode = oprMode;
    }

    /**
     * @return the opinion
     */
    public String getOpinion()
    {
        return opinion;
    }

    /**
     * @param opinion
     *            the opinion to set
     */
    public void setOpinion(String opinion)
    {
        this.opinion = opinion;
    }

    /**
     * @return the nextTokenId
     */
    public String getNextTokenId()
    {
        return nextTokenId;
    }

    /**
     * @param nextTokenId
     *            the nextTokenId to set
     */
    public void setNextTokenId(String nextTokenId)
    {
        this.nextTokenId = nextTokenId;
    }

    /**
     * @return the stafferId
     */
    public String getStafferId()
    {
        return stafferId;
    }

    /**
     * @param stafferId
     *            the stafferId to set
     */
    public void setStafferId(String stafferId)
    {
        this.stafferId = stafferId;
    }

    /**
     * @return the nextStafferId
     */
    public String getNextStafferId()
    {
        return nextStafferId;
    }

    /**
     * @param nextStafferId
     *            the nextStafferId to set
     */
    public void setNextStafferId(String nextStafferId)
    {
        this.nextStafferId = nextStafferId;
    }
}
