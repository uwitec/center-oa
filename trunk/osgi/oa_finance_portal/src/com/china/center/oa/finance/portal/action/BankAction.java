/**
 * File Name: LocationAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.portal.action;


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
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.PaymentBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.finance.dao.PaymentDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.vo.BankVO;
import com.china.center.oa.finance.vo.PaymentVO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.manager.UserManager;
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
 * @see BankAction
 * @since 1.0
 */
public class BankAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private FinanceFacade financeFacade = null;

    private BankDAO bankDAO = null;

    private DutyDAO dutyDAO = null;

    private UserManager userManager = null;

    private PaymentDAO paymentDAO = null;

    private static final String QUERYBANK = "queryBank";

    private static final String RPTQUERYBANK = "rptQueryBank";

    private static final String QUERYPAYMENT = "queryPayment";

    private static final String QUERYSELFPAYMENT = "querySelfPayment";

    /**
     * default constructor
     */
    public BankAction()
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

    public ActionForward queryBank(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYBANK, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYBANK, request, condtion,
            this.bankDAO);

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

        condtion.addCondition("order by PaymentBean.logTime desc");

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

        condtion.addCondition("order by PaymentBean.logTime desc");

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
     * 领取回款
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

        PaymentVO bean = paymentDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("querySelfPayment");
        }

        request.setAttribute("bean", bean);

        return mapping.findForward("drawPayment");
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

}
