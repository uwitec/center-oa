package com.china.centet.yongyin.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.util.PageSeparate;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.FlowLogBean;
import com.china.centet.yongyin.bean.OutBean;


/**
 * 〈一句话功能简述〉
 * 
 * @author zhu
 * @version 2006-7-16
 * @see OutDAO
 * @since
 */

public class OutDAO
{
    private JdbcOperation jdbcOperation2 = null;

    /**
     * 需要事务提交
     * 
     * @param outBean
     * @return
     */
    public boolean addOut(final OutBean outBean)
    {
        return jdbcOperation2.save(outBean) > 0;
    }

    public boolean updateOut(final OutBean outBean)
    {
        return jdbcOperation2.update(outBean) > 0;
    }

    public boolean addOutLog(final FlowLogBean bean)
    {
        return jdbcOperation2.save(bean) > 0;
    }

    public boolean delOutLogByFullId(final String fullId)
    {
        return jdbcOperation2.delete(fullId, "fullId", FlowLogBean.class) > 0;
    }

    /**
     * 需要事务提交
     * 
     * @param baseBean
     * @return
     */
    public boolean addBase(final BaseBean baseBean)
    {
        return jdbcOperation2.save(baseBean) > 0;
    }

    public List<BaseBean> queryBaseByOutFullId(final String id)
    {
        return jdbcOperation2.queryForList("where outId = ?", BaseBean.class, id);
    }

    public List<FlowLogBean> queryOutLogByFullId(final String id)
    {
        return jdbcOperation2.queryForList("where fullId = ? order by id", FlowLogBean.class, id);
    }

    public OutBean findOutById(final String id)
    {
        return jdbcOperation2.find(id, OutBean.class);
    }

    public boolean delOutById2(final String id)
    {
        jdbcOperation2.delete(id, OutBean.class);

        return true;
    }

    public boolean delOutBaseById2(final String id)
    {
        jdbcOperation2.delete(id, "outId", BaseBean.class);

        return true;
    }

    public List<OutBean> queryOutBeanByCondtion(ConditionParse condtion)
    {
        condtion.addWhereStr();

        if (condtion.toString().toLowerCase().indexOf("order") == -1)
        {
            condtion.addCondition(" order by id desc");
        }

        return jdbcOperation2.queryForList(condtion.toString(), OutBean.class);
    }

    public List<OutBean> queryOutBeanByCondtion(ConditionParse condtion, PageSeparate page)
    {
        condtion.addWhereStr();

        return jdbcOperation2.queryObjectsByPageSeparate(condtion.toString(), page, OutBean.class);
    }

    public int countOutBeanByCondtion(ConditionParse condtion)
    {
        condtion.addWhereStr();

        return jdbcOperation2.queryObjects(condtion.toString(), OutBean.class).getCount();
    }

    public int countOutBeanByCondtion2(ConditionParse condtion)
    {
        condtion.addWhereStr();

        return jdbcOperation2.queryObjects(condtion.toString(), OutBean.class).getCount();
    }

    public boolean modifyOutStatus2(String fullId, int status)
    {
        int i = jdbcOperation2.updateField("status", status, fullId, OutBean.class);

        return i != 0;
    }

    public boolean modifyManagerTime(String fullId, String managerTime)
    {
        int i = jdbcOperation2.updateField("managerTime", managerTime, fullId, OutBean.class);

        return i != 0;
    }

    /**
     * updateOutReserve4
     * 
     * @param fullId
     * @param reserve4
     * @return
     */
    public boolean updateOutReserve2(String fullId, int reserve4, String reserve6)
    {
        jdbcOperation2.updateField("reserve2", reserve4, fullId, OutBean.class);

        jdbcOperation2.updateField("reserve6", reserve6, fullId, OutBean.class);

        return true;
    }

    /**
     * updateCurcredit
     * 
     * @param fullId
     * @param curcredit
     * @return
     */
    public boolean updateCurcredit(String fullId, double curcredit)
    {
        jdbcOperation2.updateField("curcredit", curcredit, fullId, OutBean.class);

        return true;
    }

    /**
     * updateStaffcredit
     * 
     * @param fullId
     * @param staffcredit
     * @return
     */
    public boolean updateStaffcredit(String fullId, double staffcredit)
    {
        jdbcOperation2.updateField("staffcredit", staffcredit, fullId, OutBean.class);

        return true;
    }

    public int countBaseByIds(String fullId, String productId)
    {
        String sql = "select count(1) from t_center_base where outId = ? and productId = ?";

        return jdbcOperation2.queryForInt(sql, new Object[] {fullId, productId});

    }

    /**
     * sumPreassignAmount
     * 
     * @param parMap
     * @return
     */
    public Integer sumPreassignAmount(Map parMap)
    {
        return (Integer)jdbcOperation2.getIbatisDaoSupport().queryForObject(
            "OutDAO.sumPreassignAmount", parMap);

    }

    public boolean modifyChecks(String fullId, String checks)
    {
        int i = jdbcOperation2.updateField("checks", checks, fullId, OutBean.class);

        return i != 0;
    }

    public boolean modifyData(String fullId, String date)
    {
        int i = jdbcOperation2.updateField("outTime", date, fullId, OutBean.class);

        return i != 0;
    }

    public boolean updataInWay(String fullId, int inway)
    {
        int i = jdbcOperation2.updateField("inway", inway, fullId, OutBean.class);

        return i != 0;
    }

    public boolean modifyOutHadPay2(String fullId, String hadPay)
    {
        int i = jdbcOperation2.updateField("hadPay", hadPay, fullId, OutBean.class);

        return i != 0;
    }

    public boolean modifyReDate2(String fullId, String reDate)
    {
        int i = jdbcOperation2.updateField("redate", reDate, fullId, OutBean.class);

        return i != 0;
    }

    public boolean modifyPay2(String fullId, int pay)
    {
        int i = jdbcOperation2.updateField("pay", pay, fullId, OutBean.class);

        return i != 0;
    }

    public boolean modifyTempType(String fullId, int tempType)
    {
        int i = jdbcOperation2.updateField("tempType", tempType, fullId, OutBean.class);

        return i != 0;
    }

    public boolean mark2(String fullId, boolean status)
    {
        int i = jdbcOperation2.updateField("mark", status, fullId, OutBean.class);

        return i != 0;
    }

    /**
     * sumNoPayBusiness(统计基于客户信用额度的预占金额)
     * 
     * @param cid
     * @param beginDate
     * @param endDate
     * @return
     */
    public double sumNoPayBusiness(String cid, String beginDate, String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("customerId", cid);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object max = this.jdbcOperation2.getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNoPayBusiness", paramterMap);

        if (max == null)
        {
            return 0.0d;
        }

        return (Double)max;
    }

    /**
     * 业务员担保没有回款的
     * 
     * @param stafferId
     * @param beginDate
     * @param endDate
     * @return
     */
    public double sumNoPayAndAvouchBusinessByStafferId(String stafferId, String beginDate,
                                                       String endDate)
    {
        Map<String, String> paramterMap = new HashMap();

        paramterMap.put("stafferId", stafferId);
        paramterMap.put("beginDate", beginDate);
        paramterMap.put("endDate", endDate);

        Object max = this.jdbcOperation2.getIbatisDaoSupport().queryForObject(
            "OutDAO.sumNoPayAndAvouchBusinessByStafferId", paramterMap);

        if (max == null)
        {
            return 0.0d;
        }

        return (Double)max;
    }

    /**
     * @return the jdbcOperation2
     */
    public JdbcOperation getJdbcOperation2()
    {
        return jdbcOperation2;
    }

    /**
     * @param jdbcOperation2
     *            the jdbcOperation2 to set
     */
    public void setJdbcOperation2(JdbcOperation jdbcOperation2)
    {
        this.jdbcOperation2 = jdbcOperation2;
    }
}
