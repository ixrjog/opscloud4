package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class GpgSignature {

    private Long gpgKeyId;
    private String gpgKeyPrimaryKeyid;
    private String gpgKeyUserName;
    private String gpgKeyUserEmail;
    private String verificationStatus;
    private String gpgKeySubkeyId;

    public Long getGpgKeyId() {
        return gpgKeyId;
    }

    public void setGpgKeyId(Long gpgKeyId) {
        this.gpgKeyId = gpgKeyId;
    }

    public String getGpgKeyPrimaryKeyid() {
        return gpgKeyPrimaryKeyid;
    }

    public void setGpgKeyPrimaryKeyid(String gpgKeyPrimaryKeyid) {
        this.gpgKeyPrimaryKeyid = gpgKeyPrimaryKeyid;
    }

    public String getGpgKeyUserName() {
        return gpgKeyUserName;
    }

    public void setGpgKeyUserName(String gpgKeyUserName) {
        this.gpgKeyUserName = gpgKeyUserName;
    }

    public String getGpgKeyUserEmail() {
        return gpgKeyUserEmail;
    }

    public void setGpgKeyUserEmail(String gpgKeyUserEmail) {
        this.gpgKeyUserEmail = gpgKeyUserEmail;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getGpgKeySubkeyId() {
        return gpgKeySubkeyId;
    }

    public void setGpgKeySubkeyId(String gpgKeySubkeyId) {
        this.gpgKeySubkeyId = gpgKeySubkeyId;
    }

    @Override
    public String toString() {
	return (JacksonJson.toJsonString(this));
    }
}
