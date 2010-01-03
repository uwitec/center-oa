/**
 * File Name: customerFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.facade;


import java.io.Serializable;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.MYException;
import com.china.center.oa.constant.AuthConstant;
import com.china.center.oa.flow.bean.FlowDefineBean;
import com.china.center.oa.flow.bean.FlowViewerBean;
import com.china.center.oa.flow.bean.TemplateFileBean;
import com.china.center.oa.flow.manager.FlowInstanceManager;
import com.china.center.oa.flow.manager.FlowManager;
import com.china.center.oa.flow.manager.TemplateFileManager;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.JudgeTools;


/**
 * MailGroupFacade(È¨ÏÞ¿ØÖÆ)
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see WorkFlowFacade
 * @since 1.0
 */
@Bean(name = "workFlowFacade")
public class WorkFlowFacade extends AbstarctFacade
{
    private TemplateFileManager templateFileManager = null;

    private FlowManager flowManager = null;

    private FlowInstanceManager flowInstanceManager = null;

    private UserManager userManager = null;

    public WorkFlowFacade()
    {}

    /**
     * add TemplateFile
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addTemplateFile(String userId, TemplateFileBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.FLOW_TEMPLATE_OPR))
        {
            return templateFileManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateTemplateFile
     * 
     * @param userId
     * @param bean
     * @param rootPath
     *            rootPath
     * @return
     * @throws MYException
     */
    public boolean updateTemplateFile(String userId, TemplateFileBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.FLOW_TEMPLATE_OPR))
        {
            return templateFileManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * deleteTemplateFile
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean deleteTemplateFile(String userId, Serializable id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.FLOW_TEMPLATE_OPR))
        {
            return templateFileManager.deleteBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * addFlowDefine
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addFlowDefine(String userId, FlowDefineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.FLOW_DEFINED_OPR))
        {
            return flowManager.addFlowDefine(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateFlowDefine
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateFlowDefine(String userId, FlowDefineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.FLOW_DEFINED_OPR))
        {
            return flowManager.updateFlowDefine(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * configFlowToken
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean configFlowToken(String userId, FlowDefineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.FLOW_DEFINED_OPR))
        {
            return flowManager.configFlowToken(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * configFlowView
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean configFlowView(String userId, FlowViewerBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.FLOW_DEFINED_OPR))
        {
            return flowManager.configFlowView(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * deleteFlowDefine
     * 
     * @param userId
     * @param flowId
     * @return
     * @throws MYException
     */
    public boolean deleteFlowDefine(String userId, Serializable flowId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, flowId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.FLOW_DEFINED_OPR))
        {
            return flowManager.delFlowDefine(user, flowId);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * dropFlowDefine
     * 
     * @param userId
     * @param flowId
     * @return
     * @throws MYException
     */
    public boolean dropFlowDefine(String userId, Serializable flowId, boolean forceDrop)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, flowId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user,
            (forceDrop ? AuthConstant.FLOW_DEFINED_FORCE_DROP : AuthConstant.FLOW_DEFINED_OPR)))
        {
            return flowManager.dropFlowDefine(user, flowId, forceDrop);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * @return the templateFileManager
     */
    public TemplateFileManager getTemplateFileManager()
    {
        return templateFileManager;
    }

    /**
     * @param templateFileManager
     *            the templateFileManager to set
     */
    public void setTemplateFileManager(TemplateFileManager templateFileManager)
    {
        this.templateFileManager = templateFileManager;
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

    /**
     * @return the flowManager
     */
    public FlowManager getFlowManager()
    {
        return flowManager;
    }

    /**
     * @param flowManager
     *            the flowManager to set
     */
    public void setFlowManager(FlowManager flowManager)
    {
        this.flowManager = flowManager;
    }

    /**
     * @return the flowInstanceManager
     */
    public FlowInstanceManager getFlowInstanceManager()
    {
        return flowInstanceManager;
    }

    /**
     * @param flowInstanceManager
     *            the flowInstanceManager to set
     */
    public void setFlowInstanceManager(FlowInstanceManager flowInstanceManager)
    {
        this.flowInstanceManager = flowInstanceManager;
    }
}
