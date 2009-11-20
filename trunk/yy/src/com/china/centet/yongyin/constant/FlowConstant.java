/**
 *
 */
package com.china.centet.yongyin.constant;

/**
 * @author Administrator
 */
public interface FlowConstant
{
    /**
     * 人员式流程
     */
    int FLOW_TYPE_USER = 0;

    /**
     * 角色式流程
     */
    int FLOW_TYPE_ROLE = 1;

    /**
     * 全部
     */
    int FLOW_TYPE_ALL = 2;

    /**
     * 职员处理
     */
    int FLOW_TYPE_STAFFER = 3;

    /**
     * 无
     */
    int FLOW_TYPE_NONE = 99;

    /**
     * 流程初始化
     */
    int FLOW_STATUS_INIT = 0;

    /**
     * 流程发布
     */
    int FLOW_STATUS_REALSE = 1;

    /**
     * 流程废弃
     */
    int FLOW_STATUS_DROP = 2;

    /**
     * 流程实例开始
     */
    int FLOW_INSTANCE_BEGIN = 0;

    /**
     * 流程实例结束
     */
    int FLOW_INSTANCE_END = 99;

    /**
     * FK的userId的index值
     */
    int FK_INDEX_USERID = 1;
}
