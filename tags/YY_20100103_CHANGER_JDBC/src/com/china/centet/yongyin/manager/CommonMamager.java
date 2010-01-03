/**
 *
 */
package com.china.centet.yongyin.manager;


import java.net.InetAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.sourceforge.sannotations.annotation.Bean;
import net.sourceforge.sannotations.annotation.Property;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.tools.DecSecurity;
import com.china.center.tools.TimeTools;


/**
 * @author Administrator
 */
@Bean(name = "commonMamager", initMethod = "init")
public class CommonMamager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private String localIP = "";

    private boolean pass = false;

    @Property(value = "${licence}")
    private String licence = "";

    private String dyKey = "";

    private boolean inits = true;

    public void init()
    {
        if (1 == 1 * 1)
        {
            this.pass = true;

            return;
        }

        InetAddress addr = null;

        try
        {
            dyKey = DecSecurity.encrypt(TimeTools.now());

            addr = InetAddress.getLocalHost();

            localIP = addr.getHostAddress().toString();

            String licenceKey = "";
            if (licence.length() < 6)
            {
                licenceKey = "";
            }
            else
            {
                licenceKey = licence.substring(3, licence.length() - 3);
            }

            String bip = DecSecurity.decrypt(licenceKey);

            if (localIP.equals(bip))
            {
                if (inits)
                {
                    System.out.println("服务绑定IP:" + bip);
                }

                this.pass = true;
            }
            else
            {
                if (inits)
                {
                    System.out.println("系统致命错误:服务需要绑定IP:" + bip + ".但服务IP是:" + localIP
                                       + ".请申请正版授权[zhu.000@163.com]");
                }

                _logger.fatal("服务需要绑定IP:" + bip + ".但服务IP是:" + localIP
                              + ".请申请正版授权[zhu.000@163.com]");

                this.pass = false;
            }
        }
        catch (Exception e)
        {
            _logger.error(e, e);

            System.out.println("无法获得本机IP，无法启动服务");
        }

        if (inits)
        {
            this.inits = false;

            Timer timer = new Timer("ip");

            timer.schedule(new Task(this), new Date(), 100 * 1000);
        }
    }

    class Task extends TimerTask
    {
        private CommonMamager manager = null;

        public Task(CommonMamager manager)
        {
            this.manager = manager;
        }

        public void run()
        {
            this.manager.init();
        }
    }

    public String dy()
    {
        return this.dyKey;
    }

    /**
     * @return the localIP
     */
    public String getLocalIP()
    {
        return localIP;
    }

    /**
     * @param localIP
     *            the localIP to set
     */
    public void setLocalIP(String localIP)
    {
        this.localIP = localIP;
    }

    /**
     * @return the pass
     */
    public boolean isPass()
    {
        return pass;
    }

    /**
     * @return the licence
     */
    public String getLicence()
    {
        return licence;
    }

    /**
     * @param licence
     *            the licence to set
     */
    public void setLicence(String licence)
    {
        this.licence = licence;
    }
}
