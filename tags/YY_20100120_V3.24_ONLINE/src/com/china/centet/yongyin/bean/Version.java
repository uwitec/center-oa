/*
 * File Name: Version.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-17
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.centet.yongyin.constant.Constant;


/**
 * °æ±¾bean
 * 
 * @author ZHUZHU
 * @version 2007-8-17
 * @see
 * @since
 */
public class Version implements Serializable
{
    private int index = Constant.VERSION_INDEX;

    private String version = "3.0";

    private String sequences = "100";

    private String lastdate = "2007-08-08 12:00:00";

    /**
     * default constructor
     */
    public Version()
    {}

    /**
     * @return the index
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * @return the lastdate
     */
    public String getLastdate()
    {
        return lastdate;
    }

    /**
     * @return the sequences
     */
    public String getSequences()
    {
        return sequences;
    }

    /**
     * @return the version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @param index
     *            the index to set
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * @param lastdate
     *            the lastdate to set
     */
    public void setLastdate(String lastdate)
    {
        this.lastdate = lastdate;
    }

    /**
     * @param sequences
     *            the sequences to set
     */
    public void setSequences(String sequences)
    {
        this.sequences = sequences;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

}
