/**
 *
 */
package com.china.centet.yongyin.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.OldPageSeparateTools;
import com.china.center.eltools.ElTools;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.FlowLogBean;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.PriceAskBean;
import com.china.centet.yongyin.bean.PriceAskProviderBean;
import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.StockBean;
import com.china.centet.yongyin.bean.StockItemBean;
import com.china.centet.yongyin.bean.StockPayBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.FlowLogHelper;
import com.china.centet.yongyin.bean.helper.LocationHelper;
import com.china.centet.yongyin.bean.helper.PriceAskHelper;
import com.china.centet.yongyin.bean.helper.StockHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.PriceConstant;
import com.china.centet.yongyin.constant.StockConstant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.FlowLogDAO;
import com.china.centet.yongyin.dao.LocationDAO;
import com.china.centet.yongyin.dao.PriceAskDAO;
import com.china.centet.yongyin.dao.PriceAskProviderDAO;
import com.china.centet.yongyin.dao.PriceDAO;
import com.china.centet.yongyin.dao.ProductAmountDAO;
import com.china.centet.yongyin.dao.ProductDAO;
import com.china.centet.yongyin.dao.StockDAO;
import com.china.centet.yongyin.dao.StockItemDAO;
import com.china.centet.yongyin.dao.StockPayDAO;
import com.china.centet.yongyin.dao.StockPayItemDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.manager.PriceManager;
import com.china.centet.yongyin.manager.StockManager;
import com.china.centet.yongyin.vo.FlowLogBeanVO;
import com.china.centet.yongyin.vo.PriceAskProviderBeanVO;
import com.china.centet.yongyin.vo.StockBeanVO;
import com.china.centet.yongyin.vo.StockItemBeanVO;
import com.china.centet.yongyin.vo.StockPayBeanVO;
import com.china.centet.yongyin.vo.StockPayItemBeanVO;


/**
 * 采购的的action
 * 
 * @author Administrator
 */
public class StockAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private PriceManager priceManager = null;

    private PriceDAO priceDAO = null;

    private StockManager stockManager = null;

    private StockItemDAO stockItemDAO = null;

    private StockDAO stockDAO = null;

    private ProductDAO productDAO = null;

    private LocationDAO locationDAO = null;

    private ProductAmountDAO productAmountDAO = null;

    private StockPayItemDAO stockPayItemDAO = null;

    private StockPayDAO stockPayDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private UserDAO userDAO = null;

    private CommonDAO commonDAO = null;

    private PriceAskProviderDAO priceAskProviderDAO = null;

    private PriceAskDAO priceAskDAO = null;

    /**
     *
     */
    public StockAction()
    {}

    /**
     * 增加采购单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addStock(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        StockBean bean = new StockBean();

        String oprMode = request.getParameter("oprMode");

        try
        {
            BeanUtil.getBean(bean, request);

            setStockBean(bean, request);

            User user = Helper.getUser(request);

            bean.setUserId(user.getId());

            bean.setLocationId(user.getLocationID());

            bean.setLogTime(TimeTools.now());

            stockManager.addStockBean(user, bean);

            if ("1".equals(oprMode))
            {
                stockManager.passStock(user, bean.getId());
            }

            request.setAttribute(KeyConstant.MESSAGE, "成功增加采购单:" + bean.getId());
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加采购单失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * addStockPay
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addStockPay(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        StockPayBean bean = new StockPayBean();

        String[] ids = request.getParameterValues("ids");

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            stockManager.addStockPayBean(user, ListTools.changeArrayToList(ids));

            request.setAttribute(KeyConstant.MESSAGE, "成功增加汇总单");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加汇总单失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryStockPay(mapping, form, request, reponse);
    }

    /**
     * 修改采购单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateStock(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        StockBean bean = new StockBean();

        try
        {
            BeanUtil.getBean(bean, request);

            setStockBean(bean, request);

            User user = Helper.getUser(request);

            bean.setUserId(user.getId());

            bean.setLocationId(user.getLocationID());

            bean.setLogTime(TimeTools.now());

            stockManager.updateStockBean(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功修改采购单:" + bean.getId());
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改采购单失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        request.setAttribute("auto", "1");

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * 修改采购单的状态
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateStockStatus(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        String pass = request.getParameter("pass");

        String reject = request.getParameter("reject");

        try
        {
            User user = Helper.getUser(request);

            if ( !StringTools.isNullOrNone(pass))
            {
                stockManager.passStock(user, id);

                request.setAttribute(KeyConstant.MESSAGE, "成功修改采购单状态:" + id);
            }
            else
            {
                if ("1".equals(reject))
                {
                    stockManager.rejectStock(user, id, reason);
                }

                request.setAttribute(KeyConstant.MESSAGE, "成功修改采购单状态:" + id);

                if ("2".equals(reject))
                {
                    stockManager.rejectStockToAsk(user, id, reason);

                    request.setAttribute(KeyConstant.MESSAGE, "成功驳回采购单到询价员:" + id);
                }
            }
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改采购单状态失败:" + e.getMessage());
        }

        CommonTools.saveParamers(request);

        request.setAttribute("forward", "1");

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * 修改采购单的状态
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payStock(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String payStatus = request.getParameter("payStatus");

        try
        {
            User user = Helper.getUser(request);

            stockManager.updateStockPayStatus(user, id, CommonTools.parseInt(payStatus));

            request.setAttribute(KeyConstant.MESSAGE, "成功申请采购单付款状态:" + id);
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "申请采购单付款状态失败:" + e.getMessage());
        }

        CommonTools.saveParamers(request);

        request.setAttribute("forward", "1");

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * 结束采购单，并自动生成入库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward endStock(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        try
        {
            User user = Helper.getUser(request);

            String fullId = stockManager.endStock(user, id);

            request.setAttribute(KeyConstant.MESSAGE, "成功结束采购单,并自动生成了入库单:" + fullId);
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "结束采购单失败:" + e.getMessage());
        }

        CommonTools.saveParamers(request);

        request.setAttribute("forward", "1");

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * 修改采购单的状态
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward stockItemAskChange(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String stockId = request.getParameter("stockId");

        String providerId = request.getParameter("providerId");

        try
        {
            stockManager.stockItemAskChange(id, providerId);

            request.setAttribute(KeyConstant.MESSAGE, "成功修改产品的供应商");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改采购单状态失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        request.setAttribute("stockId", stockId);

        request.setAttribute("stockAskChange", "1");

        return findStock(mapping, form, request, reponse);
    }

    /**
     * 采购item变成调出
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward stockItemChangeToOut(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String tranNo = request.getParameter("tranNo");

        try
        {
            User user = Helper.getUser(request);

            stockManager.stockItemChangeToOut(user, id, tranNo);

            request.setAttribute(KeyConstant.MESSAGE, "采购单成功调出");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改采购单状态失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * 收集数据
     * 
     * @param pbean
     * @param item
     * @param request
     */
    private void setStockBean(StockBean pbean, HttpServletRequest request)
    {
        String[] indexs = request.getParameterValues("check_init");

        List<StockItemBean> item = new ArrayList<StockItemBean>();

        for (int i = 0; i < indexs.length; i++ )
        {
            if ( !StringTools.isNullOrNone(indexs[i]))
            {
                StockItemBean bean = new StockItemBean();

                bean.setProductId(request.getParameter("productId_" + indexs[i]));

                bean.setPriceAskProviderId(request.getParameter("netaskId_" + indexs[i]));

                bean.setLogTime(TimeTools.now());

                bean.setPrePrice(Float.parseFloat(request.getParameter("price_" + indexs[i])));

                bean.setAmount(CommonTools.parseInt(request.getParameter("amount_" + indexs[i])));

                int num = productAmountDAO.countProductAmountByProductId(bean.getProductId());

                bean.setProductNum(num);

                item.add(bean);
            }
        }

        pbean.setItem(item);
    }

    /**
     * rptInQueryPriceAskProvider
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptInQueryPriceAskProvider(ActionMapping mapping, ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        User user = Helper.getUser(request);

        List<PriceAskProviderBeanVO> beanList = priceAskProviderDAO.queryByCondition(user.getId(),
            TimeTools.now("yyyyMMdd"));

        // 获取PID
        for (PriceAskProviderBeanVO vo : beanList)
        {
            PriceAskBean ask = priceAskDAO.find(vo.getAskId());

            if (ask == null || StringTools.isNullOrNone(ask.getParentAsk()))
            {
                continue;
            }

            PriceAskProviderBean pp = priceAskProviderDAO.findBeanByAskIdAndProviderId(
                ask.getParentAsk(), vo.getProviderId());

            if (pp != null)
            {
                vo.setPid(pp.getId());
            }

            int sum = stockItemDAO.sumNetProductByPid(pp.getId());

            vo.setRemainmount(pp.getSupportAmount() - sum);
        }

        request.setAttribute("beanList", beanList);

        return mapping.findForward("rptPriceAskProviderList");
    }

    /**
     * 查询采购单据
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findStock(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        if (StringTools.isNullOrNone(id))
        {
            id = (String)request.getAttribute("stockId");
        }

        String out = request.getParameter("out");

        String update = request.getParameter("update");

        String stockAskChange = request.getParameter("stockAskChange");

        if (StringTools.isNullOrNone(stockAskChange))
        {
            stockAskChange = (String)request.getAttribute("stockAskChange");
        }

        String process = request.getParameter("process");

        if (StringTools.isNullOrNone(process))
        {
            process = (String)request.getAttribute("process");
        }

        StockBeanVO vo = null;
        try
        {
            vo = stockManager.findStockVO(id);

            request.setAttribute("bean", vo);
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询采购单失败:" + e.getMessage());

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        if ( !StringTools.isNullOrNone(update))
        {
            int last = 5 - vo.getItemVO().size();

            // 补齐
            for (int i = 0; i < last; i++ )
            {
                vo.getItemVO().add(new StockItemBeanVO());
            }

            request.setAttribute("maxItem", 5 - last);

            List<String> departementList = commonDAO.listAll("t_center_departement");

            request.setAttribute("departementList", departementList);

            return mapping.findForward("updateStock");
        }

        if ("1".equals(process))
        {
            return mapping.findForward("processStock");
        }

        // 获取审批日志
        List<FlowLogBean> logs = flowLogDAO.queryEntityBeansByFK(id);

        List<FlowLogBeanVO> logsVO = new ArrayList<FlowLogBeanVO>();

        for (FlowLogBean flowLogBean : logs)
        {
            logsVO.add(FlowLogHelper.getStockFlowLogVO(flowLogBean));
        }

        // 获得询价的列表
        Map<String, String> map = new HashMap<String, String>();
        Map<String, List<PriceAskProviderBeanVO>> map1 = new HashMap<String, List<PriceAskProviderBeanVO>>();

        for (StockItemBeanVO stockItemBeanVO : vo.getItemVO())
        {
            if (stockItemBeanVO.getStatus() > StockConstant.STOCK_ITEM_STATUS_INIT)
            {
                List<PriceAskProviderBeanVO> items = priceAskProviderDAO.queryEntityVOsByFK(stockItemBeanVO.getId());

                map1.put(stockItemBeanVO.getId(), items);

                User user = Helper.getUser(request);

                map.put(stockItemBeanVO.getId(), PriceAskHelper.createTable(items, user));
            }
        }

        request.setAttribute("map", map);
        request.setAttribute("map1", map1);

        request.setAttribute("logs", logsVO);

        if ("1".equals(stockAskChange))
        {
            return mapping.findForward("stockAskChange");
        }

        if ("1".equals(out))
        {
            request.setAttribute("out", 1);
        }

        return mapping.findForward("detailStock");
    }

    /**
     * 增加采购的准备
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddStock(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<String> departementList = commonDAO.listAll("t_center_departement");

        request.setAttribute("departementList", departementList);

        return mapping.findForward("addStock");
    }

    /**
     * 采购单据询价的准备
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForSockAsk(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String itemId = request.getParameter("itemId");

        StockItemBeanVO vo = stockItemDAO.findVO(itemId);

        request.setAttribute("bean", vo);

        if (vo == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "采购不存在");

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        Product product = productDAO.findProductById(vo.getProductId());

        if (product == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "产品不存在");

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        request.setAttribute("product", product);

        return mapping.findForward("stockAskPrice");
    }

    /**
     * 处理询价(把界面上询价的结果保存到数据库)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward stockItemAskPrice(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String stockId = request.getParameter("stockId");

        List<PriceAskProviderBean> item = new ArrayList<PriceAskProviderBean>();

        StockItemBean bean = stockItemDAO.find(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "采购不存在");

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        setPriceAskProviderBeans(bean, item, request);

        double min = Integer.MAX_VALUE;

        String pid = "";

        for (int i = item.size() - 1; i >= 0; i-- )
        {
            PriceAskProviderBean priceAskProviderBean = item.get(i);

            if (priceAskProviderBean.getHasAmount() == PriceConstant.HASAMOUNT_OK)
            {
                if (priceAskProviderBean.getPrice() <= min)
                {
                    min = priceAskProviderBean.getPrice();

                    pid = priceAskProviderBean.getProviderId();
                }
            }
        }

        if (min == Integer.MAX_VALUE)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "采购询价失败");

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        bean.setPrice(min);

        bean.setProviderId(pid);

        try
        {
            stockManager.stockItemAsk(bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功处理采购询价");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理采购询价失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("process", "1");

        request.setAttribute("stockId", stockId);

        return findStock(mapping, form, request, reponse);
    }

    /**
     * 收集数据
     * 
     * @param pbean
     * @param item
     * @param request
     */
    private void setPriceAskProviderBeans(StockItemBean pbean, List<PriceAskProviderBean> item,
                                          HttpServletRequest request)
    {
        String[] providers = request.getParameterValues("check_init");

        for (int i = 0; i < providers.length; i++ )
        {
            if ( !StringTools.isNullOrNone(providers[i]))
            {
                PriceAskProviderBean bean = new PriceAskProviderBean();

                // 询价ID
                bean.setAskId(pbean.getId());

                bean.setLogTime(TimeTools.now());

                bean.setProductId(pbean.getProductId());

                bean.setPrice(Float.parseFloat(request.getParameter("price_" + providers[i])));

                bean.setProviderId(request.getParameter("customerId_" + providers[i]));

                bean.setHasAmount(CommonTools.parseInt(request.getParameter("hasAmount_"
                                                                            + providers[i])));

                bean.setUserId(Helper.getUser(request).getId());

                item.add(bean);
            }
        }

        pbean.setAsks(item);
    }

    /**
     * 查询采购单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryStock(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        User user = Helper.getUser(request);

        List<StockBeanVO> list = null;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setCondition(request, condtion);

                int total = stockDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryStock");

                list = stockDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryStock");

                list = stockDAO.queryEntityVOsBycondition(OldPageSeparateTools.getCondition(
                    request, "queryStock"), OldPageSeparateTools.getPageSeparate(request,
                    "queryStock"));
            }

            // 页面显示div用
            Map<String, String> div = new HashMap<String, String>();
            for (StockBeanVO stockBeanVO : list)
            {
                setStockDisplay(user, stockBeanVO);

                if (StringTools.compare(stockBeanVO.getNeedTime(), TimeTools.now_short()) < 0)
                {
                    stockBeanVO.setOverTime(StockConstant.STOCK_OVERTIME_YES);
                }

                List<StockItemBeanVO> itemVO = stockItemDAO.queryEntityVOsByFK(stockBeanVO.getId());

                div.put(stockBeanVO.getId(), StockHelper.createTable(itemVO));
            }

            List<LocationBean> locations = locationDAO.listLocation();

            request.setAttribute("locations", locations);

            request.setAttribute("map", div);

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询采购单价格失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryStock");
    }

    /**
     * queryStockPayItem
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryStockPayItem(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        // User user = Helper.getUser(request);

        List<StockPayItemBeanVO> list = null;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setStockPayItemCondition(request, condtion);

                int total = stockPayItemDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, 50);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryStockPayItem");

                list = stockPayItemDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryStockPayItem");

                list = stockPayItemDAO.queryEntityVOsBycondition(
                    OldPageSeparateTools.getCondition(request, "queryStockPayItem"),
                    OldPageSeparateTools.getPageSeparate(request, "queryStockPayItem"));
            }

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryStockPayItem");
    }

    /**
     * queryStockPay
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryStockPay(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        List<StockPayBeanVO> list = null;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setStockPayCondition(request, condtion);

                int total = stockPayDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryStockPay");

                list = stockPayDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryStockPay");

                list = stockPayDAO.queryEntityVOsBycondition(OldPageSeparateTools.getCondition(
                    request, "queryStockPay"), OldPageSeparateTools.getPageSeparate(request,
                    "queryStockPay"));
            }

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryStockPay");
    }

    /**
     * setStockDisplay
     * 
     * @param user
     * @param stockBeanVO
     */
    private void setStockDisplay(User user, StockBeanVO stockBeanVO)
    {
        Map<Integer, Role> map = new HashMap<Integer, Role>();

        map.put(StockConstant.STOCK_STATUS_END, null);

        map.put(StockConstant.STOCK_STATUS_INIT, Role.COMMON);

        map.put(StockConstant.STOCK_STATUS_REJECT, Role.COMMON);

        map.put(StockConstant.STOCK_STATUS_SUBMIT, Role.MANAGER);

        map.put(StockConstant.STOCK_STATUS_MANAGERPASS, Role.PRICE);

        map.put(StockConstant.STOCK_STATUS_PRICEPASS, Role.STOCK);

        map.put(StockConstant.STOCK_STATUS_STOCKPASS, Role.STOCKMANAGER);

        if (map.get(stockBeanVO.getStatus()) == user.getRole())
        {
            stockBeanVO.setDisplay(StockConstant.DISPLAY_YES);
        }
        else
        {
            stockBeanVO.setDisplay(StockConstant.DISPLAY_NO);
        }

    }

    /**
     * 处理采购的分页
     * 
     * @param request
     * @param condtion
     */
    private void setCondition(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        User user = Helper.getUser(request);

        // 只能看到通过的
        if (user.getRole() == Role.COMMON)
        {
            // request.setAttribute("readonly", "true");
            condtion.addCondition("StockBean.userId", "=", user.getId());
        }

        if (user.getRole() == Role.MANAGER)
        {
            if ( !LocationHelper.isSystemLocation(user.getLocationID()))
            {
                condtion.addCondition("StockBean.locationId", "=", user.getLocationID());
            }
        }

        String status = request.getParameter("status");

        // 只有菜单进入的菜自动匹配
        String auto = request.getParameter("auto");

        if (StringTools.isNullOrNone(auto))
        {
            auto = (String)request.getAttribute("auto");
        }

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("StockBean.status", "=", status);
        }
        else
        {
            if ( !StringTools.isNullOrNone(auto))
            {
                // 设置不同角色的默认状态
                Map<Role, Integer> map = new HashMap<Role, Integer>();

                map.put(Role.MANAGER, StockConstant.STOCK_STATUS_SUBMIT);
                map.put(Role.PRICE, StockConstant.STOCK_STATUS_MANAGERPASS);
                map.put(Role.STOCK, StockConstant.STOCK_STATUS_PRICEPASS);
                map.put(Role.STOCKMANAGER, StockConstant.STOCK_STATUS_STOCKPASS);

                for (Map.Entry<Role, Integer> entry : map.entrySet())
                {
                    if (user.getRole() == entry.getKey())
                    {
                        condtion.addIntCondition("StockBean.status", "=", entry.getValue());

                        request.setAttribute("status", entry.getValue());

                        break;
                    }
                }
            }
        }

        String locationId = request.getParameter("locationId");

        if ( !StringTools.isNullOrNone(locationId))
        {
            condtion.addCondition("StockBean.locationId", "=", locationId);
        }

        String over = request.getParameter("over");

        if ( !StringTools.isNullOrNone(over))
        {
            if ("0".equals(over))
            {
                condtion.addCondition("StockBean.needTime", ">=", TimeTools.now_short());
            }

            if ("1".equals(over))
            {
                condtion.addCondition("StockBean.needTime", "<", TimeTools.now_short());
            }
        }

        String pay = request.getParameter("pay");

        if ( !StringTools.isNullOrNone(pay))
        {
            condtion.addIntCondition("StockBean.pay", "=", pay);
        }

        String type = request.getParameter("type");

        if ( !StringTools.isNullOrNone(type))
        {
            condtion.addIntCondition("StockBean.type", "=", type);
        }

        String id = request.getParameter("ids");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("StockBean.id", "like", id);
        }

        String alogTime = request.getParameter("alogTime");

        if ( !StringTools.isNullOrNone(alogTime))
        {
            condtion.addCondition("StockBean.logTime", ">=", alogTime + " 00:00:00");
        }
        else
        {
            condtion.addCondition("StockBean.logTime", ">=", TimeTools.getDateShortString( -5)
                                                             + " 00:00:00");

            request.setAttribute("alogTime", TimeTools.getDateShortString( -5));
        }

        String blogTime = request.getParameter("blogTime");

        if ( !StringTools.isNullOrNone(blogTime))
        {
            condtion.addCondition("StockBean.logTime", "<=", blogTime + " 23:59:59");
        }
        else
        {
            condtion.addCondition("StockBean.logTime", "<=", TimeTools.getDateShortString(0)
                                                             + " 23:59:59");

            request.setAttribute("blogTime", TimeTools.getDateShortString(0));
        }

        condtion.addCondition("order by StockBean.logTime desc");
    }

    /**
     * setStockPayItemCondition
     * 
     * @param request
     * @param condtion
     */
    private void setStockPayItemCondition(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("StockPayItemBean.status", "=", status);
        }

        String providerName = request.getParameter("providerName");

        if ( !StringTools.isNullOrNone(providerName))
        {
            condtion.addCondition("ProviderBean.name", "like", providerName);
        }

        // payId
        String providerCode = request.getParameter("providerCode");

        if ( !StringTools.isNullOrNone(providerCode))
        {
            condtion.addCondition("ProviderBean.code", "like", providerCode);
        }

        String payId = request.getParameter("payId");

        if ( !StringTools.isNullOrNone(payId))
        {
            condtion.addCondition("StockPayItemBean.payId", "like", payId);
        }

        String alogTime = request.getParameter("alogTime");

        if ( !StringTools.isNullOrNone(alogTime))
        {
            condtion.addCondition("StockPayItemBean.logTime", ">=", alogTime + " 00:00:00");
        }

        String blogTime = request.getParameter("blogTime");

        if ( !StringTools.isNullOrNone(blogTime))
        {
            condtion.addCondition("StockPayItemBean.logTime", "<=", blogTime + " 23:59:59");
        }

        condtion.addCondition("order by StockPayItemBean.logTime desc");
    }

    /**
     * setStockPayCondition
     * 
     * @param request
     * @param condtion
     */
    private void setStockPayCondition(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        String providerName = request.getParameter("providerName");

        if ( !StringTools.isNullOrNone(providerName))
        {
            condtion.addCondition("ProviderBean.name", "like", providerName);
        }

        String providerCode = request.getParameter("providerCode");

        if ( !StringTools.isNullOrNone(providerCode))
        {
            condtion.addCondition("ProviderBean.code", "like", providerCode);
        }

        String alogTime = request.getParameter("alogTime");

        if ( !StringTools.isNullOrNone(alogTime))
        {
            condtion.addCondition("StockPayBean.logTime", ">=", alogTime + " 00:00:00");
        }

        String blogTime = request.getParameter("blogTime");

        if ( !StringTools.isNullOrNone(blogTime))
        {
            condtion.addCondition("StockPayBean.logTime", "<=", blogTime + " 23:59:59");
        }

        condtion.addCondition("order by StockPayBean.logTime desc");
    }

    /**
     * 删除采购单价格
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delStock(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        User user = Helper.getUser(request);

        try
        {
            stockManager.delStockBean(user, id);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除采购单价格");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除采购单失败:" + e.getMessage());
        }

        request.setAttribute("forward", 1);

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * 导出查询中的stock
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportStock(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        PageSeparate oldPage = OldPageSeparateTools.getPageSeparate(request, "queryStock");

        final ConditionParse condition = OldPageSeparateTools.getCondition(request, "queryStock");

        PageSeparate page = (PageSeparate)CommonTools.deepCopy(oldPage);

        if (page.getRowCount() > 1000)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的记录数不能超过1000");

            return mapping.findForward("error");
        }

        page.reset(page.getRowCount(), Constant.PAGE_EXPORT);

        String filenName = "Stock_" + TimeTools.now("MMddHHmmss") + "_ALL.xls";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;

        WritableSheet ws = null;

        try
        {
            out = reponse.getOutputStream();

            wwb = Workbook.createWorkbook(out);
            ws = wwb.createSheet(TimeTools.now("yyyyMMdd") + "_" + page.getRowCount(), 0);
            int i = 0, j = 0;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableCellFormat format = new WritableCellFormat(font);

            ws.addCell(new Label(j++ , i, "时间", format));
            ws.addCell(new Label(j++ , i, "采购单号", format));
            ws.addCell(new Label(j++ , i, "采购人", format));
            ws.addCell(new Label(j++ , i, "采购区域", format));
            ws.addCell(new Label(j++ , i, "采购总金额", format));
            ws.addCell(new Label(j++ , i, "产品名称", format));
            ws.addCell(new Label(j++ , i, "产品编码", format));
            ws.addCell(new Label(j++ , i, "采购数量", format));
            ws.addCell(new Label(j++ , i, "参考价格", format));
            ws.addCell(new Label(j++ , i, "采购价格", format));
            ws.addCell(new Label(j++ , i, "供应商", format));
            ws.addCell(new Label(j++ , i, "本品合计", format));

            boolean fr = true;

            while (fr || page.nextPage())
            {
                List<StockBeanVO> list = stockDAO.queryEntityVOsBycondition(condition, page);

                for (StockBeanVO item : list)
                {
                    List<StockItemBeanVO> itemVOs = stockItemDAO.queryEntityVOsByFK(item.getId());

                    for (StockItemBeanVO vo : itemVOs)
                    {
                        j = 0;

                        i++ ;

                        ws.addCell(new Label(j++ , i, item.getLogTime()));
                        ws.addCell(new Label(j++ , i, item.getId()));
                        ws.addCell(new Label(j++ , i, item.getUserName()));
                        ws.addCell(new Label(j++ , i, item.getLocationName()));
                        ws.addCell(new Label(j++ , i,
                            String.valueOf(ElTools.formatNum(item.getTotal()))));

                        ws.addCell(new Label(j++ , i, vo.getProductName()));
                        ws.addCell(new Label(j++ , i, vo.getProductCode()));
                        ws.addCell(new Label(j++ , i, String.valueOf(vo.getAmount())));
                        ws.addCell(new Label(j++ , i,
                            String.valueOf(ElTools.formatNum(vo.getPrePrice()))));
                        ws.addCell(new Label(j++ , i,
                            String.valueOf(ElTools.formatNum(vo.getPrice()))));
                        ws.addCell(new Label(j++ , i, vo.getProviderName()));
                        ws.addCell(new Label(j++ , i,
                            String.valueOf(ElTools.formatNum(vo.getTotal()))));
                    }

                }

                fr = false;
            }

            wwb.write();

        }
        catch (Exception e)
        {
            return null;
        }
        finally
        {
            if (wwb != null)
            {
                try
                {
                    wwb.close();
                }
                catch (Exception e1)
                {}
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {}
            }
        }

        return null;
    }

    /**
     * @return the priceManager
     */
    public PriceManager getPriceManager()
    {
        return priceManager;
    }

    /**
     * @param priceManager
     *            the priceManager to set
     */
    public void setPriceManager(PriceManager priceManager)
    {
        this.priceManager = priceManager;
    }

    /**
     * @return the priceDAO
     */
    public PriceDAO getPriceDAO()
    {
        return priceDAO;
    }

    /**
     * @param priceDAO
     *            the priceDAO to set
     */
    public void setPriceDAO(PriceDAO priceDAO)
    {
        this.priceDAO = priceDAO;
    }

    /**
     * @return the stockManager
     */
    public StockManager getStockManager()
    {
        return stockManager;
    }

    /**
     * @param stockManager
     *            the stockManager to set
     */
    public void setStockManager(StockManager stockManager)
    {
        this.stockManager = stockManager;
    }

    /**
     * @return the priceAskProviderDAO
     */
    public PriceAskProviderDAO getPriceAskProviderDAO()
    {
        return priceAskProviderDAO;
    }

    /**
     * @param priceAskProviderDAO
     *            the priceAskProviderDAO to set
     */
    public void setPriceAskProviderDAO(PriceAskProviderDAO priceAskProviderDAO)
    {
        this.priceAskProviderDAO = priceAskProviderDAO;
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
     * @return the stockDAO
     */
    public StockDAO getStockDAO()
    {
        return stockDAO;
    }

    /**
     * @param stockDAO
     *            the stockDAO to set
     */
    public void setStockDAO(StockDAO stockDAO)
    {
        this.stockDAO = stockDAO;
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
     * @return the stockItemDAO
     */
    public StockItemDAO getStockItemDAO()
    {
        return stockItemDAO;
    }

    /**
     * @param stockItemDAO
     *            the stockItemDAO to set
     */
    public void setStockItemDAO(StockItemDAO stockItemDAO)
    {
        this.stockItemDAO = stockItemDAO;
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
     * @return the productAmountDAO
     */
    public ProductAmountDAO getProductAmountDAO()
    {
        return productAmountDAO;
    }

    /**
     * @param productAmountDAO
     *            the productAmountDAO to set
     */
    public void setProductAmountDAO(ProductAmountDAO productAmountDAO)
    {
        this.productAmountDAO = productAmountDAO;
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
     * @return the priceAskDAO
     */
    public PriceAskDAO getPriceAskDAO()
    {
        return priceAskDAO;
    }

    /**
     * @param priceAskDAO
     *            the priceAskDAO to set
     */
    public void setPriceAskDAO(PriceAskDAO priceAskDAO)
    {
        this.priceAskDAO = priceAskDAO;
    }

    /**
     * @return the stockPayItemDAO
     */
    public StockPayItemDAO getStockPayItemDAO()
    {
        return stockPayItemDAO;
    }

    /**
     * @param stockPayItemDAO
     *            the stockPayItemDAO to set
     */
    public void setStockPayItemDAO(StockPayItemDAO stockPayItemDAO)
    {
        this.stockPayItemDAO = stockPayItemDAO;
    }

    /**
     * @return the stockPayDAO
     */
    public StockPayDAO getStockPayDAO()
    {
        return stockPayDAO;
    }

    /**
     * @param stockPayDAO
     *            the stockPayDAO to set
     */
    public void setStockPayDAO(StockPayDAO stockPayDAO)
    {
        this.stockPayDAO = stockPayDAO;
    }
}
