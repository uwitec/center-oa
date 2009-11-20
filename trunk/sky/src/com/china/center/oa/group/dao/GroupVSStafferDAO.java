/**
 * File Name: GroupDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.group.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.group.vo.GroupVSStafferVO;
import com.china.center.oa.group.vs.GroupVSStafferBean;


/**
 * GroupDAO
 * 
 * @author ZHUZHU
 * @version 2009-4-7
 * @see GroupVSStafferDAO
 * @since 1.0
 */
@Bean(name = "groupVSStafferDAO")
public class GroupVSStafferDAO extends BaseDAO2<GroupVSStafferBean, GroupVSStafferVO>
{

}
