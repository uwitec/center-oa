/**
 * File Name: MailBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.mail.bean;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * MailBean
 * 
 * @author zhuzhu
 * @version 2009-4-12
 * @see MailBean
 * @since 1.0
 */
@Entity(inherit = true)
@Table(name = "T_CENTER_MAIL")
public class MailBean extends AbstractMail
{
    @Id
    private String id = "";

    /**
     * default constructor
     */
    public MailBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }
}
