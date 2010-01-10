/**
 * File Name: ConsumeManager.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.manager;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.tools.CommonTools;
import com.china.center.tools.JudgeTools;
import com.china.centet.yongyin.bean.PriceAskBean;
import com.china.centet.yongyin.bean.PriceAskProviderBean;
import com.china.centet.yongyin.bean.PriceBean;
import com.china.centet.yongyin.bean.PriceTemplateBean;
import com.china.centet.yongyin.bean.PriceWebBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.constant.PriceConstant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.PriceAskDAO;
import com.china.centet.yongyin.dao.PriceAskProviderDAO;
import com.china.centet.yongyin.dao.PriceDAO;
import com.china.centet.yongyin.dao.PriceTemplateDAO;
import com.china.centet.yongyin.dao.PriceWebDAO;
import com.china.centet.yongyin.vo.PriceAskBeanVO;
import com.china.centet.yongyin.vo.PriceAskProviderBeanVO;
import com.china.centet.yongyin.vo.PriceTemplateBeanVO;
import com.china.centet.yongyin.wrap.PriceTemplateWrap;


/**
 * 会员消费的manager
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
@Bean(name = "priceManager")
public class PriceManager
{
    private final Log _triggerLogger = LogFactory.getLog("trigger");

    private PriceDAO priceDAO = null;

    private PriceWebDAO priceWebDAO = null;

    private PriceAskDAO priceAskDAO = null;

    private CommonDAO commonDAO = null;

    private PriceAskProviderDAO priceAskProviderDAO = null;

    private PriceTemplateDAO priceTemplateDAO = null;

    /**
     * default constructor
     */
    public PriceManager()
    {}

    /**
     * 增加询价
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean addPriceBean(List<PriceBean> beans)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(beans);

        for (PriceBean priceBean : beans)
        {
            priceDAO.saveEntityBean(priceBean);
        }

        return true;
    }

    /**
     * 增加询价
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean addPriceAskBean(final PriceAskBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        priceAskDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * 驳回询价
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean rejectPriceAskBean(String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        PriceAskBean bean = checkAskReject(id);

        bean.setStatus(PriceConstant.PRICE_ASK_STATUS_EXCEPTION);

        bean.setReason(reason);

        priceAskDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * 结束询价
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean endPriceAskBean(final User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        PriceAskBean bean = checkAskEnd(id);

        bean.setStatus(PriceConstant.PRICE_ASK_STATUS_END);

        priceAskDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * @param id
     * @throws MYException
     */
    private PriceAskBean checkAskReject(String id)
        throws MYException
    {
        PriceAskBean bean = priceAskDAO.find(id);

        if (bean == null)
        {
            throw new MYException("询价不存在");
        }

        if (bean.getStatus() != PriceConstant.PRICE_ASK_STATUS_INIT)
        {
            throw new MYException("不能驳回询价");
        }

        return bean;
    }

    /**
     * @param id
     * @throws MYException
     */
    private PriceAskBean checkAskEnd(String id)
        throws MYException
    {
        PriceAskBean bean = priceAskDAO.find(id);

        if (bean == null)
        {
            throw new MYException("询价不存在");
        }

        if (bean.getStatus() != PriceConstant.PRICE_ASK_STATUS_PROCESSING)
        {
            throw new MYException("不能结束询价,询价单不处于询价中");
        }

        return bean;
    }

    /**
     * 修改询价
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean processPriceAskBean(final User user, final PriceAskBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        List<PriceAskProviderBean> item = (List<PriceAskProviderBean>)CommonTools.deepCopy(bean.getItem());

        // 只要STOCK询价，此单自动结束
        if (user.getRole() == Role.STOCK)
        {
            bean.setStatus(PriceConstant.PRICE_ASK_STATUS_END);
        }
        else
        {
            bean.setStatus(PriceConstant.PRICE_ASK_STATUS_PROCESSING);

            item.addAll(priceAskProviderDAO.queryEntityBeansByFK(bean.getId()));
        }

        double min = (double)Integer.MAX_VALUE;

        for (PriceAskProviderBean priceAskProviderBean : item)
        {
            if (priceAskProviderBean.getHasAmount() == PriceConstant.HASAMOUNT_OK)
            {
                min = Math.min(priceAskProviderBean.getPrice(), min);
            }
        }

        bean.setPrice(min == Integer.MAX_VALUE ? 0.0d : min);

        priceAskDAO.updateEntityBean(bean);

        List<PriceAskProviderBean> items = bean.getItem();

        for (PriceAskProviderBean priceAskProviderBean : items)
        {
            priceAskProviderDAO.deleteByProviderId(priceAskProviderBean.getAskId(),
                priceAskProviderBean.getProviderId());

            priceAskProviderDAO.saveEntityBean(priceAskProviderBean);
        }

        return true;
    }

    /**
     * findPriceAskBeanVO
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    public PriceAskBeanVO findPriceAskBeanVO(final String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        PriceAskBeanVO vo = priceAskDAO.findVO(id);

        List<PriceAskProviderBeanVO> items = priceAskProviderDAO.queryEntityVOsByFK(id);

        vo.setItemVO(items);

        return vo;
    }

    /**
     * 修改询价
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional(sensitiveException = {DataAccessException.class})
    public boolean updatePriceBean(final PriceBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        PriceBean old = priceDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("系统内部错误");
        }

        priceDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * check overtime 定时器触发的job
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional(sensitiveException = {DataAccessException.class})
    public boolean checkOverTime_Job()
    {
        _triggerLogger.info("begin checkOverTime_Job...");

        priceAskDAO.checkAndUpdateOverTime();

        _triggerLogger.info("end checkOverTime_Job");

        return true;
    }

    /**
     * 增加网站
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional(sensitiveException = {DataAccessException.class})
    public boolean addPriceWebBean(final PriceWebBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        int count = priceWebDAO.countByUnique(bean.getName());

        if (count > 0)
        {
            throw new MYException("网站名称已经存在");
        }

        priceWebDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * 删除网站
     * 
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean delPriceWebBean(final String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        checkDelPriceWeb(id);

        priceWebDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * @param id
     * @throws MYException
     */
    private void checkDelPriceWeb(final String id)
        throws MYException
    {
        PriceWebBean bean = priceWebDAO.find(id);

        if (bean == null)
        {
            throw new MYException("网站不存在");
        }

        int count = priceDAO.countByPriceWebId(id);

        if (count > 0)
        {
            throw new MYException("网站名称已经被使用,不能删除");
        }

        count = priceTemplateDAO.countByPriceWebId(id);

        if (count > 0)
        {
            throw new MYException("网站名称已经被使用,不能删除");
        }
    }

    /**
     * 删除网站
     * 
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional(sensitiveException = {DataAccessException.class})
    public boolean delPriceBean(final String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        PriceBean bean = priceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("网站价格不存在");
        }

        if (bean.getStatus() == PriceConstant.PRICE_COMMON)
        {
            throw new MYException("只有驳回的网站价格可以删除");
        }

        priceDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * 删除询价
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean delPriceAskBean(final String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        PriceAskBean bean = priceAskDAO.find(id);

        if (bean == null)
        {
            throw new MYException("不存在");
        }

        if ( ! (bean.getStatus() == PriceConstant.PRICE_ASK_STATUS_INIT || bean.getStatus() == PriceConstant.PRICE_ASK_STATUS_EXCEPTION))
        {
            throw new MYException("不能删除");
        }

        priceAskDAO.deleteEntityBean(id);

        priceAskProviderDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /**
     * 增加网站价格录入的模板
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean addPriceTemplateBean(String productId, PriceTemplateBean[] bean)
        throws MYException
    {
        List<PriceTemplateBean> list = priceTemplateDAO.queryEntityBeansByFK(productId);

        if (list.size() != 0)
        {
            throw new MYException("此产品的询价模板已经存在");
        }

        for (PriceTemplateBean priceTemplateBean : bean)
        {
            priceTemplateBean.setProductId(productId);

            priceTemplateDAO.saveEntityBean(priceTemplateBean);
        }

        return true;
    }

    /**
     * 删除网站价格录入的模板
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean delPriceTemplateByProductId(String productId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(productId);

        priceTemplateDAO.deleteEntityBeansByFK(productId);

        return true;
    }

    /**
     * 修改网站价格录入的模板
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean updatePriceTemplateBean(String productId, PriceTemplateBean[] bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(productId, bean);

        priceTemplateDAO.deleteEntityBeansByFK(productId);

        for (PriceTemplateBean priceTemplateBean : bean)
        {
            priceTemplateBean.setProductId(productId);

            priceTemplateDAO.saveEntityBean(priceTemplateBean);
        }

        return true;
    }

    /**
     * 获得PriceTemplateBean的包装类
     * 
     * @param priceTemplateBean
     * @return
     */
    public PriceTemplateWrap findPriceTemplateWrap(String productId)
    {
        PriceTemplateWrap wrap = new PriceTemplateWrap(productId);

        List<PriceTemplateBeanVO> vos = priceTemplateDAO.queryEntityVOsByFK(productId);

        List<PriceWebBean> items = new ArrayList<PriceWebBean>();

        for (PriceTemplateBeanVO priceTemplateBeanVO : vos)
        {
            wrap.setProductName(priceTemplateBeanVO.getProductName());

            wrap.setProductCode(priceTemplateBeanVO.getProductCode());

            PriceWebBean webBean = new PriceWebBean();

            webBean.setId(priceTemplateBeanVO.getPriceWebId());

            webBean.setName(priceTemplateBeanVO.getPriceWebName());

            items.add(webBean);
        }

        wrap.setItems(items);

        return wrap;
    }

    /**
     * @return the priceDAO
     */
    public PriceDAO getPriceDAO()
    {
        return priceDAO;
    }

    /**
     * @param priceDAO
     *            the priceDAO to set
     */
    public void setPriceDAO(PriceDAO priceDAO)
    {
        this.priceDAO = priceDAO;
    }

    /**
     * @return the priceWebDAO
     */
    public PriceWebDAO getPriceWebDAO()
    {
        return priceWebDAO;
    }

    /**
     * @param priceWebDAO
     *            the priceWebDAO to set
     */
    public void setPriceWebDAO(PriceWebDAO priceWebDAO)
    {
        this.priceWebDAO = priceWebDAO;
    }

    /**
     * @return the priceAskDAO
     */
    public PriceAskDAO getPriceAskDAO()
    {
        return priceAskDAO;
    }

    /**
     * @param priceAskDAO
     *            the priceAskDAO to set
     */
    public void setPriceAskDAO(PriceAskDAO priceAskDAO)
    {
        this.priceAskDAO = priceAskDAO;
    }

    /**
     * @return the priceAskProviderDAO
     */
    public PriceAskProviderDAO getPriceAskProviderDAO()
    {
        return priceAskProviderDAO;
    }

    /**
     * @param priceAskProviderDAO
     *            the priceAskProviderDAO to set
     */
    public void setPriceAskProviderDAO(PriceAskProviderDAO priceAskProviderDAO)
    {
        this.priceAskProviderDAO = priceAskProviderDAO;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }

    /**
     * @return the priceTemplateDAO
     */
    public PriceTemplateDAO getPriceTemplateDAO()
    {
        return priceTemplateDAO;
    }

    /**
     * @param priceTemplateDAO
     *            the priceTemplateDAO to set
     */
    public void setPriceTemplateDAO(PriceTemplateDAO priceTemplateDAO)
    {
        this.priceTemplateDAO = priceTemplateDAO;
    }
}
