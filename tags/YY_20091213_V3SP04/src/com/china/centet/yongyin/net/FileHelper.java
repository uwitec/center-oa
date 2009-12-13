/**
 *
 */
package com.china.centet.yongyin.net;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.china.center.fileWriter.WriteFile;
import com.china.center.fileWriter.WriteFileFactory;
import com.china.center.tools.FileTools;

import com.china.center.tools.UtilStream;


/**
 * @author Administrator
 */
public class FileHelper
{
    private String root = "";

    private boolean debug = false;

    /**
     *
     */
    public FileHelper(String root)
    {
        this.setRoot(root);
    }

    public boolean clear()
    {
        try
        {
            FileTools.delete(this.root);

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public FileHelper(String root, boolean debug)
    {
        this.setRoot(root);

        this.debug = debug;
    }

    public void saveStreamToFile(InputStream in, String filePath)
        throws IOException
    {
        File file = FileTools.createFile(root + filePath);

        saveStreamToFile(in, file);
    }

    public void saveStreamToFile(InputStream in, File file)
        throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);

        UtilStream us = new UtilStream(in, out);

        us.copyAndCloseStream();

        wiriteLog("已经下载:" + file.getAbsolutePath());
    }

    private void wiriteLog(String log)
    {
        if (this.debug)
        {
            System.out.println(log);
        }
    }

    public void saveStreamToFile(GetMethod httpget, String filePath)
        throws IOException
    {
        saveStreamToFile(httpget.getResponseBodyAsStream(), filePath);
    }

    public void saveStreamToFile(GetMethod httpget, File file)
        throws IOException
    {
        saveStreamToFile(httpget.getResponseBodyAsStream(), file);
    }

    public String saveStreamToAutoFile(GetMethod httpget, String filePath)
        throws IOException
    {
        // Content-Type
        Header[] headers = httpget.getResponseHeaders();

        String type = "";

        for (Header header : headers)
        {
            if (header.getName().equalsIgnoreCase("Content-Type"))
            {
                type = header.getValue();

                break;
            }
        }

        type = type.substring(type.indexOf("/") + 1);

        String fileP = filePath + '.' + type;

        File file = FileTools.createFile(root + fileP);

        FileOutputStream out = new FileOutputStream(file);

        UtilStream us = new UtilStream(httpget.getResponseBodyAsStream(), out);

        us.copyAndCloseStream();

        wiriteLog("已经下载:" + file.getAbsolutePath());

        return fileP;
    }

    public void saveStringToFile(String content, String filePath)
        throws IOException
    {
        WriteFile write = WriteFileFactory.getMyTXTWriter();

        write.openFile(this.root + filePath);

        write.writeLine(content);

        write.close();

        wiriteLog("已经下载:" + this.root + filePath);
    }

    public void saveStringToFile(String content, File file)
        throws IOException
    {
        WriteFile write = WriteFileFactory.getMyTXTWriter();

        write.openFile(file);

        write.writeLine(content);

        write.close();

        wiriteLog("已经下载:" + file.getAbsolutePath());
    }

    public void saveUrlToFile(String url, String filePath)
        throws IOException
    {
        HttpClient httpclient = new HttpClient();

        GetMethod httpget = new GetMethod(url);

        httpclient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler());

        httpclient.executeMethod(httpget);

        saveStreamToFile(httpget, filePath);
    }

    public String saveUrlToAutoFile(String url, String filePath)
        throws IOException
    {
        HttpClient httpclient = new HttpClient();

        GetMethod httpget = new GetMethod(url);

        httpclient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler());

        httpclient.executeMethod(httpget);

        return saveStreamToAutoFile(httpget, filePath);
    }

    /**
     * @return the root
     */
    public String getRoot()
    {
        return root;
    }

    /**
     * @param root
     *            the root to set
     */
    public void setRoot(String root)
    {
        root = root.replaceAll("\\\\", "/");

        root = root.endsWith("/") ? root : root + '/';

        FileTools.createFolders(root);

        this.root = root;
    }

    /**
     * @return the debug
     */
    public boolean isDebug()
    {
        return debug;
    }

    /**
     * @param debug
     *            the debug to set
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }
}
