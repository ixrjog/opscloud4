package org.gitlab4j.api.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.GitLabApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class uses HTML scraping to create and revoke GitLab personal access tokens,
 * the user's Feed token, and for fetching the current Health Check access token.
 *
 * <p>NOTE: This relies on HTML scraping and has been tested on GitLab-CE 11.0.0 to 11.10.1 for
 *          proper functionality.  It may not work on earlier or later versions.</p>
 */
public final class AccessTokenUtils {

    /**
     * This enum defines the available scopes for a personal access token.
     */
    public enum Scope {

        /**
         * Grants complete access to the API and Container Registry (read/write) (introduced in GitLab 8.15).
         */
        API,

        /**
         * Allows to read (pull) container registry images if a project is private and
         * authorization is required (introduced in GitLab 9.3).  If the GitLab server you
         * are using does not have the Registry properly configured, using this scope will
         * result in an exception.
         */
        READ_REGISTRY,

        /**
         * Allows read-only access (pull) to the repository through git clone.
         */
        READ_REPOSITORY,

        /**
         * Allows access to the read-only endpoints under /users. Essentially, any of the GET
         * requests in the Users API are allowed (introduced in GitLab 8.15).
         */
        READ_USER,

        /**
         * Allows performing API actions as any user in the system,
         * if the authenticated user is an admin (introduced in GitLab 10.2).
         */
        SUDO,

        /**
         * Grants read-write access to repositories on private projects using Git-over-HTTP (not using the API).
         */
        WRITE_REPOSITORY;

        private static JacksonJsonEnumHelper<Scope> enumHelper = new JacksonJsonEnumHelper<>(Scope.class);

        @JsonCreator
        public static Scope forValue(String value) {
            return enumHelper.forValue(value);
        }

        @JsonValue
        public String toValue() {
            return (enumHelper.toString(this));
        }

        @Override
        public String toString() {
            return (enumHelper.toString(this));
        }
    }

    protected static final String USER_AGENT = "GitLab4J Client";
    protected static final String COOKIES_HEADER = "Set-Cookie";

    protected static final String NEW_USER_AUTHENTICITY_TOKEN_REGEX = "\"new_user\".*name=\\\"authenticity_token\\\"\\svalue=\\\"([^\\\"]*)\\\".*new_new_user";
    protected static final Pattern NEW_USER_AUTHENTICITY_TOKEN_PATTERN = Pattern.compile(NEW_USER_AUTHENTICITY_TOKEN_REGEX);

    protected static final String AUTHENTICITY_TOKEN_REGEX = "name=\\\"authenticity_token\\\"\\svalue=\\\"([^\\\"]*)\\\"";
    protected static final Pattern AUTHENTICITY_TOKEN_PATTERN = Pattern.compile(AUTHENTICITY_TOKEN_REGEX);

    protected static final String PERSONAL_ACCESS_TOKEN_REGEX = "name=\\\"created-personal-access-token\\\".*data-clipboard-text=\\\"([^\\\"]*)\\\".*\\/>";
    protected static final Pattern PERSONAL_ACCESS_TOKEN_PATTERN = Pattern.compile(PERSONAL_ACCESS_TOKEN_REGEX);

    protected static final String REVOKE_PERSONAL_ACCESS_TOKEN_REGEX = "href=\\\"([^\\\"]*)\\\"";
    protected static final Pattern REVOKE_PERSONAL_ACCESS_TOKEN_PATTERN = Pattern.compile(REVOKE_PERSONAL_ACCESS_TOKEN_REGEX);

    protected static final String FEED_TOKEN_REGEX = "name=\\\"feed_token\\\".*value=\\\"([^\\\"]*)\\\".*\\/>";
    protected static final Pattern FEED_TOKEN_PATTERN = Pattern.compile(FEED_TOKEN_REGEX);

    protected static final String HEALTH_CHECK_ACCESS_TOKEN_REGEX = "id=\"health-check-token\">([^<]*)<\\/code>";
    protected static final Pattern HEALTH_CHECK_ACCESS_TOKEN_PATTERN = Pattern.compile(HEALTH_CHECK_ACCESS_TOKEN_REGEX);

    /**
     * Create a GitLab personal access token with the provided configuration.
     *
     * @param baseUrl the GitLab server base URL
     * @param username the user name to create the personal access token for
     * @param password the password of the user to create the personal access token for
     * @param tokenName the name for the new personal access token
     * @param scopes an array of scopes for the new personal access token
     * @return the created personal access token
     * @throws GitLabApiException if any exception occurs
     */
    public static final String createPersonalAccessToken(final String baseUrl, final String username,
            final String password, final String tokenName, final Scope[] scopes) throws GitLabApiException {

        if (scopes == null || scopes.length == 0) {
            throw new RuntimeException("scopes cannot be null or empty");
        }

        return (createPersonalAccessToken(baseUrl, username, password, tokenName, Arrays.asList(scopes)));
    }

    /**
     * Create a GitLab personal access token with the provided configuration.
     *
     * @param baseUrl the GitLab server base URL
     * @param username the user name to create the personal access token for
     * @param password the password of the user to create the personal access token for
     * @param tokenName the name for the new personal access token
     * @param scopes a List of scopes for the new personal access token
     * @return the created personal access token
     * @throws GitLabApiException if any exception occurs
     */
    public static final String createPersonalAccessToken(final String baseUrl, final String username,
            final String password, final String tokenName, final List<Scope> scopes) throws GitLabApiException {

        // Save the follow redirect state so it can be restored later
        boolean savedFollowRedirects = HttpURLConnection.getFollowRedirects();
        String cookies = null;

        try {

            // Must manually follow redirects
            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(false);
            }

            /*******************************************************************************
             * Step 1: Login and get the session cookie.                                   *
             *******************************************************************************/
            cookies = login(baseUrl, username, password);

            /*******************************************************************************
             * Step 2: Go to the /profile/personal_access_tokens page to fetch a           *
             * new authenticity token.                                                     *
             *******************************************************************************/
            String urlString = baseUrl + "/profile/personal_access_tokens";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Make sure the response code is 200, otherwise there is a failure
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new GitLabApiException("Failure loading Access Tokens page, aborting!");
            }

            String content = getContent(connection);
            Matcher matcher = AUTHENTICITY_TOKEN_PATTERN.matcher(content);
            if (!matcher.find()) {
                throw new GitLabApiException("authenticity_token not found, aborting!");
            }

            String csrfToken = matcher.group(1);

            /*******************************************************************************
             * Step 3: Submit the /profile/personalccess_tokens page with the info to      *
             * create a new personal access token.                                         *
             *******************************************************************************/
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            StringBuilder formData = new StringBuilder();
            addFormData(formData, "authenticity_token", csrfToken);
            addFormData(formData, "personal_access_token[name]", tokenName);
            addFormData(formData, "personal_access_token[expires_at]", "");

            if (scopes != null && scopes.size() > 0) {
                for (Scope scope : scopes) {
                    addFormData(formData, "personal_access_token[scopes][]", scope.toString());
                }
            }

            connection.setRequestProperty("Content-Length", String.valueOf(formData.length()));
            OutputStream output = connection.getOutputStream();
            output.write(formData.toString().getBytes());
            output.flush();
            output.close();

            // Make sure a redirect was provided, otherwise there is a failure
            responseCode = connection.getResponseCode();
            if (responseCode != 302) {
                throw new GitLabApiException("Failure creating personal access token, aborting!");
            }

            // Follow the redirect with the provided session cookie
            String redirectUrl = connection.getHeaderField("Location");
            url = new URL(redirectUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Make sure the response code is 200, otherwise there is a failure
            responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new GitLabApiException("Failure creating personal access token, aborting!");
            }

            // Extract the personal access token from the page and return it
            content = getContent(connection);
            matcher = PERSONAL_ACCESS_TOKEN_PATTERN.matcher(content);
            if (!matcher.find()) {
                throw new GitLabApiException("created-personal-access-token not found, aborting!");
            }

            String personalAccessToken = matcher.group(1);
            return (personalAccessToken);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        } finally {

            if (cookies != null) {
                try { logout(baseUrl, cookies); } catch (Exception ignore) {}
            }

            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(true);
            }
        }
    }

    /**
     * Revoke the first matching GitLab personal access token.
     *
     * @param baseUrl the GitLab server base URL
     * @param username the user name to revoke the personal access token for
     * @param password the password of the user to revoke the personal access token for
     * @param tokenName the name of the personal access token to revoke
     * @param scopes an array of scopes of the personal access token to revoke
     * @throws GitLabApiException if any exception occurs
     */
    public static final void revokePersonalAccessToken(final String baseUrl, final String username,
            final String password, final String tokenName, final Scope[] scopes) throws GitLabApiException {

        if (scopes == null || scopes.length == 0) {
            throw new RuntimeException("scopes cannot be null or empty");
        }

        revokePersonalAccessToken(baseUrl, username, password, tokenName, Arrays.asList(scopes));
    }

    /**
     * Revoke the first matching GitLab personal access token.
     *
     * @param baseUrl the GitLab server base URL
     * @param username the user name to revoke the personal access token for
     * @param password the password of the user to revoke the personal access token for
     * @param tokenName the name of the personal access token to revoke
     * @param scopes a List of scopes of the personal access token to revoke
     * @throws GitLabApiException if any exception occurs
     */
    public static final void revokePersonalAccessToken(final String baseUrl, final String username,
            final String password, final String tokenName, final List<Scope> scopes) throws GitLabApiException {

        // Save the follow redirect state so it can be restored later
        boolean savedFollowRedirects = HttpURLConnection.getFollowRedirects();
        String cookies = null;

        try {

            // Must manually follow redirects
            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(false);
            }

            /*******************************************************************************
             * Step 1: Login and get the session cookie.                                   *
             *******************************************************************************/
            cookies = login(baseUrl, username, password);

            /*******************************************************************************
             * Step 2: Go to the /profile/personal_access_tokens page and fetch the        *
             *         authenticity token.                                                 *
             *******************************************************************************/
            String urlString = baseUrl + "/profile/personal_access_tokens";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Make sure the response code is 200, otherwise there is a failure
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new GitLabApiException("Failure loading Access Tokens page, aborting!");
            }

            String content = getContent(connection);
            Matcher matcher = AUTHENTICITY_TOKEN_PATTERN.matcher(content);
            if (!matcher.find()) {
                throw new GitLabApiException("authenticity_token not found, aborting!");
            }

            String csrfToken = matcher.group(1);

            /*******************************************************************************
             * Step 3: Submit the /profile/personal_access_tokens page with the info to    *
             * revoke the first matching personal access token.                            *
             *******************************************************************************/
            int indexOfTokenName = content.indexOf("<td>" + tokenName + "</td>");
            if (indexOfTokenName == -1) {
                throw new GitLabApiException("personal access token not found, aborting!");
            }

            content = content.substring(indexOfTokenName);
            int indexOfLinkEnd = content.indexOf("</a>");
            if (indexOfTokenName == -1) {
                throw new GitLabApiException("personal access token not found, aborting!");
            }

            content = content.substring(0, indexOfLinkEnd);
            String scopesText = "";
            if (scopes != null && scopes.size() > 0) {
                final StringJoiner joiner = new StringJoiner(", ");
                scopes.forEach(s -> joiner.add(s.toString()));
                scopesText = joiner.toString();
            }

            if (content.indexOf(scopesText) == -1) {
                throw new GitLabApiException("personal access token not found, aborting!");
            }

            matcher = REVOKE_PERSONAL_ACCESS_TOKEN_PATTERN.matcher(content);
            if (!matcher.find()) {
                throw new GitLabApiException("personal access token not found, aborting!");
            }

            String revokePath = matcher.group(1);
            url = new URL(baseUrl + revokePath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Submit the form
            StringBuilder formData = new StringBuilder();
            addFormData(formData, "authenticity_token", csrfToken);
            connection.setRequestProperty("Content-Length", String.valueOf(formData.length()));
            OutputStream output = connection.getOutputStream();
            output.write(formData.toString().getBytes());
            output.flush();
            output.close();

            // Make sure a redirect was provided, otherwise there is a failure
            responseCode = connection.getResponseCode();
            if (responseCode != 302) {
                throw new GitLabApiException("Failure revoking personal access token, aborting!");
            }

            // Follow the redirect with the provided session cookie
            String redirectUrl = connection.getHeaderField("Location");
            url = new URL(redirectUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Make sure the response code is 200, otherwise there is a failure
            responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new GitLabApiException("Failure revoking personal access token, aborting!");
            }

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        } finally {

            if (cookies != null) {
                try { logout(baseUrl, cookies); } catch (Exception ignore) {}
            }

            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(true);
            }
        }
    }

    /**
     * Fetches the user's GitLab Feed token using HTML scraping.
     *
     * @param baseUrl the GitLab server base URL
     * @param username the user name the user to log in with
     * @param password the password of the provided username
     * @return the fetched Feed token
     * @throws GitLabApiException if any exception occurs
     */
    public static final String getFeedToken(final String baseUrl, final String username,
            final String password) throws GitLabApiException {

        // Save the follow redirect state so it can be restored later
        boolean savedFollowRedirects = HttpURLConnection.getFollowRedirects();
        String cookies = null;

        try {

            // Must manually follow redirects
            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(false);
            }

            /*******************************************************************************
             * Step 1: Login and get the session cookie. *
             *******************************************************************************/
            cookies = login(baseUrl, username, password);

            /*******************************************************************************
             * Step 2: Go to the /profile/personal_access_tokens page and fetch the        *
             *         Feed token.                                                         *
             *******************************************************************************/
            String urlString = baseUrl + "/profile/personal_access_tokens";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Make sure the response code is 200, otherwise there is a failure
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new GitLabApiException("Failure loading Access Tokens page, aborting!");
            }

            // Extract the Feed token from the page and return it
            String content = getContent(connection);
            Matcher matcher = FEED_TOKEN_PATTERN.matcher(content);
            if (!matcher.find()) {
                throw new GitLabApiException("Feed token not found, aborting!");
            }

            String feedToken = matcher.group(1);
            return (feedToken);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        } finally {

            if (cookies != null) {
                try { logout(baseUrl, cookies); } catch (Exception ignore) {}
            }

            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(true);
            }
        }
    }

    /**
     * Fetches the GitLab health check access token using HTML scraping.
     *
     * @param baseUrl the GitLab server base URL
     * @param username the user name of an admin user to log in with
     * @param password the password of the provided username
     * @return the fetched health check access token
     * @throws GitLabApiException if any exception occurs
     */
    public static final String getHealthCheckAccessToken(final String baseUrl, final String username,
            final String password) throws GitLabApiException {

        // Save the follow redirect state so it can be restored later
        boolean savedFollowRedirects = HttpURLConnection.getFollowRedirects();
        String cookies = null;

        try {

            // Must manually follow redirects
            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(false);
            }

            /*******************************************************************************
             * Step 1: Login and get the session cookie. *
             *******************************************************************************/
            cookies = login(baseUrl, username, password);

            /*******************************************************************************
             * Step 2: Go to the /admin/health_check page and fetch the * health check
             * access token. *
             *******************************************************************************/
            String urlString = baseUrl + "/admin/health_check";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Make sure the response code is 200, otherwise there is a failure
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new GitLabApiException("Failure loading Health Check page, aborting!");
            }

            // Extract the personal access token from the page and return it
            String content = getContent(connection);
            Matcher matcher = HEALTH_CHECK_ACCESS_TOKEN_PATTERN.matcher(content);
            if (!matcher.find()) {
                throw new GitLabApiException("health-check-access-token not found, aborting!");
            }

            String healthCheckAccessToken = matcher.group(1);
            return (healthCheckAccessToken);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        } finally {

            if (cookies != null) {
                try { logout(baseUrl, cookies); } catch (Exception ignore) {}
            }

            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(true);
            }
        }
    }

    /**
     * Gets a GitLab session cookie by logging in the specified user.
     *
     * @param baseUrl the GitLab server base URL
     * @param username the user name to to login for
     * @param password the password of the user to login for
     * @return the GitLab seesion token as a cookie value
     * @throws GitLabApiException if any error occurs
     */
    protected static final String login(final String baseUrl, final String username,
            final String password) throws GitLabApiException {

        // Save the follow redirect state so it can be restored later
        boolean savedFollowRedirects = HttpURLConnection.getFollowRedirects();

        try {

            // Must manually follow redirects
            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(false);
            }

            /*******************************************************************************
             * Step 1: Go to the login page to get a session cookie and an athuicity_token *
             *******************************************************************************/
            String urlString = baseUrl + "/users/sign_in";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Make sure a redirect was provided, otherwise it is a login failure
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new GitLabApiException("Invalid state, aborting!");
            }

            // Get the session cookie from the headers
            String[] cookieParts = connection.getHeaderField(COOKIES_HEADER).split(";");
            String cookies = cookieParts[0];

            // Extract the authenticity token from the content, need this to submit the
            // login form
            String content = getContent(connection);
            Matcher matcher = NEW_USER_AUTHENTICITY_TOKEN_PATTERN.matcher(content);
            if (!matcher.find()) {
                throw new GitLabApiException("authenticity_token not found, aborting!");
            }

            String csrfToken = matcher.group(1);

            /*******************************************************************************
             * Step 2: Submit the login form wih the session cookie and authenticity_token *
             * fetching a new session cookie along the way.                                *
             *******************************************************************************/
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");

            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            StringBuilder formData = new StringBuilder();
            addFormData(formData, "user[login]", username);
            addFormData(formData, "user[password]", password);
            addFormData(formData, "authenticity_token", csrfToken);
            connection.setRequestProperty("Content-Length", String.valueOf(formData.length()));

            OutputStream output = connection.getOutputStream();
            output.write(formData.toString().getBytes());
            output.flush();
            output.close();

            // Make sure a redirect was provided, otherwise it is a login failure
            responseCode = connection.getResponseCode();
            if (responseCode != 302) {
                throw new GitLabApiException("Login failure, aborting!", 401);
            }

            cookieParts = connection.getHeaderField(COOKIES_HEADER).split(";");
            cookies = cookieParts[0];

            // Follow the redirect with the provided session cookie
            String redirectUrl = connection.getHeaderField("Location");
            url = new URL(redirectUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Cookie", cookies);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // The response code should be 200, otherwise something is wrong, consider it a login failure
            responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new GitLabApiException("Login failure, aborting!", 401);
            }

            return (cookies);

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        } finally {
            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(true);
            }
        }
    }

    /**
     * Logs out the user associated with the GitLab session cookie.
     *
     * @param baseUrl the GitLab server base URL
     * @param cookies the GitLab session cookie to logout
     * @throws GitLabApiException if any error occurs
     */
    protected static final void logout(final String baseUrl, final String cookies) throws GitLabApiException {

        // Save so it can be restored later
        boolean savedFollowRedirects = HttpURLConnection.getFollowRedirects();

        try {

            // Must manually follow redirects
            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(false);
            }

            String urlString = baseUrl + "/users/sign_out";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Cookie", cookies);
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Make sure a redirect was provided, otherwise it is a logout failure
            int responseCode = connection.getResponseCode();
            if (responseCode != 302) {
                throw new GitLabApiException("Logout failure, aborting!");
            }

        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        } finally {
            if (savedFollowRedirects) {
                HttpURLConnection.setFollowRedirects(true);
            }
        }
    }

    /**
     * Adds the specified form param to the form data StringBuilder.  If the provided formData is null,
     * will create the StringBuilder instance first.
     *
     * @param formData the StringBuilder instance holding the form data, if null will create the StringBuilder
     * @param name the form param name
     * @param value the form param value
     * @return the form data StringBuilder
     * @throws GitLabApiException if any error occurs.
     */
    public static final StringBuilder addFormData(StringBuilder formData, String name, String value) throws GitLabApiException {

        if (formData == null) {
            formData = new StringBuilder();
        } else if (formData.length() > 0) {
            formData.append("&");
        }

        formData.append(name);
        formData.append("=");
        try {
            formData.append(URLEncoder.encode(value, "UTF-8"));
            return (formData);
        } catch (Exception e) {
            throw new GitLabApiException(e);
        }
    }

    /**
     * Reads and returns the content from the provided URLConnection.
     *
     * @param connection the URLConnection to read the content from
     * @return the read content as a String
     * @throws GitLabApiException if any error occurs
     */
    protected static String getContent(URLConnection connection) throws GitLabApiException {

        StringBuilder buf = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            reader.lines().forEach(b -> buf.append(b));
        } catch (IOException ioe) {
            throw new GitLabApiException(ioe);
        }

        return (buf.toString());
    }
}
