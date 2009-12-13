/*
 * 文 件 名:  InnerBean.java
 * 版    权:  centerchina Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  admin
 * 修改时间:  2007-9-30
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.china.center.annosql;


import java.io.Serializable;
import java.lang.reflect.Field;


/**
 * @author admin
 * @version [版本号, 2007-9-30]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class InnerBean implements Serializable
{
    private String columnName = "";

    private String fieldName = "";

    private boolean relationship = false;

    private Field field = null;

    public InnerBean()
    {}

    /**
     * @return 返回 columnName
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * @param 对columnName进行赋值
     */
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    /**
     * @return 返回 fieldName
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * @param 对fieldName进行赋值
     */
    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    /**
     * @return the relationship
     */
    public boolean isRelationship()
    {
        return relationship;
    }

    /**
     * @param relationship
     *            the relationship to set
     */
    public void setRelationship(boolean relationship)
    {
        this.relationship = relationship;
    }

    /**
     * @return the field
     */
    public Field getField()
    {
        return field;
    }

    /**
     * @param field
     *            the field to set
     */
    public void setField(Field field)
    {
        this.field = field;
    }

}
