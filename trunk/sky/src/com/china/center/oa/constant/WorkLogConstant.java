/**
 * File Name: WorkLogConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.constant;


import com.china.center.annotation.Defined;


/**
 * WorkLogConstant
 * 
 * @author zhuzhu
 * @version 2009-2-15
 * @see WorkLogConstant
 * @since 1.0
 */
public interface WorkLogConstant
{
    /**
     * 日志初始
     */
    @Defined(key = "workLogStatus", value = "初始")
    int WORKLOG_STATUS_INIT = 0;

    /**
     * 日志提交
     */
    @Defined(key = "workLogStatus", value = "提交")
    int WORKLOG_STATUS_SUBMIT = 1;

    /**
     * 正常提交
     */
    @Defined(key = "workLogResult", value = "正常")
    int WORKLOG_RESULT_COMMON = 0;

    /**
     * 正常提交
     */
    @Defined(key = "workLogResult", value = "延期")
    int WORKLOG_RESULT_LAZY = 1;
    
    /**
     * 拜访
     */
    int WORKTYPE_VISIT = 0;
}
