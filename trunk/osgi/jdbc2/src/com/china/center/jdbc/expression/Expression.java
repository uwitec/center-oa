/**
 * File Name: Expression.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-10<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.expression;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.annosql.tools.BeanUtils;
import com.china.center.jdbc.inter.DAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.JDBCCommonTools;


/**
 * Expression
 * 
 * @author ZHUZHU
 * @version 2010-7-10
 * @see Expression
 * @since 1.0
 */
public class Expression
{
    private Object managerService = null;

    private Object dataBean = null;

    /**
     * Copy Constructor
     * 
     * @param expression
     *            a <code>Expression</code> object
     */
    public Expression(Object dataBean, Object managerService)
    {
        this.managerService = managerService;
        this.dataBean = dataBean;
    }

    public void check(String expression, String msg)
        throws MYException
    {
        // exp.check("#name || #code isunique userDAO", "不能重复");
        // exp.check("#locationId as locationId notin userDAO && stafferDAO", "不能重复");
        String[] splits = expression.split(" ");

        List<ValueBean> valueList = new ArrayList();

        List<DAO> daoList = new ArrayList();

        OprationType valueOT = OprationType.AND;

        OprationType conditionTP = OprationType.AND;

        CompareType ct = CompareType.ISUNIQUE;

        // 0 value 1 操作条件 2 DAO条件
        int postion = 0;

        for (int i = 0; i < splits.length; i++ )
        {
            String each = splits[i];

            if (JDBCCommonTools.isNullOrNone(each))
            {
                continue;
            }

            each = each.trim();

            // 取值
            if (each.startsWith("#"))
            {
                ValueBean value = new ValueBean();

                value.setName(each.substring(1));

                value.setValue(getPropertyValue(this.dataBean, each.substring(1)).toString().trim());

                Field field = BeanTools.getFieldIgnoreCase(value.getName(), dataBean.getClass());

                value.setColumnName(BeanTools.getColumnName(field));

                valueList.add(value);

                postion = 0;

                continue;
            }

            if ("as".equals(each) && postion == 0)
            {
                i = i + 1;

                valueList.get(valueList.size() - 1).setColumnName(splits[i].trim());

                continue;
            }

            if ("||".equals(each) && postion == 0)
            {
                valueOT = OprationType.OR;

                continue;
            }

            if ("&&".equals(each) && postion == 0)
            {
                valueOT = OprationType.AND;

                continue;
            }

            if (CompareType.ISUNIQUE.toString().equalsIgnoreCase(each))
            {
                postion = 1;

                ct = CompareType.ISUNIQUE;

                continue;
            }

            if (CompareType.ISUNIQUE2.toString().equalsIgnoreCase(each))
            {
                postion = 1;

                ct = CompareType.ISUNIQUE2;

                continue;
            }

            if (CompareType.NOTIN.toString().equalsIgnoreCase(each))
            {
                postion = 1;

                ct = CompareType.NOTIN;

                continue;
            }

            if (CompareType.IN.toString().equalsIgnoreCase(each))
            {
                postion = 1;

                ct = CompareType.IN;

                continue;
            }

            if (each.startsWith("@"))
            {
                daoList.add((DAO)getPropertyValue(this.managerService, each.substring(1)));

                postion = 2;

                continue;
            }

            if ("||".equals(each) && postion == 2)
            {
                conditionTP = OprationType.OR;

                continue;
            }

            if ("&&".equals(each) && postion == 2)
            {
                conditionTP = OprationType.AND;

                continue;
            }

            throw new RuntimeException("expression is error:" + expression);
        }

        // ISUNIQUE
        if (ct == CompareType.ISUNIQUE)
        {
            // 都不在的时候
            if (valueOT == OprationType.AND)
            {
                // 处理唯一的(string)
                ConditionParse condition = new ConditionParse();

                condition.addWhereStr();
                for (ValueBean valueBean : valueList)
                {
                    condition.addCondition(valueBean.getColumnName(), "=", valueBean.getValue().toString());
                }

                DAO dao = daoList.get(0);

                int count = dao.countByCondition(condition.toString());

                if (count > 0)
                {
                    throw new MYException(msg);
                }

                return;
            }

            if (valueOT == OprationType.OR)
            {
                for (ValueBean valueBean : valueList)
                {
                    // 处理唯一的(string)
                    ConditionParse condition = new ConditionParse();

                    condition.addWhereStr();

                    condition.addCondition(valueBean.getColumnName(), "=", valueBean.getValue().toString());

                    DAO dao = daoList.get(0);

                    int count = dao.countByCondition(condition.toString());

                    if (count > 0)
                    {
                        throw new MYException(msg);
                    }
                }

                return;
            }

            throw new RuntimeException("expression is error:" + expression);
        }

        // 更新中不能重复
        if (ct == CompareType.ISUNIQUE2)
        {
            String idColumn = BeanTools.getIdColumn(this.dataBean.getClass());

            String id = BeanTools.getIdValue(this.dataBean).toString();

            // 都不在的时候
            if (valueOT == OprationType.AND)
            {
                // 处理唯一的(string)
                ConditionParse condition = new ConditionParse();

                condition.addWhereStr();

                condition.addCondition(idColumn, "<>", id);

                for (ValueBean valueBean : valueList)
                {
                    condition.addCondition(valueBean.getColumnName(), "=", valueBean.getValue().toString());
                }

                DAO dao = daoList.get(0);

                int count = dao.countByCondition(condition.toString());

                if (count > 0)
                {
                    throw new MYException(msg);
                }

                return;
            }

            if (valueOT == OprationType.OR)
            {
                for (ValueBean valueBean : valueList)
                {
                    // 处理唯一的(string)
                    ConditionParse condition = new ConditionParse();

                    condition.addWhereStr();

                    condition.addCondition(idColumn, "<>", id);

                    condition.addCondition(valueBean.getColumnName(), "=", valueBean.getValue().toString());

                    DAO dao = daoList.get(0);

                    int count = dao.countByCondition(condition.toString());

                    if (count > 0)
                    {
                        throw new MYException(msg);
                    }
                }

                return;
            }

            throw new RuntimeException("expression is error:" + expression);
        }

        // NOTIN
        if (ct == CompareType.NOTIN)
        {
            ValueBean valueBean = valueList.get(0);

            if (conditionTP == OprationType.AND)
            {
                for (DAO dao : daoList)
                {
                    // 处理唯一的(string)
                    ConditionParse condition = new ConditionParse();

                    condition.addWhereStr();

                    condition.addCondition(valueBean.getColumnName(), "=", valueBean.getValue().toString());

                    int count = dao.countByCondition(condition.toString());

                    if (count > 0)
                    {
                        throw new MYException(msg);
                    }
                }

                return;
            }

            if (conditionTP == OprationType.OR)
            {

                for (DAO dao : daoList)
                {
                    // 处理唯一的(string)
                    ConditionParse condition = new ConditionParse();

                    condition.addWhereStr();

                    condition.addCondition(valueBean.getColumnName(), "=", valueBean.getValue().toString());

                    int count = dao.countByCondition(condition.toString());

                    if (count == 0)
                    {
                        return;
                    }
                }

                throw new MYException(msg);
            }

            throw new RuntimeException("expression is error:" + expression);
        }

        // 处理IN
        if (ct == CompareType.IN)
        {
            ValueBean valueBean = valueList.get(0);

            if (conditionTP == OprationType.AND)
            {
                for (DAO dao : daoList)
                {
                    // 处理唯一的(string)
                    ConditionParse condition = new ConditionParse();

                    condition.addWhereStr();

                    condition.addCondition(valueBean.getColumnName(), "=", valueBean.getValue().toString());

                    int count = dao.countByCondition(condition.toString());

                    if (count == 0)
                    {
                        throw new MYException(msg);
                    }
                }

                return;
            }

            if (conditionTP == OprationType.OR)
            {
                for (DAO dao : daoList)
                {
                    // 处理唯一的(string)
                    ConditionParse condition = new ConditionParse();

                    condition.addWhereStr();

                    condition.addCondition(valueBean.getColumnName(), "=", valueBean.getValue().toString());

                    int count = dao.countByCondition(condition.toString());

                    if (count > 0)
                    {
                        return;
                    }
                }

                throw new MYException(msg);
            }

            throw new RuntimeException("expression is error:" + expression);
        }
    }

    private Object getPropertyValue(Object obj, String name)
    {
        try
        {
            return BeanUtils.getPropertyValue(obj, name);
        }
        catch (IllegalAccessException e)
        {
            return null;
        }
        catch (InvocationTargetException e)
        {
            return null;
        }
        catch (NoSuchMethodException e)
        {
            return null;
        }
    }

    /**
     * default constructor
     */
    public Expression()
    {
    }

    /**
     * @return the managerService
     */
    public Object getManagerService()
    {
        return managerService;
    }

    /**
     * @param managerService
     *            the managerService to set
     */
    public void setManagerService(Object managerService)
    {
        this.managerService = managerService;
    }

    /**
     * @return the dataBean
     */
    public Object getDataBean()
    {
        return dataBean;
    }

    /**
     * @param dataBean
     *            the dataBean to set
     */
    public void setDataBean(Object dataBean)
    {
        this.dataBean = dataBean;
    }

}
