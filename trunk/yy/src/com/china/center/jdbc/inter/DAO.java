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
    boolean saveEntityBean(Bean entity);

    /**
     * update entity bean
     * 
     * @param entity
     * @return
     */
    boolean updateEntityBean(Bean entity);

    /**
     * delete entity bean
     * 
     * @param key
     * @return
     */
    boolean deleteEntityBean(Serializable key);

    /**
     * delete entity beans by frist FK
     * 
     * @param key
     * @return
     */
    boolean deleteEntityBeansByFK(Object fk);

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
    Bean find(Object key);

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
    VO findVO(Object key);

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
     * @param condtition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansBycondition(ConditionParse condtition, Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsBycondition(ConditionParse condtition, Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByFK(Object fk);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByFK(Object fk);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByFK(Object fk, int index);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByFK(Object fk, int index);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansBycondition(ConditionParse condtition, PageSeparate page,
                                           Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<Bean> queryEntityBeansByLimit(ConditionParse condtition, int limit, Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsBycondition(ConditionParse condtition, PageSeparate page,
                                       Object... args);

    /**
     * query entitys by special condition
     * 
     * @param condtition
     * @param args
     * @return
     */
    List<VO> queryEntityVOsByLimit(ConditionParse condtition, int limit, Object... args);

    /**
     * count bean amount by special condition
     * 
     * @param whereSql
     * @param args
     * @return
     */
    int countBycondition(String whereSql, Object... args);

    /**
     * count entry by define unique
     * 
     * @param keys
     * @return
     */
    int countByUnique(Object... keys);

    /**
     * count vo amount by special condition
     * 
     * @param whereSql
     * @param args
     * @return
     */
    int countVOBycondition(String whereSql, Object... args);

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
}
