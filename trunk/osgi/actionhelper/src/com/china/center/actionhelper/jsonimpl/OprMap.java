/**
 * File Name: OprMap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-10-26<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.jsonimpl;

/**
 * <描述>
 * 
 * @author ZHUZHU
 * @version 2008-10-26
 * @see
 * @since
 */
public class OprMap implements JSONString
{
    private Object key = null;

    private Object value = null;

    public OprMap()
    {}

    public OprMap(Object key, Object value)
    {
        this.key = key;

        this.value = value;
    }

    public OprMap put(Object key, Object value)
    {
        this.key = key;

        this.value = value;
        
        return this;
    }

    public String toString()
    {
        return "{'" + key + "': '" + value + "'}";
    }

    /**
     * @return the key
     */
    public Object getKey()
    {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(Object key)
    {
        this.key = key;
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

    public String toJSONString()
    {
        return toString();
    }
}
