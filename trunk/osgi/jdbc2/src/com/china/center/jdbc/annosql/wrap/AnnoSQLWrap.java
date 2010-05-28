/**
 * File Name: AnnoSQLWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-7-11<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annosql.wrap;


import java.io.Serializable;


/**
 * AnnoSQLWrap
 * 
 * @author ZHUZHU
 * @version 2009-7-11
 * @see AnnoSQLWrap
 * @since 1.0
 */
public class AnnoSQLWrap implements Serializable
{
    private Class claz = null;

    private String insertSQL = "";

    private String updateSQL = "";

    private String querySQL = "";

    private String deleteSQL = "";

    private boolean xml = false;

    /**
     * default constructor
     */
    public AnnoSQLWrap()
    {}

    public AnnoSQLWrap(Class claz)
    {
        this.claz = claz;
    }

    /**
     * @return the insertSQL
     */
    public String getInsertSQL()
    {
        return insertSQL;
    }

    /**
     * @param insertSQL
     *            the insertSQL to set
     */
    public void setInsertSQL(String insertSQL)
    {
        this.insertSQL = insertSQL;
    }

    /**
     * @return the updateSQL
     */
    public String getUpdateSQL()
    {
        return updateSQL;
    }

    /**
     * @param updateSQL
     *            the updateSQL to set
     */
    public void setUpdateSQL(String updateSQL)
    {
        this.updateSQL = updateSQL;
    }

    /**
     * @return the querySQL
     */
    public String getQuerySQL()
    {
        return querySQL;
    }

    /**
     * @param querySQL
     *            the querySQL to set
     */
    public void setQuerySQL(String querySQL)
    {
        this.querySQL = querySQL;
    }

    /**
     * @return the deleteSQL
     */
    public String getDeleteSQL()
    {
        return deleteSQL;
    }

    /**
     * @param deleteSQL
     *            the deleteSQL to set
     */
    public void setDeleteSQL(String deleteSQL)
    {
        this.deleteSQL = deleteSQL;
    }

    /**
     * @return the xml
     */
    public boolean isXml()
    {
        return xml;
    }

    /**
     * @param xml
     *            the xml to set
     */
    public void setXml(boolean xml)
    {
        this.xml = xml;
    }

    /**
     * @return the claz
     */
    public Class getClaz()
    {
        return claz;
    }

    /**
     * @param claz
     *            the claz to set
     */
    public void setClaz(Class claz)
    {
        this.claz = claz;
    }
}
