/**
 * File Name: OutDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao.impl;


import java.util.HashMap;
import java.util.Map;

import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.IbatisDaoSupport;
import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.vo.OutVO;
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

    public boolean mark2(String fullId, boolean status)
    {
        int i = jdbcOperation.updateField("mark", status, fullId, this.claz);

        return i != 0;
    }

    public boolean modifyChecks(String fullId, String checks)
    {
        int i = jdbcOperation.updateField("checks", checks, fullId, this.claz);

        return i != 0;
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

    public boolean modifyOutHadPay2(String fullId, String hadPay)
    {
        jdbcOperation.updateField("hadPay", hadPay, fullId, this.claz);

        return true;
    }

    public boolean modifyOutStatus2(String fullId, int status)
    {
        jdbcOperation.updateField("status", status, fullId, this.claz);

        return true;
    }

    public boolean modifyPay2(String fullId, int pay)
    {
        jdbcOperation.updateField("pay", pay, fullId, this.claz);

        return true;
    }

    public boolean modifyReDate2(String fullId, String reDate)
    {
        jdbcOperation.updateField("redate", reDate, fullId, this.claz);

        return true;
    }

    public boolean modifyTempType(String fullId, int tempType)
    {
        jdbcOperation.updateField("tempType", tempType, fullId, this.claz);

        return true;
    }

    public double sumNoPayAndAvouchBusinessByStafferId(String stafferId, String beginDate,
                                                       String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("stafferId", stafferId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object max = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNoPayAndAvouchBusinessByStafferId", paramterMap);

        if (max == null)
        {
            return 0.0d;
        }

        return (Double)max;
    }

    public double sumNoPayAndAvouchBusinessByManagerId(String stafferId, String beginDate,
                                                       String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("stafferId", stafferId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object max = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNoPayAndAvouchBusinessByManagerId", paramterMap);

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
        Integer result = (Integer)jdbcOperation.getIbatisDaoSupport().queryForObject(
            "OutDAO.sumPreassignAmount", parMap);

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
        paramterMap.put("beginDate", TimeTools.getDateShortString( -365));
        paramterMap.put("endDate", TimeTools.now_short());

        Object count = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNotEndProductInInByStorageRelation", paramterMap);

        if (count == null)
        {
            return 0;
        }

        return (Integer)count;
    }

    public Integer sumNotEndProductInOutByStorageRelation(String productId, String depotpartId,
                                                          String priceKey, String ower)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("productId", productId);
        paramterMap.put("depotpartId", depotpartId);
        paramterMap.put("costPriceKey", priceKey);
        paramterMap.put("owner", ower);
        paramterMap.put("beginDate", TimeTools.getDateShortString( -365));
        paramterMap.put("endDate", TimeTools.now_short());

        Object count = getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNotEndProductInOutByStorageRelation", paramterMap);

        if (count == null)
        {
            return 0;
        }

        return (Integer)count;
    }

    public boolean updataInWay(String fullId, int inway)
    {
        jdbcOperation.updateField("inway", inway, fullId, this.claz);

        return true;
    }

    public boolean updateCurcredit(String fullId, double curcredit)
    {
        jdbcOperation.updateField("curcredit", curcredit, fullId, this.claz);

        return true;
    }

    public boolean updateOutReserve2(String fullId, int reserve4, String reserve6)
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

    public boolean updateManagercredit(String fullId, double managercredit)
    {
        jdbcOperation.updateField("managercredit", managercredit, fullId, this.claz);

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
}
