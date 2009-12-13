/**
 * File Name: MakeHelper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-13<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.helper;


import com.china.center.oa.constant.MakeConstant;


/**
 * MakeHelper
 * 
 * @author ZHUZHU
 * @version 2009-10-13
 * @see MakeHelper
 * @since 1.0
 */
public abstract class MakeHelper
{
    /**
     * findForwardHandler
     * 
     * @param token
     * @return
     */
    public static String findForwardHandler(int token)
    {
        if (MakeConstant.MAKE_TOKEN_01 == token)
        {
            return "detailMake01";
        }

        return "detailMake02";
    }

    /**
     * findForwardUpdate
     * 
     * @param token
     * @return
     */
    public static String findForwardUpdate(int token)
    {
        if (MakeConstant.MAKE_TOKEN_01 == token)
        {
            return "updateMake01";
        }

        return "updateMake01";
    }
}
