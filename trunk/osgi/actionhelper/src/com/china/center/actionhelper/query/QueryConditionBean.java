/**
 * File Name: QueryCondtionBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-8<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.query;


import java.io.Serializable;

import com.china.center.tools.CommonTools;


/**
 * QueryCondtionBean
 * 
 * @author ZHUZHU
 * @version 2008-11-8
 * @see QueryConditionBean
 * @since 1.0
 */
public class QueryConditionBean implements Serializable
{
    private String name = "";

    private String filed = "";

    private String caption = "";

    private String opr = "";

    private String type = "";

    private String pfix = "";

    private String inner = "";

    private String option = "";

    /**
     * 0:string 1:int/double/float
     */
    private int datatype = 0;

    private String value = "";

    /**
     * Copy Constructor
     * 
     * @param queryConditionBean
     *            a <code>QueryConditionBean</code> object
     */
    public QueryConditionBean(QueryConditionBean queryConditionBean)
    {
        this.name = queryConditionBean.name;
        this.filed = queryConditionBean.filed;
        this.caption = queryConditionBean.caption;
        this.opr = queryConditionBean.opr;
        this.type = queryConditionBean.type;
        this.pfix = queryConditionBean.pfix;
        this.inner = queryConditionBean.inner;
        this.option = queryConditionBean.option;
        this.datatype = queryConditionBean.datatype;
        this.value = queryConditionBean.value;
    }

    /**
     * default constructor
     */
    public QueryConditionBean()
    {
    }

    public String getAssistant()
    {
        return "filed:" + this.filed + ";opr:" + this.opr + ";pfix:" + this.pfix + ";datatype:" + this.datatype;
    }

    public void parser(String str)
    {
        if (str == null || "".equals(str.trim()))
        {
            return;
        }

        String[] par = str.split(";");

        for (String string : par)
        {
            string = " " + string + " ";

            String[] parInner = string.split(":");

            if (parInner.length != 2)
            {
                continue;
            }

            String key = parInner[0].trim();

            String value = parInner[1].trim();

            if ("filed".equalsIgnoreCase(key))
            {
                this.filed = value;
            }

            if ("opr".equalsIgnoreCase(key))
            {
                this.opr = value;
            }

            if ("pfix".equalsIgnoreCase(key))
            {
                this.pfix = value;
            }

            if ("datatype".equalsIgnoreCase(key))
            {
                this.datatype = CommonTools.parseInt(value);
            }
        }
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
     * @return the filed
     */
    public String getFiled()
    {
        return filed;
    }

    /**
     * @param filed
     *            the filed to set
     */
    public void setFiled(String filed)
    {
        this.filed = filed;
    }

    /**
     * @return the caption
     */
    public String getCaption()
    {
        return caption;
    }

    /**
     * @param caption
     *            the caption to set
     */
    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    /**
     * @return the opr
     */
    public String getOpr()
    {
        return opr;
    }

    /**
     * @param opr
     *            the opr to set
     */
    public void setOpr(String opr)
    {
        this.opr = opr;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return the inner
     */
    public String getInner()
    {
        return inner;
    }

    /**
     * @param inner
     *            the inner to set
     */
    public void setInner(String inner)
    {
        this.inner = inner;
    }

    /**
     * @return the option
     */
    public String getOption()
    {
        return option;
    }

    /**
     * @param option
     *            the option to set
     */
    public void setOption(String option)
    {
        this.option = option;
    }

    /**
     * @return the pfix
     */
    public String getPfix()
    {
        return pfix;
    }

    /**
     * @param pfix
     *            the pfix to set
     */
    public void setPfix(String pfix)
    {
        this.pfix = pfix;
    }

    /**
     * @return the datatype
     */
    public int getDatatype()
    {
        return datatype;
    }

    /**
     * @param datatype
     *            the datatype to set
     */
    public void setDatatype(int datatype)
    {
        this.datatype = datatype;
    }

    /**
     * @return the value
     */
    public String getValue()
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
