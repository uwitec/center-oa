/**
 * File Name: TestHtml.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-7<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.net;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


/**
 * TestHtml
 * 
 * @author zhuzhu
 * @version 2009-6-7
 * @see TestHtml
 * @since 1.0
 */
public class TestHtml
{

    private static String url = "http://rent.house365.com/rentlist_rent.php?infofrom=1&price=0"
                                + "&renttype=%D5%FB%D7%E2&district=%D3%EA%BB%A8%CC%A8%C7%F8&keyword"
                                + "=%BE%B0%C3%F7&page=&roomtype=0&streetid=&blockid=&order=&dis=1&kind=&topic=&channel=";

    private static Map<String, String> map = new HashMap<String, String>();

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
        throws Exception
    {
        Timer timer = new Timer("zufang");

        timer.schedule(new Task(), new Date(), 60 * 1000 * 10);
    }

    static class Task extends TimerTask
    {
        public Task()
        {}

        public void run()
        {
            try
            {
                access();
            }
            catch (ParserException e)
            {}
        }
    }

    public synchronized static void access()
        throws ParserException
    {
        System.out.println("开始扫描" + new Date());

        Parser parserHtml = new Parser(url);

        NodeFilter filter = new NodeClassFilter(Div.class);

        NodeList nodeList = null;

        Node[] nodes = null;

        nodeList = parserHtml.parse(filter);

        nodes = nodeList.toNodeArray();

        for (int i = 0; i < nodes.length; i++ )
        {
            Div anode = (Div)nodes[i];

            if ("selllist_list".equalsIgnoreCase(anode.getAttribute("id")))
            {
                TableTag table = fiter(anode, TableTag.class);

                List<TableRow> fiterList = fiterList(table, org.htmlparser.tags.TableRow.class);

                for (TableRow tableRow : fiterList)
                {
                    List<TableColumn> tdList = fiterList(tableRow,
                        org.htmlparser.tags.TableColumn.class);

                    StringBuilder builder = new StringBuilder();

                    boolean contain = true;

                    String id = "";

                    String href = "";

                    for (int j = 0; j < tdList.size(); j++ )
                    {
                        String text = tdList.get(j).toPlainTextString().replaceAll("&nbsp;", "");

                        builder.append(text).append(" ");

                        if (j == 0)
                        {
                            InputTag input = fiter(tdList.get(j),
                                org.htmlparser.tags.InputTag.class);

                            if (input != null)
                            {
                                id = input.getAttribute("id");

                                if (id != null && !"".equals(id.trim()))
                                {
                                    contain = map.containsKey(id);
                                }
                                else
                                {
                                    contain = true;
                                }
                            }
                        }

                        // 处理链接
                        if (j == tdList.size() - 1)
                        {
                            LinkTag link = fiter(tdList.get(j), org.htmlparser.tags.LinkTag.class);

                            if (link != null)
                            {
                                href = link.getAttribute("href");
                            }
                        }
                    }

                    if ( !contain)
                    {
                        SendMail mail = null;

                        builder.append("<br><a href='");

                        builder.append(href).append("' target=_blank>查看详细</a>");

                        mail = new SendMail("smtp.163.com", "mac-csd@163.com", "zhuzhu",
                            "mac-csd", "123456789q~", new String[] {"cnchengshudong@163.com"},
                            "租房信息--" + id, builder.toString());

                        HashMap send = mail.send();

                        System.out.println(send);

                        // state
                        String state = send.get("state").toString();

                        if ("success".equals(state))
                        {
                            map.put(id, builder.toString());

                            System.out.println("发送邮件成功");
                        }
                        else
                        {
                            System.out.println("发送邮件失败");
                        }
                    }
                    else
                    {
                        System.out.println("已经发生过了，忽略处理");
                    }
                }
            }
        }

        System.out.println("扫描结束" + new Date());
    }

    private static <T> List<T> fiterList(Node node, Class<T> claz)
    {
        Node[] childrenAsNodeArray = node.getChildren().toNodeArray();

        List<T> result = new ArrayList<T>();
        for (Node node2 : childrenAsNodeArray)
        {
            if (node2.getClass() == claz)
            {
                T each = (T)node2;

                result.add(each);
            }
        }

        return result;
    }

    private static <T> T fiter(Node node, Class<T> claz)
    {
        Node[] childrenAsNodeArray = node.getChildren().toNodeArray();

        for (Node node2 : childrenAsNodeArray)
        {
            if (node2.getClass() == claz)
            {
                return (T)node2;
            }
        }

        return null;
    }
}
