package org.gitlab4j.api;

import org.gitlab4j.api.GitLabApi.ApiVersion;
import org.gitlab4j.api.models.*;
import org.gitlab4j.api.models.ImpersonationToken.Scope;
import org.gitlab4j.api.utils.EmailChecker;

import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * This class provides an entry point to all the GitLab API users calls.
 *
 * @see <a href="https://docs.gitlab.com/ce/api/users.html">Users API at GitLab</a>
 */
public class UserApi extends AbstractApi {

    private boolean customAttributesEnabled = false;

    public UserApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Enables custom attributes to be returned when fetching User instances.
     */
    public void enableCustomAttributes() {
        customAttributesEnabled = true;
    }

    /**
     * Disables custom attributes to be returned when fetching User instances.
     */
    public void disableCustomAttributes() {
        customAttributesEnabled = false;
    }

    /**
     * <p>Get a list of users.</p>
     *
     * <strong>WARNING:</strong> Do not use this method to fetch users from https://gitlab.com,
     * gitlab.com has many 1,000,000's of users and it will a long time to fetch all of them.
     * Instead use {@link #getUsers(int itemsPerPage)} which will return a Pager of Group instances.
     *
     * <pre><code>GitLab Endpoint: GET /users</code></pre>
     *
     * @return a list of Users
     * @throws GitLabApiException if any exception occurs
     */
    public List<User> getUsers() throws GitLabApiException {

        String url = this.gitLabApi.getGitLabServerUrl();
        if (url.startsWith("https://gitlab.com")) {
            GitLabApi.getLogger().warning("Fetching all users from " + url +
                    " may take many minutes to complete, use Pager<User> getUsers(int) instead.");
        }

        return (getUsers(getDefaultPerPage()).all());
    }

    /**
     * Get a list of users using the specified page and per page settings.
     *
     * <pre><code>GitLab Endpoint: GET /users</code></pre>
     *
     * @param page    the page to get
     * @param perPage the number of users per page
     * @return the list of Users in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<User> getUsers(int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage, customAttributesEnabled), "users");
        return (response.readEntity(new GenericType<List<User>>() {}));
    }

    /**
     * Get a Pager of users.
     *
     * <pre><code>GitLab Endpoint: GET /users</code></pre>
     *
     * @param itemsPerPage the number of User instances that will be fetched per page
     * @return a Pager of User
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<User> getUsers(int itemsPerPage) throws GitLabApiException {
        return (new Pager<User>(this, User.class, itemsPerPage, createGitLabApiForm().asMap(), "users"));
    }

    /**
     * Get a Stream of users.
     *
     * <pre><code>GitLab Endpoint: GET /users</code></pre>
     *
     * @return a Stream of Users.
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<User> getUsersStream() throws GitLabApiException {
        return (getUsers(getDefaultPerPage()).stream());
    }

    /**
     * Get a list of active users
     *
     * <pre><code>GitLab Endpoint: GET /users?active=true</code></pre>
     *
     * @return a list of active Users
     * @throws GitLabApiException if any exception occurs
     */
    public List<User> getActiveUsers() throws GitLabApiException {
        return (getActiveUsers(getDefaultPerPage()).all());
    }

    /**
     * Get a list of active users using the specified page and per page settings.
     *
     * <pre><code>GitLab Endpoint: GET /users?active=true</code></pre>
     *
     * @param page the page to get
     * @param perPage the number of users per page
     * @return the list of active Users in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<User> getActiveUsers(int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = createGitLabApiForm()
                .withParam("active", true)
                .withParam(PAGE_PARAM, page)
                .withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "users");
        return (response.readEntity(new GenericType<List<User>>() {}));
    }

    /**
     * Get a Pager of active users.
     *
     * <pre><code>GitLab Endpoint: GET /users?active=true</code></pre>
     *
     * @param itemsPerPage the number of active User instances that will be fetched per page
     * @return a Pager of active User
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<User> getActiveUsers(int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = createGitLabApiForm().withParam("active", true);
        return (new Pager<User>(this, User.class, itemsPerPage, formData.asMap(), "users"));
    }

    /**
     * Get a Stream of active users
     *
     * <pre><code>GitLab Endpoint: GET /users?active=true</code></pre>
     *
     * @return a Stream of active Users
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<User> getActiveUsersStream() throws GitLabApiException {
        return (getActiveUsers(getDefaultPerPage()).stream());
    }

    /**
     * Blocks the specified user. Available only for admin.
     *
     * <pre><code>GitLab Endpoint: POST /users/:id/block</code></pre>
     *
     * @param userId the ID of the user to block
     * @throws GitLabApiException if any exception occurs
     */
    public void blockUser(Long userId) throws GitLabApiException {
        if (userId == null) {
            throw new RuntimeException("userId cannot be null");
        }

        if (isApiVersion(ApiVersion.V3)) {
            put(Response.Status.CREATED, null, "users", userId, "block");
        } else {
            post(Response.Status.CREATED, (Form) null, "users", userId, "block");
        }
    }

    /**
     * Unblocks the specified user. Available only for admin.
     *
     * <pre><code>GitLab Endpoint: POST /users/:id/unblock</code></pre>
     *
     * @param userId the ID of the user to unblock
     * @throws GitLabApiException if any exception occurs
     */
    public void unblockUser(Long userId) throws GitLabApiException {

        if (userId == null) {
            throw new RuntimeException("userId cannot be null");
        }

        if (isApiVersion(ApiVersion.V3)) {
            put(Response.Status.CREATED, null, "users", userId, "unblock");
        } else {
            post(Response.Status.CREATED, (Form) null, "users", userId, "unblock");
        }
    }

    /**
     * Get a list of blocked users.
     *
     * <pre><code>GitLab Endpoint: GET /users?blocked=true</code></pre>
     *
     * @return a list of blocked Users
     * @throws GitLabApiException if any exception occurs
     */
    public List<User> getBlockedUsers() throws GitLabApiException {
        return (getBlockedUsers(getDefaultPerPage()).all());
    }

    /**
     * Get a list of blocked users using the specified page and per page settings.
     *
     * <pre><code>GitLab Endpoint: GET /users?blocked=true</code></pre>
     *
     * @param page    the page to get
     * @param perPage the number of users per page
     * @return the list of blocked Users in the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<User> getblockedUsers(int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = createGitLabApiForm()
                .withParam("blocked", true)
                .withParam(PAGE_PARAM, page)
                .withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "users");
        return (response.readEntity(new GenericType<List<User>>() {}));
    }

    /**
     * Get a Pager of blocked users.
     *
     * <pre><code>GitLab Endpoint: GET /users?blocked=true</code></pre>
     *
     * @param itemsPerPage the number of blocked User instances that will be fetched per page
     * @return a Pager of blocked User
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<User> getBlockedUsers(int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = createGitLabApiForm().withParam("blocked", true);
        return (new Pager<User>(this, User.class, itemsPerPage, formData.asMap(), "users"));
    }

    /**
     * Get a Stream of blocked users.
     *
     * <pre><code>GitLab Endpoint: GET /users?blocked=true</code></pre>
     *
     * @return a Stream of blocked Users
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<User> getBlockedUsersStream() throws GitLabApiException {
        return (getBlockedUsers(getDefaultPerPage()).stream());
    }

    /**
     * Get a single user.
     *
     * <pre><code>GitLab Endpoint: GET /users/:id</code></pre>
     *
     * @param userId the ID of the user to get
     * @return the User instance for the specified user ID
     * @throws GitLabApiException if any exception occurs
     */
    public User getUser(Long userId) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("with_custom_attributes", customAttributesEnabled);
        Response response = get(Response.Status.OK, formData.asMap(), "users", userId);
        return (response.readEntity(User.class));
    }

    /**
     * Get a single user as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /users/:id</code></pre>
     *
     * @param userId the ID of the user to get
     * @return the User for the specified user ID as an Optional instance
     */
    public Optional<User> getOptionalUser(Long userId) {
        try {
            return (Optional.ofNullable(getUser(userId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Lookup a user by username.  Returns null if not found.
     *
     * <p>NOTE: This is for admin users only.</p>
     *
     * <pre><code>GitLab Endpoint: GET /users?username=:username</code></pre>
     *
     * @param username the username of the user to get
     * @return the User instance for the specified username, or null if not found
     * @throws GitLabApiException if any exception occurs
     */
    public User getUser(String username) throws GitLabApiException {
        GitLabApiForm formData = createGitLabApiForm()
                .withParam("username", username, true)
                .withParam(PAGE_PARAM, 1)
                .withParam(PER_PAGE_PARAM, 1);
        Response response = get(Response.Status.OK, formData.asMap(), "users");
        List<User> users = response.readEntity(new GenericType<List<User>>() {});
        return (users.isEmpty() ? null : users.get(0));
    }

    /**
     * Lookup a user by username and return an Optional instance.
     *
     * <p>NOTE: This is for admin users only.</p>
     *
     * <pre><code>GitLab Endpoint: GET /users?username=:username</code></pre>
     *
     * @param username the username of the user to get
     * @return the User for the specified username as an Optional instance
     */
    public Optional<User> getOptionalUser(String username) {
        try {
            return (Optional.ofNullable(getUser(username)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Lookup a user by email address.  Returns null if not found.
     *
     * <pre><code>GitLab Endpoint: GET /users?search=:email_or_username</code></pre>
     *
     * @param email the email of the user to get
     * @return the User instance for the specified email, or null if not found
     * @throws GitLabApiException if any exception occurs
     * @throws IllegalArgumentException if email is not valid
     */
    public User getUserByEmail(String email) throws GitLabApiException {

        if (!EmailChecker.isValidEmail(email)) {
            throw new IllegalArgumentException("email is not valid");
        }

        List<User> users = findUsers(email, 1, 1);
        return (users.isEmpty() ? null : users.get(0));
    }

    /**
     * Lookup a user by email address and returns an Optional with the User instance as the value.
     *
     * <pre><code>GitLab Endpoint: GET /users?search=:email_or_username</code></pre>
     *
     * @param email the email of the user to get
     * @return the User for the specified email as an Optional instance
     */
    public Optional<User> getOptionalUserByEmail(String email) {
        try {
            return (Optional.ofNullable(getUserByEmail(email)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Lookup a user by external UID.  Returns null if not found.
     *
     * <p>NOTE: This is for admin users only.</p>
     *
     * <pre><code>GitLab Endpoint: GET /users?extern_uid=:externalUid&amp;provider=:provider</code></pre>
     *
     * @param provider the provider of the external uid
     * @param externalUid the external UID of the user
     * @return the User instance for the specified external UID, or null if not found
     * @throws GitLabApiException if any exception occurs
     */
    public User getUserByExternalUid(String provider, String externalUid) throws GitLabApiException {
        GitLabApiForm formData = createGitLabApiForm()
                .withParam("provider", provider, true)
                .withParam("extern_uid", externalUid, true)
                .withParam(PAGE_PARAM, 1)
                .withParam(PER_PAGE_PARAM, 1);
        Response response = get(Response.Status.OK, formData.asMap(), "users");
        List<User> users = response.readEntity(new GenericType<List<User>>() {});
        return (users.isEmpty() ? null : users.get(0));
    }

    /**
     * Lookup a user by external UID and return an Optional instance.
     *
     * <p>NOTE: This is for admin users only.</p>
     *
     * <pre><code>GitLab Endpoint: GET /users?extern_uid=:externUid&amp;provider=:provider</code></pre>
     *
     * @param provider the provider of the external uid
     * @param externalUid the external UID of the user
     * @return the User for the specified external UID as an Optional instance
     */
    public Optional<User> getOptionalUserByExternalUid(String provider, String externalUid) {
        try {
            return (Optional.ofNullable(getUserByExternalUid(provider, externalUid)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Search users by Email or username
     *
     * <pre><code>GitLab Endpoint: GET /users?search=:email_or_username</code></pre>
     *
     * @param emailOrUsername the email or username to search for
     * @return the User List with the email or username like emailOrUsername
     * @throws GitLabApiException if any exception occurs
     */
    public List<User> findUsers(String emailOrUsername) throws GitLabApiException {
        return (findUsers(emailOrUsername, getDefaultPerPage()).all());
    }

    /**
     * Search users by Email or username in the specified page range.
     *
     * <pre><code>GitLab Endpoint: GET /users?search=:email_or_username</code></pre>
     *
     * @param emailOrUsername the email or username to search for
     * @param page            the page to get
     * @param perPage         the number of users per page
     * @return the User List with the email or username like emailOrUsername in the specified page range
     * @throws GitLabApiException if any exception occurs
     */
    public List<User> findUsers(String emailOrUsername, int page, int perPage) throws GitLabApiException {
        GitLabApiForm formData = createGitLabApiForm()
                .withParam("search", emailOrUsername, true)
                .withParam(PAGE_PARAM, page)
                .withParam(PER_PAGE_PARAM, perPage);
        Response response = get(Response.Status.OK, formData.asMap(), "users");
        return (response.readEntity(new GenericType<List<User>>() {
        }));
    }

    /**
     * Search users by Email or username and return a Pager
     *
     * <pre><code>GitLab Endpoint: GET /users?search=:email_or_username</code></pre>
     *
     * @param emailOrUsername the email or username to search for
     * @param itemsPerPage    the number of Project instances that will be fetched per page
     * @return the User Pager with the email or username like emailOrUsername
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<User> findUsers(String emailOrUsername, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = createGitLabApiForm().withParam("search", emailOrUsername, true);
        return (new Pager<User>(this, User.class, itemsPerPage, formData.asMap(), "users"));
    }

    /**
     * Search users by Email or username.
     *
     * <pre><code>GitLab Endpoint: GET /users?search=:email_or_username</code></pre>
     *
     * @param emailOrUsername the email or username to search for
     * @return a Stream of User instances with the email or username like emailOrUsername
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<User> findUsersStream(String emailOrUsername) throws GitLabApiException {
        return (findUsers(emailOrUsername, getDefaultPerPage()).stream());
    }

    /**
     * <p>Creates a new user. Note only administrators can create new users.
     * Either password or reset_password should be specified (reset_password takes priority).</p>
     *
     * <p>If both the User object's projectsLimit and the parameter projectsLimit is specified
     * the parameter will take precedence.</p>
     *
     * <pre><code>GitLab Endpoint: POST /users</code></pre>
     *
     * <p>The following properties of the provided User instance can be set during creation:<pre><code> email (required) - Email
     * username (required) - Username
     * name (required) - Name
     * skype (optional) - Skype ID
     * linkedin (optional) - LinkedIn
     * twitter (optional) - Twitter account
     * websiteUrl (optional) - Website URL
     * organization (optional) - Organization name
     * projectsLimit (optional) - Number of projects user can create
     * externUid (optional) - External UID
     * provider (optional) - External provider name
     * bio (optional) - User's biography
     * location (optional) - User's location
     * admin (optional) - User is admin - true or false (default)
     * canCreateGroup (optional) - User can create groups - true or false
     * skipConfirmation (optional) - Skip confirmation - true or false (default)
     * external (optional) - Flags the user as external - true or false(default)
     * sharedRunnersMinutesLimit (optional) - Pipeline minutes quota for this user
     * </code></pre>
     *
     * @param user          the User instance with the user info to create
     * @param password      the password for the new user
     * @param projectsLimit the maximum number of project
     * @return created User instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated Will be removed in version 6.0, replaced by {@link #createUser(User, CharSequence, boolean)}
     */
    @Deprecated
    public User createUser(User user, CharSequence password, Integer projectsLimit) throws GitLabApiException {
        Form formData = userToForm(user, projectsLimit, password, null, true);
        Response response = post(Response.Status.CREATED, formData, "users");
        return (response.readEntity(User.class));
    }

    /**
     * <p>Creates a new user. Note only administrators can create new users.
     * Either password or resetPassword should be specified (resetPassword takes priority).</p>
     *
     * <pre><code>GitLab Endpoint: POST /users</code></pre>
     *
     * <p>The following properties of the provided User instance can be set during creation:<pre><code> email (required) - Email
     * username (required) - Username
     * name (required) - Name
     * skype (optional) - Skype ID
     * linkedin (optional) - LinkedIn
     * twitter (optional) - Twitter account
     * websiteUrl (optional) - Website URL
     * organization (optional) - Organization name
     * projectsLimit (optional) - Number of projects user can create
     * externUid (optional) - External UID
     * provider (optional) - External provider name
     * bio (optional) - User's biography
     * location (optional) - User's location
     * admin (optional) - User is admin - true or false (default)
     * canCreateGroup (optional) - User can create groups - true or false
     * skipConfirmation (optional) - Skip confirmation - true or false (default)
     * external (optional) - Flags the user as external - true or false(default)
     * sharedRunnersMinutesLimit (optional) - Pipeline minutes quota for this user
     * </code></pre>
     *
     * @param user          the User instance with the user info to create
     * @param password      the password for the new user
     * @param resetPassword whether to send a password reset link
     * @return created User instance
     * @throws GitLabApiException if any exception occurs
     */
    public User createUser(User user, CharSequence password, boolean resetPassword) throws GitLabApiException {
        Form formData = userToForm(user, null, password, resetPassword, true);
        Response response = post(Response.Status.CREATED, formData, "users");
        return (response.readEntity(User.class));
    }

    /**
     * <p>Modifies an existing user. Only administrators can change attributes of a user.</p>
     *
     * <pre><code>GitLab Endpoint: PUT /users</code></pre>
     *
     * <p>The following properties of the provided User instance can be set during update:<pre><code> email (required) - Email
     * username (required) - Username
     * name (required) - Name
     * skype (optional) - Skype ID
     * linkedin (optional) - LinkedIn
     * twitter (optional) - Twitter account
     * websiteUrl (optional) - Website URL
     * organization (optional) - Organization name
     * projectsLimit (optional) - Number of projects user can create
     * externUid (optional) - External UID
     * provider (optional) - External provider name
     * bio (optional) - User's biography
     * location (optional) - User's location
     * admin (optional) - User is admin - true or false (default)
     * canCreateGroup (optional) - User can create groups - true or false
     * skipConfirmation (optional) - Skip confirmation - true or false (default)
     * external (optional) - Flags the user as external - true or false(default)
     * sharedRunnersMinutesLimit (optional) - Pipeline minutes quota for this user
     * </code></pre>
     *
     * @param user     the User instance with the user info to modify
     * @param password the new password for the user
     * @return the modified User instance
     * @throws GitLabApiException if any exception occurs
     */
    public User updateUser(User user, CharSequence password) throws GitLabApiException {
        Form form = userToForm(user, null, password, false, false);
        Response response = put(Response.Status.OK, form.asMap(), "users", user.getId());
        return (response.readEntity(User.class));
    }

    /**
     * Modifies an existing user. Only administrators can change attributes of a user.
     *
     * <pre><code>GitLab Endpoint: PUT /users/:id</code></pre>
     *
     * <p>The following properties of the provided User instance can be set during update:<pre><code> email (required) - Email
     * username (required) - Username
     * name (required) - Name
     * skype (optional) - Skype ID
     * linkedin (optional) - LinkedIn
     * twitter (optional) - Twitter account
     * websiteUrl (optional) - Website URL
     * organization (optional) - Organization name
     * projectsLimit (optional) - Number of projects user can create
     * externUid (optional) - External UID
     * provider (optional) - External provider name
     * bio (optional) - User's biography
     * location (optional) - User's location
     * admin (optional) - User is admin - true or false (default)
     * canCreateGroup (optional) - User can create groups - true or false
     * skipConfirmation (optional) - Skip confirmation - true or false (default)
     * external (optional) - Flags the user as external - true or false(default)
     * sharedRunnersMinutesLimit (optional) - Pipeline minutes quota for this user
     * </code></pre>
     *
     * @param user          the User instance with the user info to modify
     * @param password      the new password for the user
     * @param projectsLimit the maximum number of project
     * @return the modified User instance
     * @throws GitLabApiException if any exception occurs
     * @deprecated Will be removed in version 6.0, replaced by {@link #updateUser(User, CharSequence)}
     */
    @Deprecated
    public User modifyUser(User user, CharSequence password, Integer projectsLimit) throws GitLabApiException {
        Form form = userToForm(user, projectsLimit, password, false, false);
        Response response = put(Response.Status.OK, form.asMap(), "users", user.getId());
        return (response.readEntity(User.class));
    }

    /**
     * Deletes a user. Available only for administrators.
     *
     * <pre><code>GitLab Endpoint: DELETE /users/:id</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteUser(Object userIdOrUsername) throws GitLabApiException {
        deleteUser(userIdOrUsername, null);
    }

    /**
     * Deletes a user. Available only for administrators.
     *
     * <pre><code>GitLab Endpoint: DELETE /users/:id</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param hardDelete If true, contributions that would usually be moved to the
     *                   ghost user will be deleted instead, as well as groups owned solely by this user
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteUser(Object userIdOrUsername, Boolean hardDelete) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("hard_delete ", hardDelete);
        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, formData.asMap(), "users", getUserIdOrUsername(userIdOrUsername));
    }

    /**
     * Get currently authenticated user.
     *
     * <pre><code>GitLab Endpoint: GET /user</code></pre>
     *
     * @return the User instance for the currently authenticated user
     * @throws GitLabApiException if any exception occurs
     */
    public User getCurrentUser() throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "user");
        return (response.readEntity(User.class));
    }

    /**
     * Get a list of currently authenticated user's SSH keys.
     *
     * <pre><code>GitLab Endpoint: GET /user/keys</code></pre>
     *
     * @return a list of currently authenticated user's SSH keys
     * @throws GitLabApiException if any exception occurs
     */
    public List<SshKey> getSshKeys() throws GitLabApiException {
        Response response = get(Response.Status.OK, getDefaultPerPageParam(), "user", "keys");
        return (response.readEntity(new GenericType<List<SshKey>>() {}));
    }

    /**
     * Get a list of a specified user's SSH keys. Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: GET /users/:id/keys</code></pre>
     *
     * @param userId the user ID to get the SSH keys for
     * @return a list of a specified user's SSH keys
     * @throws GitLabApiException if any exception occurs
     */
    public List<SshKey> getSshKeys(Long userId) throws GitLabApiException {

        if (userId == null) {
            throw new RuntimeException("userId cannot be null");
        }

        Response response = get(Response.Status.OK, getDefaultPerPageParam(), "users", userId, "keys");
        List<SshKey> keys = response.readEntity(new GenericType<List<SshKey>>() {});

        if (keys != null) {
            keys.forEach(key -> key.setUserId(userId));
        }

        return (keys);
    }

    /**
     * Get a single SSH Key.
     *
     * <pre><code>GitLab Endpoint: GET /user/keys/:key_id</code></pre>
     *
     * @param keyId the ID of the SSH key.
     * @return an SshKey instance holding the info on the SSH key specified by keyId
     * @throws GitLabApiException if any exception occurs
     */
    public SshKey getSshKey(Long keyId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "user", "keys", keyId);
        return (response.readEntity(SshKey.class));
    }

    /**
     * Get a single SSH Key as an Optional instance.
     *
     * <pre><code>GitLab Endpoint: GET /user/keys/:key_id</code></pre>
     *
     * @param keyId the ID of the SSH key
     * @return an SshKey as an Optional instance holding the info on the SSH key specified by keyId
     */
    public Optional<SshKey> getOptionalSshKey(Long keyId) {
        try {
            return (Optional.ofNullable(getSshKey(keyId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Creates a new key owned by the currently authenticated user.
     *
     * <pre><code>GitLab Endpoint: POST /user/keys</code></pre>
     *
     * @param title the new SSH Key's title
     * @param key   the new SSH key
     * @return an SshKey instance with info on the added SSH key
     * @throws GitLabApiException if any exception occurs
     */
    public SshKey addSshKey(String title, String key) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("title", title).withParam("key", key);
        Response response = post(Response.Status.CREATED, formData, "user", "keys");
        return (response.readEntity(SshKey.class));
    }

    /**
     * Create new key owned by specified user. Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: POST /users/:id/keys</code></pre>
     *
     * @param userId the ID of the user to add the SSH key for
     * @param title  the new SSH Key's title
     * @param key    the new SSH key
     * @return an SshKey instance with info on the added SSH key
     * @throws GitLabApiException if any exception occurs
     */
    public SshKey addSshKey(Long userId, String title, String key) throws GitLabApiException {

        if (userId == null) {
            throw new RuntimeException("userId cannot be null");
        }

        GitLabApiForm formData = new GitLabApiForm().withParam("title", title).withParam("key", key);
        Response response = post(Response.Status.CREATED, formData, "users", userId, "keys");
        SshKey sshKey = response.readEntity(SshKey.class);
        if (sshKey != null) {
            sshKey.setUserId(userId);
        }

        return (sshKey);
    }

    /**
     * Deletes key owned by currently authenticated user. This is an idempotent function and calling it
     * on a key that is already deleted or not available results in success.
     *
     * <pre><code>GitLab Endpoint: DELETE /user/keys/:key_id</code></pre>
     *
     * @param keyId the key ID to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteSshKey(Long keyId) throws GitLabApiException {

        if (keyId == null) {
            throw new RuntimeException("keyId cannot be null");
        }

        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "user", "keys", keyId);
    }

    /**
     * Deletes key owned by a specified user. Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: DELETE /users/:id/keys/:key_id</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param keyId  the key ID to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteSshKey(Object userIdOrUsername, Long keyId) throws GitLabApiException {

        if (keyId == null) {
            throw new RuntimeException("keyId cannot be null");
        }

        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "users", getUserIdOrUsername(userIdOrUsername), "keys", keyId);
    }

    /**
     * Get a list of a specified user's impersonation tokens.  Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: GET /users/:id/impersonation_tokens</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @return a list of a specified user's impersonation tokens
     * @throws GitLabApiException if any exception occurs
     */
    public List<ImpersonationToken> getImpersonationTokens(Object userIdOrUsername) throws GitLabApiException {
        return (getImpersonationTokens(userIdOrUsername, null));
    }

    /**
     * Get a list of a specified user's impersonation tokens.  Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: GET /users/:id/impersonation_tokens</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param state  the state of impersonation tokens to list (ALL, ACTIVE, INACTIVE)
     * @return a list of a specified user's impersonation tokens
     * @throws GitLabApiException if any exception occurs
     */
    public List<ImpersonationToken> getImpersonationTokens(Object userIdOrUsername, ImpersonationState state) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("state", state)
                .withParam(PER_PAGE_PARAM, getDefaultPerPage());
        Response response = get(Response.Status.OK, formData.asMap(), "users", getUserIdOrUsername(userIdOrUsername), "impersonation_tokens");
        return (response.readEntity(new GenericType<List<ImpersonationToken>>() {}));
    }

    /**
     * Get an impersonation token of a user.  Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: GET /users/:user_id/impersonation_tokens/:impersonation_token_id</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param tokenId the impersonation token ID to get
     * @return the specified impersonation token
     * @throws GitLabApiException if any exception occurs
     */
    public ImpersonationToken getImpersonationToken(Object userIdOrUsername, Long tokenId) throws GitLabApiException {

        if (tokenId == null) {
            throw new RuntimeException("tokenId cannot be null");
        }

        Response response = get(Response.Status.OK, null, "users", getUserIdOrUsername(userIdOrUsername), "impersonation_tokens", tokenId);
        return (response.readEntity(ImpersonationToken.class));
    }

    /**
     * Get an impersonation token of a user as an Optional instance. Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: GET /users/:user_id/impersonation_tokens/:impersonation_token_id</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param tokenId the impersonation token ID to get
     * @return the specified impersonation token as an Optional instance
     */
    public Optional<ImpersonationToken> getOptionalImpersonationToken(Object userIdOrUsername, Long tokenId) {
        try {
            return (Optional.ofNullable(getImpersonationToken(userIdOrUsername, tokenId)));
        } catch (GitLabApiException glae) {
            return (GitLabApi.createOptionalFromException(glae));
        }
    }

    /**
     * Create an impersonation token.  Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: POST /users/:user_id/impersonation_tokens</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param name the name of the impersonation token, required
     * @param expiresAt the expiration date of the impersonation token, optional
     * @param scopes an array of scopes of the impersonation token
     * @return the created ImpersonationToken instance
     * @throws GitLabApiException if any exception occurs
     */
    public ImpersonationToken createImpersonationToken(Object userIdOrUsername, String name, Date expiresAt, Scope[] scopes) throws GitLabApiException {

        if (scopes == null || scopes.length == 0) {
            throw new RuntimeException("scopes cannot be null or empty");
        }

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("name", name, true)
                .withParam("expires_at", expiresAt);

        for (Scope scope : scopes) {
            formData.withParam("scopes[]", scope.toString());
        }

        Response response = post(Response.Status.CREATED, formData, "users", getUserIdOrUsername(userIdOrUsername), "impersonation_tokens");
        return (response.readEntity(ImpersonationToken.class));
    }

    /**
     * Revokes an impersonation token. Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: DELETE /users/:user_id/impersonation_tokens/:impersonation_token_id</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param tokenId the impersonation token ID to revoke
     * @throws GitLabApiException if any exception occurs
     */
    public void revokeImpersonationToken(Object userIdOrUsername, Long tokenId) throws GitLabApiException {

        if (tokenId == null) {
            throw new RuntimeException("tokenId cannot be null");
        }

        Response.Status expectedStatus = (isApiVersion(ApiVersion.V3) ? Response.Status.OK : Response.Status.NO_CONTENT);
        delete(expectedStatus, null, "users", getUserIdOrUsername(userIdOrUsername), "impersonation_tokens", tokenId);
    }

    /**
     * Populate the REST form with data from the User instance.
     *
     * @param user          the User instance to populate the Form instance with
     * @param projectsLimit the maximum number of projects the user is allowed (optional)
     * @param password      the password, required when creating a new user
     * @param create        whether the form is being populated to create a new user
     * @return the populated Form instance
     */
    Form userToForm(User user, Integer projectsLimit, CharSequence password, Boolean resetPassword, boolean create) {

        if (create) {
            if ((password == null || password.toString().trim().isEmpty()) && !resetPassword) {
                throw new IllegalArgumentException("either password or reset_password must be set");
            }
        }

        projectsLimit = (projectsLimit == null) ? user.getProjectsLimit() : projectsLimit;
        String skipConfirmationFeildName = create ? "skip_confirmation" : "skip_reconfirmation";

        return (new GitLabApiForm()
                .withParam("email", user.getEmail(), create)
                .withParam("password", password, false)
                .withParam("reset_password", resetPassword, false)
                .withParam("username", user.getUsername(), create)
                .withParam("name", user.getName(), create)
                .withParam("skype", user.getSkype(), false)
                .withParam("linkedin", user.getLinkedin(), false)
                .withParam("twitter", user.getTwitter(), false)
                .withParam("website_url", user.getWebsiteUrl(), false)
                .withParam("organization", user.getOrganization(), false)
                .withParam("projects_limit", projectsLimit, false)
                .withParam("extern_uid", user.getExternUid(), false)
                .withParam("provider", user.getProvider(), false)
                .withParam("bio", user.getBio(), false)
                .withParam("location", user.getLocation(), false)
                .withParam("admin", user.getIsAdmin(), false)
                .withParam("theme_id", user.getThemeId(), false)
                .withParam("color_scheme_id", user.getColorSchemeId(), false)
                .withParam("can_create_group", user.getCanCreateGroup(), false)
                .withParam(skipConfirmationFeildName, user.getSkipConfirmation(), false)
                .withParam("external", user.getExternal(), false)
                .withParam("shared_runners_minutes_limit", user.getSharedRunnersMinutesLimit(), false));
    }

    /**
     * Creates custom attribute for the given user
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param customAttribute the custom attribute to set
     * @return the created CustomAttribute
     * @throws GitLabApiException on failure while setting customAttributes
     */
    public CustomAttribute createCustomAttribute(final Object userIdOrUsername, final CustomAttribute customAttribute) throws GitLabApiException {
        if (Objects.isNull(customAttribute)) {
            throw new IllegalArgumentException("CustomAttributes can't be null");
        }
        return createCustomAttribute(userIdOrUsername, customAttribute.getKey(), customAttribute.getValue());
    }

    /**
     * Creates custom attribute for the given user
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param key for the customAttribute
     * @param value  or the customAttribute
     * @return the created CustomAttribute
     * @throws GitLabApiException on failure while setting customAttributes
     */
    public CustomAttribute createCustomAttribute(final Object userIdOrUsername, final String key, final String value) throws GitLabApiException {

        if (Objects.isNull(key) || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key can't be null or empty");
        }
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Value can't be null or empty");
        }

        GitLabApiForm formData = new GitLabApiForm().withParam("value", value);
        Response response = put(Response.Status.OK, formData.asMap(),
                "users", getUserIdOrUsername(userIdOrUsername), "custom_attributes", key);
        return (response.readEntity(CustomAttribute.class));
    }

    /**
     * Change custom attribute for the given user
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param customAttribute the custome attribute to change
     * @return the changed CustomAttribute
     * @throws GitLabApiException on failure while changing customAttributes
     */
    public CustomAttribute changeCustomAttribute(final Object userIdOrUsername, final CustomAttribute customAttribute) throws GitLabApiException {

        if (Objects.isNull(customAttribute)) {
            throw new IllegalArgumentException("CustomAttributes can't be null");
        }

        //changing & creating custom attributes is the same call in gitlab api
        // -> https://docs.gitlab.com/ce/api/custom_attributes.html#set-custom-attribute
        return createCustomAttribute(userIdOrUsername, customAttribute.getKey(), customAttribute.getValue());
    }

    /**
     * Changes custom attribute for the given user
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param key    for the customAttribute
     * @param value  for the customAttribute
     * @return changedCustomAttribute
     * @throws GitLabApiException on failure while changing customAttributes
     */
    public CustomAttribute changeCustomAttribute(final Object userIdOrUsername, final String key, final String value) throws GitLabApiException {
        return createCustomAttribute(userIdOrUsername, key, value);
    }

    /**
     * Delete a custom attribute for the given user
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param customAttribute to remove
     * @throws GitLabApiException on failure while deleting customAttributes
     */
    public void deleteCustomAttribute(final Object userIdOrUsername, final CustomAttribute customAttribute) throws GitLabApiException {
        if (Objects.isNull(customAttribute)) {
            throw new IllegalArgumentException("customAttributes can't be null");
        }

        deleteCustomAttribute(userIdOrUsername, customAttribute.getKey());
    }

    /**
     * Delete a custom attribute for the given user
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param key    of the customAttribute to remove
     * @throws GitLabApiException on failure while deleting customAttributes
     */
    public void deleteCustomAttribute(final Object userIdOrUsername, final String key) throws GitLabApiException {

        if (Objects.isNull(key) || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key can't be null or empty");
        }

        delete(Response.Status.OK, null, "users", getUserIdOrUsername(userIdOrUsername), "custom_attributes", key);
    }

    /**
     * Creates a GitLabApiForm instance that will optionally include the
     * with_custom_attributes query param if enabled.
     *
     * @return a GitLabApiForm instance that will optionally include the
     * with_custom_attributes query param if enabled
     */
    private GitLabApiForm createGitLabApiForm() {
        GitLabApiForm formData = new GitLabApiForm();
        return (customAttributesEnabled ? formData.withParam("with_custom_attributes", true) : formData);
    }

    /**
     * Uploads and sets the user's avatar for the specified user.
     *
     * <pre><code>PUT /users/:id</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param avatarFile the File instance of the avatar file to upload
     * @return the updated User instance
     * @throws GitLabApiException if any exception occurs
     */
    public User setUserAvatar(final Object userIdOrUsername, File avatarFile) throws GitLabApiException {
        Response response = putUpload(Response.Status.OK, "avatar", avatarFile,  "users", getUserIdOrUsername(userIdOrUsername));
        return (response.readEntity(User.class));
    }

    /**
     * Get a list of emails for the current user.
     *
     * <pre><code>GitLab Endpoint: GET /users/emails</code></pre>
     *
     * @return a List of Email instances for the current user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Email> getEmails() throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "user", "emails");
        return (response.readEntity(new GenericType<List<Email>>() {}));
    }

    /**
     * Get a list of a specified user’s emails. Available only for admin users.
     *
     * <pre><code>GitLab Endpoint: GET /user/:id/emails</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @return a List of Email instances for the specified user
     * @throws GitLabApiException if any exception occurs
     */
    public List<Email> getEmails(final Object userIdOrUsername) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "users", getUserIdOrUsername(userIdOrUsername), "emails");
        return (response.readEntity(new GenericType<List<Email>>() {}));
    }

    /**
     * Add an email to the current user's emails.
     *
     * <pre><code>GitLab Endpoint: POST /user/:id/emails</code></pre>
     *
     * @param email the email address to add
     * @return the Email instance for the added email
     * @throws GitLabApiException if any exception occurs
     */
    public Email addEmail(String email) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm().withParam("email", email, true);
        Response response = post(Response.Status.CREATED, formData, "user", "emails");
        return (response.readEntity(Email.class));
    }

    /**
     * Get a single Email instance specified by he email ID
     *
     * <pre><code>GitLab Endpoint: GET /user/emails/:emailId</code></pre>
     *
     * @param emailId the email ID to get
     * @return the Email instance for the provided email ID
     * @throws GitLabApiException if any exception occurs
     */
    public Email getEmail(final Long emailId) throws GitLabApiException {
        Response response = get(Response.Status.CREATED, null, "user", "emails", emailId);
        return (response.readEntity(Email.class));
    }

    /**
     * Add an email to the user's emails.
     *
     * <pre><code>GitLab Endpoint: POST /user/:id/emails</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param email the email address to add
     * @param skipConfirmation skip confirmation and assume e-mail is verified - true or false (default)
     * @return the Email instance for the added email
     * @throws GitLabApiException if any exception occurs
     */
    public Email addEmail(final Object userIdOrUsername, String email, Boolean skipConfirmation) throws GitLabApiException {

        GitLabApiForm formData = new GitLabApiForm()
                .withParam("email", email, true)
                .withParam("skip_confirmation ", skipConfirmation);
        Response response = post(Response.Status.CREATED, formData, "users", getUserIdOrUsername(userIdOrUsername), "emails");
        return (response.readEntity(Email.class));
    }

    /**
     * Deletes an email belonging to the current user.
     *
     * <pre><code>GitLab Endpoint: DELETE /user/emails/:emailId</code></pre>
     *
     * @param emailId the email ID to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteEmail(final Long emailId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "user", "emails", emailId);
    }

    /**
     * Deletes a user's email
     *
     * <pre><code>GitLab Endpoint: DELETE /user/:id/emails/:emailId</code></pre>
     *
     * @param userIdOrUsername the user in the form of an Long(ID), String(username), or User instance
     * @param emailId the email ID to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteEmail(final Object userIdOrUsername, final Long emailId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "users", getUserIdOrUsername(userIdOrUsername), "emails", emailId);
    }

    /**
     * Get all GPG keys for the current user.
     *
     * <pre><code>GitLab Endpoint: GET /user/gpg_keys</code></pre>
     *
     * @throws GitLabApiException if any exception occurs
     */
    public List<GpgKey> listGpgKeys() throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "user", "gpg_keys");
        return (response.readEntity(new GenericType<List<GpgKey>>() {}));
    }

    /**
     * Add a GPG key for the current user
     *
     * <pre><code>GitLab Endpoint: POST /user/gpg_keys</code></pre>
     *
     * @param key the ASCII-armored exported public GPG key to add
     * @throws GitLabApiException if any exception occurs
     */
    public GpgKey addGpgKey(final String key) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("key", key, true);
        Response response = post(Response.Status.CREATED, formData, "user", "gpg_keys");
        return (response.readEntity(GpgKey.class));
    }

    /**
     * Remove a specific GPG key for the current user
     *
     * <pre><code>GitLab Endpoint: DELETE /user/gpg_keys/:keyId</code></pre>
     *
     * @param keyId the key ID  in the form if an Long(ID)
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteGpgKey(final Long keyId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "user", "gpg_keys", keyId);
    }

    /**
     * Get all GPG keys for a given user.
     *
     * <pre><code>GitLab Endpoint: GET /users/:id/gpg_keys</code></pre>
     *
     * @param userId the user in the form of an Long(ID)
     * @throws GitLabApiException if any exception occurs
     */
    public List<GpgKey> listGpgKeys(final Long userId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null, "users", userId, "gpg_keys");
        return (response.readEntity(new GenericType<List<GpgKey>>() {}));
    }

    /**
     * Add a GPG key for a specific user
     *
     * <pre><code>GitLab Endpoint: POST /users/:id/gpg_keys</code></pre>
     *
     * @param userId the user in the form of an Long(ID)
     * @param key the ASCII-armored exported public GPG key to add
     * @throws GitLabApiException if any exception occurs
     */
    public GpgKey addGpgKey(final Long userId, final String key) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm()
                .withParam("key", key, true);
        Response response = post(Response.Status.CREATED, formData, "users", userId, "gpg_keys");
        return (response.readEntity(GpgKey.class));
    }

    /**
     * Remove a specific GPG key for a specific user
     *
     * <pre><code>GitLab Endpoint: DELETE /users/:id/gpg_keys/:keyId</code></pre>
     *
     * @param userId the user in the form of an Long(ID)
     * @param keyId the key ID  in the form if an Long(ID)
     * @throws GitLabApiException if any exception occurs
     */
    public void deleteGpgKey(final Long userId, final Long keyId) throws GitLabApiException {
        delete(Response.Status.NO_CONTENT, null, "users", userId, "gpg_keys", keyId);
    }

    /**
     * Lists all projects and groups a user is a member of. (admin only)
     *
     * <pre><code>GitLab Endpoint: GET /users/:id/memberships</code></pre>
     *
     * @param userId the ID of the user to get the memberships for
     * @return the list of memberships of the given user
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 12.8
     */
    public List<Membership> getMemberships(Long userId) throws GitLabApiException {
        return getMemberships(userId, getDefaultPerPage()).all();
    }

    /**
     * Returns a Pager that lists all projects and groups a user is a member of. (admin only)
     *
     * This allows lazy-fetching of huge numbers of memberships.
     *
     * <pre><code>GitLab Endpoint: GET /users/:id/memberships</code></pre>
     *
     * @param userId the ID of the user to get the memberships for
     * @param itemsPerPage the number of Membership instances that will be fetched per page
     * @return a Pager of user's memberships
     * @throws GitLabApiException if any exception occurs
     * @since GitLab 12.8
     */
    public Pager<Membership> getMemberships(Long userId, int itemsPerPage) throws GitLabApiException {
        GitLabApiForm formData = new GitLabApiForm();
        return (new Pager<>(this, Membership.class, itemsPerPage, formData.asMap(), "users", userId, "memberships"));
    }
}
