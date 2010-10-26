/*
 * �ļ���JpCreater.java
 * ��Ȩ��Copyright by www.huawei.com
 * ����
 * �޸��ˣ�t60021625
 * �޸�ʱ�䣺2006-11-9
 * ���ٵ��ţ�
 * �޸ĵ��ţ�
 * �޸����ݣ�
 */

package com.china.center.tools;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;


/**
 * JPTools
 * 
 * @author ZHUZHU
 * @version 2010-10-26
 * @see JPTools
 * @since 1.0
 */
public abstract class JPTools
{
    public static String createShortSpell(String chinese, int length)
    {
        String result = "";

        if (chinese == null)
        {
            return null;
        }

        if (chinese.equals(""))
        {
            return "";
        }

        result = jpCreateHelper(chineseFilter(chinese, length));

        return result;
    }

    public static String createShortSpell(String chinese)
    {
        return createShortSpell(chinese, chinese.length() * 3);
    }

    public static String jpCreateHelper(String chinese)
    {

        String pinyin = "";
        String temp = "";
        temp = chinese;
        StringBuffer sb = new StringBuffer();

        if (temp == null)
        {
            return null;
        }

        if (temp.equals(""))
        {
            return "";
        }

        // ���ȫƴ
        pinyin = createFullSpell(temp, true);

        if ( ! ("".equals(pinyin)))
        {
            // ȡȫƴ����ĸ��ɼ�ƴ
            String[] word = pinyin.split(" ");
            char tempchar;

            for (int i = 0; i < word.length; i++ )
            {
                if ( !"".equals(word[i]))
                {
                    tempchar = word[i].charAt(0);
                    if (SortHelper.isNormalChar(tempchar))
                    {
                        sb.append(word[i].charAt(0));
                    }
                }
                else
                {
                    sb.append(" ");
                }
            }

        }
        return sb.toString();

    }

    public static String chineseFilter(String mix, int length)
    {

        String temp = mix;
        StringBuffer sb = new StringBuffer();
        int index = length;
        boolean spacestate = false;

        if ("".equals(temp) || temp == null)
        {
            return null;
        }

        for (int i = 0; i < temp.length(); i++ )
        {
            final char firstChar = temp.charAt(i);
            if ( !Character.isDigit(firstChar) && !SortHelper.isNormalChar(firstChar)
                && !SortHelper.isASCIIChar(firstChar) && isChineseChar(firstChar))
            {
                sb.append(firstChar);
                index-- ;
                spacestate = false;
                // ������󳤶ȣ��˳�
                if (index <= 0)
                {
                    break;
                }
            }
            else if (spacestate == false && !"".equals(sb.toString()))
            {
                sb.append(" ");
                spacestate = true;
            }

        }

        return sb.toString();
    }

    public static boolean isChineseChar(char ch)
    {
        String temp = String.valueOf(ch);

        if ( (SortHelper.compare(temp, "\u3007") >= 0) && (SortHelper.compare(temp, "\u9FA5") <= 0))
        {
            return true;
        }

        return false;
    }

    public static String createFullSpell(String chinese)
    {
        return createFullSpell(chinese, false);
    }

    public static String createFullSpell(String chinese, boolean blank)
    {
        String result = "";

        if (chinese == null)
        {
            return "";
        }

        if ("".equals(chinese))
        {
            return "";
        }

        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        if (blank)
        {
            result = PinyinHelper.toHanyuPinyinString(chinese, outputFormat, " ");
        }
        else
        {
            result = PinyinHelper.toHanyuPinyinString(chinese, outputFormat, "");
        }

        return result;

    }

    public static boolean isChineseString(String keyword)
    {
        if (keyword == null || "".equals(keyword))
        {
            return false;
        }

        for (int i = 0; i < keyword.length(); i++ )
        {
            final char firstChar = keyword.charAt(i);
            if ( !isChineseChar(firstChar))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean isDigitString(String keyword)
    {
        if (keyword == null || "".equals(keyword))
        {
            return false;
        }

        for (int i = 0; i < keyword.length(); i++ )
        {
            final char firstChar = keyword.charAt(i);
            if ( !Character.isDigit(firstChar))
            {
                return false;
            }
        }

        return true;
    }

    public static List chineseFilterInList(String mix, int length)
    {

        String temp = mix;
        StringBuffer sb = new StringBuffer();
        List resultlist = new ArrayList();
        int index = length;
        boolean spacestate = false;

        if ("".equals(temp) || temp == null)
        {
            return null;
        }

        for (int i = 0; i < temp.length(); i++ )
        {
            final char firstChar = temp.charAt(i);
            if ( !Character.isDigit(firstChar) && !SortHelper.isNormalChar(firstChar)
                && !SortHelper.isASCIIChar(firstChar) && isChineseChar(firstChar))
            {
                sb.append(firstChar);
                index-- ;
                spacestate = false;
                // ������󳤶ȣ��˳�
                if (index <= 0)
                {
                    resultlist.add(sb.toString());
                    return resultlist;
                }
            }
            else if (spacestate == false && !"".equals(sb.toString()))
            {
                resultlist.add(sb.toString());
                sb = null;
                sb = new StringBuffer();
                spacestate = true;
            }
        }
        resultlist.add(sb.toString());

        return resultlist;
    }

    public static void main(String[] args)
    {
        String chinesegongfu = "asdfasdf测试的中国";
        System.out.println(JPTools.createShortSpell(chinesegongfu));
        System.out.println(JPTools.createFullSpell(chinesegongfu));
    }

}
