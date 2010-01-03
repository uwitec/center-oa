/**
 *
 */
package com.china.centet.yongyin.net;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.china.center.tools.StringTools;


/**
 * @author Administrator
 */
public class ParserHtml
{
    private String rootUrl = "";

    private String url = "";

    private Parser parserHtml = null;

    private boolean parser = false;

    /**
     *
     */
    public ParserHtml(String rootUrl, String url)
    {
        this.rootUrl = rootUrl;

        this.url = url;

        try
        {
            this.parserHtml = new Parser(url);

            parser = true;
        }
        catch (ParserException e)
        {
            parser = false;
        }
    }

    public String getTitle()
    {
        if ( !parser)
        {
            return "";
        }

        parserHtml.reset();

        NodeFilter filter = new NodeClassFilter(TitleTag.class);

        NodeList nodeList = null;

        Node[] nodes = null;

        try
        {
            nodeList = parserHtml.parse(filter);
        }
        catch (ParserException e)
        {
            return "";
        }

        nodes = nodeList.toNodeArray();

        for (int i = 0; i < nodes.length; i++ )
        {
            Node anode = (Node)nodes[i];

            TitleTag title = (TitleTag)anode;

            return title.getTitle();
        }

        return "";
    }

    /**
     *
     */
    public ParserHtml(String url)
    {
        this.url = url;

        try
        {
            this.parserHtml = new Parser(url);

            parser = true;
        }
        catch (ParserException e)
        {
            parser = false;
        }
    }

    public List<String> getCss()
    {
        if ( !parser)
        {
            return new ArrayList<String>();
        }

        parserHtml.reset();

        NodeFilter filter = new NodeClassFilter(HeadTag.class);

        NodeList nodeList = null;

        Node[] nodes = null;

        try
        {
            nodeList = parserHtml.parse(filter);
        }
        catch (ParserException e)
        {
            return new ArrayList<String>();
        }

        nodes = nodeList.toNodeArray();

        for (int i = 0; i < nodes.length; i++ )
        {
            Node anode = (Node)nodes[i];

            HeadTag head = (HeadTag)anode;

            return pxml("<head>" + head.getStringText() + "</head>");
        }

        return new ArrayList<String>();
    }

    public List<String> getScript()
    {
        List<String> result = new ArrayList<String>();

        if ( !parser)
        {
            return result;
        }

        parserHtml.reset();

        NodeFilter scriptFilter = new NodeClassFilter(ScriptTag.class);

        NodeList nodeList = null;

        Node[] nodes = null;

        try
        {
            nodeList = parserHtml.parse(scriptFilter);
        }
        catch (ParserException e)
        {
            return result;
        }

        nodes = nodeList.toNodeArray();

        for (int i = 0; i < nodes.length; i++ )
        {
            Node anode = (Node)nodes[i];

            ScriptTag script = (ScriptTag)anode;

            String webPath = script.getAttribute("src");

            if ( !StringTools.isNullOrNone(webPath))
            {
                if (webPath.startsWith("http://"))
                {
                    result.add(webPath);
                }
                else if (webPath.startsWith("/") && !webPath.startsWith("//"))
                {
                    result.add(webPath);
                }
            }
        }

        return result;
    }

    public List<String> getImg()
    {
        if ( !parser)
        {
            return new ArrayList<String>();
        }

        List<String> result = new ArrayList<String>();

        parserHtml.reset();

        NodeFilter imgFilter = new NodeClassFilter(ImageTag.class);

        NodeList nodeList = null;

        Node[] nodes = null;

        try
        {
            nodeList = parserHtml.parse(imgFilter);
        }
        catch (ParserException e)
        {
            return result;
        }

        nodes = nodeList.toNodeArray();

        for (int i = 0; i < nodes.length; i++ )
        {
            Node anode = (Node)nodes[i];

            ImageTag head = (ImageTag)anode;

            String webPath = head.getAttribute("src");

            if ( !StringTools.isNullOrNone(webPath))
            {
                if (webPath.startsWith("http://"))
                {
                    result.add(webPath);
                }
                else if (webPath.startsWith("/") && !webPath.startsWith("//"))
                {
                    result.add(webPath);
                }
            }
        }

        return result;
    }

    public List<String> getMeta()
    {
        if ( !parser)
        {
            return new ArrayList<String>();
        }

        List<String> result = new ArrayList<String>();

        parserHtml.reset();

        NodeFilter metaFilter = new NodeClassFilter(MetaTag.class);

        NodeList nodeList = null;

        Node[] nodes = null;

        try
        {
            nodeList = parserHtml.parse(metaFilter);
        }
        catch (ParserException e)
        {
            return result;
        }

        nodes = nodeList.toNodeArray();

        for (int i = 0; i < nodes.length; i++ )
        {
            Node anode = (Node)nodes[i];

            MetaTag meta = (MetaTag)anode;

            String content = meta.getAttribute("content");

            if (content != null && content.toLowerCase().indexOf("charset") != -1)
            {
                result.add(content);
            }
        }

        return result;
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

            List<Element> elist = element.elements("link");

            for (Element ele : elist)
            {
                String webPath = ele.attributeValue("href");

                if ( !StringTools.isNullOrNone(webPath))
                {
                    if (webPath.startsWith("http://"))
                    {
                        result.add(webPath);
                    }
                    else if (webPath.startsWith("/") && !webPath.startsWith("//"))
                    {
                        result.add(webPath);
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
     * @return the rootUrl
     */
    public String getRootUrl()
    {
        return rootUrl;
    }

    /**
     * @param rootUrl
     *            the rootUrl to set
     */
    public void setRootUrl(String rootUrl)
    {
        this.rootUrl = rootUrl;
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

    /**
     * @return the parser
     */
    public boolean isParser()
    {
        return parser;
    }

    /**
     * @param parser
     *            the parser to set
     */
    public void setParser(boolean parser)
    {
        this.parser = parser;
    }
}
