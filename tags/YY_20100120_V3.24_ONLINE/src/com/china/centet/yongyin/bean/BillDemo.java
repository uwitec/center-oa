/**
 * 
 */
package com.china.centet.yongyin.bean;

/**
 * @author Administrator
 */
public class BillDemo extends Bill
{
    private LogBean[] logBean = null;

    private BillDemo demo = null;

    private String[] str = null;

    /**
     * 
     */
    public BillDemo()
    {}

    /**
     * @return the logBean
     */
    public LogBean[] getLogBean()
    {
        return logBean;
    }

    /**
     * @param logBean
     *            the logBean to set
     */
    public void setLogBean(LogBean[] logBean)
    {
        this.logBean = logBean;
    }

    /**
     * @return the demo
     */
    public BillDemo getDemo()
    {
        return demo;
    }

    /**
     * @param demo
     *            the demo to set
     */
    public void setDemo(BillDemo demo)
    {
        this.demo = demo;
    }

    /**
     * @return the str
     */
    public String[] getStr()
    {
        return str;
    }

    /**
     * @param str
     *            the str to set
     */
    public void setStr(String[] str)
    {
        this.str = str;
    }
}
