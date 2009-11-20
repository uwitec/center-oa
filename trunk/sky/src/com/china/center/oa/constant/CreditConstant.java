/**
 * File Name: CreditConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-11-1<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.constant;


import com.china.center.annotation.Defined;


/**
 * CreditConstant
 * 
 * @author ZHUZHU
 * @version 2009-11-1
 * @see CreditConstant
 * @since 1.0
 */
public interface CreditConstant
{
    /**
     * static credit
     */
    @Defined(key = "creditType", value = "静态指标")
    int CREDIT_TYPE_STATIC = 0;

    /**
     * dynamic credit
     */
    @Defined(key = "creditType", value = "动态指标")
    int CREDIT_TYPE_DYNAMIC = 1;

    /**
     * percent item
     */
    @Defined(key = "creditItemType", value = "百分制")
    int CREDIT_ITEM_TYPE_PERCENT = 0;

    /**
     * real item
     */
    @Defined(key = "creditItemType", value = "实际值")
    int CREDIT_ITEM_TYPE_REAL = 1;

    /**
     * obverse face
     */
    @Defined(key = "creditItemFace", value = "正向指标")
    int CREDIT_ITEM_FACE_OBVERSE = 0;

    /**
     * negative face
     */
    @Defined(key = "creditItemFace", value = "负向指标")
    int CREDIT_ITEM_FACE_NEGATIVE = 1;

    /**
     * sub yes
     */
    @Defined(key = "creditItemSub", value = "子项设值")
    int CREDIT_ITEM_SUB_YES = 0;

    /**
     * sub no
     */
    @Defined(key = "creditItemSub", value = "直接设值")
    int CREDIT_ITEM_SUB_NO = 1;
}
