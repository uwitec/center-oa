/**
 * File Name: ReportsAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-2-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.action;


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
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.bean.StorageBean;
import com.china.center.oa.product.bean.StorageLogBean;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.StorageDAO;
import com.china.center.oa.product.dao.StorageLogDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.vo.StorageRelationVO;
import com.china.center.oa.product.wrap.StatProductBean;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * 报表
 * 
 * @author ZHUZHU
 * @version 2008-2-18
 * @see
 * @since
 */
public class ReportsAction extends DispatchAction
{
    private StorageDAO storageDAO = null;

    private StorageLogDAO storageLogDAO = null;

    private ProductDAO productDAO = null;

    private StorageRelationDAO storageRelationDAO = null;

    private DepotpartDAO depotpartDAO = null;

    /**
     * default constructor
     */
    public ReportsAction()
    {
    }

    /**
     * 提供盘点报表，处理当天的异动的产品数量。<br>
     * 提供报表时需要分仓位提供报表，报表结构：储位、品名、原始数量、异动数量，<br>
     * 当前数量（查询条件时间，仓区，盘点属性）
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward statReports(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condition = new ConditionParse();

        setCondition(request, condition);

        // 获取指定时间内的仓区的异动
        List<StorageLogBean> logs = storageLogDAO.queryStorageLogByCondition(condition);

        List<StatProductBean> statList = statProduct(logs, request);

        request.getSession().setAttribute("statList", statList);

        List<DepotpartBean> list = depotpartDAO.listEntityBeans();

        request.setAttribute("depotpartList", list);

        return mapping.findForward("queryReports");
    }

    /**
     * 查询仓区的
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryForStatReports(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<DepotpartBean> list = depotpartDAO.listEntityBeans();

        request.setAttribute("depotpartList", list);

        request.getSession().removeAttribute("statList");

        return mapping.findForward("queryReports");
    }

    /**
     * listStorageLog
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward listStorageLog(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        Map<String, List<StorageLogBean>> map = (Map<String, List<StorageLogBean>>)request
            .getSession()
            .getAttribute("logsMap");

        String productId = request.getParameter("productId");

        List<StorageLogBean> lits = map.get(productId);

        List<StatProductBean> statList = new ArrayList<StatProductBean>();

        for (StorageLogBean storageLogBean : lits)
        {
            StatProductBean bean = new StatProductBean();

            BeanUtil.copyProperties(bean, storageLogBean);

            getProductReprot(bean);

            statList.add(bean);
        }

        request.setAttribute("listStorageLog", statList);

        return mapping.findForward("listStorageLog");
    }

    /**
     * listStorageLog2
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward listStorageLog2(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        ConditionParse condition = new ConditionParse();

        condition.addCondition("and t1.productId = t2.id");

        String productId = request.getParameter("productId");

        String depotpartId = request.getParameter("depotpartId");

        condition.addCondition("t1.productId", "=", productId);

        condition.addCondition("t1.depotpartId", "=", depotpartId);

        condition.addCondition("order by t1.logTime");

        // 获取指定时间内的仓区的异动
        List<StorageLogBean> lits = storageLogDAO.queryStorageLogByCondition(condition);

        List<StatProductBean> statList = new ArrayList<StatProductBean>();

        for (StorageLogBean storageLogBean : lits)
        {
            StatProductBean bean = new StatProductBean();

            BeanUtil.copyProperties(bean, storageLogBean);

            getProductReprot(bean);

            statList.add(bean);
        }

        request.setAttribute("listStorageLog", statList);

        return mapping.findForward("listStorageLog");
    }

    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        List<StatProductBean> statList = (List<StatProductBean>)request.getSession().getAttribute(
            "statList");

        String filenName = TimeTools.now("MMddHHmmss") + ".xls";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;
        WritableSheet ws = null;

        try
        {
            out = reponse.getOutputStream();

            wwb = Workbook.createWorkbook(out);
            ws = wwb.createSheet("sheel1", 0);
            int i = 0, j = 0;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableCellFormat format = new WritableCellFormat(font);

            ws.addCell(new Label(j++ , i, "仓区", format));
            ws.addCell(new Label(j++ , i, "储位", format));
            ws.addCell(new Label(j++ , i, "产品", format));
            ws.addCell(new Label(j++ , i, "原始数量", format));
            ws.addCell(new Label(j++ , i, "异动数量", format));
            ws.addCell(new Label(j++ , i, "当前数量", format));

            // NumberFormat nf = new NumberFormat("###,##0.00");
            NumberFormat nf = new NumberFormat("###,##0");

            jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);

            for (StatProductBean statProductBean : statList)
            {
                j = 0;
                i++ ;

                ws.addCell(new Label(j++ , i, statProductBean.getDepotpartName()));
                ws.addCell(new Label(j++ , i, statProductBean.getStorageName()));
                ws.addCell(new Label(j++ , i, statProductBean.getProductName()));
                ws.addCell(new jxl.write.Number(j++ , i, statProductBean.getPreAmount(), wcfN));
                ws.addCell(new jxl.write.Number(j++ , i, statProductBean.getChangeAmount(), wcfN));
                ws.addCell(new jxl.write.Number(j++ , i, statProductBean.getCurrentAmount(), wcfN));
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

    public ActionForward exportAll(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        List<StorageRelationVO> statList = storageRelationDAO.queryStorageRelationByCondition(
            new ConditionParse(), false);

        String filenName = TimeTools.now("MMddHHmmss") + "_ALL.xls";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;

        WritableSheet ws = null;

        try
        {
            out = reponse.getOutputStream();

            wwb = Workbook.createWorkbook(out);
            ws = wwb.createSheet(TimeTools.now("MMddHHmmss") + "全部盘点", 0);
            int i = 0, j = 0;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableCellFormat format = new WritableCellFormat(font);

            ws.addCell(new Label(j++ , i, "仓区", format));
            ws.addCell(new Label(j++ , i, "储位", format));
            ws.addCell(new Label(j++ , i, "产品", format));
            ws.addCell(new Label(j++ , i, "产品编码", format));
            ws.addCell(new Label(j++ , i, "数量", format));

            for (StorageRelationVO statProductBean : statList)
            {
                j = 0;
                i++ ;

                ws.addCell(new Label(j++ , i, statProductBean.getDepotpartName()));
                ws.addCell(new Label(j++ , i, statProductBean.getStorageName()));
                ws.addCell(new Label(j++ , i, statProductBean.getProductName()));
                ws.addCell(new Label(j++ , i, statProductBean.getProductCode()));
                ws.addCell(new Label(j++ , i, String.valueOf(statProductBean.getAmount())));
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
     * 统计
     * 
     * @param logs
     */
    private List<StatProductBean> statProduct(List<StorageLogBean> logs, HttpServletRequest request)
    {
        Map<String, List<StorageLogBean>> map = new HashMap<String, List<StorageLogBean>>();

        // 把分类统计的产品放到map里面[仓区+product:Logs]
        for (StorageLogBean storageLogBean : logs)
        {
            String key = storageLogBean.getProductId() + ";" + storageLogBean.getDepotpartId();

            if ( !map.containsKey(key))
            {
                map.put(key, new ArrayList<StorageLogBean>());
            }

            map.get(key).add(storageLogBean);
        }

        // 放入session方便查询
        request.getSession().setAttribute("logsMap", map);

        // 统计
        return statInner(map);
    }

    /**
     * 统计开始
     * 
     * @param map
     * @return
     */
    private List<StatProductBean> statInner(Map<String, List<StorageLogBean>> map)
    {
        List<StatProductBean> statList = new ArrayList<StatProductBean>();

        List<StorageLogBean> temp = null;

        // 统计每个产品
        for (Map.Entry<String, List<StorageLogBean>> entry : map.entrySet())
        {
            StatProductBean bean = new StatProductBean();

            temp = entry.getValue();

            boolean frist = true;
            int init = 0, change = 0;
            for (StorageLogBean log : temp)
            {
                if (frist)
                {
                    init = log.getPreAmount();

                    BeanUtil.copyProperties(bean, log);

                    frist = false;
                }

                change += log.getChangeAmount();
            }

            bean.setPreAmount(init);

            bean.setChangeAmount(change);

            // 获得当前的数量
            int sum = storageRelationDAO.sumProductInDepotpartId(bean.getProductId(), bean
                .getDepotpartId());

            bean.setCurrentAmount(sum);

            getProductReprot(bean);

            statList.add(bean);
        }

        return statList;
    }

    /**
     * @param bean
     */
    private void getProductReprot(StatProductBean bean)
    {
        // 获得产品
        ProductBean pp = productDAO.find(bean.getProductId());

        if (pp != null)
        {
            bean.setProductName(pp.getName());
        }

        StorageBean sb = storageDAO.find(bean.getStorageId());
        if (sb != null)
        {
            bean.setStorageName(sb.getName());
        }

        DepotpartBean db = depotpartDAO.find(bean.getDepotpartId());

        if (db != null)
        {
            bean.setDepotpartName(db.getName());
        }
    }

    private void setCondition(HttpServletRequest request, ConditionParse condition)
    {
        String beginDate = request.getParameter("beginDate");

        condition.addCondition("and t1.productId = t2.id");

        if ( !StringTools.isNullOrNone(beginDate))
        {
            condition.addCondition("t1.logTime", ">=", beginDate + " 00:00:00");
        }

        String endDate = request.getParameter("endDate");

        if ( !StringTools.isNullOrNone(endDate))
        {
            condition.addCondition("t1.logTime", "<=", endDate + " 23:59:59");
        }

        String depotpartId = request.getParameter("depotpartId");

        if ( !StringTools.isNullOrNone(depotpartId))
        {
            condition.addCondition("depotpartId", "=", depotpartId);
        }

        condition.addCondition("order by t1.id");
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
     * @return the storageLogDAO
     */
    public StorageLogDAO getStorageLogDAO()
    {
        return storageLogDAO;
    }

    /**
     * @param storageLogDAO
     *            the storageLogDAO to set
     */
    public void setStorageLogDAO(StorageLogDAO storageLogDAO)
    {
        this.storageLogDAO = storageLogDAO;
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

}
