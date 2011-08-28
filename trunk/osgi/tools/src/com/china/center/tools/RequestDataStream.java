/**
 *
 */
package com.china.center.tools;


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * request里面获得文件和值(存在对名称的bug需要改进)
 * 
 * @author Administrator
 */
public class RequestDataStream implements Serializable
{
    private HttpServletRequest request = null;

    private Map<String, String> parmterMap = new HashMap<String, String>();

    private Map<String, List<String>> parmtersMap = new HashMap<String, List<String>>();

    private Map<String, String> fileNameMap = new HashMap<String, String>();

    private Map<String, InputStream> streamMap = new HashMap<String, InputStream>();

    private boolean haveStream = false;

    private String encoding = "UTF-8";

    private long size = 0L;

    private static long MAX_SIZE = 1024 * 1024 * 10L;

    /**
     *
     */
    public RequestDataStream(HttpServletRequest request)
    {
        this.size = MAX_SIZE;

        this.request = request;
    }

    public RequestDataStream(HttpServletRequest request, long size)
    {
        this.size = size;
        this.request = request;
    }

    /**
     *
     */
    public RequestDataStream(HttpServletRequest request, String encoding)
    {
        this.request = request;
        this.encoding = encoding;
    }

    public String getFileName(String key)
    {
        return fileNameMap.get(key);
    }

    public InputStream getUniqueInputStream()
    {
        for (Map.Entry<String, InputStream> entry : streamMap.entrySet())
        {
            InputStream in = entry.getValue();

            if (in != null)
            {
                return in;
            }
        }

        return null;
    }

    public String getUniqueFileName()
    {
        for (Map.Entry<String, InputStream> entry : streamMap.entrySet())
        {
            InputStream in = entry.getValue();

            if (in != null)
            {
                return fileNameMap.get(entry.getKey());
            }
        }

        return null;
    }

    public boolean parser()
        throws Exception
    {
        FileItemFactory factory = new DiskFileItemFactory();

        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setSizeMax(size);

        List<FileItem> fileItems = upload.parseRequest(request);

        Iterator i = fileItems.iterator();

        try
        {
            while (i.hasNext())
            {
                FileItem fi = (FileItem)i.next();

                if (fi.isFormField())
                {
                    String value = StringTools
                        .getStringBySet(fi.getString(), "ISO8859-1", encoding);

                    if (parmtersMap.containsKey(fi.getFieldName()))
                    {
                        parmtersMap.get(fi.getFieldName()).add(value);
                    }
                    else
                    {
                        List<String> valueList = new ArrayList();

                        valueList.add(value);

                        parmtersMap.put(fi.getFieldName(), valueList);
                    }

                    parmterMap.put(fi.getFieldName(), value);
                }
                else
                {
                    if ( !StringTools.isNullOrNone(fi.getName()) && fi.getSize() > 0)
                    {
                        fileNameMap.put(fi.getFieldName(), fi.getName());

                        streamMap.put(fi.getFieldName(), fi.getInputStream());

                        haveStream = true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
        }

        return true;
    }

    public void close()
    {
        for (Map.Entry<String, InputStream> entry : streamMap.entrySet())
        {
            InputStream in = entry.getValue();

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

    /**
     * @return the request
     */
    public HttpServletRequest getRequest()
    {
        return request;
    }

    /**
     * @param request
     *            the request to set
     */
    public void setRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    /**
     * @return the parmterMap
     */
    public Map<String, String> getParmterMap()
    {
        return parmterMap;
    }

    public String getParameter(String key)
    {
        return parmterMap.get(key);
    }

    public List<String> getParameters(String key)
    {
        return parmtersMap.get(key);
    }

    /**
     * @return the streamMap
     */
    public Map<String, InputStream> getStreamMap()
    {
        return streamMap;
    }

    /**
     * @return the haveStream
     */
    public boolean haveStream()
    {
        return haveStream;
    }

    /**
     * @return the encoding
     */
    public String getEncoding()
    {
        return encoding;
    }

    /**
     * @param encoding
     *            the encoding to set
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    /**
     * @return the size
     */
    public long getSize()
    {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(long size)
    {
        this.size = size;
    }
}
