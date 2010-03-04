/**
 * File Name: ProductExamineDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.examine.bean.ProductExamineBean;
import com.china.center.oa.examine.vo.ProductExamineVO;
import com.china.center.oa.examine.wrap.CustomerWrap;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * ProductExamineDAO
 * 
 * @author zhuzhu
 * @version 2009-2-14
 * @see ProductExamineDAO
 * @since 1.0
 */
@Bean(name = "productExamineDAO")
public class ProductExamineDAO extends BaseDAO2<ProductExamineBean, ProductExamineVO>
{
    /**
     * updateStatus
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(String id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, claz);
        
        this.jdbcOperation.updateField("logTime", TimeTools.now(), id, claz);

        return true;
    }

    /**
     * 根据区域、职员和时间查询时间段内产品的开单客户(已经成交的status in (3, 4))
     * 
     * @param stafferId
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<CustomerWrap> queryProductOutCustomerByCondition(String productId,
                                                                 String stafferId,
                                                                 String beginTime, String endTime, String cityId)
    {
        // 先格式化一下
        beginTime = TimeTools.changeTimeToDate(beginTime);

        endTime = TimeTools.changeTimeToDate(endTime);

        Map map = new HashMap();

        map.put("productId", productId);
        map.put("stafferId", stafferId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("cityId", cityId);
        map.put("type", com.china.centet.yongyin.constant.Constant.OUT_TYPE_OUTBILL);

        return this.jdbcOperation.getIbatisDaoSupport().queryForList(
            "Examine.queryProductOutCustomerByCondition", map);
    }
    
    /**
     * countProductExamineByCondition
     * @param stafferId
     * @param condition
     * @return
     */
    public int countProductExamineByCondition(String stafferId, ConditionParse condition)
    {
        return jdbcOperation.queryObjectsBySql(getLastQuerySql(stafferId, condition), stafferId).getCount();
    }

    /**
     * 查询没有操作权限,但是涉及本身的考核
     * 
     * @param stafferId
     * @param condition
     * @param page
     * @return
     */
    public List<ProductExamineVO> queryProductExamineByConstion(String stafferId,
                                                                ConditionParse condition,
                                                                PageSeparate page)
    {
        return jdbcOperation.queryObjectsBySqlAndPageSeparate(
            getLastQuerySql(stafferId, condition), page, ProductExamineVO.class, stafferId);
    }

    private String getLastQuerySql(String stafferId, ConditionParse condition)
    {
        condition.removeWhereStr();

        if (StringTools.isNullOrNone(condition.toString()))
        {
            condition.addString("1 = 1");
        }

        return getSql() + " and " + condition.toString();
    }

    private String getSql()
    {
        StringBuilder buffer = new StringBuilder();

        buffer.append("select StafferBean.NAME as createrName, LocationBean.NAME as locationName,");
        buffer.append("ProductBean.NAME as productName, ProductBean.CODE as productCode,");
        buffer.append("ProductExamineBean.* FROM T_CENTER_PROEXAMINE ProductExamineBean,");
        buffer.append("T_CENTER_OASTAFFER StafferBean, T_CENTER_OALOCATION LocationBean,");
        buffer.append("T_CENTER_PRODUCT ProductBean,T_CENTER_PROSTAFFER ProductExamineItemBean ");
        buffer.append("WHERE ProductExamineBean.createrId = StafferBean.id and ProductExamineBean.locationId = LocationBean.id ");
        buffer.append("and ProductExamineItemBean.pid = ProductExamineBean.id and ProductExamineBean.productId = ProductBean.id  ");
        buffer.append("and ProductExamineItemBean.stafferId = ?");

        return buffer.toString();
    }
}
