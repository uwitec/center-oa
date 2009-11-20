/**
 * File Name: ProviderManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-12-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.manager;


import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.customer.bean.ProviderBean;
import com.china.center.oa.customer.bean.ProviderHisBean;
import com.china.center.oa.customer.dao.ProviderDAO;
import com.china.center.oa.customer.dao.ProviderHisDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.TimeTools;


/**
 * ProviderManager
 * 
 * @author zhuzhu
 * @version 2008-12-26
 * @see ProviderManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "providerManager")
public class ProviderManager
{
    private ProviderDAO providerDAO = null;

    private ProviderHisDAO providerHisDAO = null;

    private CommonDAO2 commonDAO2 = null;

    public ProviderManager()
    {}

    /**
     * addBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addBean(User user, ProviderBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getName(), bean.getCode());

        checkAddBean(bean);

        bean.setLogTime(TimeTools.now());
        
        bean.setId(commonDAO2.getSquenceString());
        
        providerDAO.saveEntityBean(bean);
        
        addHis(bean);

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
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, ProviderBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getName());

        checkUpdateBean(bean);

        providerDAO.updateEntityBean(bean);
        
        addHis(bean);

        return true;
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkdelBean(id);

        providerDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * checkHisCustomer
     * 
     * @param user
     * @param cid
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean checkHisProvider(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        providerHisDAO.updateCheckStatus(cid, CustomerConstant.HIS_CHECK_YES);

        return true;
    }

    /**
     * 检查add是否符合逻辑
     * 
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(ProviderBean bean)
        throws MYException
    {
        if (providerDAO.countByUnique(bean.getCode()) > 0)
        {
            throw new MYException("供应商CODE重复");
        }

        if (providerDAO.countProviderByName(bean.getName()) > 0)
        {
            throw new MYException("供应商名称重复");
        }
    }

    /**
     * 修改检查 code不能改变的
     * 
     * @param bean
     * @throws MYException
     */
    private void checkUpdateBean(ProviderBean bean)
        throws MYException
    {
        ProviderBean old = providerDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("供应商不存在");
        }

        bean.setCode(old.getCode());

        bean.setLogTime(old.getLogTime());

        if ( !bean.getName().equals(old.getName()))
        {
            if (providerDAO.countProviderByName(bean.getName()) > 0)
            {
                throw new MYException("供应商名称重复");
            }
        }
    }

    private void checkdelBean(String id)
        throws MYException
    {
        if (providerDAO.countProviderInOut(id) > 0)
        {
            throw new MYException("供应商已经被使用不能删除");
        }
    }

    /**
     * 增加历史记录
     * 
     * @param bean
     */
    private void addHis(ProviderBean bean)
    {
        ProviderHisBean his = new ProviderHisBean();

        BeanUtil.copyProperties(his, bean);

        his.setProviderId(bean.getId());

        his.setCheckStatus(CustomerConstant.HIS_CHECK_NO);

        providerHisDAO.saveEntityBean(his);
    }

    /**
     * @return the providerDAO
     */
    public ProviderDAO getProviderDAO()
    {
        return providerDAO;
    }

    /**
     * @param providerDAO
     *            the providerDAO to set
     */
    public void setProviderDAO(ProviderDAO providerDAO)
    {
        this.providerDAO = providerDAO;
    }

    /**
     * @return the commonDAO2
     */
    public CommonDAO2 getCommonDAO2()
    {
        return commonDAO2;
    }

    /**
     * @param commonDAO2
     *            the commonDAO2 to set
     */
    public void setCommonDAO2(CommonDAO2 commonDAO2)
    {
        this.commonDAO2 = commonDAO2;
    }

    /**
     * @return the providerHisDAO
     */
    public ProviderHisDAO getProviderHisDAO()
    {
        return providerHisDAO;
    }

    /**
     * @param providerHisDAO
     *            the providerHisDAO to set
     */
    public void setProviderHisDAO(ProviderHisDAO providerHisDAO)
    {
        this.providerHisDAO = providerHisDAO;
    }
}
