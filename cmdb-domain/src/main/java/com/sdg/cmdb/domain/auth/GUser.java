package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

public class GUser implements Serializable{


    public GUser(){

    }


    public GUser(String name,String mail){
        this.userVO = new UserVO();
        this.userVO.setDisplayName(name);
        String[] s=mail.split("@");

        this.userVO.setUsername(s[0]);
        this.userVO.setUserpassword(s[0]);
        this.userVO.setMail(mail);
    }

    public GUser(String name,String username,String mobile){

        this.userVO = new UserVO();
        this.userVO.setDisplayName(name);
        this.userVO.setUsername(username);
        this.userVO.setMail(username + "@gegejia.com");
        this.userVO.setUserpassword(mobile);
        this.userVO.setMobile(mobile);

    }

    private UserVO userVO;

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    private static final long serialVersionUID = 7252457069691396840L;


}
