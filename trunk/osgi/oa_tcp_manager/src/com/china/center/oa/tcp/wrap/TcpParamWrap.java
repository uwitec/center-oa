/**
 * File Name: TcpParamWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-9-13<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.wrap;


import java.io.Serializable;


/**
 * 处理参数
 * 
 * @author ZHUZHU
 * @version 2011-9-13
 * @see TcpParamWrap
 * @since 1.0
 */
public class TcpParamWrap implements Serializable
{
    private String id = "";

    private String processId = "";

    private String reason = "";

    private String type = "";

    private Object other = null;

    /**
     * default constructor
     */
    public TcpParamWrap()
    {
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
     * @return the processId
     */
    public String getProcessId()
    {
        return processId;
    }

    /**
     * @param processId
     *            the processId to set
     */
    public void setProcessId(String processId)
    {
        this.processId = processId;
    }

    /**
     * @return the reason
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    /**
     * @return the other
     */
    public Object getOther()
    {
        return other;
    }

    /**
     * @param other
     *            the other to set
     */
    public void setOther(Object other)
    {
        this.other = other;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuffer retValue = new StringBuffer();

        retValue.append("TcpParamWrap ( ").append(super.toString()).append(TAB).append("id = ").append(this.id).append(
            TAB).append("processId = ").append(this.processId).append(TAB).append("reason = ").append(this.reason).append(
            TAB).append("type = ").append(this.type).append(TAB).append("other = ").append(this.other).append(TAB).append(
            " )");

        return retValue.toString();
    }

}
