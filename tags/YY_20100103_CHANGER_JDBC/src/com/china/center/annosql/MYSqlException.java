/*
 * Created on 2006-2-24
 */
package com.china.center.annosql;


import com.china.center.annosql.tools.BeanTools;


/**
 * @author zhu
 */
public class MYSqlException extends Exception
{
    private String errorNo = "";

    private String errorContent = "";

    public MYSqlException(String errorNo, String errorContent)
    {
        this.errorNo = errorNo;
        this.errorContent = errorContent;
    }

    public MYSqlException(String errorNo)
    {
        this.errorNo = errorNo;

        this.errorContent = BeanTools.getErrorMessage(errorNo);
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

    public void setErrorNo(String errorNo)
    {
        this.errorNo = errorNo;
    }

    public String toString()
    {
        return ("ErrorNumber is " + errorNo + ":" + errorContent);
    }
}