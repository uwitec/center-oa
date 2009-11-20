package com.china.center.tools;


import com.china.center.common.MYException;


/**
 * 常用判断工具
 * 
 * @see JudgeTools
 * @since
 */

public class JudgeTools
{
    public final static int JUDGE_COMMON = 0;

    public final static int JUDGE_NUMBER = 1;

    public final static int JUDGE_NUMBER_OR_LETTER = 2;

    private JudgeTools()
    {}

    /**
     * Description: 判断入参是否为空(为空抛出异常,否则返回true)<br>
     * 
     * @param oo
     * @return
     * @throws SPMException
     *             boolean
     */
    public static boolean judgeParameterIsNull(Object... oo)
        throws MYException
    {
        return judgeParameterIsNull(oo, false);
    }

    public static boolean judgeParameterIsNull(Object oo)
        throws MYException
    {
        return judgeParameterIsNull(oo, false);
    }

    public static boolean judgeParameterIsNull(Object[] oo, int judgeType)
        throws MYException
    {
        if (judgeType == JudgeTools.JUDGE_NUMBER)
        {
            judgeParameterIsNull(oo, true);
        }

        if (judgeType == JudgeTools.JUDGE_NUMBER_OR_LETTER)
        {
            judgeParameterIsNumberOrLetter(oo);
        }

        return judgeParameterIsNull(oo, false);
    }

    public static boolean judgeParameterIsNull(Object oo, int judgeType)
        throws MYException
    {
        return judgeParameterIsNull(new Object[] {oo}, judgeType);
    }

    public static boolean judgeParameterIsNull(Object oo, boolean regularNum)
        throws MYException
    {
        return judgeParameterIsNull(new Object[] {oo}, regularNum);
    }

    /**
     * Description: 判断入参是否为空(为空抛出异常,否则返回true)<br>
     * 对于string判断空和null
     * 
     * @param oo
     * @param regularNum
     *            是否对string判断纯数字
     * @return
     * @throws SPMException
     *             boolean
     */
    public static boolean judgeParameterIsNull(Object[] oo, boolean regularNum)
        throws MYException
    {
        if (oo == null)
        {
            throw new MYException("", "参数为空");
        }

        for (int i = 0; i < oo.length; i++ )
        {
            if (oo[i] == null)
            {
                throw new MYException("", "参数为空");
            }

            if (oo[i] instanceof String)
            {
                if (StringTools.isNullOrNone((String)oo[i]))
                {
                    throw new MYException("", "参数为空");
                }

                if (regularNum)
                {
                    if ( !RegularExpress.isGuid((String)oo[i]))
                    {
                        throw new MYException("", "不是全数字");
                    }
                }
            }
        }

        return false;
    }

    /**
     * Description: 判断入参是否为空(为空抛出异常,否则返回true)<br>
     * 对于string判断空和null
     * 
     * @param oo
     * @param regularNum
     *            是否对string判断纯数字
     * @return
     * @throws SPMException
     *             boolean
     */
    private static boolean judgeParameterIsNumberOrLetter(Object[] oo)
        throws MYException
    {
        if (oo == null)
        {
            throw new MYException("", "参数为空");
        }

        for (int i = 0; i < oo.length; i++ )
        {
            if (oo[i] == null)
            {
                throw new MYException("", "参数为空");
            }

            if (oo[i] instanceof String)
            {
                if (StringTools.isNullOrNone((String)oo[i]))
                {
                    throw new MYException("", "参数为空");
                }

                if ( !RegularExpress.isNumberOrLetter((String)oo[i]))
                {
                    throw new MYException("", "参数必须是数字或者字母");
                }
            }
        }

        return false;
    }

}
