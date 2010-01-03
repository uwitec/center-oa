/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;
import com.china.center.annotation.Unique;


/**
 * ÍøÕ¾bean
 * 
 * @author Administrator
 */
@Entity(name = "ÍøÕ¾", cache = true)
@Table(name = "T_CENTER_PRICEWEB")
public class PriceWebBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Unique
    @Html(title = "ÍøÕ¾Ãû³Æ", must = true, maxLength = 200)
    private String name = "";

    @Html(title = "ÍøÕ¾µØÖ·", must = true, maxLength = 200)
    private String url = "";

    /**
     *
     */
    public PriceWebBean()
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

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
}
