/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.annotation.enums.JoinType;


/**
 * @author Administrator
 */
@Entity(name = "流程实例")
@Table(name = "T_CENTER_FLOWINSTANCE")
public class FlowInstanceBean implements Serializable
{
    @Id
    private String id = "";

    @Join(tagClass = FlowDefineBean.class)
    private String flowId = "";

    @Html(title = "标题", must = true, maxLength = 100)
    private String title = "";

    private String locationId = "";

    private String logTime = "";

    @Html(title = "截止时间", type = Element.DATETIME, must = true, maxLength = 100)
    private String endTime = "";

    private int status = 0;

    @Join(tagClass = User.class, type = JoinType.LEFT)
    private String userId = "";

    @Html(title = "内容", type = Element.TEXTAREA, must = true, maxLength = 500)
    private String description = "";

    /**
     * 流程附件
     */
    private String attachment = "";

    /**
     * 文件的真实名称
     */
    private String fileName = "";

    @Ignore
    private List<FlowInstanceLogBean> logs = null;

    /**
     *
     */
    public FlowInstanceBean()
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
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
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
     * @return the endTime
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
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
     * @return the logs
     */
    public List<FlowInstanceLogBean> getLogs()
    {
        return logs;
    }

    /**
     * @param logs
     *            the logs to set
     */
    public void setLogs(List<FlowInstanceLogBean> logs)
    {
        this.logs = logs;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
    }

    /**
     * @param locationId
     *            the locationId to set
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
    }

    /**
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * @return the attachment
     */
    public String getAttachment()
    {
        return attachment;
    }

    /**
     * @param attachment
     *            the attachment to set
     */
    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }
}
