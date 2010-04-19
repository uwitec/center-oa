/**
 * File Name: UserManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.center.oa.constant.PublicConstant;
import com.china.center.oa.constant.StafferConstant;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.RoleBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.bean.UserBean;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.RoleAuthDAO;
import com.china.center.oa.publics.dao.RoleDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.vo.UserVO;
import com.china.center.oa.publics.vs.RoleAuthBean;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;


/**
 * UserManager
 * 
 * @author zhuzhu
 * @version 2008-11-9
 * @see UserManager
 * @since 1.0
 */
@Bean(name = "userManager")
public class UserManager
{
    private UserDAO userDAO = null;

    private RoleDAO roleDAO = null;

    private RoleAuthDAO roleAuthDAO = null;

    private StafferDAO stafferDAO = null;

    private CommonDAO2 commonDAO2 = null;

    public UserManager()
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
    public boolean addBean(User user, UserBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        // 先增加角色
        addRoleInner(bean);

        userDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * 增加角色
     * 
     * @param bean
     */
    private void addRoleInner(UserBean bean)
    {
        RoleBean role = new RoleBean();

        role.setId(commonDAO2.getSquenceString());

        role.setLocationId(bean.getLocationId());

        role.setName(bean.getName());

        role.setAuth(bean.getAuth());

        roleDAO.saveEntityBean(role);

        List<RoleAuthBean> auth = role.getAuth();

        if ( !ListTools.isEmptyOrNull(auth))
        {
            // 增加角色的权限
            for (RoleAuthBean roleAuthBean : auth)
            {
                roleAuthBean.setRoleId(role.getId());

                roleAuthDAO.saveEntityBean(roleAuthBean);
            }
        }

        bean.setRoleId(role.getId());
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(UserBean bean)
        throws MYException
    {
        if (userDAO.countByUnique(bean.getName()) > 0)
        {
            throw new MYException("用户名[%s]已经存在", bean.getName());
        }

        StafferBean sb = stafferDAO.find(bean.getStafferId());

        if (sb == null)
        {
            throw new MYException("职员不存在");
        }

        if (sb.getStatus() != StafferConstant.STATUS_COMMON)
        {
            throw new MYException("职员已经废弃");
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
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, UserBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        UserBean oldBean = userDAO.find(bean.getId());

        if (oldBean == null)
        {
            throw new MYException("用户不存在");
        }

        bean.setName(oldBean.getName());

        bean.setLocationId(oldBean.getLocationId());

        bean.setPassword(oldBean.getPassword());

        userDAO.updateEntityBean(bean);

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

        UserBean bean = userDAO.find(id);

        if (bean == null)
        {
            throw new MYException("用户不存在");
        }

        checkDelBean(user, id);

        userDAO.deleteEntityBean(id);

        // 然后删除角色 因为是一一对应的
        delRoleInner(bean);

        return true;
    }

    /**
     * 删除role
     * 
     * @param id
     * @param bean
     * @throws MYException
     */
    private void delRoleInner(UserBean bean)
        throws MYException
    {
        checkDelBean(bean.getRoleId());

        roleDAO.deleteEntityBean(bean.getRoleId());

        roleAuthDAO.deleteEntityBeansByFK(bean.getRoleId());
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
     * @param bean
     * @throws MYException
     */
    private void checkDelBean(User user, String id)
        throws MYException
    {
        if (user.getId().equals(id))
        {
            throw new MYException("不能删除自己");
        }

    }

    @Exceptional
    public User findUser(String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        UserVO user = userDAO.findVO(id);

        if (user == null)
        {
            return null;
        }

        List<RoleAuthBean> auth = roleAuthDAO.queryEntityBeansByFK(user.getRoleId());

        user.setAuth(auth);

        return user;
    }

    public boolean containAuth(User user, String... authId)
    {
        List<RoleAuthBean> authList = user.getAuth();

        for (RoleAuthBean roleAuthBean : authList)
        {
            for (String string : authId)
            {
                if (roleAuthBean.getAuthId().equals(string))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param id
     * @param authId
     * @return
     * @throws MYException
     */
    public boolean containAuth(String id, String authId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id, authId);

        User user = findUser(id);

        JudgeTools.judgeParameterIsNull(user);

        List<RoleAuthBean> authList = user.getAuth();

        for (RoleAuthBean roleAuthBean : authList)
        {
            if (roleAuthBean.getAuthId().equals(authId))
            {
                return true;
            }
        }

        return false;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updatePassword(String id, String password)
    {
        return this.userDAO.updatePassword(id, password);
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateStatus(String id, int status)
    {
        if (status == PublicConstant.LOGIN_STATUS_COMMON)
        {
            userDAO.updateFail(id, 0);
        }

        return this.userDAO.updateStatus(id, status);
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateLocation(String id, String locationId)
    {
        return this.userDAO.updateLocation(id, locationId);
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateFail(String id, int fail)
    {
        return this.userDAO.updateFail(id, fail);
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateLogTime(String id, String logTime)
    {
        return this.userDAO.updateLogTime(id, logTime);
    }

    /**
     * 过滤不正常的职员
     * 
     * @param locationId
     * @return
     */
    public Map queryStafferAndRoleByLocationId(String locationId)
    {
        Map map = new HashMap();

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("StafferBean.locationId", "=", locationId);

        condtion.addIntCondition("StafferBean.status", "=", StafferConstant.STATUS_COMMON);

        List<StafferBean> stafferList = stafferDAO.queryEntityBeansByCondition(condtion);

        List<RoleBean> roleList = roleDAO.queryRoleByLocationId(locationId);

        map.put("stafferList", stafferList);

        map.put("roleList", roleList);

        return map;

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
     * @return the stafferDAO
     */
    public StafferDAO getStafferDAO()
    {
        return stafferDAO;
    }

    /**
     * @param stafferDAO
     *            the stafferDAO to set
     */
    public void setStafferDAO(StafferDAO stafferDAO)
    {
        this.stafferDAO = stafferDAO;
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
}
