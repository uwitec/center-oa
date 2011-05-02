/**
 * File Name: IntegerWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-5-2<br>
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.io.Serializable;


/**
 * IntegerWrap
 * 
 * @author ZHUZHU
 * @version 2011-5-2
 * @see IntegerWrap
 * @since 3.0
 */
public class IntegerWrap implements Serializable
{
    private Integer result = new Integer(0);

    /**
     * default constructor
     */
    public IntegerWrap()
    {
    }

    public IntegerWrap(int init)
    {
        result = init;
    }

    public void add(int init)
    {
        result += init;
    }

    /**
     * @return the result
     */
    public Integer getResult()
    {
        return result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(Integer result)
    {
        this.result = result;
    }
}
