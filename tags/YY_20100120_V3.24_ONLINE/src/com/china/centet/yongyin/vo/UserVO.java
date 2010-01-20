/**
 * 文 件 名: UserVO.java <br>
 * 版 权: centerchina Technologies Co., Ltd. Copyright YYYY-YYYY, All rights reserved
 * <br>
 * 描 述: <描述> <br>
 * 修 改 人: admin <br>
 * 修改时间: 2008-1-6 <br>
 * 跟踪单号: <跟踪单号> <br>
 * 修改单号: <修改单号> <br>
 * 修改内容: <修改内容> <br>
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.centet.yongyin.bean.User;


/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author admin
 * @version [版本号, 2008-1-6]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity(inherit = true, cache = true)
public class UserVO extends User
{
    private String locationName = "";

    public UserVO()
    {}

    /**
     * @return 返回 locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param 对locationName进行赋值
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

}
