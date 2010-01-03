/*
 * 文件名：ProductionDAOImpl.java 版权：Copyright by www.centerchina.com 描述： 修改人：zhu
 * 修改时间：2006-7-16 跟踪单号： 修改单号： 修改内容：
 */

package com.china.centet.yongyin.dao;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.JdbcOperation;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.StringTools;
import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.ProductAmount;
import com.china.centet.yongyin.constant.Constant;


/**
 * 〈一句话功能简述〉
 * 
 * @author zhu
 * @version 2006-7-16
 * @see ProductDAO
 * @since
 */

public class ProductDAO
{
    private JdbcOperation jdbcOperation = null;

    private JdbcOperation jdbcOperation2 = null;

    private final Log logger1 = LogFactory.getLog("sec");

    public String check(String ids, String amont, int type, String locationId)
    {
        String[] ss1 = ids.split("~");
        String[] ss2 = amont.split("~");

        if (StringTools.isNullOrNone(locationId))
        {
            return "区域ID为空，不能操作!";
        }

        ProductAmount productAmount = null;
        for (int i = 0; i < ss1.length; i++ )
        {
            productAmount = findProductAmount(ss1[i], locationId);
            if (productAmount == null)
            {
                return "产品数量不存在,请联系管理员同步产品.";
            }

            if (type == 1)
            {
                if ( (productAmount.getNum() + Integer.parseInt(ss2[i])) < 0)
                {
                    return "警告:产品[" + productAmount.getProductName() + "]当前的库存只有:"
                           + productAmount.getNum() + ".只能保存,不能提交!";
                }
            }
            else
            {
                if (productAmount.getNum() < Integer.parseInt(ss2[i]))
                {
                    return "警告:产品[" + productAmount.getProductName() + "]当前的库存只有:"
                           + productAmount.getNum() + ".只能保存,不能提交!";
                }
            }
        }

        return "true";
    }

    public Product findProductById(final String id)
    {
        return jdbcOperation.find(id, Product.class);
    }

    public Product findProductByName(final String name)
    {
        return jdbcOperation.find(name, "name", Product.class);
    }

    public Product findProductByCode(final String code)
    {
        return jdbcOperation.find(code, "code", Product.class);
    }

    /**
     * 查询产品
     * 
     * @param condtion
     * @param needLimite
     *            是否需要限制
     * @return
     */
    public List<Product> queryProductByCondtion(ConditionParse condtion, boolean needLimite)
    {
        StringBuffer buffer = new StringBuffer();

        condtion.addWhereStr();

        buffer.append(condtion.toString());

        buffer.append(" order by id desc");

        int limite = 0;

        if (needLimite)
        {
            limite = Constant.PAGE_SIZE;
        }

        return jdbcOperation.queryObjects(buffer.toString(), Product.class).setMaxResults(limite).list(
            Product.class);
    }

    public int getTotalByCondition(ConditionParse condtion)
    {
        return jdbcOperation.queryObjects(condtion.toString(), Product.class).getCount();
    }

    /**
     * 数据库分页查询
     * 
     * @param condtion
     * @param begin
     * @param max
     * @return
     */
    public List<Product> queryProductByCondtion(ConditionParse condtion, int begin, int max)
    {
        condtion.addWhereStr();

        return jdbcOperation.queryObjects(condtion.toString() + " order by id desc", Product.class).setFirstResult(
            begin).setMaxResults(max).list(Product.class);
    }

    public List<Product> listAll()
    {
        return jdbcOperation.queryForList("where 1 = 1", Product.class);
    }

    /**
     * Description: 查询产品在区域下的数量
     * 
     * @param condtion
     * @return
     */
    public List<Product> queryProductByCondtion(ConditionParse condtion, PageSeparate page)
    {
        condtion.addWhereStr();
        return jdbcOperation.queryObjectsBySql(
            "select t1.* from t_center_product t1 " + condtion.toString()).setFirstResult(
            page.getSectionFoot()).setMaxResults(page.getPageSize()).list(Product.class);
    }

    /**
     * 直接统计产品的数量
     * 
     * @param condtion
     * @return
     */
    public int countProduct(ConditionParse condtion)
    {
        condtion.addWhereStr();
        return jdbcOperation.queryObjectsBySql(
            "select * from t_center_product " + condtion.toString()).getCount();
    }

    public List<Product> queryNotSynchronizedProductByLocationId(String locationId)
    {
        String sql = "select * from t_center_product where id not in "
                     + "(select productId from t_center_productnumber where locationId = ?)";

        return jdbcOperation.queryForListBySql(sql, Product.class, locationId);
    }

    /**
     * Description: 查询产品在区域下的数量
     * 
     * @param condtion
     * @return
     */
    public List<ProductAmount> queryProductAmountByCondtion(ConditionParse condtion)
    {
        StringBuffer buffer = new StringBuffer();

        getAmountSql(condtion, buffer);

        return jdbcOperation.queryObjectsBySql(buffer.toString()).setMaxResults(Constant.PAGE_SIZE).list(
            ProductAmount.class);
    }

    /**
     * Description: 查询产品在区域下的数量
     * 
     * @param condtion
     * @return
     */
    public List<ProductAmount> queryProductAmountByCondtionAndSeparate(ConditionParse condtion,
                                                                       PageSeparate page)
    {
        StringBuffer buffer = new StringBuffer();

        getAmountSql(condtion, buffer);

        return jdbcOperation.queryObjectsBySql(buffer.toString()).setFirstResult(
            page.getSectionFoot()).setMaxResults(page.getPageSize()).list(ProductAmount.class);
    }

    /**
     * Description: 查询产品在区域下的数量
     * 
     * @param condtion
     * @return
     */
    public int getCountByCondtion(ConditionParse condtion)
    {
        StringBuffer buffer = new StringBuffer();

        getAmountSql(condtion, buffer);

        return jdbcOperation.queryObjectsBySql(buffer.toString()).getCount();
    }

    /**
     * @param condtion
     * @param buffer
     */
    private void getAmountSql(ConditionParse condtion, StringBuffer buffer)
    {
        condtion.addWhereStr();

        // select t2.* from t_center_product t1, t_center_productnumber t2
        buffer.append("select t2.*, t1.code as productCode , t3.LOCATIONNAME as locationName from t_center_product t1, t_center_productnumber t2, t_center_location t3 ");

        buffer.append(condtion.toString());

        buffer.append(" and t1.id = t2.PRODUCTID and t2.locationId = t3.id order by t2.num desc");
    }

    public synchronized boolean modifyTatol(String productId, int total, String locationId)
    {
        String sql = "update t_center_productnumber set num = ? where PRODUCTID = ? and locationId = ?";

        int i = jdbcOperation2.update(sql, new Object[] {total, productId, locationId});

        return i != 0;
    }

    public boolean addProductAmount(ProductAmount pro)
    {
        return jdbcOperation.save(pro) > 0;
    }

    /**
     * 事务里面提交
     * 
     * @param pro
     * @return
     */
    public boolean addProductAmount2(ProductAmount pro)
    {
        return jdbcOperation2.save(pro) > 0;
    }

    public boolean delProduct(String id)
    {
        String sql = "delete from t_center_product where id = ?";

        jdbcOperation.update(sql, new Object[] {id});

        return true;
    }

    /**
     * 获得区域下产品的数量
     * 
     * @param productId
     * @param locationId
     * @return
     */
    public synchronized int getTotal(String productId, String locationId)
    {
        String sql1 = "select count(1) From t_center_productnumber where PRODUCTID = ? and locationId = ?";

        int count = jdbcOperation2.queryForInt(sql1, new Object[] {productId, locationId});

        if (count != 1)
        {
            logger1.error(productId + " and[" + locationId + "]组合不存在");

            throw new RuntimeException(productId + " and[" + locationId + "]组合不存在");
        }

        String sql = "select num From t_center_productnumber where PRODUCTID = ? and locationId = ?";

        return jdbcOperation2.queryForInt(sql, new Object[] {productId, locationId});
    }

    public synchronized boolean addProduct(Product product)
    {
        jdbcOperation.save(product);

        return true;
    }

    public synchronized boolean addProduct2(Product product)
    {
        jdbcOperation2.save(product);

        return true;
    }

    public boolean updateProduct(Product product)
    {
        jdbcOperation2.update(product);

        return true;
    }

    /**
     * 在base表里面更新产品名称
     * 
     * @param productId
     * @param productName
     * @return
     */
    public boolean updateProductInBase(String productId, String productName)
    {
        jdbcOperation2.update("update t_center_base set productName = ? where productId = ?",
            productName, productId);

        return true;
    }

    /**
     * 在base表里面更新产品名称
     * 
     * @param productId
     * @param productName
     * @return
     */
    public boolean updateProductInProductNum(String productId, String productName)
    {
        jdbcOperation2.update(
            "update t_center_productnumber set productName = ? where productId = ?", productName,
            productId);

        return true;
    }

    public int countProductByCondition(ConditionParse condtion)
    {
        condtion.addWhereStr();

        String sql = "select count(1) From t_center_product " + condtion.toString();

        return jdbcOperation.queryForInt(sql);
    }

    public int countProductInAmount(String productId, String locationId)
    {
        String sql = "select count(1) From t_center_product where productId = ? and locationId = ?";

        return jdbcOperation.queryForInt(sql, new Object[] {productId, locationId});
    }

    public ProductAmount findProductAmount(String productId, String locationId)
    {
        List<ProductAmount> list = jdbcOperation.queryForList(
            "where productId = ? and locationId = ?", ProductAmount.class, productId, locationId);

        if (list.size() > 0)
        {
            return list.get(0);
        }

        return null;
    }

    public int getNum(String userName, String code)
    {
        String sql = "select count(1) from t_center_product where name = ? or code = ?";

        return jdbcOperation.queryForInt(sql, new Object[] {userName.trim(), code.trim()});
    }

    /**
     * @return the jdbcOperation
     */
    public JdbcOperation getJdbcOperation()
    {
        return jdbcOperation;
    }

    /**
     * @param jdbcOperation
     *            the jdbcOperation to set
     */
    public void setJdbcOperation(JdbcOperation jdbcOperation)
    {
        this.jdbcOperation = jdbcOperation;
    }

    /**
     * @return the jdbcOperation2
     */
    public JdbcOperation getJdbcOperation2()
    {
        return jdbcOperation2;
    }

    /**
     * @param jdbcOperation2
     *            the jdbcOperation2 to set
     */
    public void setJdbcOperation2(JdbcOperation jdbcOperation2)
    {
        this.jdbcOperation2 = jdbcOperation2;
    }
}
