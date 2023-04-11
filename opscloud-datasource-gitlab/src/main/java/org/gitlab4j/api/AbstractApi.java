package org.gitlab4j.api;

import org.gitlab4j.api.GitLabApi.ApiVersion;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Label;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.User;
import org.gitlab4j.api.utils.UrlEncoder;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * This class is the base class for all the sub API classes. It provides implementations of
 * delete(), get(), post() and put() that are re-used by all the sub-classes.
 */
public abstract class AbstractApi implements Constants {

    protected final GitLabApi gitLabApi;

    public AbstractApi(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }

    /**
     * Returns the project ID or path from the provided Integer, String, or Project instance.
     *
     * @param obj the object to determine the ID or path from
     * @return the project ID or path from the provided Long, String, or Project instance
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Object getProjectIdOrPath(Object obj) throws GitLabApiException {

        if (obj == null) {
            throw (new RuntimeException("Cannot determine ID or path from null object"));
        } else if (obj instanceof Long) {
            return (obj);
        } else if (obj instanceof String) {
            return (urlEncode(((String) obj).trim()));
        } else if (obj instanceof Project) {

            Long id = ((Project) obj).getId();
            if (id != null && id.longValue() > 0) {
                return (id);
            }

            String path = ((Project) obj).getPathWithNamespace();
            if (path != null && path.trim().length() > 0) {
                return (urlEncode(path.trim()));
            }

            throw (new RuntimeException("Cannot determine ID or path from provided Project instance"));

        } else {
            throw (new RuntimeException("Cannot determine ID or path from provided " + obj.getClass().getSimpleName() +
                    " instance, must be Long, String, or a Project instance"));
        }
    }

    /**
     * Returns the group ID or path from the provided Integer, String, or Group instance.
     *
     * @param obj the object to determine the ID or path from
     * @return the group ID or path from the provided Long, String, or Group instance
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Object getGroupIdOrPath(Object obj) throws GitLabApiException {

        if (obj == null) {
            throw (new RuntimeException("Cannot determine ID or path from null object"));
        } else if (obj instanceof Long) {
            return (obj);
        } else if (obj instanceof String) {
            return (urlEncode(((String) obj).trim()));
        } else if (obj instanceof Group) {

            Long id = ((Group) obj).getId();
            if (id != null && id.longValue() > 0) {
                return (id);
            }

            String path = ((Group) obj).getFullPath();
            if (path != null && path.trim().length() > 0) {
                return (urlEncode(path.trim()));
            }

            throw (new RuntimeException("Cannot determine ID or path from provided Group instance"));

        } else {
            throw (new RuntimeException("Cannot determine ID or path from provided " + obj.getClass().getSimpleName() +
                    " instance, must be Long, String, or a Group instance"));
        }
    }

    /**
     * Returns the user ID or path from the provided Integer, String, or User instance.
     *
     * @param obj the object to determine the ID or username from
     * @return the user ID or username from the provided Integer, String, or User instance
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Object getUserIdOrUsername(Object obj) throws GitLabApiException {

        if (obj == null) {
            throw (new RuntimeException("Cannot determine ID or username from null object"));
        } else if (obj instanceof Long) {
            return (obj);
        } else if (obj instanceof String) {
            return (urlEncode(((String) obj).trim()));
        } else if (obj instanceof User) {

            Long id = ((User) obj).getId();
            if (id != null && id.longValue() > 0) {
                return (id);
            }

            String username = ((User) obj).getUsername();
            if (username != null && username.trim().length() > 0) {
                return (urlEncode(username.trim()));
            }

            throw (new RuntimeException("Cannot determine ID or username from provided User instance"));

        } else {
            throw (new RuntimeException("Cannot determine ID or username from provided " + obj.getClass().getSimpleName() +
                    " instance, must be Integer, String, or a User instance"));
        }
    }

    /**
     * Returns the label ID or name from the provided Integer, String, or Label instance.
     *
     * @param obj the object to determine the ID or name from
     * @return the user ID or name from the provided Integer, String, or Label instance
     * @throws GitLabApiException if any exception occurs during execution
     */
    public Object getLabelIdOrName(Object obj) throws GitLabApiException {

        if (obj == null) {
            throw (new RuntimeException("Cannot determine ID or name from null object"));
        } else if (obj instanceof Long) {
            return (obj);
        } else if (obj instanceof String) {
            return (urlEncode(((String) obj).trim()));
        } else if (obj instanceof Label) {

            Long id = ((Label) obj).getId();
            if (id != null && id.longValue() > 0) {
                return (id);
            }

            String name = ((Label) obj).getName();
            if (name != null && name.trim().length() > 0) {
                return (urlEncode(name.trim()));
            }

            throw (new RuntimeException("Cannot determine ID or name from provided Label instance"));

        } else {
            throw (new RuntimeException("Cannot determine ID or name from provided " + obj.getClass().getSimpleName() +
                    " instance, must be Integer, String, or a Label instance"));
        }
    }

    protected ApiVersion getApiVersion() {
        return (gitLabApi.getApiVersion());
    }

    protected boolean isApiVersion(ApiVersion apiVersion) {
        return (gitLabApi.getApiVersion() == apiVersion);
    }

    protected int getDefaultPerPage() {
        return (gitLabApi.getDefaultPerPage());
    }

    protected GitLabApiClient getApiClient() {
        return (gitLabApi.getApiClient());
    }

    /**
     * Encode a string to be used as in-path argument for a gitlab api request.
     *
     * Standard URL encoding changes spaces to plus signs, but for arguments that are part of the path,
     * like the :file_path in a "Get raw file" request, gitlab expects spaces to be encoded with %20.
     *
     * @param s the string to encode
     * @return encoded version of s with spaces encoded as %2F
     * @throws GitLabApiException if encoding throws an exception
     */
    protected String urlEncode(String s) throws GitLabApiException {
        return (UrlEncoder.urlEncode(s));
    }

    /**
     * Perform an HTTP GET call with the specified query parameters and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response get(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().get(queryParams, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP GET call with the specified query parameters and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param accepts if non-empty will set the Accepts header to this value
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response getWithAccepts(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, String accepts, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().getWithAccepts(queryParams, accepts, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP GET call with the specified query parameters and URL, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param url the fully formed path to the GitLab API endpoint
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response get(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, URL url) throws GitLabApiException {
        try {
            return validate(getApiClient().get(queryParams, url), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP HEAD call with the specified query parameters and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response head(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().head(queryParams, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP PATCH call with the specified query parameters and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response patch(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().patch(queryParams, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP PATCH call with the specified query parameters and URL, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param url the fully formed path to the GitLab API endpoint
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response patch(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, URL url) throws GitLabApiException {
        try {
            return validate(getApiClient().patch(queryParams, url), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP POST call with the specified form data and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param formData the Form containing the name/value pairs for the POST data
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response post(Response.Status expectedStatus, Form formData, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().post(formData, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP POST call with the specified payload object and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param payload the object instance that will be serialized to JSON and used as the POST data
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response post(Response.Status expectedStatus, Object payload, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().post(payload, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP POST call with the specified payload object and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param stream the StreamingOutput that will be used for the POST data
     * @param mediaType the content-type for the streamed data
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response post(Response.Status expectedStatus, StreamingOutput stream, String mediaType, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().post(stream, mediaType, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP POST call with the specified form data and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response post(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().post(queryParams, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP POST call with the specified form data and URL, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param formData the Form containing the name/value pairs for the POST data
     * @param url the fully formed path to the GitLab API endpoint
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response post(Response.Status expectedStatus, Form formData, URL url) throws GitLabApiException {
        try {
            return validate(getApiClient().post(formData, url), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform a file upload with the specified File instance and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param name the name for the form field that contains the file name
     * @param fileToUpload a File instance pointing to the file to upload
     * @param mediaType unused; will be removed in the next major version
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response upload(Response.Status expectedStatus, String name, File fileToUpload, String mediaType, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().upload(name, fileToUpload, mediaType, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    protected Response upload(Response.Status expectedStatus, String name, InputStream inputStream, String filename, String mediaType, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().upload(name, inputStream, filename, mediaType, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform a file upload with the specified File instance and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param name the name for the form field that contains the file name
     * @param fileToUpload a File instance pointing to the file to upload
     * @param mediaType unused; will be removed in the next major version
     * @param url the fully formed path to the GitLab API endpoint
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response upload(Response.Status expectedStatus, String name, File fileToUpload, String mediaType, URL url) throws GitLabApiException {
        try {
            return validate(getApiClient().upload(name, fileToUpload, mediaType, url), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform a file upload with the specified File instance and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param name the name for the form field that contains the file name
     * @param fileToUpload a File instance pointing to the file to upload
     * @param mediaType unused; will be removed in the next major version
     * @param formData the Form containing the name/value pairs
     * @param url the fully formed path to the GitLab API endpoint
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response upload(Response.Status expectedStatus, String name, File fileToUpload, String mediaType, Form formData, URL url) throws GitLabApiException {

        try {
            return validate(getApiClient().upload(name, fileToUpload, mediaType, formData, url), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP PUT call with the specified form data and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response put(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().put(queryParams, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP PUT call with the specified form data and URL, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param url the fully formed path to the GitLab API endpoint
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response put(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, URL url) throws GitLabApiException {
        try {
            return validate(getApiClient().put(queryParams, url), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP PUT call with the specified payload object and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param payload the object instance that will be serialized to JSON and used as the PUT data
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response put(Response.Status expectedStatus, Object payload, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().put(payload, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP PUT call with the specified form data and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param formData the Form containing the name/value pairs for the POST data
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response putWithFormData(Response.Status expectedStatus, Form formData, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().put(formData, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }


    /**
     * Perform a file upload using the HTTP PUT method with the specified File instance and path objects,
     * returning a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param name the name for the form field that contains the file name
     * @param fileToUpload a File instance pointing to the file to upload
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response putUpload(Response.Status expectedStatus, String name, File fileToUpload, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().putUpload(name, fileToUpload, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform a file upload using the HTTP PUT method with the specified File instance and path objects,
     * returning a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param name the name for the form field that contains the file name
     * @param fileToUpload a File instance pointing to the file to upload
     * @param url the fully formed path to the GitLab API endpoint
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response putUpload(Response.Status expectedStatus, String name, File fileToUpload, URL url) throws GitLabApiException {
        try {
            return validate(getApiClient().putUpload(name, fileToUpload, url), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP DELETE call with the specified form data and path objects, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param pathArgs variable list of arguments used to build the URI
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response delete(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, Object... pathArgs) throws GitLabApiException {
        try {
            return validate(getApiClient().delete(queryParams, pathArgs), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Perform an HTTP DELETE call with the specified form data and URL, returning
     * a ClientResponse instance with the data returned from the endpoint.
     *
     * @param expectedStatus the HTTP status that should be returned from the server
     * @param queryParams multivalue map of request parameters
     * @param url the fully formed path to the GitLab API endpoint
     * @return a ClientResponse instance with the data returned from the endpoint
     * @throws GitLabApiException if any exception occurs during execution
     */
    protected Response delete(Response.Status expectedStatus, MultivaluedMap<String, String> queryParams, URL url) throws GitLabApiException {
        try {
            return validate(getApiClient().delete(queryParams, url), expectedStatus);
        } catch (Exception e) {
            throw handle(e);
        }
    }

    /**
     * Convenience method for adding query and form parameters to a get() or post() call.
     *
     * @param formData the Form containing the name/value pairs
     * @param name the name of the field/attribute to add
     * @param value the value of the field/attribute to add
     */
    protected void addFormParam(Form formData, String name, Object value) throws IllegalArgumentException {
        addFormParam(formData, name, value, false);
    }

    /**
     * Convenience method for adding query and form parameters to a get() or post() call.
     * If required is true and value is null, will throw an IllegalArgumentException.
     *
     * @param formData the Form containing the name/value pairs
     * @param name the name of the field/attribute to add
     * @param value the value of the field/attribute to add
     * @param required the field is required flag
     * @throws IllegalArgumentException if a required parameter is null or empty
     */
    protected void addFormParam(Form formData, String name, Object value, boolean required) throws IllegalArgumentException {

        if (value == null) {

            if (required) {
                throw new IllegalArgumentException(name + " cannot be empty or null");
            }

            return;
        }

        String stringValue = value.toString();
        if (required && stringValue.trim().length() == 0) {
            throw new IllegalArgumentException(name + " cannot be empty or null");
        }

        formData.param(name, stringValue);
    }

    /**
     * Validates response the response from the server against the expected HTTP status and
     * the returned secret token, if either is not correct will throw a GitLabApiException.
     *
     * @param response response
     * @param expected expected response status
     * @return original response if the response status is expected
     * @throws GitLabApiException if HTTP status is not as expected, or the secret token doesn't match
     */
    protected Response validate(Response response, Response.Status expected) throws GitLabApiException {

        int responseCode = response.getStatus();
        int expectedResponseCode = expected.getStatusCode();

        if (responseCode != expectedResponseCode) {

            // If the expected code is 200-204 and the response code is 200-204 it is OK.  We do this because
            // GitLab is constantly changing the expected code in the 200 to 204 range
            if (expectedResponseCode > 204 || responseCode > 204 || expectedResponseCode < 200 || responseCode < 200) {
                throw new GitLabApiException(response);
            }
        }

        if (!getApiClient().validateSecretToken(response)) {
            throw new GitLabApiException(new NotAuthorizedException("Invalid secret token in response."));
        }

        return (response);
    }

    /**
     * Wraps an exception in a GitLabApiException if needed.
     *
     * @param thrown the exception that should be wrapped
     * @return either the untouched GitLabApiException or a new GitLabApiExceptin wrapping a non-GitLabApiException
     */
    protected GitLabApiException handle(Exception thrown) {

        if (thrown instanceof GitLabApiException) {
            return ((GitLabApiException) thrown);
        }

        return (new GitLabApiException(thrown));
    }

    /**
     * Creates a MultivaluedMap instance containing the "per_page" param.
     *
     * @param perPage the number of projects per page
     * @return a MultivaluedMap instance containing the "per_page" param
     */
    protected MultivaluedMap<String, String> getPerPageQueryParam(int perPage) {
        return (new GitLabApiForm().withParam(PER_PAGE_PARAM, perPage).asMap());
    }

    /**
     * Creates a MultivaluedMap instance containing "page" and "per_page" params.
     *
     * @param page the page to get
     * @param perPage the number of projects per page
     * @return a MultivaluedMap instance containing "page" and "per_page" params
     */
    protected MultivaluedMap<String, String> getPageQueryParams(int page, int perPage) {
        return (new GitLabApiForm().withParam(PAGE_PARAM, page).withParam(PER_PAGE_PARAM, perPage).asMap());
    }

    /**
     * Creates a MultivaluedMap instance containing "page" and "per_page" params.
     *
     * @param page the page to get
     * @param perPage the number of projects per page
     * @param customAttributesEnabled enables customAttributes for this query
     * @return a MultivaluedMap instance containing "page" and "per_page" params
     */
    protected MultivaluedMap<String, String> getPageQueryParams(int page, int perPage, boolean customAttributesEnabled) {

        GitLabApiForm form = new GitLabApiForm().withParam(PAGE_PARAM, page).withParam(PER_PAGE_PARAM, perPage);
        if (customAttributesEnabled) {
            return (form.withParam("with_custom_attributes", true).asMap());
        }

       return (form.asMap());
    }

    /**
     * Creates a MultivaluedMap instance containing the "per_page" param with the default value.
     *
     * @return a MultivaluedMap instance containing the "per_page" param with the default value
     */
    protected MultivaluedMap<String, String> getDefaultPerPageParam() {
       return (new GitLabApiForm().withParam(PER_PAGE_PARAM, getDefaultPerPage()).asMap());
    }

    /**
     * Creates a MultivaluedMap instance containing the "per_page" param with the default value.
     *
     * @param customAttributesEnabled enables customAttributes for this query
     * @return a MultivaluedMap instance containing the "per_page" param with the default value
     */
    protected MultivaluedMap<String, String> getDefaultPerPageParam(boolean customAttributesEnabled) {

        GitLabApiForm form = new GitLabApiForm().withParam(PER_PAGE_PARAM, getDefaultPerPage());
        if (customAttributesEnabled) {
            return (form.withParam("with_custom_attributes", true).asMap());
        }

        return (form.asMap());
    }
}
