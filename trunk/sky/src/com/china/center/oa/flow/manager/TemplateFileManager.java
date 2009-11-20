/**
 * File Name: TemplateFileManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.manager;


import java.io.Serializable;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.annosql.constant.AnoConstant;
import com.china.center.common.MYException;
import com.china.center.oa.flow.bean.TemplateFileBean;
import com.china.center.oa.flow.dao.FlowVSTemplateDAO;
import com.china.center.oa.flow.dao.TemplateFileDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.TimeTools;


/**
 * TemplateFileManager
 * 
 * @author zhuzhu
 * @version 2009-4-21
 * @see TemplateFileManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "templateFileManager")
public class TemplateFileManager
{
    private TemplateFileDAO templateFileDAO = null;

    private FlowVSTemplateDAO flowVSTemplateDAO = null;

    private CommonDAO2 commonDAO2 = null;

    /**
     * default constructor
     */
    public TemplateFileManager()
    {}

    /**
     * addBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addBean(User user, TemplateFileBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAdd(user, bean);

        bean.setId(commonDAO2.getSquenceString20());

        bean.setLogTime(TimeTools.now());

        templateFileDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * checkAdd
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void checkAdd(User user, TemplateFileBean bean)
        throws MYException
    {
        if (templateFileDAO.countByUnique(bean.getName()) != 0)
        {
            throw new MYException("模板名称[%s]重复", bean.getName());
        }
    }

    /**
     * deleteBean
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean deleteBean(User user, Serializable id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDelete(user, id);

        templateFileDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * checkAdd
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void checkDelete(User user, Serializable id)
        throws MYException
    {
        // check if ref by flow
        if (flowVSTemplateDAO.countByFK(id, AnoConstant.FK_FIRST) > 0)
        {
            throw new MYException("模板已经被流程使用,不能删除");
        }
    }

    /**
     * updateBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, TemplateFileBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkUpdate(user, bean);

        bean.setLogTime(TimeTools.now());

        templateFileDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * checkAdd
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void checkUpdate(User user, TemplateFileBean bean)
        throws MYException
    {
        TemplateFileBean old = templateFileDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("模板[%s]不存在", bean.getName());
        }

        if ( !old.getName().equals(bean.getName()))
        {
            if (templateFileDAO.countByUnique(bean.getName()) != 0)
            {
                throw new MYException("模板名称[%s]重复", bean.getName());
            }
        }

        // 不能修改下面的属性
        bean.setPath(old.getPath());

        bean.setFileName(old.getFileName());
    }

    /**
     * @return the templateFileDAO
     */
    public TemplateFileDAO getTemplateFileDAO()
    {
        return templateFileDAO;
    }

    /**
     * @param templateFileDAO
     *            the templateFileDAO to set
     */
    public void setTemplateFileDAO(TemplateFileDAO templateFileDAO)
    {
        this.templateFileDAO = templateFileDAO;
    }

    /**
     * @return the commonDAO2
     */
    public CommonDAO2 getCommonDAO2()
    {
        return commonDAO2;
    }

    /**
     * @param commonDAO2
     *            the commonDAO2 to set
     */
    public void setCommonDAO2(CommonDAO2 commonDAO2)
    {
        this.commonDAO2 = commonDAO2;
    }

    /**
     * @return the flowVSTemplateDAO
     */
    public FlowVSTemplateDAO getFlowVSTemplateDAO()
    {
        return flowVSTemplateDAO;
    }

    /**
     * @param flowVSTemplateDAO
     *            the flowVSTemplateDAO to set
     */
    public void setFlowVSTemplateDAO(FlowVSTemplateDAO flowVSTemplateDAO)
    {
        this.flowVSTemplateDAO = flowVSTemplateDAO;
    }
}
