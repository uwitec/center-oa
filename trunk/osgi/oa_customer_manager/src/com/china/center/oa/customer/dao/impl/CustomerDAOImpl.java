/**
 * File Name: CustomerDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.constant.CustomerConstant;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.vo.CustomerVO;
import com.china.center.oa.customer.wrap.CustomerAssignWrap;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.helper.OATools;
import com.china.center.tools.StringTools;


/**
 * CustomerDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see CustomerDAOImpl
 * @since 1.0
 */
public class CustomerDAOImpl extends BaseDAO<CustomerBean, CustomerVO> implements CustomerDAO
{
    /**
     * 区域ID
     * 
     * @param locationId
     * @return
     */
    public int countByLocationId(String locationId)
    {
        return this.jdbcOperation.queryForInt("where locationId = ?", claz, locationId);
    }

    public int countByCreditLevelId(String creditLevelId)
    {
        return this.jdbcOperation
            .queryForInt(
                "select count(1) from (select distinct(t1.id) from T_CENTER_CUSTOMER_NOW t1, t_center_out t2 where t1.id = t2.customerId and t2.outTime >= ? and t1.creditLevelId = ?) tt",
                OATools.getFinanceBeginDate(), creditLevelId);
    }

    /**
     * 统计code
     * 
     * @param code
     * @return
     */
    public int countByCode(String code)
    {
        return this.jdbcOperation.queryForInt("where code = ?", claz, code);
    }

    /**
     * 根据查询客户
     * 
     * @param code
     * @return
     */
    public CustomerBean findCustomerByCode(String code)
    {
        return this.jdbcOperation.find(code, "code", claz);
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
            "select count(1) from t_center_out where customerId = ?", id);
    }

    /**
     * 在bill里面统计客户的使用
     * 
     * @param id
     * @return
     */
    public int countCustomerInBill(String id)
    {
        return this.jdbcOperation.queryForInt(
            "select count(1) from t_center_bill where customerId = ?", id);
    }

    /**
     * 更新区域
     * 
     * @param srcLocationId
     * @param dirLocationId
     * @return
     */
    public boolean updateCustomerLocation(String srcLocationId, String dirLocationId)
    {
        this.jdbcOperation.update("set locationId = ? where locationId = ?", claz, dirLocationId,
            srcLocationId);

        return true;
    }

    /**
     * 更新客户的状态
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateCustomerstatus(String id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, claz);

        return true;
    }

    /**
     * updateCustomerCreditUpdateTime
     * 
     * @param id
     * @param creditUpdateTime
     * @return
     */
    public boolean updateCustomerCreditUpdateTime(String id, int creditUpdateTime)
    {
        this.jdbcOperation.updateField("creditUpdateTime", creditUpdateTime, id, claz);

        return true;
    }

    /**
     * listNotPayCustomerIds
     * 
     * @return
     */
    public List<String> listNotPayCustomerIds()
    {
        // 查询销售单未还款的客户
        String sql = "select distinct(t2.id) from t_center_out t1, T_CENTER_CUSTOMER_NOW t2 "
                     + "where t2.id = t1.customerId and  t1.pay = 0 and t1.type = 0 and t1.outTime >= ?";

        final List<String> result = new ArrayList();

        this.jdbcOperation.query(sql, new Object[] {OATools.getFinanceBeginDate()},
            new RowCallbackHandler()
            {
                public void processRow(ResultSet rst)
                    throws SQLException
                {
                    result.add(rst.getString("ID"));
                }
            });

        return result;
    }

    /**
     * updateCustomerCredit
     * 
     * @param id
     * @param creditLevelId
     * @param creditVal
     * @return
     */
    public boolean updateCustomerCredit(String id, String creditLevelId, int creditVal)
    {
        this.jdbcOperation.update("set creditLevelId = ? ,creditVal = ? where id = ?", claz,
            creditLevelId, (double)creditVal, id);

        return true;
    }

    /**
     * 修改客户的新老属性(变成老客户)
     * 
     * @param id
     * @param newType
     * @return
     */
    public boolean updateCustomerNewTypeToOld(String id)
    {
        this.jdbcOperation.updateField("newType", CustomerConstant.NEWTYPE_OLD, id, claz);

        this.jdbcOperation.updateField("hasNew", CustomerConstant.HASNEW_NO, id, claz);

        return true;
    }

    /**
     * 修改更新hasnew
     * 
     * @param id
     * @return
     */
    public boolean updateCustomerHasNewToOld(String id)
    {
        this.jdbcOperation.updateField("hasNew", CustomerConstant.HASNEW_NO, id, claz);

        return true;
    }

    /**
     * 根据地市更新区域属性
     * 
     * @param srcLocationId
     * @param dirLocationId
     * @return
     */
    public boolean updateCustomerLocationByCity(String cityId, String dirLocationId)
    {
        this.jdbcOperation.update("set locationId = ? where cityId = ?", claz, dirLocationId,
            cityId);

        return true;
    }

    public boolean initCustomerLocation()
    {
        this.jdbcOperation.update("set locationId = ? where 1 = 1", claz,
            PublicConstant.CENTER_LOCATION);

        return true;
    }

    /**
     * 统计总数的
     * 
     * @param condition
     * @return
     */
    public int countCustomerAssignByConstion(ConditionParse condition)
    {
        return jdbcOperation.queryObjectsBySql(getLastQuerySql(condition)).getCount();
    }

    /**
     * @param condition
     * @return
     */
    private String getLastQuerySql(ConditionParse condition)
    {
        condition.removeWhereStr();

        if (StringTools.isNullOrNone(condition.toString()))
        {
            condition.addString("1 = 1");
        }

        return getQuerySql() + " and " + condition.toString();
    }

    /**
     * 根据条件和分页查询
     * 
     * @param condition
     * @param page
     * @return
     */
    public List<CustomerAssignWrap> queryCustomerAssignByConstion(ConditionParse condition,
                                                                  PageSeparate page)
    {
        // return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, claz, args);
        return jdbcOperation.queryObjectsBySqlAndPageSeparate(getLastQuerySql(condition), page,
            CustomerAssignWrap.class);

    }

    private String getQuerySql()
    {
        // select t3.name as stafferName, t1.* from t_center_customer_now t1, t_center_vs_stacus t2, t_center_oastaffer
        // t3
        // where t1.id = t2.CUSTOMERID and t2.STAFFERID = t3.id and t3.locationid = '1009'
        return "select t3.name as stafferName, t2.stafferId, t2.customerId, "
               + "t1.name as customerName, t1.code as customerCode, t1.sellType, t1.loginTime "
               + "from t_center_customer_now t1, t_center_vs_stacus t2, t_center_oastaffer t3 "
               + "where t1.id = t2.CUSTOMERID and t2.STAFFERID = t3.id ";
    }

    private String getQuerySelfSql(String stafferId)
    {
        return "select CustomerBean.* " + "from T_CENTER_CUSTOMER_NOW CustomerBean, "
               + "(select customerid from t_center_vs_stacus where STAFFERID = '" + stafferId
               + "') tt1 " + "where CustomerBean.id = tt1.customerid";
    }

    private String getLastQuerySelfSql(String stafferId, ConditionParse condition)
    {
        ConditionParse newConditionParse = new ConditionParse();

        newConditionParse.setCondition(condition.toString());

        newConditionParse.removeWhereStr();

        if (StringTools.isNullOrNone(newConditionParse.toString()))
        {
            newConditionParse.addString("1 = 1");
        }

        return getQuerySelfSql(stafferId) + " and " + newConditionParse.toString();
    }

    /**
     * 统计自己客户的总数
     * 
     * @param stafferId
     * @param condition
     * @return
     */
    public int countSelfCustomerByConstion(String stafferId, ConditionParse condition)
    {
        return jdbcOperation
            .queryObjectsBySql(getLastQuerySelfSql(stafferId, condition))
            .getCount();
    }

    /**
     * 根据分页条件查询自己的客户
     * 
     * @param stafferId
     * @param condition
     * @param page
     * @return
     */
    public List<CustomerBean> querySelfCustomerByConstion(String stafferId,
                                                          ConditionParse condition,
                                                          PageSeparate page)
    {
        // return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, claz, args);
        return jdbcOperation.queryObjectsBySqlAndPageSeparate(getLastQuerySelfSql(stafferId,
            condition), page, CustomerBean.class);

    }

    /**
     * 客户状态全量同步
     * 
     * @return
     */
    public int autoUpdateCustomerStatus()
    {
        this.jdbcOperation.getIbatisDaoSupport().update(
            "CustomerDAOImpl.preAutoUpdateCustomerStatus", null);

        return this.jdbcOperation.getIbatisDaoSupport().update(
            "CustomerDAOImpl.autoUpdateCustomerStatus", null);
    }

    /**
     * 更新地市下拓展客户的状态为初始态，释放客户
     * 
     * @param cityId
     * @return
     */
    public boolean updateCityCustomerToInit(String cityId)
    {
        this.jdbcOperation.getIbatisDaoSupport().update("CustomerDAOImpl.updateCityCustomerToInit",
            cityId);

        return true;
    }

    /**
     * 根据职员修改职员下客户的状态
     * 
     * @param stafferId
     * @param flag
     * @return
     */
    public boolean updateCustomerByStafferId(String stafferId, int status, int flag)
    {
        Map map = new HashMap();

        map.put("stafferId", stafferId);

        map.put("status", status);

        if (flag == CustomerConstant.RECLAIMSTAFFER_ALL)
        {
            this.jdbcOperation.getIbatisDaoSupport().update(
                "CustomerDAOImpl.updateAllCustomerByStafferId", map);
        }

        if (flag == CustomerConstant.RECLAIMSTAFFER_EXPEND)
        {
            map.put("selltype", CustomerConstant.SELLTYPE_EXPEND);

            this.jdbcOperation.getIbatisDaoSupport().update(
                "CustomerDAOImpl.updateCustomerByStafferIdAndSelltype", map);
        }

        if (flag == CustomerConstant.RECLAIMSTAFFER_TER)
        {
            map.put("selltype", CustomerConstant.SELLTYPE_TER);

            this.jdbcOperation.getIbatisDaoSupport().update(
                "CustomerDAOImpl.updateCustomerByStafferIdAndSelltype", map);
        }

        return true;
    }

    /**
     * 删除职员客户的对应关系
     * 
     * @param stafferId
     * @param status
     * @param flag
     * @return
     */
    public boolean delCustomerByStafferId(String stafferId, int flag)
    {
        Map map = new HashMap();

        map.put("stafferId", stafferId);

        if (flag == CustomerConstant.RECLAIMSTAFFER_ALL)
        {
            this.jdbcOperation.getIbatisDaoSupport().delete(
                "CustomerDAOImpl.delAllCustomerByStafferId", map);
        }

        if (flag == CustomerConstant.RECLAIMSTAFFER_EXPEND)
        {
            map.put("selltype", CustomerConstant.SELLTYPE_EXPEND);

            this.jdbcOperation.getIbatisDaoSupport().delete(
                "CustomerDAOImpl.delCustomerByStafferIdAndSelltype", map);
        }

        if (flag == CustomerConstant.RECLAIMSTAFFER_TER)
        {
            map.put("selltype", CustomerConstant.SELLTYPE_TER);

            this.jdbcOperation.getIbatisDaoSupport().delete(
                "CustomerDAOImpl.delCustomerByStafferIdAndSelltype", map);
        }

        return true;
    }

    /**
     * 更新地市下正在申请中的拓展客户的状态为初始态，释放客户
     * 
     * @param cityId
     * @return
     */
    public boolean updateApplyCityCustomerToInit(String cityId)
    {
        this.jdbcOperation.getIbatisDaoSupport().update(
            "CustomerDAOImpl.updateApplyCityCustomerToInit", cityId);

        return true;
    }

    /**
     * 一年一度的同步拓展用户的新老状态
     * 
     * @return
     */
    public int synCustomerNewTypeYear(String begin, String end)
    {
        Map map = new HashMap();

        map.put("begin", begin);
        map.put("end", end);

        return this.jdbcOperation.getIbatisDaoSupport().update(
            "CustomerDAOImpl.synCustomerNewTypeYear", map);
    }

    /**
     * 一旦有成交就是老客户（终端）<br>
     * 终端的客户只要成交就是(销售单且通过的，且是终端用户的)
     * 
     * @return
     */
    public int updayeCustomerNewTypeInTer()
    {
        return this.jdbcOperation.getIbatisDaoSupport().update(
            "CustomerDAOImpl.updayeCustomerNewTypeInTer", null);

    }

    public boolean updateCustomerLever(String id, int lever)
    {
        this.jdbcOperation.updateField("lever", lever, id, claz);

        return true;
    }
}
