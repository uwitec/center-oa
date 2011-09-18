/**
 * File Name: TcpHandleHisVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-9-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.vo;


import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.tcp.bean.TcpHandleHisBean;


/**
 * TcpHandleHisVO
 * 
 * @author ZHUZHU
 * @version 2011-9-18
 * @see TcpHandleHisVO
 * @since 3.0
 */
@Entity(inherit = true)
public class TcpHandleHisVO extends TcpHandleHisBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";

    @Relationship(relationField = "applyId")
    private String applyName = "";

    @Ignore
    private String url = "";

    /**
     * default constructor
     */
    public TcpHandleHisVO()
    {
    }

    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }

    /**
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }

    /**
     * @return the applyName
     */
    public String getApplyName()
    {
        return applyName;
    }

    /**
     * @param applyName
     *            the applyName to set
     */
    public void setApplyName(String applyName)
    {
        this.applyName = applyName;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue.append("TcpHandleHisVO ( ").append(super.toString()).append(TAB).append(
            "stafferName = ").append(this.stafferName).append(TAB).append("applyName = ").append(
            this.applyName).append(TAB).append("url = ").append(this.url).append(TAB).append(" )");

        return retValue.toString();
    }

}
