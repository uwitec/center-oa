/**
 * File Name: LocationManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.annosql.constant.AnoConstant;
import com.china.center.common.MYException;
import com.china.center.oa.constant.PublicConstant;
import com.china.center.oa.customer.dao.AssignApplyDAO;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.dao.StafferVSCustomerDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.CityBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.dao.CityDAO;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.LocationVSCityDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.vo.LocationVO;
import com.china.center.oa.publics.vo.LocationVSCityVO;
import com.china.center.oa.publics.vs.LocationVSCityBean;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;


/**
 * LocationManager
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see LocationManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "locationManager")
public class LocationManager
{
    private LocationDAO locationDAO = null;

    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private CustomerDAO customerDAO = null;

    private AssignApplyDAO assignApplyDAO = null;

    private LocationVSCityDAO locationVSCityDAO = null;

    private StafferVSCustomerDAO stafferVSCustomerDAO = null;

    private CityDAO cityDAO = null;

    public LocationManager()
    {}

    @Transactional(rollbackFor = {MYException.class})
    public boolean addBean(User user, LocationBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAddBean(bean);

        bean.setId(commonDAO2.getSquenceString());

        locationDAO.saveEntityBean(bean);

        return true;

    }

    /**
     * addLocationVSCity
     * 
     * @param user
     * @param list
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean addLocationVSCity(User user, String locationId,
                                                  List<LocationVSCityBean> list)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, list);

        List<LocationVSCityBean> oldList = locationVSCityDAO.queryEntityBeansByFK(locationId);

        locationVSCityDAO.deleteEntityBeansByFK(locationId);

        // 对于客户的分公司属性的更新(先更新到总部)
        customerDAO.updateCustomerLocation(locationId, PublicConstant.CENTER_LOCATION);

        for (LocationVSCityBean locationVSCityBean : list)
        {
            locationVSCityBean.setLocationId(locationId);

            if (StringTools.isNullOrNone(locationVSCityBean.getCityId()))
            {
                continue;
            }

            int count = locationVSCityDAO.countByFK(locationVSCityBean.getCityId(),
                AnoConstant.FK_FIRST);

            CityBean city = cityDAO.find(locationVSCityBean.getCityId());

            if (city == null)
            {
                throw new MYException("数据错误请重新操作");
            }

            if (count > 0)
            {
                throw new MYException("区域[%s]已经被其他分公司使用", city.getName());
            }

            if (StringTools.isNullOrNone(locationVSCityBean.getProvinceId()))
            {
                locationVSCityBean.setProvinceId(city.getParentId());
            }

            locationVSCityDAO.saveEntityBean(locationVSCityBean);

            // 更新区域下客户
            customerDAO.updateCustomerLocationByCity(locationVSCityBean.getCityId(), locationId);
        }

        // 本次删除的地市
        List<LocationVSCityBean> delCity = analyseDelCity(oldList, list);

        handleDelCity(delCity);

        return true;
    }

    /**
     * 处理删除地市(主要是拓展客户的转移，要吐出来)
     * 
     * @param delCity
     */
    private void handleDelCity(List<LocationVSCityBean> delCity)
    {
        // NOTE zhuzhu 区域迁移核心的逻辑处理
        for (LocationVSCityBean locationVSCityBean2 : delCity)
        {
            // 更新拓展客户的申请状态
            customerDAO.updateCityCustomerToInit(locationVSCityBean2.getCityId());

            // 更新客户的新旧属性为新客户,因为关系剥离后客户的新旧属性肯定是新的
            stafferVSCustomerDAO.updateNewByCityId(locationVSCityBean2.getCityId());

            // 删除存在的客户关系
            stafferVSCustomerDAO.delVSByCityId(locationVSCityBean2.getCityId());

            // 更新申请中的拓展客户的申请状态
            customerDAO.updateApplyCityCustomerToInit(locationVSCityBean2.getCityId());

            // 删除申请
            assignApplyDAO.delAssignByCityId(locationVSCityBean2.getCityId());
        }
    }

    /**
     * 分析del的地市
     * 
     * @param oldList
     * @param newlist
     * @return
     */
    private List<LocationVSCityBean> analyseDelCity(List<LocationVSCityBean> oldList,
                                                    List<LocationVSCityBean> newlist)
    {
        List<LocationVSCityBean> result = new ArrayList<LocationVSCityBean>();

        for (LocationVSCityBean locationVSCityBean : oldList)
        {
            if ( !newlist.contains(locationVSCityBean))
            {
                result.add(locationVSCityBean);
            }
        }

        return result;
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String locationId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, locationId);

        checkDelBean(locationId);

        locationDAO.deleteEntityBean(locationId);

        locationVSCityDAO.deleteEntityBeansByFK(locationId);

        return true;
    }

    /**
     * findVO
     * 
     * @param id
     * @return
     * @throws MYException
     */
    public LocationVO findVO(String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        LocationVO vo = locationDAO.findVO(id);

        if (vo == null)
        {
            return null;
        }

        List<LocationVSCityVO> city = locationVSCityDAO.queryEntityVOsByFK(id);

        vo.setCityVOs(city);

        List<LocationVSCityBean> acity = new ArrayList<LocationVSCityBean>();

        for (LocationVSCityVO locationVSCityVO : city)
        {
            acity.add(locationVSCityVO);
        }

        vo.setCitys(acity);

        return vo;
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(LocationBean bean)
        throws MYException
    {
        if (locationDAO.countByUnique(bean.getName()) > 0)
        {
            throw new MYException("分公司名称已经存在");
        }

        if (locationDAO.countCode(bean.getCode()) > 0)
        {
            throw new MYException("分公司标识已经存在");
        }
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkDelBean(String locationId)
        throws MYException
    {
        if (PublicConstant.VIRTUAL_LOCATION.equals(locationId))
        {
            throw new MYException("虚拟区域不能删除");
        }

        // 存在人员或者user
        if (userDAO.countByLocationId(locationId) > 0)
        {
            throw new MYException("分公司下存在登录用户");
        }

        // stafferDAO
        if (stafferDAO.countByLocationId(locationId) > 0)
        {
            throw new MYException("分公司下存在注册职员");
        }
    }

    /**
     * @return the locationDAO
     */
    public LocationDAO getLocationDAO()
    {
        return locationDAO;
    }

    /**
     * @param locationDAO
     *            the locationDAO to set
     */
    public void setLocationDAO(LocationDAO locationDAO)
    {
        this.locationDAO = locationDAO;
    }

    /**
     * @return the commonDAO2
     */
    public CommonDAO2 getCommonDAO2()
    {
        return commonDAO2;
    }

    /**
     * @param commonDAO2
     *            the commonDAO2 to set
     */
    public void setCommonDAO2(CommonDAO2 commonDAO2)
    {
        this.commonDAO2 = commonDAO2;
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    /**
     * @return the stafferDAO
     */
    public StafferDAO getStafferDAO()
    {
        return stafferDAO;
    }

    /**
     * @param stafferDAO
     *            the stafferDAO to set
     */
    public void setStafferDAO(StafferDAO stafferDAO)
    {
        this.stafferDAO = stafferDAO;
    }

    /**
     * @return the locationVSCityDAO
     */
    public LocationVSCityDAO getLocationVSCityDAO()
    {
        return locationVSCityDAO;
    }

    /**
     * @param locationVSCityDAO
     *            the locationVSCityDAO to set
     */
    public void setLocationVSCityDAO(LocationVSCityDAO locationVSCityDAO)
    {
        this.locationVSCityDAO = locationVSCityDAO;
    }

    /**
     * @return the cityDAO
     */
    public CityDAO getCityDAO()
    {
        return cityDAO;
    }

    /**
     * @param cityDAO
     *            the cityDAO to set
     */
    public void setCityDAO(CityDAO cityDAO)
    {
        this.cityDAO = cityDAO;
    }

    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    /**
     * @param customerDAO
     *            the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
    }

    /**
     * @return the stafferVSCustomerDAO
     */
    public StafferVSCustomerDAO getStafferVSCustomerDAO()
    {
        return stafferVSCustomerDAO;
    }

    /**
     * @param stafferVSCustomerDAO
     *            the stafferVSCustomerDAO to set
     */
    public void setStafferVSCustomerDAO(StafferVSCustomerDAO stafferVSCustomerDAO)
    {
        this.stafferVSCustomerDAO = stafferVSCustomerDAO;
    }

    /**
     * @return the assignApplyDAO
     */
    public AssignApplyDAO getAssignApplyDAO()
    {
        return assignApplyDAO;
    }

    /**
     * @param assignApplyDAO
     *            the assignApplyDAO to set
     */
    public void setAssignApplyDAO(AssignApplyDAO assignApplyDAO)
    {
        this.assignApplyDAO = assignApplyDAO;
    }
}
