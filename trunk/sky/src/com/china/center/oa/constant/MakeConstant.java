/**
 * File Name: MakeConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.constant;


import com.china.center.annotation.Defined;


/**
 * MakeConstant
 * 
 * @author ZHUZHU
 * @version 2009-10-8
 * @see MakeConstant
 * @since 1.0
 */
public interface MakeConstant
{
    int MAKE_TOKEN_01 = 1;

    int MAKE_TOKEN_02 = 2;

    int MAKE_TOKEN_03 = 3;

    int MAKE_TOKEN_04 = 4;

    int MAKE_TOKEN_05 = 5;

    int MAKE_TOKEN_06 = 6;

    int MAKE_TOKEN_07 = 7;

    int MAKE_TOKEN_08 = 8;

    int MAKE_TOKEN_09 = 9;

    int MAKE_TOKEN_10 = 10;

    int MAKE_TOKEN_11 = 11;

    int MAKE_TOKEN_12 = 12;

    int MAKE_TOKEN_13 = 13;

    int MAKE_TOKEN_14 = 14;

    /**
     * 定制产品
     */
    @Defined(key = "makeType", value = "定制产品")
    int MAKE_TYPE_CUSTOMIZE = 0;

    /**
     * 自由产品
     */
    @Defined(key = "makeType", value = "自有产品")
    int MAKE_TYPE_SELF = 1;

    /**
     * 是否打样--是
     */
    @Defined(key = "sampleType", value = "是")
    int SAMPLETYPE_YES = 0;

    /**
     * 是否打样--否
     */
    @Defined(key = "sampleType", value = "否")
    int SAMPLETYPE_NO = 1;

    /**
     * 付款方式-分期付款
     */
    @Defined(key = "mbillType", value = "分期付款")
    int BILLTYPE_DIRECT = 0;

    /**
     * 付款方式-款到发货
     */
    @Defined(key = "mbillType", value = "款到发货")
    int BILLTYPE_REDIRECT = 1;

    /**
     * 客户类型-普通客户
     */
    @Defined(key = "mcustomerType", value = "经销商")
    int CUSTOMERTYPE_COMMON = 0;

    /**
     * 客户类型-高级客户
     */
    @Defined(key = "mcustomerType", value = "终端")
    int CUSTOMERTYPE_SPECIAL = 1;

    /**
     * 是否估价--是
     */
    @Defined(key = "appraisalType", value = "是")
    int APPRAISALTYPE_YES = 0;

    /**
     * 是否估价--否
     */
    @Defined(key = "appraisalType", value = "否")
    int APPRAISALTYPE_NO = 1;

    /**
     * 产中估价
     */
    @Defined(key = "gujiaType", value = "产中估价(4小时内给出)")
    int GUJIATYPE_CENTER = 0;

    /**
     * 工厂报价
     */
    @Defined(key = "gujiaType", value = "工厂报价(24小时内给出)")
    int GUJIATYPE_FACTORY = 1;

    /**
     * 设计--初稿
     */
    @Defined(key = "designType", value = "初稿")
    int DESIGNTYPE_INIT = 0;

    /**
     * 设计--全稿
     */
    @Defined(key = "designType", value = "全稿")
    int DESIGNTYPE_ALL = 1;

    @Defined(key = "mappType", value = "摆件及其他类询价")
    int APP_TYPE_00 = 0;

    @Defined(key = "mappType", value = "章条类询价")
    int APP_TYPE_01 = 1;

    @Defined(key = "mappType", value = "书册类询价")
    int APP_TYPE_02 = 2;

    /**
     * 是否结束-否
     */
    int END_TOKEN_NO = 0;

    /**
     * 是否结束-是
     */
    int END_TOKEN_YES = 1;

    /**
     * 流程结束
     */
    int STATUS_END = 9999;

    /**
     * 开始
     */
    int TOKEN_BEGIN = 1;

    /**
     * 第二环的定制附件
     */
    int MAKE_FILE_TYPE_21 = 101;

    /**
     * 工艺咨询
     */
    @Defined(key = "requestType", value = "工艺咨询")
    int REQUEST_TYPE_00 = 0;

    @Defined(key = "requestType", value = "成本预估")
    int REQUEST_TYPE_01 = 1;

    @Defined(key = "requestType", value = "产中估价")
    int REQUEST_TYPE_02 = 2;

    @Defined(key = "requestType", value = "工厂报价")
    int REQUEST_TYPE_03 = 3;

    @Defined(key = "requestType", value = "初稿")
    int REQUEST_TYPE_04 = 4;

    @Defined(key = "requestType", value = "全稿")
    int REQUEST_TYPE_05 = 5;

    /**
     * 正常结束
     */
    @Defined(key = "exceptionReason", value = "正常")
    int EXCEPTION_END_0 = 0;

    @Defined(key = "exceptionReason", value = "创意未过")
    int EXCEPTION_END_1 = 1;

    @Defined(key = "exceptionReason", value = "设计未过")
    int EXCEPTION_END_2 = 2;

    @Defined(key = "exceptionReason", value = "客户需求变化")
    int EXCEPTION_END_3 = 3;

    @Defined(key = "exceptionReason", value = "工艺问题")
    int EXCEPTION_END_4 = 4;

    @Defined(key = "exceptionReason", value = "交货时间不足")
    int EXCEPTION_END_5 = 5;

    @Defined(key = "exceptionReason", value = "付款和运输条件不满足")
    int EXCEPTION_END_6 = 6;

    @Defined(key = "exceptionReason", value = "其他")
    int EXCEPTION_END_99 = 99;
}
