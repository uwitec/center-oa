/**
 * File Name: QueryItemBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-8<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.query;


import java.io.Serializable;
import java.util.List;


/**
 * QueryItemBean
 * 
 * @author ZHUZHU
 * @version 2008-11-8
 * @see QueryItemBean
 * @since 1.0
 */
public class QueryItemBean implements Serializable
{
    private String name = "";

    private String alias = "";

    private String namespace = "";

    private String deaultpfix = "";

    private List<QueryConditionBean> conditions = null;

    /**
     * Copy Constructor
     * 
     * @param queryItemBean
     *            a <code>QueryItemBean</code> object
     */
    public QueryItemBean(QueryItemBean queryItemBean)
    {
        this.name = queryItemBean.name;
        this.alias = queryItemBean.alias;
        this.namespace = queryItemBean.namespace;
        this.deaultpfix = queryItemBean.deaultpfix;
        this.conditions = queryItemBean.conditions;
    }

    /**
     * default constructor
     */
    public QueryItemBean()
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
     * @return the deaultpfix
     */
    public String getDeaultpfix()
    {
        return deaultpfix;
    }

    /**
     * @param deaultpfix
     *            the deaultpfix to set
     */
    public void setDeaultpfix(String deaultpfix)
    {
        this.deaultpfix = deaultpfix;
    }

    /**
     * @return the conditions
     */
    public List<QueryConditionBean> getConditions()
    {
        return conditions;
    }

    /**
     * @param conditions
     *            the conditions to set
     */
    public void setConditions(List<QueryConditionBean> conditions)
    {
        this.conditions = conditions;
    }

    /**
     * @return the namespace
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * @param namespace
     *            the namespace to set
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * @return the alias
     */
    public String getAlias()
    {
        return alias;
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("QueryItemBean ( ")
            .append(super.toString())
            .append(TAB)
            .append("name = ")
            .append(this.name)
            .append(TAB)
            .append("alias = ")
            .append(this.alias)
            .append(TAB)
            .append("namespace = ")
            .append(this.namespace)
            .append(TAB)
            .append("deaultpfix = ")
            .append(this.deaultpfix)
            .append(TAB)
            .append("conditions = ")
            .append(this.conditions)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }
}
