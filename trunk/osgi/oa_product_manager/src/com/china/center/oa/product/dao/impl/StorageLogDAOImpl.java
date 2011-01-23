/**
 * File Name: StorageLogDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-25<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao.impl;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.bean.StorageLogBean;
import com.china.center.oa.product.dao.StorageLogDAO;
import com.china.center.oa.product.vo.StorageLogVO;


/**
 * StorageLogDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-25
 * @see StorageLogDAOImpl
 * @since 1.0
 */
public class StorageLogDAOImpl extends BaseDAO<StorageLogBean, StorageLogVO> implements StorageLogDAO
{
    /**
     * queryStorageLogByCondition
     * 
     * @param condition
     * @return
     */
    public List<StorageLogBean> queryStorageLogByCondition(ConditionParse condition)
    {
        condition.addWhereStr();

        return jdbcOperation.queryForListBySql(
            "select t1.* from T_CENTER_STORAGELOG t1, t_center_product t2 " + condition.toString(),
            StorageLogBean.class);
    }
}
