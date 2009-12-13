/**
 *
 */
package com.china.centet.yongyin.jms;


import com.china.center.common.storage.MoreHashMap;


/**
 * @author Administrator
 */
public class Test
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        MoreHashMap<String, Integer, String> map = new MoreHashMap<String, Integer, String>();

        map.put("123", 1, "234");

        System.out.println(map.getValue2("aa"));

    }

}
