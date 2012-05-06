/**
 * File Name: OutManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.manager.impl;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.center.china.osgi.publics.file.writer.WriteFile;
import com.center.china.osgi.publics.file.writer.WriteFileFactory;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.credit.bean.CreditLevelBean;
import com.china.center.oa.credit.dao.CreditCoreDAO;
import com.china.center.oa.credit.dao.CreditLevelDAO;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.constant.CustomerConstant;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.manager.CustomerManager;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.oa.note.constant.ShortMessageConstant;
import com.china.center.oa.note.dao.ShortMessageTaskDAO;
import com.china.center.oa.note.manager.HandleMessage;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.constant.ProductConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.helper.StorageRelationHelper;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.product.vs.StorageRelationBean;
import com.china.center.oa.product.wrap.ProductChangeWrap;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.InvoiceCreditBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.NotifyBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.bean.UserBean;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.constant.PluginNameConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.PublicLock;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.InvoiceCreditDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.helper.OATools;
import com.china.center.oa.publics.manager.FatalNotify;
import com.china.center.oa.publics.manager.NotifyManager;
import com.china.center.oa.publics.vo.InvoiceCreditVO;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.BaseBalanceBean;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.ConsignBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.bean.OutUniqueBean;
import com.china.center.oa.sail.bean.SailConfBean;
import com.china.center.oa.sail.bean.SailTranApplyBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.constanst.SailConstant;
import com.china.center.oa.sail.dao.BaseBalanceDAO;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.ConsignDAO;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.dao.OutUniqueDAO;
import com.china.center.oa.sail.dao.SailConfigDAO;
import com.china.center.oa.sail.dao.SailTranApplyDAO;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.helper.YYTools;
import com.china.center.oa.sail.listener.OutListener;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.sail.manager.SailConfigManager;
import com.china.center.oa.sail.vo.BaseBalanceVO;
import com.china.center.oa.sail.vo.OutVO;
import com.china.center.oa.sail.wrap.CreditWrap;
import com.china.center.osgi.dym.DynamicBundleTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.ParamterMap;
import com.china.center.tools.RandomTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.center.tools.WriteFileBuffer;


/**
 * OutManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see OutManagerImpl
 * @since 1.0
 */
public class OutManagerImpl extends AbstractListenerManager<OutListener> implements OutManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log operationLog = LogFactory.getLog("opr");

    private final Log importLog = LogFactory.getLog("sec");

    private final Log triggerLog = LogFactory.getLog("trigger");

    private final Log fatalLog = LogFactory.getLog("fatal");

    private LocationDAO locationDAO = null;

    private CommonDAO commonDAO = null;

    private ProductDAO productDAO = null;

    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private InvoiceCreditDAO invoiceCreditDAO = null;

    private OutDAO outDAO = null;

    private DepotDAO depotDAO = null;

    private BaseDAO baseDAO = null;

    private DutyDAO dutyDAO = null;

    private ConsignDAO consignDAO = null;

    private CustomerDAO customerDAO = null;

    private CustomerManager customerManager = null;

    private ParameterDAO parameterDAO = null;

    private CreditLevelDAO creditLevelDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private CreditCoreDAO creditCoreDAO = null;

    private SailTranApplyDAO sailTranApplyDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private SailConfigDAO sailConfigDAO = null;

    private OutUniqueDAO outUniqueDAO = null;

    private NotifyManager notifyManager = null;

    private InvoiceDAO invoiceDAO = null;

    private FatalNotify fatalNotify = null;

    private BaseBalanceDAO baseBalanceDAO = null;

    private OutBalanceDAO outBalanceDAO = null;

    private SailConfigManager sailConfigManager = null;

    private StorageRelationManager storageRelationManager = null;

    private StorageRelationDAO storageRelationDAO = null;

    /**
     * 短信最大停留时间
     */
    private int internal = 300000;

    private PlatformTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public OutManagerImpl()
    {
    }

    /**
     * 增加(修改)
     * 
     * @param locationBean
     * @return String 销售单的ID
     * @throws Exception
     */
    public String addOut(final OutBean outBean, final Map dataMap, final User user)
        throws MYException
    {
        ParamterMap request = new ParamterMap(dataMap);

        String fullId = request.getParameter("fullId");

        if (StringTools.isNullOrNone(fullId))
        {
            // 先保存
            String id = getAll(commonDAO.getSquence());

            LocationBean location = locationDAO.find(outBean.getLocationId());

            if (location == null)
            {
                _logger.error("区域不存在:" + outBean.getLocationId());

                throw new MYException("区域不存在:" + outBean.getLocationId());
            }

            String flag = location.getCode();

            String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");

            fullId = flag + time + id;

            outBean.setId(getOutId(id));

            outBean.setFullId(fullId);

            dataMap.put("modify", false);
        }
        else
        {
            dataMap.put("modify", true);
        }

        final String totalss = request.getParameter("totalss");

        outBean.setTotal(MathTools.parseDouble(totalss));

        outBean.setStatus(OutConstant.STATUS_SAVE);

        outBean.setInway(OutConstant.IN_WAY_NO);

        // 获得baseList
        final String[] nameList = request.getParameter("nameList").split("~");
        final String[] idsList = request.getParameter("idsList").split("~");
        final String[] showIdList = request.getParameter("showIdList").split("~");
        final String[] showNameList = request.getParameter("showNameList").split("~");
        final String[] unitList = request.getParameter("unitList").split("~");
        final String[] amontList = request.getParameter("amontList").split("~");

        // 含税价
        final String[] priceList = request.getParameter("priceList").split("~");

        // 输入价格
        final String[] inputPriceList = request.getParameter("inputPriceList").split("~");

        // 显示成本(只有V5有)
        final String[] showCostList = request.getParameter("showCostList").split("~");

        // 成本
        final String[] desList = request.getParameter("desList").split("~");

        final String[] otherList = request.getParameter("otherList").split("~");

        _logger.info(fullId + "/nameList/" + request.getParameter("nameList"));

        _logger.info(fullId + "/idsList/" + request.getParameter("idsList"));

        _logger.info(fullId + "/totalList/" + request.getParameter("totalList"));

        _logger.info(fullId + "/price/" + request.getParameter("priceList"));

        _logger.info(fullId + "/inputPriceList/" + request.getParameter("inputPriceList"));

        _logger.info(fullId + "/showCostList/" + request.getParameter("showCostList"));

        _logger.info(fullId + "/desList/" + request.getParameter("desList"));

        _logger.info(fullId + "/otherList/" + request.getParameter("otherList"));

        // 组织BaseBean
        double ttatol = 0.0d;

        for (int i = 0; i < nameList.length; i++ )
        {
            ttatol += (Double.parseDouble(priceList[i]) * Integer.parseInt(amontList[i]));
        }

        outBean.setTotal(ttatol);

        outBean.setCurcredit(0.0d);

        outBean.setStaffcredit(0.0d);

        if (StringTools.isNullOrNone(outBean.getCustomerId())
            || CustomerConstant.PUBLIC_CUSTOMER_ID.equals(outBean.getCustomerId()))
        {
            outBean.setCustomerId(CustomerConstant.PUBLIC_CUSTOMER_ID);

            outBean.setCustomerName(CustomerConstant.PUBLIC_CUSTOMER_NAME);
        }

        if (StringTools.isNullOrNone(outBean.getInvoiceId()))
        {
            outBean.setHasInvoice(OutConstant.HASINVOICE_NO);
        }
        else
        {
            outBean.setHasInvoice(OutConstant.HASINVOICE_YES);
        }

        // 赠送的价格为0
        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
        {
            outBean.setTotal(0.0d);
        }

        // 行业属性
        setInvoiceId(outBean);

        final StafferBean stafferBean = stafferDAO.find(user.getStafferId());

        // 增加管理员操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    if ((Boolean)dataMap.get("modify"))
                    {
                        outDAO.deleteEntityBean(outBean.getFullId());

                        baseDAO.deleteEntityBeansByFK(outBean.getFullId());
                    }

                    // 组织BaseBean
                    boolean addSub = false;

                    boolean hasZero = false;

                    double total = 0.0d;

                    List<BaseBean> baseList = new ArrayList();

                    boolean sailJiuBi = false;
                    List<ProductBean> tempProductList = new ArrayList();

                    // 处理每个base
                    for (int i = 0; i < nameList.length; i++ )
                    {
                        BaseBean base = new BaseBean();

                        base.setId(commonDAO.getSquenceString());

                        // 允许存在
                        base.setAmount(MathTools.parseInt(amontList[i]));

                        if (base.getAmount() == 0)
                        {
                            continue;
                        }

                        base.setOutId(outBean.getFullId());

                        base.setProductId(idsList[i]);

                        if (StringTools.isNullOrNone(base.getProductId()))
                        {
                            throw new RuntimeException("产品ID为空,数据不完备");
                        }

                        ProductBean product = productDAO.find(base.getProductId());

                        if (product == null)
                        {
                            throw new RuntimeException("产品为空,数据不完备");
                        }

                        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                            && !nameList[i].trim().equals(product.getName().trim()))
                        {
                            throw new RuntimeException("产品名不匹配,请重新操作.申请:" + nameList[i].trim()
                                                       + ".实际:" + product.getName());
                        }

                        // 旧币的产品必须单独销售，不允许和其他的产品类型一起销售
                        if (product.getType() == ProductConstant.PRODUCT_TYPE_NUMISMATICS)
                        {
                            sailJiuBi = true;
                        }

                        tempProductList.add(product);

                        // 产品名称来源于数据库
                        base.setProductName(product.getName());

                        base.setUnit(unitList[i]);

                        // 赠送的价格为0
                        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                            && outBean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
                        {
                            base.setPrice(0.0d);
                        }
                        else
                        {
                            base.setPrice(MathTools.parseDouble(priceList[i]));
                        }

                        if (base.getPrice() == 0)
                        {
                            hasZero = true;
                        }

                        // 销售价格动态获取的
                        base.setValue(base.getAmount() * base.getPrice());

                        total += base.getValue();

                        // 入库单是没有showId的
                        if (showNameList != null && showNameList.length >= (i + 1))
                        {
                            base.setShowId(showIdList[i]);
                            base.setShowName(showNameList[i]);
                        }

                        // 这里需要处理99的其他入库,因为其他入库是没有完成的otherList
                        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
                            && outBean.getOutType() == OutConstant.OUTTYPE_IN_OTHER)
                        {
                            base.setCostPrice(MathTools.parseDouble(desList[i]));
                            base.setCostPriceKey(StorageRelationHelper.getPriceKey(base
                                .getCostPrice()));

                            base.setOwner("0");

                            // 默认仓区
                            DepotpartBean defaultOKDepotpart = depotpartDAO
                                .findDefaultOKDepotpart(outBean.getLocation());

                            if (defaultOKDepotpart == null)
                            {
                                throw new RuntimeException("没有默认的良品仓,请确认操作");
                            }

                            base.setDepotpartId(defaultOKDepotpart.getId());
                        }
                        else
                        {
                            // ele.productid + '-' + ele.price + '-' + ele.stafferid + '-' + ele.depotpartid
                            String[] coreList = otherList[i].split("-");

                            if (coreList.length != 4)
                            {
                                throw new RuntimeException("数据不完备");
                            }

                            // 寻找具体的产品价格位置
                            base.setCostPrice(MathTools.parseDouble(coreList[1]));

                            base.setCostPriceKey(StorageRelationHelper.getPriceKey(base
                                .getCostPrice()));

                            base.setOwner(coreList[2]);

                            base.setDepotpartId(coreList[3]);
                        }

                        // 这里需要核对价格 调拨
                        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
                            && (outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT || outBean
                                .getOutType() == OutConstant.OUTTYPE_IN_DROP))
                        {
                            if ( !MathTools.equal(base.getPrice(), base.getCostPrice()))
                            {
                                throw new RuntimeException("调拨/报废的时候价格必须相等");
                            }
                        }

                        if (StringTools.isNullOrNone(base.getOwner()))
                        {
                            base.setOwner("0");
                        }

                        if ("0".equals(base.getOwner()))
                        {
                            base.setOwnerName("公共");
                        }
                        else
                        {
                            StafferBean sb = stafferDAO.find(base.getOwner());

                            if (sb == null)
                            {
                                throw new RuntimeException("所属职员不存在,请确认操作");
                            }

                            base.setOwnerName(sb.getName());
                        }

                        DepotpartBean deport = depotpartDAO.find(base.getDepotpartId());

                        if (deport == null)
                        {
                            throw new RuntimeException("仓区不存在,请确认操作");
                        }

                        base.setDepotpartName(deport.getName());

                        // 销售单的时候仓库必须一致
                        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                            && !deport.getLocationId().equals(outBean.getLocation()))
                        {
                            throw new RuntimeException("销售必须在一个仓库下面");
                        }

                        // 调拨的时候有bug啊
                        base.setLocationId(outBean.getLocation());

                        // 其实也是成本
                        base.setDescription(desList[i].trim());

                        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                        {
                            // 显示成本(V5新功能)
                            base.setInputPrice(MathTools.parseDouble(showCostList[i]));
                        }
                        else
                        {
                            // 入库单
                            if (inputPriceList != null && inputPriceList.length > i)
                            {
                                // 兼容
                                base.setInputPrice(MathTools.parseDouble(inputPriceList[i]));
                            }
                        }

                        // 公卖
                        if ("0".equals(base.getOwner()))
                        {
                            // 获取销售配置
                            SailConfBean sailConf = sailConfigManager.findProductConf(stafferBean,
                                product);

                            // 总部结算价(产品结算价 * (1 + 总部结算率))
                            base.setPprice(product.getSailPrice()
                                           * (1 + sailConf.getPratio() / 1000.0d));

                            // 事业部结算价(产品结算价 * (1 + 总部结算率 + 事业部结算率))
                            base.setIprice(product.getSailPrice()
                                           * (1 + sailConf.getIratio() / 1000.0d + sailConf
                                               .getPratio() / 1000.0d));

                            // 业务员结算价就是事业部结算价
                            base.setInputPrice(base.getIprice());

                            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                            {
                                // 发现一些异常,这里保护一下
                                if (base.getInputPrice() == 0)
                                {
                                    fatalLog.fatal("error price in sail:" + base);

                                    throw new RuntimeException("业务员结算价不能为0");
                                }
                            }
                        }
                        else
                        {
                            // 私卖
                            base.setPprice(base.getInputPrice());
                            base.setIprice(base.getInputPrice());
                        }

                        baseList.add(base);

                        base.setMtype(outBean.getMtype());

                        // 增加单个产品到base表
                        baseDAO.saveEntityBean(base);

                        addSub = true;
                    }

                    // 旧币的产品必须单独销售(或者都是旧币)，不允许和其他的产品类型一起销售
                    if (sailJiuBi)
                    {
                        for (ProductBean each : tempProductList)
                        {
                            if (each.getType() != ProductConstant.PRODUCT_TYPE_NUMISMATICS)
                            {
                                throw new RuntimeException("旧币的产品必须单独销售(或者都是旧币)，不允许和其他的产品类型一起销售:"
                                                           + each.getName());
                            }
                        }
                    }

                    // 自卖的东西必须先卖掉
                    if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                    {
                        for (BaseBean base : baseList)
                        {
                            if ("0".equals(base.getOwner()))
                            {
                                ConditionParse con = new ConditionParse();
                                con.addWhereStr();
                                con.addCondition("stafferId", "=", user.getStafferId());
                                con.addCondition("productId", "=", base.getProductId());

                                List<StorageRelationBean> selfRelation = storageRelationDAO
                                    .queryEntityBeansByCondition(con);

                                if (ListTools.isEmptyOrNull(selfRelation))
                                {
                                    continue;
                                }

                                int samont = 0;

                                for (StorageRelationBean seach : selfRelation)
                                {
                                    samont += seach.getAmount();
                                }

                                // 看看是否都在里面出售
                                int amount = 0;

                                for (BaseBean each : baseList)
                                {
                                    if (user.getStafferId().equals(each.getOwner())
                                        && base.getProductId().equals(each.getProductId()))
                                    {
                                        amount += each.getAmount();
                                    }
                                }

                                if (samont != amount)
                                {
                                    throw new RuntimeException("必须先销售自己名下的产品["
                                                               + base.getProductName() + "]");
                                }
                            }
                        }
                    }

                    // 销售单强制设置为赠送
                    if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL && hasZero)
                    {
                        outBean.setOutType(OutConstant.OUTTYPE_OUT_PRESENT);
                    }

                    // 重新计算价格
                    outBean.setTotal(total);

                    outBean.setBaseList(baseList);

                    // 保存入库单
                    try
                    {
                        saveOutInner(outBean);
                    }
                    catch (MYException e1)
                    {
                        throw new RuntimeException(e1.toString());
                    }

                    if ( !addSub)
                    {
                        throw new RuntimeException("没有产品数量");
                    }

                    // 防止溢出
                    if (isSwatchToSail(outBean.getFullId()))
                    {
                        try
                        {
                            checkSwithToSail(outBean.getRefOutFullId());
                        }
                        catch (MYException e)
                        {
                            throw new RuntimeException(e.getErrorContent(), e);
                        }
                    }

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("系统错误，请联系管理员:" + e);
        }

        _logger.info(user.getStafferName() + "/" + user.getName() + "/ADD:" + outBean);

        return fullId;
    }

    private void setInvoiceId(final OutBean outBean)
    {
        // 行业
        StafferBean sb = stafferDAO.find(outBean.getStafferId());

        outBean.setIndustryId(sb.getIndustryId());

        outBean.setIndustryId2(sb.getIndustryId2());

        outBean.setIndustryId3(sb.getIndustryId3());
    }

    /**
     * 领样转销售
     */
    public String addSwatchToSail(final User user, final OutBean outBean)
        throws MYException
    {
        if (OutHelper.isManagerSail(outBean))
        {
            throw new MYException("销售-个人领样转销售只能在收藏品下,请重新操作");
        }

        // 先保存
        String id = getAll(commonDAO.getSquence());

        LocationBean location = locationDAO.find(outBean.getLocationId());

        if (location == null)
        {
            _logger.error("区域不存在:" + outBean.getLocationId());

            throw new MYException("区域不存在:" + outBean.getLocationId());
        }

        String flag = location.getCode();

        String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");

        String fullId = flag + time + id;

        outBean.setId(getOutId(id));

        outBean.setFullId(fullId);

        outBean.setStatus(OutConstant.STATUS_SAVE);

        setInvoiceId(outBean);

        // 增加管理员操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    double total = 0.0d;

                    // 组织BaseBean
                    for (BaseBean base : outBean.getBaseList())
                    {
                        base.setId(commonDAO.getSquenceString());

                        base.setOutId(outBean.getFullId());

                        base.setMtype(outBean.getMtype());

                        // 增加单个产品到base表
                        baseDAO.saveEntityBean(base);

                        total += base.getAmount() * base.getPrice();
                    }

                    outBean.setTotal(total);

                    try
                    {
                        // 保存入库单
                        saveOutInner(outBean);
                    }
                    catch (MYException e)
                    {
                        throw new RuntimeException(e.toString());
                    }

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("系统错误，请联系管理员:" + e.getMessage());
        }

        return fullId;
    }

    public String coloneOutAndSubmitAffair(final OutBean outBean, final User user, final int type)
        throws MYException
    {
        // LOCK 自动生成入库单
        synchronized (PublicLock.PRODUCT_CORE)
        {
            String result = "";

            try
            {
                // 增加管理员操作在数据库事务中完成
                TransactionTemplate tran = new TransactionTemplate(transactionManager);

                result = (String)tran.execute(new TransactionCallback()
                {
                    public Object doInTransaction(TransactionStatus arg0)
                    {
                        try
                        {
                            return coloneOutAndSubmitWithOutAffair(outBean, user, type);
                        }
                        catch (MYException e)
                        {
                            throw new RuntimeException(e.getErrorContent(), e);
                        }
                    }
                });
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                throw new MYException(e.getMessage(), e);
            }

            return result;
        }
    }

    /**
     * 对外开放(锁和事务由调用的方法保证)
     */
    public String coloneOutAndSubmitWithOutAffair(OutBean outBean, User user, int type)
        throws MYException
    {
        String fullId = coloneOutWithoutAffair(outBean, user, type);

        // 提交(上级已经使用全局锁了)
        this.submitWithOutAffair(fullId, user, type);

        return fullId;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public String coloneOutWithAffair(final OutBean outBean, final User user, int type)
        throws MYException
    {
        String fullId = coloneOutWithoutAffair(outBean, user, type);

        return fullId;
    }

    public String coloneOutWithoutAffair(final OutBean outBean, final User user, int type)
        throws MYException
    {
        // 先保存
        String id = getAll(commonDAO.getSquence());

        LocationBean location = locationDAO.find(outBean.getLocationId());

        if (location == null)
        {
            _logger.error("区域不存在:" + outBean.getLocationId());

            throw new MYException("区域不存在:" + outBean.getLocationId());
        }

        // 退货入库的逻辑
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH)
        {
            // 查询是否被关联
            ConditionParse con = new ConditionParse();

            con.addWhereStr();

            con.addCondition("OutBean.refOutFullId", "=", outBean.getRefOutFullId());

            con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

            con.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SAVE);

            con.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_SWATCH);

            int count = outDAO.countByCondition(con.toString());

            if (count > 0)
            {
                throw new MYException("此领样已经申请退货请处理结束后再申请");
            }
        }

        String flag = location.getCode();

        String time = TimeTools.now("yyMMddHHmm");

        final String fullId = flag + time + id;

        outBean.setId(getOutId(id));

        outBean.setFullId(fullId);

        // 保存库单
        outBean.setStatus(OutConstant.STATUS_SAVE);

        setInvoiceId(outBean);

        // 保存入库单
        saveOutInner(outBean);

        List<BaseBean> list = outBean.getBaseList();

        for (BaseBean baseBean : list)
        {
            baseBean.setId(commonDAO.getSquenceString());

            baseBean.setOutId(fullId);

            baseBean.setMtype(outBean.getMtype());

            // 增加单个产品到base表
            baseDAO.saveEntityBean(baseBean);
        }

        return fullId;
    }

    /**
     * 提交(包括领样退库和销售退库)
     * 
     * @param outBean
     * @param user
     * @return
     * @throws Exception
     */
    public int submit(final String fullId, final User user, final int storageType)
        throws MYException
    {
        // LOCK 库存提交(当是入库单的时候是变动库存的)
        synchronized (PublicLock.PRODUCT_CORE)
        {
            Integer result = 0;

            try
            {
                // 增加管理员操作在数据库事务中完成
                TransactionTemplate tran = new TransactionTemplate(transactionManager);

                result = (Integer)tran.execute(new TransactionCallback()
                {
                    public Object doInTransaction(TransactionStatus arg0)
                    {
                        try
                        {
                            return submitWithOutAffair(fullId, user, storageType);
                        }
                        catch (MYException e)
                        {
                            _logger.error(e, e);

                            throw new RuntimeException(e.getErrorContent(), e);
                        }
                    }
                });
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                throw new MYException(e.getMessage());
            }

            return result;
        }
    }

    /**
     * 暂时没有对外开放
     */
    private int submitWithOutAffair(final String fullId, final User user, int type)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 检查日志核对
        int outStatusInLog = this.findOutStatusInLog(outBean.getFullId());

        if (outStatusInLog != -1 && outStatusInLog != OutConstant.STATUS_REJECT
            && outStatusInLog != outBean.getStatus())
        {
            String msg = "严重错误,当前单据的状态应该是:" + OutHelper.getStatus(outStatusInLog) + ",而不是"
                         + OutHelper.getStatus(outBean.getStatus()) + ".请联系管理员确认此单的正确状态!";

            loggerError(outBean.getFullId() + ":" + msg);

            throw new MYException(msg);
        }

        final List<BaseBean> baseList = checkSubmit(fullId, outBean);

        // 这里是入库单的直接库存变动(部分)
        processBuyBaseList(user, outBean, baseList, type);

        // CORE 修改库单(销售/入库)的状态(信用额度处理)
        int status = processOutStutus(fullId, user, outBean);

        // 处理在途(销售无关)/调入接受时
        int result = processBuyOutInWay(user, fullId, outBean);

        // 在途改变状态
        if (result != -1)
        {
            status = result;
        }

        // 增加数据库日志
        addOutLog(fullId, user, outBean, "提交", SailConstant.OPR_OUT_PASS, status);

        outBean.setStatus(status);

        notifyOut(outBean, user, 0);

        return status;
    }

    /**
     * @param fullId
     * @param outBean
     * @throws MYException
     */
    private int processBuyOutInWay(final User user, final String fullId, final OutBean outBean)
        throws MYException
    {
        int result = -1;

        // 调出直接变动库存 /回滚也是直接变动库存(CORE 这里特殊不产生任何凭证)
        if (OutHelper.isMoveOut(outBean) || OutHelper.isMoveRollBack(outBean))
        {
            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

            String sequence = commonDAO.getSquenceString();

            for (BaseBean element : baseList)
            {
                ProductChangeWrap wrap = new ProductChangeWrap();

                wrap.setDepotpartId(element.getDepotpartId());
                wrap.setPrice(element.getCostPrice());
                wrap.setProductId(element.getProductId());
                if (StringTools.isNullOrNone(element.getOwner()))
                {
                    wrap.setStafferId("0");
                }
                else
                {
                    wrap.setStafferId(element.getOwner());
                }
                wrap.setChange(element.getAmount());
                wrap.setDescription("库单[" + outBean.getFullId() + "]操作");
                wrap.setSerializeId(sequence);

                if (OutHelper.isMoveRollBack(outBean))
                {
                    wrap.setType(StorageConstant.OPR_STORAGE_REDEPLOY_ROLLBACK);
                }
                else
                {
                    wrap.setType(StorageConstant.OPR_STORAGE_REDEPLOY);
                }

                wrap.setRefId(outBean.getFullId());

                storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, false);
            }

            saveUnique(user, outBean);
        }

        // 如果是调入提交
        if (OutHelper.isMoveIn(outBean))
        {
            // 调入的库存(正数增加,负数减少)
            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

            String sequence = commonDAO.getSquenceString();

            // CORE 调入直接变动库存
            for (BaseBean element : baseList)
            {
                ProductChangeWrap wrap = new ProductChangeWrap();

                wrap.setDepotpartId(element.getDepotpartId());
                wrap.setPrice(element.getCostPrice());
                wrap.setProductId(element.getProductId());

                if (StringTools.isNullOrNone(element.getOwner()))
                {
                    wrap.setStafferId("0");
                }
                else
                {
                    wrap.setStafferId(element.getOwner());
                }

                wrap.setChange(element.getAmount());
                wrap.setDescription("库单[" + outBean.getFullId() + "]调入操作");
                wrap.setSerializeId(sequence);
                wrap.setType(StorageConstant.OPR_STORAGE_REDEPLOY);
                wrap.setRefId(outBean.getFullId());

                storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, true);
            }

            // 调入的存入唯一
            saveUnique(user, outBean);

            // 在途结束
            outDAO.updataInWay(fullId, OutConstant.IN_WAY_OVER);

            outDAO.modifyOutStatus(fullId, OutConstant.STATUS_PASS);

            result = OutConstant.STATUS_PASS;

            // -----------------------------------------------------------------//

            // 处理调出变动库存
            String moveOutFullId = outBean.getRefOutFullId();

            OutBean moveOut = outDAO.find(moveOutFullId);

            // 结束调出的单据
            changeMoveOutToEnd(user, moveOut, "自动接收");

            // TAX_ADD 入库单-调拨（调入接受时）
            Collection<OutListener> listenerMapValues = listenerMapValues();

            for (OutListener listener : listenerMapValues)
            {
                listener.onConfirmOutOrBuy(user, outBean);
            }
        }

        // 如果是调出(调出提交)
        if (OutHelper.isMoveOut(outBean))
        {
            outDAO.updataInWay(fullId, OutConstant.IN_WAY);

            importLog.info(fullId + "的在途状态改变成在途");
        }

        // 回滚的
        if (OutHelper.isMoveRollBack(outBean))
        {
            outDAO.updataInWay(fullId, OutConstant.IN_WAY_OVER);

            OutBean moveOut = outDAO.find(outBean.getRefOutFullId());

            // 结束调出的单据
            changeMoveOutToEnd(user, moveOut, "调拨驳回");
        }

        return result;
    }

    /**
     * 结束调出的单据
     * 
     * @param user
     * @param moveOut
     */
    private void changeMoveOutToEnd(final User user, OutBean moveOut, String reason)
    {
        outDAO.updataInWay(moveOut.getFullId(), OutConstant.IN_WAY_OVER);

        // 操作日志
        addOutLog(moveOut.getFullId(), user, moveOut, reason, SailConstant.OPR_OUT_PASS, moveOut
            .getStatus());

        importLog.info(moveOut.getFullId() + "的在途状态改变成在途结束");
    }

    /**
     * @param fullId
     * @param user
     * @param outBean
     * @throws MYException
     */
    private int processOutStutus(final String fullId, final User user, final OutBean outBean)
        throws MYException
    {
        int result = 0;

        int nextStatus = OutConstant.STATUS_SUBMIT;

        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            importLog.info(fullId + ":" + user.getStafferName() + ":" + 1 + ":redrectFrom:"
                           + outBean.getStatus());

            // 销售单处理
            try
            {
                // 分公司经理担保
                if (outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER)
                {
                    nextStatus = OutConstant.STATUS_LOCATION_MANAGER_CHECK;
                }

                // 赠送流程
                if (outBean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
                {
                    nextStatus = OutConstant.STATUS_CEO_CHECK;
                }

                outDAO.modifyOutStatus(fullId, nextStatus);

                result = nextStatus;

                processCredit(fullId, outBean);
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                throw new MYException(e);
            }
        }
        else
        {
            nextStatus = OutConstant.BUY_STATUS_PASS;

            // 采购入库直接就是库管通过结束
            if (outBean.getOutType() == OutConstant.OUTTYPE_IN_COMMON)
            {
                nextStatus = OutConstant.BUY_STATUS_PASS;
            }
            // 调拨直接通过
            else if (outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT)
            {
                nextStatus = OutConstant.BUY_STATUS_PASS;
            }
            // 领样直接通过
            else if (outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH)
            {
                nextStatus = OutConstant.BUY_STATUS_PASS;
            }
            // 销售退单直接通过
            else if (outBean.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK)
            {
                nextStatus = OutConstant.BUY_STATUS_PASS;
            }
            // 其他入库
            else if (outBean.getOutType() == OutConstant.OUTTYPE_IN_OTHER)
            {
                nextStatus = OutConstant.BUY_STATUS_SECOND_PASS;
            }
            // 其他直接是待分公司经理审核
            else
            {
                nextStatus = OutConstant.STATUS_LOCATION_MANAGER_CHECK;
            }

            try
            {
                outDAO.modifyOutStatus(fullId, nextStatus);

                result = nextStatus;
            }
            catch (Exception e)
            {
                throw new MYException(e.toString());
            }

            // 入库直接通过
            importLog.info(fullId + ":" + user.getStafferName() + ":" + nextStatus
                           + ":redrectFrom:" + outBean.getStatus());
        }

        return result;
    }

    /**
     * CORE 销售信用处理
     * 
     * @param fullId
     * @param outBean
     * @throws MYException
     */
    private void processCredit(final String fullId, final OutBean outBean)
        throws MYException
    {
        // 只有销售单才有信用(但是个人领样没有客户,就是公共客户)
        boolean isCreditOutOf = false;

        // 这里需要计算客户的信用金额-是否报送物流中心经理审批
        boolean outCredit = parameterDAO.getBoolean(SysConfigConstant.OUT_CREDIT);

        // 取消信用处理
        if ( !outCredit)
        {
            return;
        }

        DutyBean duty = dutyDAO.find(outBean.getCustomerId());

        if (duty != null)
        {
            // 纳税实体作为客户的时候,使用客户的信用等级
            outDAO.updateCurcredit(fullId, outBean.getTotal());

            return;
        }

        CustomerBean cbean = customerDAO.find(outBean.getCustomerId());

        if (cbean == null)
        {
            throw new MYException("客户不存在,请确认操作");
        }

        // query customer credit
        CreditLevelBean clevel = creditLevelDAO.find(cbean.getCreditLevelId());

        if (clevel == null)
        {
            throw new MYException("客户信用等级不存在");
        }

        // 进行逻辑处理(必须是货到收款才能有此逻辑) 此逻辑已经废除
        if (outCredit && cbean != null && !StringTools.isNullOrNone(cbean.getCreditLevelId())
            && outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_COMMON)
        {
            throw new MYException("不支持此类型,请重新操作");
        }

        // 使用业务员的信用额度(或者是事业部经理的)
        if (outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_CREDIT_AND_CUR
            || outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER)
        {
            StafferBean sb2 = stafferDAO.find(outBean.getStafferId());

            if (sb2 == null)
            {
                throw new MYException("数据不完备,请重新操作");
            }

            // 先清空预占金额,主要是统计的时候方便
            outDAO.updateCurcredit(fullId, 0.0d);

            outDAO.updateStaffcredit(fullId, 0.0d);

            outDAO.updateManagercredit(fullId, "", 0.0d);

            double noPayBusinessInCur = outDAO.sumNoPayBusiness(outBean.getCustomerId(), YYTools
                .getFinanceBeginDate(), YYTools.getFinanceEndDate());

            // 自己担保的+替人担保的(这里应该区分不同的事业部)
            double noPayBusiness = outDAO.sumAllNoPayAndAvouchBusinessByStafferId(outBean
                .getStafferId(), outBean.getIndustryId(), YYTools.getStatBeginDate(), YYTools
                .getStatEndDate());

            double remainInCur = clevel.getMoney() - noPayBusinessInCur;

            // 不是公共客户
            if ( !cbean.getId().equals(CustomerConstant.PUBLIC_CUSTOMER_ID))
            {
                if (remainInCur < 0)
                {
                    remainInCur = 0.0;
                }

                // 先客户信用 然后职员信用(信用*杠杆) 最后分公司经理
                if (remainInCur >= outBean.getTotal())
                {
                    outDAO.updateCurcredit(fullId, outBean.getTotal());

                    outDAO.updateStaffcredit(fullId, 0.0d);

                    outBean.setReserve6("客户信用最大额度是:" + MathTools.formatNum(clevel.getMoney())
                                        + ".当前客户未付款金额(不包括此单):"
                                        + MathTools.formatNum(noPayBusinessInCur) + ".职员信用额度是:"
                                        + MathTools.formatNum(sb2.getCredit() * sb2.getLever())
                                        + ".职员信用已经使用额度是:" + MathTools.formatNum(noPayBusiness)
                                        + ".信用未超支,不需要事业部经理担保");
                }
            }

            // 职员杠杆后的信用
            double staffCredit = sb2.getCredit() * sb2.getLever();

            // 一半使用客户,一半使用职员的(且不是公共客户的)
            if (remainInCur < outBean.getTotal()
                && !cbean.getId().equals(CustomerConstant.PUBLIC_CUSTOMER_ID))
            {
                // 全部使用客户的信用等级
                outDAO.updateCurcredit(fullId, remainInCur);

                // 当前单据需要使用的职员信用额度
                double remainInStaff = outBean.getTotal() - remainInCur;

                // 防止职员信用等级超支
                if ( (noPayBusiness + remainInStaff) > staffCredit)
                {
                    double lastNeed = (noPayBusiness + remainInStaff) - staffCredit;

                    outBean.setReserve6("客户信用最大额度是:" + MathTools.formatNum(clevel.getMoney())
                                        + ".当前客户未付款金额(不包括此单):"
                                        + MathTools.formatNum(noPayBusinessInCur) + ".职员信用额度是:"
                                        + MathTools.formatNum(staffCredit) + ".职员信用已经使用额度是:"
                                        + MathTools.formatNum(noPayBusiness) + ".信用超支(包括此单):"
                                        + (MathTools.formatNum(lastNeed)));

                    // 这里如果不使用分公司经理直接不允许提交此单据
                    if (outBean.getReserve3() != OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER)
                    {
                        throw new MYException(outBean.getReserve6());
                    }

                    isCreditOutOf = true;

                    outDAO.updateOutReserve(fullId, OutConstant.OUT_CREDIT_OVER, outBean
                        .getReserve6());

                    // 把剩余的信用全部给此单据
                    outDAO.updateStaffcredit(fullId, (staffCredit - noPayBusiness));
                }
                else
                {
                    // 这里完全使用职员的信用
                    outDAO.updateStaffcredit(fullId, remainInStaff);
                }
            }

            // 公共客户信用处理
            if (cbean.getId().equals(CustomerConstant.PUBLIC_CUSTOMER_ID))
            {
                // 当前单据需要使用的职员信用额度
                double remainInStaff = outBean.getTotal();

                // 防止职员信用等级超支
                if ( (noPayBusiness + remainInStaff) > staffCredit)
                {
                    double lastNeed = (noPayBusiness + remainInStaff) - staffCredit;

                    outBean.setReserve6("职员信用额度是:" + MathTools.formatNum(staffCredit)
                                        + ".职员信用已经使用额度是:" + MathTools.formatNum(noPayBusiness)
                                        + ".信用超支(包括此单):" + (MathTools.formatNum(lastNeed)));

                    // 这里如果不使用分公司经理直接不允许提交此单据
                    if (outBean.getReserve3() != OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER)
                    {
                        throw new MYException(outBean.getReserve6());
                    }

                    isCreditOutOf = true;

                    outDAO.updateOutReserve(fullId, OutConstant.OUT_CREDIT_OVER, outBean
                        .getReserve6());

                    // 把剩余的信用全部给此单据
                    outDAO.updateStaffcredit(fullId, (staffCredit - noPayBusiness));
                }
                else
                {
                    // 这里完全使用职员的信用
                    outDAO.updateStaffcredit(fullId, remainInStaff);
                }
            }
        }

        // 信用没有受限检查产品价格是否为0
        if ( !isCreditOutOf)
        {
            outDAO.updateOutReserve(fullId, OutConstant.OUT_CREDIT_COMMON, outBean.getReserve6());
        }

        // 修改人工干预,重新置人工干预信用为0
        String pid = "90000000000000009999";

        creditCoreDAO.updateCurCreToInit(pid, outBean.getCustomerId());
    }

    /**
     * CORE 处理入库单的库存变动 采购入库/调拨(调出/回滚)/领样退货/销售退单
     * 
     * @param user
     * @param outBean
     * @param baseList
     * @throws MYException
     */
    private void processBuyBaseList(final User user, final OutBean outBean,
                                    final List<BaseBean> baseList, int type)
        throws MYException
    {
        // 入库单提交后就直接移动库存了,销售需要在库管通过后生成发货单前才会变动库存
        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            return;
        }

        // 处理入库单的库存变动 采购入库/领样退货/销售退单
        if (outBean.getOutType() == OutConstant.OUTTYPE_IN_COMMON
            || outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH
            || outBean.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK)
        {
            String sequence = commonDAO.getSquenceString();

            for (BaseBean element : baseList)
            {
                ProductChangeWrap wrap = new ProductChangeWrap();

                wrap.setDepotpartId(element.getDepotpartId());
                wrap.setPrice(element.getCostPrice());
                wrap.setProductId(element.getProductId());
                if (StringTools.isNullOrNone(element.getOwner()))
                {
                    wrap.setStafferId("0");
                }
                else
                {
                    wrap.setStafferId(element.getOwner());
                }
                wrap.setChange(element.getAmount());
                wrap.setDescription("库单[" + outBean.getFullId() + "]操作");
                wrap.setSerializeId(sequence);
                wrap.setType(type);
                wrap.setRefId(outBean.getFullId());

                storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, false);
            }

            saveUnique(user, outBean);

            outBean.setBaseList(baseList);

            // TAX_ADD 采购入库/领样退货/销售退单的通过
            Collection<OutListener> listenerMapValues = listenerMapValues();

            for (OutListener listener : listenerMapValues)
            {
                listener.onConfirmOutOrBuy(user, outBean);
            }
        }
    }

    /**
     * 变动库存的时候插入唯一的值,保证库存只变动一次(领样转销售也要插入,只是不减少库存)
     * 
     * @param user
     * @param outBean
     */
    private void saveUnique(final User user, final OutBean outBean)
    {
        OutUniqueBean unique = new OutUniqueBean();

        unique.setId(outBean.getFullId());

        unique.setRef(user.getStafferName());

        unique.setLogTime(TimeTools.now());

        outUniqueDAO.saveEntityBean(unique);

        // 更新库存异动时间
        outDAO.updateChangeTime(outBean.getFullId(), TimeTools.now());
    }

    /**
     * 检查submit的准备
     * 
     * @param fullId
     * @param outBean
     * @return
     * @throws MYException
     */
    private List<BaseBean> checkSubmit(final String fullId, final OutBean outBean)
        throws MYException
    {
        if (outBean == null)
        {
            throw new MYException(fullId + " 不存在");
        }

        // 退库-事业部经理审批
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && (outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH || outBean.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK))
        {
            if (outBean.getStatus() != OutConstant.BUY_STATUS_SUBMIT)
            {
                throw new MYException(fullId + " 状态错误,不能提交");
            }
        }
        else
        {
            if ( !OutHelper.canSubmit(outBean))
            {
                throw new MYException(fullId + " 状态错误,不能提交");
            }
        }

        final List<BaseBean> baseList = checkCoreStorage(outBean, false);

        // 如果是入库的调入，验证是否在途
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT
            && outBean.getReserve1() == OutConstant.MOVEOUT_OUT)
        {
            OutBean out = outDAO.find(fullId);

            if (out == null)
            {
                throw new MYException("选择调出的库单不存在，请重新操作选择调出的库单");
            }
        }

        // 如果是入库的调入，验证是否在途
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT
            && outBean.getReserve1() == OutConstant.MOVEOUT_IN)
        {
            String ofullid = outBean.getRefOutFullId();

            if (StringTools.isNullOrNone(ofullid))
            {
                throw new MYException("由于是调入的库单需要调出的库单对应，请重新操作选择调出的库单");
            }

            OutBean moveOut = outDAO.find(ofullid);

            if (moveOut == null)
            {
                throw new MYException("选择调出的库单不存在，请重新操作选择调出的库单");
            }

            if (moveOut.getInway() != OutConstant.IN_WAY)
            {
                throw new MYException("选择调出的库单不是在途中，请确认");
            }
        }

        // 验证 销售退库
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK)
        {
            List<OutBean> refBuyList = queryRefOut(outBean.getRefOutFullId());

            // 原单据的base
            List<BaseBean> lastList = OutHelper.trimBaseList2(baseDAO.queryEntityBeansByFK(outBean
                .getRefOutFullId()));

            for (BaseBean baseBean : lastList)
            {
                int hasBack = 0;

                for (OutBean ref : refBuyList)
                {
                    List<BaseBean> refBaseList = OutHelper.trimBaseList2(ref.getBaseList());

                    for (BaseBean refBase : refBaseList)
                    {
                        if (refBase.equals2(baseBean))
                        {
                            hasBack += refBase.getAmount();

                            break;
                        }
                    }
                }

                if (hasBack > baseBean.getAmount())
                {
                    throw new MYException("退货数量溢出，可退数量合计:[%d],当前退货数量(含本单):[%d]", baseBean
                        .getAmount(), hasBack);
                }
            }
        }

        return baseList;
    }

    /**
     * 查询REF的入库单(已经通过的)
     * 
     * @param request
     * @param outId
     * @return
     */
    protected List<OutBean> queryRefOut(String outId)
    {
        // 查询当前已经有多少个人领样
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition(" and OutBean.status in (3, 4)");

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        List<OutBean> refBuyList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : refBuyList)
        {
            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            outBean.setBaseList(list);
        }

        return refBuyList;
    }

    /**
     * CORE 检查库存,包括在途的都需要检查
     * 
     * @param outBean
     * @param includeSelf
     *            是否包含自身(当没有提交的时候是false,提交后是true)
     * @return
     * @throws MYException
     */
    private List<BaseBean> checkCoreStorage(final OutBean outBean, boolean includeSelf)
        throws MYException
    {
        final List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

        // 先检查入库
        for (BaseBean element : baseList)
        {
            ProductChangeWrap wrap = new ProductChangeWrap();

            wrap.setDepotpartId(element.getDepotpartId());
            wrap.setPrice(element.getCostPrice());
            wrap.setProductId(element.getProductId());
            wrap.setStafferId(element.getOwner());
            wrap.setRefId(outBean.getFullId());

            // 销售单
            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
            {
                // 领样转销售
                if (isSwatchToSail(outBean.getFullId()))
                {
                    try
                    {
                        checkSwithToSail(outBean.getRefOutFullId());
                    }
                    catch (MYException e)
                    {
                        throw new RuntimeException(e.getErrorContent(), e);
                    }
                }
                else
                {
                    wrap.setChange( -element.getAmount());

                    storageRelationManager.checkStorageRelation(wrap, includeSelf);
                }
            }
            else
            {
                // 入库单
                wrap.setChange(element.getAmount());

                storageRelationManager.checkStorageRelation(wrap, includeSelf);
            }
        }

        return baseList;
    }

    /**
     * 驳回(只有销售单和入库单)
     * 
     * @param outBean
     * @param user
     * @return
     * @throws Exception
     */
    public int reject(final String fullId, final User user, final String reason)
        throws MYException
    {
        // LOCK 库存驳回(这里存在库存锁的问题)
        synchronized (PublicLock.PRODUCT_CORE)
        {
            final OutBean outBean = outDAO.find(fullId);

            if (outBean == null)
            {
                throw new MYException("销售单不存在，请重新操作");
            }

            final List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

            // 仓库
            final String locationId = outBean.getLocation();

            doReject(fullId, user, reason, outBean, baseList, locationId);

            return OutConstant.STATUS_REJECT;
        }
    }

    /**
     * doReject
     * 
     * @param fullId
     * @param user
     * @param reason
     * @param outBean
     * @param baseList
     * @param deportId
     * @throws MYException
     */
    private void doReject(final String fullId, final User user, final String reason,
                          final OutBean outBean, final List<BaseBean> baseList,
                          final String deportId)
        throws MYException
    {
        checkReject(outBean, baseList, deportId);

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    // TAX_ADD 驳回后坏账取消
                    Collection<OutListener> listenerMapValues = listenerMapValues();

                    for (OutListener listener : listenerMapValues)
                    {
                        try
                        {
                            listener.onReject(user, outBean);
                        }
                        catch (MYException e)
                        {
                            throw new RuntimeException(e.getErrorContent(), e);
                        }
                    }

                    // 如果销售单，需要删除发货单(当库管驳回的时候才触发)
                    if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                    {
                        ConsignBean beans = consignDAO.findDefaultConsignByFullId(fullId);

                        if (beans != null)
                        {
                            consignDAO.delConsign(fullId);
                        }

                        // 取消坏账
                        outDAO.modifyBadDebts(fullId, 0.0d);
                    }

                    // 如果是调出的驳回需要回滚
                    if (OutHelper.isMoveOut(outBean))
                    {
                        handlerMoveOutBack(fullId, user, outBean);

                        if (outBean.getCheckStatus() == PublicConstant.CHECK_STATUS_INIT)
                        {
                            outDAO.modifyChecks(outBean.getFullId(), "调拨回滚后原单据还未核对,系统自动核对原调拨单");

                            outDAO
                                .modifyOutStatus(outBean.getFullId(), OutConstant.STATUS_SEC_PASS);
                        }

                        // 操作日志
                        addOutLog(fullId, user, outBean, reason, SailConstant.OPR_OUT_REJECT,
                            OutConstant.STATUS_SEC_PASS);
                    }
                    else
                    {
                        importLog.info(fullId + ":" + user.getStafferName() + ":"
                                       + OutConstant.STATUS_REJECT + ":redrectFrom:"
                                       + outBean.getStatus());

                        outDAO.modifyOutStatus(outBean.getFullId(), OutConstant.STATUS_REJECT);

                        // 驳回修改在途方式
                        outDAO.updataInWay(fullId, OutConstant.IN_WAY_NO);

                        // 变成没有付款
                        outDAO.updatePay(fullId, OutConstant.PAY_NOT);

                        // 操作日志
                        addOutLog(fullId, user, outBean, reason, SailConstant.OPR_OUT_REJECT,
                            OutConstant.STATUS_REJECT);
                    }

                    notifyOut(outBean, user, 1);

                    return Boolean.TRUE;
                }

            });
        }
        catch (TransactionException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("增加库单错误：", e);

            throw new MYException(e.toString());
        }
    }

    /**
     * 处理驳回的验证(驳回不涉及库存的移动)
     * 
     * @param outBean
     * @param baseList
     * @param locationId
     * @throws MYException
     */
    private void checkReject(final OutBean outBean, final List<BaseBean> baseList,
                             final String deportId)
        throws MYException
    {
        if (outBean == null)
        {
            throw new MYException("销售单不存在，请重新操作");
        }

        if ( !OutHelper.canReject(outBean))
        {
            throw new MYException("状态不可以驳回!");
        }
    }

    /**
     * CORE 审核通过(这里只有销售单/入库单才有此操作)分公司经理审核/结算中心/物流审批/库管发货
     * 
     * @param outBean
     * @param user
     * @param depotpartId
     *            废弃
     * @return
     * @throws Exception
     */
    public int pass(final String fullId, final User user, final int nextStatus,
                    final String reason, final String depotpartId)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        checkPass(outBean);

        final int oldStatus = outBean.getStatus();

        final DepotBean depot = checkDepotInPass(nextStatus, outBean);

        // LOCK 销售单/入库单通过(这里是销售单库存变动的核心)
        synchronized (PublicLock.PRODUCT_CORE)
        {
            // 入库操作在数据库事务中完成
            TransactionTemplate tran = new TransactionTemplate(transactionManager);
            try
            {
                tran.execute(new TransactionCallback()
                {
                    public Object doInTransaction(TransactionStatus arg0)
                    {
                        int newNextStatus = nextStatus;

                        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                        {
                            // 直接把结算中心通过的设置成物流管理员通过(跳过物流)
                            if (newNextStatus == OutConstant.STATUS_MANAGER_PASS
                                && depot.getType() == DepotConstant.DEPOT_TYPE_LOCATION)
                            {
                                newNextStatus = OutConstant.STATUS_FLOW_PASS;
                            }
                        }

                        importLog.info(outBean.getFullId() + ":" + user.getStafferName() + ":"
                                       + newNextStatus + ":redrectFrom:" + oldStatus);

                        // 修改状态
                        outDAO.modifyOutStatus(outBean.getFullId(), newNextStatus);

                        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                        {
                            handerPassOut(fullId, user, outBean, depot, newNextStatus);
                        }
                        else
                        {
                            handerPassBuy(fullId, user, outBean, newNextStatus);
                        }

                        addOutLog(fullId, user, outBean, reason, SailConstant.OPR_OUT_PASS,
                            newNextStatus);

                        // 把状态放到最新的out里面
                        outBean.setStatus(newNextStatus);

                        // OSGI 监听实现
                        Collection<OutListener> listenerMapValues = listenerMapValues();

                        for (OutListener listener : listenerMapValues)
                        {
                            try
                            {
                                listener.onPass(user, outBean);
                            }
                            catch (MYException e)
                            {
                                throw new RuntimeException(e.getErrorContent(), e);
                            }
                        }

                        notifyOut(outBean, user, 0);

                        return Boolean.TRUE;
                    }
                });
            }
            catch (TransactionException e)
            {
                _logger.error(e, e);
                throw new MYException("数据库内部错误");
            }
            catch (DataAccessException e)
            {
                _logger.error(e, e);
                throw new MYException("数据库内部错误");
            }
            catch (Exception e)
            {
                _logger.error(e, e);
                throw new MYException("处理异常:" + e.getMessage());
            }
        }

        // 更新后的状态
        return outBean.getStatus();

    }

    /**
     * checkDepotInPass
     * 
     * @param nextStatus
     * @param outBean
     * @return
     * @throws MYException
     */
    private DepotBean checkDepotInPass(final int nextStatus, final OutBean outBean)
        throws MYException
    {
        final DepotBean depot = depotDAO.find(outBean.getLocation());

        if (depot == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 需要发货单先通过(只有中心仓库才有物流哦)
        if (nextStatus == OutConstant.STATUS_FLOW_PASS
            && depot.getType() == DepotConstant.DEPOT_TYPE_CENTER)
        {
            ConsignBean consignBean = consignDAO.findDefaultConsignByFullId(outBean.getFullId());

            if (consignBean == null)
            {
                throw new MYException("没有发货单,请重新操作!");
            }

            if (consignBean.getCurrentStatus() == SailConstant.CONSIGN_INIT)
            {
                throw new MYException("发货单没有审批通过，请先处理发货单");
            }
        }
        return depot;
    }

    /**
     * checkPass
     * 
     * @param outBean
     * @throws MYException
     */
    private void checkPass(final OutBean outBean)
        throws MYException
    {
        final int oldStatus = outBean.getStatus();

        if (outBean == null)
        {
            throw new MYException("销售单不存在，请重新操作");
        }

        // 检查pass的条件
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && (outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH || outBean.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK))
        {
            if (outBean.getStatus() != OutConstant.STATUS_SAVE)
            {
                throw new MYException("状态不可以通过!");
            }
        }
        else
        {
            if (outBean.getStatus() == OutConstant.STATUS_SAVE
                || outBean.getStatus() == OutConstant.STATUS_REJECT)
            {
                throw new MYException("状态不可以通过!");
            }
        }

        // 检查日志核对
        int outStatusInLog = this.findOutStatusInLog(outBean.getFullId());

        if (outStatusInLog != -1 && outStatusInLog != oldStatus)
        {
            String msg = "严重错误,当前单据的状态应该是:" + OutHelper.getStatus(outStatusInLog) + ",而不是"
                         + OutHelper.getStatus(oldStatus) + ".请联系管理员确认此单的正确状态!";

            loggerError(outBean.getFullId() + ":" + msg);

            throw new MYException(msg);
        }
    }

    /**
     * CORE 销售单/入库单变动库存(这里是普通销售单和普通入库单库存通过的核心操作)
     * 
     * @param user
     * @param outBean
     * @param baseList
     * @param logList
     * @throws MYException
     */
    private void processBuyAndSailPass(final User user, final OutBean outBean,
                                       final List<BaseBean> baseList, int type)
        throws MYException
    {
        // 领样转销售时,库存无变动(优先处理)
        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
            && isSwatchToSail(outBean.getFullId()))
        {
            // 检查是否溢出
            checkSwithToSail(outBean.getRefOutFullId());

            // CORE 领样转销售通过的时候,生成的对冲单据
            createBuyDuichong(user, outBean, baseList);

            saveUnique(user, outBean);

            // TAX_ADD 领样转销售
            Collection<OutListener> listenerMapValues = listenerMapValues();

            for (OutListener listener : listenerMapValues)
            {
                listener.onConfirmOutOrBuy(user, outBean);
            }

            // 直接返回
            return;
        }

        String sequence = commonDAO.getSquenceString();

        for (BaseBean element : baseList)
        {
            ProductChangeWrap wrap = new ProductChangeWrap();

            wrap.setDepotpartId(element.getDepotpartId());
            wrap.setPrice(element.getCostPrice());
            wrap.setProductId(element.getProductId());
            if (StringTools.isNullOrNone(element.getOwner()))
            {
                wrap.setStafferId("0");
            }
            else
            {
                wrap.setStafferId(element.getOwner());
            }
            wrap.setRefId(outBean.getFullId());

            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
            {
                // 这里是销售单,所以是负数
                wrap.setChange( -element.getAmount());

                wrap.setDescription("销售单[" + outBean.getFullId() + "]库管通过操作");
            }
            else
            {
                // 这里是入库单
                wrap.setChange(element.getAmount());

                wrap.setDescription("入库单[" + outBean.getFullId() + "]库管通过操作");
            }

            wrap.setSerializeId(sequence);

            wrap.setType(type);

            storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, false);
        }

        outBean.setBaseList(baseList);

        // TAX_ADD 销售单和入库单通过(最常见的)
        Collection<OutListener> listenerMapValues = listenerMapValues();

        for (OutListener listener : listenerMapValues)
        {
            listener.onConfirmOutOrBuy(user, outBean);
        }

        saveUnique(user, outBean);
    }

    /**
     * CORE 生成对称的B单据(核心逻辑,当前已经废除)<br>
     * 1.收藏品 销售 经纬公司 P产品 B单据<br>
     * 2.经纬公司 入库 P产品 C单据
     * 
     * @param outBean
     * @param baseList
     * @throws MYException
     */
    protected void createRealDuichong(User user, final OutBean outBean,
                                      final List<BaseBean> baseList)
        throws MYException
    {
        // 1.收藏品 销售 经纬公司 P产品 B单据
        OutBean newOutBean = new OutBean();

        // 拷贝基本属性
        BeanUtil.copyProperties(newOutBean, outBean);

        newOutBean.setDutyId(PublicConstant.DEFAULR_DUTY_ID);

        String productId = baseList.get(0).getProductId();

        ProductBean product = productDAO.find(productId);

        if (product.getType() == ProductConstant.PRODUCT_TYPE_NUMISMATICS)
        {
            // 旧货专用发票
            newOutBean.setInvoiceId(InvoiceConstant.INVOICE_INSTACE_NDK_2);
        }
        else
        {
            // 可抵扣增值票
            newOutBean.setInvoiceId(InvoiceConstant.INVOICE_INSTACE_DK_17);
        }

        newOutBean.setCustomerId(outBean.getDutyId());

        DutyBean duyu = dutyDAO.find(outBean.getDutyId());

        newOutBean.setCustomerName(duyu.getName());

        // 默认都是销售出库
        newOutBean.setOutType(OutConstant.OUTTYPE_OUT_COMMON);

        newOutBean.setStatus(OutConstant.STATUS_PASS);

        newOutBean.setDescription("规则生成的销售单据");

        newOutBean.setChecks("");

        newOutBean.setHadPay(0.0d);

        newOutBean.setPay(OutConstant.PAY_NOT);

        newOutBean.setVtype(OutConstant.VTYPE_SPECIAL);

        // 表明是对冲的单据
        newOutBean.setReserve8("1");

        newOutBean.setCurcredit(0);

        newOutBean.setStaffcredit(0);

        newOutBean.setManagercredit(0);

        newOutBean.setVtypeFullId(outBean.getFullId());

        String newFullId = wrapOutId(outBean, newOutBean);

        List<BaseBean> newBaseList = new ArrayList<BaseBean>();

        double newTotal = 0.0d;

        for (BaseBean baseBean : baseList)
        {
            BaseBean newEach = new BaseBean();

            BeanUtil.copyProperties(newEach, baseBean);

            newEach.setId(commonDAO.getSquenceString());

            newEach.setOutId(newFullId);

            double d1 = newEach.getPrice() * 0.05;

            double d2 = newEach.getPrice() - newEach.getCostPrice();

            // 含税价/1.03
            newEach.setInputPrice(newEach.getPrice() / 1.03d);

            // （含税价*5%） >（含税价-成本价）
            if (MathTools.compare(d1, d2) >= 0)
            {
                // 含税价等于A单含税价*95%
                newEach.setPrice(newEach.getPrice() * 0.95d);

                // 计算总价
                newEach.setValue(newEach.getPrice() * newEach.getAmount());

            }
            else
            {
                // 含税价取原单的含税价的值
                newEach.setPrice(newEach.getPrice());
            }

            // 计算新的总额
            newTotal += newEach.getValue();

            newBaseList.add(newEach);
        }

        baseDAO.saveAllEntityBeans(newBaseList);

        newOutBean.setTotal(newTotal);

        // 信用这里是纳税实体全部承担
        newOutBean.setCurcredit(newTotal);

        newOutBean.setBaseList(newBaseList);

        saveOutInner(newOutBean);

        setInvoiceId(newOutBean);

        // 更新关联
        outDAO.updateVtypeFullId(outBean.getFullId(), newFullId);

        saveUnique(user, newOutBean);

        // 增加日志
        addOutLog(newFullId, user, newOutBean, "提交", SailConstant.OPR_OUT_PASS,
            OutConstant.BUY_STATUS_PASS);

        // TAX_ADD B单对冲生成的单据
        Collection<OutListener> listenerMapValues = listenerMapValues();

        for (OutListener listener : listenerMapValues)
        {
            listener.onConfirmOutOrBuy(user, newOutBean);
        }

        // 2.经纬公司 入库 P产品 C单据
        createBuyDuichong2(user, outBean, baseList);
    }

    /**
     * 2.经纬公司 入库 P产品 C单据
     * 
     * @param user
     * @param outBean
     * @param baseList
     * @throws MYException
     */
    private void createBuyDuichong2(User user, final OutBean outBean, final List<BaseBean> baseList)
        throws MYException
    {
        OutBean newInBean = new OutBean();

        BeanUtil.copyProperties(newInBean, outBean);

        final String fullId = wrapOutId(outBean, newInBean);

        newInBean.setType(OutConstant.OUT_TYPE_INBILL);

        newInBean.setOutType(OutConstant.OUTTYPE_IN_OTHER);

        newInBean.setStatus(OutConstant.BUY_STATUS_PASS);

        newInBean.setCustomerId(outBean.getDutyId());

        DutyBean duyu = dutyDAO.find(outBean.getDutyId());

        newInBean.setCustomerName(duyu.getName());

        newInBean.setVtype(OutConstant.VTYPE_SPECIAL);

        newInBean.setVtypeFullId(outBean.getFullId());

        newInBean.setChecks("");

        newInBean.setCheckStatus(PublicConstant.CHECK_STATUS_INIT);

        newInBean.setDescription("规则生成的入库单据(自动生成的)");

        List<BaseBean> newBaseList = new ArrayList();

        double newTotal = 0.0d;

        // 这里的单据应该和原始单据的一致，因为要相互抵消
        for (BaseBean element : baseList)
        {
            BaseBean newEach = new BaseBean();

            BeanUtil.copyProperties(newEach, element);

            newEach.setOutId(fullId);

            newTotal += newEach.getValue();

            newBaseList.add(newEach);
        }

        newInBean.setTotal(newTotal);

        newInBean.setBaseList(newBaseList);

        setInvoiceId(newInBean);

        // 保存入库单
        saveOutInner(newInBean);

        for (BaseBean baseBean : newBaseList)
        {
            baseBean.setId(commonDAO.getSquenceString());

            baseBean.setOutId(fullId);

            baseBean.setMtype(newInBean.getMtype());

            // 增加单个产品到base表
            baseDAO.saveEntityBean(baseBean);
        }

        // 直接通过
        newInBean.setStatus(3);

        addOutLog(fullId, user, newInBean, "提交", SailConstant.OPR_OUT_PASS,
            OutConstant.BUY_STATUS_PASS);

        saveUnique(user, newInBean);

        // OSGI 监听实现(对冲单据,有入库的过程)
        Collection<OutListener> listenerMapValues = listenerMapValues();

        for (OutListener listener : listenerMapValues)
        {
            listener.onConfirmOutOrBuy(user, newInBean);
        }
    }

    /**
     * 领样转销售通过的时候,生成的对冲单据
     * 
     * @param outBean
     * @param baseList
     * @throws MYException
     */
    private void createBuyDuichong(User user, final OutBean outBean, final List<BaseBean> baseList)
        throws MYException
    {
        OutBean newInBean = new OutBean();

        BeanUtil.copyProperties(newInBean, outBean);

        final String fullId = wrapOutId(outBean, newInBean);

        newInBean.setType(OutConstant.OUT_TYPE_INBILL);

        newInBean.setOutType(OutConstant.OUTTYPE_IN_OTHER);

        newInBean.setStatus(OutConstant.BUY_STATUS_PASS);

        // 表明是对冲的单据
        newInBean.setReserve8("1");

        // 对冲单据
        newInBean.setRefOutFullId(outBean.getRefOutFullId());

        newInBean.setCustomerId(CustomerConstant.PUBLIC_CUSTOMER_ID);

        newInBean.setCustomerName(CustomerConstant.PUBLIC_CUSTOMER_NAME);

        newInBean.setChecks("");

        newInBean.setCheckStatus(PublicConstant.CHECK_STATUS_INIT);

        newInBean.setDescription("领样转销售[" + outBean.getFullId() + "]的库管通过的时候自动产生一张财务做帐的领样退库单,"
                                 + "此单据系统自动生成，直接到待核对状态(此单不会对库存产生任何异动)");

        String lingyangId = outBean.getRefOutFullId();

        List<BaseBean> lyBaseList = baseDAO.queryEntityBeansByFK(lingyangId);

        List<BaseBean> newBaseList = new ArrayList();

        double newTotal = 0.0d;

        for (BaseBean element : baseList)
        {
            BaseBean newEach = new BaseBean();

            BeanUtil.copyProperties(newEach, element);

            newEach.setOutId(fullId);

            // 价格是领样的价格哦
            BaseBean findBase = OutHelper.findBase(lyBaseList, newEach);

            if (findBase == null)
            {
                throw new MYException("找不到对应的领样原始单据,请确认操作");
            }

            newEach.setPrice(findBase.getPrice());

            newEach.setValue(newEach.getAmount() * findBase.getPrice());

            newTotal += newEach.getValue();

            newBaseList.add(newEach);
        }

        newInBean.setTotal(newTotal);

        newInBean.setBaseList(newBaseList);

        setInvoiceId(newInBean);

        // 保存入库单
        saveOutInner(newInBean);

        for (BaseBean baseBean : newBaseList)
        {
            baseBean.setId(commonDAO.getSquenceString());

            baseBean.setOutId(fullId);

            baseBean.setMtype(newInBean.getMtype());

            // 增加单个产品到base表
            baseDAO.saveEntityBean(baseBean);
        }

        newInBean.setStatus(0);

        addOutLog(fullId, user, newInBean, "提交", SailConstant.OPR_OUT_PASS,
            OutConstant.BUY_STATUS_PASS);

        saveUnique(user, newInBean);

        // OSGI 监听实现(对冲单据,有入库的过程)
        Collection<OutListener> listenerMapValues = listenerMapValues();

        for (OutListener listener : listenerMapValues)
        {
            listener.onConfirmOutOrBuy(user, newInBean);
        }
    }

    /**
     * warpOutId
     * 
     * @param outBean
     * @param newInBean
     * @return
     * @throws MYException
     */
    private String wrapOutId(final OutBean outBean, OutBean newInBean)
        throws MYException
    {
        // 先保存
        String id = getAll(commonDAO.getSquence());

        LocationBean location = locationDAO.find(outBean.getLocationId());

        if (location == null)
        {
            _logger.error("区域不存在:" + outBean.getLocationId());

            throw new MYException("区域不存在:" + outBean.getLocationId());
        }

        String flag = location.getCode();

        String time = TimeTools.now("yyMMddHHmm");

        final String fullId = flag + time + id;

        newInBean.setId(getOutId(id));

        newInBean.setFullId(fullId);

        return fullId;
    }

    /**
     * CORE (销售单的终结)财务核对
     * 
     * @param outBean
     * @param user
     * @return
     * @throws Exception
     */
    public boolean check(final String fullId, final User user, final String checks)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            throw new MYException("销售单不存在，请重新操作");
        }

        if (outBean.getStatus() != OutConstant.STATUS_PASS
            && outBean.getStatus() != OutConstant.STATUS_SEC_PASS)
        {
            throw new MYException("销售单不在待回款/结束状态，不能核对");
        }

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    outDAO.modifyChecks(fullId, outBean.getChecks() + " / " + checks);

                    // 结束
                    outDAO.modifyOutStatus(fullId, OutConstant.STATUS_SEC_PASS);

                    // OSGI 监听实现
                    Collection<OutListener> listenerMapValues = listenerMapValues();

                    for (OutListener listener : listenerMapValues)
                    {
                        try
                        {
                            listener.onCheck(user, outBean);
                        }
                        catch (MYException e)
                        {
                            throw new RuntimeException(e.getErrorContent(), e);
                        }
                    }

                    addOutLog(fullId, user, outBean, "核对", SailConstant.OPR_OUT_PASS,
                        OutConstant.STATUS_SEC_PASS);

                    notifyOut(outBean, user, 3);

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("核对库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("核对库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("核对库单错误：", e);
            throw new MYException("系统错误,请重新操作");
        }

        return true;

    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean tranOut(User user, String fullId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, fullId);

        OutBean out = checkTran(user, fullId);

        SailTranApplyBean apply = new SailTranApplyBean();
        apply.setId(commonDAO.getSquenceString20());
        apply.setCustomerId(out.getCustomerId());
        apply.setLogTime(TimeTools.now());
        apply.setOldStafferId(out.getStafferId());
        apply.setOutId(fullId);
        apply.setStafferId(user.getStafferId());
        apply.setStatus(SailConstant.SAILTRANAPPLY_SUBMIT);

        sailTranApplyDAO.saveEntityBean(apply);

        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());

        log.setDescription("提交申请");
        log.setFullId(apply.getId());
        log.setOprMode(PublicConstant.OPRMODE_SUBMIT);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(SailConstant.SAILTRANAPPLY_INIT);

        log.setAfterStatus(apply.getStatus());

        flowLogDAO.saveEntityBean(log);

        return true;
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean passTranApply(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        SailTranApplyBean bean = sailTranApplyDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != SailConstant.SAILTRANAPPLY_SUBMIT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        OutBean out = outDAO.find(bean.getOutId());

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !OutHelper.isSailEnd(out))
        {
            throw new MYException("单据不处于结束,请确认操作");
        }

        if (out.getPay() != OutConstant.PAY_YES)
        {
            throw new MYException("单据没有完全付款,请确认操作");
        }

        if ( !customerManager.hasCustomerAuth2(bean.getStafferId(), out.getCustomerId()))
        {
            throw new MYException("您当前和客户[%s]没有关联关系,无法移交,请确认操作", out.getCustomerName());
        }

        StafferBean sb = stafferDAO.find(bean.getStafferId());

        if (sb == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setStatus(SailConstant.SAILTRANAPPLY_PASS);

        sailTranApplyDAO.updateEntityBean(bean);

        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());

        log.setDescription("通过");
        log.setFullId(id);
        log.setOprMode(PublicConstant.OPRMODE_PASS);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(SailConstant.SAILTRANAPPLY_SUBMIT);

        log.setAfterStatus(SailConstant.SAILTRANAPPLY_PASS);

        flowLogDAO.saveEntityBean(log);

        String oldName = out.getStafferName();

        out.setStafferId(sb.getId());
        out.setStafferName(sb.getName());

        // 更新责任人
        outDAO.updateEntityBean(out);

        log = new FlowLogBean();

        log.setActor(user.getStafferName());

        log.setDescription("变更单据提交人,从:" + oldName + "到:" + sb.getName());
        log.setFullId(out.getFullId());
        log.setOprMode(PublicConstant.OPRMODE_PASS);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(out.getStatus());

        log.setAfterStatus(out.getStatus());

        // 销售单日志
        flowLogDAO.saveEntityBean(log);

        return true;
    }

    public boolean tranCompleteOutListNT(User user, List<OutBean> outList, StafferBean staffer)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, outList, staffer);

        for (OutBean outBean : outList)
        {
            if (outBean.getOutType() != OutConstant.OUTTYPE_OUT_COMMON)
            {
                throw new MYException("只能销售出库的单据可以移交,请确认操作");
            }

            if (outBean.getPay() == OutConstant.PAY_YES)
            {
                throw new MYException("单据已经完全付款,请确认操作");
            }
        }

        // TAX_ADD 未付款的销售单移交
        Collection<OutListener> listenerMapValues = this.listenerMapValues();

        for (OutListener outListener : listenerMapValues)
        {
            outListener.onTranOutList(user, outList, staffer);
        }

        for (OutBean out : outList)
        {
            String oldName = out.getStafferName();

            out.setStafferId(staffer.getId());
            out.setStafferName(staffer.getName());

            // 更新责任人
            outDAO.updateEntityBean(out);

            FlowLogBean log = new FlowLogBean();

            log.setActor(user.getStafferName());

            log.setDescription("变更单据提交人,从:" + oldName + "到:" + staffer.getName());
            log.setFullId(out.getFullId());
            log.setOprMode(PublicConstant.OPRMODE_PASS);
            log.setLogTime(TimeTools.now());

            log.setPreStatus(out.getStatus());

            log.setAfterStatus(out.getStatus());

            // 销售单日志
            flowLogDAO.saveEntityBean(log);
        }

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rejectTranApply(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        SailTranApplyBean bean = sailTranApplyDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != SailConstant.SAILTRANAPPLY_SUBMIT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setStatus(SailConstant.SAILTRANAPPLY_REJECT);

        sailTranApplyDAO.updateEntityBean(bean);

        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());

        log.setDescription(reason);
        log.setFullId(id);
        log.setOprMode(PublicConstant.OPRMODE_REJECT);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(SailConstant.SAILTRANAPPLY_SUBMIT);

        log.setAfterStatus(SailConstant.SAILTRANAPPLY_REJECT);

        flowLogDAO.saveEntityBean(log);

        return true;
    }

    /**
     * checkTran
     * 
     * @param user
     * @param fullId
     * @return
     * @throws MYException
     */
    private OutBean checkTran(User user, String fullId)
        throws MYException
    {
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (out.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("自己不能移交给自己,请确认操作");
        }

        if ( !OutHelper.isSailEnd(out))
        {
            throw new MYException("单据不处于结束,请确认操作");
        }

        if (out.getPay() != OutConstant.PAY_YES)
        {
            throw new MYException("单据没有完全付款,请确认操作");
        }

        if (out.getOutType() != OutConstant.OUTTYPE_OUT_COMMON)
        {
            throw new MYException("只能销售出库的单据可以移交,请确认操作");
        }

        if ( !customerManager.hasCustomerAuth2(user.getStafferId(), out.getCustomerId()))
        {
            throw new MYException("您当前和客户[%s]没有关联关系,无法移交,请确认操作", out.getCustomerName());
        }

        SailTranApplyBean tran = sailTranApplyDAO.findByUnique(out.getCustomerId(),
            SailConstant.SAILTRANAPPLY_SUBMIT);

        if (tran != null)
        {
            throw new MYException("申请已经存在,请确认操作");
        }

        return out;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean checkOutBalance(String id, User user, String checks)
        throws MYException
    {
        OutBalanceBean bean = outBalanceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_END)
        {
            throw new MYException("不在库管通过,请确认操作");
        }

        outBalanceDAO.updateCheck(id, PublicConstant.CHECK_STATUS_END, checks);

        addOutLog2(id, user, OutConstant.OUTBALANCE_STATUS_END, "总部核对", SailConstant.OPR_OUT_PASS,
            OutConstant.OUTBALANCE_STATUS_END);

        return true;
    }

    public OutBean findOutById(final String fullId)
    {
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            return null;
        }

        out.setBaseList(baseDAO.queryEntityBeansByFK(fullId));

        return out;
    }

    public OutVO findOutVOById(final String fullId)
    {
        OutVO out = outDAO.findVO(fullId);

        if (out == null)
        {
            return null;
        }

        out.setBaseList(baseDAO.queryEntityBeansByFK(fullId));

        return out;
    }

    /**
     * 删除库单
     * 
     * @param fullId
     * @return
     */
    public boolean delOut(final User user, final String fullId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(fullId);

        final OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 退库的逻辑比较特殊
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && (outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH || outBean.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK))
        {
            if ( ! (outBean.getStatus() == OutConstant.STATUS_SAVE
                    || outBean.getStatus() == OutConstant.STATUS_REJECT || outBean.getStatus() == OutConstant.BUY_STATUS_SUBMIT))
            {
                throw new MYException("单据不能被删除,请确认操作");
            }
        }
        else
        {
            if ( !OutHelper.canDelete(outBean))
            {
                throw new MYException("单据不能被删除,请确认操作");
            }
        }

        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

        outBean.setBaseList(baseList);

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    outDAO.deleteEntityBean(fullId);

                    baseDAO.deleteEntityBeansByFK(fullId);

                    // 删除审批记录
                    flowLogDAO.deleteEntityBeansByFK(fullId);

                    if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
                        && outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH)
                    {
                        notifyManager.notifyMessage(outBean.getStafferId(), outBean
                            .getRefOutFullId()
                                                                            + "的领样退货申请已经被["
                                                                            + user.getStafferName()
                                                                            + "]驳回,申请自动删除");
                    }

                    Collection<OutListener> listenerMapValues = listenerMapValues();

                    for (OutListener listener : listenerMapValues)
                    {
                        try
                        {
                            listener.onDelete(user, outBean);
                        }
                        catch (MYException e)
                        {
                            throw new RuntimeException(e.getErrorContent(), e);
                        }
                    }

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("删除库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("删除库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("删除库单错误：", e);
            throw new MYException("系统错误,请重新操作");
        }

        _logger.info(user.getStafferName() + "/" + user.getName() + "/DELETE:" + outBean);

        operationLog.info(user.getStafferName() + "/" + user.getName() + "/DELETE OUT:" + outBean);

        return true;
    }

    /**
     * 更新库单(但是不包括状态)
     * 
     * @param fullId
     * @return
     */
    public boolean updateOut(final OutBean out)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(out);

        JudgeTools.judgeParameterIsNull(out.getFullId());

        final OutBean oldBean = outDAO.find(out.getFullId());

        out.setStatus(oldBean.getStatus());

        setInvoiceId(out);

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    outDAO.updateEntityBean(out);

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("修改库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("修改库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("修改库单错误：", e);
            throw new MYException("系统错误,请重新操作");
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.manager.OutManager#modifyPay(com.center.china.osgi.publics.User, java.lang.String,
     *      int, java.lang.String)
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean payOut(final User user, String fullId, String reason)
        throws MYException
    {
        return payOutWithoutTransactional(user, fullId, reason);
    }

    /**
     * 强制通过付款(对于4月之前的单据使用)
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean fourcePayOut(User user, String fullId, String reason)
        throws MYException
    {
        // 需要增加是否超期 flowId
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ("2011-04-01".compareTo(out.getOutTime()) < 0)
        {
            throw new MYException("销售单必须在(2011-04-01),请确认操作");
        }

        // 如果getRedate为空说明已经超前回款了
        if ( !StringTools.isNullOrNone(out.getRedate()))
        {
            int delay = TimeTools.cdate(TimeTools.now(), out.getRedate());

            if (delay > 0)
            {
                outDAO.modifyTempType(fullId, delay);
            }
            else
            {
                outDAO.modifyTempType(fullId, 0);
            }
        }

        addOutLog(fullId, user, out, reason, SailConstant.OPR_OUT_PASS, out.getStatus());

        notifyOut(out, user, 2);

        // 修改付款标识
        return outDAO.modifyPay(fullId, OutConstant.PAY_YES);
    }

    public boolean payOutWithoutTransactional(final User user, String fullId, String reason)
        throws MYException
    {
        // 需要增加是否超期 flowId
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 如果getRedate为空说明已经超前回款了
        if ( !StringTools.isNullOrNone(out.getRedate()))
        {
            int delay = TimeTools.cdate(TimeTools.now(), out.getRedate());

            if (delay > 0)
            {
                outDAO.modifyTempType(fullId, delay);
            }
            else
            {
                outDAO.modifyTempType(fullId, 0);
            }
        }

        OutListener listener = this.findListener(PluginNameConstant.OUTLISTENER_FINANCEIMPL);

        if (listener == null)
        {
            throw new MYException("资金模块没有加载,请确认操作");
        }

        ResultBean result = listener.onHadPay(user, out);

        // 不能完全回款
        if (result.getResult() != 0)
        {
            throw new MYException(result.getMessage());
        }

        addOutLog(fullId, user, out, reason, SailConstant.OPR_OUT_PASS, out.getStatus());

        notifyOut(out, user, 2);

        // 修改付款标识
        return outDAO.modifyPay(fullId, OutConstant.PAY_YES);
    }

    public boolean payOutWithoutTransactional2(final User user, String fullId, String reason)
        throws MYException
    {
        // 需要增加是否超期 flowId
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        OutListener listener = this.findListener(PluginNameConstant.OUTLISTENER_FINANCEIMPL);

        if (listener == null)
        {
            return true;
        }

        ResultBean result = listener.onHadPay(user, out);

        if (result.getResult() != 0)
        {
            addOutLog(fullId, user, out, reason + ",但是系统核算后没有完全付款", SailConstant.OPR_OUT_PASS, out
                .getStatus());

            // 不能完全回款
            return outDAO.modifyPay(fullId, OutConstant.PAY_NOT);
        }

        // 如果getRedate为空说明已经超前回款了
        if ( !StringTools.isNullOrNone(out.getRedate()))
        {
            int delay = TimeTools.cdate(TimeTools.now(), out.getRedate());

            if (delay > 0)
            {
                outDAO.modifyTempType(fullId, delay);
            }
            else
            {
                outDAO.modifyTempType(fullId, 0);
            }
        }

        addOutLog(fullId, user, out, reason, SailConstant.OPR_OUT_PASS, out.getStatus());

        notifyOut(out, user, 2);

        // 修改付款标识
        return outDAO.modifyPay(fullId, OutConstant.PAY_YES);
    }

    public ResultBean checkOutPayStatus(User user, OutBean out)
    {
        ResultBean result = new ResultBean();

        OutListener listener = this.findListener(PluginNameConstant.OUTLISTENER_FINANCEIMPL);

        if (listener == null)
        {
            return result;
        }

        result = listener.onHadPay(user, out);

        return result;
    }

    public double outNeedPayMoney(User user, String fullId)
    {
        OutListener listener = this.findListener(PluginNameConstant.OUTLISTENER_FINANCEIMPL);

        double total = 0.0d;

        if (listener == null)
        {
            return total;
        }

        // 这里只要一个实现即可
        total = listener.outNeedPayMoney(user, fullId);

        return total;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.manager.OutManager#payBaddebts(com.center.china.osgi.publics.User,
     *      java.lang.String, double)
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean payBaddebts(final User user, String fullId, double bad)
        throws MYException
    {
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            return false;
        }

        if ( !OutHelper.isSailEnd(out))
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (MathTools.compare(out.getHadPay() + bad, out.getTotal()) > 0)
        {
            throw new MYException("坏账金额过多,请确认操作");
        }

        // 付款的金额
        outDAO.modifyBadDebts(fullId, bad);

        out.setBadDebts(bad);

        // TAX_ADD 坏账的确认
        Collection<OutListener> listenerMapValues = listenerMapValues();

        for (OutListener listener : listenerMapValues)
        {
            listener.onConfirmBadDebts(user, out);
        }

        return true;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateInvoice(User user, String fullId, String invoiceId)
        throws MYException
    {
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (out.getInvoiceMoney() > 0)
        {
            throw new MYException("单据已经开票,不能修改发票类型");
        }

        outDAO.updateInvoice(fullId, invoiceId);

        return true;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean initPayOut(final User user, String fullId)
        throws MYException
    {
        // 需要增加是否超期 flowId
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            return false;
        }

        if ( !OutHelper.isSailEnd(out))
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (out.getPay() != OutConstant.PAY_YES)
        {
            throw new MYException("数据错误,请确认操作");
        }

        outDAO.modifyOutStatus(fullId, OutConstant.STATUS_PASS);

        addOutLog(fullId, user, out, "坏账消除", SailConstant.OPR_OUT_REJECT, OutConstant.STATUS_PASS);

        outDAO.modifyBadDebts(fullId, 0.0d);

        // 关联状态取消
        outDAO.updataBadDebtsCheckStatus(fullId, OutConstant.BADDEBTSCHECKSTATUS_NO);

        Collection<OutListener> listenerMapValues = this.listenerMapValues();

        // 通知其他的模块
        for (OutListener outListener : listenerMapValues)
        {
            outListener.onCancleBadDebts(user, out);
        }

        // 修改付款标识
        return outDAO.modifyPay(fullId, OutConstant.PAY_NOT);
    }

    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean mark(String fullId, boolean status)
    {
        return outDAO.mark(fullId, status);
    }

    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean modifyReDate(String fullId, String reDate)
    {
        return outDAO.modifyReDate(fullId, reDate);
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean addOutBalance(final User user, OutBalanceBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getBaseBalanceList());

        OutBean out = outDAO.find(bean.getOutId());

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !user.getStafferId().equals(out.getStafferId()))
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (out.getType() != OutConstant.OUT_TYPE_OUTBILL
            || out.getOutType() != OutConstant.OUTTYPE_OUT_CONSIGN)
        {
            throw new MYException("不是委托代销的销售单,请确认操作");
        }

        if (out.getStatus() != OutConstant.STATUS_PASS
            && out.getStatus() != OutConstant.STATUS_SEC_PASS)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setId(commonDAO.getSquenceString20());

        bean.setMtype(out.getMtype());

        outBalanceDAO.saveEntityBean(bean);

        List<BaseBalanceBean> baseBalanceList = bean.getBaseBalanceList();

        for (BaseBalanceBean baseBalanceBean : baseBalanceList)
        {
            baseBalanceBean.setId(commonDAO.getSquenceString20());

            baseBalanceBean.setParentId(bean.getId());

            baseBalanceBean.setOutId(bean.getOutId());

            baseBalanceBean.setMtype(out.getMtype());
        }

        baseBalanceDAO.saveAllEntityBeans(baseBalanceList);

        return true;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean passOutBalance(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBalanceBean bean = checkBalancePass(id);

        bean.setStatus(OutConstant.OUTBALANCE_STATUS_PASS);

        outBalanceDAO.updateEntityBean(bean);

        addOutLog2(id, user, OutConstant.OUTBALANCE_STATUS_SUBMIT, "结算中心通过",
            SailConstant.OPR_OUT_PASS, OutConstant.OUTBALANCE_STATUS_PASS);

        List<BaseBalanceBean> baseList = baseBalanceDAO.queryEntityBeansByFK(id);

        bean.setBaseBalanceList(baseList);

        // 这里结算单结束
        if (bean.getType() == OutConstant.OUTBALANCE_TYPE_SAIL)
        {
            // TAX_ADD 销售-结算单（审核通过）
            Collection<OutListener> listenerMapValues = listenerMapValues();

            for (OutListener listener : listenerMapValues)
            {
                listener.onOutBalancePass(user, bean);
            }
        }

        notifyManager.notifyMessage(bean.getStafferId(), bean.getOutId() + "的结算清单已经被["
                                                         + user.getStafferName() + "]通过");

        return true;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean passOutBalanceToDepot(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBalanceBean bean = checkBalancePassToDepot(id);

        bean.setStatus(OutConstant.OUTBALANCE_STATUS_END);

        outBalanceDAO.updateEntityBean(bean);

        boolean useDefaultDepotpart = true;

        DepotpartBean defaultOKDepotpart = null;

        if (bean.getType() == OutConstant.OUTBALANCE_TYPE_BACK
            && !StringTools.isNullOrNone(bean.getDirDepot()))
        {
            useDefaultDepotpart = false;

            defaultOKDepotpart = depotpartDAO.findDefaultOKDepotpart(bean.getDirDepot());

            if (defaultOKDepotpart == null)
            {
                throw new MYException("默认仓区不存在,请确认操作");
            }
        }
        else
        {
            throw new MYException("不是退货单或者没有选择退货的的仓库,请确认操作");
        }

        List<BaseBalanceBean> baseList = baseBalanceDAO.queryEntityBeansByFK(id);

        String sequence = commonDAO.getSquenceString();

        // 退货单是要变动库存的
        if (bean.getType() == OutConstant.OUTBALANCE_TYPE_BACK)
        {
            // 这里需要变动库存(增加库存)
            for (BaseBalanceBean each : baseList)
            {
                ProductChangeWrap wrap = new ProductChangeWrap();

                BaseBean element = baseDAO.find(each.getBaseId());

                if (useDefaultDepotpart)
                {
                    // 使用默认仓区
                    wrap.setDepotpartId(element.getDepotpartId());
                }
                else
                {
                    // 使用用户选择的仓库
                    wrap.setDepotpartId(defaultOKDepotpart.getId());
                }
                wrap.setPrice(element.getCostPrice());
                wrap.setProductId(element.getProductId());
                if (StringTools.isNullOrNone(element.getOwner()))
                {
                    wrap.setStafferId("0");
                }
                else
                {
                    wrap.setStafferId(element.getOwner());
                }

                // 增加的数量来自退货的数量
                wrap.setChange(each.getAmount());

                wrap.setDescription("库单[" + bean.getOutId() + "]代销退货操作");
                wrap.setSerializeId(sequence);
                wrap.setType(StorageConstant.OPR_STORAGE_BALANCE);
                wrap.setRefId(id);

                storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, false);
            }
        }

        addOutLog2(id, user, OutConstant.OUTBALANCE_STATUS_PASS, "库管通过", SailConstant.OPR_OUT_PASS,
            OutConstant.OUTBALANCE_STATUS_END);

        bean.setBaseBalanceList(baseList);

        if (bean.getType() == OutConstant.OUTBALANCE_TYPE_BACK)
        {
            // TAX_ADD 销售-结算退货单（审核通过）
            Collection<OutListener> listenerMapValues = listenerMapValues();

            for (OutListener listener : listenerMapValues)
            {
                listener.onOutBalancePass(user, bean);
            }
        }

        notifyManager.notifyMessage(bean.getStafferId(), bean.getOutId() + "的结算清单已经被["
                                                         + user.getStafferName() + "]退库通过");

        return true;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean deleteOutBalance(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBalanceBean bean = outBalanceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_REJECT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        outBalanceDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * 是否是领样转销售
     * 
     * @param fullId
     * @return
     */
    public boolean isSwatchToSail(String fullId)
    {
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            return false;
        }

        if (StringTools.isNullOrNone(out.getRefOutFullId()))
        {
            return false;
        }

        OutBean src = outDAO.find(out.getRefOutFullId());

        if (src == null)
        {
            return false;
        }

        if (src.getType() == OutConstant.OUT_TYPE_OUTBILL
            && src.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH)
        {
            return true;
        }

        return false;
    }

    /**
     * checkBalancePass
     * 
     * @param id
     * @return
     * @throws MYException
     */
    private OutBalanceBean checkBalancePass(String id)
        throws MYException
    {
        OutBalanceBean bean = outBalanceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_SUBMIT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        checkBalanceOver(id);

        return bean;
    }

    private OutBalanceBean checkBalancePassToDepot(String id)
        throws MYException
    {
        OutBalanceBean bean = outBalanceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_PASS)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getType() != OutConstant.OUTBALANCE_TYPE_BACK)
        {
            throw new MYException("只能是退货单入库,请确认操作");
        }

        checkBalanceOver(id);

        return bean;
    }

    private void checkBalanceOver(String id)
        throws MYException
    {
        // 检查是否结算溢出
        List<BaseBalanceBean> currentBaseList = baseBalanceDAO.queryEntityBeansByFK(id);

        // 看看是否溢出了
        for (BaseBalanceBean baseBalanceBean : currentBaseList)
        {
            BaseBean baseBean = baseDAO.find(baseBalanceBean.getBaseId());

            int total = baseBalanceBean.getAmount();
            List<BaseBalanceVO> hasPassBaseList = baseBalanceDAO
                .queryPassBaseBalance(baseBalanceBean.getBaseId());

            for (BaseBalanceBean pss : hasPassBaseList)
            {
                if ( !pss.getId().equals(baseBalanceBean.getId()))
                {
                    total += pss.getAmount();
                }
            }

            if (total > baseBean.getAmount())
            {
                throw new MYException(baseBean.getProductName() + "的数量溢出");
            }
        }
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean rejectOutBalance(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBalanceBean bean = outBalanceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getType() == OutConstant.OUTBALANCE_TYPE_SAIL)
        {
            if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_SUBMIT)
            {
                throw new MYException("结算中心通过的单子不能被驳回,请确认操作");
            }
        }
        else
        {
            if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_SUBMIT
                && bean.getStatus() != OutConstant.OUTBALANCE_STATUS_PASS)
            {
                throw new MYException("数据错误,请确认操作");
            }
        }

        int old = bean.getStatus();

        bean.setStatus(OutConstant.OUTBALANCE_STATUS_REJECT);

        bean.setReason(reason);

        outBalanceDAO.updateEntityBean(bean);

        addOutLog2(id, user, old, "驳回", SailConstant.OPR_OUT_REJECT,
            OutConstant.OUTBALANCE_STATUS_REJECT);

        notifyManager.notifyMessage(bean.getStafferId(), bean.getOutId() + "的结算清单已经被["
                                                         + user.getStafferName() + "]驳回");

        return true;
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

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @return the productDAO
     */
    public ProductDAO getProductDAO()
    {
        return productDAO;
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
     * @param productDAO
     *            the productDAO to set
     */
    public void setProductDAO(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
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

    private String getAll(int i)
    {
        String s = "00000000" + i;

        return s.substring(s.length() - 9);
    }

    private String getOutId(String idStr)
    {
        while (idStr.length() > 0 && idStr.charAt(0) == '0')
        {
            idStr = idStr.substring(1);
        }

        return idStr;
    }

    /**
     * @return the consignDAO
     */
    public ConsignDAO getConsignDAO()
    {
        return consignDAO;
    }

    /**
     * @param consignDAO
     *            the consignDAO to set
     */
    public void setConsignDAO(ConsignDAO consignDAO)
    {
        this.consignDAO = consignDAO;
    }

    /**
     * TODO 发送短信(只有销售单发送短信)(从短信接收模块来说需要修复回复短信处理结果)
     * 
     * @param out
     * @param user
     */
    private void notifyOut(OutBean out, User user, int type)
    {
        if (out.getType() == 1)
        {
            return;
        }

        NotifyBean notify = new NotifyBean();

        if (type == 0)
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]审批通过");
        }
        else if (type == 1)
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]驳回");
        }
        else if (type == 2)
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]确认付款");
        }
        else if (type == 3)
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]总部核对,单据流程结束");
        }
        else
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]处理");
        }

        notify.setUrl("../sail/out.do?method=findOut&fow=99&outId=" + out.getFullId());

        notifyManager.notifyWithoutTransaction(out.getStafferId(), notify);

        if (false)
        {
            // 0:保存 1:提交 2:驳回 3:发货 4:会计审核通过 6:总经理审核通过
            if (out.getStatus() == OutConstant.STATUS_SAVE
                || out.getStatus() == OutConstant.STATUS_REJECT
                || out.getStatus() == OutConstant.STATUS_SEC_PASS)
            {
                return;
            }

            // 发送短信给区域总经理审核
            if (out.getStatus() == OutConstant.STATUS_SUBMIT)
            {
                ConditionParse condtition = new ConditionParse();

                condtition.addCondition("locationId", "=", out.getLocationId());

                condtition.addIntCondition("status", "=", 0);

                condtition.addIntCondition("role", "=", 4);

                queryUserToSendSMS(out, user, condtition, "总经理审批");
            }

            // 发送短信给库管审核(非总部)
            if (out.getStatus() == OutConstant.STATUS_MANAGER_PASS
                && !"0".equals(out.getLocation()))
            {
                ConditionParse condtition = new ConditionParse();

                condtition.addCondition("locationId", "=", out.getLocationId());

                condtition.addIntCondition("status", "=", 0);

                condtition.addIntCondition("role", "=", 1);

                queryUserToSendSMS(out, user, condtition, "库管员审批");
            }
        }

    }

    /**
     * queryUserToSendSMS
     * 
     * @param out
     * @param user
     * @param condtition
     */
    private void queryUserToSendSMS(OutBean out, User user, ConditionParse condtition,
                                    String tokenName)
    {
        List<UserBean> userList = ListTools.distinct(userDAO
            .queryEntityBeansByCondition(condtition));

        for (UserBean baseUser : userList)
        {
            StafferBean sb = stafferDAO.find(baseUser.getStafferId());

            if (sb != null && !StringTools.isNullOrNone(sb.getHandphone()))
            {
                sendSMSInner(out, user, sb, tokenName);
            }
        }
    }

    /**
     * sendSMS
     * 
     * @param out
     * @param user
     * @param sb
     */
    private void sendSMSInner(OutBean out, User user, StafferBean sb, String tokenName)
    {
        StafferBean realStaffer = stafferDAO.find(out.getStafferId());

        if (realStaffer == null)
        {
            return;
        }

        // DBT 动态引入的判断
        if ( !DynamicBundleTools.isServiceExist("com.china.center.oa.note.dao.ShortMessageTaskDAO"))
        {
            return;
        }

        ShortMessageTaskDAO shortMessageTaskDAO = DynamicBundleTools
            .getService(ShortMessageTaskDAO.class);

        if (shortMessageTaskDAO == null)
        {
            return;
        }

        // TODO 恢复短信回复的处理 send short message
        ShortMessageTaskBean sms = new ShortMessageTaskBean();

        sms.setId(commonDAO.getSquenceString20());

        sms.setFk(out.getFullId());

        sms.setType(HandleMessage.TYPE_OUT);

        sms.setHandId(RandomTools.getRandomMumber(4));

        sms.setStatus(ShortMessageConstant.STATUS_INIT);

        sms.setMtype(ShortMessageConstant.MTYPE_ONLY_SEND_RECEIVE);

        sms.setFktoken(String.valueOf(out.getStatus()));

        sms.setMessage(realStaffer.getName() + "发起销售单[" + out.getDescription() + "(回款天数:"
                       + out.getReday() + ";总金额:" + MathTools.formatNum(out.getTotal()) + ")]"
                       + "需您审批(" + tokenName + ").0通过,1驳回.回复格式[" + sms.getHandId() + ":0]或["
                       + sms.getHandId() + ":1:理由]");

        sms.setReceiver(sb.getHandphone());

        sms.setStafferId(sb.getId());

        sms.setLogTime(TimeTools.now());

        // 24 hour
        sms.setEndTime(TimeTools.now(1));

        // internal
        sms.setSendTime(TimeTools.getDateTimeString(this.internal));

        // add sms
        shortMessageTaskDAO.saveEntityBean(sms);
    }

    public int findOutStatusInLog(String fullId)
    {
        // 获取日志，正排序
        List<FlowLogBean> logList = flowLogDAO.queryEntityBeansByFK(fullId);

        if (ListTools.isEmptyOrNull(logList))
        {
            return -1;
        }

        return logList.get(logList.size() - 1).getAfterStatus();
    }

    /**
     * 增加日志
     * 
     * @param fullId
     * @param user
     * @param outBean
     */
    private void addOutLog(final String fullId, final User user, final OutBean outBean, String des,
                           int mode, int astatus)
    {
        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());

        log.setDescription(des);
        log.setFullId(fullId);
        log.setOprMode(mode);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(outBean.getStatus());

        log.setAfterStatus(astatus);

        flowLogDAO.saveEntityBean(log);
    }

    private void addOutLog2(final String fullId, final User user, final int preStatus, String des,
                            int mode, int astatus)
    {
        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());

        log.setDescription(des);
        log.setFullId(fullId);
        log.setOprMode(mode);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(preStatus);

        log.setAfterStatus(astatus);

        flowLogDAO.saveEntityBean(log);
    }

    /**
     * loggerError(严重错误的日志哦)
     * 
     * @param msg
     */
    private void loggerError(String msg)
    {
        importLog.error(msg);

        fatalNotify.notify(msg);
    }

    /**
     * @return the depotpartDAO
     */
    public DepotpartDAO getDepotpartDAO()
    {
        return depotpartDAO;
    }

    /**
     * @param depotpartDAO
     *            the depotpartDAO to set
     */
    public void setDepotpartDAO(DepotpartDAO depotpartDAO)
    {
        this.depotpartDAO = depotpartDAO;
    }

    /**
     * @return the internal
     */
    public int getInternal()
    {
        return internal;
    }

    /**
     * @param internal
     *            the internal to set
     */
    public void setInternal(int internal)
    {
        this.internal = internal;
    }

    /**
     * @return the parameterDAO
     */
    public ParameterDAO getParameterDAO()
    {
        return parameterDAO;
    }

    /**
     * @param parameterDAO
     *            the parameterDAO to set
     */
    public void setParameterDAO(ParameterDAO parameterDAO)
    {
        this.parameterDAO = parameterDAO;
    }

    /**
     * @return the creditLevelDAO
     */
    public CreditLevelDAO getCreditLevelDAO()
    {
        return creditLevelDAO;
    }

    /**
     * @param creditLevelDAO
     *            the creditLevelDAO to set
     */
    public void setCreditLevelDAO(CreditLevelDAO creditLevelDAO)
    {
        this.creditLevelDAO = creditLevelDAO;
    }

    /**
     * @return the stafferDAO
     */
    public StafferDAO getStafferDAO()
    {
        return stafferDAO;
    }

    /**
     * @param stafferDAO
     *            the stafferDAO to set
     */
    public void setStafferDAO(StafferDAO stafferDAO)
    {
        this.stafferDAO = stafferDAO;
    }

    /**
     * @return the baseDAO
     */
    public BaseDAO getBaseDAO()
    {
        return baseDAO;
    }

    /**
     * @param baseDAO
     *            the baseDAO to set
     */
    public void setBaseDAO(BaseDAO baseDAO)
    {
        this.baseDAO = baseDAO;
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
     * @return the creditCoreDAO
     */
    public CreditCoreDAO getCreditCoreDAO()
    {
        return creditCoreDAO;
    }

    /**
     * @param creditCoreDAO
     *            the creditCoreDAO to set
     */
    public void setCreditCoreDAO(CreditCoreDAO creditCoreDAO)
    {
        this.creditCoreDAO = creditCoreDAO;
    }

    /**
     * @return the flowLogDAO
     */
    public FlowLogDAO getFlowLogDAO()
    {
        return flowLogDAO;
    }

    /**
     * @param flowLogDAO
     *            the flowLogDAO to set
     */
    public void setFlowLogDAO(FlowLogDAO flowLogDAO)
    {
        this.flowLogDAO = flowLogDAO;
    }

    /**
     * @return the transactionManager
     */
    public PlatformTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param transactionManager
     *            the transactionManager to set
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    /**
     * @return the storageRelationManager
     */
    public StorageRelationManager getStorageRelationManager()
    {
        return storageRelationManager;
    }

    /**
     * @param storageRelationManager
     *            the storageRelationManager to set
     */
    public void setStorageRelationManager(StorageRelationManager storageRelationManager)
    {
        this.storageRelationManager = storageRelationManager;
    }

    /**
     * @return the outUniqueDAO
     */
    public OutUniqueDAO getOutUniqueDAO()
    {
        return outUniqueDAO;
    }

    /**
     * @param outUniqueDAO
     *            the outUniqueDAO to set
     */
    public void setOutUniqueDAO(OutUniqueDAO outUniqueDAO)
    {
        this.outUniqueDAO = outUniqueDAO;
    }

    /**
     * @return the depotDAO
     */
    public DepotDAO getDepotDAO()
    {
        return depotDAO;
    }

    /**
     * @param depotDAO
     *            the depotDAO to set
     */
    public void setDepotDAO(DepotDAO depotDAO)
    {
        this.depotDAO = depotDAO;
    }

    /**
     * @return the notifyManager
     */
    public NotifyManager getNotifyManager()
    {
        return notifyManager;
    }

    /**
     * @param notifyManager
     *            the notifyManager to set
     */
    public void setNotifyManager(NotifyManager notifyManager)
    {
        this.notifyManager = notifyManager;
    }

    /**
     * @return the fatalNotify
     */
    public FatalNotify getFatalNotify()
    {
        return fatalNotify;
    }

    /**
     * @param fatalNotify
     *            the fatalNotify to set
     */
    public void setFatalNotify(FatalNotify fatalNotify)
    {
        this.fatalNotify = fatalNotify;
    }

    /**
     * @return the baseBalanceDAO
     */
    public BaseBalanceDAO getBaseBalanceDAO()
    {
        return baseBalanceDAO;
    }

    /**
     * @param baseBalanceDAO
     *            the baseBalanceDAO to set
     */
    public void setBaseBalanceDAO(BaseBalanceDAO baseBalanceDAO)
    {
        this.baseBalanceDAO = baseBalanceDAO;
    }

    /**
     * @return the outBalanceDAO
     */
    public OutBalanceDAO getOutBalanceDAO()
    {
        return outBalanceDAO;
    }

    /**
     * @param outBalanceDAO
     *            the outBalanceDAO to set
     */
    public void setOutBalanceDAO(OutBalanceDAO outBalanceDAO)
    {
        this.outBalanceDAO = outBalanceDAO;
    }

    /**
     * CORE 处理销售单的通过
     * 
     * @param fullId
     * @param user
     * @param outBean
     * @param depot
     * @param newNextStatus
     */
    private void handerPassOut(final String fullId, final User user, final OutBean outBean,
                               final DepotBean depot, int newNextStatus)
    {
        // 从分公司经理审核通过到提交
        if (newNextStatus == OutConstant.STATUS_SUBMIT)
        {
            // 只有信用已经超支的情况下才启用分事业部经理的信用
            if (outBean.getReserve2() == OutConstant.OUT_CREDIT_OVER)
            {
                // 加入审批人的信用(是自己使用的信用+担保的信用)
                double noPayBusinessByManager = outDAO.sumAllNoPayAndAvouchBusinessByStafferId(user
                    .getStafferId(), outBean.getIndustryId(), YYTools.getStatBeginDate(), YYTools
                    .getStatEndDate());

                StafferBean staffer = stafferDAO.find(user.getStafferId());

                // 这里自己不能给自己担保的
                if (outBean.getStafferId().equals(user.getStafferId()))
                {
                    throw new RuntimeException("事业部经理担保中,自己不能给自己担保");
                }

                // 事业部经理的信用
                double industryIdCredit = getIndustryIdCredit(outBean.getIndustryId(), staffer
                    .getId())
                                          * staffer.getLever();

                // 这里分公司总经理的信用已经使用结束了,此时直接抛出异常
                if (noPayBusinessByManager > industryIdCredit)
                {
                    throw new RuntimeException("您的信用额度已经全部占用[使用了"
                                               + MathTools.formatNum(noPayBusinessByManager)
                                               + "],不能再担保业务员的销售");
                }

                // 本次需要担保的信用
                double lastCredit = outBean.getTotal() - outBean.getStaffcredit()
                                    - outBean.getCurcredit();

                if ( (lastCredit + noPayBusinessByManager) > industryIdCredit)
                {
                    throw new RuntimeException("您杠杆后的信用额度是["
                                               + MathTools.formatNum(industryIdCredit) + "],已经使用了["
                                               + MathTools.formatNum(noPayBusinessByManager)
                                               + "],本单需要您担保的额度是[" + MathTools.formatNum(lastCredit)
                                               + "],加上本单已经超出您的最大额度,不能再担保业务员的销售");
                }

                // 这里使用分公司经理信用担保
                outDAO.updateManagercredit(outBean.getFullId(), user.getStafferId(), lastCredit);

                // 此时信用不超支了
                outDAO.updateOutReserve(fullId, OutConstant.OUT_CREDIT_COMMON, outBean
                    .getReserve6());
            }

            try
            {
                checkCoreStorage(outBean, true);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent(), e);
            }
        }

        // 结算中心通过/总裁通过 修改manager的入库时间
        if (newNextStatus == OutConstant.STATUS_MANAGER_PASS)
        {
            outDAO.modifyManagerTime(outBean.getFullId(), TimeTools.now());

            // 验证是否是款到发货
            if (outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_MONEY
                && outBean.getPay() != OutConstant.PAY_YES)
            {
                throw new RuntimeException("此单据是款到发货,当前此单未付款,不能通过");
            }

            try
            {
                checkCoreStorage(outBean, true);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent(), e);
            }
        }

        // 结算中心审核通过后/总裁通过，中心仓库的销售单转到物流管理员，同时自动生成发货单
        if (newNextStatus == OutConstant.STATUS_MANAGER_PASS
            && depot.getType() == DepotConstant.DEPOT_TYPE_CENTER)
        {
            ConsignBean bean = new ConsignBean();

            bean.setCurrentStatus(SailConstant.CONSIGN_INIT);

            bean.setGid(commonDAO.getSquenceString20());

            bean.setFullId(outBean.getFullId());

            bean.setArriveDate(outBean.getArriveDate());

            consignDAO.addConsign(bean);

            try
            {
                checkCoreStorage(outBean, true);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent(), e);
            }
        }

        // CORE 需要把回款日志敲定且变动销售库存(库管通过)
        if (newNextStatus == OutConstant.STATUS_PASS)
        {
            // 只有未付款的时候才有
            if (outBean.getPay() == OutConstant.PAY_NOT)
            {
                long add = outBean.getReday() * 24 * 3600 * 1000L;

                // 这里需要把出库单的回款日期修改
                outDAO.modifyReDate(fullId, TimeTools.getStringByFormat(new Date(new Date()
                    .getTime()
                                                                                 + add),
                    "yyyy-MM-dd"));
            }

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            try
            {
                // 变动库存
                processBuyAndSailPass(user, outBean, baseList, StorageConstant.OPR_STORAGE_OUTBILL);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent(), e);
            }
        }
    }

    /**
     * CORE 处理入库单的流程
     * 
     * @param fullId
     * @param user
     * @param outBean
     * @param newNextStatus
     */
    private void handerPassBuy(final String fullId, final User user, final OutBean outBean,
                               int newNextStatus)
    {
        // 退库-事业部经理审批
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && (outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH || outBean.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK))
        {
            // 不处理
            return;
        }

        // 分公司总经理审批-->待总裁审批
        if (newNextStatus == OutConstant.STATUS_CEO_CHECK)
        {
            try
            {
                checkCoreStorage(outBean, true);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent(), e);
            }
        }

        // 待总裁审批-->待董事长审批
        if (newNextStatus == OutConstant.STATUS_CHAIRMA_CHECK)
        {
            try
            {
                checkCoreStorage(outBean, true);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent(), e);
            }
        }

        // CORE 待董事长审批-->发货
        if (newNextStatus == OutConstant.STATUS_PASS)
        {
            long add = outBean.getReday() * 24 * 3600 * 1000L;

            // 这里需要把出库单的回款日期修改
            outDAO.modifyReDate(fullId, TimeTools.getStringByFormat(new Date(new Date().getTime()
                                                                             + add), "yyyy-MM-dd"));

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            try
            {
                // 变动库存
                processBuyAndSailPass(user, outBean, baseList, StorageConstant.OPR_STORAGE_INOTHER);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent(), e);
            }
        }
    }

    /**
     * 检查是否可以转销售(入数据库但是没有提交)
     * 
     * @param bean
     * @param request
     * @return
     * @throws MYException
     */
    public boolean checkSwithToSail(String outId)
        throws MYException
    {
        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outId);

        // 验证ref
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        // 包括保存的,防止溢出
        List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

        con.clear();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        // 排除其他入库(对冲单据)
        con.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_IN_OTHER);

        // 包括保存的,防止溢出
        List<OutBean> refBuyList = outDAO.queryEntityBeansByCondition(con);

        // 计算出已经退货的数量
        for (Iterator iterator = baseList.iterator(); iterator.hasNext();)
        {
            BaseBean baseBean = (BaseBean)iterator.next();

            int hasBack = 0;

            // 退库
            for (OutBean ref : refBuyList)
            {
                List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(ref.getFullId());

                for (BaseBean refBase : refBaseList)
                {
                    if (refBase.equals(baseBean))
                    {
                        hasBack += refBase.getAmount();

                        break;
                    }
                }
            }

            // 转销售的
            for (OutBean ref : refList)
            {
                List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(ref.getFullId());

                for (BaseBean refBase : refBaseList)
                {
                    if (refBase.equals(baseBean))
                    {
                        hasBack += refBase.getAmount();

                        break;
                    }
                }
            }

            baseBean.setAmount(baseBean.getAmount() - hasBack);

            if (baseBean.getAmount() < 0)
            {
                throw new MYException("领样转销售或者退库数量不够[%s],请重新操作", baseBean.getProductName());
            }
        }

        return true;
    }

    private double getIndustryIdCredit(String industryId, String managerStafferId)
    {
        List<InvoiceCreditBean> inList = invoiceCreditDAO.queryEntityBeansByFK(managerStafferId);

        for (InvoiceCreditBean invoiceCreditBean : inList)
        {
            if (invoiceCreditBean.getInvoiceId().equals(industryId))
            {
                return invoiceCreditBean.getCredit();
            }
        }

        return 0.0d;
    }

    /**
     * @return the invoiceCreditDAO
     */
    public InvoiceCreditDAO getInvoiceCreditDAO()
    {
        return invoiceCreditDAO;
    }

    /**
     * @param invoiceCreditDAO
     *            the invoiceCreditDAO to set
     */
    public void setInvoiceCreditDAO(InvoiceCreditDAO invoiceCreditDAO)
    {
        this.invoiceCreditDAO = invoiceCreditDAO;
    }

    /**
     * handlerMoveOutBack
     * 
     * @param fullId
     * @param user
     * @param outBean
     */
    private void handlerMoveOutBack(final String fullId, final User user, final OutBean outBean)
    {
        // 这里的回滚先把入库单结束掉(就是把在途状态修改)
        OutBean newOut = new OutBean(outBean);

        newOut.setStatus(0);

        newOut.setLocationId(user.getLocationId());

        // 自己调入自己
        newOut.setDestinationId(outBean.getLocation());

        newOut.setOutType(OutConstant.OUTTYPE_IN_MOVEOUT);

        newOut.setFullId("");

        newOut.setRefOutFullId(fullId);

        newOut.setDestinationId(outBean.getLocation());

        newOut.setDescription("驳回调拨单自动回滚:" + fullId + ".生成的回滚单据");

        newOut.setInway(OutConstant.IN_WAY_NO);

        if (outBean.getCheckStatus() == PublicConstant.CHECK_STATUS_INIT)
        {
            newOut.setChecks("原调拨单据还未核对,系统自动核对回滚单据");

            newOut.setCheckStatus(PublicConstant.CHECK_STATUS_END);
        }
        else
        {
            newOut.setChecks("");

            newOut.setCheckStatus(PublicConstant.CHECK_STATUS_INIT);
        }

        newOut.setReserve1(OutConstant.MOVEOUT_ROLLBACK);

        newOut.setPay(OutConstant.PAY_NOT);

        newOut.setTotal( -newOut.getTotal());

        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

        for (BaseBean baseBean : baseList)
        {
            baseBean.setValue( -baseBean.getValue());
            baseBean.setLocationId(outBean.getDestinationId());
            baseBean.setAmount( -baseBean.getAmount());
        }

        List<BaseBean> lastList = OutHelper.trimBaseList(baseList);

        newOut.setBaseList(lastList);

        // 直接入库
        try
        {
            coloneOutAndSubmitWithOutAffair(newOut, user,
                StorageConstant.OPR_STORAGE_REDEPLOY_ROLLBACK);

            if (outBean.getCheckStatus() == PublicConstant.CHECK_STATUS_INIT)
            {
                outDAO.modifyOutStatus(newOut.getFullId(), OutConstant.STATUS_SEC_PASS);
            }
        }
        catch (MYException e)
        {
            _logger.error(e, e);

            throw new RuntimeException(e.getErrorContent(), e);
        }
    }

    /**
     * @return the dutyDAO
     */
    public DutyDAO getDutyDAO()
    {
        return dutyDAO;
    }

    /**
     * @param dutyDAO
     *            the dutyDAO to set
     */
    public void setDutyDAO(DutyDAO dutyDAO)
    {
        this.dutyDAO = dutyDAO;
    }

    @Transactional(rollbackFor = {MYException.class})
    public void initPmtype()
    {
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("outTime", ">=", "2012-01-01");

        con.addIntCondition("mtype", "=", PublicConstant.MANAGER_TYPE_MANAGER);

        List<OutBean> outList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : outList)
        {
            // 管理的开单类型必须一致(要么全部是普通,要么全部是管理)
            int mtype = 0;

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            for (BaseBean baseBean : baseList)
            {
                ProductBean product = productDAO.find(baseBean.getProductId());

                if (product != null)
                {
                    mtype = CommonTools.parseInt(product.getReserve4());

                    break;
                }
            }

            // 更新pmtype
            outDAO.updatePmtype(outBean.getFullId(), mtype);
        }
    }

    @Transactional(rollbackFor = {MYException.class})
    public void initOutBackBasePrice()
    {
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("outTime", ">=", "2012-02-01");

        con.addIntCondition("type", "=", OutConstant.OUT_TYPE_INBILL);

        con.addCondition("and outType in (4, 5)");

        List<OutBean> outList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : outList)
        {
            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            OutBean refOut = outDAO.find(outBean.getRefOutFullId());

            if (refOut != null)
            {
                List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(refOut.getFullId());

                // 修改结算价
                for (BaseBean each1 : baseList)
                {
                    for (BaseBean each2 : refBaseList)
                    {
                        if (each1.equals2(each2))
                        {
                            each1.setIprice(each2.getIprice());
                            each1.setPprice(each2.getPprice());
                            each1.setInputPrice(each2.getInputPrice());

                            // 更新
                            baseDAO.updateEntityBean(each1);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = {MYException.class})
    public void initOutPrice()
    {
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("outTime", ">=", "2012-04-01");

        con.addIntCondition("type", "=", OutConstant.OUT_TYPE_OUTBILL);

        List<OutBean> outList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : outList)
        {
            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            StafferBean sb = stafferDAO.find(outBean.getStafferId());

            if (sb == null)
            {
                continue;
            }

            for (BaseBean baseBean : baseList)
            {
                ProductBean product = productDAO.find(baseBean.getProductId());

                if (product != null && baseBean.getInputPrice() == 0.0)
                {
                    SailConfBean sailConf = sailConfigManager.findProductConf(sb, product);

                    double sailPrice = product.getSailPrice();

                    baseBean.setPprice(sailPrice * (1 + sailConf.getPratio() / 1000.0d));

                    // 就是看到的结算价
                    baseBean
                        .setIprice(sailPrice
                                   * (1 + sailConf.getPratio() / 1000.0d + sailConf.getIratio() / 1000.0d));

                    baseBean.setInputPrice(baseBean.getIprice());

                    System.out.println("更新[" + product.getName() + "]在:" + outBean.getFullId()
                                       + ".新价格:" + MathTools.formatNum(baseBean.getInputPrice()));

                    baseDAO.updateEntityBean(baseBean);
                }
            }
        }
    }

    public int[] initPriceKey()
    {
        int[] result = new int[2];

        int success = 0;
        int fail = 0;

        String lastTime = "2011-04-01";

        String outTime = TimeTools.now_short( -180);

        if (lastTime.compareTo(outTime) > 0)
        {
            outTime = lastTime;
        }

        int count = baseDAO.countBaseByOutTime(outTime);

        PageSeparate page = new PageSeparate();

        page.reset2(count, 2000);

        while (page.hasNextPage())
        {
            page.nextPage();

            List<BaseBean> baseList = baseDAO.queryBaseByOutTime(outTime, page);

            for (final BaseBean each : baseList)
            {
                // 核对品名和开单不对的单据
                ProductBean product = productDAO.find(each.getProductId());

                if (product == null)
                {
                    _logger.error("BASE:" + each.getId() + ",product is null:"
                                  + each.getProductId());
                }

                if (product != null && !product.getName().equals(each.getProductName()))
                {
                    _logger.error("BASE:" + each.getId() + ",product is name error:"
                                  + each.getProductName() + ", real is:" + product.getName()
                                  + ",fullid is:" + each.getOutId());
                }

                String priceKey = StorageRelationHelper.getPriceKey(each.getCostPrice());

                if ( !priceKey.equals(each.getCostPriceKey()))
                {
                    _logger.info(each + "||old CostPriceKey:" + each.getCostPriceKey()
                                 + ";new CostPriceKey:" + priceKey);

                    each.setCostPriceKey(priceKey);

                    try
                    {
                        // 增加管理员操作在数据库事务中完成
                        TransactionTemplate tran = new TransactionTemplate(transactionManager);

                        tran.execute(new TransactionCallback()
                        {
                            public Object doInTransaction(TransactionStatus arg0)
                            {
                                baseDAO.updateCostPricekey(each.getId(), each.getCostPriceKey());

                                return Boolean.TRUE;
                            }
                        });

                        success++ ;
                    }
                    catch (Exception e)
                    {
                        fail++ ;

                        _logger.error(e, e);
                    }
                }
            }
        }

        result[0] = success;
        result[1] = fail;

        return result;
    }

    public void exportAllStafferCredit()
    {
        triggerLog.info("begin exportAllStafferCredit...");

        WriteFile write = null;

        OutputStream out = null;

        try
        {
            out = new FileOutputStream(getStafferCreditStorePath() + "/StafferCredit_"
                                       + TimeTools.now("yyyyMMddHHmmss") + ".csv");

            List<StafferBean> stafferList = stafferDAO.listCommonEntityBeans();

            write = WriteFileFactory.getMyTXTWriter();

            write.openFile(out);

            writeStafferCredit(write, stafferList);

            write.close();

        }
        catch (Throwable e)
        {
            triggerLog.error(e, e);
        }
        finally
        {
            if (write != null)
            {
                try
                {
                    write.close();
                }
                catch (IOException e1)
                {
                }
            }

            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                }
            }
        }

        triggerLog.info("end exportAllStafferCredit...");
    }

    public void writeStafferCredit(WriteFile write, List<StafferBean> stafferList)
        throws IOException
    {
        write.writeLine("日期,类别,职员,总信用额度,单据,原始信用,信用杠杆,开单使用额度,担保使用额度,被担保额度,剩余额度,其他");

        String now = TimeTools.now("yyyy-MM-dd");

        WriteFileBuffer line = new WriteFileBuffer(write);

        for (StafferBean staffer : stafferList)
        {
            if (staffer.getCredit() <= 0)
            {
                continue;
            }

            // 自己信用
            double st = outDAO.sumNoPayAndAvouchBusinessByStafferId(staffer.getId(), staffer
                .getIndustryId(), YYTools.getStatBeginDate(), YYTools.getStatEndDate());

            // 担保他人
            double mt = outDAO.sumNoPayAndAvouchBusinessByManagerId2(staffer.getId(), YYTools
                .getStatBeginDate(), YYTools.getStatEndDate());

            // 被担保
            double bei = outDAO.sumNoPayAndAvouchBusinessByManagerId3(staffer.getId(), YYTools
                .getStatBeginDate(), YYTools.getStatEndDate());

            // 总信用
            double total = staffer.getCredit() * staffer.getLever();

            StringBuffer buffer = new StringBuffer();

            List<InvoiceCreditVO> vsList = invoiceCreditDAO.queryEntityVOsByFK(staffer.getId());

            for (InvoiceCreditVO invoiceCreditVO : vsList)
            {
                buffer.append(invoiceCreditVO.getInvoiceName()).append("下的信用额度:").append(
                    MathTools.formatNum(invoiceCreditVO.getCredit() * staffer.getLever())).append(
                    ".");
            }

            line.writeColumn(now);
            line.writeColumn("合计");
            line.writeColumn(staffer.getName());
            line.writeColumn(total);
            line.writeColumn("");
            line.writeColumn(staffer.getCredit());
            line.writeColumn(staffer.getLever());
            line.writeColumn(st);
            line.writeColumn(mt);
            line.writeColumn(bei);
            line.writeColumn(total - st - mt);
            line.writeColumn(buffer.toString());

            line.writeLine();

            // 自己信用使用明细
            List<CreditWrap> creditList = outDAO.queryNoPayAndAvouchBusinessByStafferId(staffer
                .getId(), staffer.getIndustryId(), YYTools.getStatBeginDate(), YYTools
                .getStatEndDate());

            for (CreditWrap creditWrap : creditList)
            {
                if (creditWrap.getCredit() > 0)
                {
                    line.writeColumn(creditWrap.getOutTime());
                    line.writeColumn("自己使用");
                    line.writeColumn(staffer.getName());
                    line.writeColumn("");
                    line.writeColumn(creditWrap.getFullId());
                    line.writeColumn("");
                    line.writeColumn("");
                    line.writeColumn(creditWrap.getCredit());
                    line.writeLine();
                }
            }

            creditList = outDAO.queryNoPayAndAvouchBusinessByManagerId2(staffer.getId(), YYTools
                .getStatBeginDate(), YYTools.getStatEndDate());

            for (CreditWrap creditWrap : creditList)
            {
                line.writeColumn(creditWrap.getOutTime());
                line.writeColumn("担保他人");
                line.writeColumn(staffer.getName());
                line.writeColumn("");
                line.writeColumn(creditWrap.getFullId());
                line.writeColumn("");
                line.writeColumn("");
                line.writeColumn("");
                line.writeColumn(creditWrap.getCredit());
                line.writeLine();
            }

            creditList = outDAO.queryNoPayAndAvouchBusinessByManagerId3(staffer.getId(), YYTools
                .getStatBeginDate(), YYTools.getStatEndDate());

            for (CreditWrap creditWrap : creditList)
            {
                line.writeColumn(creditWrap.getOutTime());
                line.writeColumn("被担保");
                line.writeColumn(staffer.getName());
                line.writeColumn("");
                line.writeColumn(creditWrap.getFullId());
                line.writeColumn("");
                line.writeColumn("");
                line.writeColumn("");
                line.writeColumn("");
                line.writeColumn(creditWrap.getCredit());
                line.writeLine();
            }
            line.writeLine();
        }
    }

    /**
     * CORE 内部实现(所有的增加库单都调用这个方法)
     * 
     * @param outBean
     */
    private void saveOutInner(final OutBean outBean)
        throws MYException
    {
        configOutBean(outBean);

        List<BaseBean> baseList = outBean.getBaseList();

        // MANAGER 管理类型的库单处理
        if (OATools.getManagerFlag())
        {
            if (OATools.isCommon(outBean.getMtype()))
            {
                for (BaseBean baseBean : baseList)
                {
                    ProductBean product = productDAO.find(baseBean.getProductId());

                    if (OATools.isManager(product.getReserve4()))
                    {
                        throw new MYException("库单当前所属的纳税实体是普通类型,当前产品[%s]不是", product.getName());
                    }
                }

                outBean.setPmtype(PublicConstant.MANAGER_TYPE_COMMON);
            }
            else
            {
                // 管理的开单类型必须一致(要么全部是普通,要么全部是管理)
                int mtype = -1;

                for (BaseBean baseBean : baseList)
                {
                    ProductBean product = productDAO.find(baseBean.getProductId());

                    if (mtype == -1)
                    {
                        mtype = OATools.getManagerType(product.getReserve4());
                    }
                    else
                    {
                        if (OATools.getManagerType(product.getReserve4()) != mtype)
                        {
                            throw new MYException("库单里面的产品管理属性必须完全一致,当前产品[%s]不是", product.getName());
                        }
                    }

                }

                // 产品类型
                outBean.setPmtype(mtype);

                // 产品管理类型和单据的管理类型不一样
                if (mtype != outBean.getMtype())
                {
                    outBean.setVtype(OutConstant.VTYPE_SPECIAL);
                }
            }
        }

        outDAO.saveEntityBean(outBean);
    }

    private void configOutBean(final OutBean outBean)
    {
        DutyBean duty = dutyDAO.find(outBean.getDutyId());

        if (duty != null)
        {
            outBean.setMtype(duty.getMtype());
        }
    }

    /**
     * @return the mailAttchmentPath
     */
    public String getStafferCreditStorePath()
    {
        return ConfigLoader.getProperty("stafferCreditStore");
    }

    /**
     * @return the storageRelationDAO
     */
    public StorageRelationDAO getStorageRelationDAO()
    {
        return storageRelationDAO;
    }

    /**
     * @param storageRelationDAO
     *            the storageRelationDAO to set
     */
    public void setStorageRelationDAO(StorageRelationDAO storageRelationDAO)
    {
        this.storageRelationDAO = storageRelationDAO;
    }

    /**
     * @return the invoiceDAO
     */
    public InvoiceDAO getInvoiceDAO()
    {
        return invoiceDAO;
    }

    /**
     * @param invoiceDAO
     *            the invoiceDAO to set
     */
    public void setInvoiceDAO(InvoiceDAO invoiceDAO)
    {
        this.invoiceDAO = invoiceDAO;
    }

    /**
     * @return the sailConfigDAO
     */
    public SailConfigDAO getSailConfigDAO()
    {
        return sailConfigDAO;
    }

    /**
     * @param sailConfigDAO
     *            the sailConfigDAO to set
     */
    public void setSailConfigDAO(SailConfigDAO sailConfigDAO)
    {
        this.sailConfigDAO = sailConfigDAO;
    }

    /**
     * @return the sailConfigManager
     */
    public SailConfigManager getSailConfigManager()
    {
        return sailConfigManager;
    }

    /**
     * @param sailConfigManager
     *            the sailConfigManager to set
     */
    public void setSailConfigManager(SailConfigManager sailConfigManager)
    {
        this.sailConfigManager = sailConfigManager;
    }

    /**
     * @return the sailTranApplyDAO
     */
    public SailTranApplyDAO getSailTranApplyDAO()
    {
        return sailTranApplyDAO;
    }

    /**
     * @param sailTranApplyDAO
     *            the sailTranApplyDAO to set
     */
    public void setSailTranApplyDAO(SailTranApplyDAO sailTranApplyDAO)
    {
        this.sailTranApplyDAO = sailTranApplyDAO;
    }

    /**
     * @return the customerManager
     */
    public CustomerManager getCustomerManager()
    {
        return customerManager;
    }

    /**
     * @param customerManager
     *            the customerManager to set
     */
    public void setCustomerManager(CustomerManager customerManager)
    {
        this.customerManager = customerManager;
    }
}
