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
}
