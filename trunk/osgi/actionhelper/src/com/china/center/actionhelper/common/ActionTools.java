/**
 * File Name: QueryTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.common;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.china.center.actionhelper.query.CommonQuery;
import com.china.center.actionhelper.query.HandleHint;
import com.china.center.actionhelper.query.HandleResult;
import com.china.center.actionhelper.query.QueryConditionBean;
import com.china.center.actionhelper.query.QueryConfig;
import com.china.center.actionhelper.query.QueryItemBean;
import com.china.center.jdbc.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.DAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.CommonTools;
import com.china.center.tools.StringTools;


/**
 * QueryTools
 * 
 * @author ZHUZHU
 * @version 2008-10-2
 * @see
 * @since
 */
public abstract class ActionTools
{
    public static String ORDER = "order by";

    public static String SORTNAME = "sortname";

    public static String SORTORDER = "sortorder";

    public static String HIDDEN_QUERY = "hidden_query_";

    public static String HIDDEN_CHANGE_MAP = "HIDDEN_CHANGE_MAP";

    /**
     * 设置记忆分页
     * 
     * @param request
     */
    public static void setMemoryQuery(HttpServletRequest request)
    {
        request.setAttribute(PageSeparateTools.MEMORY, 1);
    }

    public static void setForwardQuery(HttpServletRequest request)
    {
        request.setAttribute(PageSeparateTools.FORWARD, 1);
    }

    public static void setJustQuery(HttpServletRequest request)
    {
        request.setAttribute(PageSeparateTools.JUST, 1);
    }

    public static boolean isJSONRequest(HttpServletRequest request)
    {
        String json = request.getParameter("JSON");

        return !StringTools.isNullOrNone(json);
    }

    /**
     * 处理查询
     * 
     * @param request
     * @param condtion
     */
    public static void processJSONQueryCondition(String skey, HttpServletRequest request,
                                                 ConditionParse condtion,
                                                 Map<String, String> initMap)
    {
        if (initMap != null && initMap.size() > 0)
        {
            // 设置到Attribute
            for (Map.Entry<String, String> entry : initMap.entrySet())
            {
                request.setAttribute(PageConstant.PAREXT + entry.getKey(), entry.getValue());
            }
        }

        if ( !JSONPageSeparateTools.isLoad(request))
        {
            return;
        }

        Map<String, String> map = CommonTools.saveParamersToMap(request);

        if (initMap != null && initMap.size() > 0)
        {
            map.putAll(initMap);
        }

        Map<String, QueryConditionBean> conmap = new HashMap<String, QueryConditionBean>();

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            String key = entry.getKey();

            if (key.startsWith(HIDDEN_QUERY))
            {
                String fieldName = key.substring(HIDDEN_QUERY.length());

                QueryConditionBean bean = new QueryConditionBean();

                bean.parser(entry.getValue());

                bean.setValue(map.get(fieldName));

                conmap.put(fieldName, bean);
            }
        }

        QueryConditionBean value = null;
        for (Map.Entry<String, QueryConditionBean> entry : conmap.entrySet())
        {
            value = entry.getValue();

            // 设置string型
            if (value.getDatatype() == 0)
            {
                String fieldName = "";

                if (StringTools.isNullOrNone(value.getPfix()))
                {
                    fieldName = value.getFiled();
                }
                else
                {
                    fieldName = value.getPfix() + "." + value.getFiled();
                }

                condtion.addCondition(fieldName, value.getOpr(), value.getValue());
            }

            if (value.getDatatype() == 1)
            {
                String fieldName = "";

                if (StringTools.isNullOrNone(value.getPfix()))
                {
                    fieldName = value.getFiled();
                }
                else
                {
                    fieldName = value.getPfix() + "." + value.getFiled();
                }

                condtion.addIntCondition(fieldName, value.getOpr(), value.getValue());
            }
        }
    }

    /**
     * 处理查询
     * 
     * @param request
     * @param condtion
     */
    public static void processJSONQueryCondition(String skey, HttpServletRequest request,
                                                 ConditionParse condtion)
    {
        processJSONQueryCondition(skey, request, condtion, null);
    }

    /**
     * 设置页面参数
     * 
     * @param request
     * @param key
     * @param value
     */
    public static void setParMapAttribute(HttpServletRequest request, String key, Object value)
    {
        request.setAttribute(PageSeparateTools.PAREXT + key, value);
    }

    /**
     * 常见的查询方法
     * 
     * @param request
     *            请求
     * @param list
     *            存放结果的列表
     * @param condtion
     *            条件
     * @param dao
     *            使用的dao
     * @param pagesize
     *            获取的数量
     */
    public static void commonQueryVO(String key, HttpServletRequest request, List list,
                                     ConditionParse condtion, DAO dao, int... pagesize)
    {
        int size = 10;

        if (pagesize == null || pagesize.length == 0)
        {
            size = 10;
        }
        else
        {
            size = pagesize[0];
        }

        list.addAll(commonQueryVOInner(key, request, condtion, dao, size));
    }

    /**
     * 常见的查询方法
     * 
     * @param request
     *            请求
     * @param list
     *            存放结果的列表
     * @param condtion
     *            条件
     * @param dao
     *            使用的dao
     * @param pagesize
     *            获取的数量
     */
    private static List<?> commonQueryVOInner(String key, HttpServletRequest request,
                                              ConditionParse condtion, DAO dao, int pagesize)
    {
        int size = pagesize;

        if (PageSeparateTools.isFirstLoad(request, key))
        {
            int total = dao.countVOByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, size);

            PageSeparateTools.initPageSeparate(condtion, page, request, key);
        }
        else if (PageSeparateTools.isMemory(request))
        {
            int total = dao.countVOByCondition(PageSeparateTools
                .getCondition(request, key)
                .toString());

            PageSeparateTools.processMemory(total, request, key);
        }
        else
        {
            PageSeparateTools.processSeparate(request, key);
        }

        return dao.queryEntityVOsByCondition(PageSeparateTools.getCondition(request, key),
            PageSeparateTools.getPageSeparate(request, key));
    }

    public static void commonQueryBean(String key, HttpServletRequest request, List list,
                                       ConditionParse condtion, DAO dao, int... pagesize)
    {
        int size = 10;

        if (pagesize == null || pagesize.length == 0)
        {
            size = 10;
        }
        else
        {
            size = pagesize[0];
        }

        list.addAll(commonQueryBeanInner(key, request, condtion, dao, size));
    }

    /**
     * queryBeanByJSON
     * 
     * @param key
     * @param request
     * @param list
     * @param condtion
     * @param dao
     */
    public static void queryBeanByJSON(String key, HttpServletRequest request, List list,
                                       ConditionParse condtion, DAO dao)
    {
        list.addAll(commonQueryBeanInnerByJSON(key, request, condtion, dao, true));
    }

    /**
     * queryVOByJSON
     * 
     * @param key
     * @param request
     * @param list
     * @param condtion
     * @param dao
     */
    public static void queryVOByJSON(String key, HttpServletRequest request, List list,
                                     ConditionParse condtion, DAO dao)
    {
        list.addAll(commonQueryBeanInnerByJSON(key, request, condtion, dao, false));
    }

    /**
     * queryVOByJSON
     * 
     * @param <T>
     * @param <V>
     * @param key
     * @param request
     * @param condtion
     * @param dao
     * @param handle
     */
    public static <T extends Serializable, V extends Serializable> void queryVOByJSON(
                                                                                      String key,
                                                                                      HttpServletRequest request,
                                                                                      List<V> list,
                                                                                      ConditionParse condtion,
                                                                                      DAO<T, V> dao,
                                                                                      HandleResult<V> handle)
    {
        list.addAll((List<V>)commonQueryBeanInnerByJSON(key, request, condtion, dao, false));

        for (V object : list)
        {
            handle.handle(object);
        }
    }

    /**
     * 查询到返回结果
     * 
     * @param key
     * @param request
     * @param list
     * @param condtion
     * @param dao
     * @return
     */
    public static String queryBeanByJSONAndToString(String key, HttpServletRequest request,
                                                    List list, ConditionParse condtion, DAO dao)
    {
        list.addAll(commonQueryBeanInnerByJSON(key, request, condtion, dao, true));

        return JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request, key));
    }

    /**
     * queryVOByJSONAndToString
     * 
     * @param key
     * @param request
     * @param list
     * @param condtion
     * @param dao
     * @return
     */
    public static String queryVOByJSONAndToString(String key, HttpServletRequest request,
                                                  List list, ConditionParse condtion, DAO dao)
    {
        list.addAll(commonQueryBeanInnerByJSON(key, request, condtion, dao, false));

        return JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request, key));
    }

    /**
     * 查询到返回结果
     * 
     * @param key
     * @param request
     * @param condtion
     * @param dao
     * @return
     */
    public static String queryBeanByJSONAndToString(String key, HttpServletRequest request,
                                                    ConditionParse condtion, DAO dao)
    {
        List list = new ArrayList();

        list.addAll(commonQueryBeanInnerByJSON(key, request, condtion, dao, true));

        return JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request, key));
    }

    /**
     * queryVOByJSONAndToString
     * 
     * @param key
     * @param request
     * @param condtion
     * @param dao
     * @return
     */
    public static String queryVOByJSONAndToString(String key, HttpServletRequest request,
                                                  ConditionParse condtion, DAO dao)
    {
        List list = new ArrayList();

        list.addAll(commonQueryBeanInnerByJSON(key, request, condtion, dao, false));

        return JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request, key));
    }

    /**
     * queryVOByJSONAndToString
     * 
     * @param <T>
     * @param <V>
     * @param key
     * @param request
     * @param condtion
     * @param dao
     * @param handle
     * @return
     */
    public static <T extends Serializable, V extends Serializable> String queryVOByJSONAndToString(
                                                                                                   String key,
                                                                                                   HttpServletRequest request,
                                                                                                   ConditionParse condtion,
                                                                                                   DAO<T, V> dao,
                                                                                                   HandleResult<V> handle)
    {
        return queryVOByJSONAndToString(key, request, condtion, dao, handle, "");
    }

    public static <T extends Serializable, V extends Serializable> String queryVOByJSONAndToString(
                                                                                                   String key,
                                                                                                   HttpServletRequest request,
                                                                                                   ConditionParse condtion,
                                                                                                   DAO<T, V> dao,
                                                                                                   HandleResult<V> handle,
                                                                                                   HandleHint hint)
    {
        List<V> list = new ArrayList<V>();

        list.addAll((List<V>)commonQueryBeanInnerByJSON(key, request, condtion, dao, false));

        for (V object : list)
        {
            handle.handle(object);
        }

        return queryVOByJSONAndToString(key, request, condtion, dao, handle, hint.getHint());
    }

    public static <T extends Serializable, V extends Serializable> String queryVOByJSONAndToString(
                                                                                                   String key,
                                                                                                   HttpServletRequest request,
                                                                                                   ConditionParse condtion,
                                                                                                   DAO<T, V> dao,
                                                                                                   HandleResult<V> handle,
                                                                                                   String hint)
    {
        List<V> list = new ArrayList<V>();

        list.addAll((List<V>)commonQueryBeanInnerByJSON(key, request, condtion, dao, false));

        for (V object : list)
        {
            handle.handle(object);
        }

        return JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request, key), hint);
    }

    public static <T extends Serializable, V extends Serializable> String queryBeanByJSONAndToString(
                                                                                                     String key,
                                                                                                     HttpServletRequest request,
                                                                                                     ConditionParse condtion,
                                                                                                     DAO<T, V> dao,
                                                                                                     HandleResult<T> handle)
    {
        List<T> list = new ArrayList<T>();

        list.addAll((List<T>)commonQueryBeanInnerByJSON(key, request, condtion, dao, false));

        for (T object : list)
        {
            handle.handle(object);
        }

        return JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request, key));
    }

    /**
     * 通用查询
     * 
     * @param key
     * @param request
     * @param queryCondition
     * @param dao
     * @param isBean
     *            是否是bean
     * @return
     */
    public static List<?> commonQueryBeanInnerByJSON(final String key,
                                                     final HttpServletRequest request,
                                                     final ConditionParse queryCondition,
                                                     final DAO dao, final boolean isBean)
    {
        return selfCommonQueryBeanInnerByJSON(key, request, queryCondition, new CommonQuery()
        {
            public int getCount(String key, HttpServletRequest request,
                                ConditionParse queryCondition)
            {
                return countEntry(dao, queryCondition, isBean);
            }

            public String getOrderPfix(String key, HttpServletRequest request)
            {
                return BeanTools.getClassName(dao.getBeanClass());
            }

            public String getSortname(HttpServletRequest request)
            {
                return processOrderColumn(request, queryCondition, dao.getBeanClass());
            }

            public List queryResult(String key, HttpServletRequest request,
                                    ConditionParse queryCondition)
            {
                if (isBean)
                {
                    return dao.queryEntityBeansByCondition(PageSeparateTools.getCondition(request,
                        key), PageSeparateTools.getPageSeparate(request, key));
                }
                else
                {
                    return dao.queryEntityVOsByCondition(PageSeparateTools.getCondition(request,
                        key), PageSeparateTools.getPageSeparate(request, key));
                }
            }
        });
    }

    /**
     * countEntry
     * 
     * @param dao
     * @param condtion
     * @param isBean
     * @return
     */
    private static int countEntry(DAO dao, ConditionParse condtion, boolean isBean)
    {
        if (isBean)
        {
            return dao.countByCondition(condtion.toString());
        }
        else
        {
            return dao.countVOByCondition(condtion.toString());
        }
    }

    /**
     * processOrder
     * 
     * @param request
     * @param condtion
     * @param beanClass
     */
    private static String processOrderColumn(HttpServletRequest request, ConditionParse condtion,
                                             Class beanClass)
    {
        String sortname = request.getParameter(SORTNAME);

        Field field = BeanTools.getFieldIgnoreCase(sortname, beanClass);

        String column = "";

        if (field != null)
        {
            column = BeanTools.getColumnName(field);
        }
        else
        {
            column = sortname;
        }

        return column;

    }

    private static void processOrder2(HttpServletRequest request, ConditionParse condtion,
                                      String column, String pfix)
    {
        String sortorder = request.getParameter(SORTORDER);

        if (condtion.toString().toLowerCase().indexOf(ORDER) == -1)
        {
            if (column.indexOf(".") == -1)
            {
                condtion.addCondition(ORDER + " " + pfix + "." + column + " " + sortorder);
            }
            else
            {
                condtion.addCondition(ORDER + " " + column + " " + sortorder);
            }
        }
        else
        {
            String conStr = condtion.toString();

            int index = condtion.toString().toLowerCase().indexOf(ORDER);

            ConditionParse newCondtion = new ConditionParse();

            newCondtion.addString(conStr.substring(0, index + ORDER.length()));

            if (column.indexOf(".") == -1)
            {
                newCondtion.addCondition(pfix + "." + column + " " + sortorder);
            }
            else
            {
                newCondtion.addCondition(column + " " + sortorder);
            }

            condtion.setCondition(newCondtion.toString());
        }
    }

    /**
     * commonQueryBeanInner
     * 
     * @param key
     * @param request
     * @param condtion
     * @param dao
     * @param pagesize
     * @return
     */
    private static List<?> commonQueryBeanInner(String key, HttpServletRequest request,
                                                ConditionParse condtion, DAO dao, int pagesize)
    {
        int size = pagesize;

        if (PageSeparateTools.isFirstLoad(request, key))
        {
            int total = dao.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, size);

            PageSeparateTools.initPageSeparate(condtion, page, request, key);
        }
        else if (PageSeparateTools.isMemory(request))
        {
            int total = dao.countByCondition(PageSeparateTools
                .getCondition(request, key)
                .toString());

            PageSeparateTools.processMemory(total, request, key);
        }
        else
        {
            PageSeparateTools.processSeparate(request, key);
        }

        return dao.queryEntityBeansByCondition(PageSeparateTools.getCondition(request, key),
            PageSeparateTools.getPageSeparate(request, key));
    }

    /**
     * 自定义的查询
     * 
     * @param key
     * @param request
     * @param queryCondition
     * @param dao
     * @param isBean
     * @return
     */
    public static List<?> selfCommonQueryBeanInnerByJSON(String key, HttpServletRequest request,
                                                         ConditionParse queryCondition,
                                                         CommonQuery query)
    {
        int size = 10;

        ConditionParse condtion = JSONPageSeparateTools.getCondition(request, key);

        if (condtion == null)
        {
            condtion = queryCondition;
        }

        if (JSONPageSeparateTools.isMemeryMode(request))
        {
            // 如果是从菜单第一次加载,清除缓存的条件
            if (JSONPageSeparateTools.isPageFirstInit(request, key))
            {
                JSONPageSeparateTools.removeParameterMap(request, key);
            }

            // 初始化
            if (JSONPageSeparateTools.isMemeryInit(request, key))
            {
                // 这里必须是处理后的condition，不能从session里面获得
                int total = query.getCount(key, request, queryCondition);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                JSONPageSeparateTools.initPageSeparate(queryCondition, page, request, key);
            }
            // 记忆分页
            else if (JSONPageSeparateTools.isMemery(request, key))
            {
                PageSeparate page = JSONPageSeparateTools.getPageSeparate(request, key);

                if (page == null)
                {
                    int total = query.getCount(key, request, condtion);

                    size = JSONTools.getPageSize(request);

                    page = new PageSeparate(total, size);

                    page.setNowPage(JSONTools.getNowPage(request));

                    JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
                }
                else
                {
                    int total = query.getCount(key, request, condtion);

                    page.setRowCount(total);
                }
            }
            // 改变页面大小
            else if (JSONPageSeparateTools.isChangePageSize(request, key))
            {
                int total = query.getCount(key, request, condtion);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
            }
            // 翻页
            else if (JSONPageSeparateTools.isPageTurning(request, key))
            {
                PageSeparate page = JSONPageSeparateTools.getPageSeparate(request, key);

                if (page != null)
                {
                    page.setNowPage(JSONTools.getNowPage(request));
                }
                else
                {
                    int total = query.getCount(key, request, condtion);

                    size = JSONTools.getPageSize(request);

                    page = new PageSeparate(total, size);

                    page.setNowPage(JSONTools.getNowPage(request));

                    JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
                }
            }
            // 刷新
            else if (JSONPageSeparateTools.isFlush(request, key))
            {
                int total = query.getCount(key, request, condtion);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
            }
            // 排序(需要初始化，但是不需要改变页面大小)
            else if (JSONPageSeparateTools.isSort(request, key))
            {
                // sortorder desc acs
                // sortname name
                int total = query.getCount(key, request, condtion);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                processOrder2(request, condtion, query.getSortname(request), query.getOrderPfix(
                    key, request));

                JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
            }
            else
            {
                int total = query.getCount(key, request, condtion);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
            }
        }
        // 处理init
        else
        {
            // 初始化
            if (JSONPageSeparateTools.isCommonInit(request, key))
            {
                // //这里必须是处理后的condition，不能从session里面获得
                int total = query.getCount(key, request, queryCondition);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                JSONPageSeparateTools.initPageSeparate(queryCondition, page, request, key);
            }
            // 改变页面大小
            else if (JSONPageSeparateTools.isChangePageSize(request, key))
            {
                int total = query.getCount(key, request, condtion);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
            }
            // 翻页
            else if (JSONPageSeparateTools.isPageTurning(request, key))
            {
                PageSeparate page = JSONPageSeparateTools.getPageSeparate(request, key);

                if (page != null)
                {
                    page.setNowPage(JSONTools.getNowPage(request));
                }
                else
                {
                    int total = query.getCount(key, request, condtion);

                    size = JSONTools.getPageSize(request);

                    page = new PageSeparate(total, size);

                    page.setNowPage(JSONTools.getNowPage(request));

                    JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
                }
            }
            // 刷新
            else if (JSONPageSeparateTools.isFlush(request, key))
            {
                int total = query.getCount(key, request, condtion);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
            }
            // 排序(需要初始化，但是不需要改变页面大小)
            else if (JSONPageSeparateTools.isSort(request, key))
            {
                // sortorder desc acs
                // sortname name
                int total = query.getCount(key, request, condtion);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                processOrder2(request, condtion, query.getSortname(request), query.getOrderPfix(
                    key, request));

                JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
            }
            else
            {
                int total = query.getCount(key, request, condtion);

                size = JSONTools.getPageSize(request);

                PageSeparate page = new PageSeparate(total, size);

                JSONPageSeparateTools.initPageSeparate(condtion, page, request, key);
            }
        }

        JSONPageSeparateTools.updateParameterMap(request, key);

        return query.queryResult(key, request, condtion);
    }

    /**
     * 自定义的查询
     * 
     * @param key
     * @param request
     * @param list
     * @param condtion
     * @param query
     * @return
     */
    public static String querySelfBeanByJSONAndToString(String key, HttpServletRequest request,
                                                        ConditionParse condtion, CommonQuery query)
    {
        List list = new ArrayList();

        list.addAll(selfCommonQueryBeanInnerByJSON(key, request, condtion, query));

        return JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request, key));
    }

    /**
     * 转到error
     * 
     * @param message
     * @param mapping
     * @param request
     * @return
     */
    public static ActionForward toError(String message, ActionMapping mapping,
                                        HttpServletRequest request)
    {
        request.setAttribute(KeyConstant.ERROR_MESSAGE, message);

        return mapping.findForward("error");
    }

    /**
     * 转到error
     * 
     * @param message(数据错误,请重新操作)
     * @param mapping
     * @param request
     * @return
     */
    public static ActionForward toError(ActionMapping mapping, HttpServletRequest request)
    {
        request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误,请重新操作");

        return mapping.findForward("error");
    }

    /**
     * toError
     * 
     * @param message
     * @param forward
     * @param mapping
     * @param request
     * @return
     */
    public static ActionForward toError(String message, String forward, ActionMapping mapping,
                                        HttpServletRequest request)
    {
        request.setAttribute(KeyConstant.ERROR_MESSAGE, message);

        return mapping.findForward(forward);
    }

    /**
     * isNullQuery
     * 
     * @param request
     * @param queryConfig
     * @param key
     * @return
     */
    public static boolean isNullQuery(HttpServletRequest request, QueryConfig queryConfig,
                                      String key)
    {
        QueryItemBean findQueryCondition = queryConfig.findQueryCondition(key);

        if (findQueryCondition == null)
        {
            return true;
        }

        List<QueryConditionBean> conditions = findQueryCondition.getConditions();

        for (QueryConditionBean queryConditionBean : conditions)
        {
            String parameter = request.getParameter(queryConditionBean.getName());

            if ( !StringTools.isNullOrNone(parameter))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * commonQueryInPageSeparate
     * 
     * @param <T>
     * @param <V>
     * @param cacheKey
     * @param request
     * @param dao
     * @param pageSize
     * @param handle
     * @return
     */
    public static <T extends Serializable, V extends Serializable> List<V> commonQueryInPageSeparate(
                                                                                                     String cacheKey,
                                                                                                     HttpServletRequest request,
                                                                                                     DAO<T, V> dao,
                                                                                                     int pageSize,
                                                                                                     HandleQueryCondition handle)
    {
        List<V> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            if (handle != null)
            {
                handle.setQueryCondition(request, condtion);
            }

            int total = dao.countVOByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, pageSize);

            PageSeparateTools.initPageSeparate(condtion, page, request, cacheKey);

            list = dao.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, cacheKey);

            list = dao.queryEntityVOsByCondition(PageSeparateTools.getCondition(request, cacheKey),
                PageSeparateTools.getPageSeparate(request, cacheKey));
        }

        request.setAttribute("beanList", list);

        request.setAttribute("cacheQueryKey", cacheKey);

        return list;
    }

    /**
     * commonQueryInPageSeparate
     * 
     * @param <T>
     * @param <V>
     * @param cacheKey
     * @param request
     * @param dao
     * @param handle
     * @return
     */
    public static <T extends Serializable, V extends Serializable> List<V> commonQueryInPageSeparate(
                                                                                                     String cacheKey,
                                                                                                     HttpServletRequest request,
                                                                                                     DAO<T, V> dao,

                                                                                                     HandleQueryCondition handle)
    {
        return commonQueryInPageSeparate(cacheKey, request, dao, 10, handle);
    }

}
