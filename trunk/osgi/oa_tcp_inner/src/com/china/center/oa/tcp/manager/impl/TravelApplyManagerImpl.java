/**
 * File Name: TravelApplyManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.manager.impl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.iaop.annotation.IntegrationAOP;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.bean.AttachmentBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.constant.IDPrefixConstant;
import com.china.center.oa.publics.dao.AttachmentDAO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.oa.tcp.bean.TcpApplyBean;
import com.china.center.oa.tcp.bean.TcpShareBean;
import com.china.center.oa.tcp.bean.TravelApplyBean;
import com.china.center.oa.tcp.bean.TravelApplyItemBean;
import com.china.center.oa.tcp.bean.TravelApplyPayBean;
import com.china.center.oa.tcp.constanst.TcpConstanst;
import com.china.center.oa.tcp.dao.TcpApplyDAO;
import com.china.center.oa.tcp.dao.TcpApproveDAO;
import com.china.center.oa.tcp.dao.TcpFlowDAO;
import com.china.center.oa.tcp.dao.TcpPrepaymentDAO;
import com.china.center.oa.tcp.dao.TcpShareDAO;
import com.china.center.oa.tcp.dao.TravelApplyDAO;
import com.china.center.oa.tcp.dao.TravelApplyItemDAO;
import com.china.center.oa.tcp.dao.TravelApplyPayDAO;
import com.china.center.oa.tcp.helper.TCPHelper;
import com.china.center.oa.tcp.manager.TravelApplyManager;
import com.china.center.oa.tcp.vo.TcpShareVO;
import com.china.center.oa.tcp.vo.TravelApplyItemVO;
import com.china.center.oa.tcp.vo.TravelApplyVO;
import com.china.center.tools.FileTools;
import com.china.center.tools.JudgeTools;


/**
 * TravelApplyManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-7-17
 * @see TravelApplyManagerImpl
 * @since 3.0
 */
@IntegrationAOP
public class TravelApplyManagerImpl implements TravelApplyManager
{
    private final Log operationLog = LogFactory.getLog("opr");

    private TcpApplyDAO tcpApplyDAO = null;

    private TcpFlowDAO tcpFlowDAO = null;

    private TcpPrepaymentDAO tcpPrepaymentDAO = null;

    private TcpShareDAO tcpShareDAO = null;

    private TravelApplyDAO travelApplyDAO = null;

    private TravelApplyItemDAO travelApplyItemDAO = null;

    private TravelApplyPayDAO travelApplyPayDAO = null;

    private CommonDAO commonDAO = null;

    private OrgManager orgManager = null;

    private AttachmentDAO attachmentDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private TcpApproveDAO tcpApproveDAO = null;

    /**
     * default constructor
     */
    public TravelApplyManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tcp.manager.TravelApplyManager#addTravelApplyBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.tcp.bean.TravelApplyBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addTravelApplyBean(User user, TravelApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20(IDPrefixConstant.ID_TCP_PREFIX));

        bean.setStafferId(user.getStafferId());

        bean.setStatus(TcpConstanst.TCP_STATUS_INIT);

        // 获取flowKey
        TCPHelper.setFlowKey(bean);

        // TravelApplyItemBean
        List<TravelApplyItemBean> itemList = bean.getItemList();

        for (TravelApplyItemBean travelApplyItemBean : itemList)
        {
            travelApplyItemBean.setId(commonDAO.getSquenceString20());
            travelApplyItemBean.setParentId(bean.getId());
        }

        travelApplyItemDAO.saveAllEntityBeans(itemList);

        if (bean.getBorrow() == TcpConstanst.TRAVELAPPLY_BORROW_YES)
        {
            List<TravelApplyPayBean> payList = bean.getPayList();

            for (TravelApplyPayBean travelApplyPayBean : payList)
            {
                travelApplyPayBean.setId(commonDAO.getSquenceString20());
                travelApplyPayBean.setParentId(bean.getId());
            }

            travelApplyPayDAO.saveAllEntityBeans(payList);
        }

        List<TcpShareBean> shareList = bean.getShareList();

        for (TcpShareBean tcpShareBean : shareList)
        {
            tcpShareBean.setId(commonDAO.getSquenceString20());
            tcpShareBean.setRefId(bean.getId());
        }

        tcpShareDAO.saveAllEntityBeans(shareList);

        List<AttachmentBean> attachmentList = bean.getAttachmentList();

        for (AttachmentBean attachmentBean : attachmentList)
        {
            attachmentBean.setId(commonDAO.getSquenceString20());
            attachmentBean.setRefId(bean.getId());
        }

        attachmentDAO.saveAllEntityBeans(attachmentList);

        travelApplyDAO.saveEntityBean(bean);

        saveApply(user, bean);

        return true;
    }

    /**
     * saveApply
     * 
     * @param user
     * @param bean
     */
    public void saveApply(User user, TravelApplyBean bean)
    {
        TcpApplyBean apply = new TcpApplyBean();

        apply.setId(bean.getId());
        apply.setName(bean.getId());
        apply.setFlowKey(bean.getFlowKey());
        apply.setApplyId(bean.getId());
        apply.setApplyId(user.getStafferId());
        apply.setType(TcpConstanst.TCP_TYPE_TRAVEL);
        apply.setStatus(TcpConstanst.TCP_STATUS_INIT);
        apply.setTotal(bean.getTotal());
        apply.setLogTime(bean.getLogTime());
        apply.setDescription(bean.getDescription());

        tcpApplyDAO.saveEntityBean(apply);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tcp.manager.TravelApplyManager#updateTravelApplyBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.tcp.bean.TravelApplyBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateTravelApplyBean(User user, TravelApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        if ( !bean.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能修改自己的申请");
        }

        bean.setStatus(TcpConstanst.TCP_STATUS_INIT);

        // 获取flowKey
        TCPHelper.setFlowKey(bean);

        // 先清理
        travelApplyItemDAO.deleteEntityBeansByFK(bean.getId());
        travelApplyPayDAO.deleteEntityBeansByFK(bean.getId());
        tcpShareDAO.deleteEntityBeansByFK(bean.getId());

        // TravelApplyItemBean
        List<TravelApplyItemBean> itemList = bean.getItemList();

        for (TravelApplyItemBean travelApplyItemBean : itemList)
        {
            travelApplyItemBean.setId(commonDAO.getSquenceString20());
            travelApplyItemBean.setParentId(bean.getId());
        }

        List<TravelApplyPayBean> payList = bean.getPayList();

        for (TravelApplyPayBean travelApplyPayBean : payList)
        {
            travelApplyPayBean.setId(commonDAO.getSquenceString20());
            travelApplyPayBean.setParentId(bean.getId());
        }

        List<TcpShareBean> shareList = bean.getShareList();

        for (TcpShareBean tcpShareBean : shareList)
        {
            tcpShareBean.setId(commonDAO.getSquenceString20());
            tcpShareBean.setRefId(bean.getId());
        }

        travelApplyDAO.saveEntityBean(bean);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteTravelApplyBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        TravelApplyBean bean = travelApplyDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !TCPHelper.canTravelApplyDelete(bean))
        {
            throw new MYException("不是初始态和驳回态,不能删除");
        }

        // 删除
        travelApplyItemDAO.deleteEntityBeansByFK(bean.getId());
        travelApplyPayDAO.deleteEntityBeansByFK(bean.getId());
        tcpShareDAO.deleteEntityBeansByFK(bean.getId());

        String rootPath = ConfigLoader.getProperty("tcpAttachmentPath");

        List<AttachmentBean> attachmenList = attachmentDAO.queryEntityBeansByFK(id);

        for (AttachmentBean attachmentBean : attachmenList)
        {
            FileTools.deleteFile(rootPath + attachmentBean.getPath());
        }

        attachmentDAO.deleteEntityBeansByFK(bean.getId());

        travelApplyDAO.deleteEntityBean(id);

        tcpApplyDAO.deleteEntityBean(id);

        operationLog.info(user + " delete TravelApplyBean:" + bean);

        return true;
    }

    public TravelApplyVO findVO(String id)
    {
        TravelApplyVO bean = travelApplyDAO.findVO(id);

        if (bean == null)
        {
            return bean;
        }

        List<TravelApplyItemVO> itemVOList = travelApplyItemDAO.queryEntityVOsByFK(id);

        bean.setItemVOList(itemVOList);

        List<AttachmentBean> attachmentList = attachmentDAO.queryEntityVOsByFK(id);

        bean.setAttachmentList(attachmentList);

        List<TravelApplyPayBean> payList = travelApplyPayDAO.queryEntityVOsByFK(id);

        bean.setPayList(payList);

        List<TcpShareVO> shareList = tcpShareDAO.queryEntityVOsByFK(id);

        for (TcpShareVO tcpShareVO : shareList)
        {
            PrincipalshipBean dep = orgManager.findPrincipalshipById(tcpShareVO.getDepartmentId());

            if (dep != null)
            {
                tcpShareVO.setDepartmentName(dep.getFullName());
            }
        }

        bean.setShareVOList(shareList);

        TCPHelper.chageVO(bean);

        return bean;
    }

    /**
     * @return the tcpApplyDAO
     */
    public TcpApplyDAO getTcpApplyDAO()
    {
        return tcpApplyDAO;
    }

    /**
     * @param tcpApplyDAO
     *            the tcpApplyDAO to set
     */
    public void setTcpApplyDAO(TcpApplyDAO tcpApplyDAO)
    {
        this.tcpApplyDAO = tcpApplyDAO;
    }

    /**
     * @return the tcpFlowDAO
     */
    public TcpFlowDAO getTcpFlowDAO()
    {
        return tcpFlowDAO;
    }

    /**
     * @param tcpFlowDAO
     *            the tcpFlowDAO to set
     */
    public void setTcpFlowDAO(TcpFlowDAO tcpFlowDAO)
    {
        this.tcpFlowDAO = tcpFlowDAO;
    }

    /**
     * @return the tcpPrepaymentDAO
     */
    public TcpPrepaymentDAO getTcpPrepaymentDAO()
    {
        return tcpPrepaymentDAO;
    }

    /**
     * @param tcpPrepaymentDAO
     *            the tcpPrepaymentDAO to set
     */
    public void setTcpPrepaymentDAO(TcpPrepaymentDAO tcpPrepaymentDAO)
    {
        this.tcpPrepaymentDAO = tcpPrepaymentDAO;
    }

    /**
     * @return the tcpShareDAO
     */
    public TcpShareDAO getTcpShareDAO()
    {
        return tcpShareDAO;
    }

    /**
     * @param tcpShareDAO
     *            the tcpShareDAO to set
     */
    public void setTcpShareDAO(TcpShareDAO tcpShareDAO)
    {
        this.tcpShareDAO = tcpShareDAO;
    }

    /**
     * @return the travelApplyDAO
     */
    public TravelApplyDAO getTravelApplyDAO()
    {
        return travelApplyDAO;
    }

    /**
     * @param travelApplyDAO
     *            the travelApplyDAO to set
     */
    public void setTravelApplyDAO(TravelApplyDAO travelApplyDAO)
    {
        this.travelApplyDAO = travelApplyDAO;
    }

    /**
     * @return the travelApplyItemDAO
     */
    public TravelApplyItemDAO getTravelApplyItemDAO()
    {
        return travelApplyItemDAO;
    }

    /**
     * @param travelApplyItemDAO
     *            the travelApplyItemDAO to set
     */
    public void setTravelApplyItemDAO(TravelApplyItemDAO travelApplyItemDAO)
    {
        this.travelApplyItemDAO = travelApplyItemDAO;
    }

    /**
     * @return the travelApplyPayDAO
     */
    public TravelApplyPayDAO getTravelApplyPayDAO()
    {
        return travelApplyPayDAO;
    }

    /**
     * @param travelApplyPayDAO
     *            the travelApplyPayDAO to set
     */
    public void setTravelApplyPayDAO(TravelApplyPayDAO travelApplyPayDAO)
    {
        this.travelApplyPayDAO = travelApplyPayDAO;
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
     * @return the flowLogDAO
     */
    public FlowLogDAO getFlowLogDAO()
    {
        return flowLogDAO;
    }

    /**
     * @param flowLogDAO
     *            the flowLogDAO to set
     */
    public void setFlowLogDAO(FlowLogDAO flowLogDAO)
    {
        this.flowLogDAO = flowLogDAO;
    }

    /**
     * @return the tcpApproveDAO
     */
    public TcpApproveDAO getTcpApproveDAO()
    {
        return tcpApproveDAO;
    }

    /**
     * @param tcpApproveDAO
     *            the tcpApproveDAO to set
     */
    public void setTcpApproveDAO(TcpApproveDAO tcpApproveDAO)
    {
        this.tcpApproveDAO = tcpApproveDAO;
    }

    /**
     * @return the attachmentDAO
     */
    public AttachmentDAO getAttachmentDAO()
    {
        return attachmentDAO;
    }

    /**
     * @param attachmentDAO
     *            the attachmentDAO to set
     */
    public void setAttachmentDAO(AttachmentDAO attachmentDAO)
    {
        this.attachmentDAO = attachmentDAO;
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
}
