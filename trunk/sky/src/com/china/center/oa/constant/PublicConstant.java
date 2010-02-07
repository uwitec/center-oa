/**
 * File Name: PublicConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.constant;

/**
 * PublicConstant
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see
 * @since
 */
public interface PublicConstant
{
    /**
     * 可见
     */
    int ROLE_VISIBLE_YES = 1;

    /**
     * 不可见
     */
    int ROLE_VISIBLE_NO = 0;

    /**
     * 根权限的级别
     */
    int ROLE_LEVEL_ROOT = 0;

    /**
     * 区域权限
     */
    int AUTH_TYPE_LOCATION = 0;

    /**
     * 系统权限
     */
    int AUTH_TYPE_SYSTEM = 1;

    /**
     * 男
     */
    int SEX_MAN = 0;

    /**
     * 女
     */
    int SEX_WOMAN = 0;

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
     * 根节点
     */
    int BOTTOMFLAG_NO = 0;

    /**
     * 叶节点
     */
    int BOTTOMFLAG_YES = 1;

    /**
     * 虚拟区域
     */
    String VIRTUAL_LOCATION = "-1";

    /**
     * 总部区域
     */
    String CENTER_LOCATION = "0";

    /**
     * 超级管理员
     */
    String SUPER_MANAGER = "0";

    /**
     * 密码最小长度
     */
    int PASSWORD_MIN_LENGTH = 10;

    /**
     * 组织结构的顶层
     */
    int ORG_ROOT = 0;

    /**
     * 组织结构的第一层
     */
    int ORG_FIRST = 1;

    /**
     * 操作--提交
     */
    String OPERATION_SUBMIT = "提交";

    /**
     * 操作--通过
     */
    String OPERATION_PASS = "通过";

    /**
     * 操作--驳回
     */
    String OPERATION_REJECT = "驳回";

    /**
     * 操作--异常结束
     */
    String OPERATION_EXCEPTIONEND = "异常结束";

    /**
     * 变更
     */
    String OPERATION_CHANGE = "变更";

    /**
     * 变更
     */
    String OPERATION_DEL = "删除";
}
