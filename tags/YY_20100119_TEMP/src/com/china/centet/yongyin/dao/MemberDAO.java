/*
 * File Name: BankDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.MemberBean;


/**
 * ÒøÐÐµÄdao
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class MemberDAO extends BaseDAO2<MemberBean, MemberBean>
{
    /**
     * default constructor
     */
    public MemberDAO()
    {}

    public MemberBean findMemberByCardNo(String cardNo)
    {
        return jdbcOperation2.find(cardNo, "cardNo", this.claz);
    }

    public boolean updatePoint(String id, int point)
    {
        return jdbcOperation2.update("update " + BeanTools.getTableName(this.claz)
                                     + " set point = ? where id = ?", point, id) > 0;
    }

    public boolean updateUserPoint(String id, int point)
    {
        return jdbcOperation2.update("update " + BeanTools.getTableName(this.claz)
                                     + " set usepoint = ? where id = ?", point, id) > 0;
    }

    public boolean updateGrate(String id, int grate)
    {
        return jdbcOperation2.update("update " + BeanTools.getTableName(this.claz)
                                     + " set grade = ? where id = ?", grate, id) > 0;
    }

    public boolean updateRebate(String id, double rebate)
    {
        return jdbcOperation2.update("update " + BeanTools.getTableName(this.claz)
                                     + " set rebate = ? where id = ?", rebate, id) > 0;
    }

    public int countByCardNo(String cardNo)
    {
        return super.countBycondition("where cardNo = ?", cardNo);
    }
}
