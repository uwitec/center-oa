/**
 * File Name: GroupManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.group.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.annosql.constant.AnoConstant;
import com.china.center.common.MYException;
import com.china.center.oa.constant.GroupConstant;
import com.china.center.oa.flow.dao.FlowViewerDAO;
import com.china.center.oa.flow.dao.TokenVSHanderDAO;
import com.china.center.oa.group.bean.GroupBean;
import com.china.center.oa.group.dao.GroupDAO;
import com.china.center.oa.group.dao.GroupVSStafferDAO;
import com.china.center.oa.group.vs.GroupVSStafferBean;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;


/**
 * GroupManager
 * 
 * @author ZHUZHU
 * @version 2009-4-7
 * @see GroupManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "groupManager")
public class GroupManager
{
    private GroupDAO groupDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private GroupVSStafferDAO groupVSStafferDAO = null;

    private FlowViewerDAO flowViewerDAO = null;

    private TokenVSHanderDAO tokenVSHanderDAO = null;

    /**
     * default constructor
     */
    public GroupManager()
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
    public boolean addBean(User user, GroupBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getName());

        checkAdd(user, bean);

        bean.setStafferId(user.getStafferId());

        bean.setId(commonDAO2.getSquenceString20());

        groupDAO.saveEntityBean(bean);

        List<GroupVSStafferBean> items = bean.getItems();

        // save items
        if ( !ListTools.isEmptyOrNull(items))
        {
            for (GroupVSStafferBean groupVSStafferBean : items)
            {
                groupVSStafferBean.setGroupId(bean.getId());

                groupVSStafferDAO.saveEntityBean(groupVSStafferBean);
            }
        }

        return true;
    }

    /**
     * checkAdd
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void checkAdd(User user, GroupBean bean)
        throws MYException
    {
        // handle duplicate name
        if (groupDAO.countByNameAndStafferId(bean.getName(), user.getStafferId()) > 0)
        {
            throw new MYException("群组名称[%s]重复", bean.getName());
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
    public boolean updateBean(User user, GroupBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getName());

        checkUpdate(user, bean);

        groupDAO.updateEntityBean(bean);

        List<GroupVSStafferBean> items = bean.getItems();

        // save items
        if ( !ListTools.isEmptyOrNull(items))
        {
            // delete old items
            groupVSStafferDAO.deleteEntityBeansByFK(bean.getId());

            // save new items
            for (GroupVSStafferBean groupVSStafferBean : items)
            {
                groupVSStafferBean.setGroupId(bean.getId());

                groupVSStafferDAO.saveEntityBean(groupVSStafferBean);
            }
        }

        return true;
    }

    /**
     * checkUpdate
     * 
     * @param user
     * @param bean
     * @throws MYException
     */
    private void checkUpdate(User user, GroupBean bean)
        throws MYException
    {
        GroupBean old = groupDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("群组[%s]不存在,请核实", bean.getName());
        }

        bean.setStafferId(old.getStafferId());

        if ( !user.getStafferId().equals(old.getStafferId())
            && bean.getType() != GroupConstant.GROUP_TYPE_SYSTEM)
        {
            throw new MYException("只能操作自己创建的群组");
        }

        // handle duplicate name
        if ( !old.getName().equals(bean.getName()))
        {
            if (groupDAO.countByNameAndStafferId(bean.getName(), user.getStafferId()) > 0)
            {
                throw new MYException("群组名称[%s]重复", bean.getName());
            }
        }

        bean.setType(old.getType());
    }

    /**
     * delBean
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDel(user, id);

        groupDAO.deleteEntityBean(id);

        groupVSStafferDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /**
     * checkDel
     * 
     * @param user
     * @param id
     * @throws MYException
     */
    private void checkDel(User user, String id)
        throws MYException
    {
        GroupBean old = groupDAO.find(id);

        if (old == null)
        {
            throw new MYException("群组不存在,请核实");
        }

        if ( !user.getStafferId().equals(old.getStafferId()))
        {
            throw new MYException("只能操作自己创建的群组");
        }

        if (old.getType() == GroupConstant.GROUP_TYPE_SYSTEM)
        {
            throw new MYException("不能删除系统群组");
        }

        if (tokenVSHanderDAO.countByFK(id, AnoConstant.FK_FIRST) > 0)
        {
            throw new MYException("群组已经被流程定义绑定,不能删除");
        }

        // flow view can bing grop flowViewerDAO
        if (flowViewerDAO.countByProcesser(id) > 0)
        {
            throw new MYException("群组已经被流程查阅绑定,不能删除");
        }
    }

    /**
     * @return the groupDAO
     */
    public GroupDAO getGroupDAO()
    {
        return groupDAO;
    }

    /**
     * @param groupDAO
     *            the groupDAO to set
     */
    public void setGroupDAO(GroupDAO groupDAO)
    {
        this.groupDAO = groupDAO;
    }

    /**
     * @return the groupVSStafferDAO
     */
    public GroupVSStafferDAO getGroupVSStafferDAO()
    {
        return groupVSStafferDAO;
    }

    /**
     * @param groupVSStafferDAO
     *            the groupVSStafferDAO to set
     */
    public void setGroupVSStafferDAO(GroupVSStafferDAO groupVSStafferDAO)
    {
        this.groupVSStafferDAO = groupVSStafferDAO;
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
     * @return the tokenVSHanderDAO
     */
    public TokenVSHanderDAO getTokenVSHanderDAO()
    {
        return tokenVSHanderDAO;
    }

    /**
     * @param tokenVSHanderDAO
     *            the tokenVSHanderDAO to set
     */
    public void setTokenVSHanderDAO(TokenVSHanderDAO tokenVSHanderDAO)
    {
        this.tokenVSHanderDAO = tokenVSHanderDAO;
    }

    /**
     * @return the flowViewerDAO
     */
    public FlowViewerDAO getFlowViewerDAO()
    {
        return flowViewerDAO;
    }

    /**
     * @param flowViewerDAO
     *            the flowViewerDAO to set
     */
    public void setFlowViewerDAO(FlowViewerDAO flowViewerDAO)
    {
        this.flowViewerDAO = flowViewerDAO;
    }
}
