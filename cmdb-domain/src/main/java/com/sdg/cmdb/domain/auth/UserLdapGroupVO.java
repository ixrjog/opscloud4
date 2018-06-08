package com.sdg.cmdb.domain.auth;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liangjian on 2017/8/9.
 */
public class UserLdapGroupVO implements Serializable {

    private static final long serialVersionUID = 1621385723154928545L;
    private int confluenceUsers = 0;

    private int confluenceAdministrators = 0;

    private int confluenceAdmin = 0;

    private int jiraUsers = 0;

    private int jiraAdministrators = 0;

    private int stashAdmin = 0;

    private int crowdAdmin = 0;

    private int administrators = 0;

    private int nexusDeveloper = 0;

    private int nexusAdministrators = 0;

    public void setLdapGroup(LdapGroupVO ldapGroupVO) {
        switch (ldapGroupVO.getType()) {
            case (1):
                confluenceUsers = 1;
                break;
            case (2):
                confluenceAdministrators = 1;
                break;
            case (3):
                confluenceAdmin = 1;
                break;
            case (4):
                jiraUsers = 1;
                break;
            case (5):
                jiraAdministrators = 1;
                break;
            case (6):
                stashAdmin = 1;
                break;
            case (7):
                crowdAdmin = 1;
                break;
            case (8):
                administrators = 1;
                break;
            case (9):
                nexusDeveloper = 1;
                break;
            case (10):
                nexusAdministrators = 1;
                break;
        }
    }

    public int getConfluenceUsers() {
        return confluenceUsers;
    }

    public void setConfluenceUsers(int confluenceUsers) {
        this.confluenceUsers = confluenceUsers;
    }

    public int getConfluenceAdministrators() {
        return confluenceAdministrators;
    }

    public void setConfluenceAdministrators(int confluenceAdministrators) {
        this.confluenceAdministrators = confluenceAdministrators;
    }

    public int getConfluenceAdmin() {
        return confluenceAdmin;
    }

    public void setConfluenceAdmin(int confluenceAdmin) {
        this.confluenceAdmin = confluenceAdmin;
    }

    public int getJiraUsers() {
        return jiraUsers;
    }

    public void setJiraUsers(int jiraUsers) {
        this.jiraUsers = jiraUsers;
    }

    public int getJiraAdministrators() {
        return jiraAdministrators;
    }

    public void setJiraAdministrators(int jiraAdministrators) {
        this.jiraAdministrators = jiraAdministrators;
    }

    public int getStashAdmin() {
        return stashAdmin;
    }

    public void setStashAdmin(int stashAdmin) {
        this.stashAdmin = stashAdmin;
    }

    public int getCrowdAdmin() {
        return crowdAdmin;
    }

    public void setCrowdAdmin(int crowdAdmin) {
        this.crowdAdmin = crowdAdmin;
    }

    public int getAdministrators() {
        return administrators;
    }

    public void setAdministrators(int administrators) {
        this.administrators = administrators;
    }

    public int getNexusAdministrators() {
        return nexusAdministrators;
    }

    public void setNexusAdministrators(int nexusAdministrators) {
        this.nexusAdministrators = nexusAdministrators;
    }

    public int getNexusDeveloper() {
        return nexusDeveloper;
    }

    public void setNexusDeveloper(int nexusDeveloper) {
        this.nexusDeveloper = nexusDeveloper;
    }
}
