/**
 * File Name: BaseDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao;


import java.util.List;

import com.china.center.jdbc.inter.DAO;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.sail.bean.BaseBean;


/**
 * BaseDAO
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see BaseDAO
 * @since 1.0
 */
public interface BaseDAO extends DAO<BaseBean, BaseBean>
{
    int countBaseByOutTime(String outTime);

    List<BaseBean> queryBaseByOutTime(String outTime, PageSeparate pageSeparate);

    boolean updateCostPricekey(String id, String costPricekey);
}
