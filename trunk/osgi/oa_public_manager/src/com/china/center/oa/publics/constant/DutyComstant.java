/**
 * File Name: DutyComstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.constant;


import com.china.center.jdbc.annotation.Defined;


/**
 * DutyComstant
 * 
 * @author ZHUZHU
 * @version 2010-8-8
 * @see DutyComstant
 * @since 1.0
 */
public interface DutyComstant
{
    @Defined(key = "dutyType", value = "一般纳税人")
    int DUTY_TYPE_COMMON = 0;

    @Defined(key = "dutyType", value = "小规模纳税人")
    int DUTY_TYPE_SMALL = 1;

    @Defined(key = "dutyType", value = "服务类")
    int DUTY_TYPE_SERVICE = 2;

    @Defined(key = "dutyType", value = "个体户")
    int DUTY_TYPE_PERSONAL = 3;
}
