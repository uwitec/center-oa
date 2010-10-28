/**
 * File Name: QueryConfigImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-8<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.query;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.china.center.tools.CommonTools;
import com.china.center.tools.ResourceLocator;
import com.china.center.tools.StringTools;


/**
 * QueryConfigImpl
 * 
 * @author ZHUZHU
 * @version 2008-11-8
 * @see DefaultQueryConfigImpl
 * @since 1.0
 */
public class DefaultQueryConfigImpl implements QueryConfig
{
    private Map<String, QueryItemBean> configMap = new HashMap<String, QueryItemBean>();

    private String configXMLPath = "";

    /**
     * init
     * 
     * @throws FileNotFoundException
     */
    public void init()
        throws FileNotFoundException
    {
        String[] splits = configXMLPath.split(",");

        for (String string : splits)
        {
            if ( !StringTools.isNullOrNone(string))
            {
                System.out.println("loading query XML:" + string.trim());

                InputStream in = ResourceLocator.getResource(string.trim());

                parserXML(in);
            }
        }

        QueryConfigResource.getConfigMap().putAll(configMap);

    }

    /**
     * destory
     */
    public void destory()
    {
        Set<String> keySet = configMap.keySet();

        for (String string : keySet)
        {
            QueryConfigResource.getConfigMap().remove(string);
        }
    }

    /**
     * parserXML
     * 
     * @param in
     */
    private void parserXML(InputStream in)
    {
        try
        {
            SAXReader reader = new SAXReader();

            Document document = reader.read(in);

            Element root = document.getRootElement();

            Element element = (Element)root;

            List<Element> elements = element.elements("item");

            for (Element eachItem : elements)
            {
                QueryItemBean item = new QueryItemBean();

                String name = eachItem.attribute("name").getText();

                item.setName(name);

                configMap.put(name, item);

                Attribute attribute = eachItem.attribute("alias");

                if (attribute != null)
                {
                    String alias = attribute.getText();

                    if ( !StringTools.isNullOrNone(alias))
                    {
                        QueryItemBean queryItemBean = configMap.get(alias);

                        QueryItemBean deepCopy = new QueryItemBean(queryItemBean);

                        deepCopy.setName(name);

                        configMap.put(name, deepCopy);

                        continue;
                    }
                }

                String deaultpfix = eachItem.attribute("deaultpfix").getText();

                item.setDeaultpfix(deaultpfix);

                List<Element> cons = eachItem.elements("condtion");

                List<QueryConditionBean> qconList = new ArrayList<QueryConditionBean>();

                item.setConditions(qconList);

                for (Element qItem : cons)
                {
                    QueryConditionBean condition = new QueryConditionBean();

                    qconList.add(condition);

                    String cname = getAttString(qItem, "name");

                    condition.setName(cname);

                    String filed = getAttString(qItem, "filed");

                    if (StringTools.isNullOrNone(filed))
                    {
                        condition.setFiled(cname);
                    }
                    else
                    {
                        condition.setFiled(filed);
                    }

                    String pfix = getAttString(qItem, "pfix");

                    if (StringTools.isNullOrNone(pfix))
                    {
                        condition.setPfix(item.getDeaultpfix());
                    }
                    else
                    {
                        condition.setPfix(pfix);
                    }

                    String caption = getAttString(qItem, "caption");

                    condition.setCaption(caption);

                    String opr = qItem.elementTextTrim("opr");

                    condition.setOpr(opr);

                    String type = qItem.elementTextTrim("type");

                    condition.setType(type);

                    String inner = qItem.elementTextTrim("inner");

                    condition.setInner(inner);

                    String datatype = qItem.elementTextTrim("datatype");

                    condition.setDatatype(CommonTools.parseInt(datatype));

                    String option = qItem.elementTextTrim("option");

                    if (StringTools.isNullOrNone(option))
                    {
                        condition.setOption(condition.getName());
                    }
                    else
                    {
                        condition.setOption(option);
                    }
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                }
            }
        }
    }

    private String getAttString(Element ele, String name)
    {
        Attribute att = ele.attribute(name);

        if (att == null)
        {
            return "";
        }

        return att.getText();
    }

    /**
     * findQueryCondition
     * 
     * @param name
     * @return
     */
    public QueryItemBean findQueryCondition(String name)
    {
        return configMap.get(name);
    }

    /**
     * listQueryCondition
     * 
     * @return
     */
    public Collection<QueryItemBean> listQueryCondition()
    {
        return configMap.values();
    }

    /**
     * @return the configXMLPath
     */
    public String getConfigXMLPath()
    {
        return configXMLPath;
    }

    /**
     * @param configXMLPath
     *            the configXMLPath to set
     */
    public void setConfigXMLPath(String configXMLPath)
    {
        this.configXMLPath = configXMLPath;
    }
}
