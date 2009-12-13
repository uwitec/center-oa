/**
 * File Name: StatWorkLogWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-7-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.wrap;


import java.io.Serializable;


/**
 * StatWorkLogWrap
 * 
 * @author zhuzhu
 * @version 2009-7-4
 * @see StatWorkLogWrap
 * @since 1.0
 */
public class StatWorkLogWrap implements Serializable
{
    private String stafferId = "";

    private String stafferName = "";

    private int total = 0;

    private int expectTotal = 0;

    private int exceptionNum = 0;

    private String exRatio = "0%";

    /**
     * default constructor
     */
    public StatWorkLogWrap()
    {}

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
     * @return the total
     */
    public int getTotal()
    {
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(int total)
    {
        this.total = total;
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
     * @return the exceptionNum
     */
    public int getExceptionNum()
    {
        return exceptionNum;
    }

    /**
     * @param exceptionNum
     *            the exceptionNum to set
     */
    public void setExceptionNum(int exceptionNum)
    {
        this.exceptionNum = exceptionNum;
    }

    /**
     * @return the exRatio
     */
    public String getExRatio()
    {
        return exRatio;
    }

    /**
     * @param exRatio
     *            the exRatio to set
     */
    public void setExRatio(String exRatio)
    {
        this.exRatio = exRatio;
    }

    /**
     * @return the expectTotal
     */
    public int getExpectTotal()
    {
        return expectTotal;
    }

    /**
     * @param expectTotal
     *            the expectTotal to set
     */
    public void setExpectTotal(int expectTotal)
    {
        this.expectTotal = expectTotal;
    }
}
