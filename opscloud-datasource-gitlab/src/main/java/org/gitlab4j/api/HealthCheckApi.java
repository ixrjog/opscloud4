package org.gitlab4j.api;

import org.gitlab4j.api.models.HealthCheckInfo;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;

public class HealthCheckApi extends AbstractApi {

    public HealthCheckApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get Health Checks from the liveness endpoint.
     *
     * Requires ip_whitelist, see the following link for more info:
     * See <a href="https://docs.gitlab.com/ee/administration/monitoring/ip_whitelist.html">https://docs.gitlab.com/ee/administration/monitoring/ip_whitelist.html</a>
     *
     * <pre><code>GitLab Endpoint: GET /-/liveness</code></pre>
     *
     * @return HealthCheckInfo instance
     * @throws GitLabApiException if any exception occurs
     */
    public HealthCheckInfo getLiveness() throws GitLabApiException {
        return (getLiveness(null));
    }

    /**
     * Get Health Checks from the liveness endpoint.
     *
     * <pre><code>GitLab Endpoint: GET /-/liveness</code></pre>
     *
     * @param token Health Status token
     * @return HealthCheckInfo instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated
     */
    public HealthCheckInfo getLiveness(String token) throws GitLabApiException {
        try {
            URL livenessUrl = getApiClient().getUrlWithBase("-", "liveness");
            GitLabApiForm formData = new GitLabApiForm().withParam("token", token, false);
            Response response = get(Response.Status.OK, formData.asMap(), livenessUrl);
            return (response.readEntity(HealthCheckInfo.class));
        } catch (IOException ioe) {
            throw (new GitLabApiException(ioe));
        }
    }

    /**
     * Get Health Checks from the readiness endpoint.
     *
     * Requires ip_whitelist, see the following link for more info:
     * See <a href="https://docs.gitlab.com/ee/administration/monitoring/ip_whitelist.html">https://docs.gitlab.com/ee/administration/monitoring/ip_whitelist.html</a>
     *
     * <pre><code>GitLab Endpoint: GET /-/readiness</code></pre>
     *
     * @return HealthCheckInfo instance
     * @throws GitLabApiException if any exception occurs
     */
    public HealthCheckInfo getReadiness() throws GitLabApiException {
        return (getReadiness(null));
    }

    /**
     * Get Health Checks from the readiness endpoint.
     *
     * <pre><code>GitLab Endpoint: GET /-/readiness</code></pre>
     *
     * @param token Health Status token
     * @return HealthCheckInfo instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated
     */
    public HealthCheckInfo getReadiness(String token) throws GitLabApiException {
        try {
            URL readinessUrl = getApiClient().getUrlWithBase("-", "readiness");
            GitLabApiForm formData = new GitLabApiForm().withParam("token", token, false);
            Response response = get(Response.Status.OK, formData.asMap(), readinessUrl);
            return (response.readEntity(HealthCheckInfo.class));
        } catch (IOException ioe) {
            throw (new GitLabApiException(ioe));
        }
    }
}
