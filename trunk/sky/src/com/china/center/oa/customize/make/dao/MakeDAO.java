/**
 * File Name: MakeDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.common.ConditionParse;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customize.make.bean.MakeBean;
import com.china.center.oa.customize.make.vo.MakeVO;
import com.china.center.oa.customize.make.wrap.MakeStatWrap;


/**
 * MakeDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-8
 * @see MakeDAO
 * @since 1.0
 */
@Bean(name = "makeDAO")
public class MakeDAO extends BaseDAO2<MakeBean, MakeVO>
{
    public List<MakeStatWrap> queryStatMake(ConditionParse condition)
    {
        StringBuffer sb = new StringBuffer();

        String sql = "select count(1) as amount , t.endType, t.position from t_center_make t ";

        sb.append(sql);

        sb.append(condition.toString());

        sb.append(" group by t.endType, t.position");

        final List<MakeStatWrap> result = new ArrayList();

        this.jdbcOperation.query(sb.toString(), new RowCallbackHandler()
        {
            public void processRow(ResultSet rs)
                throws SQLException
            {
                MakeStatWrap wrap = new MakeStatWrap();
                wrap.setAmount(rs.getInt("amount"));
                wrap.setEndType(rs.getInt("endType"));
                wrap.setPosition(rs.getInt("position"));

                result.add(wrap);
            }
        });

        return result;
    }
}
