package com.sdg.cmdb.service.gitlabTest;

import org.gitlab.api.GitlabAPI;
import org.junit.AssumptionViolatedException;
import java.io.IOException;

/**
 * Token holder for integration tests.
 * <ul>
 *    <li>By throwing an {@link AssumptionViolatedException} when the host is not reachable,
 *   provoke skipping of tests.</li>
 *   <li>GitLab is strict about creating too much sessions for username/password.
 *   If you create too many sessions, you will receive HTTP/429 (Retry Later).</li>
 * </ul>
 */
public class APIForIntegrationTestingHolder {

    private static final String TEST_URL = "http://" + System.getProperty("docker.host.address", "10.10.10.10") + ":" + System.getProperty("gitlab.port", "80");

    public static APIForIntegrationTestingHolder INSTANCE = new APIForIntegrationTestingHolder();

    private Object api;

    private APIForIntegrationTestingHolder(){
        api = GitlabAPI.connect(TEST_URL, "aaaaa");
    }

    public GitlabAPI getApi() {
        if (api instanceof IOException) {
            throw new AssumptionViolatedException("GITLAB not running on '" + TEST_URL + "', skipping...", (IOException)api);
        }
        return (GitlabAPI)api;
    }
}
