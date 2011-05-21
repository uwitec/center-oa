/**
 * File Name: StafferManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-23<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager.impl;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.oa.publics.bean.InvoiceCreditBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.OrgConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.StafferConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.InvoiceCreditDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.StafferVSIndustryDAO;
import com.china.center.oa.publics.dao.StafferVSPriDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.listener.StafferListener;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.oa.publics.manager.StafferManager;
import com.china.center.oa.publics.vo.StafferVO;
import com.china.center.oa.publics.vs.StafferVSPriBean;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;


/**
 * StafferManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-6-23
 * @see StafferManagerImpl
 * @since 1.0
 */
@Exceptional
public class StafferManagerImpl extends AbstractListenerManager<StafferListener> implements StafferManager
{
    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private CommonDAO commonDAO = null;

    private StafferVSPriDAO stafferVSPriDAO = null;

    private StafferVSIndustryDAO stafferVSIndustryDAO = null;

    private PrincipalshipDAO principalshipDAO = null;

    private InvoiceCreditDAO invoiceCreditDAO = null;

    private OrgManager orgManager = null;

    public StafferManagerImpl()
    {
    }

    /**
     * addBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean addBean(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        bean.setId(commonDAO.getSquenceString());

        bean.setLever(StafferConstant.LEVER_DEFAULT);

        // save VS
        List<StafferVSPriBean> priList = bean.getPriList();

        if (priList.size() >= 1)
        {
            bean.setPrincipalshipId(priList.get(0).getPrincipalshipId());
        }

        Set<String> set = new HashSet<String>();

        String industryId = "";

        for (StafferVSPriBean stafferVSPriBean : priList)
        {
            stafferVSPriBean.setStafferId(bean.getId());

            PrincipalshipBean four = orgManager.findByIdAndSpecialLevel(stafferVSPriBean
                .getPrincipalshipId(), 4);

            if (four != null)
            {
                PrincipalshipBean three = principalshipDAO.find(four.getParentId());

                if (three != null && OrgConstant.ORG_BIG_DEPARTMENT.equals(three.getParentId()))
                {
                    set.add(four.getId());

                    // 四级组织ID
                    industryId = four.getId();

                    PrincipalshipBean five = orgManager.findByIdAndSpecialLevel(stafferVSPriBean
                        .getPrincipalshipId(), 5);

                    if (five != null)
                    {
                        if ( !StringTools.isNullOrNone(bean.getIndustryId2()))
                        {
                            throw new MYException("职员只能在一个事业部下面的一个5级组织下");
                        }

                        bean.setIndustryId2(five.getId());
                    }
                }
            }
        }

        handlerOne(bean, set, null, null);

        // 这里只保存第一个
        bean.setIndustryId(industryId);

        stafferDAO.saveEntityBean(bean);

        stafferVSPriDAO.saveAllEntityBeans(priList);

        return true;
    }

    /**
     * equal
     * 
     * @param set
     * @param oldVS
     * @return
     */
    private boolean equal(Set<String> set, List<InvoiceCreditBean> oldVS)
    {
        if (oldVS == null)
        {
            return false;
        }

        if (set.size() != oldVS.size())
        {
            return false;
        }

        for (InvoiceCreditBean invoiceCreditBean : oldVS)
        {
            boolean eq = false;

            for (String id : set)
            {
                if (id.equals(invoiceCreditBean.getInvoiceId()))
                {
                    eq = true;

                    break;
                }
            }

            if ( !eq)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * handlerOne
     * 
     * @param bean
     * @param set
     * @throws MYException
     */
    private void handlerOne(StafferBean bean, Set<String> set, List<InvoiceCreditBean> oldVS,
                            StafferBean oldBean)
        throws MYException
    {
        if (set.size() > 1)
        {
            // 如果是事业部经理就不能这么处理了
            if ( !bean.getPostId().equals(PublicConstant.POST_SHI_MANAGER))
            {
                if (set.size() > 1)
                {
                    throw new MYException("职员只能在一个事业部下面,除非是事业部经理");
                }
            }
        }

        if ( !equal(set, oldVS) || oldBean.getCredit() != bean.getCredit())
        {
            // 事业部信用
            invoiceCreditDAO.deleteEntityBeansByFK(bean.getId());

            // 总额度
            double credit = bean.getCredit();

            double each = credit / set.size();

            // 保存对应关系
            for (String invoiceId : set)
            {
                InvoiceCreditBean vs = new InvoiceCreditBean();

                vs.setId(commonDAO.getSquenceString20());

                vs.setCredit(each);

                vs.setInvoiceId(invoiceId);

                vs.setStafferId(bean.getId());

                invoiceCreditDAO.saveEntityBean(vs);
            }
        }
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean updateCredit(User user, String id, double credit)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        InvoiceCreditBean bean = invoiceCreditDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setCredit(credit);

        invoiceCreditDAO.updateEntityBean(bean);

        List<InvoiceCreditBean> inList = invoiceCreditDAO.queryEntityBeansByFK(bean.getStafferId());

        double total = 0.0d;

        for (InvoiceCreditBean invoiceCreditBean : inList)
        {
            total += invoiceCreditBean.getCredit();
        }

        StafferBean sb = stafferDAO.find(bean.getStafferId());

        if (sb == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        sb.setCredit(total);

        stafferDAO.updateEntityBean(sb);

        return true;
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
    public boolean updateBean(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        StafferBean oldBean = stafferDAO.find(bean.getId());

        if (oldBean == null)
        {
            throw new MYException("人员不存在");
        }

        bean.setName(oldBean.getName());

        bean.setCode(oldBean.getCode());

        bean.setPwkey(oldBean.getPwkey());

        bean.setLever(oldBean.getLever());

        List<StafferVSPriBean> priList = bean.getPriList();

        if (priList.size() >= 1)
        {
            bean.setPrincipalshipId(priList.get(0).getPrincipalshipId());
        }

        // save VS
        stafferVSPriDAO.deleteEntityBeansByFK(bean.getId());

        stafferVSIndustryDAO.deleteEntityBeansByFK(bean.getId());

        Set<String> set = new HashSet<String>();

        String industryId = "";

        for (StafferVSPriBean stafferVSPriBean : priList)
        {
            stafferVSPriBean.setStafferId(bean.getId());

            PrincipalshipBean four = orgManager.findByIdAndSpecialLevel(stafferVSPriBean
                .getPrincipalshipId(), 4);

            if (four != null)
            {
                PrincipalshipBean three = principalshipDAO.find(four.getParentId());

                if (three != null && OrgConstant.ORG_BIG_DEPARTMENT.equals(three.getParentId()))
                {
                    set.add(four.getId());

                    // 四级组织ID
                    industryId = four.getId();

                    PrincipalshipBean five = orgManager.findByIdAndSpecialLevel(stafferVSPriBean
                        .getPrincipalshipId(), 5);

                    if (five != null)
                    {
                        if ( !StringTools.isNullOrNone(bean.getIndustryId2()))
                        {
                            throw new MYException("职员只能在一个事业部下面的一个5级组织下");
                        }

                        bean.setIndustryId2(five.getId());
                    }
                }
            }
        }

        List<InvoiceCreditBean> oldVS = invoiceCreditDAO.queryEntityBeansByFK(bean.getId());

        handlerOne(bean, set, oldVS, oldBean);

        if (set.size() > 0)
        {
            bean.setIndustryId(industryId);
        }

        stafferDAO.updateEntityBean(bean);

        stafferVSPriDAO.saveAllEntityBeans(priList);

        return true;
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
    public boolean updatePwkey(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        stafferDAO.updatePwkey(bean.getId(), bean.getPwkey());

        return true;
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean updateLever(User user, StafferBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        stafferDAO.updateLever(bean.getId(), bean.getLever());

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

    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String stafferId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, stafferId);

        StafferBean bean = stafferDAO.find(stafferId);

        if (bean == null)
        {
            throw new MYException("人员不存在");
        }

        if (bean.getStatus() != StafferConstant.STATUS_COMMON)
        {
            throw new MYException("拒绝操作");
        }

        checkDelBean(stafferId);

        bean.setStatus(StafferConstant.STATUS_DROP);

        stafferDAO.updateEntityBean(bean);

        stafferVSPriDAO.deleteEntityBeansByFK(stafferId);

        stafferVSIndustryDAO.deleteEntityBeansByFK(bean.getId());

        return true;
    }

    /**
     * 获得本职员的上级
     * 
     * @param stafferId
     * @return
     */
    public Collection<StafferVO> querySuperiorStaffer(String stafferId)
    {
        Set<StafferVO> set = new HashSet<StafferVO>();

        // 获得人员的组织结构
        List<StafferVSPriBean> vsList = stafferVSPriDAO.queryEntityBeansByFK(stafferId);

        // 循环获得所有可以操作的人员
        for (StafferVSPriBean stafferVSPriBean : vsList)
        {
            PrincipalshipBean pri = principalshipDAO.find(stafferVSPriBean.getPrincipalshipId());

            if (pri == null || StringTools.isNullOrNone(pri.getParentId()))
            {
                continue;
            }

            String parentId = pri.getParentId();

            List<StafferVSPriBean> svsp = stafferVSPriDAO.queryEntityBeansByFK(parentId,
                AnoConstant.FK_FIRST);

            for (StafferVSPriBean each : svsp)
            {
                set.add(stafferDAO.findVO(each.getStafferId()));
            }
        }

        return set;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.publics.manager.StafferManager#queryStafferByAuthId(java.lang.String)
     */
    public List<StafferBean> queryStafferByAuthId(String authId)
    {
        return stafferDAO.queryStafferByAuthId(authId);
    }

    public List<StafferBean> queryStafferByAuthIdAndIndustryId(String authId, String industryId)
    {
        return stafferDAO.queryStafferByAuthIdAndIndustryId(authId, industryId);
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(StafferBean bean)
        throws MYException
    {
        if (stafferDAO.countByUnique(bean.getName()) > 0)
        {
            throw new MYException("职员名称不能重复");
        }

        if (stafferDAO.countByCode(bean.getCode()) > 0)
        {
            throw new MYException("职员工号不能重复");
        }
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkDelBean(String stafferId)
        throws MYException
    {
        if ("0".equals(stafferId) || "1".equals(stafferId))
        {
            throw new MYException("拒绝操作");
        }

        // 存在人员或者user
        if (userDAO.countByStafferId(stafferId) > 0)
        {
            throw new MYException("人员下存在注册登录用户,请先删除登录用户");
        }

        for (StafferListener listener : this.listenerMapValues())
        {
            listener.onDelete(stafferId);
        }
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
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }

    /**
     * @return the stafferVSPriDAO
     */
    public StafferVSPriDAO getStafferVSPriDAO()
    {
        return stafferVSPriDAO;
    }

    /**
     * @param stafferVSPriDAO
     *            the stafferVSPriDAO to set
     */
    public void setStafferVSPriDAO(StafferVSPriDAO stafferVSPriDAO)
    {
        this.stafferVSPriDAO = stafferVSPriDAO;
    }

    /**
     * @return the principalshipDAO
     */
    public PrincipalshipDAO getPrincipalshipDAO()
    {
        return principalshipDAO;
    }

    /**
     * @param principalshipDAO
     *            the principalshipDAO to set
     */
    public void setPrincipalshipDAO(PrincipalshipDAO principalshipDAO)
    {
        this.principalshipDAO = principalshipDAO;
    }

    /**
     * @return the stafferVSIndustryDAO
     */
    public StafferVSIndustryDAO getStafferVSIndustryDAO()
    {
        return stafferVSIndustryDAO;
    }

    /**
     * @param stafferVSIndustryDAO
     *            the stafferVSIndustryDAO to set
     */
    public void setStafferVSIndustryDAO(StafferVSIndustryDAO stafferVSIndustryDAO)
    {
        this.stafferVSIndustryDAO = stafferVSIndustryDAO;
    }

    /**
     * @return the orgManager
     */
    public OrgManager getOrgManager()
    {
        return orgManager;
    }

    /**
     * @param orgManager
     *            the orgManager to set
     */
    public void setOrgManager(OrgManager orgManager)
    {
        this.orgManager = orgManager;
    }

    /**
     * @return the invoiceCreditDAO
     */
    public InvoiceCreditDAO getInvoiceCreditDAO()
    {
        return invoiceCreditDAO;
    }

    /**
     * @param invoiceCreditDAO
     *            the invoiceCreditDAO to set
     */
    public void setInvoiceCreditDAO(InvoiceCreditDAO invoiceCreditDAO)
    {
        this.invoiceCreditDAO = invoiceCreditDAO;
    }
}
