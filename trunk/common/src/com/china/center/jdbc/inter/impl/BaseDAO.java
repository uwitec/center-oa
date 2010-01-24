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
import java.util.Collection;
import java.util.List;

import com.china.center.annosql.constant.AnoConstant;
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
public abstract class BaseDAO<Bean extends Serializable, VO extends Serializable> implements DAO<Bean, VO>
{
    protected JdbcOperation jdbcOperation = null;

    protected Class<Bean> claz = null;

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
                this.claz = (Class<Bean>)tt[0];

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
    public boolean deleteByIds(Collection<? extends Serializable> keys)
    {
        jdbcOperation.deleteByIds(keys, claz);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#deleteByEntityBeans(java.util.Collection)
     */
    public boolean deleteByEntityBeans(Collection<Bean> beans)
    {
        jdbcOperation.deleteByBeans(beans, claz);

        return true;
    }

    public boolean deleteEntityBean(Serializable key)
    {
        jdbcOperation.delete(key, claz);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#deleteAllEntityBean()
     */
    public boolean deleteAllEntityBean()
    {
        jdbcOperation.update(BeanTools.getDeleteHead(claz) + "where 1 = 1");

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#deleteEntityBeansByFK(java.lang.Object)
     */
    public boolean deleteEntityBeansByFK(Serializable fk)
    {
        jdbcOperation.delete(fk, BeanTools.getFKFieldName(claz, AnoConstant.FK_DEFAULT), claz);

        return true;
    }

    public boolean deleteEntityBeansByCondition(ConditionParse condition, Object... args)
    {
        jdbcOperation.delete(condition.toString(), claz, args);

        return true;
    }

    public boolean deleteEntityBeansByFK(Serializable fk, int index)
    {
        jdbcOperation.delete(fk, BeanTools.getFKFieldName(claz, index), claz);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#find(java.lang.Object)
     */
    public Bean find(Serializable key)
    {
        return jdbcOperation.find(key, claz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#findByUnique(java.lang.Object[])
     */
    public Bean findByUnique(Object... key)
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
    public List<Bean> queryEntityBeansByCondition(ConditionParse condtition, Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryForList(condtition.toString(), claz, args);
    }

    public List<Bean> queryEntityBeansByCondition(String condtition, Object... args)
    {
        return jdbcOperation.queryForList(condtition, claz, args);
    }

    public List<Bean> queryEntityBeansByCondition(ConditionParse condtition, PageSeparate page,
                                                  Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, claz, args);
    }

    public List<Bean> queryEntityBeansByLimit(ConditionParse condtition, int limit, Object... args)
    {
        condtition.addWhereStr();

        PageSeparate page = new PageSeparate(limit, limit);

        return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, claz, args);
    }

    public List<Bean> listEntityBeans()
    {
        return jdbcOperation.queryForList("where 1= 1", claz);
    }

    public List<Bean> listEntityBeans(String order)
    {
        return jdbcOperation.queryForList("where 1= 1 " + order, claz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#saveEntryBean(java.io.Serializable)
     */
    public boolean saveEntityBean(Bean bean)
    {
        return jdbcOperation.save(bean) > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#saveAllEntityBeans(java.util.Collection)
     */
    public boolean saveAllEntityBeans(Collection<Bean> list)
    {
        jdbcOperation.saveAll(list, this.claz);

        return true;
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
    public boolean updateEntityBean(Bean bean)
    {
        jdbcOperation.update(bean);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#updateAllEntityBeans(java.util.Collection)
     */
    public boolean updateAllEntityBeans(Collection<Bean> list)
    {
        jdbcOperation.updateAll(list, this.claz);

        return true;
    }

    public int countByCondition(String whereSql, Object... args)
    {
        return this.jdbcOperation.queryObjects(whereSql, this.claz, args).getCount();
    }

    public int countVOByCondition(String whereSql, Object... args)
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
     * @see com.china.center.jdbc.inter.DAO#countById(java.io.Serializable)
     */
    public boolean isExist(Serializable id)
    {
        return this.jdbcOperation.queryObjectById(claz, id).getCount() > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#countByFK(java.lang.Object)
     */
    public int countByFK(Serializable fk)
    {
        return this.jdbcOperation.queryObjectsByFK(this.claz, fk, 0).getCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.jdbc.inter.DAO#countByFK(java.lang.Object, int)
     */
    public int countByFK(Serializable fk, int index)
    {
        return this.jdbcOperation.queryObjectsByFK(this.claz, fk, index).getCount();
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

    public VO findVO(Serializable key)
    {
        return jdbcOperation.find(key, clazVO);
    }

    public List<VO> queryEntityVOsByCondition(ConditionParse condtition, Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryForList(condtition.toString(), clazVO, args);
    }

    public List<VO> queryEntityVOsByCondition(String condtition, Object... args)
    {
        return jdbcOperation.queryForList(condtition, clazVO, args);
    }

    public List<VO> queryEntityVOsByCondition(ConditionParse condtition, PageSeparate page,
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
    public List<Bean> queryEntityBeansByFK(Serializable fk, int index)
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
    public List<VO> queryEntityVOsByFK(Serializable fk, int index)
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
    public List<Bean> queryEntityBeansByFK(Serializable fk)
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
    public List<VO> queryEntityVOsByFK(Serializable fk)
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

    public Bean findUnique(String condition, Object... args)
    {
        return jdbcOperation.queryObjects(condition, this.claz, args).uniqueResult(this.claz);
    }

    public VO findUniqueVO(String condition, Object... args)
    {
        return jdbcOperation.queryObjects(condition, this.clazVO, args).uniqueResult(this.clazVO);
    }

    public int countBycondition(String whereSql, Object... args)
    {
        return this.jdbcOperation.queryObjects(whereSql, this.claz, args).getCount();
    }

    public int countVOBycondition(String whereSql, Object... args)
    {
        return this.jdbcOperation.queryObjects(whereSql, this.clazVO, args).getCount();
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

    public List<Bean> queryEntityBeansBycondition(ConditionParse condtition, Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryForList(condtition.toString(), claz, args);
    }

    public List<Bean> queryEntityBeansBycondition(ConditionParse condtition, PageSeparate page,
                                                  Object... args)
    {
        condtition.addWhereStr();

        return jdbcOperation.queryObjectsByPageSeparate(condtition.toString(), page, claz, args);
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
    public Class<Bean> getClaz()
    {
        return claz;
    }

    public Class<Bean> getBeanClass()
    {
        return this.claz;
    }

    public Class<VO> getVOClass()
    {
        return this.clazVO;
    }
}
