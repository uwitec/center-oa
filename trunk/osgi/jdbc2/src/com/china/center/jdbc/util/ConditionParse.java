package com.china.center.jdbc.util;


import java.io.Serializable;


/**
 * @author ZHUZHU
 * @version 2007-12-18
 * @see
 * @since
 */
public class ConditionParse implements Serializable
{
    private StringBuffer condition = new StringBuffer();

    /**
     *
     *
     */
    public ConditionParse()
    {
    }

    /**
     * 根据条件字段、运算符和条件值拼凑查询条件
     * 
     * @param fieldName
     *            字段名称
     * @param oper
     *            关系运算符
     * @param conditionValue
     *            输入的查询条件值
     */
    public void addCondition(String fieldName, String oper, String conditionValue)
    {
        if ( ! (conditionValue == null || "".equals(conditionValue)))
        {
            String tempOper = oper.trim().toUpperCase();

            if (tempOper.indexOf("LIKE") != -1)
            {
                if (conditionValue.startsWith("%") || conditionValue.endsWith("%"))
                {
                    condition.append(" AND ").append(dbValueString(fieldName)).append(" ").append(
                        dbValueString(oper)).append(" ").append("'").append(
                        dbValueString(conditionValue)).append("'");
                }
                else
                {
                    condition.append(" AND ").append(dbValueString(fieldName)).append(" ").append(
                        dbValueString(oper)).append(" ").append("'%").append(
                        dbValueString(conditionValue)).append("%'");
                }
            }
            else
            {
                condition.append(" AND ").append(dbValueString(fieldName)).append(" ").append(
                    dbValueString(oper)).append(" ").append("'").append(
                    dbValueString(conditionValue)).append("'");
            }
        }
    }

    /**
     * Description: <br>
     * 指定增加条件语句 <br>
     * Implement: <br>
     * 
     * @param conditionValue
     */
    public void addCondition(String conditionValue)
    {
        condition.append(" ").append(conditionValue);
    }

    /**
     * 永远不成立的
     * 
     * @param conditionValue
     */
    public void addFlaseCondition()
    {
        condition.append(" ").append("and 1 = 2").append(" ");
    }

    /**
     * Description: <br>
     * 指定增加条件语句 <br>
     * Implement: <br>
     * 
     * @param conditionValue
     */
    public void addString(String conditionValue)
    {
        condition.append(conditionValue);
    }

    /**
     * @param fieldName
     * @param oper
     * @param conditionValue
     */
    public void addCondition(String fieldName, String oper, int conditionValue)
    {
        String tempValue = String.valueOf(conditionValue);
        if ( ! (tempValue == null || "".equals(tempValue)))
        {
            condition.append(" AND ").append(dbValueString(fieldName)).append(" ").append(
                dbValueString(oper)).append(" ").append(dbValueString(tempValue)).append(" ");
        }
    }

    /**
     * Description:增加整型条件字段 <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param fieldName
     * @param oper
     * @param conditionValue
     *            void
     */
    public void addIntCondition(String fieldName, String oper, String conditionValue)
    {
        if ( ! (conditionValue == null || "".equals(conditionValue)))
        {
            int tempValue = Integer.parseInt(conditionValue);
            condition.append(" AND ").append(dbValueString(fieldName)).append(" ").append(
                dbValueString(oper)).append(tempValue);
        }

    }

    public void clear()
    {
        condition.delete(0, condition.length());
    }

    public void addIntCondition(String fieldName, String oper, int conditionValue)
    {
        condition.append(" AND ").append(dbValueString(fieldName)).append(" ").append(
            dbValueString(oper)).append(conditionValue);
    }

    public void addCommonCondition(String fieldName, String oper, String conditionValue)
    {
        if ( ! (conditionValue == null || "".equals(conditionValue)))
        {
            condition.append(" AND ").append(dbValueString(fieldName)).append(" ").append(
                dbValueString(oper)).append(conditionValue);
        }
    }

    /**
     * 增加WHERE字符串在前面，有些情况下原查询语句没有条件语句， 则要在条件语句前增加"WHERE"开头。
     */
    public void addWhereStr()
    {
        if (toString().toUpperCase().indexOf("WHERE") == -1)
        {
            condition.insert(0, " WHERE 1=1 ");
        }
    }

    /**
     * containOrder
     * 
     * @return
     */
    public boolean containOrder()
    {
        if (toString().toUpperCase().indexOf("ORDER BY ") == -1)
        {
            return false;
        }

        return true;
    }

    public void removeWhereStr()
    {
        if (toString().toUpperCase().indexOf("WHERE") != -1)
        {
            String newConstiton = condition.toString().trim();

            condition.delete(0, condition.length());

            condition.append(newConstiton);

            condition.delete(0, "WHERE".length());
        }
    }

    public String getCondition()
    {
        return condition.toString();
    }

    public String toString()
    {
        return getCondition();
    }

    public void setCondition(String condition)
    {
        this.condition.delete(0, this.condition.length());

        this.condition.append(condition);
    }

    /**
     * 把'变成 ''
     * 
     * @param src
     * @return
     */
    private String dbValueString(String src)
    {
        if (JDBCCommonTools.isNullOrNone(src))
        {
            return "";
        }

        return src.replaceAll("'", "''");
    }
}
