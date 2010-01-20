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
import com.china.center.oa.customer.bean.ProviderUserBean;
import com.china.center.oa.customer.dao.ProductTypeDAO;
import com.china.center.oa.customer.dao.ProductTypeVSCustomerDAO;
import com.china.center.oa.customer.dao.ProviderDAO;
import com.china.center.oa.customer.dao.ProviderHisDAO;
import com.china.center.oa.customer.dao.ProviderUserDAO;
import com.china.center.oa.customer.vs.ProductTypeVSCustomer;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.Security;
import com.china.center.tools.StringTools;
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

    private ProviderUserDAO providerUserDAO = null;

    private ProductTypeDAO productTypeDAO = null;

    private ProductTypeVSCustomerDAO productTypeVSCustomerDAO = null;

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
     * bingProductTypeToCustmer
     * 
     * @param pid
     * @param productTypeIds
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean bingProductTypeToCustmer(User user, String pid, String[] productTypeIds)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, pid, productTypeIds);

        productTypeVSCustomerDAO.delVSByCustomerId(pid);

        for (String item : productTypeIds)
        {
            ProductTypeVSCustomer bean = new ProductTypeVSCustomer();

            bean.setCustomerId(pid);

            bean.setProductTypeId(item);

            productTypeVSCustomerDAO.saveEntityBean(bean);
        }

        return true;
    }

    /**
     * addUserBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addOrUpdateUserBean(User user, ProviderUserBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getName());

        boolean isAdd = StringTools.isNullOrNone(bean.getId());

        checkUser(bean, isAdd);

        if (isAdd)
        {
            providerUserDAO.saveEntityBean(bean);
        }
        else
        {
            providerUserDAO.updateEntityBean(bean);
        }

        return true;
    }

    /**
     * updateUserPassword
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateUserPassword(User user, String id, String newpwd)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id, newpwd);

        String md5 = Security.getSecurity(newpwd);

        providerUserDAO.updatePassword(id, md5);

        return true;
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean updateUserPwkey(User user, String id, String newpwkey)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id, newpwkey);

        providerUserDAO.updatePwkey(id, newpwkey);

        return true;
    }

    /**
     * checkUser
     * 
     * @param bean
     * @param isAdd
     * @throws MYException
     */
    private void checkUser(ProviderUserBean bean, boolean isAdd)
        throws MYException
    {
        if (isAdd)
        {
            if (providerUserDAO.countByUnique(bean.getName()) > 0)
            {
                throw new MYException("名称重复,请确认操作");
            }

            String md5 = Security.getSecurity(bean.getPassword());

            bean.setPassword(md5);

            bean.setLoginTime(TimeTools.now());
        }
        else
        {
            ProviderUserBean old = providerUserDAO.find(bean.getId());

            if (old == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            if ( !old.getName().equals(bean.getName()))
            {
                if (providerUserDAO.countByUnique(bean.getName()) > 0)
                {
                    throw new MYException("名称重复,请确认操作");
                }
            }

            bean.setPassword(old.getPassword());
        }
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

    /**
     * delBean
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
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

    /**
     * @return the providerUserDAO
     */
    public ProviderUserDAO getProviderUserDAO()
    {
        return providerUserDAO;
    }

    /**
     * @param providerUserDAO
     *            the providerUserDAO to set
     */
    public void setProviderUserDAO(ProviderUserDAO providerUserDAO)
    {
        this.providerUserDAO = providerUserDAO;
    }

    /**
     * @return the productTypeDAO
     */
    public ProductTypeDAO getProductTypeDAO()
    {
        return productTypeDAO;
    }

    /**
     * @param productTypeDAO
     *            the productTypeDAO to set
     */
    public void setProductTypeDAO(ProductTypeDAO productTypeDAO)
    {
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * @return the productTypeVSCustomerDAO
     */
    public ProductTypeVSCustomerDAO getProductTypeVSCustomerDAO()
    {
        return productTypeVSCustomerDAO;
    }

    /**
     * @param productTypeVSCustomerDAO
     *            the productTypeVSCustomerDAO to set
     */
    public void setProductTypeVSCustomerDAO(ProductTypeVSCustomerDAO productTypeVSCustomerDAO)
    {
        this.productTypeVSCustomerDAO = productTypeVSCustomerDAO;
    }
}
