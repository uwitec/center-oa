/**
 *
 */
package com.china.centet.yongyin.dao;


import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.ExchangeBean;
import com.china.centet.yongyin.vo.ExchangeBeanVO;


/**
 * @author Administrator
 */
public class ExchangeDAO extends BaseDAO2<ExchangeBean, ExchangeBeanVO>
{
    /**
     * 删除会员的消费记录
     * 
     * @param memberId
     * @return
     */
    public boolean delExchangesByMemberId(String memberId)
    {
        return this.jdbcOperation.delete(memberId, "memberId", this.claz) > 0;
    }
}
