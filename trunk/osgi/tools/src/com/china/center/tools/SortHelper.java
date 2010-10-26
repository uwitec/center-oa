/*
 * �ļ���GetSpellOfCN.java
 * ��Ȩ��Copyright by www.huawei.com
 * ����
 * �޸��ˣ�public0244
 * �޸�ʱ�䣺2006-3-31
 * ���ٵ��ţ�
 * �޸ĵ��ţ�
 * �޸����ݣ�
 */

package com.china.center.tools;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;


/**
 * ��ƴ������
 * 
 * @author public0244
 * @version 2006-3-31
 * @see SortHelper
 * @since
 */
public class SortHelper
{

    private static final int CHARCODE = 100;

    private static final String _FromEncode_ = "GBK";

    private static final String _ToEncode_ = "GBK";

    /**
     * Automatically generated method: SortHelper
     */
    private SortHelper()
    {

    }

    /**
     * Automatically generated method: toString
     */
    public String toString()
    {
        return super.toString();
    }

    /**
     * Description: <br>
     * 1���� <br>
     * 2���� <br>
     * Implement: <br>
     * 1���� <br>
     * 2���� <br>
     * [�����б?˵��ÿ�������;]
     * 
     * @param str
     * @return String
     */
    public static String getSpell(String str)
    {
        if ( (str == null) || "".equals(str))
        {
            return "";
        }

        char firstChar = str.charAt(0);

        if (Character.isDigit(firstChar) || isNormalChar(firstChar))
        {
            return Character.toString(firstChar).toUpperCase();
        }

        return getBeginCharacter(Character.toString(firstChar)).toUpperCase();

    }

    /**
     * Description: <br>
     * 1�������ַ��ÿ�������ַ�����ĸ�� <br>
     * 2���� <br>
     * Implement: <br>
     * 1��������ĸ�����жϣ������������򷵻ظ��ַ��д������ĸ�� <br>
     * 2���� <br>
     * 
     * @param str
     *            ԭʼ�ַ�
     * @return ���ַ��ƴ������ĸ
     */
    public static String getPinStr(String str)
    {
        if ( (str == null) || "".equals(str))
        {
            return "";
        }
        String pinStr = "";
        for (int i = 0; i < str.length(); i++ )
        {
            final char firstChar = str.charAt(i);

            if (Character.isDigit(firstChar) || isNormalChar(firstChar))
            {
                pinStr = pinStr + Character.toString(firstChar).toUpperCase();
            }
            else
            {

                pinStr = pinStr + getBeginCharacter(Character.toString(firstChar)).toUpperCase();
            }
        }

        return pinStr;
    }

    /**
     * Description: <br>
     * 1�������ַ��ÿ�������ַ�����ĸ�� <br>
     * 2���� <br>
     * Implement: <br>
     * 1��������ĸ�����жϣ������������򷵻ظ��ַ��д������ĸ�� <br>
     * 2���� <br>
     * 
     * @param str
     *            ԭʼ�ַ�
     * @return ���ַ��ƴ������ĸ
     */
    public static String getFullPinStr(String str)
    {
        String pinStr = "";
        if ( (str == null) || "".equals(str))
        {
            return "";
        }
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        pinStr = PinyinHelper.toHanyuPinyinString(str, outputFormat, " ");
        return pinStr;
    }

    /**
     * @param ��ƴ
     * @return ���п�����Ҫ��ѯ�ļ�ƴ
     */
    public static List compartPINYIN(String pinyin)
    {
        List ls = null;
        if (pinyin == null || "".equals(pinyin))
        {
            return null;
        }
        ls = new ArrayList();
        String[] sts = pinyin.split(" ");
        String st1 = "";

        if (sts.length > 2)
        {
            for (int i = 2; i < sts.length; i++ )
            {
                for (int j = 1; j < (sts.length - i + 2); j++ )
                {
                    for (int k = 0; k < i; k++ )
                    {
                        if ("".equals(st1))
                        {
                            st1 += sts[j - 1 + k];
                        }
                        else
                        {
                            st1 += (" " + sts[j - 1 + k]);
                        }
                    }
                    ls.add(st1);
                    st1 = "";
                }
            }
        }
        ls.add(pinyin);
        return ls;
    }

    /**
     * Description: <br>
     * 1�������ַ��Ƿ������ġ� <br>
     * 
     * @return �����ַ��Ƿ�����
     */
    public static boolean isChineseWord(String str)
    {
        if ( (str == null) || "".equals(str))
        {
            return false;
        }
        for (int i = 0; i < str.length(); i++ )
        {
            final char firstChar = str.charAt(i);
            if ( !Character.isDigit(firstChar) && !isASCIIChar(firstChar))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Description: ����Ƿ�Ϊ0��128�ĵ��ַ� <br>
     * [�����б?˵��ÿ�������;]
     * 
     * @param ch
     * @return boolean
     */
    public static boolean isASCIIChar(char ch)
    {
        if ((int)ch >= 0 && (int)ch <= 128)
        {
            return true;
        }

        return false;
    }

    /**
     * Description: ����Ƿ�Ϊ����ַ� <br>
     * [�����б?˵��ÿ�������;]
     * 
     * @param ch
     * @return boolean
     */
    public static boolean isNormalChar(char ch)
    {
        if ( ( (ch >= 'a') && (ch <= 'z')) || ( (ch >= 'A') && (ch <= 'Z')))
        {
            return true;
        }

        return false;
    }

    /**
     * Description: �Ƚ�}���ַ� <br>
     * [�����б?˵��ÿ�������;]
     * 
     * @param str1
     * @param str2
     * @return int
     */
    public static int compare(String str1, String str2)
    {
        String m_s1 = null;
        String m_s2 = null;

        try
        {
            m_s1 = new String(str1.getBytes(_FromEncode_), _ToEncode_);
            m_s2 = new String(str2.getBytes(_FromEncode_), _ToEncode_);
        }
        catch (UnsupportedEncodingException e)
        {
            return str1.compareTo(str2);
        }
        return chineseCompareTo(m_s1, m_s2);
    }

    /**
     * Description: �õ��ַ���� <br>
     * [�����б?˵��ÿ�������;]
     * 
     * @param s
     * @return int
     */
    private static int getCharCode(String s)
    {
        if (s == null || "".equals(s))
        {
            return -1;
        }

        byte[] b = s.getBytes();
        int value = 0;

        for (int i = 0; (i < b.length) && (i <= 2); i++ )
        {
            value = value * CHARCODE + b[i];
        }

        return value;
    }

    /**
     * Description: �Ƚ�}�������ַ� <br>
     * [�����б?˵��ÿ�������;]
     * 
     * @param s1
     * @param s2
     * @return int
     */
    private static int chineseCompareTo(String s1, String s2)
    {
        int minLength = Math.min(s1.length(), s2.length());

        for (int i = 0; i < minLength; i++ )
        {
            int s1_code = getCharCode(s1.charAt(i) + "");
            int s2_code = getCharCode(s2.charAt(i) + "");

            if (s1_code * s2_code < 0)
            {
                return Math.min(s1_code, s2_code);
            }

            if (s1_code != s2_code)
            {
                return s1_code - s2_code;
            }
        }

        return s1.length() - s2.length();
    }

    /**
     * Description: �õ��ַ����ʼ�ַ� <br>
     * [�����б?˵��ÿ�������;]
     * 
     * @param res
     * @return String
     */
    private static String getBeginCharacter(String res)
    {
        String a = res;
        String result = "";

        for (int i = 0; i < a.length(); i++ )
        {
            String current = a.substring(i, i + 1);
            if (compare(current, "\u554A") < 0)
            {
                result = result + current;
            }
            else if ( (compare(current, "\u554A") >= 0) && (compare(current, "\u5EA7") <= 0))
            {
                if (compare(current, "\u531D") >= 0)
                {
                    result = result + "z";
                }
                else if (compare(current, "\u538B") >= 0)
                {
                    result = result + "y";
                }
                else if (compare(current, "\u6614") >= 0)
                {
                    result = result + "x";
                }
                else if (compare(current, "\u6316") >= 0)
                {
                    result = result + "w";
                }
                else if (compare(current, "\u584C") >= 0)
                {
                    result = result + "t";
                }
                else if (compare(current, "\u6492") >= 0)
                {
                    result = result + "s";
                }
                else if (compare(current, "\u7136") >= 0)
                {
                    result = result + "r";
                }
                else if (compare(current, "\u671F") >= 0)
                {
                    result = result + "q";
                }
                else if (compare(current, "\u556A") >= 0)
                {
                    result = result + "p";
                }
                else if (compare(current, "\u54E6") >= 0)
                {
                    result = result + "o";
                }
                else if (compare(current, "\u62FF") >= 0)
                {
                    result = result + "n";
                }
                else if (compare(current, "\u5988") >= 0)
                {
                    result = result + "m";
                }
                else if (compare(current, "\u5783") >= 0)
                {
                    result = result + "l";
                }
                else if (compare(current, "\u5580") >= 0)
                {
                    result = result + "k";
                }
                else if (compare(current, "\u51FB") > 0)
                {
                    result = result + "j";
                }
                else if (compare(current, "\u54C8") >= 0)
                {
                    result = result + "h";
                }
                else if (compare(current, "\u5676") >= 0)
                {
                    result = result + "g";
                }
                else if (compare(current, "\u53D1") >= 0)
                {
                    result = result + "f";
                }
                else if (compare(current, "\u86FE") >= 0)
                {
                    result = result + "e";
                }
                else if (compare(current, "\u642D") >= 0)
                {
                    result = result + "d";
                }
                else if (compare(current, "\u64E6") >= 0)
                {
                    result = result + "c";
                }
                else if (compare(current, "\u82AD") >= 0)
                {
                    result = result + "b";
                }
                else if (compare(current, "\u554A") >= 0)
                {
                    result = result + "a";
                }
            }
        }

        return result;
    }

    /**
     * Description�������ж��ַ�ĸ�ʽ�Ƿ�Ϊ�����������<br>
     * 1��������ҵ�����Ȩ�صĺϷ����жϣ�<br>
     * 2����<br>
     * Implement: <br>
     * 1����<br>
     * 2����<br>
     * 
     * @param weight
     * @return
     * @see
     */
    public static boolean isCurrectWeight(String weight)
    {
        if (weight == null || "".equals(weight))
        {
            return false;
        }

        // ��Ϊ��0��ͷ���ַ�
        if (weight.charAt(0) == '0' && weight.length() > 1)
        {
            return false;
        }

        char tempchar;

        // ���ÿ���ַ�
        for (int i = 0; i < weight.length(); i++ )
        {
            tempchar = weight.charAt(i);

            if ( ! (tempchar >= '0' && tempchar <= '9'))
            {
                return false;
            }
        }

        return true;
    }
}