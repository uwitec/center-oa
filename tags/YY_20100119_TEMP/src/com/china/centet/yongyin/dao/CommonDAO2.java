package com.china.centet.yongyin.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.tools.SequenceTools;


/**
 * 必须是单立的类
 * 
 * @author zhu
 * @version 2006-7-16
 * @see CommonDAO2
 * @since
 */
@Bean(name = "commonDAO2")
public class CommonDAO2
{
    public String getSquenceString20()
    {
        return SequenceTools.getSequence(6);
    }
}
