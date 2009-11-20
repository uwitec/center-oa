/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.FK;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.annotation.enums.JoinType;
import com.china.centet.yongyin.constant.FlowConstant;


/**
 * @author Administrator
 */
@Entity(name = "流程定义", cache = true)
@Table(name = "T_CENTER_FLOWDEFINE")
public class FlowDefineBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "名称", must = true, maxLength = 100)
    private String name = "";

    private String logTime = "";

    @Join(tagClass = User.class, type = JoinType.LEFT)
    private String userId = "";

    /**
     * 暂时不使用
     */
    private int type = FlowConstant.FLOW_TYPE_USER;

    @FK
    private int status = FlowConstant.FLOW_STATUS_INIT;

    @Html(title = "描述", type = Element.TEXTAREA, maxLength = 255)
    private String description = "";

    @Ignore
    private List<FlowTokenBean> tokens = null;

    @Ignore
    private List<FlowViewerBean> views = null;

    /**
     *
     */
    public FlowDefineBean()
    {}

    public String toString()
    {
        return this.id + ';' + this.name;
    }

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
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
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

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the tokens
     */
    public List<FlowTokenBean> getTokens()
    {
        return tokens;
    }

    /**
     * @param tokens
     *            the tokens to set
     */
    public void setTokens(List<FlowTokenBean> tokens)
    {
        this.tokens = tokens;
    }

    /**
     * @return the views
     */
    public List<FlowViewerBean> getViews()
    {
        return views;
    }

    /**
     * @param views
     *            the views to set
     */
    public void setViews(List<FlowViewerBean> views)
    {
        this.views = views;
    }
}
