package com.china.center.webplugin.inter;


import java.util.ArrayList;
import java.util.List;


/**
 * FilterLoad
 * 
 * @author ZHUZHU
 * @version 2009-10-15
 * @see FilterLoad
 * @since 1.0
 */
public class FilterLoad
{
    private static final List<String> IGNORE_FILTER = new ArrayList()
    {
        {
            add("/index.jsp");
            add("/timeout.jsp");
        }
    };

    private static final List<String> IGNORE_FILTER_MATCH = new ArrayList()
    {
        {

        }
    };

    /**
     * loadIgnoreFilter
     * 
     * @param ignoreFilterList
     */
    public static void loadIgnoreFilter(List<String> ignoreFilterList)
    {
        for (String string : ignoreFilterList)
        {
            if ( !IGNORE_FILTER.contains(string))
            {
                IGNORE_FILTER.add(string);
            }
        }
    }

    /**
     * removeIgnoreFilter
     * 
     * @param ignoreFilterList
     */
    public static void removeIgnoreFilter(List<String> ignoreFilterList)
    {
        for (String string : ignoreFilterList)
        {
            IGNORE_FILTER.remove(string);
        }
    }

    /**
     * loadIgnoreFilterMatch
     * 
     * @param ignoreFilterMatchList
     */
    public static void loadIgnoreFilterMatch(List<String> ignoreFilterMatchList)
    {
        for (String string : ignoreFilterMatchList)
        {
            if ( !IGNORE_FILTER_MATCH.contains(string))
            {
                IGNORE_FILTER_MATCH.add(string);
            }
        }
    }

    /**
     * removeIgnoreFilterMatch
     * 
     * @param ignoreFilterMatchList
     */
    public static void removeIgnoreFilterMatch(List<String> ignoreFilterMatchList)
    {
        for (String string : ignoreFilterMatchList)
        {
            IGNORE_FILTER_MATCH.remove(string);
        }
    }

    /**
     * get iGNORE_FILTER
     * 
     * @return iGNORE_FILTER
     */
    public static List<String> getIGNORE_FILTER()
    {
        return IGNORE_FILTER;
    }

    /**
     * get iGNORE_FILTER_MATCH
     * 
     * @return iGNORE_FILTER_MATCH
     */
    public static List<String> getIGNORE_FILTER_MATCH()
    {
        return IGNORE_FILTER_MATCH;
    }
}
