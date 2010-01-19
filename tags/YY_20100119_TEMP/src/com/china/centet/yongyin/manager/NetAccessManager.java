/**
 *
 */
package com.china.centet.yongyin.manager;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.china.center.tools.StringTools;
import com.china.centet.yongyin.bean.NetAccessBean;
import com.china.centet.yongyin.net.SaveHtml;
import com.china.centet.yongyin.net.SendMail;


/**
 * 访问网络获取文件
 * 
 * @author Administrator
 */
public class NetAccessManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private EhcacheManager ehcacheManager = null;

    private NetAccessBean accessBean = new NetAccessBean();

    private String ip = "";

    public void access()
        throws ParserException
    {
        Parser parserHtml = new Parser("http://www.javaeye.com/");

        NodeFilter filter = new NodeClassFilter(Div.class);

        NodeList nodeList = null;

        Node[] nodes = null;

        nodeList = parserHtml.parse(filter);

        nodes = nodeList.toNodeArray();

        int divIndex = -1;

        for (int i = 0; i < nodes.length; i++ )
        {
            Div anode = (Div)nodes[i];

            if ("advert_a4".equalsIgnoreCase(anode.getAttribute("id")))
            {
                divIndex = i + 2;
            }

            if ("news".equalsIgnoreCase(anode.getAttribute("id")))
            {
                coreProcess(anode);
            }

            // 处理java热点新闻的
            if (i == divIndex)
            {
                coreProcess(anode);
            }
        }
    }

    /**
     * @param anode
     */
    private void coreProcess(Div anode)
    {
        String divOuterHtml = "<div>" + anode.getChildrenHTML() + "</div>";

        List<String> links = pxml(divOuterHtml);

        for (String string : links)
        {
            if (ehcacheManager.getCache_local().get(string) == null)
            {
                saveHtmlAndSend(string);

                net.sf.ehcache.Element ele = new net.sf.ehcache.Element(string, string);

                ehcacheManager.getCache_local().put(ele);

                ehcacheManager.getCache_local().flush();

                _logger.info(string + " add to cache");
            }
            else
            {
                ehcacheManager.getCache_local().flush();
            }
        }
    }

    /**
     * @return the accessBean
     */
    public NetAccessBean getAccessBean()
    {
        return accessBean;
    }

    public void init()
    {
        // 获取accessbeam
        Object oo = ehcacheManager.getCache_local().get("NetAccessBean_CENTER");

        if (oo == null)
        {
            this.accessBean = initAccessBean();
        }
        else
        {
            net.sf.ehcache.Element eleNet = (net.sf.ehcache.Element)oo;

            this.accessBean = (NetAccessBean)eleNet.getValue();

            net.sf.ehcache.Element ele = new net.sf.ehcache.Element("NetAccessBean_CENTER",
                this.accessBean);

            ehcacheManager.getCache_local().put(ele);

            ehcacheManager.getCache_local().flush();
        }

        InetAddress addr = null;

        try
        {
            addr = InetAddress.getLocalHost();

            this.ip = addr.getHostAddress().toString();
        }
        catch (UnknownHostException e)
        {
            System.out.println("unknownHost in this machince");
        }

    }

    public boolean clear()
    {
        List<String> keys = ehcacheManager.getCache_local().getKeys();

        for (String string : keys)
        {
            if (string.toLowerCase().startsWith("http://"))
            {
                ehcacheManager.getCache_local().remove(string);

                _logger.info("clear url:" + string);

                ehcacheManager.getCache_local().flush();
            }
        }

        return true;
    }

    public boolean updateNetAccessBean(NetAccessBean bean)
    {
        if (accessBean != null && accessBean.getRecivers() != null)
        {
            this.accessBean = bean;
        }

        net.sf.ehcache.Element ele = new net.sf.ehcache.Element("NetAccessBean_CENTER",
            this.accessBean);

        ehcacheManager.getCache_local().put(ele);

        ehcacheManager.getCache_local().flush();

        return true;
    }

    private void saveHtmlAndSend(String url)
    {
        try
        {
            SaveHtml save = new SaveHtml(url, "http://www.javaeye.com/", "c:/Tomcat 5.5/papa/");

            save.save();

            String zipFile = "c:/Tomcat 5.5/news.zip";

            save.zip(zipFile);

            SendMail mail = null;

            if (accessBean == null || accessBean.getRecivers() == null)
            {
                this.accessBean = initAccessBean();
            }

            mail = new SendMail(accessBean.getSmtp(), accessBean.getFromUser(),
                accessBean.getDisplayName(), accessBean.getUser(), accessBean.getPassword(),
                accessBean.getRecivers(), save.getTitle(), "欢迎订阅java新闻:" + this.ip);

            mail.addAttachfile(zipFile);

            mail.send();

            _logger.info("发送邮件成功:" + save.getTitle());
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }
    }

    /**
     *
     */
    private NetAccessBean initAccessBean()
    {
        NetAccessBean accessBean = new NetAccessBean();

        accessBean.setSmtp("smtp.163.com");

        accessBean.setFromUser("mac-csd@163.com");

        accessBean.setDisplayName("zhuzhu");

        accessBean.setUser("mac-csd");

        accessBean.setPassword("123456789q~");

        accessBean.setRecivers(new String[] {"z.h@centerchina.com"});

        return accessBean;
    }

    private List<String> pxml(String xml)
    {
        List<String> result = new ArrayList<String>();

        Reader readers = new StringReader(xml);
        try
        {
            SAXReader reader = new SAXReader();
            Document document = reader.read(readers);
            Element root = document.getRootElement();

            Element element = (Element)root;

            Element elem = element.element("ul");

            List<Element> elist = elem.elements("li");

            for (Element ele : elist)
            {
                Element ex = ele.element("a");

                String webPath = ex.attributeValue("href");

                if ( !StringTools.isNullOrNone(webPath))
                {
                    if (webPath.startsWith("http://"))
                    {
                        result.add(webPath);
                    }
                    else if (webPath.startsWith("/"))
                    {
                        result.add("http://www.javaeye.com" + webPath);
                    }
                }
            }
        }
        catch (DocumentException e)
        {

        }
        finally
        {
            try
            {
                if (readers != null)
                {
                    readers.close();
                }
            }
            catch (IOException e)
            {}
        }

        return result;
    }

    /**
     * @return the ehcacheManager
     */
    public EhcacheManager getEhcacheManager()
    {
        return ehcacheManager;
    }

    /**
     * @param ehcacheManager
     *            the ehcacheManager to set
     */
    public void setEhcacheManager(EhcacheManager ehcacheManager)
    {
        this.ehcacheManager = ehcacheManager;
    }
}
