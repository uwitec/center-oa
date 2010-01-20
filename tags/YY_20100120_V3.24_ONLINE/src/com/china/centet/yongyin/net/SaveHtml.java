/**
 *
 */
package com.china.centet.yongyin.net;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;

import com.china.center.tools.Zips;


/**
 * ±£´æÍøÒ³
 * 
 * @author Administrator
 */
public class SaveHtml
{
    private String rootUrl = "";

    private String url = "";

    private String localRoot = "";

    private String title = "";

    /**
     *
     */
    public SaveHtml(String url, String rootUrl, String localRoot)
    {
        this.url = url;

        this.rootUrl = rootUrl;

        this.localRoot = localRoot;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    public boolean save()
    {
        ParserHtml parser = new ParserHtml(this.url);

        if ( !parser.isParser())
        {
            return false;
        }

        this.title = parser.getTitle();

        List<String> resouceUrl = null;

        Map<String, String> urlMap = new HashMap<String, String>();

        FileHelper helper = new FileHelper(this.localRoot);

        helper.clear();

        resouceUrl = parser.getMeta();

        for (String string : resouceUrl)
        {
            if (string.indexOf("charset") != -1)
            {
                urlMap.put(string, "text/html; charset=GBK");
            }
        }

        resouceUrl = parser.getCss();

        for (int i = 0; i < resouceUrl.size(); i++ )
        {
            String webPath = resouceUrl.get(i);

            String fileLocal = "css/" + i + ".css";

            if (webPath.startsWith("http://"))
            {
                saveUrlToFile(helper, webPath, fileLocal);

                urlMap.put(webPath, fileLocal);
            }
            else if (webPath.startsWith("/"))
            {
                saveUrlToFile(helper, this.rootUrl + webPath, fileLocal);

                urlMap.put(webPath, fileLocal);
            }
        }

        resouceUrl = parser.getScript();

        for (int i = 0; i < resouceUrl.size(); i++ )
        {
            String webPath = resouceUrl.get(i);

            String fileLocal = "script/" + i + ".js";

            if (webPath.startsWith("http://"))
            {
                saveUrlToFile(helper, webPath, fileLocal);

                urlMap.put(webPath, fileLocal);
            }
            else if (webPath.startsWith("/"))
            {
                saveUrlToFile(helper, this.rootUrl + webPath, fileLocal);

                urlMap.put(webPath, fileLocal);
            }
        }

        resouceUrl = parser.getImg();

        for (int i = 0; i < resouceUrl.size(); i++ )
        {
            String webPath = resouceUrl.get(i);

            String fileLocal = "img/" + i;

            if (webPath.startsWith("http://"))
            {
                fileLocal = saveUrlToAutoFile(helper, webPath, fileLocal);

                urlMap.put(webPath, fileLocal);
            }
            else if (webPath.startsWith("/"))
            {
                fileLocal = saveUrlToAutoFile(helper, this.rootUrl + webPath, fileLocal);

                urlMap.put(webPath, fileLocal);
            }
        }

        try
        {
            saveHtmlInner(urlMap, helper);
        }
        catch (HttpException e)
        {
            return false;
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

    /**
     * @param urlMap
     * @param helper
     * @throws IOException
     * @throws HttpException
     * @throws FileNotFoundException
     */
    private void saveHtmlInner(Map<String, String> urlMap, FileHelper helper)
        throws IOException, HttpException, FileNotFoundException
    {
        HttpClient httpclient = new HttpClient();

        GetMethod httpget = new GetMethod(this.url);

        httpclient.getParams().setParameter("http.protocol.content-charset", "GB2312");

        httpclient.executeMethod(httpget);

        String html = httpget.getResponseBodyAsString();

        for (Map.Entry<String, String> entry : urlMap.entrySet())
        {
            html = StringUtils.replace(html, entry.getKey(), entry.getValue());
        }

        helper.saveStringToFile(html, "index.html");
    }

    public boolean zip(File file)
    {
        try
        {
            Zips.zipFloder(this.localRoot, new FileOutputStream(file));
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

    public boolean zip(String path)
    {
        return zip(new File(path));
    }

    private boolean saveUrlToFile(FileHelper helper, String webPath, String replaceUrl)
    {
        try
        {
            helper.saveUrlToFile(webPath, replaceUrl);
        }
        catch (IOException e)
        {
            return false;
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    private String saveUrlToAutoFile(FileHelper helper, String webPath, String replaceUrl)
    {
        try
        {
            return helper.saveUrlToAutoFile(webPath, replaceUrl);
        }
        catch (IOException e)
        {
            return "";
        }
        catch (Exception e)
        {
            return "";
        }
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
     * @return the localRoot
     */
    public String getLocalRoot()
    {
        return localRoot;
    }

    /**
     * @param localRoot
     *            the localRoot to set
     */
    public void setLocalRoot(String localRoot)
    {
        this.localRoot = localRoot;
    }
}
