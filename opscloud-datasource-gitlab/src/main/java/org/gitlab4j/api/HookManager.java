
package org.gitlab4j.api;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface provides a base class handler for processing GitLab Web Hook and System Hook callouts.
 */
public interface HookManager {

    /**
     * Get the secret token that received hook events should be validated against.
     *
     * @return the secret token that received hook events should be validated against
     */
    String getSecretToken();

    /**
     * Set the secret token that received hook events should be validated against.
     *
     * @param secretToken the secret token to verify against
     */
    void setSecretToken(String secretToken);

    /**
     * Validate the provided secret token against the reference secret token. Returns true if
     * the secret token is valid or there is no reference secret token to validate against,
     * otherwise returns false.
     * 
     * @param secretToken the token to validate
     * @return true if the secret token is valid or there is no reference secret token to validate against
     */
    default public boolean isValidSecretToken(String secretToken) {
        String ourSecretToken = getSecretToken();
        return (ourSecretToken == null ||
                ourSecretToken.trim().isEmpty() ||
                ourSecretToken.equals(secretToken));
    }

    /**
     * Validate the provided secret token found in the HTTP header against the reference secret token.
     * Returns true if the secret token is valid or there is no reference secret token to validate
     * against, otherwise returns false.
     * 
     * @param request the HTTP request to verify the secret token
     * @return true if the secret token is valid or there is no reference secret token to validate against
     */
    default public boolean isValidSecretToken(HttpServletRequest request) {

        if (getSecretToken() != null) {
            String secretToken = request.getHeader("X-Gitlab-Token");
            return (isValidSecretToken(secretToken));
        }

        return (true);
    }

    /**
     * Parses and verifies an Event instance from the HTTP request and
     * fires it off to the registered listeners.
     * 
     * @param request the HttpServletRequest to read the Event instance from
     * @throws GitLabApiException if the parsed event is not supported
     */
    public void handleEvent(HttpServletRequest request) throws GitLabApiException;
}