/**
 * File Name: BaseBalanceDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-12-4<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.dao.impl;


import java.util.List;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.sail.bean.BaseBalanceBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.BaseBalanceDAO;
import com.china.center.oa.sail.vo.BaseBalanceVO;


/**
 * BaseBalanceDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-12-4
 * @see BaseBalanceDAOImpl
 * @since 3.0
 */
public class BaseBalanceDAOImpl extends BaseDAO<BaseBalanceBean, BaseBalanceVO> implements BaseBalanceDAO
{
    public List<BaseBalanceVO> queryPassBaseBalance(String id)
    {
        // 检查是否结算溢出
        ConditionParse condition = new ConditionParse();

        condition.addCondition("BaseBalanceBean.baseId", "=", id);

        condition.addIntCondition("OutBalanceBean.status", "=", OutConstant.OUTBALANCE_STATUS_PASS);

        return queryEntityVOsByCondition(condition);
    }

    public int sumPassBaseBalance(String baseId)
    {
        List<BaseBalanceVO> list = queryPassBaseBalance(baseId);

        int sum = 0;

        for (BaseBalanceBean baseBalanceBean : list)
        {
            sum += baseBalanceBean.getAmount();
        }

        return sum;
    }
}
