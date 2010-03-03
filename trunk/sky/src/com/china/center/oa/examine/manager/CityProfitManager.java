/**
 * File Name: CityProfitManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-29<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.manager;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.examine.bean.CityProfitBean;
import com.china.center.oa.examine.bean.PercentBean;
import com.china.center.oa.examine.dao.CityProfitDAO;
import com.china.center.oa.examine.dao.PercentDAO;
import com.china.center.oa.publics.User;
import com.china.center.tools.JudgeTools;


/**
 * 区域利润的配置
 * 
 * @author ZHUZHU
 * @version 2009-1-29
 * @see
 * @since
 */
@Exceptional
@Bean(name = "cityProfitManager")
public class CityProfitManager
{
    private CityProfitDAO cityProfitDAO = null;

    private PercentDAO percentDAO = null;

    /**
     * default constructor
     */
    public CityProfitManager()
    {}

    /**
     * addBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addBean(User user, CityProfitBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        cityProfitDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * updateBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateBean(User user, CityProfitBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkUpdate(bean);

        cityProfitDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * 根据总额更新
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateBeanByTotal(User user, String cityId, int total)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cityId);

        List<PercentBean> list = percentDAO.listEntityBeans();

        for (PercentBean percentBean : list)
        {
            int month = percentBean.getMonth();

            double profit = (total / 100.0d) * percentBean.getPercent();

            cityProfitDAO.updateByCityAndMonth(cityId, month, profit);
        }

        return true;
    }

    /**
     * checkUpdate
     * 
     * @param bean
     * @throws MYException
     */
    private void checkUpdate(CityProfitBean bean)
        throws MYException
    {
        CityProfitBean old = cityProfitDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据不完整,请重新操作");
        }

        // 防止数据错误，这里是修正数据
        bean.setCityId(old.getCityId());

        bean.setMonth(old.getMonth());
    }

    /**
     * @return the cityProfitDAO
     */
    public CityProfitDAO getCityProfitDAO()
    {
        return cityProfitDAO;
    }

    /**
     * @param cityProfitDAO
     *            the cityProfitDAO to set
     */
    public void setCityProfitDAO(CityProfitDAO cityProfitDAO)
    {
        this.cityProfitDAO = cityProfitDAO;
    }

    /**
     * @return the percentDAO
     */
    public PercentDAO getPercentDAO()
    {
        return percentDAO;
    }

    /**
     * @param percentDAO
     *            the percentDAO to set
     */
    public void setPercentDAO(PercentDAO percentDAO)
    {
        this.percentDAO = percentDAO;
    }
}
