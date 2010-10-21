/**
 * File Name: OrgConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.constant;

/**
 * OrgConstant
 * 
 * @author ZHUZHU
 * @version 2010-10-21
 * @see OrgConstant
 * @since 1.0
 */
public interface OrgConstant
{
    /**
     * 大区(二级组织)
     */
    String ORG_BIG_LOCATION = "2";

    /**
     * 事业部(二级组织)
     */
    String ORG_BIG_DEPARTMENT = "3";

    /**
     * 永银总部
     */
    String CENTER_LOCATION = "99";

    /**
     * 总裁
     */
    String ORG_CEO = "1";

    /**
     * 董事会
     */
    String ORG_ROOT = "0";

    /**
     * 区域级别
     */
    int LOCATION_LEVEL = 4;

    /**
     * 行业级别
     */
    int CALLING_LEVEL = 3;
}
