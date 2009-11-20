/**
 * File Name: MailDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.mail.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.mail.bean.AttachmentBean;


/**
 * MailDAO
 * 
 * @author zhuzhu
 * @version 2009-4-12
 * @see AttachmentDAO
 * @since 1.0
 */
@Bean(name = "attachmentDAO")
public class AttachmentDAO extends BaseDAO2<AttachmentBean, AttachmentBean>
{

}
