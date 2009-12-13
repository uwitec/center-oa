/**
 * File Name: customerFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.facade;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.MYException;
import com.china.center.oa.constant.AuthConstant;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.worklog.bean.WorkLogBean;
import com.china.center.oa.worklog.manager.WorkLogManager;
import com.china.center.tools.JudgeTools;


/**
 * customerFacade(权限控制)
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see WorkLogFacade
 * @since 1.0
 */
@Bean(name = "workLogFacade")
public class WorkLogFacade extends AbstarctFacade
{
    private WorkLogManager workLogManager = null;

    private UserManager userManager = null;

    public WorkLogFacade()
    {}

    /**
     * 增加日志
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addWorkLog(String userId, WorkLogBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.WORKLOG_OPR))
        {
            return workLogManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * update日志
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateWorkLog(String userId, WorkLogBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.WORKLOG_OPR))
        {
            return workLogManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 提交
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean submitWorkLog(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.WORKLOG_OPR))
        {
            return workLogManager.submitBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 删除日志
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean delWorkLog(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.WORKLOG_OPR))
        {
            return workLogManager.delBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * @return the workLogManager
     */
    public WorkLogManager getWorkLogManager()
    {
        return workLogManager;
    }

    /**
     * @param workLogManager
     *            the workLogManager to set
     */
    public void setWorkLogManager(WorkLogManager workLogManager)
    {
        this.workLogManager = workLogManager;
    }

    /**
     * @return the userManager
     */
    public UserManager getUserManager()
    {
        return userManager;
    }

    /**
     * @param userManager
     *            the userManager to set
     */
    public void setUserManager(UserManager userManager)
    {
        this.userManager = userManager;
    }
}
