/**
 *
 */
package com.china.center.oa.stock.action;


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

import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.OldPageSeparateTools;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.DepartmentBean;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.helper.AuthHelper;
import com.china.center.oa.publics.vo.FlowLogVO;
import com.china.center.oa.stock.action.helper.PriceAskHelper;
import com.china.center.oa.stock.action.helper.StockHelper;
import com.china.center.oa.stock.bean.PriceAskBean;
import com.china.center.oa.stock.bean.PriceAskProviderBean;
import com.china.center.oa.stock.bean.StockBean;
import com.china.center.oa.stock.bean.StockItemBean;
import com.china.center.oa.stock.constant.PriceConstant;
import com.china.center.oa.stock.constant.StockConstant;
import com.china.center.oa.stock.dao.PriceAskDAO;
import com.china.center.oa.stock.dao.PriceAskProviderDAO;
import com.china.center.oa.stock.dao.StockDAO;
import com.china.center.oa.stock.dao.StockItemDAO;
import com.china.center.oa.stock.manager.PriceAskManager;
import com.china.center.oa.stock.manager.StockManager;
import com.china.center.oa.stock.vo.PriceAskProviderBeanVO;
import com.china.center.oa.stock.vo.StockItemVO;
import com.china.center.oa.stock.vo.StockVO;
import com.china.center.osgi.jsp.ElTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * 采购的的action
 * 
 * @author Administrator
 */
public class StockAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private PriceAskManager priceAskManager = null;

    private StockManager stockManager = null;

    private StockItemDAO stockItemDAO = null;

    private StockDAO stockDAO = null;

    private ProductDAO productDAO = null;

    private LocationDAO locationDAO = null;

    private StorageRelationDAO storageRelationDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private UserDAO userDAO = null;

    private DutyDAO dutyDAO = null;

    private CommonDAO commonDAO = null;

    private DepartmentDAO departmentDAO = null;

    private PriceAskProviderDAO priceAskProviderDAO = null;

    private PriceAskDAO priceAskDAO = null;

    private InvoiceDAO invoiceDAO = null;

    /**
     *
     */
    public StockAction()
    {
    }

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

            bean.setLocationId(user.getLocationId());

            bean.setLogTime(TimeTools.now());

            // 提交人
            bean.setOwerId(user.getStafferId());

            if (StringTools.isNullOrNone(bean.getStafferId()))
            {
                // 自己的
                bean.setStafferId(user.getStafferId());
            }

            bean.setExceptStatus(StockConstant.EXCEPTSTATUS_COMMON);

            stockManager.addStockBean(user, bean);

            if ("1".equals(oprMode))
            {
                stockManager.passStock(user, bean.getId());
            }

            request.setAttribute(KeyConstant.MESSAGE, "成功增加采购单:" + bean.getId());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加采购单失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        request.getSession().setAttribute("g_ltype", "0");

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * 自动生成外网询价单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward createAskBean(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String itemId = request.getParameter("itemId");

        StockItemBean item = stockItemDAO.find(itemId);

        if (item == null)
        {
            return ActionTools.toError("数据错误,请确认操作", mapping, request);
        }

        PriceAskBean old = priceAskDAO.findByDescription(itemId);

        if (old != null)
        {
            return ActionTools.toError("已经生成自动询价单,请确认操作", mapping, request);
        }

        User user = Helper.getUser(request);

        try
        {
            PriceAskBean bean = new PriceAskBean();

            setAutoAskBean(mapping, request, itemId, item, user, bean);

            priceAskManager.addPriceAskBean(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加询价申请");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加询价申失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryStock(mapping, form, request, reponse);
    }

    /**
     * setAutoAskBean
     * 
     * @param mapping
     * @param request
     * @param itemId
     * @param item
     * @param user
     * @param bean
     */
    private void setAutoAskBean(ActionMapping mapping, HttpServletRequest request, String itemId,
                                StockItemBean item, User user, PriceAskBean bean)
    {
        bean.setId(SequenceTools.getSequence("ASK", 5));

        bean.setProductId(item.getProductId());

        bean.setAmount(item.getAmount());

        bean.setSrcamount(item.getAmount());

        bean.setType(PriceConstant.PRICE_ASK_TYPE_NET);

        bean.setUserId(user.getId());

        bean.setLogTime(TimeTools.now());

        bean.setStatus(PriceConstant.PRICE_COMMON);

        bean.setLocationId(user.getLocationId());

        bean.setDescription(itemId);

        ProductBean product = productDAO.find(bean.getProductId());

        if (product != null)
        {
            bean.setProductType(product.getType());
        }

        StockBean stock = stockDAO.find(item.getStockId());

        if (stock != null)
        {
            bean.setUserId(stock.getUserId());

            bean.setLocationId(stock.getLocationId());
        }

        bean.setProcessTime(TimeTools.getDateString(1, TimeTools.LONG_FORMAT));

        bean.setAskDate(TimeTools.getDateString(1, "yyyyMMdd"));
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

            bean.setLocationId(user.getLocationId());

            bean.setLogTime(TimeTools.now());

            bean.setStafferId(user.getStafferId());

            bean.setExceptStatus(StockConstant.EXCEPTSTATUS_COMMON);

            stockManager.updateStockBean(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功修改采购单:" + bean.getId());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

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

        String nearlyPayDate = request.getParameter("nearlyPayDate");

        String reject = request.getParameter("reject");

        try
        {
            User user = Helper.getUser(request);

            if ( !StringTools.isNullOrNone(nearlyPayDate))
            {
                stockManager.updateStockNearlyPayDate(user, id, nearlyPayDate);
            }

            if ( !StringTools.isNullOrNone(pass))
            {
                stockManager.passStock(user, id);

                request.setAttribute(KeyConstant.MESSAGE, "成功处理采购单:" + id);
            }
            else
            {
                if ("1".equals(reject))
                {
                    stockManager.rejectStock(user, id, reason);
                }

                request.setAttribute(KeyConstant.MESSAGE, "成功处理采购单:" + id);

                if ("2".equals(reject))
                {
                    stockManager.rejectStockToAsk(user, id, reason);

                    request.setAttribute(KeyConstant.MESSAGE, "成功驳回采购单到询价员:" + id);
                }
            }
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理采购单失败:" + e.getMessage());
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

                bean.setShowId(request.getParameter("showId_" + indexs[i]));

                bean.setAmount(CommonTools.parseInt(request.getParameter("amount_" + indexs[i])));

                int num = storageRelationDAO.sumAllProductByProductId(bean.getProductId());

                bean.setProductNum(num);

                bean.setStatus(StockConstant.STOCK_ITEM_STATUS_INIT);

                item.add(bean);
            }
        }

        String invoiceType = request.getParameter("invoiceType");

        if (StringTools.isNullOrNone(invoiceType))
        {
            pbean.setInvoice(StockConstant.INVOICE_YES);
        }
        else
        {
            pbean.setInvoice(StockConstant.INVOICE_NO);
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

        String productId = request.getParameter("productId");

        List<PriceAskProviderBeanVO> beanList = priceAskProviderDAO.queryByCondition(TimeTools
            .now("yyyyMMdd"), productId);

        // 获取PID
        for (PriceAskProviderBeanVO vo : beanList)
        {
            PriceAskBean ask = priceAskDAO.find(vo.getAskId());

            if (ask == null)
            {
                continue;
            }

            vo.setPid(vo.getId());

            int sum = stockItemDAO.sumNetProductByPid(vo.getId());

            vo.setRemainmount(vo.getSupportAmount() - sum);

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

        String ltype = request.getParameter("ltype");

        request.setAttribute("ltype", ltype);

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

        StockVO vo = null;

        vo = stockManager.findStockVO(id);

        request.setAttribute("bean", vo);

        // 获得所有的发票类型
        List<InvoiceBean> invoiceList = invoiceDAO.listEntityBeans();

        request.setAttribute("invoiceList", invoiceList);

        if ( !StringTools.isNullOrNone(update))
        {
            int last = 5 - vo.getItemVO().size();

            // 补齐
            for (int i = 0; i < last; i++ )
            {
                vo.getItemVO().add(new StockItemVO());
            }

            request.setAttribute("maxItem", 5 - last);

            List<DepartmentBean> departementList = departmentDAO.listEntityBeans();

            request.setAttribute("departementList", departementList);

            List<DutyBean> dutyList = dutyDAO.listEntityBeans();

            request.setAttribute("dutyList", dutyList);

            return mapping.findForward("updateStock");
        }

        if ("1".equals(process))
        {
            return mapping.findForward("processStock");
        }

        // 获取审批日志
        List<FlowLogBean> logs = flowLogDAO.queryEntityBeansByFK(id);

        List<FlowLogVO> logsVO = new ArrayList<FlowLogVO>();

        for (FlowLogBean flowLogBean : logs)
        {
            logsVO.add(StockHelper.getStockFlowLogVO(flowLogBean));
        }

        // 获得询价的列表
        Map<String, String> map = new HashMap<String, String>();
        Map<String, List<PriceAskProviderBeanVO>> map1 = new HashMap<String, List<PriceAskProviderBeanVO>>();

        for (StockItemVO StockItemVO : vo.getItemVO())
        {
            if (StockItemVO.getStatus() > StockConstant.STOCK_ITEM_STATUS_INIT)
            {
                List<PriceAskProviderBeanVO> items = priceAskProviderDAO
                    .queryEntityVOsByFK(StockItemVO.getId());

                map1.put(StockItemVO.getId(), items);

                User user = Helper.getUser(request);

                map.put(StockItemVO.getId(), PriceAskHelper.createTable(items, user));
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
        List<DepartmentBean> departementList = departmentDAO.listEntityBeans();

        request.setAttribute("departementList", departementList);

        ConditionParse condition = new ConditionParse();

        condition.addIntCondition("forward", "=", InvoiceConstant.INVOICE_FORWARD_IN);

        // 获得所有的发票类型
        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition(condition);

        request.setAttribute("invoiceList", invoiceList);

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

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

        String ltype = request.getParameter("ltype");

        StockItemVO vo = stockItemDAO.findVO(itemId);

        request.setAttribute("bean", vo);

        if (vo == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "采购不存在");

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        String stockId = vo.getStockId();

        StockBean stock = stockDAO.find(stockId);

        if (stock == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "采购不存在");

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        request.setAttribute("stock", stock);

        ProductBean product = productDAO.find(vo.getProductId());

        if (product == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "产品不存在");

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        request.setAttribute("product", product);

        User user = Helper.getUser(request);

        if (AuthHelper.containAuth(user, AuthConstant.PRICE_ASK_NET_MANAGER) && "2".equals(ltype))
        {
            return mapping.findForward("stockAskPriceForNet");
        }

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
     * 处理询价(外网询价员替供应商选择产品入库重新生成stockItem)<br>
     * 这里不存在最低价,只有单价超过数额的
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward stockItemAskPriceForNet(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String stockId = request.getParameter("stockId");

        StockItemBean bean = stockItemDAO.find(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "采购不存在");

            request.setAttribute("forward", 1);

            return queryStock(mapping, form, request, reponse);
        }

        List<StockItemBean> newItemList = new ArrayList();

        setNewStockItemList(request, bean, newItemList);

        try
        {
            stockManager.stockItemAskForNet(bean, newItemList);

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
     * setNewStockItemList
     * 
     * @param request
     * @param bean
     * @param newItemList
     */
    private void setNewStockItemList(HttpServletRequest request, StockItemBean bean,
                                     List<StockItemBean> newItemList)
    {
        String[] providers = request.getParameterValues("check_init");

        for (int i = 0; i < providers.length; i++ )
        {
            if ( !StringTools.isNullOrNone(providers[i]))
            {
                StockItemBean newBean = new StockItemBean();

                BeanUtil.copyProperties(newBean, bean);

                newBean.setAmount(CommonTools.parseInt(request.getParameter("amount_"
                                                                            + providers[i])));

                newBean.setPrice(Float.parseFloat(request.getParameter("price_" + providers[i])));

                newBean.setLogTime(TimeTools.now());

                newBean.setProviderId(request.getParameter("customerId_" + providers[i]));

                newBean.setPriceAskProviderId(request.getParameter("netaskId_" + providers[i]));

                newItemList.add(newBean);
            }
        }
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
     * 查询采购单(自己的)
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

        String ltype = getLType(request);

        List<StockVO> list = null;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setCondition(request, condtion, CommonTools.parseInt(ltype));

                int total = stockDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryStock");

                list = stockDAO.queryEntityVOsByCondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryStock");

                list = stockDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(
                    request, "queryStock"), OldPageSeparateTools.getPageSeparate(request,
                    "queryStock"));
            }

            // 页面显示div用
            Map<String, String> div = new HashMap<String, String>();

            for (StockVO StockVO : list)
            {
                setStockDisplay(user, StockVO, CommonTools.parseInt(ltype));

                if (StringTools.compare(StockVO.getNeedTime(), TimeTools.now_short()) < 0)
                {
                    StockVO.setOverTime(StockConstant.STOCK_OVERTIME_YES);
                }

                List<StockItemVO> itemVO = stockItemDAO.queryEntityVOsByFK(StockVO.getId());

                div.put(StockVO.getId(), StockHelper.createTable(itemVO));
            }

            List<LocationBean> locations = locationDAO.listEntityBeans();

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

    private String getLType(HttpServletRequest request)
    {
        String ltype = request.getParameter("ltype");

        if (StringTools.isNullOrNone(ltype))
        {
            Object attribute = request.getSession().getAttribute("g_ltype");

            if (attribute != null)
            {
                ltype = attribute.toString();
            }

            request.setAttribute("ltype", ltype);
        }
        else
        {
            // 放到session里面
            request.getSession().setAttribute("g_ltype", ltype);
        }

        return ltype;
    }

    /**
     * setStockDisplay
     * 
     * @param user
     * @param StockVO
     */
    private void setStockDisplay(User user, StockVO stockBeanVO, int type)
    {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        map.put(StockConstant.STOCK_STATUS_END, null);

        map.put(StockConstant.STOCK_STATUS_INIT, 0);

        map.put(StockConstant.STOCK_STATUS_REJECT, 0);

        map.put(StockConstant.STOCK_STATUS_SUBMIT, 1);

        map.put(StockConstant.STOCK_STATUS_MANAGERPASS, 3);

        map.put(StockConstant.STOCK_STATUS_PRICEPASS, 4);

        map.put(StockConstant.STOCK_STATUS_STOCKPASS, 5);

        if (map.get(stockBeanVO.getStatus()) != null && map.get(stockBeanVO.getStatus()) == type)
        {
            stockBeanVO.setDisplay(StockConstant.DISPLAY_YES);
        }
        else
        {
            stockBeanVO.setDisplay(StockConstant.DISPLAY_NO);
        }

        if (type == 2)
        {
            if (stockBeanVO.getStatus() == StockConstant.STOCK_STATUS_PRICEPASS)
            {
                stockBeanVO.setDisplay(StockConstant.DISPLAY_YES);
            }
        }
    }

    /**
     * 处理采购的分页
     * 
     * @param request
     * @param condtion
     */
    private void setCondition(HttpServletRequest request, ConditionParse condtion, int type)
    {
        condtion.addWhereStr();

        User user = Helper.getUser(request);

        // 只能看到通过的 COMMON
        if (type == 0)
        {
            condtion.addCondition("StockBean.stafferId", "=", user.getStafferId());
        }

        // NETSTOCK
        if (type == 2)
        {
            condtion.addIntCondition("StockBean.type", "=", PriceConstant.PRICE_ASK_TYPE_NET);
        }
        else
        {
            // 采购里面只有内外询价或者外网询价，其他的没有
            condtion.addIntCondition("StockBean.type", "=", PriceConstant.PRICE_ASK_TYPE_INNER);
        }

        // manager
        if (type == 1)
        {
            // 总部的manager可以看到所有的
            if ( !AuthHelper.containAuth(user, AuthConstant.STOCK_NOTICE_CEO))
            {
                condtion.addCondition("StockBean.locationId", "=", user.getLocationId());
            }
        }

        String status = request.getParameter("status");

        // 查询固定状态的
        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("StockBean.status", "=", status);
        }
        else
        {
            // 设置不同角色的默认状态
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();

            // MANAGER
            map.put(1, StockConstant.STOCK_STATUS_SUBMIT);

            // PRICE
            map.put(3, StockConstant.STOCK_STATUS_MANAGERPASS);
            // STOCK
            map.put(4, StockConstant.STOCK_STATUS_PRICEPASS);
            // STOCKMANAGER
            map.put(5, StockConstant.STOCK_STATUS_STOCKPASS);

            for (Map.Entry<Integer, Integer> entry : map.entrySet())
            {
                if (type == entry.getKey())
                {
                    condtion.addIntCondition("StockBean.status", "=", entry.getValue());

                    request.setAttribute("status", entry.getValue());

                    break;
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

        String stype = request.getParameter("type");

        if ( !StringTools.isNullOrNone(stype))
        {
            condtion.addIntCondition("StockBean.type", "=", stype);
        }

        String stockType = request.getParameter("stype");

        if ( !StringTools.isNullOrNone(stockType))
        {
            condtion.addIntCondition("StockBean.stype", "=", stockType);
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

        PageSeparate page = new PageSeparate(oldPage);

        if (page.getRowCount() > 1000)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的记录数不能超过1000");

            return mapping.findForward("error");
        }

        page.reset(page.getRowCount(), PublicConstant.PAGE_EXPORT);

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
                List<StockVO> list = stockDAO.queryEntityVOsByCondition(condition, page);

                for (StockVO item : list)
                {
                    List<StockItemVO> itemVOs = stockItemDAO.queryEntityVOsByFK(item.getId());

                    for (StockItemVO vo : itemVOs)
                    {
                        j = 0;

                        i++ ;

                        ws.addCell(new Label(j++ , i, item.getLogTime()));
                        ws.addCell(new Label(j++ , i, item.getId()));
                        ws.addCell(new Label(j++ , i, item.getUserName()));
                        ws.addCell(new Label(j++ , i, item.getLocationName()));
                        ws.addCell(new Label(j++ , i, String.valueOf(ElTools.formatNum(item
                            .getTotal()))));

                        ws.addCell(new Label(j++ , i, vo.getProductName()));
                        ws.addCell(new Label(j++ , i, vo.getProductCode()));
                        ws.addCell(new Label(j++ , i, String.valueOf(vo.getAmount())));
                        ws.addCell(new Label(j++ , i, String.valueOf(ElTools.formatNum(vo
                            .getPrePrice()))));
                        ws.addCell(new Label(j++ , i, String.valueOf(ElTools.formatNum(vo
                            .getPrice()))));
                        ws.addCell(new Label(j++ , i, vo.getProviderName()));
                        ws.addCell(new Label(j++ , i, String.valueOf(ElTools.formatNum(vo
                            .getTotal()))));
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
     * @return the priceAskManager
     */
    public PriceAskManager getPriceAskManager()
    {
        return priceAskManager;
    }

    /**
     * @param priceAskManager
     *            the priceAskManager to set
     */
    public void setPriceAskManager(PriceAskManager priceAskManager)
    {
        this.priceAskManager = priceAskManager;
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
     * @return the storageRelationDAO
     */
    public StorageRelationDAO getStorageRelationDAO()
    {
        return storageRelationDAO;
    }

    /**
     * @param storageRelationDAO
     *            the storageRelationDAO to set
     */
    public void setStorageRelationDAO(StorageRelationDAO storageRelationDAO)
    {
        this.storageRelationDAO = storageRelationDAO;
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
}
