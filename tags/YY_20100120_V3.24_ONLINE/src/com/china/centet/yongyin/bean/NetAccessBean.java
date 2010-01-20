/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;


/**
 * @author Administrator
 */
public class NetAccessBean implements Serializable
{
    private String smtp = "";

    private String user = "";

    private String password = "";

    private String displayName = "";

    private String fromUser = "";

    private String[] recivers = null;

    /**
     *
     */
    public NetAccessBean()
    {}

    /**
     * @return the smtp
     */
    public String getSmtp()
    {
        return smtp;
    }

    /**
     * @param smtp
     *            the smtp to set
     */
    public void setSmtp(String smtp)
    {
        this.smtp = smtp;
    }

    /**
     * @return the user
     */
    public String getUser()
    {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * @return the recivers
     */
    public String[] getRecivers()
    {
        return recivers;
    }

    /**
     * @param recivers
     *            the recivers to set
     */
    public void setRecivers(String[] recivers)
    {
        this.recivers = recivers;
    }

    /**
     * @return the fromUser
     */
    public String getFromUser()
    {
        return fromUser;
    }

    /**
     * @param fromUser
     *            the fromUser to set
     */
    public void setFromUser(String fromUser)
    {
        this.fromUser = fromUser;
    }

}
