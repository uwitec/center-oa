/**
 * File Name: CustomerConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.constant;


import com.china.center.annotation.Defined;


/**
 * CustomerConstant
 * 
 * @author ZHUZHU
 * @version 2008-11-9
 * @see CustomerConstant
 * @since 1.0
 */
public interface CustomerConstant
{
    /**
     * ok
     */
    int STATUS_OK = 0;

    /**
     * apply
     */
    int STATUS_APPLY = 1;

    /**
     * 审批后等待分配code
     */
    int STATUS_WAIT_CODE = 3;

    /**
     * reject
     */
    int STATUS_REJECT = 2;

    int OPR_ADD = 0;

    int OPR_UPDATE = 1;

    int OPR_DEL = 2;

    /**
     * UPATE_CREDIT
     */
    int OPR_UPATE_CREDIT = 3;

    /**
     * 空闲的客户
     */
    int REAL_STATUS_IDLE = 0;

    /**
     * 被使用的客户
     */
    int REAL_STATUS_USED = 1;

    /**
     * 申请中的客户
     */
    int REAL_STATUS_APPLY = 2;

    /**
     * 无成交记录
     */
    int BLOG_NO = 0;

    /**
     * 有成交记录
     */
    int BLOG_YES = 1;

    /**
     * 无名片
     */
    int CARD_NO = 0;

    /**
     * 有名片
     */
    int CARD_YES = 1;

    /**
     * 回收全部
     */
    int RECLAIMSTAFFER_ALL = 0;

    /**
     * 回收拓展
     */
    int RECLAIMSTAFFER_EXPEND = 1;

    /**
     * 回收终端
     */
    int RECLAIMSTAFFER_TER = 2;

    /**
     * 终端
     */
    int SELLTYPE_TER = 0;

    /**
     * 拓展
     */
    int SELLTYPE_EXPEND = 1;

    @Defined(key = "checkStatus", value = "未核对")
    int HIS_CHECK_NO = 0;

    @Defined(key = "checkStatus", value = "核对")
    int HIS_CHECK_YES = 1;

    /**
     * 是新客户
     */
    int HASNEW_YES = 0;

    /**
     * 不是新客户
     */
    int HASNEW_NO = 1;

    /**
     * 新客户
     */
    @Defined(key = "c_newType", value = "新客户")
    int NEWTYPE_NEW = 0;

    /**
     * 老客户
     */
    @Defined(key = "c_newType", value = "老客户")
    int NEWTYPE_OLD = 1;

    /**
     * 默认级别
     */
    String CREDITLEVELID_DEFAULT = "90000000000000000001";
}
