/**
 * File Name: HandleQueryCondition.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-9-10<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.common;


import javax.servlet.http.HttpServletRequest;

import com.china.center.jdbc.util.ConditionParse;


/**
 * HandleQueryCondition
 * 
 * @author ZHUZHU
 * @version 2011-9-10
 * @see HandleQueryCondition
 * @since 1.0
 */
public interface HandleQueryCondition
{
    /**
     * 处理查询条件
     * 
     * @param request
     * @param condtion
     */
    void setQueryCondition(HttpServletRequest request, ConditionParse condtion);
}
