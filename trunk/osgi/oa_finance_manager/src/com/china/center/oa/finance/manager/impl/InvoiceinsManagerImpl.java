/**
 * File Name: InvoiceinsManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-1<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager.impl;


import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.bean.InvoiceinsItemBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InsVSOutDAO;
import com.china.center.oa.finance.dao.InvoiceinsDAO;
import com.china.center.oa.finance.dao.InvoiceinsItemDAO;
import com.china.center.oa.finance.manager.InvoiceinsManager;
import com.china.center.oa.finance.vo.InvoiceinsVO;
import com.china.center.oa.finance.vs.InsVSOutBean;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.TimeTools;


/**
 * InvoiceinsManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-1-1
 * @see InvoiceinsManagerImpl
 * @since 3.0
 */
@Exceptional
public class InvoiceinsManagerImpl implements InvoiceinsManager
{
    private CommonDAO commonDAO = null;

    private InvoiceinsDAO invoiceinsDAO = null;

    private InvoiceinsItemDAO invoiceinsItemDAO = null;

    private OutBalanceDAO outBalanceDAO = null;

    private InsVSOutDAO insVSOutDAO = null;

    private OutDAO outDAO = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.InvoiceinsManager#addInvoiceinsBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.finance.bean.InvoiceinsBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addInvoiceinsBean(User user, InvoiceinsBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        bean.setLogTime(TimeTools.now());

        invoiceinsDAO.saveEntityBean(bean);

        List<InvoiceinsItemBean> itemList = bean.getItemList();

        for (InvoiceinsItemBean invoiceinsItemBean : itemList)
        {
            invoiceinsItemBean.setId(commonDAO.getSquenceString20());

            invoiceinsItemBean.setParentId(bean.getId());
        }

        invoiceinsItemDAO.saveAllEntityBeans(itemList);

        List<InsVSOutBean> vsList = bean.getVsList();

        if ( !ListTools.isEmptyOrNull(vsList))
        {
            for (InsVSOutBean insVSOutBean : vsList)
            {
                insVSOutBean.setId(commonDAO.getSquenceString20());

                insVSOutBean.setInsId(bean.getId());

                if (insVSOutBean.getType() == FinanceConstant.INSVSOUT_TYPE_OUT)
                {
                    // 销售单
                    OutBean out = outDAO.find(insVSOutBean.getOutId());

                    if (out == null)
                    {
                        throw new MYException("数据错误,请确认操作");
                    }

                    if (insVSOutBean.getMoneys() + out.getInvoiceMoney() > out.getTotal())
                    {
                        // TEMPLATE 数字格式化显示
                        throw new MYException("单据[%s]开票溢出,开票金额[%.2f],销售金额[%.2f]", out.getFullId(),
                            (insVSOutBean.getMoneys() + out.getInvoiceMoney()), out.getTotal());
                    }

                    if (insVSOutBean.getMoneys() + out.getInvoiceMoney() == out.getTotal())
                    {
                        // 更新开票状态-结束
                        outDAO.updateInvoiceStatus(out.getFullId(), out.getTotal(), OutConstant.INVOICESTATUS_END);
                    }

                    if (insVSOutBean.getMoneys() + out.getInvoiceMoney() < out.getTotal())
                    {
                        // 更新开票状态-过程
                        outDAO.updateInvoiceStatus(out.getFullId(), (insVSOutBean.getMoneys() + out.getInvoiceMoney()),
                            OutConstant.INVOICESTATUS_INIT);
                    }
                }
                else
                {
                    // 结算清单
                    OutBalanceBean balance = outBalanceDAO.find(insVSOutBean.getOutId());

                    if (balance == null)
                    {
                        throw new MYException("数据错误,请确认操作");
                    }

                    if (insVSOutBean.getMoneys() + balance.getInvoiceMoney() > balance.getTotal())
                    {
                        // TEMPLATE 数字格式化显示
                        throw new MYException("委托结算单[%s]开票溢出,开票金额[%.2f],销售金额[%.2f]", balance.getId(),
                            (insVSOutBean.getMoneys() + balance.getInvoiceMoney()), balance.getTotal());
                    }

                    if (insVSOutBean.getMoneys() + balance.getInvoiceMoney() == balance.getTotal())
                    {
                        // 更新开票状态-结束
                        outBalanceDAO.updateInvoiceStatus(balance.getId(), balance.getTotal(),
                            OutConstant.INVOICESTATUS_END);
                    }

                    if (insVSOutBean.getMoneys() + balance.getInvoiceMoney() < balance.getTotal())
                    {
                        // 更新开票状态-过程
                        outBalanceDAO.updateInvoiceStatus(balance.getId(),
                            (insVSOutBean.getMoneys() + balance.getInvoiceMoney()), OutConstant.INVOICESTATUS_INIT);
                    }
                }
            }

            insVSOutDAO.saveAllEntityBeans(vsList);
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.finance.manager.InvoiceinsManager#deleteInvoiceinsBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean deleteInvoiceinsBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        invoiceinsDAO.deleteEntityBean(id);

        invoiceinsItemDAO.deleteEntityBeansByFK(id);

        insVSOutDAO.deleteEntityBeansByFK(id, AnoConstant.FK_FIRST);

        return true;
    }

    public InvoiceinsVO findVO(String id)
    {
        InvoiceinsVO vo = invoiceinsDAO.findVO(id);

        if (vo == null)
        {
            return null;
        }

        vo.setItemList(invoiceinsItemDAO.queryEntityBeansByFK(id));

        vo.setVsList(insVSOutDAO.queryEntityBeansByFK(id, AnoConstant.FK_FIRST));

        return vo;
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
     * @return the invoiceinsDAO
     */
    public InvoiceinsDAO getInvoiceinsDAO()
    {
        return invoiceinsDAO;
    }

    /**
     * @param invoiceinsDAO
     *            the invoiceinsDAO to set
     */
    public void setInvoiceinsDAO(InvoiceinsDAO invoiceinsDAO)
    {
        this.invoiceinsDAO = invoiceinsDAO;
    }

    /**
     * @return the invoiceinsItemDAO
     */
    public InvoiceinsItemDAO getInvoiceinsItemDAO()
    {
        return invoiceinsItemDAO;
    }

    /**
     * @param invoiceinsItemDAO
     *            the invoiceinsItemDAO to set
     */
    public void setInvoiceinsItemDAO(InvoiceinsItemDAO invoiceinsItemDAO)
    {
        this.invoiceinsItemDAO = invoiceinsItemDAO;
    }

    /**
     * @return the insVSOutDAO
     */
    public InsVSOutDAO getInsVSOutDAO()
    {
        return insVSOutDAO;
    }

    /**
     * @param insVSOutDAO
     *            the insVSOutDAO to set
     */
    public void setInsVSOutDAO(InsVSOutDAO insVSOutDAO)
    {
        this.insVSOutDAO = insVSOutDAO;
    }

    /**
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    /**
     * @return the outBalanceDAO
     */
    public OutBalanceDAO getOutBalanceDAO()
    {
        return outBalanceDAO;
    }

    /**
     * @param outBalanceDAO
     *            the outBalanceDAO to set
     */
    public void setOutBalanceDAO(OutBalanceDAO outBalanceDAO)
    {
        this.outBalanceDAO = outBalanceDAO;
    }
}
