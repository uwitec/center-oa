package com.china.centet.yongyin.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import org.jbpm.JbpmConfiguration;

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.OldPageSeparateTools;
import com.china.center.eltools.ElTools;
import com.china.center.fileWriter.WriteFile;
import com.china.center.fileWriter.WriteFileFactory;
import com.china.center.jdbc.inter.PublicSQL;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.ParamterMap;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.ConsignBean;
import com.china.centet.yongyin.bean.DepotpartBean;
import com.china.centet.yongyin.bean.FlowLogBean;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.ProviderBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.StafferBean2;
import com.china.centet.yongyin.bean.StorageBean;
import com.china.centet.yongyin.bean.TransportBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.FlowLogHelper;
import com.china.centet.yongyin.bean.helper.LocationHelper;
import com.china.centet.yongyin.bean.helper.OutBeanHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.OutConstanst;
import com.china.centet.yongyin.constant.SysConfigConstant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.ConsignDAO;
import com.china.centet.yongyin.dao.CustomerDAO;
import com.china.centet.yongyin.dao.DepotpartDAO;
import com.china.centet.yongyin.dao.OutDAO;
import com.china.centet.yongyin.dao.ParameterDAO;
import com.china.centet.yongyin.dao.ProductDAO;
import com.china.centet.yongyin.dao.ProviderDAO;
import com.china.centet.yongyin.dao.StafferDAO2;
import com.china.centet.yongyin.dao.StorageDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.manager.DepotpartManager;
import com.china.centet.yongyin.manager.FlowManager;
import com.china.centet.yongyin.manager.LocationManager;
import com.china.centet.yongyin.manager.OutManager;
import com.china.centet.yongyin.tools.YYTools;
import com.china.centet.yongyin.vo.CustomerVO2;
import com.china.centet.yongyin.vo.FlowLogBeanVO;
import com.china.centet.yongyin.vo.OutBeanVO;
import com.china.centet.yongyin.wokflow.sale.SaleConstant;


/**
 * 增加出库单
 * 
 * @author zhuzhu
 * @version 2007-4-1
 * @see
 * @since
 */
public class OutAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log logger1 = LogFactory.getLog("sec");

    private UserDAO userDAO = null;

    private OutManager outManager = null;

    private ProductDAO productDAO = null;

    private CustomerDAO customerDAO = null;

    private ProviderDAO providerDAO = null;

    private StafferDAO2 stafferDAO2 = null;

    private ParameterDAO parameterDAO = null;

    private LocationManager locationManager = null;

    private CommonDAO commonDAO = null;

    private FlowManager flowManager = null;

    private DepotpartManager depotpartManager = null;

    private StorageDAO storageDAO = null;

    private PublicSQL publicSQL = null;

    private OutDAO outDAO = null;

    private JbpmConfiguration jbpmConfiguration = null;

    private ConsignDAO consignDAO = null;

    private DepotpartDAO depotpartDAO = null;

    public ActionForward queryForAdd(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String flag = request.getParameter("flag");

        // 0:out 1:in
        request.setAttribute("GFlag", flag);

        request.setAttribute("ff", Boolean.valueOf("1".equals(flag)));

        // 得到部门
        List<String> list2 = commonDAO.listAll("t_center_departement");

        User user = Helper.getUser(request);

        // 业务员只能看到总部和本区域的产品
        if (user.getRole() == Role.COMMON)
        {
            List<LocationBean> locationList = new ArrayList<LocationBean>();

            locationList.add(locationManager.findLocationById(user.getLocationID()));

            if ( !"0".equals(user.getLocationID()))
            {
                locationList.add(locationManager.findLocationById("0"));
            }

            request.setAttribute("locationList", locationList);

            if (hasOver(user.getStafferName()))
            {
                request.setAttribute("hasOver", "注意:您的名下存在超期的销售单");
            }
        }
        else
        {
            List<LocationBean> locationList = locationManager.listLocation();
            request.setAttribute("locationList", locationList);
        }

        request.setAttribute("departementList", list2);

        request.setAttribute("current", TimeTools.now("yyyy-MM-dd"));

        ConditionParse condition = new ConditionParse();
        User oprUser = Helper.getUser(request);

        condition.addCondition("locationId", "=", oprUser.getLocationID());

        List<DepotpartBean> list = depotpartDAO.queryDepotpartByCondition(condition);

        request.setAttribute("depotpartList", list);

        double noPayBusiness = outDAO.sumNoPayAndAvouchBusinessByStafferId(user.getStafferId(),
            YYTools.getFinanceBeginDate(), YYTools.getFinanceEndDate());

        StafferBean2 sb2 = stafferDAO2.find(user.getStafferId());

        if (sb2 != null)
        {
            // 设置其剩余的信用额度
            request.setAttribute("credit", ElTools.formatNum(sb2.getCredit() - noPayBusiness));
        }

        // 增加入库单
        if ("1".equals(flag))
        {
            return mapping.findForward("addOut1");
        }

        return mapping.findForward("addOut");
    }

    private boolean hasOver(String stafferName)
    {
        ConditionParse condtion = new ConditionParse();

        // 获得条件
        getCondition(condtion, stafferName);

        List<OutBean> list = outDAO.queryOutBeanByCondtion(condtion);

        long current = new Date().getTime();
        for (OutBean outBean : list)
        {
            Date temp = TimeTools.getDateByFormat(outBean.getRedate(), "yyyy-MM-dd");

            if (temp != null)
            {
                if (temp.getTime() < current)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 增加库单时查询客户(供应商不在里面了)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryCustomer(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        List<CustomerBean> list = null;

        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        // 1是common的
        if (user.getRole() == Role.COMMON)
        {
            condition.addCondition("t2.stafferId", "=", user.getStafferId());
        }

        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condition.addCondition("name", "like", name);
            request.setAttribute("name", name);
        }

        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(code))
        {
            condition.addCondition("code", "like", code);
            request.setAttribute("code", code);
        }

        list = customerDAO.queryCustomerByCondtion(condition);

        request.setAttribute("customerList", list);

        // rptCustomer.jsp
        return mapping.findForward("rptCustomerList");
    }

    /**
     * 增加库单时查询客户(供应商不在里面了)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryAllCustomer(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        if (user.getRole() == Role.COMMON)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "非法操作");

            return mapping.findForward("error");
        }

        List<CustomerVO2> list = null;

        Map<String, String> map = new HashMap<String, String>();

        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            map.put("name", name);
            request.setAttribute("name", name);
        }

        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(code))
        {
            map.put("code", code);
            request.setAttribute("code", code);
        }

        list = customerDAO.queryAllCustomerByCondtion(map);

        request.setAttribute("customerList", list);

        // rptCustomer.jsp
        return mapping.findForward("rptAllCustomer");
    }

    /**
     * 增加库单时查询用户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryProvider(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<ProviderBean> list = null;

        ConditionParse condition = new ConditionParse();

        String flag = request.getParameter("flagg");

        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condition.addCondition("name", "like", name);
            request.setAttribute("name", name);
        }

        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(code))
        {
            condition.addCondition("code", "like", code);
            request.setAttribute("code", code);
        }

        list = providerDAO.queryEntityBeansByLimit(condition, 100);

        request.setAttribute("customerList", list);

        request.setAttribute("flagg", flag);

        return mapping.findForward("rptProvider");
    }

    /**
     * export销售单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward export(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String ff = request.getParameter("flags");

        String flag = (String)request.getSession().getAttribute("flag");
        List<OutBean> outList = null;
        // yyyy-MM-dd HH:mm:ss
        String filenName = null;

        if ("1".equals(ff))
        {
            if (OldPageSeparateTools.getPageSeparate(request, "queryOut").getRowCount() > 1500)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的记录数不能超过1500");

                return mapping.findForward("error");
            }

            outList = outDAO.queryOutBeanByCondtion(OldPageSeparateTools.getCondition(request,
                "queryOut"));

            filenName = flag + TimeTools.now("MMddHHmmss") + ".xls";
        }

        if ("2".equals(ff))
        {
            if (OldPageSeparateTools.getPageSeparate(request, "queryOut2").getRowCount() > 1500)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的记录数不能超过1500");

                return mapping.findForward("error");
            }

            outList = outDAO.queryOutBeanByCondtion(OldPageSeparateTools.getCondition(request,
                "queryOut2"));

            filenName = flag + TimeTools.now("MMddHHmmss") + ".xls";
        }

        if (outList.size() == 0)
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
            ws = wwb.createSheet("sheel1", 0);
            int i = 0, j = 0;

            OutBean element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
                false, jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

            WritableCellFormat format = new WritableCellFormat(font);

            WritableCellFormat format2 = new WritableCellFormat(font2);

            element = (OutBean)outList.get(0);

            String ffs = null;
            if (element.getType() == 0)
            {
                ffs = "出";
            }
            else
            {
                ffs = "入";
            }

            ws.addCell(new Label(j++ , i, ffs + "库日期", format));
            ws.addCell(new Label(j++ , i, "调" + ffs + "部门", format));
            ws.addCell(new Label(j++ , i, element.getType() == 0 ? "客户" : "供应商(调出部门)", format));
            ws.addCell(new Label(j++ , i, "联系人", format));
            ws.addCell(new Label(j++ , i, "联系电话", format));
            ws.addCell(new Label(j++ , i, "单据号码", format));
            ws.addCell(new Label(j++ , i, "回款日期", format));
            ws.addCell(new Label(j++ , i, "状态", format));
            ws.addCell(new Label(j++ , i, "经办人", format));
            ws.addCell(new Label(j++ , i, "描述", format));

            ws.addCell(new Label(j++ , i, "品名", format));
            ws.addCell(new Label(j++ , i, "单位", format));
            ws.addCell(new Label(j++ , i, "数量", format));
            ws.addCell(new Label(j++ , i, "单价", format));
            ws.addCell(new Label(j++ , i, "金额", format));
            ws.addCell(new Label(j++ , i, "描述", format));
            ws.addCell(new Label(j++ , i, "发货单号", format));
            ws.addCell(new Label(j++ , i, "发货方式", format));
            ws.addCell(new Label(j++ , i, "总金额", format));

            // 写outbean
            for (Iterator iter = outList.iterator(); iter.hasNext();)
            {
                element = (OutBean)iter.next();

                // 写baseBean
                List<BaseBean> baseList = null;
                BaseBean base = null;

                ConsignBean consignBean = null;

                TransportBean transportBean = null;

                try
                {
                    baseList = outDAO.queryBaseByOutFullId(element.getFullId());

                    consignBean = consignDAO.findConsignById(element.getFullId());

                    if (consignBean != null)
                    {
                        transportBean = consignDAO.findTransportById(consignBean.getTransport());
                    }
                }
                catch (Exception e)
                {
                    _logger.error(e, e);
                }

                for (Iterator iterator = baseList.iterator(); iterator.hasNext();)
                {
                    j = 0;
                    i++ ;

                    ws.addCell(new Label(j++ , i, element.getOutTime()));

                    ws.addCell(new Label(j++ , i, element.getDepartment()));

                    ws.addCell(new Label(j++ , i, element.getCustomerName()));

                    ws.addCell(new Label(j++ , i, element.getConnector()));

                    ws.addCell(new Label(j++ , i, element.getPhone()));

                    ws.addCell(new Label(j++ , i, element.getFullId()));

                    ws.addCell(new Label(j++ , i, element.getRedate()));

                    ws.addCell(new Label(j++ , i, OutBeanHelper.getStatus(element.getStatus(),
                        false)));

                    ws.addCell(new Label(j++ , i, element.getStafferName()));

                    ws.addCell(new Label(j++ , i, element.getDescription()));

                    base = (BaseBean)iterator.next();

                    ws.addCell(new Label(j++ , i, base.getProductName()));
                    ws.addCell(new Label(j++ , i, base.getUnit()));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getAmount())));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getPrice())));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getValue())));
                    ws.addCell(new Label(j++ , i, base.getDescription()));

                    if ( !iterator.hasNext())
                    {
                        // 到出发货单和发货方式
                        if (consignBean != null)
                        {
                            ws.addCell(new Label(j++ , i, consignBean.getTransportNo()));

                            if (transportBean != null)
                            {
                                ws.addCell(new Label(j++ , i, transportBean.getName()));
                            }
                            else
                            {
                                ws.addCell(new Label(j++ , i, ""));
                            }
                        }
                        else
                        {
                            ws.addCell(new Label(j++ , i, ""));
                            ws.addCell(new Label(j++ , i, ""));
                        }

                        ws.addCell(new Label(j++ , i, String.valueOf(element.getTotal()), format2));
                    }
                }

            }
        }
        catch (Exception e)
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
                    wwb.write();
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

    public ActionForward exportWord(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {

        OutputStream out = null;

        String outId = request.getParameter("outId");

        String flag = (String)request.getSession().getAttribute("flag");
        // yyyy-MM-dd HH:mm:ss

        String filenName = flag + TimeTools.now("MMddHHmmss") + ".doc";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WriteFile writer = null;

        try
        {
            writer = WriteFileFactory.getMyWordWriter();

            out = reponse.getOutputStream();

            writer.openFile(out);

            OutBean element = outDAO.findOutById(outId);

            if (element == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在");

                return mapping.findForward("error");
            }

            List<BaseBean> list = outDAO.queryBaseByOutFullId(outId);

            element.setBaseList(list);

            String ffs = null;
            if (element.getType() == 0)
            {
                ffs = "出";
            }
            else
            {
                ffs = "入";
            }

            StringBuffer buffer = new StringBuffer(256);

            buffer.append(ffs + "库日期:").append(element.getOutTime()).append("   ");

            if (element.getType() == 0)
            {
                buffer.append(ffs + "库类别：").append(element.getOutType() == 0 ? "销售出库" : "调拨出库").append(
                    "\r\n");
            }
            else
            {
                buffer.append(ffs + "库类别：").append(
                    ElTools.getValue(element.getOutType(), "采购入库", "调拨", "盘亏出库", "盘盈入库", "调拨")).append(
                    "\r\n");
            }

            writer.writeContent(buffer.toString());

            writer.close();

        }
        catch (Exception e)
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
     * 增加(保存修改)修改库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addOut(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        String locationId = Helper.getCurrentLocationId(request);

        String saves = request.getParameter("saves");

        // 客户信用级别
        String customercreditlevel = request.getParameter("customercreditlevel");

        String fullId = request.getParameter("fullId");

        if ("saves".equals(saves))
        {
            saves = "保存";
        }
        else
        {
            saves = "提交";
        }

        OutBean outBean = new OutBean();

        outBean.setLocationId(locationId);

        // 增加职员的ID
        outBean.setStafferId(user.getStafferId());

        BeanUtil.getBean(outBean, request);

        if (outBean.getOutType() == OutConstanst.INBILL_SELF_IN)
        {
            // 设置成调出
            outBean.setOutType(Constant.INBILL_OUT);

            String productLocationId = request.getParameter("sdestinationId");

            if (StringTools.isNullOrNone(productLocationId))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据不合法,请重新操作");

                return mapping.findForward("error");
            }

            outBean.setLocationId(productLocationId);

            outBean.setLocation(productLocationId);
        }

        if (StringTools.isNullOrNone(outBean.getLocation()))
        {
            outBean.setLocation(outBean.getLocationId());
        }

        ParamterMap map = new ParamterMap(request);

        ActionForward action = null;
        if (outBean.getType() == Constant.OUT_TYPE_OUTBILL)
        {
            // 强制设置成OUT_SAIL_TYPE_MONEY
            if (OutConstanst.BLACK_LEVEL.equals(customercreditlevel))
            {
                outBean.setReserve3(OutConstanst.OUT_SAIL_TYPE_MONEY);
            }

            action = processCommonOut(mapping, request, user, saves, fullId, outBean, map);
        }
        else
        {
            // 入库单的处理
            try
            {
                outManager.addOut(outBean, map.getParameterMap(), user);

                if ("提交".equals(saves))
                {
                    outManager.submit(outBean.getFullId(), user);
                }
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

                return mapping.findForward("error");
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:请重新操作!");

                return mapping.findForward("error");
            }
        }

        if (action != null)
        {
            return action;
        }

        request.getSession().setAttribute(KeyConstant.MESSAGE, "此库单的单号是:" + outBean.getFullId());

        request.setAttribute("forward", "10");

        if (outBean.getType() == 0)
        {
            request.setAttribute("Qq", "0");
        }
        else
        {
            request.setAttribute("Qq", "1");
        }

        // 设置去哪个查询页面

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 处理销售单的逻辑
     * 
     * @param mapping
     * @param request
     * @param user
     * @param saves
     * @param fullId
     * @param outBean
     * @param map
     */
    private ActionForward processCommonOut(ActionMapping mapping, HttpServletRequest request,
                                           User user, String saves, String fullId,
                                           OutBean outBean, ParamterMap map)
    {
        // 增加库单
        if ( !StringTools.isNullOrNone(fullId))
        {
            // 修改
            OutBean out = outDAO.findOutById(fullId);

            if (out == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在");

                return mapping.findForward("error");
            }

            BeanUtil.getBean(out, request);
        }

        try
        {
            String id = outManager.addOut(outBean, map.getParameterMap(), user);

            // 提交
            if (SaleConstant.FLOW_DECISION_SUBMIT.equals(saves))
            {
                outManager.submit(id, user);
            }
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * 提交库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward submitOut(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 管理员查询入库单<br>
     * 业务员查询出库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryOut(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fow = (String)request.getAttribute("forward");

        User user = (User)request.getSession().getAttribute("user");

        String load = request.getParameter("load");

        String firstLoad = request.getParameter("firstLoad");

        String flagg = request.getParameter("flagg");

        if (StringTools.isNullOrNone(flagg))
        {
            flagg = (String)request.getAttribute("Bflagg");
        }

        request.setAttribute("GFlag", flagg);

        List<OutBean> list = null;

        CommonTools.saveParamers(request);

        // 0：出库 1:入库
        String queryType = request.getParameter("queryType");

        // 是否处理调出
        String proIn = request.getParameter("proIn");

        if (StringTools.isNullOrNone(proIn))
        {
            proIn = "0";
        }

        ConditionParse condtion = new ConditionParse();
        try
        {

            if (OldPageSeparateTools.isFirstLoad(request))
            {
                String locationId = (String)request.getSession().getAttribute("flag");

                // 物流可以看见所有的入库单 处理调出
                if ( !StringTools.isNullOrNone(locationId) && user.getRole() != Role.FLOW
                    && "0".equals(proIn))
                {
                    condtion.addIntCondition("locationId", "=", locationId);
                }

                if ( !StringTools.isNullOrNone(queryType))
                {
                    condtion.addIntCondition("type", "=", queryType);
                }

                if ("1".equals(load))
                {
                    // 增加出入库后的类型
                    String qq = (String)request.getAttribute("Qq");

                    if ( !StringTools.isNullOrNone(qq))
                    {
                        condtion.addIntCondition("type", "=", qq);
                        request.setAttribute("GFlag", qq);
                    }

                    Calendar cal = Calendar.getInstance();

                    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);

                    String now = TimeTools.getStringByFormat(new Date(), "yyyy-MM-dd");

                    String now1 = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()),
                        "yyyy-MM-dd");

                    condtion.addCommonCondition("outTime", ">=", publicSQL.to_date(now1,
                        "yyyy-MM-dd"));

                    condtion.addCommonCondition("outTime", "<=", publicSQL.to_date(now,
                        "yyyy-MM-dd"));

                    request.setAttribute("outTime", now1);

                    request.setAttribute("outTime1", now);

                    if (user.getRole() != Role.ADMIN && user.getRole() != Role.FLOW
                        && user.getRole() != Role.MANAGER)
                    {
                        condtion.addCondition("STAFFERNAME", "=", user.getStafferName());
                    }
                }

                // 增加/删除
                if ("10".equals(fow))
                {
                    // 增加出入库后的类型
                    String qq = (String)request.getAttribute("Qq");

                    if ( !StringTools.isNullOrNone(qq))
                    {
                        // condtion.addIntCondition("type", "=", qq);
                        request.setAttribute("GFlag", qq);
                    }

                    Calendar cal = Calendar.getInstance();

                    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);

                    String now = TimeTools.getStringByFormat(new Date(), "yyyy-MM-dd");

                    String now1 = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()),
                        "yyyy-MM-dd");

                    condtion.addCommonCondition("outTime", ">=", publicSQL.to_date(now1,
                        "yyyy-MM-dd"));

                    condtion.addCommonCondition("outTime", "<=", publicSQL.to_date(now,
                        "yyyy-MM-dd"));

                    request.setAttribute("outTime", now1);

                    request.setAttribute("outTime1", now);

                    if (user.getRole() != Role.ADMIN && user.getRole() != Role.FLOW)
                    {
                        condtion.addCondition("STAFFERNAME", "=", user.getStafferName());
                        condtion.addIntCondition("type", "=", "0");
                    }
                    else
                    {
                        condtion.addIntCondition("type", "=", "1");
                    }

                }

                if ("1".equals(firstLoad) || "9".equals(fow))
                {
                    setCondition(request, condtion);

                    // 咨询员也可以查询入库单
                    if (user.getRole() != Role.ADMIN && user.getRole() != Role.MANAGER
                        && user.getRole() != Role.FLOW && user.getRole() != Role.THR)
                    {
                        condtion.addCondition("STAFFERNAME", "=", user.getStafferName());
                    }

                    // 增加调入到本区域的
                    if (user.getRole() == Role.ADMIN && ("1".equals(proIn) || "2".equals(proIn)))
                    {
                        condtion.addIntCondition("type", "=", Constant.OUT_TYPE_INBILL);
                        condtion.addIntCondition("outType", "=", Constant.INBILL_OUT);
                        condtion.addCondition("and status in (3, 4)");

                        if ( ! (LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request)) && "2".equals(proIn)))
                        {
                            condtion.addCondition("destinationId", "=",
                                Helper.getCurrentLocationId(request));
                        }
                    }
                }

                int tatol = outDAO.countOutBeanByCondtion(condtion);

                PageSeparate page = new PageSeparate(tatol, Constant.PAGE_SIZE - 5);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryOut");

                list = outDAO.queryOutBeanByCondtion(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryOut");

                list = outDAO.queryOutBeanByCondtion(OldPageSeparateTools.getCondition(request,
                    "queryOut"), OldPageSeparateTools.getPageSeparate(request, "queryOut"));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");
            _logger.error("addOut", e);

            return mapping.findForward("error");
        }

        request.setAttribute("listOut", list);

        // 发货单
        ConsignBean temp = null;
        for (OutBean outBean : list)
        {
            temp = consignDAO.findConsignById(outBean.getFullId());
            if (temp != null)
            {
                outBean.setConsign(temp.getReprotType());
            }
        }

        request.getSession().setAttribute("listOut1", list);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        request.setAttribute("ff", Boolean.valueOf("1".equals(request.getAttribute("GFlag"))));

        getDivs(request, list);

        if (user.getRole() == Role.COMMON)
        {
            return mapping.findForward("listOut");
        }

        return mapping.findForward("listOut1");
    }

    /**
     * 管理员【出库单管理】需要翻页的实现
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryOut2(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String load = request.getParameter("load");
        String fow = (String)request.getAttribute("forward");
        String firstLoad = request.getParameter("firstLoad");

        // 0：出库 1:入库
        String queryType = request.getParameter("queryType");

        if (StringTools.isNullOrNone(queryType))
        {
            queryType = "0";
        }

        List<OutBean> list = null;
        User user = (User)request.getSession().getAttribute("user");

        try
        {
            ConditionParse condtion = new ConditionParse();

            if (user.getRole() == Role.COMMON)
            {
                condtion.addCondition("STAFFERNAME", "=", user.getStafferName());
            }

            condtion.addIntCondition("type", "=", queryType);

            // 总经理审核本区域的销售单
            if (user.getRole() == Role.MANAGER)
            {
                // 非总部的manager可以看到自己区域下的 但是总部的不限制区域
                if ( !LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request)))
                {
                    condtion.addCondition("locationId", "=", user.getLocationID());
                }
            }
            else
            {
                // 其他的人员审核产品区域的相关
                condtion.addCondition("location", "=", user.getLocationID());
            }

            if (OldPageSeparateTools.isFirstLoad(request))
            {
                if ("1".equals(load))
                {
                    CommonTools.saveParamers(request);

                    Calendar cal = Calendar.getInstance();

                    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);

                    String now = TimeTools.getStringByFormat(new Date(), "yyyy-MM-dd");

                    String now1 = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()),
                        "yyyy-MM-dd");

                    condtion.addCommonCondition("outTime", ">=", publicSQL.to_date(now1,
                        "yyyy-MM-dd"));

                    condtion.addCommonCondition("outTime", "<=", publicSQL.to_date(now,
                        "yyyy-MM-dd"));

                    request.setAttribute("outTime", now1);

                    request.setAttribute("outTime1", now);

                    if (user.getRole() == Role.ADMIN)
                    {
                        if (Helper.getCurrentLocationId(request).equals(Constant.SYSTEM_LOCATION))
                        {
                            condtion.addIntCondition("status", "=", Constant.STATUS_FLOW_PASS);
                            request.setAttribute("status", Constant.STATUS_FLOW_PASS);
                        }
                        else
                        {
                            condtion.addIntCondition("status", "=", Constant.STATUS_MANAGER_PASS);
                            request.setAttribute("status", Constant.STATUS_MANAGER_PASS);
                        }
                    }
                    else if (user.getRole() == Role.FLOW)
                    {
                        condtion.addIntCondition("status", "=", Constant.STATUS_MANAGER_PASS);
                        request.setAttribute("status", Constant.STATUS_MANAGER_PASS);
                    }
                    else
                    {
                        condtion.addIntCondition("status", "=", "1");
                        request.setAttribute("status", "1");
                    }

                    condtion.addIntCondition("type", "=", "0");
                }

                if ("1".equals(firstLoad) || "10".equals(fow))
                {
                    setCondition(request, condtion);

                    condtion.addIntCondition("type", "=", "0");
                }

                if ("11".equals(fow))
                {
                    setCondition(request, condtion);
                }

                int tatol = outDAO.countOutBeanByCondtion(condtion);

                PageSeparate page = new PageSeparate(tatol, Constant.PAGE_SIZE - 5);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryOut2");

                list = outDAO.queryOutBeanByCondtion(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryOut2");

                list = outDAO.queryOutBeanByCondtion(OldPageSeparateTools.getCondition(request,
                    "queryOut2"), OldPageSeparateTools.getPageSeparate(request, "queryOut2"));

            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");
            _logger.error("addOut", e);

            return mapping.findForward("error");
        }

        // 物流的需要知道是否有发货单
        double total = 0.0d;
        ConsignBean temp = null;

        Map<String, String> hasMap = new HashMap<String, String>();
        Map<String, Integer> overDayMap = new HashMap<String, Integer>();
        for (OutBean outBean : list)
        {
            temp = consignDAO.findConsignById(outBean.getFullId());

            if (temp != null)
            {
                outBean.setConsign(temp.getCurrentStatus());
            }

            total += outBean.getTotal();

            // 是否超期 超期几天
            if ( !StringTools.isNullOrNone(outBean.getRedate())
                && outBean.getPay() == Constant.PAY_NOT)
            {
                int overDays = TimeTools.cdate(TimeTools.now_short(), outBean.getRedate());

                overDayMap.put(outBean.getFullId(), overDays);
            }

            if (hasOver(outBean.getStafferName()))
            {
                hasMap.put(outBean.getFullId(), "true");
            }
            else
            {
                hasMap.put(outBean.getFullId(), "false");
            }
        }

        request.getSession().setAttribute("listOut2", list);

        request.setAttribute("hasMap", hasMap);

        request.setAttribute("overDayMap", overDayMap);

        List<String> lists = commonDAO.listAll("t_center_oastaffer");

        request.setAttribute("staffers", lists);

        request.setAttribute("total", total);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        getDivs(request, list);

        return mapping.findForward("listOut2");
    }

    /**
     * 会计【总部核对】【往来核对】
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryOut3(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String load = request.getParameter("load");
        String flag = request.getParameter("flag");
        String forward = (String)request.getAttribute("forward");
        String firstLoad = request.getParameter("firstLoad");

        CommonTools.saveParamers(request);

        User user = Helper.getUser(request);
        List<OutBean> list = null;

        if ("99".equals(flag))
        {
            if ( !Constant.SYSTEM_LOCATION.equals(Helper.getCurrentLocationId(request)))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有总部的会计才能总部核对");

                return mapping.findForward("error");
            }
        }

        try
        {
            ConditionParse condtion = new ConditionParse();

            preCondition(request, user, condtion);

            // 分页处理
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                if ("1".equals(load))
                {
                    setCondtion4(request, flag, condtion);
                }

                if ("1".equals(firstLoad))
                {
                    setCondition3(request, condtion, true);
                }

                if ("10".equals(forward))
                {
                    setCondition3(request, condtion, false);
                }

                int tatol = outDAO.countOutBeanByCondtion(condtion);

                PageSeparate page = new PageSeparate(tatol, Constant.PAGE_SIZE - 5);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryOut3");

                list = outDAO.queryOutBeanByCondtion(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryOut3");

                list = outDAO.queryOutBeanByCondtion(OldPageSeparateTools.getCondition(request,
                    "queryOut3"), OldPageSeparateTools.getPageSeparate(request, "queryOut3"));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");

            _logger.error("addOut", e);

            return mapping.findForward("error");
        }

        request.setAttribute("listOut3", list);

        List<String> lists = commonDAO.listAll("t_center_oastaffer");

        request.setAttribute("staffers", lists);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        getDivs(request, list);

        return mapping.findForward("listOut3");
    }

    /**
     * 设置查询条件
     * 
     * @param request
     * @param flag
     * @param condtion
     */
    private void setCondtion4(HttpServletRequest request, String flag, ConditionParse condtion)
    {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 3);

        String now = TimeTools.getStringByFormat(new Date(), "yyyy-MM-dd");

        String now1 = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyy-MM-dd");

        condtion.addCommonCondition("outTime", ">=", publicSQL.to_date(now1, "yyyy-MM-dd"));

        condtion.addCommonCondition("outTime", "<=", publicSQL.to_date(now, "yyyy-MM-dd"));

        if ("4".equals(flag))
        {
            condtion.addCondition("destinationId", "=", Helper.getCurrentLocationId(request));
        }

        request.setAttribute("outTime", now1);

        request.setAttribute("outTime1", now);

        Map out3Map = new HashMap();

        out3Map.put("outTime", now1);
        out3Map.put("outTime1", now);

        out3Map.put("rIndex", 0);

        // 全局保存sec的查询
        request.getSession().setAttribute("out3Map", out3Map);
    }

    /**
     * 准备查询条件
     * 
     * @param request
     * @param user
     * @param condtion
     */
    private void preCondition(HttpServletRequest request, User user, ConditionParse condtion)
    {
        condtion.addWhereStr();

        // 会计可以查询3 4
        if (user.getRole() == Role.SEC)
        {
            // 这里需要计算客户的信用金额-是否报送物流中心经理审批
            boolean outCredit = parameterDAO.getBoolean(SysConfigConstant.OUT_CREDIT);

            if (outCredit)
            {
                condtion.addCondition("and status in (1, 3, 4)");
            }
            else
            {
                condtion.addCondition("and status in (3, 4)");
            }
        }
        else
        {
            condtion.addIntCondition("status", "=", Constant.STATUS_PASS);
        }

        // 会计只能查询本区域提交的库单
        if (Helper.getUser(request).getRole() == Role.SEC)
        {
            if ( !Constant.SYSTEM_LOCATION.equals(Helper.getCurrentLocationId(request)))
            {
                condtion.addCondition("locationID", "=", Helper.getCurrentLocationId(request));
            }
        }

        // 管理员查询在途
        if (Helper.getUser(request).getRole() == Role.ADMIN)
        {
            condtion.addIntCondition("inway", "=", Constant.IN_WAY);
        }
    }

    /**
     * 业务员预警
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryWarnOut(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        ConditionParse condtion = new ConditionParse();

        // 获得条件
        getCondition(condtion, user.getStafferName());

        List<OutBean> list = outDAO.queryOutBeanByCondtion(condtion);

        long current = new Date().getTime();
        for (OutBean outBean : list)
        {
            Date temp = TimeTools.getDateByFormat(outBean.getRedate(), "yyyy-MM-dd");

            if (temp != null)
            {
                if (temp.getTime() < current)
                {
                    // 超期的
                    outBean.setPay(2);
                }
            }
        }

        // 提示页面
        getDivs(request, list);

        request.setAttribute("flagOut", list);

        return mapping.findForward("warnOutList");
    }

    private void getCondition(ConditionParse condtion, String stafferName)
    {
        // 只查询出库单
        condtion.addIntCondition("type", "=", Constant.OUT_TYPE_OUTBILL);

        condtion.addIntCondition("status", "=", Constant.STATUS_PASS);

        condtion.addCondition("STAFFERNAME", "=", stafferName);

        condtion.addIntCondition("pay", "=", Constant.PAY_NOT);

        condtion.addCondition("reday", "<>", "0");
    }

    /**
     * 删除库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delOut(ActionMapping mapping, ActionForm form,
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

        OutBean bean = outDAO.findOutById(fullId);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        if (bean.getStatus() == Constant.STATUS_SAVE || bean.getStatus() == Constant.STATUS_REJECT)
        {
            try
            {
                outManager.delOut(fullId);

                logger1.info(user.getName() + "删除了库单:" + fullId);

                request.setAttribute(KeyConstant.MESSAGE, "库单删除成功:" + fullId);
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
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有保存态,驳回态的库单才可以删除");

            return mapping.findForward("error");
        }

        request.setAttribute("forward", "10");

        if (user.getRole() != Role.ADMIN)
        {
            request.setAttribute("Bflagg", "0");
        }
        else
        {
            request.setAttribute("Bflagg", "1");
        }

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 处理调出的库单
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

        User user = (User)request.getSession().getAttribute("user");

        if (StringTools.isNullOrNone(fullId))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        OutBean bean = outDAO.findOutById(fullId);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        // 管理员通过和会计通过的都可以处理
        if ( ! ( (bean.getStatus() == Constant.STATUS_PASS || bean.getStatus() == Constant.STATUS_SEC_PASS) && bean.getOutType() == Constant.INBILL_OUT))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能转调，请核实");

            return mapping.findForward("error");
        }

        if (bean.getInway() != Constant.IN_WAY)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不在在途中，不能处理");

            return mapping.findForward("error");
        }

        // 直接接受自动生成一个调入的库单
        if ("1".equals(flag))
        {
            OutBean newOut = (OutBean)CommonTools.deepCopy(bean);

            newOut.setStatus(0);

            newOut.setLocationId(Helper.getCurrentLocationId(request));
            newOut.setLocation(Helper.getCurrentLocationId(request));

            newOut.setOutType(Constant.INBILL_IN);

            newOut.setFullId("");

            newOut.setRefOutFullId(fullId);

            newOut.setDestinationId("");

            newOut.setDescription("自动接收:" + fullId + ".生成的调入单据");

            newOut.setInway(0);

            newOut.setChecks("");

            newOut.setPay(Constant.PAY_NOT);

            newOut.setTotal( -newOut.getTotal());

            String depotpartId = request.getParameter("depotpartId");

            if (Constant.SYSTEM_LOCATION.equals(Helper.getCurrentLocationId(request)))
            {
                if (StringTools.isNullOrNone(depotpartId))
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "物流中心自动接收没有选择仓区");

                    return mapping.findForward("error");
                }

                newOut.setLocation(Constant.SYSTEM_LOCATION);
            }

            if ( !StringTools.isNullOrNone(depotpartId))
            {
                // 设置仓区
                newOut.setDepotpartId(depotpartId);
            }

            List<BaseBean> baseList = outDAO.queryBaseByOutFullId(fullId);

            for (BaseBean baseBean : baseList)
            {
                baseBean.setValue( -baseBean.getValue());
                baseBean.setLocationId(Helper.getCurrentLocationId(request));
                baseBean.setAmount( -baseBean.getAmount());
            }

            newOut.setBaseList(baseList);

            try
            {
                String ful = outManager.coloneOutAndSubmitAffair(newOut, user);

                request.setAttribute(KeyConstant.MESSAGE, fullId + "成功自动接收:" + ful);
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能自动接收，请核实:"
                                                                + e.getErrorContent());

                return mapping.findForward("error");
            }

        }

        // 转调处理
        if ("2".equals(flag))
        {
            String changeLocationId = request.getParameter("changeLocationId");

            if (bean.getLocation().equals(changeLocationId))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "转调区域不能是产品调出区域，请核实");

                return mapping.findForward("error");
            }

            LocationBean lb = locationManager.findLocationById(changeLocationId);

            if (lb == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "转调区域不存在，库单不能转调，请核实");

                return mapping.findForward("error");
            }
            bean.setDestinationId(changeLocationId);

            try
            {
                outManager.updateOut(bean);
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能转调，请核实:"
                                                                + e.getErrorContent());

                return mapping.findForward("error");
            }

            request.setAttribute(KeyConstant.MESSAGE, fullId + "成功转调至:" + lb.getLocationName());
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
                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                return mapping.findForward("error");
            }
        }

        request.setAttribute("forward", "10");

        if (user.getRole() != Role.ADMIN)
        {
            request.setAttribute("Bflagg", "0");
        }
        else
        {
            request.setAttribute("Bflagg", "1");
        }

        return queryOut(mapping, form, request, reponse);
    }

    public ActionForward mark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                              HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean bean = outDAO.findOutById(fullId);

        outManager.mark(fullId, !bean.isMark());

        request.setAttribute("forward", "9");

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 付款标记
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward checks(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");
        String checks = request.getParameter("checks");
        String pay = request.getParameter("pay");

        if ( !StringTools.isNullOrNone(checks))
        {
            OutBean out = outDAO.findOutById(fullId);
            if (out == null || !StringTools.isNullOrNone(out.getChecks()))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

                return mapping.findForward("error");
            }

            User user = (User)request.getSession().getAttribute("user");

            try
            {
                outManager.check(fullId, user, checks);
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

                return mapping.findForward("error");
            }
        }

        // 修改付款
        if ("1".equals(pay))
        {
            outManager.modifyPay(fullId, Integer.parseInt(pay));
        }

        CommonTools.saveParamers(request);
        request.setAttribute("forward", "10");

        return queryOut3(mapping, form, request, reponse);
    }

    /**
     * 修改库单的状态
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

        logger1.info(fullId + ":" + user.getStafferName() + ":" + statuss);

        CommonTools.saveParamers(request);

        OutBean out = null;

        out = outDAO.findOutById(fullId);

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

        // 入库单的提交
        if (out.getType() == Constant.OUT_TYPE_INBILL && statuss == Constant.STATUS_SUBMIT)
        {
            try
            {
                outManager.submit(out.getFullId(), user);

                request.setAttribute(KeyConstant.MESSAGE, out.getFullId() + "库单成功提交!");
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理异常：" + e.getErrorContent());

                return mapping.findForward("error");
            }
        }
        else
        {
            // 提交
            if (statuss == Constant.STATUS_SUBMIT)
            {
                try
                {
                    outManager.submit(fullId, user);
                }
                catch (MYException e)
                {
                    _logger.warn("fullId:" + fullId);

                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 总经理 物流管理员 库管通过
            if (statuss == Constant.STATUS_MANAGER_PASS || statuss == Constant.STATUS_FLOW_PASS
                || statuss == Constant.STATUS_PASS)
            {
                // 这里需要计算客户的信用金额-是否报送物流中心经理审批
                boolean outCredit = parameterDAO.getBoolean(SysConfigConstant.OUT_CREDIT);

                // 客户超支了(必须是销售单且是总经理通过这个环节设卡)
                if (outCredit && out.getReserve2() == OutConstanst.OUT_CREDIT_OVER
                    && out.getType() == Constant.OUT_TYPE_OUTBILL
                    && statuss == Constant.STATUS_MANAGER_PASS)
                {
                    // 总部的管理员
                    if ( !LocationHelper.isSystemLocation(user.getLocationID()))
                    {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有物流的总经理可以审批此销售单");

                        return mapping.findForward("error");
                    }
                }

                // 如果是黑名单的客户(且没有付款)
                if (outCredit && out.getReserve3() == OutConstanst.OUT_SAIL_TYPE_MONEY
                    && out.getType() == Constant.OUT_TYPE_OUTBILL && out.getPay() == 0)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有出纳确定已经回款后才可以审批此销售单");

                    return mapping.findForward("error");
                }

                try
                {
                    outManager.pass(fullId, user, statuss, reason, depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 驳回
            if (statuss == Constant.STATUS_REJECT)
            {
                try
                {
                    outManager.reject(fullId, user, reason);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }
        }

        request.setAttribute("forward", "10");

        request.setAttribute(KeyConstant.MESSAGE, "单据[" + fullId + "]操作成功");

        if (statuss == 1)
        {
            return queryOut(mapping, form, request, reponse);
        }

        return queryOut2(mapping, form, request, reponse);
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
        String outId = request.getParameter("outId");
        String fow = request.getParameter("fow");

        CommonTools.saveParamers(request);

        OutBean bean = null;
        try
        {
            bean = outDAO.findOutById(outId);

            if (bean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在,请重新操作");

                return mapping.findForward("error");
            }

            List<BaseBean> list = outDAO.queryBaseByOutFullId(outId);

            for (BaseBean baseBean : list)
            {
                if ( !StringTools.isNullOrNone(baseBean.getStorageId()))
                {
                    StorageBean sbs = storageDAO.findStorageById(baseBean.getStorageId());

                    if (sbs != null)
                    {
                        baseBean.setStorageId(sbs.getName());
                    }
                }
            }

            bean.setBaseList(list);

            OutBeanVO vo = new OutBeanVO();

            setOutVO(bean, vo);

            List<FlowLogBean> logs = outDAO.queryOutLogByFullId(outId);

            List<FlowLogBeanVO> voList = ListTools.changeList(logs, FlowLogBeanVO.class,
                FlowLogHelper.class, "getOutLogVO");

            request.setAttribute("out", vo);

            request.setAttribute("baseList", list);

            request.setAttribute("logList", voList);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");
            _logger.error("addOut", e);

            return mapping.findForward("error");
        }

        if ("1".equals(fow))
        {
            // 处理修改
            return processModify(mapping, request, bean);
        }

        if ("2".equals(fow))
        {
            Map out3Map = (Map)request.getSession().getAttribute("out3Map");

            if (out3Map != null)
            {
                out3Map.put("rIndex", new Integer(
                    CommonTools.parseInt(request.getParameter("index"))));
            }

            if ("2".equals(request.getParameter("flag")))
            {
                // 往来核对
                return mapping.findForward("detailOut3");
            }

            return mapping.findForward("detailOut2");
        }

        // 管理员的审核通过
        if ("3".equals(fow))
        {
            String locationId = Helper.getCurrentLocationId(request);

            ConditionParse condition = new ConditionParse();

            condition.addCondition("locationId", "=", locationId);

            condition.addIntCondition("type", "=", Constant.TYPE_DEPOTPART_OK);

            List<DepotpartBean> list = depotpartManager.queryDepotpartByCondition(condition);

            request.setAttribute("depotpartList", list);

            return mapping.findForward("detailOut4");
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
            if (bean.getInway() != Constant.IN_WAY)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不在在途中，不能处理");

                return mapping.findForward("error");
            }

            if (bean.getLocationId().equals(Helper.getCurrentLocationId(request)))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "不能操作自身区域的调拨");

                return mapping.findForward("error");
            }

            if ( !bean.getDestinationId().equals(Helper.getCurrentLocationId(request)))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "调出已经转到其他区域,没有权限处理");

                return mapping.findForward("error");
            }

            List<LocationBean> locationList = locationManager.listLocation();
            request.setAttribute("locationList", locationList);

            ConditionParse condition = new ConditionParse();

            condition.addWhereStr();

            condition.addIntCondition("type", "=", Constant.TYPE_DEPOTPART_OK);

            List<DepotpartBean> depotpartList = depotpartManager.queryDepotpartByCondition(condition);

            request.setAttribute("depotpartList", depotpartList);

            // 设置是否是系统区域
            request.setAttribute("isSystem",
                Constant.SYSTEM_LOCATION.equals(Helper.getCurrentLocationId(request)));

            return mapping.findForward("processOut");
        }

        return mapping.findForward("detailOut");
    }

    /**
     * @param mapping
     * @param request
     * @param bean
     * @return
     */
    private ActionForward processModify(ActionMapping mapping, HttpServletRequest request,
                                        OutBean bean)
    {
        if (bean.getStatus() == 0 || bean.getStatus() == 2)
        {
            User user = (User)request.getSession().getAttribute("user");

            ConditionParse condition = new ConditionParse();

            condition.addCondition("stafferName", "=", user.getStafferName());

            // 得到部门
            List<String> list2 = commonDAO.listAll("t_center_departement");

            List<LocationBean> locationList = new ArrayList<LocationBean>();

            if (LocationHelper.isSystemLocation(bean.getLocationId()))
            {
                locationList.addAll(locationManager.listLocation());
            }
            else
            {
                locationList.add(locationManager.findLocationById(Constant.SYSTEM_LOCATION));

                locationList.add(locationManager.findLocationById(Helper.getCurrentLocationId(request)));
            }

            request.setAttribute("locationList", locationList);

            request.setAttribute("departementList", list2);

            condition = new ConditionParse();

            User oprUser = Helper.getUser(request);

            condition.addCondition("locationId", "=", oprUser.getLocationID());

            List<DepotpartBean> list1 = depotpartDAO.queryDepotpartByCondition(condition);

            request.setAttribute("depotpartList", list1);

            if (bean.getType() == Constant.OUT_TYPE_INBILL)
            {
                return mapping.findForward("modifyOut1");
            }

            return mapping.findForward("modifyOut");
        }
        else
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有保存态和驳回态的出库单可以修改");

            return mapping.findForward("error");
        }
    }

    /**
     * 设置vo
     * 
     * @param bean
     * @param vo
     */
    private void setOutVO(OutBean bean, OutBeanVO vo)
    {
        BeanUtil.copyProperties(vo, bean);

        LocationBean plo = locationManager.findLocationById(vo.getLocation());

        if (plo != null)
        {
            vo.setLocationName(plo.getLocationName());
        }

        // 目的区域的名称
        if ( !StringTools.isNullOrNone(vo.getDestinationId())
            && vo.getOutType() == Constant.INBILL_OUT)
        {
            plo = locationManager.findLocationById(vo.getDestinationId());

            if (plo != null)
            {
                vo.setDestinationName(plo.getLocationName());
            }
        }

        if ( !StringTools.isNullOrNone(bean.getDepotpartId()))
        {
            DepotpartBean db = depotpartDAO.findDepotpartById(bean.getDepotpartId());

            if (db != null)
            {
                vo.setDepotpartName(db.getName());
            }
        }
    }

    private void setCondition3(HttpServletRequest request, ConditionParse condtion,
                               boolean userRequest)
    {
        CommonTools.saveParamers(request);

        Map<String, String> out3Map = null;
        if (userRequest)
        {
            out3Map = CommonTools.saveParamersToMap(request);

            out3Map.put("rIndex", "0");
            // 全局保存sec的查询
            request.getSession().setAttribute("out3Map", out3Map);
        }
        else
        {
            out3Map = (Map)request.getSession().getAttribute("out3Map");
        }

        String outTime = out3Map.get("outTime");
        String outTime1 = out3Map.get("outTime1");
        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCommonCondition("outTime", ">=", publicSQL.to_date(outTime, "yyyy-MM-dd"));

        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCommonCondition("outTime", "<=", publicSQL.to_date(outTime1, "yyyy-MM-dd"));

        }

        // String customerId = out3Map.get("customerId");

        String customerName = out3Map.get("customerName");

        if ( !StringTools.isNullOrNone(customerName))
        {
            condtion.addCondition("customerName", "like", customerName);
        }

        String id = out3Map.get("id");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("fullid", "like", id);
        }

        String type = out3Map.get("type");

        if ( !StringTools.isNullOrNone(type))
        {
            condtion.addIntCondition("type", "=", type);
        }

        String checks = out3Map.get("checks");

        if ( !StringTools.isNullOrNone(checks))
        {
            // 0:没有check 1:check
            if ("0".equals(checks))
            {
                condtion.addCondition("and checks = ''");
            }
            else
            {
                condtion.addCondition("and checks <> ''");
            }
        }

        String stafferName = request.getParameter("stafferName");

        if ( !StringTools.isNullOrNone(stafferName))
        {
            condtion.addCondition("stafferName", "like", stafferName);
        }

        String redate = out3Map.get("redate");
        String reCom = out3Map.get("reCom");

        if ( !StringTools.isNullOrNone(redate) && !StringTools.isNullOrNone(reCom))
        {
            condtion.addCondition("redate", reCom, redate);

            condtion.addCondition("and redate <> ''");
        }

        String pay = out3Map.get("pay");

        if ( !StringTools.isNullOrNone(pay))
        {
            condtion.addIntCondition("pay", "=", pay);
            condtion.addCondition("and redate <> ''");
        }
    }

    private void setCondition(HttpServletRequest request, ConditionParse condtion)
    {
        CommonTools.saveParamers(request);

        String outTime = request.getParameter("outTime");
        String outTime1 = request.getParameter("outTime1");
        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCondition("outTime", ">=", outTime);
        }
        else
        {
            condtion.addCondition("outTime", ">=", TimeTools.now_short());
            request.setAttribute("outTime", TimeTools.now_short());
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCondition("outTime", "<=", outTime1);
        }
        else
        {
            condtion.addCondition("outTime", "<=", TimeTools.now_short());

            request.setAttribute("outTime1", TimeTools.now_short());
        }

        String id = request.getParameter("id");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("fullid", "like", id);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            if (String.valueOf(Constant.STATUS_PASS).equals(status))
            {
                condtion.addCondition(" and status in (3, 4) ");
            }
            else
            {
                condtion.addIntCondition("status", "=", status);
            }
        }
        else
        {
            request.setAttribute("status", null);
        }

        String customerName = request.getParameter("customerId");

        if ( !StringTools.isNullOrNone(customerName))
        {
            condtion.addCondition("customerId", "=", customerName);
        }

        String outType = request.getParameter("outType");

        if ( !StringTools.isNullOrNone(outType))
        {
            condtion.addIntCondition("outType", "=", outType);
        }
        else
        {
            // condtion.addIntCondition("outType", "=",
            // Constant.OUT_TYPE_OUTBILL);
        }

        String stafferName = request.getParameter("stafferName");

        if ( !StringTools.isNullOrNone(stafferName))
        {
            condtion.addCondition("stafferName", "like", stafferName);
        }

        String redate = request.getParameter("redate");
        String reCom = request.getParameter("reCom");

        if ( !StringTools.isNullOrNone(redate) && !StringTools.isNullOrNone(reCom))
        {
            condtion.addCondition("redate", reCom, redate);

            condtion.addCondition("and redate <> ''");
        }

        String pay = request.getParameter("pay");

        if ( !StringTools.isNullOrNone(pay))
        {
            if ( !pay.equals(String.valueOf(Constant.PAY_OVER)))
            {
                condtion.addIntCondition("pay", "=", pay);
            }
            else
            {
                condtion.addCondition(" and status in (3, 4)");

                condtion.addCondition(" and redate < '" + TimeTools.now_short() + "'");

                condtion.addIntCondition("pay", "=", 0);
            }
        }

        String tempType = request.getParameter("tempType");
        if ( !StringTools.isNullOrNone(tempType))
        {
            condtion.addIntCondition("tempType", "=", tempType);
        }

        String inway = request.getParameter("inway");
        if ( !StringTools.isNullOrNone(inway))
        {
            condtion.addIntCondition("inway", "=", inway);
        }

    }

    private void getDivs(HttpServletRequest request, List<OutBean> list)
    {
        Map divMap = new HashMap();

        if (list != null)
        {
            for (OutBean bean : list)
            {
                try
                {
                    List<BaseBean> baseList = outDAO.queryBaseByOutFullId(bean.getFullId());

                    divMap.put(bean.getFullId(), OutBeanHelper.createTable(baseList,
                        bean.getTotal()));
                }
                catch (Exception e)
                {
                    _logger.error("addOut", e);
                }
            }
        }

        request.setAttribute("divMap", divMap);
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
     * @return the jbpmConfiguration
     */
    public JbpmConfiguration getJbpmConfiguration()
    {
        return jbpmConfiguration;
    }

    /**
     * @param jbpmConfiguration
     *            the jbpmConfiguration to set
     */
    public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration)
    {
        this.jbpmConfiguration = jbpmConfiguration;
    }

    /**
     * @return the flowManager
     */
    public FlowManager getFlowManager()
    {
        return flowManager;
    }

    /**
     * @param flowManager
     *            the flowManager to set
     */
    public void setFlowManager(FlowManager flowManager)
    {
        this.flowManager = flowManager;
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
     * @return the depotpartManager
     */
    public DepotpartManager getDepotpartManager()
    {
        return depotpartManager;
    }

    /**
     * @param depotpartManager
     *            the depotpartManager to set
     */
    public void setDepotpartManager(DepotpartManager depotpartManager)
    {
        this.depotpartManager = depotpartManager;
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
     * @return the stafferDAO2
     */
    public StafferDAO2 getStafferDAO2()
    {
        return stafferDAO2;
    }

    /**
     * @param stafferDAO2
     *            the stafferDAO2 to set
     */
    public void setStafferDAO2(StafferDAO2 stafferDAO2)
    {
        this.stafferDAO2 = stafferDAO2;
    }

}
