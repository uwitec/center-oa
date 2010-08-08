/*
 * File Name: MyWORDReadFile.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-1-19
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.file.read;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


/**
 * use jxl or POI read XLS(default use jxl)
 * 
 * @author zhuzhu
 * @version 2007-1-19
 * @see
 * @since
 */
public class MyXLSReader extends AbstractReaderFile
{
    private int sheet = 0;

    private Workbook workbook = null;

    private Sheet sheetBook = null;

    private Cell[] cells = null;

    /**
     * default constructor
     */
    public MyXLSReader(int sheet)
    {
        if (sheet >= 0 && sheet < 65536)
        {
            this.sheet = sheet;
        }
    }

    /**
     * Ĭ�Ϲ�����
     */
    public MyXLSReader()
    {}

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.fileReader.AbstractReaderFile#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        if (current >= rows)
        {
            return false;
        }

        cells = sheetBook.getRow(current++ );

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.fileReader.AbstractReaderFile#next()
     */
    @Override
    public Object next()
    {
        return getStrings(cells);
    }

    @Override
    public void readFile(String filePath)
        throws IOException
    {
        File file = new File(filePath);
        readFile(file);
    }

    @Override
    public void readFile(File srcFile)
        throws IOException
    {
        try
        {
            workbook = Workbook.getWorkbook(srcFile);
            sheetBook = workbook.getSheet(sheet);
            rows = sheetBook.getRows();
        }
        catch (BiffException e)
        {
            throw new IOException("Read file failed");
        }
    }

    public void readFile(InputStream in)
        throws IOException
    {
        super.readFile(in);

        try
        {
            workbook = Workbook.getWorkbook(inputStream);
            sheetBook = workbook.getSheet(sheet);
            rows = sheetBook.getRows();
        }
        catch (BiffException e)
        {
            throw new IOException("Read file failed");
        }
    }

    @Override
    public void close()
        throws IOException
    {
        super.close();

        if (workbook != null)
        {
            workbook.close();
        }
    }

    private String[] getStrings(Cell[] cells)
    {
        if (cells == null)
        {
            return new String[] {};
        }

        String[] result = new String[cells.length];

        for (int i = 0; i < cells.length; i++ )
        {
            if (cells[i] != null)
            {
                result[i] = cells[i].getContents();
            }
            else
            {
                result[i] = "";
            }
        }
        return result;

    }

}
