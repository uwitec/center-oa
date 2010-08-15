package com.china.center.struts.wrapinner;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.china.center.struts.wrap.ActionConfigManager;
import com.china.center.struts.wrap.ActionConfigWrap;
import com.china.center.struts.wrap.ForwardConfigWrap;
import com.china.center.struts.wrap.ResourceLocator;
import com.china.center.tools.StringTools;


/**
 * RegisterActionConfig
 * 
 * @author ZHUZHU
 * @version 2010-5-24
 * @see RegisterActionConfig
 * @since 1.0
 */
public class RegisterActionConfig implements ApplicationContextAware
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ActionConfigManager actionConfigManager = null;

    private ApplicationContext context = null;

    private String paths = "";

    private Set<ActionConfigWrap> actionConfigSet = new HashSet();

    public RegisterActionConfig()
    {
    }

    /**
     * @return ���� actionConfigManager
     */
    public ActionConfigManager getActionConfigManager()
    {
        return actionConfigManager;
    }

    public void destroy()
    {
        // ͨ��actionConfigManagerע�ᵽwp����
        for (ActionConfigWrap config : actionConfigSet)
        {
            actionConfigManager.removeActionConfig(config.getPath());
        }
    }

    /**
     * @param ��actionConfigManager���и�ֵ
     */
    public void setActionConfigManager(ActionConfigManager actionConfigManager)
    {
        this.actionConfigManager = actionConfigManager;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        context = applicationContext;

        // Process each specified resource path
        while (paths.length() > 0)
        {
            String path = null;
            int comma = paths.indexOf(',');
            if (comma >= 0)
            {
                path = paths.substring(0, comma).trim();
                paths = paths.substring(comma + 1);
            }
            else
            {
                path = paths.trim();
                paths = "";
            }

            if (path.length() < 1)
            {
                break;
            }

            readMemberFromXML(path);
        }

        // ͨ��actionConfigManagerע�ᵽwp����
        for (ActionConfigWrap config : actionConfigSet)
        {
            actionConfigManager.addActionConfig(config, context.getBean(config.getPath()));

            _logger.debug("addActionConfig:" + config.getPath());
        }
    }

    private void readMemberFromXML(String fileName)
    {
        InputStream in = null;

        try
        {
            in = ResourceLocator.getResource(fileName);

            SAXReader reader = new SAXReader();

            Document document = reader.read(in);

            Element root = document.getRootElement();

            Element element = (Element)root;

            Element mappingsElement = element.element("action-mappings");

            List<Element> actionElements = mappingsElement.elements("action");

            for (Element actionElement : actionElements)
            {
                ActionConfigWrap config = new ActionConfigWrap();

                config.setPath(actionElement.attributeValue("path"));

                config.setParameter(actionElement.attributeValue("parameter"));

                config.setType(actionElement.attributeValue("type"));

                config.setFolder(actionElement.attributeValue("folder"));

                config.setBean(actionElement.attributeValue("bean"));

                config.setValidate(Boolean.valueOf(actionElement.attributeValue("validate")));

                // ��������forward
                List<Element> forwardElements = actionElement.elements("forward");

                boolean hasError = false;
                boolean hasSuccess = false;
                for (Element forwardElement : forwardElements)
                {
                    ForwardConfigWrap forward = new ForwardConfigWrap();

                    forward.setName(forwardElement.attributeValue("name"));

                    String path = forwardElement.attributeValue("path");

                    if (StringTools.isNullOrNone(path))
                    {
                        forward.setPath(config.getFolder() + forward.getName() + ".jsp");
                    }
                    else
                    {
                        forward.setPath(forwardElement.attributeValue("path"));
                    }

                    forward.setRedirect(Boolean.valueOf(forwardElement.attributeValue("redirect")));

                    forward.setModule(forwardElement.attributeValue("module"));

                    config.addForwardConfig(forward);

                    if (forward.getName().equals("error"))
                    {
                        hasError = true;
                    }

                    if (forward.getName().equals("success"))
                    {
                        hasSuccess = true;
                    }
                }

                if ( !hasError)
                {
                    // 增加默认的成功和失败页面
                    ForwardConfigWrap forward = new ForwardConfigWrap();

                    forward.setName("error");

                    forward.setPath("/common/error.jsp");

                    forward.setRedirect(false);

                    config.addForwardConfig(forward);
                }

                if ( !hasSuccess)
                {
                    // 增加默认的成功和失败页面
                    ForwardConfigWrap forward = new ForwardConfigWrap();

                    forward.setName("success");

                    forward.setPath("/common/success.jsp");

                    forward.setRedirect(false);

                    config.addForwardConfig(forward);
                }

                if ( !StringTools.isNullOrNone(config.getBean()) && !StringTools.isNullOrNone(config.getFolder()))
                {
                    // 增加默认的 addBean updateBean queryBean detailBean
                    String[] ss = new String[] {"add", "update", "delete", "detail", "query"};

                    for (String each : ss)
                    {
                        if ( !contain(each + config.getBean(), config.getForwards()))
                        {
                            ForwardConfigWrap forward = new ForwardConfigWrap();

                            forward.setName(each + config.getBean());

                            forward.setPath(config.getFolder() + forward.getName() + ".jsp");

                            forward.setRedirect(false);

                            config.addForwardConfig(forward);
                        }
                    }
                }

                actionConfigSet.add(config);
            }
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    _logger.error(e, e);
                }
            }
        }
    }

    private boolean contain(String name, List<ForwardConfigWrap> list)
    {
        for (ForwardConfigWrap forwardConfigWrap : list)
        {
            if (forwardConfigWrap.getName().equals(name))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @return ���� paths
     */
    public String getPaths()
    {
        return paths;
    }

    /**
     * @param ��paths���и�ֵ
     */
    public void setPaths(String paths)
    {
        this.paths = paths;
    }
}
