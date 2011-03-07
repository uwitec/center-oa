/*
 * File Name: ConsignAction.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-14
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.ConsignBean;
import com.china.center.oa.sail.bean.TransportBean;
import com.china.center.oa.sail.constanst.SailConstant;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.ConsignDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.manager.ConsignManager;
import com.china.center.oa.sail.vo.OutVO;
import com.china.center.osgi.jsp.ElTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.DecSecurity;
import com.china.center.tools.RequestTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * 运输方式和发货单
 * 
 * @author ZHUZHU
 * @version 2008-1-14
 * @see
 * @since
 */
public class ConsignAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ConsignDAO consignDAO = null;

    private ConsignManager consignManager = null;

    private CommonDAO commonDAO = null;

    private CustomerDAO customerDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private OutDAO outDAO = null;

    private BaseDAO baseDAO = null;

    private OutAction outAction = null;

    private DepotDAO depotDAO = null;

    /**
     * default constructor
     */
    public ConsignAction()
    {
    }

    public ActionForward queryTransport(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<TransportBean> list = consignDAO.queryTransportByType(SailConstant.TRANSPORT_COMMON);

        TransportBean item = null;

        for (TransportBean transportBean : list)
        {
            item = consignDAO.findTransportById(transportBean.getParent());

            if (item != null)
            {
                transportBean.setParent(item.getName());
            }
        }

        request.getSession().setAttribute("transportList", list);

        return mapping.findForward("transportList");
    }

    public ActionForward preForAddTransport(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<TransportBean> list = consignDAO.queryTransportByType(SailConstant.TRANSPORT_TYPE);

        request.getSession().setAttribute("transportList", list);

        return mapping.findForward("addTransport");
    }

    public ActionForward addTransport(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        TransportBean bean = new TransportBean();

        BeanUtil.getBean(bean, request);

        try
        {
            if (consignDAO.countByName(bean.getName(), bean.getType()) > 0)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "名称重复，请重新增加");

                return queryTransport(mapping, form, request, reponse);
            }

            consignManager.addTransport(bean);
        }
        catch (MYException e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return queryTransport(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功增加运输方式:" + bean.getName());

        return queryTransport(mapping, form, request, reponse);
    }

    /**
     * updateTransport
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateTransport(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        TransportBean bean = new TransportBean();

        BeanUtil.getBean(bean, request);

        try
        {
            consignManager.updateTransport(bean);
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return queryTransport(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功修改运输方式:" + bean.getName());

        return queryTransport(mapping, form, request, reponse);
    }

    /**
     * 查询发货单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryConsign(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condition = new ConditionParse();

        setCondition(request, condition);

        List<ConsignBean> list = consignDAO.queryConsignByCondition(condition);

        request.setAttribute("consignList", list);

        return mapping.findForward("queryConsign");
    }

    /**
     * 查询今天到货(到客户那)发货单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryTodayConsign(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        ConditionParse condition = new ConditionParse();

        condition.addIntCondition("currentStatus", "=", SailConstant.CONSIGN_PASS);

        condition.addIntCondition("reprotType", "=", SailConstant.CONSIGN_REPORT_INIT);

        condition.addCondition("t1.arriveDate", ">=", TimeTools.now_short());

        List<ConsignBean> list = consignDAO.queryConsignByCondition(condition);

        request.setAttribute("consignList", list);

        return mapping.findForward("queryTodayConsign");
    }

    /**
     * setCondition
     * 
     * @param request
     * @param condition
     */
    private void setCondition(HttpServletRequest request, ConditionParse condition)
    {
        String init = request.getParameter("load");

        if (StringTools.isNullOrNone(init))
        {
            init = (String)request.getAttribute("load");
        }

        // 从菜单的入口
        if ("1".equals(init))
        {
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 7);

            String now = TimeTools.getStringByFormat(new Date(), "yyyy-MM-dd");

            String old = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyy-MM-dd");

            condition.addCondition("outTime", ">=", old);

            request.setAttribute("beginDate", old);

            condition.addCondition("outTime", "<=", now);

            request.setAttribute("endDate", now);
        }
        else
        {
            String beginDate = request.getParameter("beginDate");

            if ( !StringTools.isNullOrNone(beginDate))
            {
                condition.addCondition("outTime", ">=", beginDate);
            }

            String endDate = request.getParameter("endDate");

            if ( !StringTools.isNullOrNone(endDate))
            {
                condition.addCondition("outTime", "<=", endDate);
            }

            String abeginDate = request.getParameter("abeginDate");

            if ( !StringTools.isNullOrNone(abeginDate))
            {
                condition.addCondition("arriveDate", ">=", abeginDate);
            }

            String aendDate = request.getParameter("aendDate");

            if ( !StringTools.isNullOrNone(aendDate))
            {
                condition.addCondition("arriveDate", "<=", aendDate);
            }
        }

        String currentStatus = request.getParameter("currentStatus");

        if ( !StringTools.isNullOrNone(currentStatus))
        {
            condition.addIntCondition("currentStatus", "=", currentStatus);
        }

        String fullId = request.getParameter("fullId");

        if ( !StringTools.isNullOrNone(fullId))
        {
            condition.addCondition("t2.fullId", "like", fullId);
        }

        String reprotType = request.getParameter("reprotType");

        if ( !StringTools.isNullOrNone(reprotType))
        {
            condition.addIntCondition("reprotType", "=", reprotType);
        }

        condition.addCondition("order by t2.arriveDate desc");

        request.getSession().setAttribute("g_queryConsign_condition", condition);
    }

    /**
     * findConsign
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findConsign(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String outId = request.getParameter("fullId");

        String gid = request.getParameter("gid");

        String forward = request.getParameter("forward");

        CommonTools.saveParamers(request);

        OutVO vo = null;
        try
        {
            vo = outDAO.findVO(outId);

            if (vo == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在");

                return mapping.findForward("error");
            }

            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outId);

            vo.setBaseList(list);

            // 获取客户的联系方式
            CustomerBean customer = customerDAO.find(vo.getCustomerId());

            if (customer != null)
            {
                vo.setCustomerAddress(DecSecurity.decrypt(customer.getAddress()));
            }

            if ( !StringTools.isNullOrNone(vo.getDepotpartId()))
            {
                DepotpartBean db = depotpartDAO.find(vo.getDepotpartId());

                if (db != null)
                {
                    vo.setDepotpartName(db.getName());
                }
            }

            request.setAttribute("out", vo);

            request.setAttribute("baseList", list);

            ConsignBean cb = null;

            if ( !StringTools.isNullOrNone(gid))
            {
                cb = consignDAO.findById(gid);

                if (cb == null)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

                    return mapping.findForward("error");
                }
            }
            else
            {
                cb = consignDAO.findDefaultConsignByFullId(outId);

                if (cb == null)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

                    return mapping.findForward("error");
                }
            }

            if (cb.getCurrentStatus() == SailConstant.CONSIGN_PASS)
            {
                request.setAttribute("init", false);
            }
            else
            {
                request.setAttribute("init", true);
            }

            request.setAttribute("consignBean", cb);

            List<TransportBean> ts = consignDAO.queryTransportByType(SailConstant.TRANSPORT_TYPE);

            TransportBean tss = consignDAO.findTransportById(cb.getTransport());

            TransportBean tss1 = null;

            if (tss != null)
            {
                tss1 = consignDAO.findTransportById(tss.getParent());
            }

            Map<String, String> map = new HashMap<String, String>();

            for (TransportBean transportBean : ts)
            {
                List<TransportBean> inner = consignDAO.queryTransportByParentId(transportBean
                    .getId());

                String temp = "";
                for (TransportBean transportBean2 : inner)
                {
                    temp += transportBean2.getName() + "~" + transportBean2.getId() + "#";
                }

                map.put(transportBean.getId(), temp);
            }

            request.setAttribute("map", map);
            request.setAttribute("transportList", ts);
            request.setAttribute("tss", tss);
            request.setAttribute("tss1", tss1);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");

            _logger.error("addOut", e);

            return mapping.findForward("error");
        }

        if ("1".equals(forward))
        {
            request.setAttribute("year", TimeTools.now("yyyy"));
            request.setAttribute("month", TimeTools.now("MM"));
            request.setAttribute("day", TimeTools.now("dd"));
            request.setAttribute("exporData", TimeTools.now_short());
            return mapping.findForward("print1");
        }

        if ("2".equals(forward))
        {
            request.setAttribute("readonly", true);
        }
        else if ("3".equals(forward))
        {
            request.setAttribute("readonly", true);

            List<ConsignBean> beanList = consignDAO.queryConsignByFullId(outId);

            request.removeAttribute("consignBean");

            request.setAttribute("beanList", beanList);

            return mapping.findForward("detailAllConsign");
        }
        else
        {
            request.setAttribute("readonly", false);
        }

        return mapping.findForward("detailOut5");
    }

    /**
     * 通过发货单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward passConsign(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        ConsignBean bean = new ConsignBean();

        BeanUtil.getBean(bean, request);

        bean.setCurrentStatus(SailConstant.CONSIGN_PASS);

        ConsignBean cc = consignDAO.findDefaultConsignByFullId(bean.getFullId());

        if (cc == null || cc.getCurrentStatus() == SailConstant.CONSIGN_PASS)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return mapping.findForward("error");
        }

        try
        {
            consignManager.updateConsign(bean);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return queryTransport(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功通过发货单");

        request.setAttribute("queryType", "3");

        RequestTools.menuInitQuery(request);

        return outAction.queryOut(mapping, form, request, reponse);
    }

    /**
     * findTransport
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findTransport(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        TransportBean transport = consignDAO.findTransport(id);

        List<TransportBean> ts = consignDAO.queryTransportByType(SailConstant.TRANSPORT_TYPE);

        request.setAttribute("transportList", ts);

        request.setAttribute("bean", transport);

        return mapping.findForward("updateTransport");
    }

    /**
     * 通过发货单(修改发货信息)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward reportConsign(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        ConsignBean bean = new ConsignBean();

        BeanUtil.getBean(bean, request);

        ConsignBean cc = consignDAO.findDefaultConsignByFullId(bean.getFullId());

        if (cc == null || cc.getReprotType() != SailConstant.CONSIGN_REPORT_INIT)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return mapping.findForward("error");
        }

        try
        {
            consignManager.updateConsign(bean);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功确认发货单收货");

        CommonTools.removeParamers(request);

        request.setAttribute("load", "1");

        return queryConsign(mapping, form, request, reponse);
    }

    public ActionForward readdConsign(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String outId = request.getParameter("fullId");

        String arriveDate = request.getParameter("arriveDate");

        ConsignBean bean = new ConsignBean();

        bean.setCurrentStatus(SailConstant.CONSIGN_INIT);

        bean.setCurrentStatus(SailConstant.CONSIGN_PASS);

        bean.setGid(commonDAO.getSquenceString20());

        bean.setFullId(outId);

        bean.setArriveDate(arriveDate);

        try
        {
            consignManager.addConsign(bean);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功新增发货单:" + bean.getGid());

        CommonTools.removeParamers(request);

        request.setAttribute("load", "1");

        return queryConsign(mapping, form, request, reponse);
    }

    /**
     * 删除运输方式
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delTransport(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String transportId = request.getParameter("id");

        try
        {
            if (consignDAO.countTransport(transportId) > 0)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "运输方式已经被使用，不能删除");

                return queryTransport(mapping, form, request, reponse);
            }

            consignManager.delTransport(transportId);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return queryTransport(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功删除运输方式");

        return queryTransport(mapping, form, request, reponse);
    }

    /**
     * exportConsign
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportConsign(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        ConditionParse condition = (ConditionParse)request.getSession().getAttribute(
            "g_queryConsign_condition");

        List<ConsignBean> beanList = consignDAO.queryConsignByCondition(condition);

        OutputStream out = null;

        String filenName = null;

        filenName = "Consign_" + TimeTools.now("MMddHHmmss") + ".xls";

        if (beanList.size() == 0)
        {
            return null;
        }

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;

        WritableSheet ws = null;

        try
        {
            out = reponse.getOutputStream();

            // create a excel
            wwb = Workbook.createWorkbook(out);

            ws = wwb.createSheet("CONSIGN", 0);

            int i = 0, j = 0;

            ConsignBean element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableCellFormat format = new WritableCellFormat(font);

            ws.addCell(new Label(j++ , i, "销售单号", format));
            ws.addCell(new Label(j++ , i, "运输方式", format));
            ws.addCell(new Label(j++ , i, "发货单号", format));
            ws.addCell(new Label(j++ , i, "检验人", format));
            ws.addCell(new Label(j++ , i, "打包人", format));
            ws.addCell(new Label(j++ , i, "打包时间", format));
            ws.addCell(new Label(j++ , i, "包装件数", format));
            ws.addCell(new Label(j++ , i, "包装重量", format));
            ws.addCell(new Label(j++ , i, "回访时间", format));
            ws.addCell(new Label(j++ , i, "到货时间", format));
            ws.addCell(new Label(j++ , i, "备货人", format));
            ws.addCell(new Label(j++ , i, "监控设备", format));
            ws.addCell(new Label(j++ , i, "运费", format));
            ws.addCell(new Label(j++ , i, "是否满意服务", format));
            ws.addCell(new Label(j++ , i, "收货类型", format));
            ws.addCell(new Label(j++ , i, "备注", format));

            for (Iterator iter = beanList.iterator(); iter.hasNext();)
            {
                element = (ConsignBean)iter.next();

                j = 0;
                i++ ;

                String tname = "";

                TransportBean transportBean = consignDAO.findTransport(element.getTransport());

                if (transportBean != null)
                {
                    tname = transportBean.getName();
                }

                ws.addCell(new Label(j++ , i, element.getFullId()));
                ws.addCell(new Label(j++ , i, tname));
                ws.addCell(new Label(j++ , i, element.getTransportNo()));
                ws.addCell(new Label(j++ , i, element.getChecker()));
                ws.addCell(new Label(j++ , i, element.getPackager()));
                ws.addCell(new Label(j++ , i, element.getPackageTime()));
                ws.addCell(new Label(j++ , i, element.getPackageAmount()));
                ws.addCell(new Label(j++ , i, element.getPackageWeight()));
                ws.addCell(new Label(j++ , i, element.getVisitTime()));
                ws.addCell(new Label(j++ , i, element.getArriveTime()));
                ws.addCell(new Label(j++ , i, element.getPreparer()));
                ws.addCell(new Label(j++ , i, element.getMathine()));
                ws.addCell(new Label(j++ , i, element.getTransportFee()));
                ws.addCell(new Label(j++ , i, ElTools.get("consignPromitType", element
                    .getPromitType())));
                ws.addCell(new Label(j++ , i, ElTools.get("consignReprotType", element
                    .getReprotType())));
                ws.addCell(new Label(j++ , i, element.getApplys()));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        finally
        {
            if (wwb != null)
            {
                try
                {
                    wwb.write();
                    wwb.close();
                }
                catch (Exception e1)
                {
                }
            }
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
        }

        return null;
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

    public OutAction getOutAction()
    {
        return outAction;
    }

    public void setOutAction(OutAction outAction)
    {
        this.outAction = outAction;
    }

    /**
     * @return the consignManager
     */
    public ConsignManager getConsignManager()
    {
        return consignManager;
    }

    /**
     * @param consignManager
     *            the consignManager to set
     */
    public void setConsignManager(ConsignManager consignManager)
    {
        this.consignManager = consignManager;
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
}
