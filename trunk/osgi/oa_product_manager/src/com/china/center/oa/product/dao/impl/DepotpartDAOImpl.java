/**
 * File Name: DepotpartDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-22<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao.impl;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.vo.DepotpartVO;


/**
 * DepotpartDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-22
 * @see DepotpartDAOImpl
 * @since 1.0
 */
public class DepotpartDAOImpl extends BaseDAO<DepotpartBean, DepotpartVO> implements DepotpartDAO
{
    public List<DepotpartBean> queryOkDepotpartInDepot(String depotId)
    {
        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addCondition("DepotpartBean.locationId", "=", depotId);

        condition.addIntCondition("DepotpartBean.type", "=", DepotConstant.DEPOTPART_TYPE_OK);

        return this.queryEntityBeansByCondition(condition);
    }
}
