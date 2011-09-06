/**
 * File Name: TcpFlowManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-9-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.manager.impl;


import org.china.center.spring.iaop.annotation.IntegrationAOP;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.tcp.bean.TcpApproveBean;
import com.china.center.oa.tcp.constanst.TcpConstanst;
import com.china.center.oa.tcp.dao.TcpApproveDAO;
import com.china.center.oa.tcp.manager.TcpFlowManager;
import com.china.center.tools.JudgeTools;


/**
 * TcpFlowManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-9-6
 * @see TcpFlowManagerImpl
 * @since 3.0
 */
@IntegrationAOP
public class TcpFlowManagerImpl implements TcpFlowManager
{
    private TcpApproveDAO tcpApproveDAO = null;

    private CommonDAO commonDAO = null;

    /**
     * default constructor
     */
    public TcpFlowManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tcp.manager.TcpFlowManager#drawApprove(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean drawApprove(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        TcpApproveBean approve = tcpApproveDAO.find(id);

        if (approve == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !approve.getApproverId().equals(user.getStafferId()))
        {
            throw new MYException("没有权限,请确认操作");
        }

        if (approve.getPool() != TcpConstanst.TCP_POOL_POOL)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 情况
        tcpApproveDAO.deleteEntityBeansByFK(approve.getApplyId());

        approve.setId(commonDAO.getSquenceString20());

        approve.setPool(TcpConstanst.TCP_POOL_COMMON);

        tcpApproveDAO.saveEntityBean(approve);

        return true;
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

}
