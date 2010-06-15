/*
 * Created on 2006-2-24
 */
package com.china.center.common;


import java.util.Formatter;

import com.china.center.jdbc.annosql.tools.BeanTools;


/**
 * @author zhu
 */
public class MYException extends Exception
{
    private String errorNo = "";

    private String errorContent = "";

    public MYException(String fomContent, Object... par)
    {
        Formatter formatter = new Formatter();

        this.errorContent = formatter.format(fomContent, par).toString();
    }

    public MYException create(String errorNo, String errorContent)
    {
        this.errorNo = errorNo;

        this.errorContent = errorContent;

        return this;
    }

    public MYException(String errorContent)
    {
        this.errorContent = errorContent;
    }

    public MYException(Throwable cause)
    {
        super(cause);
    }

    /**
     * default constructor
     */
    public MYException(Throwable cause, String error)
    {
        super(cause);

        this.errorContent = error;
    }

    /**
     * 动态获得异常信息
     */
    public MYException(Class claz, String errorContent)
    {
        this.errorContent = BeanTools.getEntryName(claz) + errorContent;
    }

    public String getErrorContent()
    {
        return errorContent;
    }

    public void setErrorContent(String errorContent)
    {
        this.errorContent = errorContent;
    }

    public String getErrorNo()
    {
        return errorNo;
    }

    public String getMessage()
    {
        return errorContent;
    }

    public void setErrorNo(String errorNo)
    {
        this.errorNo = errorNo;
    }

    public String toString()
    {
        return ("ErrorNumber is " + errorNo + ":" + errorContent);
    }
}