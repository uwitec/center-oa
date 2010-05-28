/*
 * FileName：UtilStream.java
 * CopyRight：Copyright by www.center.earth.com
 * Description：
 * Modifier：Admin
 * ModifyTime：2006-12-1
 */

package com.china.center.tools;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Stream Tools Class
 * 
 * @author ZHUZHU
 * @version 1.0
 * @see UtilStream
 * @Date 2006-12-1
 * @since
 */
public class UtilStream
{
    /**
     * buffer byte
     */
    private int buffer = 1024;

    /**
     * InputStream
     */
    private InputStream in = null;

    /**
     * OutputStream
     */
    private OutputStream out = null;

    /**
     * default construct
     */
    public UtilStream()
    {}

    public UtilStream(InputStream in, OutputStream out)
    {
        this.in = in;
        this.out = out;
    }

    /**
     * @param in
     *            InputStream
     * @param out
     *            OutputStream
     * @param buffer
     *            buffer byte
     */
    public UtilStream(InputStream in, OutputStream out, int buffer)
    {
        this.in = in;
        this.out = out;
        if (buffer > 0)
        {
            this.buffer = buffer;
        }
    }

    /**
     * copy inputstream to outputstream<br>
     * but the inputstream and outputstream will not be closed<br>
     */
    public void copyStream()
        throws IOException
    {
        int bytesRead = 0;
        byte[] buffers = new byte[buffer];
        while ( (bytesRead = in.read(buffers, 0, buffer)) != -1)
        {
            // write out
            out.write(buffers, 0, bytesRead);
            out.flush();
        }
    }

    /**
     * copy inputstream to outputstream<br>
     * but the inputstream and outputstream will not be closed<br>
     */
    public void copyStream(long length)
        throws IOException
    {
        int bytesRead = 0;
        int i = 0;
        while ( (bytesRead = in.read()) != -1)
        {
            // write out
            i++ ;
            out.write(bytesRead);
            out.flush();
            if (i >= length)
            {
                break;
            }
        }
    }

    /**
     * copy inputstream to outputstream<br>
     * and the inputstream and outputstream will be closed<br>
     */
    public void copyAndCloseStream()
        throws IOException
    {
        try
        {
            this.copyStream();
        }
        finally
        {
            this.close();
        }
    }

    /**
     * close inputstream and outputstream<br>
     */
    public void close()
        throws IOException
    {
        if (in != null)
        {
            in.close();
        }

        if (out != null)
        {
            out.close();
        }
    }

    /**
     * @return Returns the buffer.
     */
    public int getBuffer()
    {
        return buffer;
    }

    /**
     * @param buffer
     *            The buffer to set.
     */
    public void setBuffer(int buffer)
    {
        if (buffer > 0)
        {
            this.buffer = buffer;
        }
    }

    /**
     * @return Returns the in.
     */
    public InputStream getIn()
    {
        return in;
    }

    /**
     * @param in
     *            The in to set.
     */
    public void setIn(InputStream in)
    {
        this.in = in;
    }

    /**
     * @return Returns the out.
     */
    public OutputStream getOut()
    {
        return out;
    }

    /**
     * @param out
     *            The out to set.
     */
    public void setOut(OutputStream out)
    {
        this.out = out;
    }

}
