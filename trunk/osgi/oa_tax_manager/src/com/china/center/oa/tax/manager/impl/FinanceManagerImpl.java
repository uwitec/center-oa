/**
 * File Name: FinanceManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.manager.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.tax.bean.CheckViewBean;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.constanst.CheckConstant;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.dao.CheckViewDAO;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.FinanceItemDAO;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.helper.TaxHelper;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * FinanceManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-2-7
 * @see FinanceManagerImpl
 * @since 1.0
 */
@Exceptional
public class FinanceManagerImpl implements FinanceManager
{
    private final Log operationLog = LogFactory.getLog("opr");

    private FinanceDAO financeDAO = null;

    private CommonDAO commonDAO = null;

    private CheckViewDAO checkViewDAO = null;

    private FinanceItemDAO financeItemDAO = null;

    private TaxDAO taxDAO = null;

    /**
     * default constructor
     */
    public FinanceManagerImpl()
    {
    }

    public boolean addFinanceBeanWithoutTransactional(User user, FinanceBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getItemList());

        bean.setId(commonDAO.getSquenceString20());

        bean.setName(bean.getId());

        bean.setCreaterId(user.getStafferId());

        // 允许自己制定凭证日期
        if (StringTools.isNullOrNone(bean.getFinanceDate()))
        {
            bean.setFinanceDate(TimeTools.now_short());
        }

        // 校验凭证时间不能大于当前时间,也不能小于最近的结算时间
        if (bean.getFinanceDate().compareTo(TimeTools.now_short()) > 0)
        {
            throw new MYException("凭证时间不能大于[%s]", TimeTools.now_short());
        }

        // 入库时间
        bean.setLogTime(TimeTools.now());

        // 默认纳税实体
        if (bean.getType() == TaxConstanst.FINANCE_TYPE_MANAGER
            && StringTools.isNullOrNone(bean.getDutyId()))
        {
            bean.setDutyId(PublicConstant.DEFAULR_DUTY_ID);
        }

        if (bean.getType() == TaxConstanst.FINANCE_TYPE_DUTY
            && StringTools.isNullOrNone(bean.getDutyId()))
        {
            throw new MYException("税务凭证必须有纳税实体的属性");
        }

        List<FinanceItemBean> itemList = bean.getItemList();

        Map<String, List<FinanceItemBean>> pareMap = new HashMap<String, List<FinanceItemBean>>();

        long inTotal = 0;

        long outTotal = 0;

        // 整理出凭证对(且校验凭证的合法性)
        for (FinanceItemBean financeItemBean : itemList)
        {
            financeItemBean.setId(commonDAO.getSquenceString20());

            financeItemBean.setFinanceDate(bean.getFinanceDate());

            financeItemBean.setName(financeItemBean.getId());

            financeItemBean.setPid(bean.getId());

            // 纳税实体
            financeItemBean.setDutyId(bean.getDutyId());

            financeItemBean.setLogTime(TimeTools.now());

            String taxId = financeItemBean.getTaxId();

            if (StringTools.isNullOrNone(taxId))
            {
                throw new MYException("缺少科目信息,请确认操作");
            }

            TaxBean tax = taxDAO.find(taxId);

            if (tax == null)
            {
                throw new MYException("科目不存在,请确认操作");
            }

            // 检查辅助核算项
            checkItem(financeItemBean, tax);

            // 拷贝凭证的父级ID
            TaxHelper.copyParent(financeItemBean, tax);

            String key = financeItemBean.getPareId();

            if (pareMap.get(key) == null)
            {
                pareMap.put(key, new ArrayList<FinanceItemBean>());
            }

            pareMap.get(key).add(financeItemBean);

            // 必须有一个为0
            if (financeItemBean.getInmoney() * financeItemBean.getOutmoney() != 0)
            {
                throw new MYException("借方金额或者贷方金额不能都不为0");
            }

            inTotal += financeItemBean.getInmoney();

            outTotal += financeItemBean.getOutmoney();
        }

        bean.setInmoney(inTotal);

        bean.setOutmoney(outTotal);

        if (inTotal != outTotal)
        {
            throw new MYException("总借[%s],总贷[%s]不等,凭证增加错误", MathTools
                .formatNum(inTotal / (TaxConstanst.DOUBLE_TO_INT + 0.0d)), MathTools
                .formatNum(outTotal / (TaxConstanst.DOUBLE_TO_INT + 0.0d)));
        }

        // CORE 核对借贷必相等的原则
        checkPare(pareMap);

        financeDAO.saveEntityBean(bean);

        financeItemDAO.saveAllEntityBeans(itemList);

        return true;
    }

    /**
     * checkItem
     * 
     * @param financeItemBean
     * @param tax
     * @throws MYException
     */
    private void checkItem(FinanceItemBean financeItemBean, TaxBean tax)
        throws MYException
    {
        if (tax.getUnit() == TaxConstanst.TAX_CHECK_YES
            && StringTools.isNullOrNone(financeItemBean.getUnitId()))
        {
            throw new MYException("科目[%s]下辅助核算型-单位必须存在,请确认操作", tax.getName());
        }

        if (tax.getDepartment() == TaxConstanst.TAX_CHECK_YES
            && StringTools.isNullOrNone(financeItemBean.getDepartmentId()))
        {
            throw new MYException("科目[%s]下辅助核算型-部门必须存在,请确认操作", tax.getName());
        }

        if (tax.getStaffer() == TaxConstanst.TAX_CHECK_YES
            && StringTools.isNullOrNone(financeItemBean.getStafferId()))
        {
            throw new MYException("科目[%s]下辅助核算型-职员必须存在,请确认操作", tax.getName());
        }

        if (tax.getProduct() == TaxConstanst.TAX_CHECK_YES
            && StringTools.isNullOrNone(financeItemBean.getProductId()))
        {
            throw new MYException("科目[%s]下辅助核算型-产品必须存在,请确认操作", tax.getName());
        }

        if (tax.getDepot() == TaxConstanst.TAX_CHECK_YES
            && StringTools.isNullOrNone(financeItemBean.getDepotId()))
        {
            throw new MYException("科目[%s]下辅助核算型-仓库必须存在,请确认操作", tax.getName());
        }

        if (tax.getDuty() == TaxConstanst.TAX_CHECK_YES
            && StringTools.isNullOrNone(financeItemBean.getDuty2Id()))
        {
            throw new MYException("科目[%s]下辅助核算型-纳税实体必须存在,请确认操作", tax.getName());
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean addFinanceBean(User user, FinanceBean bean)
        throws MYException
    {
        return addFinanceBeanWithoutTransactional(user, bean);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateFinanceBean(User user, FinanceBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getItemList());

        FinanceBean old = financeDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (old.getStatus() == TaxConstanst.FINANCE_STATUS_CHECK)
        {
            throw new MYException("已经被核对(锁定)不能修改,请重新操作");
        }

        bean.setType(old.getType());
        bean.setCreateType(old.getCreateType());
        bean.setStatus(old.getStatus());
        bean.setLogTime(old.getLogTime());
        bean.setCreaterId(old.getCreaterId());

        bean.setName(bean.getId());

        // 允许自己制定凭证日期
        if (StringTools.isNullOrNone(bean.getFinanceDate()))
        {
            bean.setFinanceDate(TimeTools.now_short());
        }

        // 校验凭证时间不能大于当前时间,也不能小于最近的结算时间
        if (bean.getFinanceDate().compareTo(TimeTools.now_short()) > 0)
        {
            throw new MYException("凭证时间不能大于[%s]", TimeTools.now_short());
        }

        // 默认纳税实体
        if (bean.getType() == TaxConstanst.FINANCE_TYPE_MANAGER
            && StringTools.isNullOrNone(bean.getDutyId()))
        {
            bean.setDutyId(PublicConstant.DEFAULR_DUTY_ID);
        }

        if (bean.getType() == TaxConstanst.FINANCE_TYPE_DUTY
            && StringTools.isNullOrNone(bean.getDutyId()))
        {
            throw new MYException("税务凭证必须有纳税实体的属性");
        }

        List<FinanceItemBean> itemList = bean.getItemList();

        Map<String, List<FinanceItemBean>> pareMap = new HashMap<String, List<FinanceItemBean>>();

        long inTotal = 0;

        long outTotal = 0;

        // 整理出凭证对(且校验凭证的合法性)
        for (FinanceItemBean financeItemBean : itemList)
        {
            financeItemBean.setId(commonDAO.getSquenceString20());

            financeItemBean.setFinanceDate(bean.getFinanceDate());

            financeItemBean.setName(financeItemBean.getId());

            financeItemBean.setPid(bean.getId());

            // 纳税实体
            financeItemBean.setDutyId(bean.getDutyId());

            financeItemBean.setLogTime(TimeTools.now());

            String taxId = financeItemBean.getTaxId();

            if (StringTools.isNullOrNone(taxId))
            {
                throw new MYException("缺少科目信息,请确认操作");
            }

            TaxBean tax = taxDAO.find(taxId);

            if (tax == null)
            {
                throw new MYException("科目不存在,请确认操作");
            }

            // 检查辅助核算项
            checkItem(financeItemBean, tax);

            // 拷贝凭证的父级ID
            TaxHelper.copyParent(financeItemBean, tax);

            String key = financeItemBean.getPareId();

            if (pareMap.get(key) == null)
            {
                pareMap.put(key, new ArrayList<FinanceItemBean>());
            }

            pareMap.get(key).add(financeItemBean);

            // 必须有一个为0
            if (financeItemBean.getInmoney() * financeItemBean.getOutmoney() != 0)
            {
                throw new MYException("借方金额或者贷方金额不能都不为0");
            }

            inTotal += financeItemBean.getInmoney();

            outTotal += financeItemBean.getOutmoney();
        }

        bean.setInmoney(inTotal);

        bean.setOutmoney(outTotal);

        if (inTotal != outTotal)
        {
            throw new MYException("总借[%s],总贷[%s]不等,凭证增加错误", MathTools
                .formatNum(inTotal / (TaxConstanst.DOUBLE_TO_INT + 0.0d)), MathTools
                .formatNum(outTotal / (TaxConstanst.DOUBLE_TO_INT + 0.0d)));
        }

        if (bean.getInmoney() != old.getInmoney())
        {
            throw new MYException("原单据金额[%s],当前金额[%s]不等,凭证增加错误", MathTools
                .formatNum(bean.getInmoney() / (TaxConstanst.DOUBLE_TO_INT + 0.0d)), MathTools
                .formatNum(old.getInmoney() / (TaxConstanst.DOUBLE_TO_INT + 0.0d)));
        }

        // CORE 核对借贷必相等的原则
        checkPare(pareMap);

        financeDAO.updateEntityBean(bean);

        // 先删除
        financeItemDAO.deleteEntityBeansByFK(bean.getId());

        financeItemDAO.saveAllEntityBeans(itemList);

        return true;
    }

    /**
     * checkPare
     * 
     * @param pareMap
     * @throws MYException
     */
    private void checkPare(Map<String, List<FinanceItemBean>> pareMap)
        throws MYException
    {
        // 核对借贷必相等的原则
        Set<String> keySet = pareMap.keySet();

        for (String key : keySet)
        {
            List<FinanceItemBean> pareList = pareMap.get(key);

            long inMoney = 0;

            long outMoney = 0;

            for (FinanceItemBean item : pareList)
            {
                inMoney += item.getInmoney();

                outMoney += item.getOutmoney();
            }

            if (inMoney != outMoney)
            {
                throw new MYException("借[%d],贷[%d]不等,凭证错误", inMoney, outMoney);
            }
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteFinanceBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        FinanceBean old = financeDAO.find(id);

        if (old == null)
        {
            throw new MYException("数据错误,请重新操作");
        }

        if (old.getCreateType() != TaxConstanst.FINANCE_CREATETYPE_HAND)
        {
            throw new MYException("只能删除手工凭证,请重新操作");
        }

        if (old.getStatus() == TaxConstanst.FINANCE_STATUS_CHECK)
        {
            throw new MYException("已经被核对(锁定)不能删除,请重新操作");
        }

        // 获取凭证项
        old.setItemList(financeItemDAO.queryEntityBeansByFK(id));

        // 删除凭证
        financeDAO.deleteEntityBean(id);

        // 删除凭证项
        financeItemDAO.deleteEntityBeansByFK(id);

        // 删除需要记录操作日志
        operationLog.info(user.getStafferName() + "删除了凭证:" + old);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateFinanceCheck(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        financeDAO.updateCheck(id, reason);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean checks2(User user, String id, int type, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        String tableName = "";

        if (type == CheckConstant.CHECK_TYPE_COMPOSE)
        {
            tableName = "T_CENTER_COMPOSE";
        }
        else if (type == CheckConstant.CHECK_TYPE_CHANGE)
        {
            tableName = "T_CENTER_PRICE_CHANGE";
        }
        else if (type == CheckConstant.CHECK_TYPE_INBILL)
        {
            tableName = "T_CENTER_INBILL";
        }
        else if (type == CheckConstant.CHECK_TYPE_OUTBILL)
        {
            tableName = "T_CENTER_OUTBILL";
        }
        else if (type == CheckConstant.CHECK_TYPE_STOCK)
        {
            tableName = "T_CENTER_STOCK";
        }
        else if (type == CheckConstant.CHECK_TYPE_BUY)
        {
            tableName = "T_CENTER_OUT";
        }
        else if (type == CheckConstant.CHECK_TYPE_CUSTOMER)
        {
            tableName = "T_CENTER_CUSTOMER_NOW";
        }
        else if (type == CheckConstant.CHECK_TYPE_BASEBALANCE)
        {
            tableName = "T_CENTER_OUTBALANCE";
        }
        else
        {
            throw new MYException("数据错误,请确认操作");
        }

        checkViewDAO.updateCheck(tableName, id, reason);

        checkViewDAO.deleteEntityBean(id);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean checks(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        CheckViewBean bean = checkViewDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        return checks2(user, id, bean.getType(), reason);
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteChecks(User user, String id)
        throws MYException
    {
        checkViewDAO.deleteEntityBean(id);
        return true;
    }

    /**
     * @return the financeDAO
     */
    public FinanceDAO getFinanceDAO()
    {
        return financeDAO;
    }

    /**
     * @param financeDAO
     *            the financeDAO to set
     */
    public void setFinanceDAO(FinanceDAO financeDAO)
    {
        this.financeDAO = financeDAO;
    }

    /**
     * @return the financeItemDAO
     */
    public FinanceItemDAO getFinanceItemDAO()
    {
        return financeItemDAO;
    }

    /**
     * @param financeItemDAO
     *            the financeItemDAO to set
     */
    public void setFinanceItemDAO(FinanceItemDAO financeItemDAO)
    {
        this.financeItemDAO = financeItemDAO;
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
     * @return the checkViewDAO
     */
    public CheckViewDAO getCheckViewDAO()
    {
        return checkViewDAO;
    }

    /**
     * @param checkViewDAO
     *            the checkViewDAO to set
     */
    public void setCheckViewDAO(CheckViewDAO checkViewDAO)
    {
        this.checkViewDAO = checkViewDAO;
    }

    /**
     * @return the taxDAO
     */
    public TaxDAO getTaxDAO()
    {
        return taxDAO;
    }

    /**
     * @param taxDAO
     *            the taxDAO to set
     */
    public void setTaxDAO(TaxDAO taxDAO)
    {
        this.taxDAO = taxDAO;
    }
}
