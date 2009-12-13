/*
 * File Name: ConsignAction.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2008-1-14
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.action;


import java.util.Calendar;
import java.util.Date;
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

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.jdbc.inter.PublicSQL;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.DecSecurity;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.ConsignBean;
import com.china.centet.yongyin.bean.DepotpartBean;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.TransportBean;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.ConsignDAO;
import com.china.centet.yongyin.dao.CustomerDAO;
import com.china.centet.yongyin.dao.DepotpartDAO;
import com.china.centet.yongyin.dao.OutDAO;
import com.china.centet.yongyin.manager.LocationManager;
import com.china.centet.yongyin.vo.OutBeanVO;


/**
 * 运输方式和发货单
 * 
 * @author zhuzhu
 * @version 2008-1-14
 * @see
 * @since
 */
public class ConsignAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ConsignDAO consignDAO = null;

    private PublicSQL publicSQL = null;

    private CommonDAO commonDAO = null;

    private CustomerDAO customerDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private OutDAO outDAO = null;

    private OutAction outAction = null;

    private LocationManager locationManager = null;

    /**
     * default constructor
     */
    public ConsignAction()
    {}

    public ActionForward queryTransport(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<TransportBean> list = consignDAO.queryTransportByType(Constant.TRANSPORT_COMMON);

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
        List<TransportBean> list = consignDAO.queryTransportByType(Constant.TRANSPORT_TYPE);

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

            consignDAO.addTransport(bean);
        }
        catch (Exception e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return queryTransport(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功增加运输方式:" + bean.getName());

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

    private void setCondition(HttpServletRequest request, ConditionParse condition)
    {
        String init = request.getParameter("init");

        if (StringTools.isNullOrNone(init))
        {
            init = (String)request.getAttribute("init");
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

        String forward = request.getParameter("forward");

        CommonTools.saveParamers(request);

        OutBean bean = null;
        try
        {
            bean = outDAO.findOutById(outId);

            if (bean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在");

                return mapping.findForward("error");
            }

            List<BaseBean> list = outDAO.queryBaseByOutFullId(outId);

            bean.setBaseList(list);

            OutBeanVO vo = new OutBeanVO();

            BeanUtil.copyProperties(vo, bean);

            // 获取客户的联系方式
            CustomerBean customer = customerDAO.findCustomerById(bean.getCustomerId());

            if (customer != null)
            {
                vo.setCustomerAddress(DecSecurity.decrypt(customer.getAddress()));
            }

            LocationBean plo = locationManager.findLocationById(vo.getLocation());

            if (plo != null)
            {
                vo.setLocationName(plo.getLocationName());
            }

            if ( !StringTools.isNullOrNone(vo.getDepotpartId()))
            {
                DepotpartBean db = depotpartDAO.findDepotpartById(vo.getDepotpartId());

                if (db != null)
                {
                    vo.setDepotpartName(db.getName());
                }
            }

            request.setAttribute("out", vo);

            request.setAttribute("baseList", list);

            ConsignBean cb = consignDAO.findConsignById(outId);

            if (cb == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

                return mapping.findForward("error");
            }

            if (cb.getCurrentStatus() == Constant.CONSIGN_PASS)
            {
                request.setAttribute("init", false);
            }
            else
            {
                request.setAttribute("init", true);
            }

            request.setAttribute("consignBean", cb);

            List<TransportBean> ts = consignDAO.queryTransportByType(Constant.TRANSPORT_TYPE);

            TransportBean tss = consignDAO.findTransportById(cb.getTransport());

            TransportBean tss1 = null;
            if (tss != null)
            {
                tss1 = consignDAO.findTransportById(tss.getParent());
            }

            Map<String, String> map = new HashMap<String, String>();
            for (TransportBean transportBean : ts)
            {
                List<TransportBean> inner = consignDAO.queryTransportByParentId(transportBean.getId());
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

        bean.setCurrentStatus(Constant.CONSIGN_PASS);

        ConsignBean cc = consignDAO.findConsignById(bean.getFullId());

        if (cc == null || cc.getCurrentStatus() == Constant.CONSIGN_PASS)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return mapping.findForward("error");
        }

        try
        {
            consignDAO.updateConsign(bean);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return queryTransport(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功通过发货单");

        request.setAttribute("forward", "11");

        return outAction.queryOut2(mapping, form, request, reponse);
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
    public ActionForward reportConsign(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        ConsignBean bean = new ConsignBean();

        BeanUtil.getBean(bean, request);

        ConsignBean cc = consignDAO.findConsignById(bean.getFullId());

        if (cc == null || cc.getReprotType() != Constant.CONSIGN_REPORT_INIT)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return mapping.findForward("error");
        }

        cc.setReprotType(bean.getReprotType());

        try
        {
            consignDAO.updateConsign(cc);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return queryTransport(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功确认发货单收货");

        CommonTools.removeParamers(request);

        request.setAttribute("init", "1");

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

            consignDAO.delTransport(transportId);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return queryTransport(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功删除运输方式");

        return queryTransport(mapping, form, request, reponse);
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
     * @return the publicSQL
     */
    public PublicSQL getPublicSQL()
    {
        return publicSQL;
    }

    /**
     * @param publicSQL
     *            the publicSQL to set
     */
    public void setPublicSQL(PublicSQL publicSQL)
    {
        this.publicSQL = publicSQL;
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
     * @return the locationManager
     */
    public LocationManager getLocationManager()
    {
        return locationManager;
    }

    /**
     * @param locationManager
     *            the locationManager to set
     */
    public void setLocationManager(LocationManager locationManager)
    {
        this.locationManager = locationManager;
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
}
