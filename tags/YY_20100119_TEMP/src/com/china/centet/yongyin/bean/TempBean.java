/*
 * File Name: TempBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-4-26
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-4-26
 * @see
 * @since
 */
public class TempBean implements Serializable
{
    private int add = 0;

    private int del = 0;

    /**
     * default constructor
     */
    public TempBean()
    {}

    /**
     * @return the add
     */
    public int getAdd()
    {
        return add;
    }

    /**
     * @param add
     *            the add to set
     */
    public void setAdd(int add)
    {
        this.add = add;
    }

    /**
     * @return the del
     */
    public int getDel()
    {
        return del;
    }

    /**
     * @param del
     *            the del to set
     */
    public void setDel(int del)
    {
        this.del = del;
    }

    public int hashCode()
    {
        return Math.abs(add) + Math.abs(del);
    }

}
