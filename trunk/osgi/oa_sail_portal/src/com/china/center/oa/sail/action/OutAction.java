package com.china.center.oa.sail.action;


import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.vo.InBillVO;
import com.china.center.oa.finance.vo.OutBillVO;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProviderDAO;
import com.china.center.oa.product.dao.StorageDAO;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.PublicLock;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.DutyVSInvoiceDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.InvoiceCreditDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.ShowDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.manager.AuthManager;
import com.china.center.oa.publics.manager.FatalNotify;
import com.china.center.oa.publics.manager.StafferManager;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.publics.vo.FlowLogVO;
import com.china.center.oa.publics.vo.InvoiceCreditVO;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.BaseBalanceDAO;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.ConsignDAO;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.dao.OutQueryDAO;
import com.china.center.oa.sail.helper.FlowLogHelper;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.helper.YYTools;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.sail.vo.OutBalanceVO;
import com.china.center.oa.sail.vo.OutVO;
import com.china.center.oa.sail.wrap.CreditWrap;
import com.china.center.tools.CommonTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.RequestTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * 增加出库单
 * 
 * @author ZHUZHU
 * @version 2007-4-1
 * @see
 * @since
 */
public class OutAction extends ParentOutAction
{

    /**
     * rejectBack
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rejectBack(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        User user = (User)request.getSession().getAttribute("user");

        if (StringTools.isNullOrNone(fullId))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        OutBean bean = outDAO.find(fullId);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        if (bean.getStatus() == OutConstant.STATUS_SAVE
            || bean.getStatus() == OutConstant.STATUS_REJECT)
        {
            try
            {
                outManager.delOut(user, fullId);

                request.setAttribute(KeyConstant.MESSAGE, "成功操作:" + fullId);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "流程异常，请重新操作:" + e.toString());

                return mapping.findForward("error");
            }
        }
        else
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有保存态的库单才可以驳回");

            return mapping.findForward("error");
        }

        CommonTools.removeParamers(request);

        RequestTools.menuInitQuery(request);

        request.setAttribute("queryType", "5");

        return queryBuy(mapping, form, request, reponse);
    }

    /**
     * 处理调出的库单(入库单的处理)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward processInvoke(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        String flag = request.getParameter("flag");

        String depotpartId = request.getParameter("depotpartId");

        User user = (User)request.getSession().getAttribute("user");

        if (StringTools.isNullOrNone(fullId))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        if ( ! (outBean.getType() == OutConstant.OUT_TYPE_INBILL && outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能转调，请核实");

            return mapping.findForward("error");
        }

        if (outBean.getInway() != OutConstant.IN_WAY)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不在在途中，不能处理");

            return mapping.findForward("error");
        }

        // 直接接受自动生成一个调入的库单
        if ("1".equals(flag))
        {
            OutBean newOut = new OutBean(outBean);

            newOut.setStatus(0);

            newOut.setLocationId(user.getLocationId());

            // 仓库就是调出的目的仓区
            newOut.setLocation(outBean.getDestinationId());

            newOut.setOutType(OutConstant.OUTTYPE_IN_MOVEOUT);

            newOut.setFullId("");

            newOut.setRefOutFullId(fullId);

            newOut.setDestinationId(outBean.getLocation());

            newOut.setDescription("自动接收调拨单:" + fullId + ".生成的调入单据");

            newOut.setInway(OutConstant.IN_WAY_NO);

            newOut.setChecks("");

            // 调入的单据
            newOut.setReserve1(OutConstant.MOVEOUT_IN);

            newOut.setPay(OutConstant.PAY_NOT);

            newOut.setTotal( -newOut.getTotal());

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

            if (StringTools.isNullOrNone(depotpartId))
            {
                DepotpartBean defaultOKDepotpart = depotpartDAO.findDefaultOKDepotpart(outBean
                    .getDestinationId());

                if (defaultOKDepotpart == null)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "仓库下没有良品仓，请核实");

                    return mapping.findForward("error");
                }

                depotpartId = defaultOKDepotpart.getId();
            }

            DepotpartBean depotpart = depotpartDAO.find(depotpartId);

            if (depotpart == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "仓库下没有良品仓，请核实");

                return mapping.findForward("error");
            }

            for (BaseBean baseBean : baseList)
            {
                // 获得仓库默认的仓区
                baseBean.setDepotpartId(depotpartId);
                baseBean.setValue( -baseBean.getValue());
                baseBean.setLocationId(outBean.getDestinationId());
                baseBean.setAmount( -baseBean.getAmount());
                baseBean.setDepotpartName(depotpart.getName());
            }

            List<BaseBean> lastList = OutHelper.trimBaseList(baseList);

            newOut.setBaseList(lastList);

            try
            {
                String ful = outManager.coloneOutAndSubmitAffair(newOut, user,
                    StorageConstant.OPR_STORAGE_REDEPLOY);

                request.setAttribute(KeyConstant.MESSAGE, fullId + "成功自动接收:" + ful);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能自动接收，请核实:"
                                                                + e.getErrorContent());

                return mapping.findForward("error");
            }

        }

        // 转调处理
        if ("2".equals(flag))
        {
            String changeLocationId = request.getParameter("changeLocationId");

            if (outBean.getLocation().equals(changeLocationId))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "转调区域不能是产品调出区域，请核实");

                return mapping.findForward("error");
            }

            outBean.setDestinationId(changeLocationId);

            try
            {
                outManager.updateOut(outBean);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request
                    .setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能转调，请核实:" + e.getErrorContent());

                return mapping.findForward("error");
            }

            request.setAttribute(KeyConstant.MESSAGE, fullId + "成功转调");
        }

        // 直接驳回
        if ("3".equals(flag))
        {
            try
            {
                outManager.reject(fullId, user, "调出驳回");

                request.setAttribute(KeyConstant.MESSAGE, fullId + "成功驳回:" + fullId);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                return mapping.findForward("error");
            }
        }

        request.setAttribute("forward", "10");

        request.setAttribute("queryType", "4");

        return queryBuy(mapping, form, request, reponse);
    }

    /**
     * querySelfCredit
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfCredit(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        StafferBean staffer = Helper.getStaffer(request);

        List<CreditWrap> allWrap = outDAO.queryAllNoPay(staffer.getId(), staffer.getIndustryId(),
            YYTools.getStatBeginDate(), YYTools.getStatEndDate());

        for (CreditWrap creditWrap : allWrap)
        {
            PrincipalshipBean pri = principalshipDAO.find(creditWrap.getIndustryId());

            if (pri != null)
            {
                creditWrap.setIndustryId(pri.getName());
            }

            StafferBean sb = stafferDAO.find(creditWrap.getStafferId());

            if (sb != null)
            {
                creditWrap.setStafferId(sb.getName());
            }
        }

        String jsonstr = JSONTools.getJSONString(allWrap);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * findCreditDetail
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findCreditDetail(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        StafferBean staffer = Helper.getStaffer(request);

        // 查询是否
        double mt = outDAO.sumNoPayAndAvouchBusinessByManagerId2(staffer.getId(), YYTools
            .getStatBeginDate(), YYTools.getStatEndDate());

        // 这个不应该
        double st = outDAO.sumNoPayAndAvouchBusinessByStafferId(staffer.getId(), staffer
            .getIndustryId(), YYTools.getStatBeginDate(), YYTools.getStatEndDate());

        double total = staffer.getCredit() * staffer.getLever();

        StringBuffer buffer = new StringBuffer();

        List<InvoiceCreditVO> vsList = invoiceCreditDAO.queryEntityVOsByFK(staffer.getId());

        for (InvoiceCreditVO invoiceCreditVO : vsList)
        {
            buffer.append(invoiceCreditVO.getInvoiceName()).append("下的信用额度:").append(
                MathTools.formatNum(invoiceCreditVO.getCredit() * staffer.getLever())).append(
                "<br>");
        }

        String msg0 = "总信用额度:" + MathTools.formatNum(total) + "<br>";
        String msg00 = "原始信用:" + MathTools.formatNum(staffer.getCredit()) + "<br>";
        String msg01 = "信用杠杆:" + staffer.getLever() + "<br>";
        String msg1 = "开单使用额度:" + MathTools.formatNum(st) + "<br>";
        String msg2 = "担保使用额度:" + MathTools.formatNum(mt) + "<br>";
        String msg3 = "剩余额度:" + MathTools.formatNum(total - st - mt);

        ajax.setSuccess(msg0 + msg00 + msg01 + buffer + msg1 + msg2 + msg3);

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * mark(标记单据)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward mark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                              HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean bean = outDAO.find(fullId);

        outManager.mark(fullId, !bean.isMark());

        request.setAttribute("forward", "1");

        return querySelfOut(mapping, form, request, reponse);
    }

    /**
     * 通过委托结算
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward passOutBalance(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        User user = (User)request.getSession().getAttribute("user");

        // LOCK 委托代销结算单通过(退库)
        synchronized (PublicLock.PRODUCT_CORE)
        {
            try
            {
                outManager.passOutBalance(user, id);

                request.setAttribute(KeyConstant.MESSAGE, "成功操作");
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());
            }
        }

        RequestTools.actionInitQuery(request);

        request.setAttribute("queryType", "2");

        return queryOutBalance(mapping, form, request, reponse);
    }

    /**
     * 驳回委托结算
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rejectOutBalance(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        User user = (User)request.getSession().getAttribute("user");

        try
        {
            outManager.rejectOutBalance(user, id, reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());
        }

        RequestTools.actionInitQuery(request);

        return queryOutBalance(mapping, form, request, reponse);
    }

    /**
     * 删除委托结算
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward deleteOutBalance(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        User user = (User)request.getSession().getAttribute("user");

        try
        {
            outManager.deleteOutBalance(user, id);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());
        }

        RequestTools.actionInitQuery(request);

        return queryOutBalance(mapping, form, request, reponse);
    }

    /**
     * 总部核对(结束销售单)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward checks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误,请确认操作");

            return mapping.findForward("error");
        }

        String checks = request.getParameter("reason");

        User user = (User)request.getSession().getAttribute("user");

        try
        {
            outManager.check(fullId, user, checks);
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功核对单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            return queryOut(mapping, form, request, reponse);
        }
        else
        {
            return queryBuy(mapping, form, request, reponse);
        }
    }

    /**
     * 付款(结算中心)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payOut(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getReserve3() != OutConstant.OUT_SAIL_TYPE_MONEY)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只能是款到发货的单据可以操作");

            return mapping.findForward("error");
        }

        if (out.getStatus() != OutConstant.STATUS_SUBMIT)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getPay() == OutConstant.PAY_YES)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已经付款");

            return mapping.findForward("error");
        }

        try
        {
            outManager.payOut(user, fullId, "结算中心确认收款");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        request.setAttribute(KeyConstant.MESSAGE, "成功确认单据:" + fullId);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * TEMPIMPL 强制通过付款
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward fourcePayOut(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if ( !OutHelper.isSailEnd(out))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getPay() == OutConstant.PAY_YES)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已经付款");

            return mapping.findForward("error");
        }

        try
        {
            outManager.fourcePayOut(user, fullId, "财务确认收款");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功核对单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 付款(财务收款心--往来核对)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payOut2(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if ( !OutHelper.isSailEnd(out))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getPay() == OutConstant.PAY_YES)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已经付款");

            return mapping.findForward("error");
        }

        try
        {
            outManager.payOut(user, fullId, "财务确认收款");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功核对单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 产生坏账
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payOut3(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        String baddebts = request.getParameter("baddebts");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if ( !OutHelper.isSailEnd(out))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getPay() == OutConstant.PAY_YES)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已经付款");

            return mapping.findForward("error");
        }

        try
        {
            outManager.payBaddebts(user, fullId, MathTools.parseDouble(baddebts));
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功记录坏账单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * updateInvoice
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateInvoice(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        String invoices = request.getParameter("invoices");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getInvoiceMoney() > 0)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        try
        {
            outManager.updateInvoice(user, fullId, invoices);
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功更新发票类型:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return querySelfOut(mapping, form, request, reponse);
    }

    /**
     * 坏账取消
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payOut4(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if ( !OutHelper.isSailEnd(out))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getPay() != OutConstant.PAY_YES)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "还没确认付款");

            return mapping.findForward("error");
        }

        try
        {
            outManager.initPayOut(user, fullId);
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功取消坏账单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * CORE 修改库单的状态(销售单的流程)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public synchronized ActionForward modifyOutStatus(ActionMapping mapping, ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        User user = (User)request.getSession().getAttribute("user");

        int statuss = Integer.parseInt(request.getParameter("statuss"));

        String oldStatus = request.getParameter("oldStatus");

        if (StringTools.isNullOrNone(oldStatus))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有历史状态,请重新操作!");

            return mapping.findForward("error");
        }

        int ioldStatus = Integer.parseInt(oldStatus);

        String reason = request.getParameter("reason");

        String depotpartId = request.getParameter("depotpartId");

        importLog.info(fullId + ":" + user.getStafferName() + ";oldStatus:" + oldStatus);

        importLog.info(fullId + ":" + user.getStafferName() + ";nextStatus:" + statuss);

        CommonTools.saveParamers(request);

        OutBean out = null;

        out = outDAO.find(fullId);

        if (out.getStatus() == statuss)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作!");

            return mapping.findForward("error");
        }

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作!");

            return mapping.findForward("error");
        }

        if (out.getStatus() != ioldStatus)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "单据已经被审批,请重新操作!");

            return mapping.findForward("error");
        }

        int resultStatus = -1;

        // 入库单的提交(调拨)
        if (out.getType() == OutConstant.OUT_TYPE_INBILL && statuss == OutConstant.STATUS_SUBMIT)
        {
            try
            {
                resultStatus = outManager.submit(out.getFullId(), user,
                    StorageConstant.OPR_STORAGE_OUTBILLIN);

                request.setAttribute(KeyConstant.MESSAGE, out.getFullId() + "库单成功提交!");
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理异常：" + e.getErrorContent());

                return mapping.findForward("error");
            }
        }
        // 进入库单 库管--分经理--总裁--董事长
        else if (out.getType() == OutConstant.OUT_TYPE_INBILL
                 && statuss != OutConstant.STATUS_SUBMIT)
        {
            if (out.getOutType() == OutConstant.OUTTYPE_IN_COMMON
                || out.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "采购入库和调拨没有此操作");

                return mapping.findForward("error");
            }

            // 进入待总裁审批
            if (statuss == OutConstant.BUY_STATUS_CEO_CHECK)
            {
                try
                {
                    resultStatus = outManager.pass(fullId, user, OutConstant.STATUS_CEO_CHECK,
                        reason, depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 进入待董事长审批
            if (statuss == OutConstant.BUY_STATUS_CHAIRMA_CHECK)
            {
                try
                {
                    resultStatus = outManager.pass(fullId, user, OutConstant.STATUS_CHAIRMA_CHECK,
                        reason, depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 进入库管发货(结束了)
            if (statuss == OutConstant.BUY_STATUS_PASS)
            {
                try
                {
                    resultStatus = outManager.pass(fullId, user, OutConstant.STATUS_PASS, reason,
                        depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 驳回
            if (statuss == OutConstant.BUY_STATUS_REJECT)
            {
                try
                {
                    resultStatus = outManager.reject(fullId, user, reason);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }
        }
        // 销售单的审批流程
        else
        {
            // 业务员提交销售单
            if (statuss == OutConstant.STATUS_SUBMIT)
            {
                try
                {
                    resultStatus = outManager.submit(fullId, user,
                        StorageConstant.OPR_STORAGE_OUTBILL);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 区域总经理信用审核通过
            if (statuss == OutConstant.STATUS_TEMP)
            {
                try
                {
                    resultStatus = outManager.pass(fullId, user, OutConstant.STATUS_SUBMIT, reason,
                        depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 结算中心通过 物流管理员 库管通过 总裁通过
            if (statuss == OutConstant.STATUS_MANAGER_PASS
                || statuss == OutConstant.STATUS_FLOW_PASS || statuss == OutConstant.STATUS_PASS)
            {
                // 这里需要计算客户的信用金额-是否报送物流中心经理审批
                boolean outCredit = parameterDAO.getBoolean(SysConfigConstant.OUT_CREDIT);

                // 如果是黑名单的客户(且没有付款)
                if (outCredit && out.getReserve3() == OutConstant.OUT_SAIL_TYPE_MONEY
                    && out.getType() == OutConstant.OUT_TYPE_OUTBILL
                    && out.getPay() == OutConstant.PAY_NOT)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有结算中心确定已经回款后才可以审批此销售单");

                    return mapping.findForward("error");
                }

                try
                {
                    resultStatus = outManager.pass(fullId, user, statuss, reason, depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 驳回
            if (statuss == OutConstant.STATUS_REJECT)
            {
                try
                {
                    resultStatus = outManager.reject(fullId, user, reason);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }
        }

        // 核对状态方式发生异常
        OutBean realOut = outDAO.findRealOut(fullId);

        if (realOut != null && realOut.getStatus() != resultStatus)
        {
            String msg = "严重错误,当前单据的状态应该是:" + OutHelper.getStatus(resultStatus) + ",而不是"
                         + OutHelper.getStatus(realOut.getStatus()) + ".请联系管理员确认此单的正确状态!";

            request.setAttribute(KeyConstant.ERROR_MESSAGE, msg);

            loggerError(fullId + ":" + msg);

            return mapping.findForward("error");

        }

        importLog.info(fullId + ":" + user.getStafferName() + ";form:" + oldStatus + ";to"
                       + resultStatus + "(SUCCESS)");

        RequestTools.actionInitQuery(request);

        request.setAttribute(KeyConstant.MESSAGE, "单据[" + fullId + "]操作成功,下一步是:"
                                                  + OutHelper.getStatus(realOut.getStatus()));

        if (realOut.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            if (StringTools.isNullOrNone(request.getParameter("queryType")))
            {
                return querySelfOut(mapping, form, request, reponse);
            }

            return queryOut(mapping, form, request, reponse);
        }
        else
        {
            if (StringTools.isNullOrNone(request.getParameter("queryType")))
            {
                return querySelfBuy(mapping, form, request, reponse);
            }

            return queryBuy(mapping, form, request, reponse);
        }
    }

    /**
     * 领样转销售
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward swatchToSail(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        synchronized (S_LOCK)
        {
            String outId = request.getParameter("outId");

            OutBean bean = outDAO.find(outId);

            if (bean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在,请重新操作");

                return mapping.findForward("error");
            }

            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outId);

            bean.setBaseList(list);

            // 验证ref
            ConditionParse con = new ConditionParse();

            con.addWhereStr();

            con.addCondition("OutBean.refOutFullId", "=", outId);

            con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

            List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

            for (OutBean outBean : refList)
            {
                if (outManager.isSwatchToSail(outBean.getFullId()) && !OutHelper.isSailEnd(outBean))
                {
                    // 异常不能增加,只能有一个当前的
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "领样转销售只能存在一个未审批结束的,未审批单据:"
                                                                    + outBean.getFullId());

                    return mapping.findForward("error");
                }
            }

            List<OutBean> refBuyList = queryRefOut(request, outId);

            List<BaseBean> baseList = bean.getBaseList();

            // 计算出已经退货的数量
            for (Iterator iterator = baseList.iterator(); iterator.hasNext();)
            {
                BaseBean baseBean = (BaseBean)iterator.next();

                int hasBack = 0;

                // 退库
                for (OutBean ref : refBuyList)
                {
                    List<BaseBean> refBaseList = ref.getBaseList();

                    for (BaseBean refBase : refBaseList)
                    {
                        if (refBase.equals(baseBean))
                        {
                            hasBack += refBase.getAmount();

                            break;
                        }
                    }
                }

                // 转销售的
                for (OutBean ref : refList)
                {
                    List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(ref.getFullId());

                    for (BaseBean refBase : refBaseList)
                    {
                        if (refBase.equals(baseBean))
                        {
                            hasBack += refBase.getAmount();

                            break;
                        }
                    }
                }

                baseBean.setAmount(baseBean.getAmount() - hasBack);

                if (baseBean.getAmount() <= 0)
                {
                    iterator.remove();
                }
            }

            if (baseList.size() == 0)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "领样已经全部退库或者销售,请重新操作");

                return mapping.findForward("error");
            }

            // 生成销售单,然后保存
            OutBean newOut = new OutBean();

            newOut.setOutTime(TimeTools.now_short());
            newOut.setType(OutConstant.OUT_TYPE_OUTBILL);
            newOut.setOutType(OutConstant.OUTTYPE_OUT_COMMON);
            newOut.setRefOutFullId(outId);
            newOut.setDescription("领样转销售,领样单据:" + outId);
            newOut.setDepartment(bean.getDepartment());
            newOut.setLocation(bean.getLocation());
            newOut.setLocationId(bean.getLocationId());
            newOut.setDepotpartId(bean.getDepotpartId());
            newOut.setStafferId(bean.getStafferId());
            newOut.setStafferName(bean.getStafferName());

            newOut.setBaseList(baseList);

            try
            {
                String newFullId = outManager.addSwatchToSail(Helper.getUser(request), newOut);

                CommonTools.removeParamers(request);

                request.setAttribute("fow", "1");

                request.setAttribute("outId", newFullId);

                request.setAttribute("lock_sw", true);

                return this.findOut(mapping, form, request, reponse);
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                return mapping.findForward("error");
            }
        }
    }

    /**
     * 查询库单（或者修改）
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findOut(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String outId = RequestTools.getValueFromRequest(request, "outId");

        String fow = RequestTools.getValueFromRequest(request, "fow");

        CommonTools.saveParamers(request);

        User user = Helper.getUser(request);

        OutVO bean = null;
        try
        {
            bean = outDAO.findVO(outId);

            if (bean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在,请重新操作");

                return mapping.findForward("error");
            }

            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outId);

            bean.setBaseList(list);

            List<FlowLogBean> logs = flowLogDAO.queryEntityBeansByFK(outId);

            List<FlowLogVO> voList = ListTools.changeList(logs, FlowLogVO.class,
                FlowLogHelper.class, "getOutLogVO");

            request.setAttribute("bean", bean);

            request.setAttribute("fristBase", list.get(0));

            if (list.size() > 1)
            {
                request.setAttribute("lastBaseList", list.subList(1, list.size()));
            }

            request.setAttribute("logList", voList);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        if (bean.getType() == OutConstant.OUT_TYPE_INBILL)
        {
            request.setAttribute("flag", "1");
        }

        innerForPrepare(request);

        if ("1".equals(fow))
        {
            if (bean.getType() == OutConstant.OUT_TYPE_OUTBILL)
            {
                if (outManager.isSwatchToSail(outId))
                {
                    request.setAttribute("lock_sw", true);
                }
                // 处理修改
                return mapping.findForward("updateOut");
            }
            else
            {
                // 处理修改
                return mapping.findForward("updateBuy");
            }
        }

        if ("4".equals(fow))
        {
            request.setAttribute("year", TimeTools.now("yyyy"));
            request.setAttribute("month", TimeTools.now("MM"));
            request.setAttribute("day", TimeTools.now("dd"));
            return mapping.findForward("print");
        }

        // 调出的处理
        if ("5".equals(fow))
        {
            if (bean.getInway() != OutConstant.IN_WAY)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不在在途中，不能处理");

                return mapping.findForward("error");
            }

            // 查询目的库的良品仓区
            List<DepotpartBean> depotpartList = depotpartDAO.queryOkDepotpartInDepot(bean
                .getDestinationId());

            request.setAttribute("depotpartList", depotpartList);

            return mapping.findForward("handerInvokeBuy");
        }

        // 修改发票类型
        if ("6".equals(fow))
        {
            List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition(
                "where forward = ?", InvoiceConstant.INVOICE_FORWARD_OUT);

            request.setAttribute("invoiceList", invoiceList);

            return mapping.findForward("handerInvokeBuy");
        }

        // 处理个人领样退库
        if ("91".equals(fow))
        {
            synchronized (S_LOCK)
            {
                ConditionParse con = new ConditionParse();

                con.addWhereStr();

                con.addCondition("OutBean.refOutFullId", "=", outId);

                con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

                // 领样转销售
                List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

                for (OutBean outBean : refList)
                {
                    if (outManager.isSwatchToSail(outBean.getFullId())
                        && !OutHelper.isSailEnd(outBean))
                    {
                        // 异常不能增加,只能有一个当前的
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "领样转销售只能存在一个未审批结束的,请重新操作");

                        return mapping.findForward("error");
                    }
                }

                // 领样退库未审批的
                con.clear();

                con.addWhereStr();

                con.addCondition("OutBean.refOutFullId", "=", outId);

                con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

                con.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SAVE);

                con.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_SWATCH);

                int count = outDAO.countByCondition(con.toString());

                if (count > 0)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "此领样已经申请退货请处理结束后再申请");

                    return mapping.findForward("error");
                }

                List<OutBean> refBuyList = queryRefOut(request, outId);

                List<BaseBean> baseList = bean.getBaseList();

                // 计算出已经退货的数量
                for (BaseBean baseBean : baseList)
                {
                    int hasBack = 0;

                    // 退库
                    for (OutBean ref : refBuyList)
                    {
                        List<BaseBean> refBaseList = ref.getBaseList();

                        for (BaseBean refBase : refBaseList)
                        {
                            if (refBase.equals(baseBean))
                            {
                                hasBack += refBase.getAmount();

                                break;
                            }
                        }
                    }

                    // 转销售的
                    for (OutBean ref : refList)
                    {
                        List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(ref.getFullId());

                        for (BaseBean refBase : refBaseList)
                        {
                            if (refBase.equals(baseBean))
                            {
                                hasBack += refBase.getAmount();

                                break;
                            }
                        }
                    }

                    baseBean.setInway(hasBack);
                }

                return mapping.findForward("handerOutBack");
            }
        }

        // 处理销售退库
        if ("92".equals(fow))
        {
            synchronized (S_LOCK)
            {
                ActionForward error = checkAddOutBack(mapping, request, outId);

                if (error != null)
                {
                    return error;
                }

                List<OutBean> refBuyList = queryRefOut(request, outId);

                List<BaseBean> baseList = bean.getBaseList();

                // 计算出已经退货的数量
                for (BaseBean baseBean : baseList)
                {
                    int hasBack = 0;

                    // 退库
                    for (OutBean ref : refBuyList)
                    {
                        List<BaseBean> refBaseList = ref.getBaseList();

                        for (BaseBean refBase : refBaseList)
                        {
                            if (refBase.equals(baseBean))
                            {
                                hasBack += refBase.getAmount();

                                break;
                            }
                        }
                    }

                    baseBean.setInway(hasBack);
                }

                return mapping.findForward("handerOutBack2");
            }
        }

        // 申请退款
        if ("93".equals(fow))
        {
            double backTotal = outDAO.sumOutBackValue(outId);

            double hadOut = outBillDAO.sumByRefId(outId);

            request.setAttribute("hadOut", hadOut);

            request.setAttribute("backTotal", backTotal);

            ResultBean check = outManager.checkOutPayStatus(user, bean);

            if (check.getResult() != -1)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "金额全部使用,无法申请退款");

                return mapping.findForward("error");
            }

            request.setAttribute("check", check);

            return mapping.findForward("applyBackPay");
        }

        if (bean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            List<InBillVO> billList = inBillDAO.queryEntityVOsByFK(outId);

            request.setAttribute("billList", billList);

            List<OutBillVO> outBillList = outBillDAO.queryEntityVOsByFK(outId);

            request.setAttribute("outBillList", outBillList);

            if (bean.getOutType() == OutConstant.OUTTYPE_OUT_CONSIGN)
            {
                // 委托代销的结算单
                List<OutBalanceBean> balanceList = outBalanceDAO.queryEntityBeansByFK(outId);

                for (Iterator iterator = balanceList.iterator(); iterator.hasNext();)
                {
                    OutBalanceBean outBalanceBean = (OutBalanceBean)iterator.next();

                    if (outBalanceBean.getStatus() != OutConstant.OUTBALANCE_STATUS_PASS)
                    {
                        iterator.remove();
                    }
                }

                request.setAttribute("balanceList", balanceList);
            }

            if (bean.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH)
            {
                queryRefOut(request, outId);

                queryRefOut2(request, outId);
            }

            // 退单
            if (bean.getOutType() == OutConstant.OUTTYPE_OUT_COMMON
                || bean.getOutType() == OutConstant.OUTTYPE_OUT_RETAIL
                || bean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
            {
                queryRefOut(request, outId);
            }

            ResultBean checkOutPayStatus = outManager.checkOutPayStatus(user, bean);

            request.setAttribute("checkOutPayStatus", checkOutPayStatus);

            // 显示行业

            return mapping.findForward("detailOut");
        }

        if (bean.getStatus() == OutConstant.STATUS_PASS)
        {
            // 标识为结束
            bean.setStatus(OutConstant.STATUS_SEC_PASS);
        }

        return mapping.findForward("detailBuy");
    }

    /**
     * 查询销售单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryOut(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<OutBean> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition2(request, condtion);

            int total = outDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYOUT);

            list = outDAO.queryEntityBeansByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYOUT);

            list = outDAO.queryEntityBeansByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYOUT), PageSeparateTools.getPageSeparate(request, RPTQUERYOUT));
        }

        request.setAttribute("list", list);

        return mapping.findForward("rptQueryOut");
    }

    /**
     * 查询委托代销的清单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryOutBalance(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<OutBalanceVO> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition3(request, condtion);

            int total = outBalanceDAO.countVOByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYOUTBALANCE);

            list = outBalanceDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYOUTBALANCE);

            list = outBalanceDAO
                .queryEntityVOsByCondition(PageSeparateTools.getCondition(request,
                    RPTQUERYOUTBALANCE), PageSeparateTools.getPageSeparate(request,
                    RPTQUERYOUTBALANCE));
        }

        request.setAttribute("list", list);

        return mapping.findForward("rptQueryOutBalance");
    }

    /**
     * @param request
     * @param condtion
     */
    protected void setInnerCondition2(HttpServletRequest request, ConditionParse condtion)
    {
        // 条件查询
        String outTime = request.getParameter("outTime");

        String outTime1 = request.getParameter("outTime1");

        String customerName = request.getParameter("customerName");

        String fullId = request.getParameter("fullId");

        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCondition("OutBean.outTime", ">=", outTime);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", ">=", TimeTools.now_short( -180));

            request.setAttribute("outTime", TimeTools.now_short( -180));
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCondition("OutBean.outTime", "<=", outTime1);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", "<=", TimeTools.now_short());

            request.setAttribute("outTime1", TimeTools.now_short());
        }

        if ( !StringTools.isNullOrNone(fullId))
        {
            condtion.addCondition("OutBean.fullId", "like", fullId);
        }

        if ( !StringTools.isNullOrNone(customerName))
        {
            condtion.addCondition("OutBean.customerName", "like", customerName);
        }

        // (url)固定查询
        String stafferId = request.getParameter("stafferId");

        String mode = request.getParameter("mode");

        String invoiceId = request.getParameter("invoiceId");

        String dutyId = request.getParameter("dutyId");

        String customerId = request.getParameter("customerId");

        String invoiceStatus = request.getParameter("invoiceStatus");

        String bad = request.getParameter("bad");

        if ( !StringTools.isNullOrNone(invoiceId))
        {
            condtion.addCondition("OutBean.invoiceId", "=", invoiceId);
        }

        if ( !StringTools.isNullOrNone(dutyId))
        {
            condtion.addCondition("OutBean.dutyId", "=", dutyId);
        }

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerId", "=", customerId);
        }

        if ( !StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("OutBean.stafferId", "=", stafferId);
        }

        if ( !StringTools.isNullOrNone(invoiceStatus))
        {
            condtion.addIntCondition("OutBean.invoiceStatus", "=", invoiceStatus);
        }

        // 查询需要勾款的销售单
        if ("0".equals(mode))
        {
            condtion.addCondition("and OutBean.status in (1, 3, 6, 7, 8, 9)");

            // 过滤委托代销
            condtion.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_OUT_CONSIGN);
        }

        // 需要开票的销售单
        if ("1".equals(mode))
        {
            condtion.addCondition(" and OutBean.status in (3, 4)");

            // 过滤委托代销
            condtion.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_OUT_CONSIGN);
        }

        if ("1".equals(bad))
        {
            condtion.addCondition(" and OutBean.status in (3, 4)");

            condtion.addIntCondition("OutBean.badDebts", ">", 0);

            condtion.addIntCondition("OutBean.badDebtsCheckStatus", "=",
                OutConstant.BADDEBTSCHECKSTATUS_NO);
        }

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        condtion.addCondition("order by OutBean.id desc");
    }

    /**
     * 委托代销清单过滤
     * 
     * @param request
     * @param condtion
     */
    protected void setInnerCondition3(HttpServletRequest request, ConditionParse condtion)
    {
        // 条件查询
        String alogTime = request.getParameter("alogTime");

        String blogTime = request.getParameter("blogTime");

        String fullId = request.getParameter("outId");

        if ( !StringTools.isNullOrNone(alogTime))
        {
            condtion.addCondition("OutBalanceBean.logTime", ">=", alogTime);
        }
        else
        {
            condtion.addCondition("OutBalanceBean.logTime", ">=", TimeTools.now( -180));

            request.setAttribute("alogTime", TimeTools.now( -180));
        }

        if ( !StringTools.isNullOrNone(blogTime))
        {
            condtion.addCondition("OutBalanceBean.logTime", "<=", blogTime);
        }
        else
        {
            condtion.addCondition("OutBalanceBean.logTime", "<=", TimeTools.now());

            request.setAttribute("blogTime", TimeTools.now());
        }

        if ( !StringTools.isNullOrNone(fullId))
        {
            condtion.addCondition("OutBalanceBean.outId", "like", fullId);
        }

        // (url)固定查询
        String stafferId = request.getParameter("stafferId");

        String invoiceId = request.getParameter("invoiceId");

        String dutyId = request.getParameter("dutyId");

        String type = request.getParameter("type");

        String pay = request.getParameter("pay");

        String customerId = request.getParameter("customerId");

        String invoiceStatus = request.getParameter("invoiceStatus");

        if ( !StringTools.isNullOrNone(invoiceId))
        {
            condtion.addCondition("OutBean.invoiceId", "=", invoiceId);
        }

        if ( !StringTools.isNullOrNone(dutyId))
        {
            condtion.addCondition("OutBean.dutyId", "=", dutyId);
        }

        if ( !StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("OutBean.stafferId", "=", stafferId);
        }

        if ( !StringTools.isNullOrNone(invoiceStatus))
        {
            condtion.addIntCondition("OutBalanceBean.invoiceStatus", "=", invoiceStatus);
        }

        if ( !StringTools.isNullOrNone(type))
        {
            condtion.addIntCondition("OutBalanceBean.type", "=", type);
        }

        if ( !StringTools.isNullOrNone(pay))
        {
            condtion.addIntCondition("OutBalanceBean.pay", "=", pay);
        }

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBalanceBean.customerId", "=", customerId);
        }

        condtion.addIntCondition("OutBalanceBean.status", "=", OutConstant.OUTBALANCE_STATUS_PASS);

        condtion.addCondition("order by OutBalanceBean.logTime desc");
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
     * @return the productDAO
     */
    public ProductDAO getProductDAO()
    {
        return productDAO;
    }

    /**
     * @param productDAO
     *            the productDAO to set
     */
    public void setProductDAO(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
    }

    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    /**
     * @param customerDAO
     *            the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
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
     * @return the consignDAO
     */
    public ConsignDAO getConsignDAO()
    {
        return consignDAO;
    }

    /**
     * @param consignDAO
     *            the consignDAO to set
     */
    public void setConsignDAO(ConsignDAO consignDAO)
    {
        this.consignDAO = consignDAO;
    }

    /**
     * @return the depotpartDAO
     */
    public DepotpartDAO getDepotpartDAO()
    {
        return depotpartDAO;
    }

    /**
     * @param depotpartDAO
     *            the depotpartDAO to set
     */
    public void setDepotpartDAO(DepotpartDAO depotpartDAO)
    {
        this.depotpartDAO = depotpartDAO;
    }

    /**
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
    }

    /**
     * @return the storageDAO
     */
    public StorageDAO getStorageDAO()
    {
        return storageDAO;
    }

    /**
     * @param storageDAO
     *            the storageDAO to set
     */
    public void setStorageDAO(StorageDAO storageDAO)
    {
        this.storageDAO = storageDAO;
    }

    /**
     * @return the providerDAO
     */
    public ProviderDAO getProviderDAO()
    {
        return providerDAO;
    }

    /**
     * @param providerDAO
     *            the providerDAO to set
     */
    public void setProviderDAO(ProviderDAO providerDAO)
    {
        this.providerDAO = providerDAO;
    }

    /**
     * @return the parameterDAO
     */
    public ParameterDAO getParameterDAO()
    {
        return parameterDAO;
    }

    /**
     * @param parameterDAO
     *            the parameterDAO to set
     */
    public void setParameterDAO(ParameterDAO parameterDAO)
    {
        this.parameterDAO = parameterDAO;
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
     * @return the departmentDAO
     */
    public DepartmentDAO getDepartmentDAO()
    {
        return departmentDAO;
    }

    /**
     * @param departmentDAO
     *            the departmentDAO to set
     */
    public void setDepartmentDAO(DepartmentDAO departmentDAO)
    {
        this.departmentDAO = departmentDAO;
    }

    /**
     * @return the baseDAO
     */
    public BaseDAO getBaseDAO()
    {
        return baseDAO;
    }

    /**
     * @param baseDAO
     *            the baseDAO to set
     */
    public void setBaseDAO(BaseDAO baseDAO)
    {
        this.baseDAO = baseDAO;
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
     * @return the locationDAO
     */
    public LocationDAO getLocationDAO()
    {
        return locationDAO;
    }

    /**
     * @param locationDAO
     *            the locationDAO to set
     */
    public void setLocationDAO(LocationDAO locationDAO)
    {
        this.locationDAO = locationDAO;
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
     * @return the depotDAO
     */
    public DepotDAO getDepotDAO()
    {
        return depotDAO;
    }

    /**
     * @param depotDAO
     *            the depotDAO to set
     */
    public void setDepotDAO(DepotDAO depotDAO)
    {
        this.depotDAO = depotDAO;
    }

    /**
     * @return the storageRelationManager
     */
    public StorageRelationManager getStorageRelationManager()
    {
        return storageRelationManager;
    }

    /**
     * @param storageRelationManager
     *            the storageRelationManager to set
     */
    public void setStorageRelationManager(StorageRelationManager storageRelationManager)
    {
        this.storageRelationManager = storageRelationManager;
    }

    /**
     * @return the invoiceDAO
     */
    public InvoiceDAO getInvoiceDAO()
    {
        return invoiceDAO;
    }

    /**
     * @param invoiceDAO
     *            the invoiceDAO to set
     */
    public void setInvoiceDAO(InvoiceDAO invoiceDAO)
    {
        this.invoiceDAO = invoiceDAO;
    }

    /**
     * @return the dutyDAO
     */
    public DutyDAO getDutyDAO()
    {
        return dutyDAO;
    }

    /**
     * @param dutyDAO
     *            the dutyDAO to set
     */
    public void setDutyDAO(DutyDAO dutyDAO)
    {
        this.dutyDAO = dutyDAO;
    }

    /**
     * @return the fatalNotify
     */
    public FatalNotify getFatalNotify()
    {
        return fatalNotify;
    }

    /**
     * @param fatalNotify
     *            the fatalNotify to set
     */
    public void setFatalNotify(FatalNotify fatalNotify)
    {
        this.fatalNotify = fatalNotify;
    }

    /**
     * @return the authManager
     */
    public AuthManager getAuthManager()
    {
        return authManager;
    }

    /**
     * @param authManager
     *            the authManager to set
     */
    public void setAuthManager(AuthManager authManager)
    {
        this.authManager = authManager;
    }

    /**
     * @return the baseBalanceDAO
     */
    public BaseBalanceDAO getBaseBalanceDAO()
    {
        return baseBalanceDAO;
    }

    /**
     * @param baseBalanceDAO
     *            the baseBalanceDAO to set
     */
    public void setBaseBalanceDAO(BaseBalanceDAO baseBalanceDAO)
    {
        this.baseBalanceDAO = baseBalanceDAO;
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

    /**
     * @return the outQueryDAO
     */
    public OutQueryDAO getOutQueryDAO()
    {
        return outQueryDAO;
    }

    /**
     * @param outQueryDAO
     *            the outQueryDAO to set
     */
    public void setOutQueryDAO(OutQueryDAO outQueryDAO)
    {
        this.outQueryDAO = outQueryDAO;
    }

    /**
     * @return the stafferManager
     */
    public StafferManager getStafferManager()
    {
        return stafferManager;
    }

    /**
     * @param stafferManager
     *            the stafferManager to set
     */
    public void setStafferManager(StafferManager stafferManager)
    {
        this.stafferManager = stafferManager;
    }

    /**
     * @return the showDAO
     */
    public ShowDAO getShowDAO()
    {
        return showDAO;
    }

    /**
     * @param showDAO
     *            the showDAO to set
     */
    public void setShowDAO(ShowDAO showDAO)
    {
        this.showDAO = showDAO;
    }

    /**
     * @return the inBillDAO
     */
    public InBillDAO getInBillDAO()
    {
        return inBillDAO;
    }

    /**
     * @param inBillDAO
     *            the inBillDAO to set
     */
    public void setInBillDAO(InBillDAO inBillDAO)
    {
        this.inBillDAO = inBillDAO;
    }

    /**
     * @return the dutyVSInvoiceDAO
     */
    public DutyVSInvoiceDAO getDutyVSInvoiceDAO()
    {
        return dutyVSInvoiceDAO;
    }

    /**
     * @param dutyVSInvoiceDAO
     *            the dutyVSInvoiceDAO to set
     */
    public void setDutyVSInvoiceDAO(DutyVSInvoiceDAO dutyVSInvoiceDAO)
    {
        this.dutyVSInvoiceDAO = dutyVSInvoiceDAO;
    }

    /**
     * @return the outBillDAO
     */
    public OutBillDAO getOutBillDAO()
    {
        return outBillDAO;
    }

    /**
     * @param outBillDAO
     *            the outBillDAO to set
     */
    public void setOutBillDAO(OutBillDAO outBillDAO)
    {
        this.outBillDAO = outBillDAO;
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
}
