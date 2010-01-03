/**
 * File Name: CityProfitManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-29<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.manager;


import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.examine.bean.ProfitBean;
import com.china.center.oa.examine.dao.ProfitDAO;
import com.china.center.oa.publics.User;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.TimeTools;


/**
 * CityProfitManager
 * 
 * @author zhuzhu
 * @version 2009-1-29
 * @see
 * @since
 */
@Exceptional
@Bean(name = "profitManager")
public class ProfitManager
{
    private ProfitDAO profitDAO = null;

    /**
     * default constructor
     */
    public ProfitManager()
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
    public synchronized boolean addOrUpdateBean(User user, ProfitBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        // 设置记录时间
        bean.setLogTime(TimeTools.now());

        if (profitDAO.countByCusotmerIdAndLogDate(bean.getCustomerId(), bean.getOrgDate()) > 0)
        {
            // update bean
            profitDAO.updateProfitByCusotmerIdAndLogDate(bean.getCustomerId(), bean.getOrgDate(),
                bean.getProfit());

            return true;
        }

        profitDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * 删除某月的利润
     * 
     * @param user
     * @param year
     * @param month
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public int delBeanByOrgDate(User user, int year, int month)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user);

        if (year < 1000 || month > 12)
        {
            throw new MYException("数据非法");
        }

        String smonth = String.valueOf(month);

        if (month < 10)
        {
            smonth = "0" + smonth;
        }

        int day = TimeTools.getDaysOfMonth(year, month);

        String begin = year + "-" + smonth + "-01";

        String end = year + "-" + smonth + "-" + day;

        return profitDAO.deleteByOrgDate(begin, end);
    }

    /**
     * @return the profitDAO
     */
    public ProfitDAO getProfitDAO()
    {
        return profitDAO;
    }

    /**
     * @param profitDAO
     *            the profitDAO to set
     */
    public void setProfitDAO(ProfitDAO profitDAO)
    {
        this.profitDAO = profitDAO;
    }
}
