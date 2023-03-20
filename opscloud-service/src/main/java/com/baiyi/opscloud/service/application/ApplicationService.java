package com.baiyi.opscloud.service.application;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/12 11:48 上午
 * @Version 1.0
 */
public interface ApplicationService {

    List<Application> queryAll();

    DataTable<Application> queryPageByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

    DataTable<Application> queryPageByParam(ApplicationParam.ApplicationPageQuery pageQuery);

    Application getById(Integer id);

    Application getByKey(String applicationKey);

    Application getByName(String name);

    void add(Application application);

    void update(Application application);

    void updateByPrimaryKeySelective(Application application);

    void deleteById(Integer id);

    int countWithReport();

}
