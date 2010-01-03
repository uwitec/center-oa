/*
 * File Name: WriteRequestAssignmentHandler.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-9
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wokflow.sale.action;


import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;


/**
 * 请求流程的操作者
 * 
 * @author zhuzhu
 * @version 2007-12-9
 * @see
 * @since
 */
public class SaveOutHandler implements AssignmentHandler
{
    public void assign(Assignable assignable, ExecutionContext executionContext)

        throws Exception
    {
        System.out.println(">>>>>>>>>>>>>>>>>SaveOutHandler<<<<<<<<<<<<<<<<<");
    }
}
