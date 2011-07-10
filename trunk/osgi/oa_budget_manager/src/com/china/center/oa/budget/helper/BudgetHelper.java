/**
 * File Name: BudgetHelper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-30<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.budget.helper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.china.center.oa.budget.bean.BudgetApplyBean;
import com.china.center.oa.budget.bean.BudgetBean;
import com.china.center.oa.budget.constant.BudgetConstant;
import com.china.center.oa.budget.vo.BudgetItemVO;
import com.china.center.oa.budget.vo.BudgetLogVO;
import com.china.center.oa.budget.vo.BudgetVO;
import com.china.center.tools.MathTools;


/**
 * BudgetHelper
 * 
 * @author ZHUZHU
 * @version 2009-5-30
 * @see BudgetHelper
 * @since 1.0
 */
public abstract class BudgetHelper
{
    private static Map<Integer, Integer> budgetNextMap = new HashMap<Integer, Integer>();

    static
    {
        budgetNextMap.put(BudgetConstant.BUDGET_LEVEL_YEAR, BudgetConstant.BUDGET_LEVEL_MONTH);

        budgetNextMap.put(BudgetConstant.BUDGET_LEVEL_MONTH, 9999);

        // 月度预算变更只要财务总监核准 年度变更需要总经理 公司年度变更需要董事长
    }

    /**
     * 或者下一个apply的状态
     * 
     * @param budget
     * @param status
     * @return
     */
    public static int getNextApplyStatus(BudgetBean budget, BudgetApplyBean apply)
    {
        return BudgetConstant.BUDGET_APPLY_STATUS_END;
    }

    /**
     * getNextType
     * 
     * @param type
     * @return
     */
    public static int getNextType(int type)
    {
        return budgetNextMap.get(type);
    }

    public static void formatBudgetVO(BudgetVO vo)
    {
        vo.setStotal( (MathTools.formatNum(vo.getTotal())));

        vo.setSrealMonery( (MathTools.formatNum(vo.getRealMonery())));
    }

    /**
     * format
     * 
     * @param budgetItemVO
     */
    public static void formatBudgetItem(BudgetItemVO budgetItemVO)
    {
        budgetItemVO.setSbudget(MathTools.formatNum(budgetItemVO.getBudget()));

        budgetItemVO.setSrealMonery(MathTools.formatNum(budgetItemVO.getRealMonery()));

        budgetItemVO.setSuseMonery(MathTools.formatNum(budgetItemVO.getUseMonery()));

        budgetItemVO.setSremainMonery(MathTools.formatNum(budgetItemVO.getBudget()
                                                          - budgetItemVO.getRealMonery()));
    }

    /**
     * format log
     * 
     * @param log
     */
    public static void formatBudgetLog(BudgetLogVO log)
    {
        log.setSmonery(MathTools.formatNum(log.getMonery()));
        log.setSbeforemonery(MathTools.formatNum(log.getBeforemonery()));
        log.setSaftermonery(MathTools.formatNum(log.getAftermonery()));
    }

    /**
     * format list
     * 
     * @param items
     * @return
     */
    public static List<BudgetItemVO> formatBudgetItemList(List<BudgetItemVO> items)
    {
        for (BudgetItemVO budgetItemVO : items)
        {
            formatBudgetItem(budgetItemVO);
        }

        return items;
    }

    public static List<BudgetVO> formatBudgetList(List<BudgetVO> items)
    {
        for (BudgetVO budgetItemVO : items)
        {
            formatBudgetVO(budgetItemVO);
        }

        return items;
    }

    /**
     * formatBudgetLogList
     * 
     * @param items
     * @return List<BudgetLogVO>
     */
    public static List<BudgetLogVO> formatBudgetLogList(List<BudgetLogVO> items)
    {
        for (BudgetLogVO log : items)
        {
            formatBudgetLog(log);
        }

        return items;
    }
}
