/**
 * File Name: QueryTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.common.ConditionParse;
import com.china.center.common.JSONPageSeparateTools;
import com.china.center.common.KeyConstant;
import com.china.center.common.PageSeparateTools;
import com.china.center.common.query.CommonQuery;
import com.china.center.common.query.HandleResult;
import com.china.center.common.query.QueryConditionBean;
import com.china.center.common.query.QueryConfig;
import com.china.center.common.query.QueryItemBean;
import com.china.center.jdbc.inter.DAO;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.constant.OAConstant;


/**
 * QueryTools
 * 
 * @author zhuzhu
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
                                                 ConditionParse condtion)
    {
        if ( !JSONPageSeparateTools.isLoad(request))
        {
            return;
        }

        Map<String, String> map = CommonTools.saveParamersToMap(request);

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
        int size = OAConstant.PAGE_SIZE;

        if (pagesize == null || pagesize.length == 0)
        {
            size = OAConstant.PAGE_SIZE;
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
            int total = dao.countVOByCondition(PageSeparateTools.getCondition(request, key).toString());

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
        int size = OAConstant.PAGE_SIZE;

        if (pagesize == null || pagesize.length == 0)
        {
            size = OAConstant.PAGE_SIZE;
        }
        else
        {
            size = pagesize[0];
        }

        list.addAll(commonQueryBeanInner(key, request, condtion, dao, size));
    }

    public static void queryBeanByJSON(String key, HttpServletRequest request, List list,
                                       ConditionParse condtion, DAO dao)
    {
        list.addAll(commonQueryBeanInnerByJSON(key, request, condtion, dao, true));
    }

    public static void queryVOByJSON(String key, HttpServletRequest request, List list,
                                     ConditionParse condtion, DAO dao)
    {
        list.addAll(commonQueryBeanInnerByJSON(key, request, condtion, dao, false));
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

    public static <T extends Serializable, V extends Serializable> String queryVOByJSONAndToString(
                                                                                                   String key,
                                                                                                   HttpServletRequest request,
                                                                                                   ConditionParse condtion,
                                                                                                   DAO<T, V> dao,
                                                                                                   HandleResult<V> handle)
    {
        List<V> list = new ArrayList<V>();

        list.addAll((List<V>)commonQueryBeanInnerByJSON(key, request, condtion, dao, false));

        for (V object : list)
        {
            handle.handle(object);
        }

        return JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request, key));
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
            int total = dao.countByCondition(PageSeparateTools.getCondition(request, key).toString());

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
        int size = OAConstant.PAGE_SIZE;

        ConditionParse condtion = JSONPageSeparateTools.getCondition(request, key);

        if (condtion == null)
        {
            condtion = queryCondition;
        }

        if (JSONPageSeparateTools.isMemeryMode(request))
        {
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

}
