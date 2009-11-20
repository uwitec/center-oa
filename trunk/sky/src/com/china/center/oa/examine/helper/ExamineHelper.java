/**
 * File Name: ExamineHelper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-13<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.helper;


import com.china.center.oa.constant.ExamineConstant;


/**
 * ExamineHelper
 * 
 * @author zhuzhu
 * @version 2009-2-13
 * @see ExamineHelper
 * @since 1.0
 */
public abstract class ExamineHelper
{
    /**
     *  «∑Ò÷ª∂¡
     * 
     * @param status
     * @return
     */
    public static boolean isReadonly(int status)
    {
        return (status != ExamineConstant.EXAMINE_STATUS_INIT)
               && (status != ExamineConstant.EXAMINE_STATUS_REJECT);
    }
}
