/**
 * File Name: SailConstanst.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.constanst;

/**
 * SailConstanst
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see SailConstant
 * @since 1.0
 */
public interface SailConstant
{
    int PAGE_SIZE = 20;

    int PAGE_COMMON_SIZE = 10;

    int PAGE_EXPORT = 500;

    /**
     * 0
     */
    String SYSTEM_LOCATION = "0";

    String VERSION10 = "1.0";

    String VERSION20 = "2.0";

    String VERSION30 = "3.0";

    String VERSION40 = "4.0";

    String CURRENT_VERSION = "2.0";

    String OUT_VERSION10 = "OUT";

    String OUT_VERSION20 = "OUT2.0";

    int VERSION_INDEX = 99;

    /**
     * 根节点
     */
    int BOTTOMFLAG_NO = 0;

    /**
     * 叶节点
     */
    int BOTTOMFLAG_YES = 1;

    /**
     * 收款单
     */
    int BILL_RECIVE = 0;

    /**
     * 付款单
     */
    int BILL_PAY = 1;

    /**
     * 区域内付账
     */
    int BILL_TYPE_CHANGE = 2;

    /**
     * 跨区域收账
     */
    int BILL_TYPE_CHANGE_IN_CITY = 3;

    /**
     * 跨区域付账
     */
    int BILL_TYPE_CHANGE_CITY = 4;

    /**
     * 正式保存
     */
    int TEMPTYPE_COMMON = 0;

    /**
     * 临时保存
     */
    int TEMPTYPE_TEMP = 1;

    /**
     * 权限验证数字
     */
    char AUTH_PASS = '1';

    /**
     * 可见
     */
    int ROLE_VISIBLE_YES = 1;

    /**
     * 不可见
     */
    int ROLE_VISIBLE_NO = 0;

    /**
     * 超级管理员
     */
    int SUPER_SYSTEM_ROLE = 3;

    /**
     * 密码最小长度
     */
    int PASSWORD_MIN_LENGTH = 10;

    /**
     * 全局session区域表示
     */
    String CURRENTLOCATIONID = "currentLocationId";

    /**
     * 锁定
     */
    int LOGIN_STATUS_LOCK = 1;

    /**
     * 状态正常
     */
    int LOGIN_STATUS_COMMON = 0;

    /**
     * 最大登录失败次数
     */
    int LOGIN_FAIL_MAX = 5;

    /**
     * 库单提交
     */
    int LOG_OPR_SUBMIT = 0;

    /**
     * 库单驳回
     */
    int LOG_OPR_REJECT = 1;

    /**
     * 良品仓
     */
    int TYPE_DEPOTPART_OK = 1;

    /**
     * 次品仓
     */
    int TYPE_DEPOTPART_BAD = 0;

    /**
     * 报废仓
     */
    int TYPE_DEPOTPART_FEI = 2;

    /**
     * 初始化
     */
    int OPR_STORAGE_INIT = 0;

    /**
     * 修改
     */
    int OPR_STORAGE_UPDATE = 1;

    /**
     * 库单通过
     */
    int OPR_STORAGE_OUTBILL = 2;

    /**
     * 入库单
     */
    int OPR_STORAGE_OUTBILLIN = 3;

    /**
     * 移动
     */
    int OPR_STORAGE_MOVE = 4;

    /**
     * 合成
     */
    int OPR_STORAGE_COMPOSE = 5;

    /**
     * 审批通过
     */
    int OPR_OUT_PASS = 0;

    /**
     * 审批驳回
     */
    int OPR_OUT_REJECT = 1;

    /**
     * 运输的分类
     */
    int TRANSPORT_TYPE = 0;

    /**
     * 运输方式
     */
    int TRANSPORT_COMMON = 1;

    /**
     * 发货单初始态
     */
    int CONSIGN_INIT = 1;

    /**
     * 发货单的通过态
     */
    int CONSIGN_PASS = 2;

    /**
     * 发货单的回复 初始
     */
    int CONSIGN_REPORT_INIT = 0;

    /**
     * 发货单的回复 正常收货
     */
    int CONSIGN_REPORT_COMMON = 1;

    /**
     * 发货单的回复 异常收货
     */
    int CONSIGN_REPORT_EXCEPTION = 2;

    // <option value="1">调出</option>
    // <option value="4">调入</option>

    /**
     * 货物异常单的初始态
     */
    int PRO_EXCEPTION_INIT = 0;

    /**
     * 提交
     */
    int PRO_EXCEPTION_SUBMIT = 1;

    /**
     * 驳回
     */
    int PRO_EXCEPTION_REJECT = 2;

    /**
     * 通过
     */
    int PRO_EXCEPTION_PASS = 3;

    /**
     * 男
     */
    int SEX_MALE = 0;

    /**
     * 女
     */
    int SEX_WOMAN = 1;

    /**
     * 通过操作
     */
    int OPRMODE_PASS = 0;

    /**
     * 驳回
     */
    int OPRMODE_REJECT = 1;

    int ROLE_TOP = 3;

    String SYSTEM_USER = "999999";
}
