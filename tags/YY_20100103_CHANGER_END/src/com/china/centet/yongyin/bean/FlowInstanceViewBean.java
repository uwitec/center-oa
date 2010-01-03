/**
 * File Name: FlowInstanceViewBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-9-3<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Id;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;
import com.china.centet.yongyin.constant.FlowConstant;


/**
 * FlowInstanceViewBean
 * 
 * @author zhuzhu
 * @version 2008-9-3
 * @see
 * @since
 */
@Entity(name = "流程可视")
@Table(name = "T_CENTER_FLOWINSTANCEVIEW")
public class FlowInstanceViewBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @FK(index = FlowConstant.FK_INDEX_USERID)
    private String userId = "";

    @FK
    @Unique(dependFields = "userId")
    @Join(tagClass = FlowInstanceBean.class)
    private String instanceId = "";

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
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @return the instanceId
     */
    public String getInstanceId()
    {
        return instanceId;
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
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @param instanceId
     *            the instanceId to set
     */
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }
}
