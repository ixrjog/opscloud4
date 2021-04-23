package com.baiyi.opscloud.ldap.repo;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.ldap.entry.Person;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2019/12/27 4:16 下午
 * @Version 1.0
 */
public interface PersonRepo {

    List<String> getAllPersonNames();

    List<Person> getPersonList();

    Person findPersonWithDn(String dn);

    BusinessWrapper<Boolean> create(Person person);

    BusinessWrapper<Boolean> update(Person person);

    BusinessWrapper<Boolean> delete(String username);

    Boolean checkPersonInLdap(String username);

    List<String> searchUserGroupByUsername(String username);

}
