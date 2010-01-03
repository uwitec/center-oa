/**
 * File Name: CityConfigManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.manager;


import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.examine.bean.CityConfigBean;
import com.china.center.oa.examine.dao.CityConfigDAO;
import com.china.center.oa.publics.User;
import com.china.center.tools.JudgeTools;


/**
 * CityConfigManager
 * 
 * @author zhuzhu
 * @version 2009-1-3
 * @see CityConfigManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "cityConfigManager")
public class CityConfigManager
{
    private CityConfigDAO cityConfigDAO = null;

    public CityConfigManager()
    {}

    /**
     * –ﬁ∏ƒµÿ –≈‰÷√
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, CityConfigBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        cityConfigDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * @return the cityConfigDAO
     */
    public CityConfigDAO getCityConfigDAO()
    {
        return cityConfigDAO;
    }

    /**
     * @param cityConfigDAO
     *            the cityConfigDAO to set
     */
    public void setCityConfigDAO(CityConfigDAO cityConfigDAO)
    {
        this.cityConfigDAO = cityConfigDAO;
    }
}
