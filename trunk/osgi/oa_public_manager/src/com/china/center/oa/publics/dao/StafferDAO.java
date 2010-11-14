/**
 * File Name: StafferDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;


import java.util.List;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.vo.StafferVO;


/**
 * StafferDAO
 * 
 * @author ZHUZHU
 * @version 2010-6-21
 * @see StafferDAO
 * @since 1.0
 */
public interface StafferDAO extends DAO<StafferBean, StafferVO>
{
    int countByLocationId(String locationId);

    int countByCode(String code);

    boolean updatePwkey(String id, String pwkey);

    boolean updateLever(String id, int lever);

    int countByDepartmentId(String departmentId);

    int countByPostId(String postId);

    List<StafferBean> queryStafferByLocationId(String locationId);

    List<StafferBean> listEntityBeans();

    List<StafferBean> listCommonEntityBeans();

    List<StafferBean> queryStafferByPrincipalshipId(String principalshipId);

    StafferBean findyStafferByName(String name);

    /**
     * 根据权限查询职员
     * 
     * @param authId
     * @return
     */
    List<StafferBean> queryStafferByAuthId(String authId);
}
