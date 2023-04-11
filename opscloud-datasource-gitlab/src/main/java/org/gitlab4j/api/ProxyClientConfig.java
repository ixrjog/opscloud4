package org.gitlab4j.api;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.client.ClientProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides convenience methods to create ClientConfig properties so GitLabApi can use a proxy server.
 */
public class ProxyClientConfig {

    /**
     * Create a Map instance with properties set up to use a proxy server that can be passed to the 
     * GitLabAPi constructors and login methods to configure the GitLabApi instance to use a proxy server.
     *
     * @param proxyUri the URI of the proxy server
     * @return a Map set up to allow GitLabApi to use a proxy server
     */
    public static Map<String, Object> createProxyClientConfig(String proxyUri) {
        return (createProxyClientConfig(proxyUri, null, null));
    }

    /**
     * Create a Map instance set up to use a proxy server that can be passed to the GitLabAPi constructors 
     * and login methods to configure the GitLabApi instance to use a proxy server.
     *
     * @param proxyUri the URI of the proxy server
     * @param username the username for basic auth with the proxy server
     * @param password the password for basic auth with the proxy server
     * @return a Map set up to allow GitLabApi to use a proxy server
     */
    public static Map<String, Object> createProxyClientConfig(String proxyUri, String username, String password) {

        Map<String, Object> clientConfig = new HashMap<>();
        clientConfig.put(ClientProperties.PROXY_URI, proxyUri);

        if (username != null && username.trim().length() > 0) {
            clientConfig.put(ClientProperties.PROXY_USERNAME, username);
        }

        if (password != null && password.trim().length() > 0) {
            clientConfig.put(ClientProperties.PROXY_PASSWORD, password);
        }

        return (clientConfig);
    }

    /**
     * Create a Map instance set up to use an NTLM proxy server that can be passed to the GitLabAPi constructors 
     * and login methods to configure the GitLabApi instance to use an NTLM proxy server.
     *
     * @param proxyUri the URI of the proxy server
     * @param username the user name. This should not include the domain to authenticate with.
     * For example: "user" is correct whereas "DOMAIN&#92;user" is not.
     * @param password the password
     * @param workstation the workstation the authentication request is originating from. Essentially, the computer name for this machine.
     * @param domain the domain to authenticate within
     * @return a Map set up to allow GitLabApi to use an NTLM proxy server
     */
    public static Map<String, Object> createNtlmProxyClientConfig(String proxyUri, String username, String password, String workstation, String domain) {

        Map<String, Object> clientConfig = new HashMap<>();
        clientConfig.put(ClientProperties.PROXY_URI, proxyUri);

        CredentialsProvider credentials = new BasicCredentialsProvider();
        credentials.setCredentials(AuthScope.ANY, new NTCredentials(username, password, workstation, domain));
        clientConfig.put(ApacheClientProperties.CREDENTIALS_PROVIDER, credentials);

        return (clientConfig);
    }
}
