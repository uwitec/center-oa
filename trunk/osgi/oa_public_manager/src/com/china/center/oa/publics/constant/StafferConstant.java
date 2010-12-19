/**
 * File Name: StafferConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.constant;


import com.china.center.jdbc.annotation.Defined;


/**
 * StafferConstant
 * 
 * @author ZHUZHU
 * @version 2009-2-8
 * @see StafferConstant
 * @since 1.0
 */
public interface StafferConstant
{
    /**
     * 系统管理员
     */
    String SUPER_STAFFER = "99999999";

    @Defined(key = "examType", value = "终端")
    int EXAMTYPE_TERMINAL = 0;

    @Defined(key = "examType", value = "拓展")
    int EXAMTYPE_EXPAND = 1;

    @Defined(key = "examType", value = "其他")
    int EXAMTYPE_OTHER = 99;

    @Defined(key = "stafferStatus", value = "正常")
    int STATUS_COMMON = 0;

    @Defined(key = "stafferStatus", value = "废弃")
    int STATUS_DROP = 99;

    @Defined(key = "stafferBlack", value = "正常")
    int BLACK_NO = 0;

    @Defined(key = "stafferBlack", value = "黑名单")
    int BLACK_YES = 1;

    int LEVER_DEFAULT = 1;
}
