/**
 * File Name: ValueBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-10<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.expression;


import java.io.Serializable;


/**
 * ValueBean
 * 
 * @author ZHUZHU
 * @version 2010-7-10
 * @see ValueBean
 * @since 1.0
 */
public class ValueBean implements Serializable
{
    private String name = "";

    private String columnName = "";

    private Object value = null;

    /**
     * default constructor
     */
    public ValueBean()
    {
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
     * @return the columnName
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * @param columnName
     *            the columnName to set
     */
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    /**
     * @return the value
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Object value)
    {
        this.value = value;
    }
}
