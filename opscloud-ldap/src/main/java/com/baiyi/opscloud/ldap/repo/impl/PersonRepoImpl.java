package com.baiyi.opscloud.ldap.repo.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.ldap.config.LdapConfig;
import com.baiyi.opscloud.ldap.entry.Person;
import com.baiyi.opscloud.ldap.handler.LdapHandler;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author baiyi
 * @Date 2019/12/27 5:37 下午
 * @Version 1.0
 */
@Slf4j
@Component("PersonRepo")
public class PersonRepoImpl implements PersonRepo {

    @Resource
    private LdapConfig ldapConfig;

    @Resource
    private LdapHandler ldapHandler;

    /**
     * 查询部分字段集合
     *
     * @return
     */
    @Override
    public List<String> getAllPersonNames() {
        return ldapHandler.queryPersonNameList();
    }

    /**
     * 查询对象映射集合
     *
     * @return
     */
    @Override
    public List<Person> getPersonList() {
        return ldapHandler.queryPersonList();
    }

    /**
     * 根据DN查询指定人员信息
     *
     * @param dn
     * @return
     */
    @Override
    public Person findPersonWithDn(String dn) {
        return ldapHandler.getPersonWithDn(dn);
    }

    @Override
    public BusinessWrapper<Boolean> create(Person person) {
        if (ldapHandler.bindPerson(person)) {
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.ACCOUNT_CREATE_ERROR);
        }
    }

    @Override
    public BusinessWrapper<Boolean> update(Person person) {
        if (ldapHandler.updatePerson(person)) {
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.ACCOUNT_UPDATE_ERROR);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delete(String username) {
        try {
            ldapHandler.unbind(ldapConfig.buildUserDN(username));
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorEnum.ACCOUNT_UNBIND_ERROR);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public Boolean checkPersonInLdap(String username) {
        return ldapHandler.checkPersonInLdap(username);
    }

    @Override
    public List<String> searchUserGroupByUsername(String username) {
        return ldapHandler.searchLdapGroup(username);
    }


}
