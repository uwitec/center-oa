/**
 * File Name: DAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-10<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;


/**
 * 公共dao的接口
 * 
 * @author zhuzhu
 * @version 2008-3-10
 * @see
 * @since
 */
public interface DAO<Bean extends Serializable, VO extends Serializable>
{
    /**
     * save entity bean
     * 
     * @param entity
     * @return
     */
    boolean saveEntityBean(Bean bean);

    /**
     * save all bean in list
     * 
     * @param list
     * @return
     */
    boolean saveAllEntityBeans(Collection<Bean> list);

    /**
     * update entity bean
     * 
     * @param entity
     * @return
     */
    boolean updateEntityBean(Bean bean);

    /**
     * update all bean in list
     * 
     * @param list
     * @return
     */
    boolean updateAllEntityBeans(Collection<Bean> list);

    /**
     * delete entity bean
     * 
     * @param key
     * @return
     */
    boolean deleteEntityBean(Serializable key);

    /**
     * delete all bean in table
     * 
     * @return
     */
    boolean deleteAllEntityBean();

    /**
     * delete entity beans
     * 
     * @param key
     * @return
     */
    boolean deleteByIds(Collection<Serializable> keys);

    /**
     * delete entity beans
     * 
     * @param beans
     * @return
     */
    boolean deleteByEntityBeans(Collection<Bean> beans);

    /**
     * delete entity beans by frist FK
     * 
     * @param key
     * @return
     */
    boolean deleteEntityBeansByFK(Serializable fk);

    /**
     * delete entity beans by frist FK
     * 
     * @param key
     * @return
     */
    boolean deleteEntityBeansByFK(Serializable fk, int index);

    /**
     * deleteEntityBeansByCondition
     * 
     * @param condition
     * @param args
     * @return
     */
    boolean deleteEntityBeansByCondition(ConditionParse condition, Object... args);

    /**
     * find unique entity bean
     * 
     * @param key
     * @return
     */
    Bean find(Serializable key);

    /**
     * find unique entity bean
     * 
     * @param key
     * @return
     */
    Bean findByUnique(Object... key);

    /**
     * find unique entity bean by condition
     * 
     * @param key
     * @return
     */
    Bean findUnique(String condition, Object... args);

    /**
     * find unique entity vo by condition
     * 
     * @param key
     * @return
     */
    VO findUniqueVO(String condition, Object... args);

    /**
     * find unique entity bean
     * 
     * @param key
     * @return
     */
    VO findVO(Serializable key);

    /**
     * find unique entity bean
     * 
     * @param key
     * @return
     */
    VO findVOByUnique(Object... key);

    /**
     * query entitys by special condition
     * 
     * @param condition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByCondition(ConditionParse condition, Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByCondition(String condition, Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByCondition(ConditionParse condition, Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByCondition(String condition, Object... args);

    /**
     * query entitys by FK
     * 
     * @param condition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByFK(Serializable fk);

    /**
     * query vos by FK
     * 
     * @param condition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByFK(Serializable fk);

    /**
     * query entitys by FK
     * 
     * @param condition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByFK(Serializable fk, int index);

    /**
     * query entitys by FK
     * 
     * @param condition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByFK(Serializable fk, int index);

    /**
     * query entitys by special condition
     * 
     * @param condition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByCondition(ConditionParse condition, PageSeparate page,
                                           Object... args);

    /**
     * query entitys by special condition and limit
     * 
     * @param condition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByLimit(ConditionParse condition, int limit, Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByCondition(ConditionParse condition, PageSeparate page, Object... args);

    /**
     * query entitys by special condition and limit
     * 
     * @param condition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByLimit(ConditionParse condition, int limit, Object... args);

    /**
     * count bean amount by special condition
     * 
     * @param whereSql
     * @param args
     * @return
     */
    int countByCondition(String whereSql, Object... args);

    /**
     * is object is exist
     * 
     * @param id
     * @return
     */
    boolean isExist(Serializable id);

    /**
     * count entry by define unique
     * 
     * @param keys
     * @return
     */
    int countByUnique(Object... keys);

    /**
     * countByFK
     * 
     * @param fk
     * @return
     */
    int countByFK(Serializable fk);

    /**
     * countByFK
     * 
     * @param fk
     * @param index
     * @return
     */
    int countByFK(Serializable fk, int index);

    /**
     * count vo amount by special condition
     * 
     * @param whereSql
     * @param args
     * @return
     */
    int countVOByCondition(String whereSql, Object... args);

    /**
     * count entry-vo by define unique
     * 
     * @param keys
     * @return
     */
    int countVOByUnique(Object... keys);

    /**
     * list all entitys
     * 
     * @return
     */
    List<Bean> listEntityBeans();

    /**
     * list all entitys
     * 
     * @return
     */
    List<Bean> listEntityBeans(String order);

    /**
     * list all entitys
     * 
     * @return
     */
    List<VO> listEntityVOs();

    /**
     * list all entitys
     * 
     * @return
     */
    List<VO> listEntityVOs(String order);

    /**
     * @return
     */
    Class<Bean> getBeanClass();

    /**
     * @return
     */
    Class<VO> getVOClass();
}
