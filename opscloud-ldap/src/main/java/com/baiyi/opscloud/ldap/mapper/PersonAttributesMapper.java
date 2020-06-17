package com.baiyi.opscloud.ldap.mapper;


import com.baiyi.opscloud.ldap.entry.Person;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * @Author baiyi
 * @Date 2019/12/27 2:30 下午
 * @Version 1.0
 */
public class PersonAttributesMapper implements AttributesMapper<Person> {
    /**
     * Map Attributes to an object. The supplied attributes are the attributes
     * from a single SearchResult.
     *
     * @param attrs attributes from a SearchResult.
     * @return an object built from the attributes.
     * @throws NamingException if any error occurs mapping the attributes
     */
    @Override
    public Person mapFromAttributes(Attributes attrs) throws NamingException {
        Person person = new Person();
        person.setUsername((String)attrs.get("cn").get());
        try{
            person.setDisplayName((String)attrs.get("displayName").get());
        }catch (NullPointerException e){
            person.setDisplayName(person.getUsername());
        }
        try{
            person.setMobile((String)attrs.get("mobile").get());
        }catch (NullPointerException e){
        }
        try{
            person.setEmail((String)attrs.get("mail").get());
        }catch (NullPointerException e){
        }
        try{
            person.setJobNo((String)attrs.get("jobNo").get());
        }catch (NullPointerException e){
        }
        return person;
    }
}
