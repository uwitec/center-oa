package com.china.center.jdbc.util;


import java.io.Serializable;


/**
 * page separate
 * 
 * @author ZHUZHU
 * @version 2008-3-7
 * @see
 * @since
 */
public class PageSeparate implements Serializable
{
    private static final int PAGE_SIZE = 20;

    // 定义总行数
    private int rowCount = 0;

    // 每页显示的行数
    private int pageSize = 20;

    // 定义总页数
    private int pageCount = 0;

    // 当前为第几页
    // 此值会在类构造时进行初始化为 1
    private int nowPage = 0;

    /**
     * Copy Constructor
     * 
     * @param pageSeparate
     *            a <code>PageSeparate</code> object
     */
    public PageSeparate(PageSeparate pageSeparate)
    {
        this.rowCount = pageSeparate.rowCount;
        this.pageSize = pageSeparate.pageSize;
        this.pageCount = pageSeparate.pageCount;
        this.nowPage = pageSeparate.nowPage;
    }

    /**
     * 默认构建器
     */
    public PageSeparate()
    {
    }

    public void reset(int rowCount, int pageSize)
    {
        init(rowCount, pageSize);
    }

    public PageSeparate(int rowCount, int pageSize)
    {
        init(rowCount, pageSize);
    }

    /**
     * @param rowCount
     * @param pageSize
     */
    private void init(int rowCount, int pageSize)
    {
        if ( (rowCount < 0) || (pageSize < 1))
        {
            this.rowCount = 0;
            this.pageSize = PAGE_SIZE;
        }
        else
        {
            this.rowCount = rowCount;
            this.pageSize = pageSize;
        }

        // 计算总页数，如果返回0条记录，即rowCount为0，则pageCount的值也为0
        pageCount = (this.rowCount + this.pageSize - 1) / this.pageSize;

        // 将当前页数设置为第一页(如果有数据，则设置为1，没有数据设置为0)
        nowPage = (pageCount > 0) ? 1 : 0;
    }

    /**
     * Description: <br>
     * 设置总行数，同时要计算新的pageCount <br>
     * ◎@return rowCount 总行数
     * 
     * @param rowCount
     */
    public void setRowCount(int rowCount)
    {
        this.rowCount = rowCount;
        this.pageCount = (rowCount + pageSize - 1) / this.pageSize;
        // add by pengyi 修改删除最后一页数据时，设置nowpage为最有一页
        if (nowPage > pageCount)
        {
            nowPage = pageCount;
        }

        // 如果开始为0，则说明是从无到有，应该设置为1
        if ( (nowPage == 0) && (pageCount != 0))
        {
            nowPage = 1;
        }
    }

    /**
     * Description: <br>
     * 获得总行数 <br>
     * ◎@return rowCount 总行数
     */
    public int getRowCount()
    {
        return rowCount;
    }

    /**
     * Description: <br>
     * 设置每页显示的行数 <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param pageSize
     *            每页显示的行数 ◎@return void
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * Description: <br>
     * 获得每页显示的行数 <br>
     * ◎@return 每页显示的行数
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * Description: <br>
     * 获得总页数 <br>
     * ◎@return 总页数
     */
    public int getPageCount()
    {
        return pageCount;
    }

    /**
     * Description: <br>
     * 判断当前页是否有下一页 <br>
     * Implement: <br>
     * 通过比较当前的页数与总页数来判断 <br>
     * [参数列表，说明每个参数用途] ◎@return 如果有下一页，返回true; 如果没有下一页，返回false
     */
    public boolean hasNextPage()
    {
        return (nowPage < pageCount) ? true : false;
    }

    /**
     * Description: <br>
     * 判断当前页是否有上一页 <br>
     * Implement: <br>
     * 通过比较当前页是否是第一页 <br>
     * [参数列表，说明每个参数用途] ◎@return 如果有上一页，返回true; 如果没有上一页，返回false
     */
    public boolean hasPrevPage()
    {
        return (nowPage > 1) ? true : false;
    }

    /**
     * Description: <br>
     * 向下翻页 <br>
     * Implement: <br>
     * 1. 判断当前页是否有下页 <br>
     * 2. 如果有下页，当前页面数加1 <br>
     * [参数列表，说明每个参数用途] ◎@return void
     */
    public boolean nextPage()
    {
        if (hasNextPage())
        {
            nowPage++ ;

            return true;
        }

        // 输出提示：已经是最后一页
        return false;
    }

    /**
     * Description: <br>
     * 向上翻页 <br>
     * Implement: <br>
     * 1. 判断当前页是否有上页 <br>
     * 2. 如果有上页，当前页面数减1 <br>
     * [参数列表，说明每个参数用途] ◎@return void
     */
    public boolean prevPage()
    {
        if (hasPrevPage())
        {
            nowPage-- ;
            return true;
        }

        // 输出提示：已经是第一页
        return false;

    }

    /**
     * Description: <br>
     * 判断当前页是否是第一页 <br>
     * Implement: <br>
     * 1. 当前页面数与1比较 <br>
     * [参数列表，说明每个参数用途] ◎@return 如果是第一页，返回true，否则返回false
     */
    public boolean isFirstPage()
    {
        return (nowPage == 1) ? true : false;
    }

    /**
     * Description: <br>
     * 判断当前页是否是最后一页 <br>
     * Implement: <br>
     * 1. 当前页面数与总页数比较；或者判断总页数是否为0 <br>
     * [参数列表，说明每个参数用途] ◎@return 如果是最后一页，返回true，否则返回false
     */
    public boolean isLastPage()
    {
        return ( (pageCount == 0) || (nowPage == pageCount)) ? true : false;
    }

    /**
     * Description: <br>
     * 获得当前页第一行在总记录中的行数 <br>
     * Implement: <br>
     * 1. 当前页面数减1然后乘以每页显示的行数 <br>
     * [参数列表，说明每个参数用途] ◎@return 当前页第一行在总记录中的行数
     */
    public int getSectionFoot()
    {

        return (pageCount == 0) ? 0 : (nowPage - 1) * pageSize;
    }

    /**
     * Description: <br>
     * 获得当前页最后一行在总记录中的行数 <br>
     * Implement: <br>
     * 1. 当前页面第一行的行数加上每页显示的行数 <br>
     * [参数列表，说明每个参数用途] ◎@return 当前页最后一行在总记录中的行数
     */
    public int getSectionTop()
    {
        return (isLastPage()) ? rowCount : (getSectionFoot() + pageSize);
    }

    /**
     * Description: <br>
     * 设置当前页 <br>
     * [参数列表，说明每个参数用途] ◎@return 如果用户输入了非法的值，返回false；否则将当前页数设置为用户输入的值，并返回true
     * 
     * @param nowPage
     */
    public boolean setNowPage(int nowPage)
    {
        // 如果用户输入的值为0或者负数，或者大于总页数的值，则返回false
        if ( (nowPage < 1))
        {
            this.nowPage = 1;

            return false;
        }

        if (nowPage > pageCount)
        {
            this.nowPage = pageCount;

            return false;
        }

        this.nowPage = nowPage;

        return true;
    }

    /**
     * Description: <br>
     * 获得当前页数 <br>
     * ◎@return 当前数
     */
    public int getNowPage()
    {
        return nowPage;
    }
}
