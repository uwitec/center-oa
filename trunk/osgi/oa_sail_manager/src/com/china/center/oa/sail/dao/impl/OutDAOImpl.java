/**
 * File Name: OutDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.IbatisDaoSupport;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.vs.StorageRelationBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.vo.OutVO;
import com.china.center.oa.sail.wrap.CreditWrap;
import com.china.center.tools.TimeTools;


/**
 * OutDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see OutDAOImpl
 * @since 1.0
 */
public class OutDAOImpl extends BaseDAO<OutBean, OutVO> implements OutDAO
{
    private IbatisDaoSupport ibatisDaoSupport = null;

    /**
     * default constructor
     */
    public OutDAOImpl()
    {
    }

    public boolean mark(String fullId, boolean status)
    {
        int i = jdbcOperation.updateField("mark", status, fullId, this.claz);

        return i != 0;
    }

    public boolean modifyChecks(String fullId, String checks)
    {
        String sql = "update t_center_out set checks = ?, checkStatus = ? where fullid = ?";

        jdbcOperation.update(sql, checks, PublicConstant.CHECK_STATUS_END, fullId);

        return true;
    }

    public boolean modifyData(String fullId, String date)
    {
        jdbcOperation.updateField("outTime", date, fullId, this.claz);

        return true;
    }

    public boolean modifyManagerTime(String fullId, String managerTime)
    {
        jdbcOperation.updateField("managerTime", managerTime, fullId, this.claz);

        return true;
    }

    public boolean modifyOutHadPay(String fullId, double hadPay)
    {
        jdbcOperation.updateField("hadPay", hadPay, fullId, this.claz);

        return true;
    }

    public boolean modifyBadDebts(String fullId, double badDebts)
    {
        jdbcOperation.updateField("badDebts", badDebts, fullId, this.claz);

        return true;
    }

    public boolean modifyOutStatus(String fullId, int status)
    {
        jdbcOperation.updateField("status", status, fullId, this.claz);

        return true;
    }

    public boolean modifyPay(String fullId, int pay)
    {
        String sql = "update t_center_out set pay = ?, redate = ? where fullid = ?";

        int i = jdbcOperation.update(sql, pay, TimeTools.now_short(), fullId);

        return i != 0;
    }

    public boolean updatePay(String fullId, int pay)
    {
        String sql = "update t_center_out set pay = ? where fullid = ?";

        int i = jdbcOperation.update(sql, pay, fullId);

        return i != 0;
    }

    public boolean modifyReDate(String fullId, String reDate)
    {
        jdbcOperation.updateField("redate", reDate, fullId, this.claz);

        return true;
    }

    public boolean modifyTempType(String fullId, int tempType)
    {
        jdbcOperation.updateField("tempType", tempType, fullId, this.claz);

        return true;
    }

    /**
     * 自己开单占用
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    public double sumNoPayAndAvouchBusinessByStafferId(String stafferId, String industryId,
                                                       String beginDate, String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("stafferId", stafferId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("industryId", industryId);
        paramterMap.put("endDate", endDate);

        Object max = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNoPayAndAvouchBusinessByStafferId", paramterMap);

        if (max == null)
        {
            return 0.0d;
        }

        return (Double)max;
    }

    public List<CreditWrap> queryNoPayAndAvouchBusinessByStafferId(String stafferId,
                                                                   String industryId,
                                                                   String beginDate, String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("stafferId", stafferId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("industryId", industryId);
        paramterMap.put("endDate", endDate);

        List<CreditWrap> result = getIbatisDaoSupport().queryForList(
            "OutDAO.queryNoPayAndAvouchBusinessByStafferId", paramterMap);

        return result;
    }

    public List<CreditWrap> queryNoPayAndAvouchBusinessByManagerId(String stafferId,
                                                                   String industryId,
                                                                   String beginDate, String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("stafferId", stafferId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("industryId", industryId);
        paramterMap.put("endDate", endDate);

        List<CreditWrap> result = getIbatisDaoSupport().queryForList(
            "OutDAO.queryNoPayAndAvouchBusinessByManagerId", paramterMap);

        return result;
    }

    public List<CreditWrap> queryAllNoPay(String stafferId, String industryId, String beginDate,
                                          String endDate)
    {
        List<CreditWrap> list = queryNoPayAndAvouchBusinessByStafferId(stafferId, industryId,
            beginDate, endDate);

        list.addAll(queryNoPayAndAvouchBusinessByManagerId(stafferId, industryId, beginDate,
            endDate));

        return list;
    }

    public double sumAllNoPayAndAvouchBusinessByStafferId(String stafferId, String industryId,
                                                          String beginDate, String endDate)
    {
        return sumNoPayAndAvouchBusinessByStafferId(stafferId, industryId, beginDate, endDate)
               + sumNoPayAndAvouchBusinessByManagerId(stafferId, industryId, beginDate, endDate);
    }

    public double sumNoPayAndAvouchBusinessByManagerId(String stafferId, String industryId,
                                                       String beginDate, String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("stafferId", stafferId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("industryId", industryId);
        paramterMap.put("endDate", endDate);

        Object max = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNoPayAndAvouchBusinessByManagerId", paramterMap);

        if (max == null)
        {
            return 0.0d;
        }

        return (Double)max;
    }

    public double sumNoPayAndAvouchBusinessByManagerId2(String stafferId, String beginDate,
                                                        String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("stafferId", stafferId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object max = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNoPayAndAvouchBusinessByManagerId2", paramterMap);

        if (max == null)
        {
            return 0.0d;
        }

        return (Double)max;
    }

    public double sumNoPayBusiness(String cid, String beginDate, String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("customerId", cid);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object max = getIbatisDaoSupport().queryForObject("OutDAO.sumNoPayBusiness", paramterMap);

        if (max == null)
        {
            return 0.0d;
        }

        return (Double)max;
    }

    public Integer sumPreassignAmount(Map parMap)
    {
        Integer result = (Integer)ibatisDaoSupport.queryForObject("OutDAO.sumPreassignAmount",
            parMap);

        if (result == null)
        {
            return 0;
        }

        return result;

    }

    public Integer countNotEndProductInIn(String productId, String beginDate, String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("productId", productId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object count = getIbatisDaoSupport().queryForObject("OutDAO.countNotEndProductInIn",
            paramterMap);

        if (count == null)
        {
            return 0;
        }

        return (Integer)count;
    }

    public Integer sumNotEndProductInIn2(StorageRelationBean relation, String beginDate,
                                         String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("productId", relation.getProductId());
        paramterMap.put("depotpartId", relation.getDepotpartId());
        paramterMap.put("costPriceKey", relation.getPriceKey());
        paramterMap.put("owner", relation.getStafferId());

        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object count = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNotEndProductInInByStorageRelation", paramterMap);

        if (count == null)
        {
            return 0;
        }

        return -(Integer)count;
    }

    public Integer sumInwayProductInBuy(StorageRelationBean relation, String beginDate,
                                        String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("productId", relation.getProductId());
        paramterMap.put("depotpartId", relation.getDepotpartId());
        paramterMap.put("costPriceKey", relation.getPriceKey());
        paramterMap.put("owner", relation.getStafferId());

        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object count = getIbatisDaoSupport().queryForObject("OutDAO.sumInwayProductInBuy",
            paramterMap);

        if (count == null)
        {
            return 0;
        }

        return (Integer)count;
    }

    public Integer sumNotEndProductInOut2(StorageRelationBean relation, String beginDate,
                                          String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("productId", relation.getProductId());
        paramterMap.put("depotpartId", relation.getDepotpartId());
        paramterMap.put("costPriceKey", relation.getPriceKey());
        paramterMap.put("owner", relation.getStafferId());

        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object count = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNotEndProductInOutByStorageRelation", paramterMap);

        if (count == null)
        {
            return 0;
        }

        return (Integer)count;
    }

    public Integer countNotEndProductInOut(String productId, String beginDate, String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("productId", productId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object count = getIbatisDaoSupport().queryForObject("OutDAO.countNotEndProductInOut",
            paramterMap);

        if (count == null)
        {
            return 0;
        }

        return (Integer)count;
    }

    public Integer sumNotEndProductInInByStorageRelation(String productId, String depotpartId,
                                                         String priceKey, String ower)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("productId", productId);
        paramterMap.put("depotpartId", depotpartId);
        paramterMap.put("costPriceKey", priceKey);
        paramterMap.put("owner", ower);
        paramterMap.put("beginDate", TimeTools.getDateShortString( -180));
        paramterMap.put("endDate", TimeTools.now_short());

        Object count = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNotEndProductInInByStorageRelation", paramterMap);

        if (count == null)
        {
            return 0;
        }

        // 负数
        return -(Integer)count;
    }

    public Integer sumNotEndProductInOutByStorageRelation(String productId, String depotpartId,
                                                          String priceKey, String ower)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("productId", productId);
        paramterMap.put("depotpartId", depotpartId);
        paramterMap.put("costPriceKey", priceKey);
        paramterMap.put("owner", ower);
        paramterMap.put("beginDate", TimeTools.getDateShortString( -180));
        paramterMap.put("endDate", TimeTools.now_short());

        Object count = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNotEndProductInOutByStorageRelation", paramterMap);

        if (count == null)
        {
            return 0;
        }

        return (Integer)count;
    }

    public List<BaseBean> queryInwayOut()
    {
        String sql = "select t1.* from t_center_base t1, t_center_out t2 "
                     + "where t1.outid = t2.fullid and t2.inway = 1 and t2.status in (3, 4) and t2.type = 1 and t2.outType = 1 and t2.outTime > ?";

        return this.jdbcOperation.queryForListBySql(sql, BaseBean.class, "2011-04-01");
    }

    public boolean updataInWay(String fullId, int inway)
    {
        jdbcOperation.updateField("inway", inway, fullId, this.claz);

        return true;
    }

    public boolean updataBadDebtsCheckStatus(String fullId, int badDebtsCheckStatus)
    {
        jdbcOperation.updateField("badDebtsCheckStatus", badDebtsCheckStatus, fullId, this.claz);

        return true;
    }

    public boolean updateCurcredit(String fullId, double curcredit)
    {
        jdbcOperation.updateField("curcredit", curcredit, fullId, this.claz);

        return true;
    }

    public boolean updateManagerId(String fullId, String managerId)
    {
        jdbcOperation.updateField("managerId", managerId, fullId, this.claz);

        return true;
    }

    public boolean updateHadPay(String fullId, double hadPay)
    {
        jdbcOperation.updateField("hadPay", hadPay, fullId, this.claz);

        return true;
    }

    public boolean updateChangeTime(String fullId, String changeTime)
    {
        jdbcOperation.updateField("changeTime", changeTime, fullId, this.claz);

        return true;
    }

    public boolean updateInvoice(String fullId, String invoiceId)
    {
        String sql = BeanTools.getUpdateHead(claz)
                     + "set invoiceId = ?, hasInvoice = ? where fullid = ?";

        jdbcOperation.update(sql, invoiceId, OutConstant.HASINVOICE_YES, fullId);

        return true;
    }

    public boolean updateInvoiceStatus(String fullId, double invoiceMoney, int invoiceStatus)
    {
        String sql = BeanTools.getUpdateHead(claz)
                     + "set invoiceMoney = ?, invoiceStatus = ? where fullid = ?";

        jdbcOperation.update(sql, invoiceMoney, invoiceStatus, fullId);

        return true;
    }

    public boolean updateOutReserve(String fullId, int reserve4, String reserve6)
    {
        jdbcOperation.updateField("reserve2", reserve4, fullId, this.claz);

        jdbcOperation.updateField("reserve6", reserve6, fullId, this.claz);

        return true;
    }

    public boolean updateStaffcredit(String fullId, double staffcredit)
    {
        jdbcOperation.updateField("staffcredit", staffcredit, fullId, this.claz);

        return true;
    }

    public boolean updateManagercredit(String fullId, String managerId, double managercredit)
    {
        jdbcOperation.updateField("managercredit", managercredit, fullId, this.claz);

        jdbcOperation.updateField("managerId", managerId, fullId, this.claz);

        return true;
    }

    /**
     * 在out里面统计客户的使用
     * 
     * @param id
     * @return
     */
    public int countCustomerInOut(String id)
    {
        return this.jdbcOperation.queryForInt(
            BeanTools.getCountHead(claz) + "where customerId = ?", id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.dao.OutDAO#findRealOut(java.lang.String)
     */
    public OutBean findRealOut(String fullId)
    {
        Connection connection = null;

        PreparedStatement prepareStatement = null;

        ResultSet rst = null;

        try
        {
            connection = this.jdbcOperation.getDataSource().getConnection();

            prepareStatement = connection
                .prepareStatement("select fullid, status, reserve3, type from t_center_out where fullid = '"
                                  + fullId + "'");

            rst = prepareStatement.executeQuery();

            rst.next();

            OutBean out = new OutBean();

            out.setFullId(rst.getString("fullid"));

            out.setStatus(rst.getInt("status"));

            out.setReserve3(rst.getInt("Reserve3"));

            out.setType(rst.getInt("type"));

            return out;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                }
            }

            if (prepareStatement != null)
            {
                try
                {
                    prepareStatement.close();
                }
                catch (SQLException e)
                {
                }
            }

            if (rst != null)
            {
                try
                {
                    rst.close();
                }
                catch (SQLException e)
                {
                }
            }
        }
    }

    /**
     * @return the ibatisDaoSupport
     */
    public IbatisDaoSupport getIbatisDaoSupport()
    {
        return ibatisDaoSupport;
    }

    /**
     * @param ibatisDaoSupport
     *            the ibatisDaoSupport to set
     */
    public void setIbatisDaoSupport(IbatisDaoSupport ibatisDaoSupport)
    {
        this.ibatisDaoSupport = ibatisDaoSupport;
    }

    public double sumOutBackValue(String fullId)
    {
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", fullId);

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        con.addCondition("and OutBean.status in (3, 4)");

        con.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_OUTBACK);

        List<OutBean> refList = this.queryEntityBeansByCondition(con);

        double backTotal = 0.0d;

        for (OutBean outBean : refList)
        {
            backTotal += outBean.getTotal();
        }

        return backTotal;
    }

}
