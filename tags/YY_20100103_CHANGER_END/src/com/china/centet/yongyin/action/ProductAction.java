package com.china.centet.yongyin.action;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.beans.support.PagedListHolder;

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.OldPageSeparateTools;
import com.china.center.fileWriter.WriteFile;
import com.china.center.fileWriter.WriteFileFactory;
import com.china.center.jdbc.inter.PublicSQL;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.FileTools;
import com.china.center.tools.RequestDataStream;
import com.china.center.tools.RequestTools;
import com.china.center.tools.Security;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.center.tools.UtilStream;
import com.china.center.tools.Zips;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.DepotpartBean;
import com.china.centet.yongyin.bean.HotProductBean;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.ProductAmount;
import com.china.centet.yongyin.bean.ProductTypeBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.TempBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.LocationHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.DepotpartDAO;
import com.china.centet.yongyin.dao.HotProductDAO;
import com.china.centet.yongyin.dao.OutDAO;
import com.china.centet.yongyin.dao.ProductAmountDAO;
import com.china.centet.yongyin.dao.ProductDAO;
import com.china.centet.yongyin.dao.ProductTypeDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.manager.LocationManager;
import com.china.centet.yongyin.manager.ProductManager;
import com.china.centet.yongyin.trigger.Trigger;
import com.china.centet.yongyin.vo.HotProductBeanVO;
import com.china.centet.yongyin.wrap.ProductWrap;


/**
 * @author Administrator
 */
public class ProductAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private UserDAO userDAO = null;

    private ProductDAO productDAO = null;

    private ProductManager productManager = null;

    private ProductTypeDAO productTypeDAO = null;

    private ProductAmountDAO productAmountDAO = null;

    private OutDAO outDAO = null;

    private CommonDAO commonDAO = null;

    private PublicSQL publicSQL = null;

    private Trigger trigger = null;

    private HotProductDAO hotProductDAO = null;

    private LocationManager locationManager = null;

    private DepotpartDAO depotpartDAO = null;

    private String picPath = "";

    private String froot = "";

    private String picWebName = "";

    /**
     * 查询产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryProduct(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String firstLoad = request.getParameter("firstLoad");
        String flag = request.getParameter("flag");
        String kk = (String)request.getAttribute("kk");

        if ( ! ( (Helper.getUser(request).getRole() == Role.SEC && LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request))) || Helper.getUser(
            request).getRole() == Role.TOP))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有总部会计才可以操作");

            return mapping.findForward("error");
        }

        if ("1".equals(firstLoad) || "2".equals(firstLoad) || "1".equals(kk))
        {
            String name = request.getParameter("name");

            String code = request.getParameter("code");
            String modify = request.getParameter("modify");

            if ( !"1".equals(kk))
            {
                request.setAttribute("name", name);
                request.setAttribute("code", code);
            }
            else
            {
                name = code = null;
            }

            ConditionParse condtion = new ConditionParse();

            if ( !StringTools.isNullOrNone(name))
            {
                condtion.addCondition("name", "like", name);
            }

            if ( !StringTools.isNullOrNone(code))
            {
                condtion.addCondition("code", "like", code);
            }

            if ( !StringTools.isNullOrNone(modify))
            {
                condtion.addCondition("modify", ">=", modify);
            }

            List<Product> list = null;

            list = productDAO.queryProductByCondtion(condtion, true);

            // 分页的关键对象，把list塞进去形成页面list
            PagedListHolder pageList = new PagedListHolder(list);
            // 一条记录一页,在实际运用中，页面参数应该是配置的
            pageList.setPageSize(10);
            request.getSession().setAttribute("ProductList", pageList);

            request.setAttribute("productList", pageList.getPageList());
        }
        else
        {
            // 如果不存在firstLoad说明数据已经在Session里面了，直接从session取数据
            PagedListHolder pageList = (PagedListHolder)request.getSession().getAttribute(
                "ProductList");

            // page参数是从页面传来的控制翻页的参数
            String page = request.getParameter("page");
            if ("next".equals(page))
            {
                pageList.nextPage();
            }
            else if ("previous".equals(page))
            {
                pageList.previousPage();
            }

            request.setAttribute("productList", pageList.getPageList());
        }

        if ("2".equals(firstLoad) || flag != null)
        {
            // return mapping.findForward("rptProductList");
            _logger.error("ERROR in firstLoad, firstLoad can not be 2");
        }

        return mapping.findForward("productList");
    }

    /**
     * 查询产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptInQueryProduct(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String firstLoad = request.getParameter("firstLoad");

        String locationInner = request.getParameter("locationInner");

        LocationBean lb = locationManager.findLocationById(locationInner);

        if (lb == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "区域不存在");

            return mapping.findForward("error");
        }

        List<LocationBean> locationList = locationManager.listLocation();

        request.setAttribute("locationList", locationList);

        request.setAttribute("location", lb);

        String kk = (String)request.getAttribute("kk");

        if ("1".equals(firstLoad))
        {
            String name = request.getParameter("name");

            String code = request.getParameter("code");

            if ( !"1".equals(kk))
            {
                request.setAttribute("name", name);
                request.setAttribute("code", code);
            }
            else
            {
                name = code = null;
            }

            ConditionParse condtion = new ConditionParse();

            if ( !StringTools.isNullOrNone(name))
            {
                condtion.addCondition("t1.name", "like", name);
            }

            if ( !StringTools.isNullOrNone(locationInner))
            {
                condtion.addIntCondition("t2.locationID", "=", locationInner);
            }

            if ( !StringTools.isNullOrNone(code))
            {
                condtion.addCondition("t1.code", "like", code);
            }

            List<ProductAmount> list = null;

            list = productDAO.queryProductAmountByCondtion(condtion);

            // 分页的关键对象，把list塞进去形成页面list
            PagedListHolder pageList = new PagedListHolder(list);
            // 一条记录一页,在实际运用中，页面参数应该是配置的
            pageList.setPageSize(10);
            request.getSession().setAttribute("ProductList", pageList);

            request.setAttribute("productList", pageList.getPageList());
        }
        else
        {
            // 如果不存在firstLoad说明数据已经在Session里面了，直接从session取数据
            PagedListHolder pageList = (PagedListHolder)request.getSession().getAttribute(
                "ProductList");

            // page参数是从页面传来的控制翻页的参数
            String page = request.getParameter("page");
            if ("next".equals(page))
            {
                pageList.nextPage();
            }
            else if ("previous".equals(page))
            {
                pageList.previousPage();
            }

            request.setAttribute("productList", pageList.getPageList());
        }

        return mapping.findForward("rptProductList");
    }

    /**
     * 查询产品(只是查询产品的定义)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptInQueryProduct2(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<Product> list = null;
        if (OldPageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            setInnerCondition(request, condtion);

            int total = productDAO.countProduct(condtion);

            PageSeparate page = new PageSeparate(total, Constant.PAGE_SIZE);

            OldPageSeparateTools.initPageSeparate(condtion, page, request, "rptInQueryProduct2");

            list = productDAO.queryProductByCondtion(condtion, page);
        }
        else
        {
            OldPageSeparateTools.processSeparate(request, "rptInQueryProduct2");

            list = productDAO.queryProductByCondtion(OldPageSeparateTools.getCondition(request,
                "rptInQueryProduct2"), OldPageSeparateTools.getPageSeparate(request,
                "rptInQueryProduct2"));
        }

        request.setAttribute("productList", list);

        return mapping.findForward("rptProductList2");
    }

    /**
     * 查询产品(采购单的产品查询)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptInQueryProduct3(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<Product> list = null;

        if (OldPageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            setInnerCondition(request, condtion);

            int total = productDAO.countProduct(condtion);

            PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

            OldPageSeparateTools.initPageSeparate(condtion, page, request, "rptInQueryProduct3");

            list = productDAO.queryProductByCondtion(condtion, page);
        }
        else
        {
            OldPageSeparateTools.processSeparate(request, "rptInQueryProduct3");

            list = productDAO.queryProductByCondtion(OldPageSeparateTools.getCondition(request,
                "rptInQueryProduct3"), OldPageSeparateTools.getPageSeparate(request,
                "rptInQueryProduct3"));
        }

        for (Product product : list)
        {
            int num = productAmountDAO.countProductAmountByProductId(product.getId());

            product.setNum(num);
        }

        request.setAttribute("random", System.currentTimeMillis());

        request.setAttribute("rootUrl", RequestTools.getRootUrl(request) + this.picWebName);

        request.setAttribute("productList", list);

        return mapping.findForward("rptProductList3");
    }

    /**
     * 查询产品(只是查询产品的定义)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddProduct(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<ProductTypeBean> ptype = productTypeDAO.listEntityBeans();

        request.setAttribute("list", ptype);

        return mapping.findForward("addProduct");
    }

    /**
     * @param request
     * @param condtion
     */
    private void setInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String type = request.getParameter("type");

        if (StringTools.isNullOrNone(type))
        {
            request.setAttribute("type", "1");
        }

        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("code", "like", code);
        }
    }

    /**
     * 查询产品的当天的进入数量
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryProductTwo(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String firstLoad = request.getParameter("firstLoad");

        CommonTools.saveParamers(request);

        if ("1".equals(firstLoad))
        {
            ConditionParse condtion = new ConditionParse();

            setProductCondtion(request, condtion);

            List<Product> list = productDAO.queryProductByCondtion(condtion, false);

            Map<String, TempBean> map = new HashMap<String, TempBean>();
            for (Product pro : list)
            {
                map.put(pro.getId(), new TempBean());
            }

            List<OutBean> res = listOut();

            process(map, res, request);

            request.getSession().setAttribute("proMap", map);

            processList(list, map);

            // 分页的关键对象，把list塞进去形成页面list
            PagedListHolder pageList = new PagedListHolder(list);

            // 一条记录一页,在实际运用中，页面参数应该是配置的
            pageList.setPageSize(10);
            request.getSession().setAttribute("ProductList", pageList);

            request.setAttribute("productList", pageList.getPageList());
        }
        else
        {
            // 如果不存在firstLoad说明数据已经在Session里面了，直接从session取数据
            PagedListHolder pageList = (PagedListHolder)request.getSession().getAttribute(
                "ProductList");

            // page参数是从页面传来的控制翻页的参数
            String page = request.getParameter("page");
            if ("next".equals(page))
            {
                pageList.nextPage();
            }
            else if ("previous".equals(page))
            {
                pageList.previousPage();
            }

            request.setAttribute("productList", pageList.getPageList());
        }

        return mapping.findForward("productListTwo");
    }

    /**
     * 查询所有区域的产品数量
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryProductThree(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String firstLoad = request.getParameter("firstLoad");

        CommonTools.saveParamers(request);

        List<LocationBean> locationList = null;

        boolean isSYSAdmin = LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request))
                             && (Helper.getUser(request).getRole() == Role.ADMIN);

        // 咨询员也可以
        if (Helper.getUser(request).getRole() != Role.MANAGER && !isSYSAdmin
            && Helper.getUser(request).getRole() != Role.FLOW
            && Helper.getUser(request).getRole() != Role.THR)
        {
            locationList = new ArrayList<LocationBean>();

            locationList.add(locationManager.findLocationById(Helper.getCurrentLocationId(request)));

            request.setAttribute("locationId", Helper.getCurrentLocationId(request));

            request.setAttribute("readonly", "readonly=\"true\"");
        }
        else
        {
            locationList = locationManager.listLocation();
        }

        request.setAttribute("locationList", locationList);

        List<ProductAmount> list = null;

        if ("1".equals(firstLoad) || "2".equals(firstLoad))
        {
            ConditionParse condtion = new ConditionParse();

            setProductCondtion1(request, condtion);

            int tatol = productDAO.getCountByCondtion(condtion);

            PageSeparate page = new PageSeparate(tatol, Constant.PAGE_SIZE);

            list = productDAO.queryProductAmountByCondtionAndSeparate(condtion, page);

            request.getSession().setAttribute("A_page", page);

            request.getSession().setAttribute("A_condtion", condtion);

            request.setAttribute("productList", list);

            request.setAttribute("next", page.hasNextPage());

            request.setAttribute("pre", page.hasPrevPage());
        }
        else
        {
            PageSeparate pageS = (PageSeparate)request.getSession().getAttribute("A_page");

            ConditionParse condtion = (ConditionParse)request.getSession().getAttribute(
                "A_condtion");

            String page = request.getParameter("page");
            if ("next".equals(page))
            {
                pageS.nextPage();
            }
            else if ("previous".equals(page))
            {
                pageS.prevPage();
            }

            list = productDAO.queryProductAmountByCondtionAndSeparate(condtion, pageS);

            request.getSession().setAttribute("A_page", pageS);

            request.setAttribute("productList", list);

            request.setAttribute("next", pageS.hasNextPage());

            request.setAttribute("pre", pageS.hasPrevPage());
        }

        int tatol = 0;

        for (ProductAmount productAmount : list)
        {
            tatol += productAmount.getNum();
        }

        request.setAttribute("total", tatol);

        return mapping.findForward("queryProductAmount");
    }

    /**
     * @param list
     * @param map
     */
    private void processList(List<Product> list, Map<String, TempBean> map)
    {
        Product element = null;
        for (Iterator iter = list.iterator(); iter.hasNext();)
        {
            element = (Product)iter.next();
            element.setTemp( (map.get(element.getId()).hashCode()));
        }

        Collections.sort(list, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                Product p1 = (Product)o1;
                Product p2 = (Product)o2;

                if (p1.getTemp() < p2.getTemp())
                {
                    return 1;
                }

                return 0;
            }
        });
    }

    /**
     * @param map
     * @param res
     * @param bseAddList
     * @param bseDelList
     */
    private void process(Map<String, TempBean> map, List<OutBean> res, HttpServletRequest request)
    {
        List<BaseBean> bseAddList = new ArrayList<BaseBean>();

        List<BaseBean> bseDelList = new ArrayList<BaseBean>();

        for (OutBean tem : res)
        {
            // 如果不是超级管理员，需要过滤出自己的产品进出 不是中心而且不是超级管理员(超级管理员可以看到)
            if ( !tem.getLocation().equals(Helper.getCurrentLocationId(request))
                && Helper.getUser(request).getRole() != Role.TOP)
            {
                continue;
            }

            if (tem.getType() == Constant.OUT_TYPE_INBILL)
            {
                bseAddList.addAll(outDAO.queryBaseByOutFullId(tem.getFullId()));
            }
            else
            {
                bseDelList.addAll(outDAO.queryBaseByOutFullId(tem.getFullId()));
            }
        }

        // 开始统计
        TempBean temp = null;
        for (BaseBean base : bseAddList)
        {
            if (map.containsKey(base.getProductId()))
            {
                temp = map.get(base.getProductId());

                if (base.getAmount() > 0)
                {
                    temp.setAdd(temp.getAdd() + Math.abs(base.getAmount()));
                }
                else
                {
                    temp.setDel(temp.getDel() + Math.abs(base.getAmount()));
                }
            }
            else
            {
                temp = new TempBean();

                if (base.getAmount() > 0)
                {
                    temp.setAdd(temp.getAdd() + Math.abs(base.getAmount()));
                }
                else
                {
                    temp.setDel(temp.getDel() + Math.abs(base.getAmount()));
                }

                map.put(base.getProductId(), temp);
            }
        }

        for (BaseBean base : bseDelList)
        {
            if (map.containsKey(base.getProductId()))
            {
                temp = map.get(base.getProductId());

                if (base.getAmount() < 0)
                {
                    temp.setAdd(temp.getAdd() + Math.abs(base.getAmount()));
                }
                else
                {
                    temp.setDel(temp.getDel() + Math.abs(base.getAmount()));
                }
            }
            else
            {
                temp = new TempBean();

                if (base.getAmount() < 0)
                {
                    temp.setAdd(temp.getAdd() + Math.abs(base.getAmount()));
                }
                else
                {
                    temp.setDel(temp.getDel() + Math.abs(base.getAmount()));
                }

                map.put(base.getProductId(), temp);
            }
        }
    }

    /**
     * 获得当天的库单
     * 
     * @return
     */
    private List<OutBean> listOut()
    {
        // 查询库单里面的出入
        ConditionParse condtion1 = new ConditionParse();

        List<OutBean> res = new ArrayList<OutBean>();
        String now = TimeTools.now("yyyy-MM-dd");

        condtion1.addCommonCondition("outTime", "=", publicSQL.to_date(now, "yyyy-MM-dd"));
        condtion1.addIntCondition("status", "=", "1");
        condtion1.addIntCondition("type", "=", "0");
        res.addAll(outDAO.queryOutBeanByCondtion(condtion1));

        condtion1 = new ConditionParse();
        condtion1.addCommonCondition("outTime", "=", publicSQL.to_date(now, "yyyy-MM-dd"));
        condtion1.addIntCondition("status", "=", "3");
        condtion1.addIntCondition("type", "=", "0");
        res.addAll(outDAO.queryOutBeanByCondtion(condtion1));

        condtion1 = new ConditionParse();
        condtion1.addCommonCondition("outTime", "=", publicSQL.to_date(now, "yyyy-MM-dd"));
        condtion1.addIntCondition("status", "=", "3");
        condtion1.addIntCondition("type", "=", "1");
        res.addAll(outDAO.queryOutBeanByCondtion(condtion1));
        return res;
    }

    /**
     * @param request
     * @param condtion
     */
    private void setProductCondtion(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        String modify = request.getParameter("modify");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("code", "like", code);
        }

        if ( !StringTools.isNullOrNone(modify))
        {
            condtion.addCondition("modify", ">=", modify);
        }
    }

    /**
     * @param request
     * @param condtion
     */
    private void setProductCondtion1(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        String locationId = request.getParameter("locationId");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("t1.code", "like", code);
        }

        // 产品数量大于0的显示
        condtion.addIntCondition("t2.num", ">", 0);

        // 查询允许区域为空
        if ( !StringTools.isNullOrNone(locationId))
        {
            condtion.addCondition("t2.locationId", "=", locationId);
        }
        else
        {
            String firstLoad = request.getParameter("firstLoad");

            if ("1".equals(firstLoad))
            {
                request.setAttribute("locationId", Helper.getCurrentLocationId(request));

                condtion.addCondition("t2.locationId", "=", Helper.getCurrentLocationId(request));
            }
        }
    }

    public ActionForward export(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String flag = (String)request.getSession().getAttribute("flag");

        // yyyy-MM-dd HH:mm:ss
        String filenName = null;

        PagedListHolder hodeList = (PagedListHolder)request.getSession().getAttribute(
            "ProductList");

        Map<String, TempBean> map = (Map)request.getSession().getAttribute("proMap");

        filenName = flag + TimeTools.now("MMddHHmmss") + ".xls";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;
        WritableSheet ws = null;

        try
        {
            out = reponse.getOutputStream();

            // create a excel
            wwb = Workbook.createWorkbook(out);
            ws = wwb.createSheet("sheel1", 0);
            int i = 0, j = 0;

            Product element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            // WritableFont font2 = new WritableFont(WritableFont.ARIAL, 10,
            // WritableFont.BOLD,
            // false, jxl.format.UnderlineStyle.NO_UNDERLINE,
            // jxl.format.Colour.BLACK);

            WritableCellFormat format = new WritableCellFormat(font);

            // WritableCellFormat format2 = new WritableCellFormat(font2);

            ws.addCell(new Label(j++ , i, "时间", format));
            ws.addCell(new Label(j++ , i, "产品名称", format));
            ws.addCell(new Label(j++ , i, "产品编码", format));
            ws.addCell(new Label(j++ , i, "产品数量", format));
            ws.addCell(new Label(j++ , i, "当日入库", format));
            ws.addCell(new Label(j++ , i, "当日出库", format));
            ws.addCell(new Label(j++ , i, "入/出(当日)", format));

            TempBean temp = null;

            // 写outbean
            for (Iterator iter = hodeList.getSource().iterator(); iter.hasNext();)
            {
                element = (Product)iter.next();

                if (map.containsKey(element.getId()))
                {
                    temp = map.get(element.getId());
                }

                if (temp.hashCode() + element.getNum() == 0)
                {
                    continue;
                }

                j = 0;
                i++ ;

                ws.addCell(new Label(j++ , i, TimeTools.now("yyyy-MM-dd")));
                ws.addCell(new Label(j++ , i, element.getName()));
                ws.addCell(new Label(j++ , i, element.getCode()));
                ws.addCell(new Label(j++ , i, String.valueOf(element.getNum())));
                ws.addCell(new Label(j++ , i, String.valueOf(temp.getAdd())));
                ws.addCell(new Label(j++ , i, String.valueOf(temp.getDel())));
                ws.addCell(new Label(j++ , i, String.valueOf(temp.getAdd()) + '/'
                                              + String.valueOf(temp.getDel())));
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

    public ActionForward exportLocationProduct(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String eLocationId = request.getParameter("eLocationId");

        String filenName = null;
        if (Helper.getUser(request).getRole() != Role.MANAGER)
            filenName = eLocationId + TimeTools.now("MMddHHmmss") + ".xls";
        else
            filenName = eLocationId + TimeTools.now("MMddHHmmss") + ".csv";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;
        WriteFile write = null;

        try
        {
            out = reponse.getOutputStream();
            ConditionParse condtion = new ConditionParse();

            if (Helper.getUser(request).getRole() == Role.MANAGER)
            {
                List<LocationBean> lList = locationManager.listLocation();

                write = WriteFileFactory.getMyTXTWriter();

                write.openFile(out);

                // ws.addCell(new Label(j++ , i, "日期", format));
                // ws.addCell(new Label(j++ , i, "区域", format));
                // ws.addCell(new Label(j++ , i, "产品名称", format));
                // ws.addCell(new Label(j++ , i, "产品编码", format));
                // ws.addCell(new Label(j++ , i, "产品数量", format));
                write.writeLine("日期,区域,产品名称,产品编码,产品数量");

                String now = TimeTools.now("yyyy-MM-dd");

                for (LocationBean locationBean : lList)
                {
                    condtion.clear();

                    condtion.addCondition("t2.locationId", "=", locationBean.getId());

                    int tatol = productDAO.getCountByCondtion(condtion);

                    PageSeparate page = new PageSeparate(tatol, tatol);

                    List<ProductAmount> list = productDAO.queryProductAmountByCondtionAndSeparate(
                        condtion, page);

                    for (ProductAmount productAmount : list)
                    {
                        if (productAmount.getNum() > 0)
                        {
                            write.writeLine(now + ',' + productAmount.getLocationName() + ','
                                            + productAmount.getProductName().replaceAll(",", " ")
                                            + ',' + productAmount.getProductCode() + ','
                                            + String.valueOf(productAmount.getNum()));
                        }
                    }

                }

                write.close();
            }
            else
            {
                wwb = exprotXLS(out, eLocationId, condtion);
            }
        }
        catch (Throwable e)
        {
            _logger.error(e, e);

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

            if (write != null)
            {

                try
                {
                    write.close();
                }
                catch (IOException e1)
                {}
            }
        }

        return null;
    }

    /**
     * 导出全局的产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportProduct(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String filenName = "PRODUCT_" + TimeTools.now("MMddHHmmss") + ".csv";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WriteFile write = null;

        try
        {
            out = reponse.getOutputStream();

            write = WriteFileFactory.getMyTXTWriter();

            write.openFile(out);

            write.writeLine("产品编码,产品名称");

            List<Product> list = productDAO.listAll();

            for (Product item : list)
            {
                write.writeLine(item.getCode() + ',' + item.getName().replaceAll(",", " "));
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
                {}
            }

            if (write != null)
            {

                try
                {
                    write.close();
                }
                catch (IOException e1)
                {}
            }
        }

        return null;
    }

    /**
     * 导出产品快照
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportSnapsho(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String filenName = "Product_Snapsho_" + TimeTools.now("MMddHHmmss") + ".zip";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        try
        {
            out = reponse.getOutputStream();

            Zips.zipFloder(FileTools.formatPath2(this.froot) + "/snapshot/product", out);
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
                {}
            }
        }

        return null;
    }

    /**
     * @param out
     * @param eLocationId
     * @param condtion
     * @return
     * @throws IOException
     * @throws WriteException
     * @throws RowsExceededException
     */
    private WritableWorkbook exprotXLS(OutputStream out, String eLocationId,
                                       ConditionParse condtion)
        throws IOException, WriteException, RowsExceededException
    {
        WritableWorkbook wwb;
        WritableSheet ws;
        condtion.addCondition("t2.locationId", "=", eLocationId);

        // create a excel
        wwb = Workbook.createWorkbook(out);
        ws = wwb.createSheet("sheel1", 0);
        int i = 0, j = 0;

        int tatol = productDAO.getCountByCondtion(condtion);

        PageSeparate page = new PageSeparate(tatol, tatol);

        List<ProductAmount> list = productDAO.queryProductAmountByCondtionAndSeparate(condtion,
            page);

        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
            jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

        // WritableFont font2 = new WritableFont(WritableFont.ARIAL, 10,
        // WritableFont.BOLD,
        // false, jxl.format.UnderlineStyle.NO_UNDERLINE,
        // jxl.format.Colour.BLACK);

        WritableCellFormat format = new WritableCellFormat(font);

        // WritableCellFormat format2 = new WritableCellFormat(font2);

        ws.addCell(new Label(j++ , i, "日期", format));
        ws.addCell(new Label(j++ , i, "区域", format));
        ws.addCell(new Label(j++ , i, "产品名称", format));
        ws.addCell(new Label(j++ , i, "产品编码", format));
        ws.addCell(new Label(j++ , i, "产品数量", format));

        // 写outbean
        for (ProductAmount element : list)
        {

            j = 0;
            i++ ;

            ws.addCell(new Label(j++ , i, TimeTools.now("yyyy-MM-dd")));
            ws.addCell(new Label(j++ , i, element.getLocationName()));
            ws.addCell(new Label(j++ , i, element.getProductName()));
            ws.addCell(new Label(j++ , i, element.getProductCode()));
            ws.addCell(new Label(j++ , i, String.valueOf(element.getNum())));
        }

        wwb.write();
        return wwb;
    }

    public ActionForward modifyPassword(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String oldPassword = request.getParameter("oldPassword");

        String newPassword = request.getParameter("newPassword");

        User user = (User)request.getSession().getAttribute("user");

        if (user.getPassword().equals(Security.getSecurity(oldPassword)))
        {
            if (userDAO.modifyPassword(user.getName(), Security.getSecurity(newPassword)))
            {
                user.setPassword(Security.getSecurity(newPassword));
                request.setAttribute(KeyConstant.MESSAGE, "密码修改成功");
            }
            else
            {
                request.setAttribute(KeyConstant.MESSAGE, "密码修改失败");
            }
        }
        else
        {
            request.setAttribute(KeyConstant.MESSAGE, "原密码错误");
        }

        return mapping.findForward("password");
    }

    /**
     * 增加热点
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addHotProduct(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        HotProductBean bean = new HotProductBean();

        try
        {
            BeanUtil.getBean(bean, request);

            bean.setLogTime(TimeTools.now());

            productManager.addHotProduct(bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加热点产品");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加热点产品失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryHotProduct(mapping, form, request, reponse);
    }

    /**
     * 删除热点
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delHotProduct(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        try
        {
            productManager.delHotProduct(id);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除热点产品");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除热点产品失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryHotProduct(mapping, form, request, reponse);
    }

    /**
     * 查询热点产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryHotProduct(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        try
        {
            List<HotProductBeanVO> list = hotProductDAO.listEntityVOs("order by HotProductBean.orders");

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询采购单价格失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryHotProduct");
    }

    /**
     * 查询统计热点产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryStatHotProduct(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        List<HotProductBeanVO> list = null;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                String beginTime = request.getParameter("alogTime");

                if (StringTools.isNullOrNone(beginTime))
                {
                    beginTime = TimeTools.getDateFullString( -30);
                }

                request.getSession().setAttribute("alogTime", beginTime);

                String endTime = request.getParameter("blogTime");

                if (StringTools.isNullOrNone(endTime))
                {
                    endTime = TimeTools.now();
                }

                request.getSession().setAttribute("blogTime", endTime);

                int total = hotProductDAO.countStatHotProduct(beginTime, endTime);

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryStatHotProduct");

                list = hotProductDAO.queryStatHotProduct(beginTime, endTime, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryStatHotProduct");

                String beginTime = (String)request.getSession().getAttribute("alogTime");

                String endTime = (String)request.getSession().getAttribute("blogTime");

                list = hotProductDAO.queryStatHotProduct(beginTime, endTime,
                    OldPageSeparateTools.getPageSeparate(request, "queryStatHotProduct"));
            }

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryStatHotProduct");
    }

    public ActionForward addProduct(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException, FileUploadException, IOException
    {
        RequestDataStream rds = new RequestDataStream(request);

        try
        {
            rds.parser();
        }
        catch (Exception e1)
        {
            _logger.error(e1, e1);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加产品失败");

            request.setAttribute("kk", "1");

            return queryProduct(mapping, form, request, reponse);
        }

        Product product = new Product();

        BeanUtil.getBean(product, rds.getParmterMap());

        if (rds.haveStream())
        {
            FileOutputStream out = null;
            try
            {
                String filePath = "/"
                                  + product.getCode()
                                  + "."
                                  + FileTools.getFilePostfix(rds.getUniqueFileName()).toLowerCase();

                out = new FileOutputStream(this.picPath + filePath);

                product.setPicPath(filePath);

                UtilStream ustream = new UtilStream(rds.getUniqueInputStream(), out);

                ustream.copyAndCloseStream();
            }
            catch (FileNotFoundException e1)
            {
                _logger.error(e1, e1);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加产品失败");

                request.setAttribute("kk", "1");

                return queryProduct(mapping, form, request, reponse);
            }
        }

        rds.close();

        try
        {
            productManager.addProduct(product);

            request.setAttribute(KeyConstant.MESSAGE, "增加产品成功");
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加产品失败:");
            _logger.error("addProduct", e);
        }

        request.setAttribute("kk", "1");

        return queryProduct(mapping, form, request, reponse);
    }

    /**
     * 同步产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward sysnProduct(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        int result = this.trigger.synchronizedProduct();

        request.getSession().setAttribute(KeyConstant.MESSAGE, "同步产品成功,成功同步了:" + result + "个产品");

        return queryProduct(mapping, form, request, reponse);
    }

    public ActionForward delProduct(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        try
        {
            String productId = request.getParameter("productId");

            productDAO.delProduct(productId);

            request.getSession().setAttribute(KeyConstant.MESSAGE, "删除产品成功");
        }
        catch (Exception e)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "删除产品失败");
            _logger.error("delProduct", e);
        }

        request.setAttribute("kk", "1");

        return queryProduct(mapping, form, request, reponse);
    }

    /**
     * 修改产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ActionForward updateProduct(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException, IOException
    {
        RequestDataStream rds = new RequestDataStream(request);

        try
        {
            rds.parser();
        }
        catch (Exception e1)
        {
            _logger.error(e1, e1);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加产品失败");

            request.setAttribute("kk", "1");

            return queryProduct(mapping, form, request, reponse);
        }

        String productId = rds.getParmterMap().get("productId");

        Product product = productDAO.findProductById(productId);

        if (product == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有产品");

            request.setAttribute("kk", "1");

            return queryProduct(mapping, form, request, reponse);
        }

        BeanUtil.getBean(product, rds.getParmterMap());

        if (rds.haveStream())
        {
            FileOutputStream out = null;
            try
            {
                String filePath = "/" + product.getCode() + "."
                                  + FileTools.getFilePostfix(rds.getUniqueFileName());

                out = new FileOutputStream(this.picPath + filePath);

                product.setPicPath(filePath);

                UtilStream ustream = new UtilStream(rds.getUniqueInputStream(), out);

                ustream.copyAndCloseStream();
            }
            catch (FileNotFoundException e1)
            {
                _logger.error(e1, e1);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加产品失败");

                request.setAttribute("kk", "1");

                return queryProduct(mapping, form, request, reponse);
            }
        }

        rds.close();

        try
        {
            productManager.updateProduct(product);

            request.getSession().setAttribute(KeyConstant.MESSAGE,
                "更新产品[" + product.getName() + "]成功");
        }
        catch (Exception e)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE,
                "更新产品失败:" + e.getMessage());
            _logger.error("delProduct", e);
        }

        request.setAttribute("kk", "1");

        return queryProduct(mapping, form, request, reponse);
    }

    /**
     * 修改时查询产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findProduct(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String productId = request.getParameter("productId");

        String detail = request.getParameter("detail");

        Product pro = productDAO.findProductById(productId);

        if (pro == null)
        {
            request.getSession().setAttribute(KeyConstant.MESSAGE, "没有产品");

            request.setAttribute("kk", "1");

            return queryProduct(mapping, form, request, reponse);
        }
        else
        {
            request.setAttribute("bean", pro);
        }

        List<ProductTypeBean> ptype = productTypeDAO.listEntityBeans();

        request.setAttribute("list", ptype);

        request.setAttribute("random", System.currentTimeMillis());

        request.setAttribute("rootUrl", RequestTools.getRootUrl(request) + this.picWebName);

        if ( !StringTools.isNullOrNone(detail))
        {
            return mapping.findForward("detailProduct");
        }

        return mapping.findForward("updateProduct");
    }

    /**
     * query depotpart for compose
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preQueryDepotpartForCompose(ActionMapping mapping, ActionForm form,
                                                     HttpServletRequest request,
                                                     HttpServletResponse reponse)
        throws ServletException
    {
        User oprUser = Helper.getUser(request);

        if ( !LocationHelper.isSystemLocation(oprUser.getLocationID()))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有总部管理员才可以操作");

            return mapping.findForward("error");
        }

        ConditionParse condition = new ConditionParse();

        condition.addCondition("locationId", "=", oprUser.getLocationID());

        List<DepotpartBean> list = depotpartDAO.queryDepotpartByCondition(condition);

        request.setAttribute("depotpartList", list);

        return mapping.findForward("composeProduct");
    }

    /**
     * preQueryDepotpartForDecompose
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preQueryDepotpartForDecompose(ActionMapping mapping, ActionForm form,
                                                       HttpServletRequest request,
                                                       HttpServletResponse reponse)
        throws ServletException
    {
        User oprUser = Helper.getUser(request);

        if ( !LocationHelper.isSystemLocation(oprUser.getLocationID()))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有总部管理员才可以操作");

            return mapping.findForward("error");
        }

        ConditionParse condition = new ConditionParse();

        condition.addCondition("locationId", "=", oprUser.getLocationID());

        List<DepotpartBean> list = depotpartDAO.queryDepotpartByCondition(condition);

        request.setAttribute("depotpartList", list);

        return mapping.findForward("decomposeProduct");
    }

    /**
     * composeProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward composeProduct(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ProductWrap dir = createDirProductWrap(request);

        List<ProductWrap> srcList = createSrcList(request);

        try
        {
            productManager.composeProduct(user, srcList, dir);

            request.setAttribute(KeyConstant.MESSAGE, "合成产品成功");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "合成产品失败:" + e.getErrorContent());
        }

        return preQueryDepotpartForCompose(mapping, form, request, reponse);
    }

    /**
     * createSrcList
     * 
     * @param request
     * @return
     */
    private List<ProductWrap> createSrcList(HttpServletRequest request)
    {
        String[] srcDepotparts = request.getParameterValues("srcDepotpart");
        String[] srcAmounts = request.getParameterValues("srcAmount");
        String[] srcProductIds = request.getParameterValues("srcProductId");

        List<ProductWrap> srcList = new ArrayList();

        for (int i = 0; i < srcProductIds.length; i++ )
        {
            if (StringTools.isNullOrNone(srcProductIds[i]))
            {
                continue;
            }

            ProductWrap each = new ProductWrap();

            each.setProductId(srcProductIds[i]);
            each.setDepotpartId(srcDepotparts[i]);
            each.setAmount(CommonTools.parseInt(srcAmounts[i]));

            srcList.add(each);
        }
        return srcList;
    }

    /**
     * createDirProductWrap
     * 
     * @param request
     * @return
     */
    private ProductWrap createDirProductWrap(HttpServletRequest request)
    {
        String dirDepotpart = request.getParameter("dirDepotpart");
        String dirProductId = request.getParameter("dirProductId");
        String dirAmount = request.getParameter("dirAmount");

        ProductWrap dir = new ProductWrap();

        dir.setProductId(dirProductId);
        dir.setDepotpartId(dirDepotpart);
        dir.setAmount(CommonTools.parseInt(dirAmount));
        return dir;
    }

    /**
     * decomposeProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward decomposeProduct(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ProductWrap src = createDirProductWrap(request);

        List<ProductWrap> dirList = createSrcList(request);

        try
        {
            productManager.decomposeProduct(user, src, dirList);

            request.setAttribute(KeyConstant.MESSAGE, "分拆产品成功");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "分拆产品失败:" + e.getErrorContent());
        }

        return preQueryDepotpartForDecompose(mapping, form, request, reponse);
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
     * @return the productManager
     */
    public ProductManager getProductManager()
    {
        return productManager;
    }

    /**
     * @param productManager
     *            the productManager to set
     */
    public void setProductManager(ProductManager productManager)
    {
        this.productManager = productManager;
    }

    /**
     * @return 返回 locationManager
     */
    public LocationManager getLocationManager()
    {
        return locationManager;
    }

    /**
     * @param 对locationManager进行赋值
     */
    public void setLocationManager(LocationManager locationManager)
    {
        this.locationManager = locationManager;
    }

    /**
     * @return the trigger
     */
    public Trigger getTrigger()
    {
        return trigger;
    }

    /**
     * @param trigger
     *            the trigger to set
     */
    public void setTrigger(Trigger trigger)
    {
        this.trigger = trigger;
    }

    /**
     * @return the productTypeDAO
     */
    public ProductTypeDAO getProductTypeDAO()
    {
        return productTypeDAO;
    }

    /**
     * @param productTypeDAO
     *            the productTypeDAO to set
     */
    public void setProductTypeDAO(ProductTypeDAO productTypeDAO)
    {
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * @return the picPath
     */
    public String getPicPath()
    {
        return picPath;
    }

    /**
     * @param picPath
     *            the picPath to set
     */
    public void setPicPath(String picPath)
    {
        this.picPath = picPath;
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
     * @return the hotProductDAO
     */
    public HotProductDAO getHotProductDAO()
    {
        return hotProductDAO;
    }

    /**
     * @param hotProductDAO
     *            the hotProductDAO to set
     */
    public void setHotProductDAO(HotProductDAO hotProductDAO)
    {
        this.hotProductDAO = hotProductDAO;
    }

    /**
     * @return the froot
     */
    public String getFroot()
    {
        return froot;
    }

    /**
     * @param froot
     *            the froot to set
     */
    public void setFroot(String froot)
    {
        this.froot = froot;
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

}
