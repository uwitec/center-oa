/*
 * File Name: TriggerImpl.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.trigger.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.cache.CacheBootstrap;
import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.PublicSQL;
import com.china.center.tools.Mathematica;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.BankBean;
import com.china.centet.yongyin.bean.Bill;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.ProductAmount;
import com.china.centet.yongyin.bean.StatBankBean;
import com.china.centet.yongyin.bean.StatBean;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.BankDAO;
import com.china.centet.yongyin.dao.BillDAO;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.CustomerDAO;
import com.china.centet.yongyin.dao.LocationDAO;
import com.china.centet.yongyin.dao.OutDAO;
import com.china.centet.yongyin.dao.ProductDAO;
import com.china.centet.yongyin.dao.StatDAO;
import com.china.centet.yongyin.trigger.Trigger;


/**
 * 银行统计
 * 
 * @author zhuzhu
 * @version 2007-8-15
 * @see
 * @since
 */
public class TriggerImpl implements Trigger
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log logger = LogFactory.getLog("sec");

    private final Log effLogger = LogFactory.getLog("eff");

    private StatDAO statDAO = null;

    private CommonDAO commonDAO = null;

    private BankDAO bankDAO = null;

    private BillDAO billDAO = null;

    private OutDAO outDAO = null;

    private CustomerDAO customerDAO = null;

    private LocationDAO locationDAO = null;

    private ProductDAO productDAO = null;

    private PublicSQL publicSQL = null;

    private CacheBootstrap cacheBootstrap = null;

    public void statBankEveryDay()
    {
        // 获得昨天
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_MONTH, -1);

        String statId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMMdd");

        List<String> bankList = commonDAO.listAll("t_center_bank");

        // 统计昨天的账单，单后生成统计的数据
        for (String bank : bankList)
        {
            StatBankBean sb = statDAO.findStatBankBean(statId, bank);

            if (sb != null)
            {
                continue;
            }

            // 执行统计
            statBankEveryDayInner(statId, bank);
        }
    }

    private void statBankEveryDayInner(String statId, String bank)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();
        condtion.addCommonCondition("dates", "=", publicSQL.to_date(getF(statId), "yyyy-MM-dd"));

        BankBean bb = bankDAO.findBankByName(bank);

        if (bb == null)
        {
            return;
        }

        condtion.addCondition("bank", "=", bb.getId());

        List<Bill> billList = billDAO.queryBillByCondition(condtion);

        statBankInner(billList, statId, bank);
    }

    /**
     * 每日统计
     * 
     * @param billList
     * @param statId
     * @param llastId
     * @param bank
     * @param isFlag
     * @return
     */
    private StatBankBean statBankInner(List<Bill> billList, String statId, String bank)
    {
        StatBankBean news = new StatBankBean();

        try
        {
            // 统计本月的
            double in = 0.0d;
            double out = 0.0d;
            for (Bill bill : billList)
            {
                if (bill.getType() == Constant.BILL_RECIVE)
                {
                    Mathematica math = new Mathematica(in);

                    in = math.add(bill.getMoneys()).toDouble();
                }

                if (bill.getType() == Constant.BILL_PAY)
                {
                    Mathematica math = new Mathematica(out);

                    out = math.add(bill.getMoneys()).toDouble();
                }
            }

            news.setInMoney(in);

            news.setOutMoney(out);

            // 获得结余
            Mathematica math = new Mathematica(in);

            news.setTatolMoney(math.subtract(out).toDouble());

            news.setBank(bank);

            news.setStatId(statId);

            news.setLogDate(TimeTools.now_short());

            BankBean bb = bankDAO.findBankByName(bank);

            if (bb != null)
            {
                news.setLocationId(bb.getLocationId());
            }

            statDAO.addStatBank(news);

            logger.info("每日统计银行:" + bank + "." + statId);
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }

        return news;
    }

    /**
     * 统计
     */
    public void statMoney()
    {
        // 获得上个月是否统计
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);

        String lastId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMM");

        cal.add(Calendar.MONTH, -1);

        String llastId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMM");

        try
        {
            List<BankBean> bankList = bankDAO.listEntityBeans();

            // 上月已经统计那就返回(银行数和统计相等)
            int hasCount = statDAO.countByStatId(lastId, true);

            if (hasCount == bankList.size())
            {
                logger.info(lastId + "已经统计!!!");

                return;
            }

            // 进行统计，根据银行统计
            logger.info(lastId + "未完全统计:" + hasCount + ";总计:" + bankList.size());

            ConditionParse condtion = new ConditionParse();

            String begin = lastId + "01";

            Calendar _cal = Calendar.getInstance();

            _cal.add(Calendar.MONTH, -1);

            // 取得结束时间
            String end = lastId + _cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            // 统计
            List<Bill> billList = null;
            for (BankBean temp : bankList)
            {
                _logger.info("统计:" + temp.getName() + '[' + lastId + ']');

                condtion.addWhereStr();
                condtion.addCommonCondition("dates", ">=", publicSQL.to_date(getF(begin),
                    "yyyy-MM-dd"));
                condtion.addCommonCondition("dates", "<=", publicSQL.to_date(getF(end),
                    "yyyy-MM-dd"));
                condtion.addCondition("bank", "=", temp.getId());

                billList = billDAO.queryBillByCondition(condtion);

                // 统计银行
                statBank(billList, lastId, llastId, temp.getName(), true);

                // 清除查询条件
                condtion.clear();
            }

            List<StatBean> oo = statDAO.queryStatByCondition(new ConditionParse());

            for (StatBean statBean : oo)
            {
                System.out.println(statBean);
            }
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }
    }

    private String getF(String src)
    {
        return src.substring(0, 4) + '-' + src.substring(4, 6) + '-' + src.substring(6, 8);
    }

    // 200708
    public StatBean statBank(List<Bill> billList, String statId, String llastId, String bank,
                             boolean isFlag)
    {
        StatBean news = new StatBean();
        try
        {
            StatBean old = statDAO.findStatBeanByBank(llastId, bank.trim());

            // 获得上月的统计结算
            if (old == null)
            {
                old = new StatBean();
            }

            // 统计本月的
            double in = 0.0d;
            double out = 0.0d;
            for (Bill bill : billList)
            {
                if (bill.getType() == Constant.BILL_RECIVE)
                {
                    Mathematica math = new Mathematica(in);

                    in = math.add(bill.getMoneys()).toDouble();
                }

                if (bill.getType() == Constant.BILL_PAY)
                {
                    Mathematica math = new Mathematica(out);

                    out = math.add(bill.getMoneys()).toDouble();
                }
            }

            news.setLastMoney(old.getTatolMoney());

            news.setInMoney(in);
            news.setOutMoney(out);

            // 获得结余
            Mathematica math = new Mathematica(old.getTatolMoney());

            news.setTatolMoney(math.add(in).subtract(out).toDouble());

            news.setBank(bank);

            news.setStatId(statId);

            news.setFlag(true);

            news.setLogDate(TimeTools.now_short());

            if (isFlag)
            {
                // 删除统计，然后增加(防止重复)
                statDAO.deleteStatBean(statId, bank);

                statDAO.addStat(news);
            }
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }

        return news;
    }

    /**
     * 同步产品到产品数量表
     */
    public int synchronizedProduct()
    {

        int count = 0;
        List<LocationBean> locationList = locationDAO.listLocation();

        ProductAmount productAmount = null;

        for (LocationBean locationBean : locationList)
        {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e1)
            {}

            // 查询区域下没有同步的产品
            List<Product> productList = productDAO.queryNotSynchronizedProductByLocationId(locationBean.getId());

            for (Product product : productList)
            {
                try
                {
                    productAmount = productDAO.findProductAmount(product.getId(),
                        locationBean.getId());

                    if (productAmount == null)
                    {
                        ProductAmount amonut = new ProductAmount();

                        amonut.setLocationId(locationBean.getId());

                        amonut.setProductName(product.getName());

                        amonut.setProductId(product.getId());

                        amonut.setNum(0);

                        productDAO.addProductAmount(amonut);

                        count++ ;

                        logger.info("增加了" + product.getName() + "的数量关系!"
                                    + locationBean.getLocationName());
                    }
                }
                catch (Exception e)
                {
                    _logger.error(e, e);
                }
            }
        }

        return count;

    }

    /**
     * @return the statDAO
     */
    public StatDAO getStatDAO()
    {
        return statDAO;
    }

    /**
     * @param statDAO
     *            the statDAO to set
     */
    public void setStatDAO(StatDAO statDAO)
    {
        this.statDAO = statDAO;
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
     * @return the billDAO
     */
    public BillDAO getBillDAO()
    {
        return billDAO;
    }

    /**
     * @param billDAO
     *            the billDAO to set
     */
    public void setBillDAO(BillDAO billDAO)
    {
        this.billDAO = billDAO;
    }

    /**
     * @return the productDAO
     */
    public ProductDAO getProductDAO()
    {
        return productDAO;
    }

    /**
     * @param productDAO
     *            the productDAO to set
     */
    public void setProductDAO(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
    }

    /**
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    /**
     * @param customerDAO
     *            the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
    }

    /**
     * @return the publicSQL
     */
    public PublicSQL getPublicSQL()
    {
        return publicSQL;
    }

    /**
     * @param publicSQL
     *            the publicSQL to set
     */
    public void setPublicSQL(PublicSQL publicSQL)
    {
        this.publicSQL = publicSQL;
    }

    /**
     * @return the bankDAO
     */
    public BankDAO getBankDAO()
    {
        return bankDAO;
    }

    /**
     * @param bankDAO
     *            the bankDAO to set
     */
    public void setBankDAO(BankDAO bankDAO)
    {
        this.bankDAO = bankDAO;
    }

    /**
     * @return the locationDAO
     */
    public LocationDAO getLocationDAO()
    {
        return locationDAO;
    }

    /**
     * @param locationDAO
     *            the locationDAO to set
     */
    public void setLocationDAO(LocationDAO locationDAO)
    {
        this.locationDAO = locationDAO;
    }

    public void printCacheEfficiency()
    {
        Set<Class> set = cacheBootstrap.cacheKeySet();

        for (Class claz : set)
        {
            effLogger.info(claz.getName() + "[MORE]," + cacheBootstrap.moreCacheEfficiency(claz));

            effLogger.info(claz.getName() + "[SINGLE],"
                           + cacheBootstrap.singleCacheEfficiency(claz));
        }
    }

    /**
     * @return the cacheBootstrap
     */
    public CacheBootstrap getCacheBootstrap()
    {
        return cacheBootstrap;
    }

    /**
     * @param cacheBootstrap
     *            the cacheBootstrap to set
     */
    public void setCacheBootstrap(CacheBootstrap cacheBootstrap)
    {
        this.cacheBootstrap = cacheBootstrap;
    }
}
