/**
 * File Name: BaseDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-10<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.impl;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.DAO;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.util.PageSeparate;


/**
 * 普通的dao
 * 
 * @author zhuzhu
 * @version 2008-3-10
 * @see
 * @since
 */
public abstract class BaseDAO<T extends Serializable, VO extends Serializable> implements DAO<T, VO>
{
    protected JdbcOperation jdbcOperation = null;

    protected Class<T> claz = null;

    protected Class<VO> clazVO = null;

    /**
     * default constructor
     */
    public BaseDAO()
    {
        Class claa = this.getClass();

        getType(claa);
    }

    /**
     * 递归获取
     * 
     * @param claa
     */
    private void getType(Class claa)
    {
        if (claa == Object.class)
        {
            return;
        }

        ParameterizedType type = ((ParameterizedType) (claa.getGenericSuperclass()));

        if (type != null)
        {
            Type[] tt = type.getActualTypeArguments();

            if (tt != null && tt.length != 0)
            {
                this.claz = (Class<T>)tt[0];

                this.clazVO = (Class<VO>)tt[1];
                return;
            }
        }

        if (this.claz == null)
        {
            getType(claa.getSuperclass());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#deleteEntryBean(java.lang.Object)
     */
    public boolean deleteEntityBean(Serializable key)
    {
        return jdbcOperation.delete(key, claz) > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#deleteEntityBeansByFK(java.lang.Object)
     */
    public boolean deleteEntityBeansByFK(Object fk)
    {
        return jdbcOperation.delete(fk, BeanTools.getFKFieldName(claz, 0), claz) > 0;
    }

    public boolean deleteEntityBeansByCondition(ConditionParse condition, Object... args)
    {
        jdbcOperation.delete(condition.toString(), claz, args);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#find(java.lang.Object)
     */
    public T find(Object key)
    {
        return jdbcOperation.find(key, claz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#findByUnique(java.lang.Object[])
     */
    public T findByUnique(Object... key)
    {
        return this.jdbcOperation.findByUnique(this.claz, key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#findVOByUnique(java.lang.Object[])
     */
    public VO findVOByUnique(Object... key)
    {
        return this.jdbcOperation.findByUnique(this.clazVO, key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#queryEntryBeansBycondition(com.centerchina.common.ConditionParse)
     */
    public List<T> queryEntityBeansBycondition(ConditionParse condtition, Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryForList(condtition.toString(), claz, args);
    }

    public List<T> queryEntityBeansBycondition(ConditionParse condtition, PageSeparate page,
                                               Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, claz, args);
    }

    public List<T> queryEntityBeansByLimit(ConditionParse condtition, int limit, Object... args)
    {
        condtition.addWhereStr();

        PageSeparate page = new PageSeparate(limit, limit);

        return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, claz, args);
    }

    public List<T> listEntityBeans()
    {
        return jdbcOperation.queryForList("where 1= 1", claz);
    }

    public List<T> listEntityBeans(String order)
    {
        return jdbcOperation.queryForList("where 1= 1 " + order, claz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#saveEntryBean(java.io.Serializable)
     */
    public boolean saveEntityBean(T entry)
    {
        return jdbcOperation.save(entry) > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#setJdbcOperation(com.china.center.jdbc.inter.JdbcOperation)
     */
    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        if (this.jdbcOperation == null)
        {
            this.jdbcOperation = jdbcOperation;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#updateEntryBean(java.io.Serializable)
     */
    public boolean updateEntityBean(T entry)
    {
        return jdbcOperation.update(entry) > 0;
    }

    public int countBycondition(String whereSql, Object... args)
    {
        return this.jdbcOperation.queryObjects(whereSql, this.claz, args).getCount();
    }

    public int countVOBycondition(String whereSql, Object... args)
    {
        return this.jdbcOperation.queryObjects(whereSql, this.clazVO, args).getCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#countByUnique(java.lang.Object[])
     */
    public int countByUnique(Object... keys)
    {
        return this.jdbcOperation.queryObjectsByUnique(this.claz, keys).getCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#countVOByUnique(java.lang.Object[])
     */
    public int countVOByUnique(Object... keys)
    {
        return this.jdbcOperation.queryObjectsByUnique(this.clazVO, keys).getCount();
    }

    public VO findVO(Object key)
    {
        return jdbcOperation.find(key, clazVO);
    }

    public List<VO> queryEntityVOsBycondition(ConditionParse condtition, Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryForList(condtition.toString(), clazVO, args);
    }

    public List<VO> queryEntityVOsBycondition(ConditionParse condtition, PageSeparate page,
                                              Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, clazVO, args);
    }

    public List<VO> queryEntityVOsByLimit(ConditionParse condtition, int limit, Object... args)
    {
        condtition.addWhereStr();

        PageSeparate page = new PageSeparate(limit, limit);

        return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, clazVO, args);
    }

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    public List<T> queryEntityBeansByFK(Object fk, int index)
    {
        return jdbcOperation.queryForListByFK(fk, this.claz, index);
    }

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    public List<VO> queryEntityVOsByFK(Object fk, int index)
    {
        return jdbcOperation.queryForListByFK(fk, this.clazVO, index);
    }

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    public List<T> queryEntityBeansByFK(Object fk)
    {
        return jdbcOperation.queryForListByFK(fk, this.claz, 0);
    }

    /**
     * query entitys by fk
     * 
     * @param condtition
     * @param args
     * @return
     */
    public List<VO> queryEntityVOsByFK(Object fk)
    {
        return jdbcOperation.queryForListByFK(fk, this.clazVO, 0);
    }

    /**
     * 全部的VO
     */
    public List<VO> listEntityVOs()
    {
        return jdbcOperation.queryForList("where 1= 1", clazVO);
    }

    /**
     * 全部的VO
     */
    public List<VO> listEntityVOs(String order)
    {
        return jdbcOperation.queryForList("where 1= 1 " + order, clazVO);
    }

    public T findUnique(String condition, Object... args)
    {
        return jdbcOperation.queryObjects(condition, this.claz, args).uniqueResult(this.claz);
    }

    public VO findUniqueVO(String condition, Object... args)
    {
        return jdbcOperation.queryObjects(condition, this.clazVO, args).uniqueResult(this.clazVO);
    }

    /**
     * @return the jdbcOperation
     */
    public JdbcOperation getJdbcOperation()
    {
        return jdbcOperation;
    }

    /**
     * @return the claz
     */
    public Class<T> getClaz()
    {
        return claz;
    }
}
