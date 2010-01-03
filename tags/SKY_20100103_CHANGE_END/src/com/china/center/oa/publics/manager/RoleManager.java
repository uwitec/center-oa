/**
 * File Name: RoleManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.RoleBean;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.RoleAuthDAO;
import com.china.center.oa.publics.dao.RoleDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.vo.RoleVO;
import com.china.center.oa.publics.vs.RoleAuthBean;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;


/**
 * RoleManager
 * 
 * @author zhuzhu
 * @version 2008-11-15
 * @see RoleManager
 * @since 1.0
 */
@Bean(name = "roleManager")
public class RoleManager
{
    private RoleDAO roleDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private UserDAO userDAO = null;

    private RoleAuthDAO roleAuthDAO = null;

    /**
     * default constructor
     */
    public RoleManager()
    {}

    /**
     * addBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean addBean(User user, RoleBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        bean.setId(commonDAO2.getSquenceString());

        roleDAO.saveEntityBean(bean);

        List<RoleAuthBean> auth = bean.getAuth();

        if ( !ListTools.isEmptyOrNull(auth))
        {
            // 增加角色的权限
            for (RoleAuthBean roleAuthBean : auth)
            {
                roleAuthBean.setRoleId(bean.getId());

                roleAuthDAO.saveEntityBean(roleAuthBean);
            }
        }

        return true;
    }

    /**
     * updateBean(只更新权限)
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, RoleBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        RoleBean oldBean = roleDAO.find(bean.getId());

        if (oldBean == null)
        {
            throw new MYException("人员不存在");
        }

        List<RoleAuthBean> auth = bean.getAuth();

        roleAuthDAO.deleteEntityBeansByFK(bean.getId());

        if ( !ListTools.isEmptyOrNull(auth))
        {
            // 增加角色的权限
            for (RoleAuthBean roleAuthBean : auth)
            {
                roleAuthBean.setRoleId(bean.getId());

                roleAuthDAO.saveEntityBean(roleAuthBean);
            }
        }

        return true;
    }

    /**
     * delBean
     * 
     * @param user
     * @param stafferId
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        RoleBean bean = roleDAO.find(id);

        if (bean == null)
        {
            throw new MYException("人员不存在");
        }

        checkDelBean(id);

        roleDAO.deleteEntityBean(id);

        roleAuthDAO.deleteEntityBeansByFK(id);

        return true;
    }

    @Exceptional
    public RoleVO findVO(String id)
        throws MYException
    {
        RoleVO vo = roleDAO.findVO(id);

        if (vo == null)
        {
            throw new MYException("角色不存在");
        }

        vo.setAuth(roleAuthDAO.queryEntityBeansByFK(id));

        return vo;
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(RoleBean bean)
        throws MYException
    {
        if (roleDAO.countByUnique(bean.getName(), bean.getLocationId()) > 0)
        {
            throw new MYException("分公司下角色名称[%s]已经存在", bean.getName());
        }
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkDelBean(String id)
        throws MYException
    {
        // 存在人员或者user
        if (userDAO.countByRoleId(id) > 0)
        {
            throw new MYException("角色被用户绑定无法删除");
        }
    }

    /**
     * @return the roleDAO
     */
    public RoleDAO getRoleDAO()
    {
        return roleDAO;
    }

    /**
     * @param roleDAO
     *            the roleDAO to set
     */
    public void setRoleDAO(RoleDAO roleDAO)
    {
        this.roleDAO = roleDAO;
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
     * @return the roleAuthDAO
     */
    public RoleAuthDAO getRoleAuthDAO()
    {
        return roleAuthDAO;
    }

    /**
     * @param roleAuthDAO
     *            the roleAuthDAO to set
     */
    public void setRoleAuthDAO(RoleAuthDAO roleAuthDAO)
    {
        this.roleAuthDAO = roleAuthDAO;
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }
}
