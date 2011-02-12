/**
 * File Name: LocationAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.portal.action;


import java.util.ArrayList;
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
import com.center.china.osgi.publics.file.read.ReadeFileFactory;
import com.center.china.osgi.publics.file.read.ReaderFile;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.actionhelper.query.HandleResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.PaymentApplyBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.PaymentApplyDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.dao.PaymentVSOutDAO;
import com.china.center.oa.finance.dao.StatBankDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.manager.StatBankManager;
import com.china.center.oa.finance.vo.BankVO;
import com.china.center.oa.finance.vo.InBillVO;
import com.china.center.oa.finance.vo.PaymentApplyVO;
import com.china.center.oa.finance.vo.PaymentVO;
import com.china.center.oa.finance.vs.PaymentVSOutBean;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.RequestDataStream;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * BankAction
 * 
 * @author ZHUZHU
 * @version 2010-6-27
 * @see FinanceAction
 * @since 1.0
 */
public class FinanceAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private FinanceFacade financeFacade = null;

    private BankDAO bankDAO = null;

    private DutyDAO dutyDAO = null;

    private InBillDAO inBillDAO = null;

    private OutDAO outDAO = null;

    private OutBalanceDAO outBalanceDAO = null;

    private UserManager userManager = null;

    private PaymentDAO paymentDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private PaymentVSOutDAO paymentVSOutDAO = null;

    private PaymentApplyDAO paymentApplyDAO = null;

    private StatBankManager statBankManager = null;

    private StatBankDAO statBankDAO = null;

    private static final String QUERYBANK = "queryBank";

    private static final String RPTQUERYBANK = "rptQueryBank";

    private static final String QUERYPAYMENT = "queryPayment";

    private static final String QUERYSELFPAYMENT = "querySelfPayment";

    private static final String QUERYPAYMENTAPPLY = "queryPaymentApply";

    private static final String QUERYSTAT = "queryStat";

    private static final String QUERYSELFPAYMENTAPPLY = "querySelfPaymentApply";

    /**
     * default constructor
     */
    public FinanceAction()
    {
    }

    /**
     * preForAddBank
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddBank(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        return mapping.findForward("addBank");
    }

    /**
     * queryBank
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryBank(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYBANK, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYBANK, request, condtion,
            this.bankDAO, new HandleResult<BankVO>()
            {
                public void handle(BankVO obj)
                {
                    double total = statBankManager.findTotalByBankId(obj.getId());

                    obj.setTotal(total);
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryStat
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryStat(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYSTAT, request, condtion);

        condtion.addCondition("order by StatBankBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSTAT, request, condtion,
            this.statBankDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryPaymentApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryPaymentApply(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        ActionTools.processJSONQueryCondition(QUERYPAYMENTAPPLY, request, condtion);

        condtion.addCondition("PaymentApplyBean.locationId", "=", user.getLocationId());

        condtion.addIntCondition("PaymentApplyBean.status", "=",
            FinanceConstant.PAYAPPLY_STATUS_INIT);

        condtion.addCondition("order by PaymentApplyBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPAYMENTAPPLY, request, condtion,
            this.paymentApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * querySelfPaymentApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfPaymentApply(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        ActionTools.processJSONQueryCondition(QUERYSELFPAYMENTAPPLY, request, condtion);

        condtion.addCondition("PaymentApplyBean.stafferId", "=", user.getStafferId());

        condtion.addCondition("order by PaymentApplyBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSELFPAYMENTAPPLY, request,
            condtion, this.paymentApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryPayment
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryPayment(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        Map<String, String> changeMap = initLogTime(request, condtion);

        ActionTools.processJSONQueryCondition(QUERYPAYMENT, request, condtion, changeMap);

        condtion.addCondition("order by PaymentBean.id desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPAYMENT, request, condtion,
            this.paymentDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    public ActionForward querySelfPayment(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        // TEMPLATE 在action里面默认查询条件
        Map<String, String> initMap = initLogTime(request, condtion);

        ActionTools.processJSONQueryCondition(QUERYSELFPAYMENT, request, condtion, initMap);

        condtion.addCondition("order by PaymentBean.id desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSELFPAYMENT, request, condtion,
            this.paymentDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * initLogTime
     * 
     * @param request
     * @param condtion
     * @return
     */
    private Map<String, String> initLogTime(HttpServletRequest request, ConditionParse condtion)
    {
        Map<String, String> changeMap = new HashMap<String, String>();

        String alogTime = request.getParameter("alogTime");

        String blogTime = request.getParameter("blogTime");

        if (StringTools.isNullOrNone(alogTime) && StringTools.isNullOrNone(blogTime))
        {
            changeMap.put("alogTime", TimeTools.now_short( -30));

            changeMap.put("blogTime", TimeTools.now_short(1));

            condtion.addCondition("PaymentBean.logTime", ">=", TimeTools.now_short( -30));

            condtion.addCondition("PaymentBean.logTime", "<=", TimeTools.now_short(1));
        }

        return changeMap;
    }

    /**
     * 查询客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryBank(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<BankVO> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition(request, condtion);

            int total = bankDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYBANK);

            list = bankDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYBANK);

            list = bankDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYBANK), PageSeparateTools.getPageSeparate(request, RPTQUERYBANK));
        }

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        request.setAttribute("list", list);

        return mapping.findForward("rptQueryBank");
    }

    /**
     * @param request
     * @param condtion
     */
    private void setInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String dutyId = request.getParameter("dutyId");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("BankBean.name", "like", name);
        }

        if ( !StringTools.isNullOrNone(dutyId))
        {
            condtion.addCondition("BankBean.dutyId", "=", dutyId);
        }

        condtion.addCondition("order by BankBean.id desc");
    }

    /**
     * addBank
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addBank(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        BankBean bean = new BankBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            financeFacade.addBankBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加帐户:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryBank");
    }

    /**
     * updateBank
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateBank(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        BankBean bean = new BankBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            financeFacade.updateBankBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作帐户:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryBank");
    }

    /**
     * delLocation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteBank(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deleteBankBean(user.getId(), id);

            ajax.setSuccess("成功删除帐户");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * deletePayment
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deletePayment(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deletePaymentBean(user.getId(), id);

            ajax.setSuccess("成功删除");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * deletePaymentApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deletePaymentApply(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deletePaymentApply(user.getId(), id);

            ajax.setSuccess("成功删除");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 领取回款(可以绑定委托清单)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward drawPayment(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        try
        {
            String id = request.getParameter("id");

            String customerId = request.getParameter("customerId");

            User user = Helper.getUser(request);

            financeFacade.drawPaymentBean(user.getId(), id, customerId);

            // 付款申请
            addApply(request, id, customerId, user);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作操作：" + e.getErrorContent());
        }

        return mapping.findForward("querySelfPayment");
    }

    /**
     * drawPayment2
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward drawPayment2(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        try
        {
            String id = request.getParameter("id");

            String customerId = request.getParameter("customerId");

            User user = Helper.getUser(request);

            // 付款申请
            addApply(request, id, customerId, user);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作操作：" + e.getErrorContent());
        }

        return mapping.findForward("querySelfPayment");
    }

    /**
     * drawPayment3(销售单和付收款单的关联)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward drawPayment3(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        try
        {
            String customerId = request.getParameter("customerId");

            User user = Helper.getUser(request);

            addApply2(request, customerId, user);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作操作：" + e.getErrorContent());
        }

        // 到自己的申请界面
        return mapping.findForward("querySelfPaymentApply");
    }

    /**
     * 付款申请
     * 
     * @param request
     * @param id
     * @param customerId
     * @param user
     * @throws MYException
     */
    private void addApply(HttpServletRequest request, String id, String customerId, User user)
        throws MYException
    {
        PaymentApplyBean apply = new PaymentApplyBean();

        apply.setCustomerId(customerId);
        apply.setLocationId(user.getLocationId());
        apply.setLogTime(TimeTools.now());
        apply.setPaymentId(id);
        apply.setStafferId(user.getStafferId());

        List<PaymentVSOutBean> vsList = new ArrayList<PaymentVSOutBean>();

        double total = 0.0d;

        // 生成付款单申请
        for (int i = 1; i <= 5; i++ )
        {
            String outId = request.getParameter("outId" + i);

            String outMoney = request.getParameter("outMoney" + i);

            if (StringTools.isNullOrNone(outMoney)
                || MathTools.parseDouble(outMoney.trim()) == 0.0d)
            {
                continue;
            }

            PaymentVSOutBean vs = new PaymentVSOutBean();

            vs.setLocationId(user.getLocationId());

            vs.setMoneys(MathTools.parseDouble(outMoney.trim()));

            if ("客户预收".equals(outId))
            {
                vs.setOutId("");
            }
            else
            {
                OutBean out = outDAO.find(outId);

                if (out != null)
                {
                    vs.setOutId(outId);
                }
                else
                {
                    // 关联委托清单
                    OutBalanceBean outBal = outBalanceDAO.find(outId);

                    if (outBal == null)
                    {
                        throw new MYException("关联的单据不存在:" + outId);
                    }

                    vs.setOutBalanceId(outId);

                    vs.setOutId(outBal.getOutId());
                }
            }

            vs.setPaymentId(id);

            vs.setStafferId(user.getStafferId());

            vsList.add(vs);

            total += vs.getMoneys();
        }

        apply.setVsList(vsList);

        // 没有申请付款
        if (vsList.size() == 0)
        {
            return;
        }

        apply.setMoneys(total);

        financeFacade.addPaymentApply(user.getId(), apply);
    }

    /**
     * 付款申请
     * 
     * @param request
     * @param id
     * @param customerId
     * @param user
     * @throws MYException
     */
    private void addApply2(HttpServletRequest request, String customerId, User user)
        throws MYException
    {
        PaymentApplyBean apply = new PaymentApplyBean();

        apply.setType(FinanceConstant.PAYAPPLY_TYPE_TEMP);
        apply.setCustomerId(customerId);
        apply.setLocationId(user.getLocationId());
        apply.setLogTime(TimeTools.now());
        apply.setStafferId(user.getStafferId());

        List<PaymentVSOutBean> vsList = new ArrayList<PaymentVSOutBean>();

        double total = 0.0d;

        String[] billIds = request.getParameterValues("billId");

        String outId = request.getParameter("outId");

        double outTotal = MathTools.parseDouble(request.getParameter("total"));

        for (String billId : billIds)
        {
            InBillBean bill = inBillDAO.find(billId);

            PaymentVSOutBean vs = new PaymentVSOutBean();

            vs.setLocationId(user.getLocationId());

            vs.setMoneys(bill.getMoneys());

            OutBean out = outDAO.find(outId);

            if (out != null)
            {
                vs.setOutId(outId);
            }
            else
            {
                // 关联委托清单
                OutBalanceBean outBal = outBalanceDAO.find(outId);

                if (outBal == null)
                {
                    throw new MYException("关联的单据不存在:" + outId);
                }

                vs.setOutBalanceId(outId);

                vs.setOutId(outBal.getOutId());
            }

            vs.setBillId(billId);

            vs.setPaymentId(bill.getPaymentId());

            vs.setStafferId(user.getStafferId());

            vsList.add(vs);

            total += vs.getMoneys();

            if (total >= outTotal)
            {
                break;
            }
        }

        apply.setVsList(vsList);

        // 没有申请付款
        if (vsList.size() == 0)
        {
            return;
        }

        apply.setMoneys(total);

        financeFacade.addPaymentApply(user.getId(), apply);
    }

    /**
     * 退领回款
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward dropPayment(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.dropPaymentBean(user.getId(), id);

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
     * findBank
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findBank(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        BankBean bean = bankDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("queryBank");
        }

        request.setAttribute("bean", bean);

        String update = request.getParameter("update");

        if ("1".equals(update))
        {
            List<DutyBean> dutyList = dutyDAO.listEntityBeans();

            request.setAttribute("dutyList", dutyList);

            return mapping.findForward("updateBank");
        }

        return mapping.findForward("detailBank");
    }

    /**
     * preForRefBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForRefBill(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        User user = Helper.getUser(request);

        String outId = request.getParameter("outId");

        String customerId = request.getParameter("customerId");

        OutBean out = outDAO.find(outId);

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据不完备");

            return mapping.findForward("error");
        }

        if (out.getTotal() - out.getHadPay() - out.getBadDebts() == 0.0)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已经全部关联预付");

            return mapping.findForward("error");
        }

        request.setAttribute("lastMoney", out.getTotal() - out.getHadPay() - out.getBadDebts());

        request.setAttribute("out", out);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("InBillBean.ownerId", "=", user.getStafferId());

        condtion.addCondition("InBillBean.customerId", "=", customerId);

        condtion.addIntCondition("InBillBean.status", "=", FinanceConstant.INBILL_STATUS_NOREF);

        condtion.addCondition("order by InBillBean.logTime desc");

        List<InBillVO> billList = inBillDAO.queryEntityVOsByCondition(condtion);

        request.setAttribute("billList", billList);

        return mapping.findForward("refBill");
    }

    /**
     * 委托代销勾款
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForRefBillForOutBalance(ActionMapping mapping, ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        User user = Helper.getUser(request);

        String outBalanceId = request.getParameter("outBalanceId");

        String customerId = request.getParameter("customerId");

        OutBalanceBean out = outBalanceDAO.find(outBalanceId);

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据不完备");

            return mapping.findForward("error");
        }

        if (out.getTotal() - out.getPayMoney() == 0.0)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已经全部关联预付");

            return mapping.findForward("error");
        }

        request.setAttribute("out", out);

        request.setAttribute("outId", outBalanceId);

        request.setAttribute("lastMoney", out.getTotal() - out.getPayMoney());

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("InBillBean.ownerId", "=", user.getStafferId());

        condtion.addCondition("InBillBean.customerId", "=", customerId);

        condtion.addIntCondition("InBillBean.status", "=", FinanceConstant.INBILL_STATUS_NOREF);

        condtion.addCondition("order by InBillBean.logTime desc");

        List<InBillVO> billList = inBillDAO.queryEntityVOsByCondition(condtion);

        request.setAttribute("billList", billList);

        return mapping.findForward("refBill");
    }

    /**
     * 导入
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward uploadPayment(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        if ( !userManager.containAuth(user, AuthConstant.PAYMENT_OPR))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

            return mapping.findForward("uploadPayment");
        }

        RequestDataStream rds = new RequestDataStream(request);

        try
        {
            rds.parser();
        }
        catch (Exception e1)
        {
            _logger.error(e1, e1);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "解析失败");

            return mapping.findForward("uploadPayment");
        }

        int success = 0;

        int fault = 0;

        StringBuilder builder = new StringBuilder();

        String bankId = rds.getParameter("bankId");

        if (rds.haveStream())
        {
            try
            {
                ReaderFile reader = ReadeFileFactory.getXLSReader();

                reader.readFile(rds.getUniqueInputStream());

                while (reader.hasNext())
                {
                    String[] obj = (String[])reader.next();

                    // 第一行忽略
                    if (reader.getCurrentLineNumber() == 1)
                    {
                        continue;
                    }

                    // 序号 回款来源 回款金额 回款日期 备注
                    int currentNumber = reader.getCurrentLineNumber();

                    boolean addSucess = false;

                    if (obj.length >= 5)
                    {
                        try
                        {
                            addSucess = createPayment(user, bankId, obj);
                        }
                        catch (MYException e)
                        {
                            builder.append("第[" + currentNumber + "]错误:").append(
                                e.getErrorContent()).append("<br>");
                        }
                    }
                    else
                    {
                        builder
                            .append("第[" + currentNumber + "]错误:")
                            .append("数据长度不足4格,备注可以为空")
                            .append("<br>");
                    }

                    if (addSucess)
                    {
                        success++ ;
                    }
                    else
                    {
                        fault++ ;
                    }
                }
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "导入失败");

                return mapping.findForward("uploadPayment");
            }
        }

        rds.close();

        StringBuilder result = new StringBuilder();

        result.append("导入成功:").append(success).append("条,失败:").append(fault).append("条<br>");

        result.append(builder.toString());

        request.setAttribute(KeyConstant.MESSAGE, result.toString());

        return mapping.findForward("uploadPayment");
    }

    private boolean createPayment(User user, String bankId, String[] obj)
        throws MYException
    {
        PaymentBean bean = new PaymentBean();

        if (StringTools.isNullOrNone(obj[1]))
        {
            throw new MYException("缺少类型");
        }

        if (StringTools.isNullOrNone(obj[3]))
        {
            throw new MYException("缺少回款金额");
        }

        if ("对私".equals(obj[1]))
        {
            bean.setType(FinanceConstant.PAYMENT_PAY_SELF);
        }
        else
        {
            bean.setType(FinanceConstant.PAYMENT_PAY_PUBLIC);
        }

        bean.setBankId(bankId);
        bean.setFromer(obj[2]);
        bean.setMoney(MathTools.parseDouble(obj[3]));
        bean.setReceiveTime(obj[4]);

        if (obj.length == 6)
        {
            bean.setDescription(obj[5]);
        }

        return financeFacade.addPaymentBean(user.getId(), bean);
    }

    /**
     * findPayment
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findPayment(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");
        String mode = request.getParameter("mode");

        PaymentVO bean = paymentDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("querySelfPayment");
        }

        List<PaymentApplyBean> queryEntityBeansByFK = paymentApplyDAO.queryEntityBeansByFK(id);

        for (PaymentApplyBean paymentApplyBean : queryEntityBeansByFK)
        {
            if (paymentApplyBean.getStatus() == FinanceConstant.PAYAPPLY_STATUS_INIT)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "有付款申请没有处理结束,请重新操作");

                return mapping.findForward("querySelfPayment");
            }
        }

        double hasUsed = inBillDAO.sumByPaymentId(id);

        request.setAttribute("bean", bean);

        request.setAttribute("hasUsed", hasUsed);

        if ("1".equals(mode))
        {
            return mapping.findForward("drawPayment2");
        }

        return mapping.findForward("drawPayment");
    }

    /**
     * findPaymentApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findPaymentApply(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        PaymentApplyVO bean = paymentApplyDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("error");
        }

        request.setAttribute("bean", bean);

        List<FlowLogBean> loglist = flowLogDAO.queryEntityBeansByFK(id);

        request.setAttribute("loglist", loglist);

        List<PaymentVSOutBean> vsList = paymentVSOutDAO.queryEntityBeansByFK(id);

        for (PaymentVSOutBean paymentVSOutBean : vsList)
        {
            if (StringTools.isNullOrNone(paymentVSOutBean.getOutId()))
            {
                paymentVSOutBean.setOutId("客户预收");
            }
        }

        request.setAttribute("vsList", vsList);

        if ("1".equals(update))
        {
            return mapping.findForward("handlePaymentApply");
        }

        return mapping.findForward("detailPaymentApply");
    }

    /**
     * passPaymentApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passPaymentApply(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        try
        {
            User user = Helper.getUser(request);

            financeFacade.passPaymentApply(user.getId(), id, reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryPaymentApply");
    }

    /**
     * rejectPaymentApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward rejectPaymentApply(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        try
        {
            User user = Helper.getUser(request);

            financeFacade.rejectPaymentApply(user.getId(), id, reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryPaymentApply");
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
     * @return the paymentApplyDAO
     */
    public PaymentApplyDAO getPaymentApplyDAO()
    {
        return paymentApplyDAO;
    }

    /**
     * @param paymentApplyDAO
     *            the paymentApplyDAO to set
     */
    public void setPaymentApplyDAO(PaymentApplyDAO paymentApplyDAO)
    {
        this.paymentApplyDAO = paymentApplyDAO;
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
     * @return the paymentVSOutDAO
     */
    public PaymentVSOutDAO getPaymentVSOutDAO()
    {
        return paymentVSOutDAO;
    }

    /**
     * @param paymentVSOutDAO
     *            the paymentVSOutDAO to set
     */
    public void setPaymentVSOutDAO(PaymentVSOutDAO paymentVSOutDAO)
    {
        this.paymentVSOutDAO = paymentVSOutDAO;
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
     * @return the statBankManager
     */
    public StatBankManager getStatBankManager()
    {
        return statBankManager;
    }

    /**
     * @param statBankManager
     *            the statBankManager to set
     */
    public void setStatBankManager(StatBankManager statBankManager)
    {
        this.statBankManager = statBankManager;
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
