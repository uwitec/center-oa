/*
 * File Name: Maths.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-11-3
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.math.BigDecimal;


/**
 * Mathematica 高精度运算类
 * 
 * @author zhuzhu
 * @version 2007-11-3
 * @see
 * @since
 */
public class Mathematica
{
    private BigDecimal result = new BigDecimal("0");

    /**
     * 初始值
     */
    private String init = "";

    /**
     * default constructor
     */
    public Mathematica()
    {
        init();
    }

    /**
     * default constructor
     */
    public Mathematica(Object oo)
    {
        this.init = oo.toString();

        init();
    }

    private void init()
    {
        if (init != null && !"".equals(init.trim()))
        {
            result = new BigDecimal(this.init);
        }
    }

    public Mathematica add(Object o)
    {
        BigDecimal bd = new BigDecimal(o.toString());

        result = result.add(bd);

        return this;
    }

    public Mathematica multiply(Object o)
    {
        BigDecimal bd = new BigDecimal(o.toString());

        result = result.multiply(bd);

        return this;
    }

    public Mathematica divide(Object o)
    {
        BigDecimal bd = new BigDecimal(o.toString());

        try
        {
            result = result.divide(bd);
        }
        catch (ArithmeticException e)
        {
            // 对于不能除尽直接使用java处理
            double dd = Double.parseDouble(result.toString());

            dd = dd / Double.parseDouble(o.toString());

            result = new BigDecimal(String.valueOf(dd));
        }

        return this;
    }

    public Mathematica subtract(Object o)
    {
        BigDecimal bd = new BigDecimal(o.toString());

        result = result.subtract(bd);

        return this;
    }

    public BigDecimal complite()
    {
        BigDecimal re = new BigDecimal(result.toString());

        result = new BigDecimal("0");

        return re;
    }

    public double toDouble()
    {
        return Double.parseDouble(result.toString());
    }

    public String toString()
    {
        return result.toString();
    }

    public int toInt()
    {
        return result.intValue();
    }

    public float toFloat()
    {
        return result.floatValue();
    }

    public BigDecimal getValue()
    {
        return result;
    }

    /**
     * @return the init
     */
    public String getInit()
    {
        return init;
    }

    /**
     * @param init
     *            the init to set
     */
    public void setInit(String init)
    {
        this.init = init;

        init();
    }

}
