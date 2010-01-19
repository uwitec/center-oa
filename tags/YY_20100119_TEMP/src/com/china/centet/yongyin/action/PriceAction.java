/**
 *
 */
package com.china.centet.yongyin.action;


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

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.OldPageSeparateTools;
import com.china.center.common.query.QueryTools;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.RequestTools;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.PriceAskBean;
import com.china.centet.yongyin.bean.PriceAskProviderBean;
import com.china.centet.yongyin.bean.PriceBean;
import com.china.centet.yongyin.bean.PriceTemplateBean;
import com.china.centet.yongyin.bean.PriceWebBean;
import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.PriceAskHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.PriceConstant;
import com.china.centet.yongyin.constant.StockConstant;
import com.china.centet.yongyin.constant.SysConfigConstant;
import com.china.centet.yongyin.dao.ParameterDAO;
import com.china.centet.yongyin.dao.PriceAskDAO;
import com.china.centet.yongyin.dao.PriceAskProviderDAO;
import com.china.centet.yongyin.dao.PriceDAO;
import com.china.centet.yongyin.dao.PriceTemplateDAO;
import com.china.centet.yongyin.dao.PriceWebDAO;
import com.china.centet.yongyin.dao.ProductDAO;
import com.china.centet.yongyin.dao.ProductTypeVSCustomerDAO;
import com.china.centet.yongyin.dao.StockItemDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.manager.PriceManager;
import com.china.centet.yongyin.vo.PriceAskBeanVO;
import com.china.centet.yongyin.vo.PriceAskProviderBeanVO;
import com.china.centet.yongyin.vo.PriceBeanVO;
import com.china.centet.yongyin.vo.PriceTemplateBeanVO;
import com.china.centet.yongyin.vo.StockItemBeanVO;
import com.china.centet.yongyin.vs.ProductTypeVSCustomer;
import com.china.centet.yongyin.wrap.PriceTemplateWrap;


/**
 * 询价的action
 * 
 * @author Administrator
 */
/**
 * @author Administrator
 */
public class PriceAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private PriceManager priceManager = null;

    private PriceDAO priceDAO = null;

    private ProductDAO productDAO = null;

    private PriceWebDAO priceWebDAO = null;

    private PriceAskDAO priceAskDAO = null;

    private UserDAO userDAO = null;

    private StockItemDAO stockItemDAO = null;

    private ParameterDAO parameterDAO = null;

    private PriceAskProviderDAO priceAskProviderDAO = null;

    private ProductTypeVSCustomerDAO productTypeVSCustomerDAO = null;

    private PriceTemplateDAO priceTemplateDAO = null;

    private String picWebName = "";

    /**
     *
     */
    public PriceAction()
    {}

    /**
     * 网站列表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward listPriceWeb(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<PriceWebBean> list = priceWebDAO.listEntityBeans();

        request.setAttribute("list", list);

        return mapping.findForward("listPriceWeb");
    }

    /**
     * 增加网站
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addPriceWeb(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        PriceWebBean bean = new PriceWebBean();

        try
        {
            BeanUtil.getBean(bean, request);

            priceManager.addPriceWebBean(bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加网站:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加网站失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return listPriceWeb(mapping, form, request, reponse);
    }

    /**
     * 增加网站价格模板
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addPriceTemplate(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String productId = request.getParameter("productId");

        String oprMode = request.getParameter("oprMode");

        try
        {
            String[] priceWebIds = request.getParameterValues("priceWebIds");

            PriceTemplateBean[] beans = new PriceTemplateBean[priceWebIds.length];

            for (int i = 0; i < beans.length; i++ )
            {
                PriceTemplateBean item = new PriceTemplateBean();

                item.setProductId(productId);

                item.setPriceWebId(priceWebIds[i]);

                beans[i] = item;
            }

            // 0：增加 1：修改
            if ("0".equals(oprMode))
            {
                priceManager.addPriceTemplateBean(productId, beans);
            }
            else
            {
                priceManager.updatePriceTemplateBean(productId, beans);
            }

            request.setAttribute(KeyConstant.MESSAGE, "成功操作网站价格录入模板");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作网站价格录入模板失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", 1);

        return queryPriceTemplate(mapping, form, request, reponse);
    }

    /**
     * 删除网站价格模板
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delPriceTemplate(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String productId = request.getParameter("productId");

        try
        {
            priceManager.delPriceTemplateByProductId(productId);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除网站价格录入模板");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除网站价格录入模板失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", 1);

        return queryPriceTemplate(mapping, form, request, reponse);
    }

    /**
     * 准备增加网上价格
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddPrice(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String productId = request.getParameter("productId");

        PriceTemplateWrap wrap = priceManager.findPriceTemplateWrap(productId);

        request.setAttribute("wrap", wrap);

        return mapping.findForward("addPrice");
    }

    /**
     * 准备增加网上价格模板
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddPriceTemplate(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse reponse)
        throws ServletException
    {
        List<PriceWebBean> list = priceWebDAO.listEntityBeans();

        request.setAttribute("list", list);

        return mapping.findForward("addPriceTemplate");
    }

    /**
     * 准备处理询价
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForProcessAskPrice(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        PriceAskBeanVO bean = priceAskDAO.findVO(id);

        User user = Helper.getUser(request);

        request.setAttribute("bean", bean);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "询价不存在");

            QueryTools.setJustQuery(request);

            return queryPriceAsk(mapping, form, request, reponse);
        }

        Product product = productDAO.findProductById(bean.getProductId());

        if (product == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "产品不存在");

            QueryTools.setJustQuery(request);

            return queryPriceAsk(mapping, form, request, reponse);
        }

        request.setAttribute("product", product);

        if (user.getRole() == Role.NETASK)
        {
            PriceAskProviderBean paskBean = priceAskProviderDAO.findBeanByAskIdAndProviderId(id,
                user.getId());

            request.setAttribute("paskBean", paskBean);

            return mapping.findForward("processAskPriceForNetAsk");
        }

        return mapping.findForward("processAskPrice");
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
    public ActionForward endAskPrice(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        PriceAskBean bean = priceAskDAO.find(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "询价不存在");

            QueryTools.setMemoryQuery(request);

            return queryPriceAsk(mapping, form, request, reponse);
        }

        User user = Helper.getUser(request);

        try
        {
            priceManager.endPriceAskBean(user, id);

            request.setAttribute(KeyConstant.MESSAGE, "成功结束询价:" + id);
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "结束询价失败:" + e.getMessage());
        }

        QueryTools.setMemoryQuery(request);

        return queryPriceAsk(mapping, form, request, reponse);
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
    public ActionForward processAskPrice(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        PriceAskBean bean = priceAskDAO.find(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "询价不存在");

            QueryTools.setMemoryQuery(request);

            return queryPriceAsk(mapping, form, request, reponse);
        }

        List<PriceAskProviderBean> item = new ArrayList<PriceAskProviderBean>();

        setPriceAskProviderBeans(bean, item, request);

        User user = Helper.getUser(request);

        try
        {
            bean.setEndTime(TimeTools.now());

            bean.setPuserId(user.getId());

            priceManager.processPriceAskBean(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功处理询价申请");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理询价申请失败:" + e.getMessage());
        }

        QueryTools.setMemoryQuery(request);

        return queryPriceAsk(mapping, form, request, reponse);
    }

    /**
     * 询价明细
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findPriceAsk(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        PriceAskBeanVO bean = null;
        try
        {
            bean = priceManager.findPriceAskBeanVO(id);

            if (bean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "询价不存在");

                QueryTools.setJustQuery(request);

                return queryPriceAsk(mapping, form, request, reponse);
            }

            User user = Helper.getUser(request);

            List<PriceAskProviderBeanVO> items = bean.getItemVO();

            if (user.getRole() == Role.PRICE)
            {
                for (int i = items.size() - 1; i >= 0; i-- )
                {
                    if ( !items.get(i).getUserId().equals(user.getId()))
                    {
                        items.remove(i);
                    }
                }
            }

            if (user.getRole() == Role.NETASK)
            {
                for (int i = items.size() - 1; i >= 0; i-- )
                {
                    if ( !items.get(i).getProviderId().equals(user.getId()))
                    {
                        items.remove(i);
                    }
                }
            }

            request.setAttribute("bean", bean);
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询询价申请失败:" + e.getMessage());
        }

        request.setAttribute("forward", 1);

        return mapping.findForward("detailPriceAsk");
    }

    /**
     * 收集数据
     * 
     * @param pbean
     * @param item
     * @param request
     */
    private void setPriceAskProviderBeans(PriceAskBean pbean, List<PriceAskProviderBean> item,
                                          HttpServletRequest request)
    {
        String[] providers = request.getParameterValues("check_init");

        User user = Helper.getUser(request);

        for (int i = 0; i < providers.length; i++ )
        {
            if ( !StringTools.isNullOrNone(providers[i]))
            {
                PriceAskProviderBean bean = new PriceAskProviderBean();

                bean.setAskId(pbean.getId());

                bean.setProductId(pbean.getProductId());

                bean.setLogTime(TimeTools.now());

                bean.setProductId(pbean.getProductId());

                bean.setPrice(Float.parseFloat(request.getParameter("price_" + providers[i])));

                bean.setProviderId(request.getParameter("customerId_" + providers[i]));

                bean.setUserId(user.getId());

                bean.setHasAmount(CommonTools.parseInt(request.getParameter("hasAmount_"
                                                                            + providers[i])));

                bean.setSupportAmount(CommonTools.parseInt(request.getParameter("supportAmount_"
                                                                                + providers[i])));

                bean.setDescription(request.getParameter("description_" + providers[i]));

                // 内网询价而且是满足，自动补足数量
                if (bean.getHasAmount() == PriceConstant.HASAMOUNT_OK)
                {
                    bean.setSupportAmount(pbean.getAmount());
                }
                item.add(bean);
            }
        }

        pbean.setItem(item);
    }

    /**
     * 增加网站价格
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addPrice(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String productId = request.getParameter("productId");

        User user = Helper.getUser(request);

        try
        {
            Map<String, Object> map = request.getParameterMap();

            List<PriceBean> list = new ArrayList<PriceBean>();

            getPriceBeanList(productId, user, map, list);

            priceManager.addPriceBean(list);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加网站价格");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加网站价格失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", 1);

        return queryPrice(mapping, form, request, reponse);
    }

    /**
     * @param productId
     * @param user
     * @param map
     * @param list
     */
    private void getPriceBeanList(String productId, User user, Map<String, Object> map,
                                  List<PriceBean> list)
    {
        for (Map.Entry<String, Object> entry : map.entrySet())
        {
            if (entry.getKey().startsWith("price_"))
            {
                PriceBean pbean = new PriceBean();

                pbean.setProductId(productId);

                pbean.setUserId(user.getId());

                pbean.setLogTime(TimeTools.now());

                pbean.setStatus(PriceConstant.PRICE_COMMON);

                String key = entry.getKey();

                String webId = key.split("_")[1];

                pbean.setPriceWebId(webId);

                pbean.setPrice(Double.parseDouble( ((String[])entry.getValue())[0]));

                list.add(pbean);
            }
        }
    }

    /**
     * 增加询价申请
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addPriceAsk(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        PriceAskBean bean = new PriceAskBean();

        User user = Helper.getUser(request);

        try
        {
            BeanUtil.getBean(bean, request);

            bean.setId(SequenceTools.getSequence("ASK", 5));

            bean.setUserId(user.getId());

            bean.setLogTime(TimeTools.now());

            bean.setAskDate(TimeTools.now("yyyyMMdd"));

            bean.setStatus(PriceConstant.PRICE_COMMON);

            bean.setLocationId(user.getLocationID());

            Product product = productDAO.findProductById(bean.getProductId());

            if (product == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            bean.setProductType(product.getGenre());

            setPriceAskProcessTime(bean);

            priceManager.addPriceAskBean(bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加询价申请");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加询价申请失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        QueryTools.setForwardQuery(request);

        return queryPriceAsk(mapping, form, request, reponse);
    }

    /**
     * @param bean
     */
    private void setPriceAskProcessTime(PriceAskBean bean)
    {
        // 设置处理时间
        if (bean.getInstancy() == PriceConstant.PRICE_INSTANCY_COMMON)
        {
            bean.setProcessTime(TimeTools.getDateTimeString(2 * 3600 * 1000));
        }

        if (bean.getInstancy() == PriceConstant.PRICE_INSTANCY_INSTANCY)
        {
            bean.setProcessTime(TimeTools.getDateTimeString(3600 * 1000));
        }

        if (bean.getInstancy() == PriceConstant.PRICE_INSTANCY_VERYINSTANCY)
        {
            bean.setProcessTime(TimeTools.getDateTimeString(1800 * 1000));
        }

        if (bean.getInstancy() == PriceConstant.PRICE_INSTANCY_NETWORK_11)
        {
            bean.setProcessTime(TimeTools.now_short() + " 11:00:00");
        }

        if (bean.getInstancy() == PriceConstant.PRICE_INSTANCY_NETWORK_14)
        {
            bean.setProcessTime(TimeTools.now_short() + " 14:00:00");
        }

        if (bean.getInstancy() == PriceConstant.PRICE_INSTANCY_NETWORK_16)
        {
            bean.setProcessTime(TimeTools.now_short() + " 16:00:00");
        }

        // 测试使用的
        if (bean.getInstancy() == 6)
        {
            bean.setProcessTime(TimeTools.now_short() + " 23:00:00");
        }
    }

    /**
     * 更新价格状态
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rejectPrice(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        PriceBean bean = priceDAO.find(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "价格不存在");

            request.setAttribute("forward", 1);

            return queryPrice(mapping, form, request, reponse);
        }

        bean.setStatus(PriceConstant.PRICE_REJECT);

        try
        {
            priceManager.updatePriceBean(bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功驳回网站价格");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "驳回网站价格失败:" + e.getMessage());
        }

        request.setAttribute("forward", 1);

        return queryPrice(mapping, form, request, reponse);
    }

    /**
     * 查询网上价格
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryPrice(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        List<PriceBeanVO> list = null;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setCondition(request, condtion);

                int total = priceDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_SIZE - 10);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryPrice");

                list = priceDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryPrice");

                list = priceDAO.queryEntityVOsBycondition(OldPageSeparateTools.getCondition(
                    request, "queryPrice"), OldPageSeparateTools.getPageSeparate(request,
                    "queryPrice"));
            }

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询网站价格失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryPrice");
    }

    /**
     * 查询最近的产品价格
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryNearlyPrice(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String productId = request.getParameter("productId");

        ConditionParse condtion = new ConditionParse();

        List<PriceBeanVO> listWebPrice = null;
        List<PriceAskBeanVO> listAskPrice = null;
        List<StockItemBeanVO> listStockPrice = null;
        try
        {
            Product product = productDAO.findProductById(productId);

            if (product == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询最近价格失败:产品不存在");

                return mapping.findForward("error");
            }

            int max = parameterDAO.getInt(SysConfigConstant.PRICE_NEARLY_MAX);

            max = (max > 0) ? max : 3;

            // 最近网上价格
            condtion.addCondition("PriceBean.productId", "=", productId);

            condtion.addIntCondition("PriceBean.status", "=", PriceConstant.PRICE_COMMON);

            condtion.addCondition("order by PriceBean.id desc");

            listWebPrice = priceDAO.queryEntityVOsByLimit(condtion, max);

            condtion.clear();

            // 最近询价
            condtion.addCondition("PriceAskBean.productId", "=", productId);

            condtion.addIntCondition("PriceAskBean.status", "=",
                PriceConstant.PRICE_ASK_STATUS_PROCESSING);

            condtion.addCondition("order by PriceAskBean.id desc");

            listAskPrice = priceAskDAO.queryEntityVOsByLimit(condtion, max);

            condtion.clear();

            // 最近采购价格
            condtion.addIntCondition("StockItemBean.productId", "=", productId);

            condtion.addIntCondition("StockItemBean.status", "=",
                StockConstant.STOCK_ITEM_STATUS_END);

            condtion.addCondition("order by StockItemBean.id desc");

            listStockPrice = stockItemDAO.queryEntityVOsByLimit(condtion, max);

            request.setAttribute("listWebPrice", listWebPrice);

            request.setAttribute("listAskPrice", listAskPrice);

            request.setAttribute("listStockPrice", listStockPrice);

            request.setAttribute("product", product);

            request.setAttribute("hasPic", !StringTools.isNullOrNone(product.getPicPath()));

            request.setAttribute("random", System.currentTimeMillis());

            request.setAttribute("rootUrl", RequestTools.getRootUrl(request) + this.picWebName);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询最近价格失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryNearlyPrice");
    }

    /**
     * 查询询价
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryPriceAsk(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        List<PriceAskBeanVO> list = new ArrayList<PriceAskBeanVO>();;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setConditionForAsk(request, condtion);
            }

            QueryTools.commonQueryVO("queryPriceAsk", request, list, condtion, this.priceAskDAO);

            Map<String, String> map = new HashMap<String, String>();

            for (PriceAskBeanVO priceAskBeanVO : list)
            {
                if (priceAskBeanVO.getStatus() == PriceConstant.PRICE_ASK_STATUS_PROCESSING
                    || priceAskBeanVO.getStatus() == PriceConstant.PRICE_ASK_STATUS_END)
                {
                    User user = Helper.getUser(request);

                    List<PriceAskProviderBeanVO> items = priceAskProviderDAO.queryEntityVOsByFK(priceAskBeanVO.getId());
                    if (user.getRole() == Role.PRICE)
                    {
                        for (int i = items.size() - 1; i >= 0; i-- )
                        {
                            if ( !items.get(i).getUserId().equals(user.getId()))
                            {
                                items.remove(i);
                            }
                        }
                    }

                    if (user.getRole() == Role.NETASK)
                    {
                        for (int i = items.size() - 1; i >= 0; i-- )
                        {
                            if ( !items.get(i).getProviderId().equals(user.getId()))
                            {
                                items.remove(i);
                            }
                        }
                    }

                    if (items.size() > 0)
                    {
                        map.put(priceAskBeanVO.getId(), PriceAskHelper.createTable(items, user));
                    }
                }
            }

            request.setAttribute("list", list);

            request.setAttribute("map", map);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询询价失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryPriceAsk");
    }

    /**
     * setCondition
     * 
     * @param request
     * @param condtion
     */
    private void setCondition(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        User user = Helper.getUser(request);

        // 只能看到通过的
        if (user.getRole() != Role.PRICE && user.getRole() != Role.REPRICE)
        {
            request.setAttribute("readonly", "true");

            condtion.addIntCondition("PriceBean.status", "=", PriceConstant.PRICE_COMMON);

            request.setAttribute("status", PriceConstant.PRICE_COMMON);
        }

        String productId = request.getParameter("productId");

        if ( !StringTools.isNullOrNone(productId))
        {
            condtion.addCondition("PriceBean.productId", "=", productId);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("PriceBean.status", "=", status);
        }

        String alogTime = request.getParameter("alogTime");

        if ( !StringTools.isNullOrNone(alogTime))
        {
            condtion.addCondition("PriceBean.logTime", ">=", alogTime + " 00:00:00");
        }
        else
        {
            condtion.addCondition("PriceBean.logTime", ">=", TimeTools.getDateShortString( -7)
                                                             + " 00:00:00");

            request.setAttribute("alogTime", TimeTools.getDateShortString( -7));
        }

        String blogTime = request.getParameter("blogTime");

        if ( !StringTools.isNullOrNone(blogTime))
        {
            condtion.addCondition("PriceBean.logTime", "<=", blogTime + " 23:59:59");
        }
        else
        {
            condtion.addCondition("PriceBean.logTime", "<=", TimeTools.now_short() + " 23:59:59");

            request.setAttribute("blogTime", TimeTools.now_short());
        }

        condtion.addCondition("order by PriceBean.id desc");
    }

    /**
     * 设置网站价格模板的查询条件
     * 
     * @param request
     * @param condtion
     */
    private void setConditionForPriceTemplate(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        String productName = request.getParameter("productName");

        if ( !StringTools.isNullOrNone(productName))
        {
            condtion.addCondition("Product.name", "like", productName);
        }

        String productCode = request.getParameter("productCode");

        if ( !StringTools.isNullOrNone(productCode))
        {
            condtion.addCondition("Product.code", "like", productCode);
        }

        condtion.addCondition("group by PriceTemplateBean.productId order by PriceTemplateBean.id desc");
    }

    /**
     * setConditionForAsk
     * 
     * @param request
     * @param condtion
     */
    private void setConditionForAsk(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        User user = Helper.getUser(request);

        // 只能看到通过的
        if (user.getRole() == Role.COMMON)
        {
            condtion.addCondition("PriceAskBean.userId", "=", user.getId());
        }
        else if (user.getRole() == Role.NETASK)
        {
            List<ProductTypeVSCustomer> typeList = (List<ProductTypeVSCustomer>)request.getSession().getAttribute(
                "typeList");

            StringBuilder sb = new StringBuilder();

            sb.append("(");

            for (int i = 0; i < typeList.size(); i++ )
            {
                if (i != typeList.size() - 1)
                {
                    sb.append(typeList.get(i).getProductTypeId()).append(",");
                }
                else
                {
                    sb.append(typeList.get(i).getProductTypeId());
                }
            }

            sb.append(")");

            condtion.addIntCondition("PriceAskBean.type", "=", PriceConstant.PRICE_ASK_TYPE_NET);

            // 只能看到虚拟存储的
            condtion.addIntCondition("PriceAskBean.saveType", "=",
                PriceConstant.PRICE_ASK_SAVE_TYPE_ABS);

            condtion.addCondition("AND PriceAskBean.status in (0, 1)");

            condtion.addCondition("AND PriceAskBean.productType in " + sb.toString());
        }
        else
        {
            condtion.addIntCondition("PriceAskBean.type", "=", PriceConstant.PRICE_ASK_TYPE_INNER);
        }

        String productId = request.getParameter("productId");

        if ( !StringTools.isNullOrNone(productId))
        {
            condtion.addCondition("PriceAskBean.productId", "=", productId);
        }

        String id = request.getParameter("qid");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("PriceAskBean.id", "like", id);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("PriceAskBean.status", "=", status);
        }

        String overTime = request.getParameter("overTime");

        if ( !StringTools.isNullOrNone(overTime))
        {
            condtion.addIntCondition("PriceAskBean.overTime", "=", overTime);
        }

        String instancy = request.getParameter("instancy");

        if ( !StringTools.isNullOrNone(instancy))
        {
            condtion.addIntCondition("PriceAskBean.instancy", "=", instancy);
        }

        String alogTime = request.getParameter("alogTime");

        if ( !StringTools.isNullOrNone(alogTime))
        {
            condtion.addCondition("PriceAskBean.logTime", ">=", alogTime + " 00:00:00");
        }
        else
        {
            condtion.addCondition("PriceAskBean.logTime", ">=", TimeTools.getDateShortString( -7)
                                                                + " 00:00:00");

            QueryTools.setParMapAttribute(request, "alogTime", TimeTools.getDateShortString( -7));
        }

        String blogTime = request.getParameter("blogTime");

        if ( !StringTools.isNullOrNone(blogTime))
        {
            condtion.addCondition("PriceAskBean.logTime", "<=", blogTime + " 23:59:59");
        }
        else
        {
            condtion.addCondition("PriceAskBean.logTime", "<=", TimeTools.now_short()
                                                                + " 23:59:59");

            QueryTools.setParMapAttribute(request, "blogTime", TimeTools.now_short());
        }

        condtion.addCondition("order by PriceAskBean.id desc");
    }

    /**
     * 查询网站价格模板
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryPriceTemplate(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        List<PriceTemplateBeanVO> list = null;

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setConditionForPriceTemplate(request, condtion);

                int total = priceTemplateDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request,
                    "queryPriceTemplate");

                list = priceTemplateDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryPriceTemplate");

                list = priceTemplateDAO.queryEntityVOsBycondition(
                    OldPageSeparateTools.getCondition(request, "queryPriceTemplate"),
                    OldPageSeparateTools.getPageSeparate(request, "queryPriceTemplate"));
            }

            // 处理list
            List<PriceTemplateWrap> wrapList = new ArrayList<PriceTemplateWrap>();

            for (PriceTemplateBean priceTemplateBean : list)
            {
                PriceTemplateWrap wrap = priceManager.findPriceTemplateWrap(priceTemplateBean.getProductId());

                wrapList.add(wrap);
            }

            request.setAttribute("list", wrapList);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询询价模板失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryPriceTemplate");
    }

    /**
     * 删除网站
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delPriceWeb(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        try
        {
            priceManager.delPriceWebBean(id);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除网站");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除网站失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return listPriceWeb(mapping, form, request, reponse);
    }

    /**
     * 删除网站价格
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delPrice(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        try
        {
            priceManager.delPriceBean(id);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除网站价格");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除网站失败:" + e.getMessage());
        }

        request.setAttribute("forward", 1);

        return queryPrice(mapping, form, request, reponse);
    }

    /**
     * 删除网站价格
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delPriceAsk(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        try
        {
            priceManager.delPriceAskBean(id);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除:" + id);
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除失败:" + e.getMessage());
        }

        QueryTools.setMemoryQuery(request);

        return queryPriceAsk(mapping, form, request, reponse);
    }

    /**
     * 删除网站价格
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rejectPriceAsk(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        try
        {
            priceManager.rejectPriceAskBean(id, reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功驳回询价:" + id);
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "驳回失败:" + e.getMessage());
        }

        QueryTools.setMemoryQuery(request);

        return queryPriceAsk(mapping, form, request, reponse);
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
     * @return the priceWebDAO
     */
    public PriceWebDAO getPriceWebDAO()
    {
        return priceWebDAO;
    }

    /**
     * @param priceWebDAO
     *            the priceWebDAO to set
     */
    public void setPriceWebDAO(PriceWebDAO priceWebDAO)
    {
        this.priceWebDAO = priceWebDAO;
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
     * @return the picWebName
     */
    public String getPicWebName()
    {
        return picWebName;
    }

    /**
     * @param picWebName
     *            the picWebName to set
     */
    public void setPicWebName(String picWebName)
    {
        this.picWebName = picWebName;
    }

    /**
     * @return the priceTemplateDAO
     */
    public PriceTemplateDAO getPriceTemplateDAO()
    {
        return priceTemplateDAO;
    }

    /**
     * @param priceTemplateDAO
     *            the priceTemplateDAO to set
     */
    public void setPriceTemplateDAO(PriceTemplateDAO priceTemplateDAO)
    {
        this.priceTemplateDAO = priceTemplateDAO;
    }

    /**
     * @return the productTypeVSCustomerDAO
     */
    public ProductTypeVSCustomerDAO getProductTypeVSCustomerDAO()
    {
        return productTypeVSCustomerDAO;
    }

    /**
     * @param productTypeVSCustomerDAO
     *            the productTypeVSCustomerDAO to set
     */
    public void setProductTypeVSCustomerDAO(ProductTypeVSCustomerDAO productTypeVSCustomerDAO)
    {
        this.productTypeVSCustomerDAO = productTypeVSCustomerDAO;
    }
}
