package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;
import java.util.Date;

@Table(name = "users_user")
public class UsersUser {
    private String password;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "date_joined")
    private Date dateJoined;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String username;

    private String name;

    private String email;

    private String role;

    private String avatar;

    private String wechat;

    private String phone;

    @Column(name = "is_first_login")
    private Boolean isFirstLogin;

    @Column(name = "date_expired")
    private Date dateExpired;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "mfa_level")
    private Short mfaLevel;

    @Column(name = "otp_secret_key")
    private String otpSecretKey;

    private String source;

    @Column(name = "date_password_last_updated")
    private Date datePasswordLastUpdated;

    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "public_key")
    private String publicKey;

    private String comment;

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return last_login
     */
    public Date getLastLogin() {
        return lastLogin;
    }

    /**
     * @param lastLogin
     */
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * @return first_name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return last_name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return is_active
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return date_joined
     */
    public Date getDateJoined() {
        return dateJoined;
    }

    /**
     * @param dateJoined
     */
    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * @return wechat
     */
    public String getWechat() {
        return wechat;
    }

    /**
     * @param wechat
     */
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return is_first_login
     */
    public Boolean getIsFirstLogin() {
        return isFirstLogin;
    }

    /**
     * @param isFirstLogin
     */
    public void setIsFirstLogin(Boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    /**
     * @return date_expired
     */
    public Date getDateExpired() {
        return dateExpired;
    }

    /**
     * @param dateExpired
     */
    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }

    /**
     * @return created_by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return mfa_level
     */
    public Short getMfaLevel() {
        return mfaLevel;
    }

    /**
     * @param mfaLevel
     */
    public void setMfaLevel(Short mfaLevel) {
        this.mfaLevel = mfaLevel;
    }

    /**
     * @return otp_secret_key
     */
    public String getOtpSecretKey() {
        return otpSecretKey;
    }

    /**
     * @param otpSecretKey
     */
    public void setOtpSecretKey(String otpSecretKey) {
        this.otpSecretKey = otpSecretKey;
    }

    /**
     * @return source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return date_password_last_updated
     */
    public Date getDatePasswordLastUpdated() {
        return datePasswordLastUpdated;
    }

    /**
     * @param datePasswordLastUpdated
     */
    public void setDatePasswordLastUpdated(Date datePasswordLastUpdated) {
        this.datePasswordLastUpdated = datePasswordLastUpdated;
    }

    /**
     * @return private_key
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @return public_key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}