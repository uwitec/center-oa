package com.china.center.common.taglib;


import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.china.center.common.taglib.active.Activator;
import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.annotation.Html;


/**
 * 页面单元格
 * 
 * @author ZHUZHU
 * @version 2007-3-14
 * @see PageBeanProperty
 * @since
 */
public class PageBeanProperty extends BodyTagCenterSupport
{
    private int width = 15;

    private String align = "left";

    private String index = "";

    private String trId = "";

    /**
     * 名字
     */
    private String field = "";

    private String endTag = "";

    /**
     * 原始值
     */
    private String value = "";

    private String innerString = "";

    private String outString = "";

    private Html html = null;

    private int cell = 1;

    /**
     * 默认构建器
     */
    public PageBeanProperty()
    {
    }

    public int getLastWidth()
    {
        int tatol = 100;
        Integer ints = (Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT + this.index);

        int ll = tatol / ints.intValue() - this.width;

        return ll > 0 ? ll : 5;
    }

    public int doStartTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        // 设置页面属性
        int allIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELL_INDEX + this.index)).intValue();

        int cells = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT + this.index)).intValue();

        int trIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_TRS_INDEX + this.index)).intValue();

        // 获得类
        String claz = (String)pageContext.getAttribute(TagLibConstant.CENTER_BEAN_CLASS);

        // 是增加还是更新
        int opr = (Integer)pageContext.getAttribute(TagLibConstant.CENTER_BEAN_OPR);

        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        Object oval = null;

        if (opr == TagLibConstant.BEAN_UPDATE)
        {
            oval = request.getAttribute(TagLibConstant.CENTER_BEAN_UPDATBEAN);
        }

        Class cla = null;
        try
        {
            if (oval == null)
            {
                BundleContext bundleContext = Activator.getBundleContext();

                Bundle[] bundles = bundleContext.getBundles();

                for (Bundle bundle : bundles)
                {
                    try
                    {
                        cla = bundle.loadClass(claz);

                        break;
                    }
                    catch (Throwable e)
                    {
                    }
                }

                if (cla == null)
                {
                    cla = Class.forName(claz);
                }
            }
            else
            {
                cla = oval.getClass();
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new JspException(e);
        }

        Field fields = BeanTools.getFieldIgnoreCase(this.field, cla);

        if (field == null)
        {
            throw new JspException(this.field + " not exist in " + claz);
        }

        // 获得field的HTML
        html = BeanTools.getPropertyHtml(fields);

        if (html == null)
        {
            throw new JspException(field + " do not have Html");
        }

        // 需要折行了
        if (allIndex % cells == 0 || this.cell <= TagLibConstant.ALL_CELLS)
        {
            String clc = null;
            if (trIndex % 2 == 0)
            {
                clc = "content1";
            }
            else
            {
                clc = "content2";
            }
            String trIds = "";

            if ( !this.isNullOrNone(trId))
            {
                trIds = "id='" + trId + "'";
            }

            buffer.append("<tr class='" + clc + "' id='" + fields.getName() + "_TR'").append(trIds).append(">\r\n");
        }

        int cellk = this.cell <= 0 ? (cells * 2 - 1) : (this.cell * 2) - 1;

        if ( (opr == TagLibConstant.BEAN_UPDATE || opr == TagLibConstant.BEAN_DISPLAY) && isNullOrNone(this.value))
        {
            oval = request.getAttribute(TagLibConstant.CENTER_BEAN_UPDATBEAN);

            if (oval != null)
            {
                fields.setAccessible(true);

                try
                {
                    Object oo = fields.get(oval);

                    if (oo != null)
                    {
                        this.value = escapeHtml(oo.toString());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        String[] rs = WriteBeanProperty.writeProperty(cla, this.field, this.value, cellk, this.innerString,
            this.outString, this.width + "%", getLastWidth() + "%", this.align, request.getContextPath(), opr);

        this.value = "";

        this.endTag = rs[1];

        this.writeContext(buffer.toString() + rs[0]);

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
        // 页面显示的字符
        StringBuffer buffer = new StringBuffer();

        // 当前的CELL数

        int allIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELL_INDEX + this.index)).intValue()
                       + this.cell;

        int cells = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_CELLS_INIT + this.index)).intValue();

        int trIndex = ((Integer)pageContext.getAttribute(TagLibConstant.CENTER_TRS_INDEX + this.index)).intValue();

        writeEnd(buffer);

        if (allIndex % cells == 0 || this.cell <= TagLibConstant.ALL_CELLS)
        {
            buffer.append("</tr>");

            pageContext.setAttribute(TagLibConstant.CENTER_TRS_INDEX + this.index, new Integer(trIndex + 1));
        }

        pageContext.setAttribute(TagLibConstant.CENTER_CELL_INDEX + this.index, new Integer(allIndex));

        this.writeContext(buffer.toString());

        return EVAL_PAGE;
    }

    /**
     * Filter the specified string for characters that are sensitive to HTML interpreters, returning the string with
     * these characters replaced by the corresponding character entities.
     * 
     * @param value
     *            The string to be filtered and returned
     */
    private String escapeHtml(String value)
    {
        if (value == null || value.length() == 0)
        {
            return value;
        }

        StringBuffer result = null;
        String filtered = null;
        for (int i = 0; i < value.length(); i++ )
        {
            filtered = null;
            switch (value.charAt(i))
            {
                case '<':
                    filtered = "&lt;";
                    break;
                case '>':
                    filtered = "&gt;";
                    break;
                case '&':
                    filtered = "&amp;";
                    break;
                case '"':
                    filtered = "&quot;";
                    break;
                case '\'':
                    filtered = "&#39;";
                    break;
            }

            if (result == null)
            {
                if (filtered != null)
                {
                    result = new StringBuffer(value.length() + 50);
                    if (i > 0)
                    {
                        result.append(value.substring(0, i));
                    }
                    result.append(filtered);
                }
            }
            else
            {
                if (filtered == null)
                {
                    result.append(value.charAt(i));
                }
                else
                {
                    result.append(filtered);
                }
            }
        }

        return result == null ? value : result.toString();
    }

    private void writeEnd(StringBuffer buffer)
    {
        String line = "\r\n";
        buffer.append(this.endTag).append(line);

    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getAlign()
    {
        return align;
    }

    public void setAlign(String align)
    {
        this.align = align;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * @return 返回 trId
     */
    public String getTrId()
    {
        return trId;
    }

    /**
     * @param 对trId进行赋值
     */
    public void setTrId(String trId)
    {
        this.trId = trId;
    }

    /**
     * @return 返回 field
     */
    public String getField()
    {
        return field;
    }

    /**
     * @param 对field进行赋值
     */
    public void setField(String field)
    {
        this.field = field;
    }

    /**
     * @return 返回 value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param 对value进行赋值
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * @return 返回 innerString
     */
    public String getInnerString()
    {
        return innerString;
    }

    /**
     * @param 对innerString进行赋值
     */
    public void setInnerString(String innerString)
    {
        this.innerString = innerString;
    }

    /**
     * @return the cell
     */
    public int getCell()
    {
        return cell;
    }

    /**
     * @param cell
     *            the cell to set
     */
    public void setCell(int cell)
    {
        this.cell = cell;
    }

    /**
     * @return the outString
     */
    public String getOutString()
    {
        return outString;
    }

    /**
     * @param outString
     *            the outString to set
     */
    public void setOutString(String outString)
    {
        this.outString = outString;
    }

}
