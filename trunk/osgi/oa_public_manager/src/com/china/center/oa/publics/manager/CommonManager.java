/**
 * File Name: CommonManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager;


import java.util.List;

import com.china.center.oa.publics.bean.AreaBean;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * CommonManager
 * 
 * @author ZHUZHU
 * @version 2010-6-21
 * @see CommonManager
 * @since 1.0
 */
public interface CommonManager
{
    List<AreaBean> queryAreaByParentId(String parentId);

    List<StafferBean> queryStafferByLocationId(String locationId);

    /**
     * 查询区域下的是业务员考核的职员
     * 
     * @param locationId
     * @param type
     * @param attType
     * @return
     */
    List<StafferBean> queryStafferByLocationIdAndFiter(String locationId, int type, int attType);
}
