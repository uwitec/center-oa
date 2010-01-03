/**
 * File Name: MapBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.common.taglib;


import java.io.Serializable;


/**
 * MapBean
 * 
 * @author zhuzhu
 * @version 2008-11-9
 * @see MapBean
 * @since 1.0
 */
public class MapBean implements Serializable
{
    private String key = "";

    private String value = "";

    public MapBean()
    {}

    public MapBean(String key, String value)
    {
        this.key = key;

        this.value = value;
    }

    public MapBean(int key, String value)
    {
        this.key = String.valueOf(key);

        this.value = value;
    }

    public String toString()
    {
        return "key:" + key + ";value=" + value;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @return the key
     */
    public String getId()
    {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @return the value
     */
    public String getName()
    {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
}
