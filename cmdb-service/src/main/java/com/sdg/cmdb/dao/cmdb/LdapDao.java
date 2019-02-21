package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.ldap.LdapGroupDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LdapDao {

    int getLdapGroupSize(@Param("cn") String cn);

    List<LdapGroupDO> getLdapGroupPage(
            @Param("cn") String cn, @Param("groupType") int groupType,
            @Param("pageStart") int pageStart, @Param("pageLength") int pageLength);

    LdapGroupDO getLdapGroup(@Param("id") long id);

    LdapGroupDO getLdapGroupByCn(@Param("cn") String cn);

    int addLdapGroup(LdapGroupDO ldapGroupDO);

    int delLdapGroup(@Param("id") long id);

    int updateLdapGroup(LdapGroupDO ldapGroupDO);

    List<LdapGroupDO> getLdapGroupByWorkflow();

}
