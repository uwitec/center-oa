package com.center.china.osgi.publics.file.writer;

public class WriteFileFactory
{
    /**
     * default constructor
     */
    private WriteFileFactory()
    {}

    public static WriteFile getMyTXTWriter()
    {
        return new MyTXTWriteFile();
    }

    public static WriteFile getMyTXTWriter(String charset)
    {
        return new MyTXTWriteFile(charset);
    }

    public static WriteFile getMyWordWriter()
    {
        return new MyWordWriteFile();
    }

    public static WriteFile getMyWordWriter(String charset)
    {
        return new MyWordWriteFile(charset);
    }

}
