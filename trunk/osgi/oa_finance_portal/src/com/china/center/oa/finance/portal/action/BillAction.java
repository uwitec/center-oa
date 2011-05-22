package com.china.center.oa.finance.portal.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.center.china.osgi.publics.User;
import com.center.china.osgi.publics.file.writer.WriteFile;
import com.center.china.osgi.publics.file.writer.WriteFileFactory;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONPageSeparateTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.common.MYException;
import com.china.center.common.taglib.DefinedCommon;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.finance.bean.BackPayApplyBean;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.constant.BackPayApplyConstant;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.dao.StatBankDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.vo.InBillVO;
import com.china.center.oa.finance.vo.OutBillVO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.manager.AuthManager;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * BillAction
 * 
 * @author ZHUZHU
 * @version 2011-1-9
 * @see BillAction
 * @since 3.0
 */
public class BillAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private InBillDAO inBillDAO = null;

    private BankDAO bankDAO = null;

    private OutBillDAO outBillDAO = null;

    private OutDAO outDAO = null;

    private AuthManager authManager = null;

    private UserManager userManager = null;

    private FinanceFacade financeFacade = null;

    private InvoiceDAO invoiceDAO = null;

    private StatBankDAO statBankDAO = null;

    private ParameterDAO parameterDAO = null;

    private PaymentDAO paymentDAO = null;

    private static final String QUERYINBILL = "queryInBill";

    private static final String QUERYOUTBILL = "queryOutBill";

    private static final String QUERYSELFINBILL = "querySelfInBill";

    /**
     * queryInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryInBill(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        Map<String, String> initMap = initLogTime(request, condtion, "InBillBean");

        ActionTools.processJSONQueryCondition(QUERYINBILL, request, condtion, initMap);

        if ( !userManager.containAuth(user.getId(), AuthConstant.BILL_QUERY_ALL))
        {
            condtion.addCondition("InBillBean.locationId", "=", user.getLocationId());
        }

        condtion.addCondition("order by InBillBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYINBILL, request, condtion,
            this.inBillDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * initLogTime
     * 
     * @param request
     * @param condtion
     * @return
     */
    private Map<String, String> initLogTime(HttpServletRequest request, ConditionParse condtion,
                                            String pfix)
    {
        Map<String, String> changeMap = new HashMap<String, String>();

        String alogTime = request.getParameter("alogTime");

        String blogTime = request.getParameter("blogTime");

        if (StringTools.isNullOrNone(alogTime) && StringTools.isNullOrNone(blogTime))
        {
            changeMap.put("alogTime", TimeTools.now_short( -15));

            changeMap.put("blogTime", TimeTools.now_short(1));

            condtion.addCondition(pfix + ".logTime", ">=", TimeTools.now_short( -15));

            condtion.addCondition(pfix + ".logTime", "<=", TimeTools.now_short(1));
        }

        return changeMap;
    }

    /**
     * queryOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryOutBill(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        Map<String, String> initMap = initLogTime(request, condtion, "OutBillBean");

        ActionTools.processJSONQueryCondition(QUERYOUTBILL, request, condtion, initMap);

        if ( !userManager.containAuth(user.getId(), AuthConstant.BILL_QUERY_ALL))
        {
            condtion.addCondition("OutBillBean.locationId", "=", user.getLocationId());
        }

        condtion.addCondition("order by OutBillBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYOUTBILL, request, condtion,
            this.outBillDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryTransferOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryTransferOutBill(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYOUTBILL, request, condtion);

        condtion.addIntCondition("OutBillBean.type", "=", FinanceConstant.OUTBILL_TYPE_TRANSFER);

        condtion.addIntCondition("OutBillBean.status", "=", FinanceConstant.OUTBILL_STATUS_SUBMIT);

        condtion.addCondition("order by OutBillBean.id desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYOUTBILL, request, condtion,
            this.outBillDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * preForAddInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddInBill(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        List<BankBean> banlList = bankDAO.listEntityBeans("order by BankBean.name");

        request.setAttribute("bankList", banlList);

        return mapping.findForward("addInBill");
    }

    /**
     * preForAddOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddOutBill(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        List<BankBean> banlList = bankDAO.listEntityBeans("order by BankBean.name");

        request.setAttribute("bankList", banlList);

        List<InvoiceBean> invoiceList = invoiceDAO.listForwardIn();

        request.setAttribute("invoiceList", invoiceList);

        return mapping.findForward("addOutBill");
    }

    /**
     * addInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addInBill(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        InBillBean bean = new InBillBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setStafferId(user.getStafferId());

            bean.setLocationId(user.getLocationId());

            bean.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

            bean.setLock(FinanceConstant.BILL_LOCK_NO);

            // TEMPIMPL 收款单在增加关联单据，给4月前的单据入库使用
            if ( !StringTools.isNullOrNone(bean.getOutId()))
            {
                OutBean out = outDAO.find(bean.getOutId());

                if (out == null)
                {
                    throw new MYException("销售单不存在,请确认操作");
                }

                if ("2011-04-01".compareTo(out.getOutTime()) < 0)
                {
                    throw new MYException("销售单必须在(2011-04-01),请确认操作");
                }
            }

            // 坏账
            if (bean.getType() == FinanceConstant.INBILL_TYPE_BADOUT)
            {
                bean.setOutId(request.getParameter("c_outId"));
            }

            financeFacade.addInBillBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryInBill");
    }

    /**
     * addOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addOutBill(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        OutBillBean bean = new OutBillBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setStafferId(user.getStafferId());

            bean.setLocationId(user.getLocationId());

            bean.setLock(FinanceConstant.BILL_LOCK_NO);

            financeFacade.addOutBillBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryOutBill");
    }

    /**
     * deleteInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteInBill(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deleteInBillBean(user.getId(), id);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * updateInBillBeanChecks
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateInBillBeanChecks(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            String checks = request.getParameter("checks");

            User user = Helper.getUser(request);

            financeFacade.updateInBillBeanChecks(user.getId(), id, checks);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * updateOutBillBeanChecks
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateOutBillBeanChecks(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            String checks = request.getParameter("checks");

            User user = Helper.getUser(request);

            financeFacade.updateOutBillBeanChecks(user.getId(), id, checks);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * deleteOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteOutBill(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deleteOutBillBean(user.getId(), id);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 接收转账
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passTransferOutBill(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.passTransferOutBillBean(user.getId(), id);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 驳回
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward rejectTransferOutBill(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.rejectTransferOutBillBean(user.getId(), id);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * querySelfInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfInBill(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYSELFINBILL, request, condtion);

        condtion.addCondition("InBillBean.ownerId", "=", user.getStafferId());

        condtion.addIntCondition("InBillBean.status", "=", FinanceConstant.INBILL_STATUS_NOREF);

        condtion.addCondition("order by InBillBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSELFINBILL, request, condtion,
            this.inBillDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * preForBingInBillByCustomerId
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForBingInBillByCustomerId(ActionMapping mapping, ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response)
        throws ServletException
    {
        String customerId = request.getParameter("customerId");

        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("InBillBean.ownerId", "=", user.getStafferId());

        condtion.addIntCondition("InBillBean.status", "=", FinanceConstant.INBILL_STATUS_NOREF);

        condtion.addCondition("InBillBean.customerId", "=", customerId);

        condtion.addCondition("order by InBillBean.logTime desc");

        List<InBillVO> billList = inBillDAO.queryEntityVOsByCondition(condtion);

        request.setAttribute("billList", billList);

        return mapping.findForward("bingBill");
    }

    /**
     * findInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findInBill(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        InBillVO bean = inBillDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", mapping, request);
        }

        request.setAttribute("bean", bean);

        return mapping.findForward("detailInBill");
    }

    /**
     * findOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findOutBill(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        OutBillVO bean = outBillDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", mapping, request);
        }

        request.setAttribute("bean", bean);

        return mapping.findForward("detailOutBill");
    }

    /**
     * splitInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward splitInBill(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            String id = request.getParameter("id");

            InBillBean inBill = inBillDAO.find(id);

            if (inBill == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            String newMoney = request.getParameter("newMoney");

            BackPayApplyBean bean = new BackPayApplyBean();

            bean.setType(BackPayApplyConstant.TYPE_BILL);

            bean.setBackPay(MathTools.parseDouble(newMoney));

            bean.setBillId(id);

            bean.setChangePayment(0.0d);

            bean.setCustomerId(inBill.getCustomerId());

            bean.setDescription("业务员申请预收退款");

            bean.setStafferId(user.getStafferId());

            financeFacade.addBackPayApplyBean(user.getId(), bean);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * exportInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportInBill(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        // String depotartId = request.getParameter("depotartId");

        String filenName = "INBill_" + TimeTools.now("MMddHHmmss") + ".csv";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WriteFile write = null;

        try
        {
            out = reponse.getOutputStream();

            write = WriteFileFactory.getMyTXTWriter();

            write.openFile(out);

            write.writeLine("日期,标识,帐户,类型,状态,核对,金额,原始金额,客户,职员,经手人,备注,核对");

            ConditionParse condtion = JSONPageSeparateTools.getCondition(request, QUERYINBILL);

            PageSeparate page = new PageSeparate(JSONPageSeparateTools.getPageSeparate(request,
                QUERYINBILL));

            page.reset2(page.getRowCount(), 1000);

            // TEMPLATE 分页导出记录
            while (page.nextPage())
            {
                List<InBillVO> voList = this.inBillDAO.queryEntityVOsByCondition(condtion, page);

                for (InBillVO each : voList)
                {
                    String typeName = DefinedCommon.getValue("inbillType", each.getType());
                    String statusName = DefinedCommon.getValue("inbillStatus", each.getStatus());
                    String pubCheckName = DefinedCommon.getValue("pubCheckStatus", each
                        .getCheckStatus());

                    write.writeLine("[" + each.getLogTime() + "]" + ',' + each.getId() + ','
                                    + each.getBankName() + ',' + typeName + ',' + statusName + ','
                                    + pubCheckName + ',' + MathTools.formatNum(each.getMoneys())
                                    + ',' + MathTools.formatNum(each.getSrcMoneys()) + ','
                                    + each.getCustomerName() + ',' + each.getOwnerName() + ","
                                    + each.getStafferName() + ','
                                    + StringTools.getExportString(each.getDescription()) + ','
                                    + StringTools.getExportString(each.getChecks()));
                }
            }

            write.close();

        }
        catch (Throwable e)
        {
            _logger.error(e, e);

            return null;
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                }
            }

            if (write != null)
            {

                try
                {
                    write.close();
                }
                catch (IOException e1)
                {
                }
            }
        }

        return null;
    }

    /**
     * exportOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportOutBill(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String filenName = "OUTBill_" + TimeTools.now("MMddHHmmss") + ".csv";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WriteFile write = null;

        try
        {
            out = reponse.getOutputStream();

            write = WriteFileFactory.getMyTXTWriter();

            write.openFile(out);

            write.writeLine("日期,标识,帐户,类型,状态,核对,金额,原始金额,客户,职员,经手人,备注,核对");

            ConditionParse condtion = JSONPageSeparateTools.getCondition(request, QUERYOUTBILL);

            PageSeparate page = new PageSeparate(JSONPageSeparateTools.getPageSeparate(request,
                QUERYOUTBILL));

            page.reset2(page.getRowCount(), 1000);

            while (page.nextPage())
            {
                List<OutBillVO> voList = this.outBillDAO.queryEntityVOsByCondition(condtion, page);

                for (OutBillVO each : voList)
                {
                    String typeName = DefinedCommon.getValue("outbillType", each.getType());
                    String statusName = DefinedCommon.getValue("outbillStatus", each.getStatus());
                    String pubCheckName = DefinedCommon.getValue("pubCheckStatus", each
                        .getCheckStatus());

                    write.writeLine("[" + each.getLogTime() + "]" + ',' + each.getId() + ','
                                    + each.getBankName() + ',' + typeName + ',' + statusName + ','
                                    + pubCheckName + ',' + MathTools.formatNum(each.getMoneys())
                                    + ',' + MathTools.formatNum(each.getSrcMoneys()) + ','
                                    + each.getProvideName() + ',' + each.getOwnerName() + ","
                                    + each.getStafferName() + ','
                                    + StringTools.getExportString(each.getDescription()) + ','
                                    + StringTools.getExportString(each.getChecks()));
                }
            }

            write.close();

        }
        catch (Throwable e)
        {
            _logger.error(e, e);

            return null;
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                }
            }

            if (write != null)
            {

                try
                {
                    write.close();
                }
                catch (IOException e1)
                {
                }
            }
        }

        return null;
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
     * @return the financeFacade
     */
    public FinanceFacade getFinanceFacade()
    {
        return financeFacade;
    }

    /**
     * @param financeFacade
     *            the financeFacade to set
     */
    public void setFinanceFacade(FinanceFacade financeFacade)
    {
        this.financeFacade = financeFacade;
    }

    /**
     * @return the bankDAO
     */
    public BankDAO getBankDAO()
    {
        return bankDAO;
    }

    /**
     * @param bankDAO
     *            the bankDAO to set
     */
    public void setBankDAO(BankDAO bankDAO)
    {
        this.bankDAO = bankDAO;
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
     * @return the statBankDAO
     */
    public StatBankDAO getStatBankDAO()
    {
        return statBankDAO;
    }

    /**
     * @param statBankDAO
     *            the statBankDAO to set
     */
    public void setStatBankDAO(StatBankDAO statBankDAO)
    {
        this.statBankDAO = statBankDAO;
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
     * @return the paymentDAO
     */
    public PaymentDAO getPaymentDAO()
    {
        return paymentDAO;
    }

    /**
     * @param paymentDAO
     *            the paymentDAO to set
     */
    public void setPaymentDAO(PaymentDAO paymentDAO)
    {
        this.paymentDAO = paymentDAO;
    }
}
