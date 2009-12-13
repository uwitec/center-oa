/**
 * 
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * 系统日志
 * 
 * @author Administrator
 */
@Entity(name = "系统日志")
@Table(name = "T_CENTER_LOGGER")
public class Logger implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    private String userId = "";

    private String module = "";

    private String operation = "";

    private String log = "";

    private String locationId = "";

    private String logTime = "";

    /**
     * 
     */
    public Logger()
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
     * @return the module
     */
    public String getModule()
    {
        return module;
    }

    /**
     * @param module
     *            the module to set
     */
    public void setModule(String module)
    {
        this.module = module;
    }

    /**
     * @return the operation
     */
    public String getOperation()
    {
        return operation;
    }

    /**
     * @param operation
     *            the operation to set
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    /**
     * @return the log
     */
    public String getLog()
    {
        return log;
    }

    /**
     * @param log
     *            the log to set
     */
    public void setLog(String log)
    {
        this.log = log;
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
}
