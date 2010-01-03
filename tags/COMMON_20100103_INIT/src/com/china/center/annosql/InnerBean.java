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

    private String aliasField = "";

    private boolean relationship = false;

    private boolean autoIncrement = false;

    private boolean alias = false;

    private Field field = null;

    /**
     * default constructor
     */
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

    /**
     * @return the alias
     */
    public boolean isAlias()
    {
        return alias;
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(boolean alias)
    {
        this.alias = alias;
    }

    /**
     * @return the aliasField
     */
    public String getAliasField()
    {
        return aliasField;
    }

    /**
     * @param aliasField
     *            the aliasField to set
     */
    public void setAliasField(String aliasField)
    {
        this.aliasField = aliasField;
    }

    /**
     * @return the autoIncrement
     */
    public boolean isAutoIncrement()
    {
        return autoIncrement;
    }

    /**
     * @param autoIncrement
     *            the autoIncrement to set
     */
    public void setAutoIncrement(boolean autoIncrement)
    {
        this.autoIncrement = autoIncrement;
    }
}
