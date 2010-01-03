/**
 * File Name: ExamineFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.facade;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.MYException;
import com.china.center.oa.constant.AuthConstant;
import com.china.center.oa.examine.bean.CityConfigBean;
import com.china.center.oa.examine.bean.CityProfitBean;
import com.china.center.oa.examine.bean.CityProfitExamineBean;
import com.china.center.oa.examine.bean.ExamineBean;
import com.china.center.oa.examine.bean.NewCustomerExamineBean;
import com.china.center.oa.examine.bean.OldCustomerExamineBean;
import com.china.center.oa.examine.bean.ProductExamineBean;
import com.china.center.oa.examine.bean.ProductExamineItemBean;
import com.china.center.oa.examine.bean.ProfitBean;
import com.china.center.oa.examine.bean.ProfitExamineBean;
import com.china.center.oa.examine.manager.CityConfigManager;
import com.china.center.oa.examine.manager.CityProfitManager;
import com.china.center.oa.examine.manager.ExamineManager;
import com.china.center.oa.examine.manager.ProductExamineManager;
import com.china.center.oa.examine.manager.ProfitManager;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.JudgeTools;


/**
 * ExamineFacade
 * 
 * @author zhuzhu
 * @version 2009-1-3
 * @see ExamineFacade
 * @since 1.0
 */
@Bean(name = "examineFacade")
public class ExamineFacade extends AbstarctFacade
{
    private CityConfigManager cityConfigManager = null;

    private ExamineManager examineManager = null;

    private UserManager userManager = null;

    private ProfitManager profitManager = null;

    private CityProfitManager cityProfitManager = null;

    private ProductExamineManager productExamineManager = null;

    /**
     * default constructor
     */
    public ExamineFacade()
    {}

    /**
     * 修改地市考核配置
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateCityConfig(String userId, CityConfigBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CITYCONFIG_OPR))
        {
            return cityConfigManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 修改区域利润
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateCityProfit(String userId, CityProfitBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CITYCONFIG_OPR))
        {
            return cityProfitManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 更具总额进行更新
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateCityProfitByTotal(String userId, String cityId, int total)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, cityId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.CITYCONFIG_OPR))
        {
            return cityProfitManager.updateBeanByTotal(user, cityId, total);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 增加考评
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addExamine(String userId, ExamineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 增加产品考核
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addProductExamine(String userId, ProductExamineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return productExamineManager.addBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 删除产品考核
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean delProductExamine(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return productExamineManager.delBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * configNewCustomerExamine
     * 
     * @param userId
     * @param parentId
     * @param items
     * @return
     * @throws MYException
     */
    public boolean configNewCustomerExamine(String userId, String parentId,
                                            List<NewCustomerExamineBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, parentId, items);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.configNewCustomerExamine(user, parentId, items);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * configOldCustomerExamine
     * 
     * @param userId
     * @param parentId
     * @param items
     * @return
     * @throws MYException
     */
    public boolean configOldCustomerExamine(String userId, String parentId,
                                            List<OldCustomerExamineBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, parentId, items);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.configOldCustomerExamine(user, parentId, items);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 配置毛利率
     * 
     * @param userId
     * @param parentId
     * @param items
     * @return
     * @throws MYException
     */
    public boolean configProfitExamine(String userId, String parentId,
                                       List<ProfitExamineBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, parentId, items);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.configProfitExamine(user, parentId, items);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 配置区域毛利率
     * 
     * @param userId
     * @param parentId
     * @param items
     * @return
     * @throws MYException
     */
    public boolean configCityProfitExamine(String userId, String parentId,
                                           List<CityProfitExamineBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, parentId, items);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.configCityProfitExamine(user, parentId, items);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * configProductExamine
     * 
     * @param userId
     * @param parentId
     * @param items
     * @return
     * @throws MYException
     */
    public boolean configProductExamine(String userId, String parentId,
                                        List<ProductExamineItemBean> items)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, parentId, items);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return productExamineManager.configProductExamine(user, parentId, items);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * updateExamine
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean updateExamine(String userId, ExamineBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.updateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 删除考评
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean delExamine(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.delBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 删除运行考评
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean delExamine2(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.delBean2(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 提交考评
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean submitExamine(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return examineManager.submitBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 提交考评
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean submitProductExamine(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            return productExamineManager.submitBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 业务员通过考核
     * 
     * @param userId
     * @param id
     * @return
     * @throws MYException
     */
    public boolean passExamine(String userId, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_QUERY))
        {
            return examineManager.passBean(user, id);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 业务员驳回考评
     * 
     * @param userId
     * @param id
     * @param reason
     * @return
     * @throws MYException
     */
    public boolean rejectExamine(String userId, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, id);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_QUERY))
        {
            return examineManager.rejectBean(user, id, reason);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 考核变更
     * 
     * @param userId
     * @param profitExamineId
     * @param reason
     * @param newProfit
     * @return
     * @throws MYException
     */
    public boolean updateProfitExamine(String userId, String profitExamineId, String reason,
                                       double newProfit)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, profitExamineId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_UPDATE_COMMIT))
        {
            return examineManager.updateProfitExamine(user, profitExamineId, reason, newProfit);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 增加/更新 客户利润
     * 
     * @param userId
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addOrUpdateProfit(String userId, ProfitBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId, bean);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_PROFIT_OPR))
        {
            return profitManager.addOrUpdateBean(user, bean);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * 删除导入的利润
     * 
     * @param userId
     * @param year
     * @param month
     * @return
     * @throws MYException
     */
    public int delProfitByYearAndMonth(String userId, int year, int month)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(userId);

        User user = userManager.findUser(userId);

        checkUser(user);

        if (containAuth(user, AuthConstant.EXAMINE_PROFIT_OPR))
        {
            return profitManager.delBeanByOrgDate(user, year, month);
        }
        else
        {
            throw noAuth();
        }
    }

    /**
     * @return the cityConfigManager
     */
    public CityConfigManager getCityConfigManager()
    {
        return cityConfigManager;
    }

    /**
     * @param cityConfigManager
     *            the cityConfigManager to set
     */
    public void setCityConfigManager(CityConfigManager cityConfigManager)
    {
        this.cityConfigManager = cityConfigManager;
    }

    /**
     * @return the userManager
     */
    public UserManager getUserManager()
    {
        return userManager;
    }

    /**
     * @param userManager
     *            the userManager to set
     */
    public void setUserManager(UserManager userManager)
    {
        this.userManager = userManager;
    }

    /**
     * @return the examineManager
     */
    public ExamineManager getExamineManager()
    {
        return examineManager;
    }

    /**
     * @param examineManager
     *            the examineManager to set
     */
    public void setExamineManager(ExamineManager examineManager)
    {
        this.examineManager = examineManager;
    }

    /**
     * @return the cityProfitManager
     */
    public CityProfitManager getCityProfitManager()
    {
        return cityProfitManager;
    }

    /**
     * @param cityProfitManager
     *            the cityProfitManager to set
     */
    public void setCityProfitManager(CityProfitManager cityProfitManager)
    {
        this.cityProfitManager = cityProfitManager;
    }

    /**
     * @return the profitManager
     */
    public ProfitManager getProfitManager()
    {
        return profitManager;
    }

    /**
     * @param profitManager
     *            the profitManager to set
     */
    public void setProfitManager(ProfitManager profitManager)
    {
        this.profitManager = profitManager;
    }

    /**
     * @return the productExamineManager
     */
    public ProductExamineManager getProductExamineManager()
    {
        return productExamineManager;
    }

    /**
     * @param productExamineManager
     *            the productExamineManager to set
     */
    public void setProductExamineManager(ProductExamineManager productExamineManager)
    {
        this.productExamineManager = productExamineManager;
    }
}
