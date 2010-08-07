/**
 * 
 */
package com.china.center.struts.wrap;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

/**
 * WrapActionServlet
 * 
 * @author ZHUZHU
 * @version 2009-7-25
 * @see WrapActionServlet
 * @since 1.0
 */
public class WrapActionServlet extends ActionServlet
{
    protected static ModuleConfig staticModuleConfig = null;
    
    public static final Object LOCK = new Object();
    
    /**
     * <p>
     * Initialize this servlet. Most of the processing has been factored into
     * support methods so that you can override particular functionality at a
     * fairly granular level.
     * </p>
     * 
     * @exception ServletException
     *                if we cannot configure ourselves correctly
     */
    public void init() throws ServletException
    {
        
        // Wraps the entire initialization in a try/catch to better handle
        // unexpected exceptions and errors to provide better feedback
        // to the developer
        try
        {
            initInternal();
            initOther();
            initServlet();
            
            getServletContext().setAttribute(Globals.ACTION_SERVLET_KEY, this);
            initModuleConfigFactory();
            // Initialize modules as needed
            ModuleConfig moduleConfig = initModuleConfig("", config);
            initModuleMessageResources(moduleConfig);
            initModuleDataSources(moduleConfig);
            initModulePlugIns(moduleConfig);
            
            // ��̬ע��ActionConfig,���ܶ���
            // moduleConfig.freeze();
            
            Enumeration names = getServletConfig().getInitParameterNames();
            while (names.hasMoreElements())
            {
                String name = (String) names.nextElement();
                if (!name.startsWith("config/"))
                {
                    continue;
                }
                String prefix = name.substring(6);
                moduleConfig = initModuleConfig(prefix,
                        getServletConfig().getInitParameter(name));
                initModuleMessageResources(moduleConfig);
                initModuleDataSources(moduleConfig);
                initModulePlugIns(moduleConfig);
                //moduleConfig.freeze();
            }
            
            setStaticModuleConfig(moduleConfig);
            
            this.initModulePrefixes(this.getServletContext());
            
            this.destroyConfigDigester();
        }
        catch (UnavailableException ex)
        {
            throw ex;
        }
        catch (Throwable t)
        {
            
            // The follow error message is not retrieved from internal message
            // resources as they may not have been able to have been
            // initialized
            log.error("Unable to initialize Struts ActionServlet due to an "
                    + "unexpected exception or error thrown, so marking the "
                    + "servlet as unavailable.  Most likely, this is due to an "
                    + "incorrect or missing library dependency.",
                    t);
            throw new UnavailableException(t.getMessage());
        }
    }
    
    /**
     * @return ���� staticModuleConfig
     */
    public static ModuleConfig getStaticModuleConfig()
    {
        return staticModuleConfig;
    }
    
    /**
     * @param ��staticModuleConfig���и�ֵ
     */
    public static void setStaticModuleConfig(ModuleConfig staticModuleConfig)
    {
        WrapActionServlet.staticModuleConfig = staticModuleConfig;
        
        try
        {
            synchronized (LOCK)
            {
                LOCK.notifyAll();
            }
        }
        catch (Throwable e)
        {
        }
    }
}
