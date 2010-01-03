/**
 * File Name: NewCustomerExamineDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.examine.bean.NewCustomerExamineBean;
import com.china.center.oa.examine.wrap.CustomerWrap;
import com.china.center.tools.TimeTools;


/**
 * NewCustomerExamineDAO
 * 
 * @author zhuzhu
 * @version 2009-1-11
 * @see NewCustomerExamineDAO
 * @since 1.0
 */
@Bean(name = "newCustomerExamineDAO")
public class NewCustomerExamineDAO extends BaseDAO2<NewCustomerExamineBean, NewCustomerExamineBean>
{
    /**
     * 根据职员和时间查询时间段内开单的新客户(拓展)
     * 
     * @param stafferId
     * @param beginTime
     * @param endTime
     * @return
     */
    public Map queryOutCustomerByCondition(String stafferId, String beginTime, String endTime)
    {
        Map result = new HashMap();

        // 先格式化一下
        beginTime = TimeTools.changeTimeToDate(beginTime);

        endTime = TimeTools.changeTimeToDate(endTime);

        String sql = "select t2.id, t2.name, t1.fullId , t1.outTime as logTime from t_center_out t1, t_center_customer_now t2 where t1.type = ? "
                     + "and t1.stafferId = ? and t1.outTime >= ? and t1.outTime <= ? and t2.hasNew = ? and t2.newtype = ? "
                     + "and t1.status >= ? and t1.customerId = t2.id";

        List<Map> list = this.jdbcOperation.queryForList(sql,
            com.china.centet.yongyin.constant.Constant.OUT_TYPE_OUTBILL, stafferId, beginTime,
            endTime, CustomerConstant.HASNEW_YES, CustomerConstant.NEWTYPE_NEW,
            com.china.centet.yongyin.constant.Constant.STATUS_PASS);

        List<CustomerWrap> clist = new ArrayList<CustomerWrap>();

        int count = 0;

        for (Map map : list)
        {
            CustomerWrap temp = new CustomerWrap();

            temp.setId(map.get("id").toString());

            temp.setName(map.get("name").toString());

            temp.setOutId(map.get("fullId").toString());
            
            temp.setLogTime(map.get("logTime").toString());

            if (add(clist, temp))
            {
                count++ ;
            }
        }

        result.put("count", count);

        result.put("clist", clist);

        return result;
    }

    private boolean add(List<CustomerWrap> clist, CustomerWrap temp)
    {
        for (CustomerWrap customerWrap : clist)
        {
            if (customerWrap.getId().equals(temp.getId()))
            {
                clist.add(temp);
                
                return false;
            }
        }

        clist.add(temp);

        return true;
    }
}
