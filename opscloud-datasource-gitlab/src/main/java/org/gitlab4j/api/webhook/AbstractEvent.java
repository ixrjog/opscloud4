package org.gitlab4j.api.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractEvent implements Event {

    private String requestUrl;
    private String requestQueryString;
    private String secretToken;

    @Override
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Override
    @JsonIgnore
    public String getRequestUrl() {
        return (requestUrl);
    }

    @Override
    public void setRequestQueryString(String requestQueryString) {
        this.requestQueryString = requestQueryString;
    }

    @Override
    @JsonIgnore
    public String getRequestQueryString() {
        return (requestQueryString);
    }

    @Override
    public void setRequestSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    @Override
    @JsonIgnore
    public String getRequestSecretToken() {
        return (secretToken);
    }
}
