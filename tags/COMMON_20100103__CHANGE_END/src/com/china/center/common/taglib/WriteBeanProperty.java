package com.china.center.common.taglib;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.annotation.Html;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.enums.Element;


/**
 * д������
 * 
 * @author admin
 * @version [�汾��, 2007-10-9]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public abstract class WriteBeanProperty
{
    /**
     * ��ǩдbean
     * 
     * @param claz
     *            ��
     * @param property
     *            ����
     * @param value
     *            Ĭ��ֵ
     * @param cell
     *            ��Ԫ��
     * @param width1
     * @param width2
     * @param algin
     * @return
     */
    public static String[] writeProperty(Class claz, String property, String value, int cell,
                                         String innerString, String outString, String width1,
                                         String width2, String algin, String rootUrl)
    {
        Field field = BeanTools.getFieldIgnoreCase(property, claz);

        StringBuffer buffer = new StringBuffer();
        if (field == null)
        {
            buffer.append("<td></td><td>");
            return new String[] {buffer.toString(), ""};
        }

        Html html = BeanTools.getPropertyHtml(field);

        if (html == null)
        {
            buffer.append("<td></td><td>");

            return new String[] {buffer.toString(), ""};
        }

        // <td align="right">��ϵ�ˣ�</td>
        // <td><input type="text" name="connector" maxlength="14"
        // readonly="readonly">
        String algins = "";
        String widths = "";
        String widths2 = "";

        String name = property;

        if ( !isNullOrNone(html.name()))
        {
            name = html.name();
        }

        String trIds1 = " id = '" + name + "_td1" + "' ";

        String trIds2 = " id = '" + name + "_td2" + "' ";

        if ( !isNullOrNone(algins))
        {
            algins = " algin = '" + algin + "' ";
        }

        if ( !isNullOrNone(width1))
        {
            widths = " width = '" + width1 + "' ";
        }

        if ( !isNullOrNone(width2))
        {
            widths2 = " width = '" + width2 + "' ";
        }

        buffer.append("<td ").append(trIds1).append(algins).append(widths).append(">");

        buffer.append(html.title()).append("��").append("</td>").append("\r\n");

        // ��ͨ��TD
        String sclospan = " colspan = " + cell + " ";

        buffer.append("<td ").append(trIds2).append(widths2).append(sclospan).append(">");

        // ����double����ʾ����
        if ( !isNullOrNone(value))
        {
            if (field.getType() == double.class || field.getType() == Double.class
                || field.getType() == Float.class || field.getType() == float.class
                || field.getType() == BigDecimal.class)
            {
                DecimalFormat df = new DecimalFormat("#0.00");

                double tem = Double.parseDouble(value);

                value = df.format(tem);
            }
        }

        String[] ele = getElement(html, property, value, innerString, rootUrl);

        String[] result = new String[2];

        result[0] = buffer.toString() + ele[0];
        result[1] = ele[1] + " " + outString + " </td>";

        return result;
    }

    /**
     * ���element
     * 
     * @param html
     * @return
     */
    private static String[] getElement(Html html, String property, String value,
                                       String innerString, String rootUrl)
    {
        StringBuffer buffer = new StringBuffer();
        StringBuffer end = new StringBuffer();

        String readonly = " ";

        String must = " ";

        String oncheck = " ";

        String values = " ";

        String style = " ";

        String maxLength = " ";

        String inner = " ";

        String tip = " ";

        String oldname = property;

        if ( !isNullOrNone(html.name()))
        {
            oldname = html.name();
        }

        if ( !isNullOrNone(innerString))
        {
            inner = " " + innerString + " ";
        }

        String name = " name='" + oldname + "' head='" + html.title() + "' " + " id ='" + oldname
                      + "' ";

        if (html.readonly())
        {
            readonly = " readonly=readonly ";
        }

        if (html.must())
        {
            must = " <font color='#FF0000'>*</font> ";
        }

        String str_oncheck = getOncheck(html.oncheck());

        if ( !isNullOrNone(str_oncheck))
        {
            if (html.must())
            {
                if (html.type() == null || html.type() == Element.INPUT)
                {
                    oncheck = " oncheck=\"" + str_oncheck + JCheck.NOT_NONE + "\" ";
                }
                else
                {
                    oncheck = " oncheck=\"" + str_oncheck + JCheck.NOT_NONE + "\" ";
                }
            }
            else
            {
                if (html.type() == null || html.type() == Element.INPUT)
                {
                    oncheck = " oncheck=\"" + str_oncheck + "\" ";
                }
                else
                {
                    oncheck = " oncheck=\"" + str_oncheck + "\" ";
                }
            }
        }
        else
        {
            if (html.must())
            {
                if (html.type() == null || html.type() == Element.INPUT)
                {
                    oncheck = " oncheck=\"" + JCheck.NOT_NONE + JCheck.ONLY_COMMONCHAR + "\" ";
                }
                else
                {
                    oncheck = "oncheck=\"" + JCheck.NOT_NONE + "\" ";
                }
            }
        }

        if ( !isNullOrNone(value))
        {
            values = " value='" + value + "' ";
        }

        if (html.maxLength() > 0)
        {
            maxLength = " maxlength=\"" + html.maxLength() + "\" ";
        }

        if ( !isNullOrNone(html.tip()))
        {
            tip = " title=\"" + html.tip() + "\" ";
        }

        if (html.type() == null || html.type() == Element.INPUT)
        {
            // <input type="text" name="connector" maxlength="14"
            // oncheck="notNone"
            // readonly="readonly">
            buffer.append("<input type=text").append(name).append(tip).append(inner).append(
                readonly).append(values).append(style);
            buffer.append(oncheck).append(maxLength).append(">");

            buffer.append(must);
        }

        if (html.type() == null || html.type() == Element.PASSWORD)
        {
            // <input type="text" name="connector" maxlength="14"
            // oncheck="notNone"
            // readonly="readonly">
            buffer.append("<input type=password").append(name).append(inner).append(readonly).append(
                values).append(style);
            buffer.append(oncheck).append(maxLength).append(">");

            buffer.append(must);
        }

        if (html.type() == Element.DATE)
        {
            // <input type="text" name="connector" maxlength="14"
            // oncheck="notNone"
            // readonly="readonly">
            String special = " readonly=readonly ";

            buffer.append("<input type=text").append(name).append(tip).append(inner).append(values).append(
                special).append(style);
            buffer.append(oncheck).append(maxLength).append(">");
            // <img src="../images/calendar.gif" style="cursor: pointer"
            // title="��ѡ��ʱ��" align="top" onclick="return
            // calDate('date');" height="20px" width="20px"/>
            buffer.append("<img src='"
                          + rootUrl
                          + TagLibConstant.DEST_FOLDER_NAME
                          + "calendar.gif' style='cursor: pointer' title='��ѡ��ʱ��' align='top' onclick='return calDate(\""
                          + oldname + "\");' height='20px' width='20px'/>");

            buffer.append(must);
        }

        if (html.type() == Element.DATETIME)
        {
            // <input type="text" name="connector" maxlength="14"
            // oncheck="notNone"
            // readonly="readonly">
            String special = " readonly=readonly ";

            buffer.append("<input type=text").append(name).append(tip).append(inner).append(values).append(
                special).append(style);
            buffer.append(oncheck).append(maxLength).append(">");
            // <img src="../images/calendar.gif" style="cursor: pointer"
            // title="��ѡ��ʱ��" align="top" onclick="return
            // calDate('date');" height="20px" width="20px"/>
            buffer.append("<img src='"
                          + rootUrl
                          + TagLibConstant.DEST_FOLDER_NAME
                          + "calendar.gif' style='cursor: pointer' title='��ѡ��ʱ��' align='top' onclick='return calDateTime(\""
                          + oldname + "\");' height='20px' width='20px'/>");

            buffer.append(must);
        }

        if (html.type() == Element.SELECT)
        {
            if ( !isNullOrNone(value))
            {
                values = " values='" + value + "' ";
            }

            buffer.append("<select ").append(name).append(inner).append(style).append(values).append(
                " class=\"select_class\" ").append(oncheck).append('>');

            end.append("</select>").append(must);
        }

        if (html.type() == Element.TEXTAREA)
        {
            if (html.maxLength() > 0)
            {
                maxLength = "maxLength(" + html.maxLength() + ");";
                if (isNullOrNone(oncheck))
                {
                    oncheck = "oncheck=\"" + maxLength + "\" ";
                }
                else
                {
                    if (oncheck.trim().endsWith("\""))
                    {
                        oncheck = oncheck.trim().substring(0, oncheck.trim().length() - 1)
                                  + maxLength + "\"";
                    }
                    else
                    {
                        oncheck += maxLength;
                    }
                }
            }

            // <textarea rows="3" cols="55"
            // name="description">${description}</textarea>
            buffer.append("<textarea ").append(name).append(tip).append(inner).append(style).append(
                oncheck).append(readonly).append(">")
            // TEXTAREA��Ҫע��
            .append(value.trim());

            end.append("</textarea>").append(must);
        }

        if (html.type() == Element.CHECKBOX)
        {
            // <textarea rows="3" cols="55"
            // name="description">${description}</textarea>
            buffer.append("<input type=checkbox").append(name).append(inner).append(values).append(
                style).append(oncheck);

            buffer.append(">");
        }

        if (html.type() == Element.RADIO)
        {
            // <textarea rows="3" cols="55"
            // name="description">${description}</textarea>
            buffer.append("<input type=radio").append(name).append(inner).append(values).append(
                style).append(oncheck);

            buffer.append(">");
        }

        return new String[] {buffer.toString(), end.toString()};
    }

    public static boolean isNullOrNone(String name)
    {
        if (name == null || "".equals(name.trim()))
        {
            return true;
        }

        return false;
    }

    public static String getOncheck(String[] name)
    {
        if (name == null || name.length == 0)
        {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (String string : name)
        {
            builder.append(string).append(";");
        }

        return builder.toString();
    }
}