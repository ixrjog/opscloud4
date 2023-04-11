package org.gitlab4j.api;

import org.gitlab4j.api.models.License;

import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class provides an entry point to all the GitLab API license calls.
 * @see <a href="https://docs.gitlab.com/ce/api/license.html">License API</a>
 */
public class LicenseApi extends AbstractApi {

    public LicenseApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Retrieve information about the current license.
     *
     * <pre><code>GitLab Endpoint: GET /license</code></pre>
     *
     * @return a License instance holding info about the current license
     * @throws GitLabApiException if any exception occurs
     */
    public License getLicense() throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "license");
        return (response.readEntity(License.class));
    }

    /**
     * Retrieve information about the current license as the value of an Optional.
     *
     * <pre><code>GitLab Endpoint: GET /license</code></pre>
     *
     * @return the current license as the value of an Optional.
     */
    public Optional<License> getOptionalLicense() {
        try {
            return (Optional.ofNullable(getLicense()));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Retrieve information about all licenses.
     *
     * <pre><code>GitLab Endpoint: GET /licenses</code></pre>
     *
     * @return a List of License instances
     * @throws GitLabApiException if any exception occurs
     */
    public List<License> getAllLicenses() throws GitLabApiException {
        return (getAllLicenses(getDefaultPerPage()).all());
    }

    /**
     * Get a Stream of all licenses.
     *
      <pre><code>GitLab Endpoint: GET /licenses</code></pre>
     *
     * @return a Stream of License instances
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<License> getAllLicensesStream() throws GitLabApiException {
        return (getAllLicenses(getDefaultPerPage()).stream());
    }

    /**
     * Get a Pager of all licenses.
     *
     * <pre><code>GitLab Endpoint: GET /licenses</code></pre>
     *
     * @param itemsPerPage the number of LicenseTemplate instances that will be
     *                     fetched per page
     * @return a Pager of license template
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<License> getAllLicenses(int itemsPerPage) throws GitLabApiException {
        return (new Pager<License>(this, License.class, itemsPerPage, null, "licenses"));
    }

    /**
     * Add a new license.
     *
     * <pre><code>GitLab Endpoint: POST /license</code></pre>
     *
     * @param licenseString the license string for the license
     * @return a License instance for the added license
     * @throws GitLabApiException if any exception occurs
     */
    public License addLicense(String licenseString) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("license", licenseString, true);
        Response response = post(Response.Status.CREATED, formData, "license");
        return (response.readEntity(License.class));
    }

    /**
     * Deletes a license.
     *
     * <pre><code>GitLab Endpoint: DELETE /license/:id</code></pre>
     *
     * @param licenseId the ID of the license to delete
     * @return a License instance for the delete license
     * @throws GitLabApiException if any exception occurs
     */
    public License deleteLicense(Long licenseId) throws GitLabApiException {
        Response response = delete(Response.Status.OK, null, "license", licenseId);
        return (response.readEntity(License.class));
    }
}