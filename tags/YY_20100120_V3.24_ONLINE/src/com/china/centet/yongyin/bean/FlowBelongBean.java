/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;


/**
 * @author Administrator
 */
/**
 * @author Administrator
 */
@Entity(name = "Á÷³Ì¹éÊô")
@Table(name = "T_CENTER_FLOWBELONGS")
public class FlowBelongBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK
    @Join(tagClass = FlowInstanceBean.class)
    private String instanceId = "";

    private String flowId = "";

    private String tokenId = "";

    @Join(tagClass = User.class)
    private String userId = "";

    /**
     *
     */
    public FlowBelongBean()
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
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

}
