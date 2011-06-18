package com.china.center.struts.wrap;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ModuleConfig;

import com.china.center.webplugin.inter.impl.DelegatingRequestProcessor;


/**
 * RegisterConfigImpl
 * 
 * @author ZHUZHU
 * @version [版本号, 2009-7-25]
 * @see [相关类/方法]
 * @since 1.0
 */
public class ActionConfigManagerImpl implements ActionConfigManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private static final Object LOCK = new Object();

    /**
     * @param config
     * @param server
     */
    public void addActionConfig(ActionConfigWrap config, Object server)
    {
        synchronized (ActionConfigManagerImpl.LOCK)
        {
            ModuleConfig moduleConfig = getModuleConfig();

            if (moduleConfig != null)
            {
                ActionMapping sconfig = new ActionMapping();

                copyConfig(config, sconfig);

                moduleConfig.addActionConfig(sconfig);

                DelegatingRequestProcessor.extContext.put(config.getPath(), server);

                _logger.info("begin register ActionConfig:" + config);
            }
            else
            {
                synchronized (WrapActionServlet.LOCK)
                {
                    try
                    {
                        WrapActionServlet.LOCK.wait();
                    }
                    catch (InterruptedException e)
                    {
                        _logger.error(e, e);
                    }
                }

                addActionConfig(config, server);
            }
        }
    }

    private void copyConfig(ActionConfigWrap config, ActionConfig sconfig)
    {
        sconfig.setPath(config.getPath());

        sconfig.setParameter(config.getParameter());

        sconfig.setType(config.getType());

        sconfig.setValidate(config.isValidate());

        List<ForwardConfigWrap> forwards = config.getForwards();

        for (ForwardConfigWrap forwardConfigWrap : forwards)
        {
            ActionForward fc = new ActionForward();

            fc.setName(forwardConfigWrap.getName());
            fc.setPath(forwardConfigWrap.getPath());
            fc.setRedirect(forwardConfigWrap.isRedirect());
            fc.setModule(forwardConfigWrap.getModule());

            sconfig.addForwardConfig(fc);
        }
    }

    /**
     * @param path
     */
    public void removeActionConfig(String path)
    {
        synchronized (ActionConfigManagerImpl.LOCK)
        {
            ActionConfig config = getModuleConfig().findActionConfig(path);

            if (config != null)
            {
                getModuleConfig().removeActionConfig(config);

                _logger.info("remove ActionConfig:" + config.getPath());
            }

            DelegatingRequestProcessor.extContext.remove(path);
        }
    }

    public ModuleConfig getModuleConfig()
    {
        return WrapActionServlet.getStaticModuleConfig();
    }

}
