package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;


/**
 * This enum provides constants and value validation for the available GitLab application settings.
 * See <a href="https://docs.gitlab.com/ce/api/settings.html#list-of-settings-that-can-be-accessed-via-api-calls">
 * List of settings that can be accessed via API calls</a> for more information.
 */
public enum Setting {

    /**
     * Abuse reports will be sent to this address if it is set. Abuse reports are
     * always available in the admin area.
     */
    ADMIN_NOTIFICATION_EMAIL(String.class),

    /**
     * Where to redirect users after logout.
     */
    AFTER_SIGN_OUT_PATH(String.class),

    /**
     * Text shown to the user after signing up
     */
    AFTER_SIGN_UP_TEXT(String.class),

    /**
     * required by: {@link #AKISMET_ENABLED} API key for Akismet spam protection.
     */
    AKISMET_API_KEY(String.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #AKISMET_API_KEY}) Enable or disable Akismet spam
     * protection.
     */
    AKISMET_ENABLED(Boolean.class),

    /**
     * (PREMIUM | SILVER) Set to true to allow group owners to manage LDAP
     */
    ALLOW_GROUP_OWNERS_TO_MANAGE_LDAP(Boolean.class),

    /**
     * Allow requests to the local network from hooks and services.
     * @deprecated Use allow_local_requests_from_web_hooks_and_services instead
     */
    @Deprecated
    ALLOW_LOCAL_REQUESTS_FROM_HOOKS_AND_SERVICES(Boolean.class),

    /**
     * Allow requests to the local network from system hooks.
     */
    ALLOW_LOCAL_REQUESTS_FROM_SYSTEM_HOOKS(Boolean.class),
    
    /**
     * Allow requests to the local network from web hooks and services.
     */
    ALLOW_LOCAL_REQUESTS_FROM_WEB_HOOKS_AND_SERVICES(Boolean.class),

    /**
     * Set the duration for which the jobs will be considered as old and expired. 
     * Once that time passes, the jobs will be archived and no longer able to be  retried.
     * Make it empty to never expire jobs. It has to be no less than 1 day,
     * for example: 15 days, 1 month, 2 years.
     */
    ARCHIVE_BUILDS_IN_HUMAN_READABLE(String.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #ASSET_PROXY_URL})
     * Enable proxying of assets. GitLab restart is required to apply changes.
     */
    ASSET_PROXY_ENABLED(Boolean.class),

    /**
     * URL of the asset proxy server. GitLab restart is required to apply changes.
     */
    ASSET_PROXY_URL(String.class),

    /**
     * Assets that match these domain(s) will NOT be proxied. Wildcards allowed.
     * Your GitLab installation URL is automatically whitelisted. GitLab restart
     * is required to apply changes.
     */
    ASSET_PROXY_WHITELIST(new Class<?>[]{String.class, String[].class}), 

    /**
     * By default, we write to the authorized_keys file to support Git over SSH
     * without additional configuration. GitLab can be optimized to authenticate SSH
     * keys via the database file. Only disable this if you have configured your
     * OpenSSH server to use the AuthorizedKeysCommand.
     */
    AUTHORIZED_KEYS_ENABLED(Boolean.class),

    /**
     * Specify a domain to use by default for every project’s Auto Review Apps and
     * Auto Deploy stages.
     */
    AUTO_DEVOPS_DOMAIN(String.class),

    /**
     * Enable Auto DevOps for projects by default. It will automatically build,
     * test, and deploy applications based on a predefined CI/CD configuration.
     */
    AUTO_DEVOPS_ENABLED(Boolean.class),
    
    /**
     * (PREMIUM | SILVER) Enabling this will make only licensed EE features
     * available to projects if the project namespace’s plan includes the feature
     * or if the project is public.
     */
    CHECK_NAMESPACE_PLAN(Boolean.class),

    /**
     * required by: {@link #CLIENTSIDE_SENTRY_DSN} Clientside Sentry Data Source Name.
     * removed by the following commit https://gitlab.com/gitlab-org/gitlab/commit/31c8ca6defd36bd08209ecc8c5913631c316ce37
     * @deprecated Will be removed in a future version of gitlab4j-api
     */
    @Deprecated
    CLIENTSIDE_SENTRY_DSN(String.class),
    
    /**
     * (<strong>If enabled, requires:</strong> {@link #CLIENTSIDE_SENTRY_DSN}) Enable Sentry error reporting for the client side.
     * @deprecated Will be removed in a future version of gitlab4j-api
     */
    @Deprecated
    CLIENTSIDE_SENTRY_ENABLED(Boolean.class),
    
    /**
     * Custom hostname (for private commit emails).
     */
    COMMIT_EMAIL_HOSTNAME(String.class),

    /**
     * Container Registry token duration in minutes.
     */
    CONTAINER_REGISTRY_TOKEN_EXPIRE_DELAY(Integer.class),

    /**
     * Set the default expiration time for each job’s artifacts.
     */
    DEFAULT_ARTIFACTS_EXPIRE_IN(String.class),

    /**
     * Determine if developers can push to master. Can take: 0 (not protected, both
     * developers and maintainers can push new commits, force push, or delete the
     * branch), 1 (partially protected, developers and maintainers can push new
     * commits, but cannot force push or delete the branch) or 2 (fully protected,
     * developers cannot push new commits, but maintainers can; no-one can force
     * push or delete the branch) as a parameter. Default is 2.
     */
    DEFAULT_BRANCH_PROTECTION(Integer.class),

    DEFAULT_CI_CONFIG_PATH(String.class),


    /**
     * What visibility level new groups receive. Can take private, internal and
     * public as a parameter. Default is private.
     */
    DEFAULT_GROUP_VISIBILITY(String.class),

    /**
     * Default project creation protection. Can take: 0 (No one), 1 (Maintainers)
     * or 2 (Developers + Maintainers)
     */
    DEFAULT_PROJECT_CREATION(Integer.class),

    /**
     * What visibility level new projects receive. Can take private, internal and
     * public as a parameter. Default is private.
     */
    DEFAULT_PROJECT_VISIBILITY(String.class),

    /**
     * Project limit per user. Default is 100000.
     */
    DEFAULT_PROJECTS_LIMIT(Integer.class),

    /**
     * What visibility level new snippets receive. Can take private, internal and
     * public as a parameter. Default is private.
     */
    DEFAULT_SNIPPET_VISIBILITY(String.class),

    /**
     * Maximum diff patch size (Bytes).
     */
    DIFF_MAX_PATCH_BYTES(Integer.class),

    /**
     * Disabled OAuth sign-in sources.
     */
    DISABLED_OAUTH_SIGN_IN_SOURCES(String[].class),

    /**
     * Enforce DNS rebinding attack protection.
     */
    DNS_REBINDING_PROTECTION_ENABLED(Boolean.class),

    /**
     * required by: {@link #DOMAIN_BLACKLIST_ENABLED} Users with e-mail addresses that match
     * these domain(s) will NOT be able to sign-up. Wildcards allowed. Use separate
     * lines for multiple entries. Ex: domain.com, *.domain.com.
     */
    DOMAIN_BLACKLIST(String[].class),

    /**
     * (<strong>If enabled, requires:</strong>  {@link #DOMAIN_BLACKLIST}) Allows 
     * blocking sign-ups from emails from specific domains.
     */
    DOMAIN_BLACKLIST_ENABLED(Boolean.class),

    /**
     * NOT DOCUMENTED: but it's returned by a call to /api/v4/application/settings
     * @deprecated Use {@link Setting#DOMAIN_BLACKLIST} instead. Will be removed in API v5
     * see https://gitlab.com/gitlab-org/gitlab/commit/85776fa3ffba6f641cf981cb0107f0e4ba882f3e#40f8529fa8ed874d8e312edb04db18420bf06d31_185_185
     */
    @Deprecated
    DOMAIN_BLACKLIST_RAW(String.class),

    /**
     * Force people to use only corporate emails for sign-up. Default is null,
     * meaning there is no restriction.
     */
    DOMAIN_WHITELIST(String[].class),

    /**
     * NOT DOCUMENTED: but it's returned by a call to /api/v4/application/settings
     * @deprecated Use {@link #DOMAIN_WHITELIST} instead. Will be removed in API v5
     * see https://gitlab.com/gitlab-org/gitlab/commit/85776fa3ffba6f641cf981cb0107f0e4ba882f3e#40f8529fa8ed874d8e312edb04db18420bf06d31_185_185
     */
    @Deprecated
    DOMAIN_WHITELIST_RAW(String.class),

    /**
     * The minimum allowed bit length of an uploaded DSA key. Default is 0 (no
     * restriction). -1 disables DSA keys.
     */
    DSA_KEY_RESTRICTION(Integer.class),

    /**
     * The minimum allowed curve size (in bits) of an uploaded ECDSA key. Default is
     * 0 (no restriction). -1 disables ECDSA keys.
     */
    ECDSA_KEY_RESTRICTION(Integer.class),

    /**
     * The minimum allowed curve size (in bits) of an uploaded ED25519 key. Default
     * is 0 (no restriction). -1 disables ED25519 keys.
     */
    ED25519_KEY_RESTRICTION(Integer.class),

    /**
     * (PREMIUM | SILVER) Enable the use of AWS hosted Elasticsearch
     */    
    ELASTICSEARCH_AWS(Boolean.class),

    /**
     * (PREMIUM | SILVER) AWS IAM access key
     */
    ELASTICSEARCH_AWS_ACCESS_KEY(String.class),

    /**
     * (PREMIUM | SILVER) The AWS region the Elasticsearch domain is configured
     */
    ELASTICSEARCH_AWS_REGION(String.class),

    /**
     * (PREMIUM | SILVER) AWS IAM secret access key
     */
    ELASTICSEARCH_AWS_SECRET_ACCESS_KEY(String.class),

    /**
     * Amazon Access Key.
     */
    EKS_ACCESS_KEY_ID(String.class),

    /**
     * Amazon account ID
     */
    EKS_ACCOUNT_ID(String.class),

    /**
     * Enable integration with Amazon EKS.
     */
    EKS_INTEGRATION_ENABLED(Boolean.class),

    /**
     * AWS IAM secret access key
     */
    EKS_SECRET_ACCESS_KEY(String.class),

    /**
     * (PREMIUM | SILVER) Use the experimental elasticsearch indexer. More info: 
     * https://gitlab.com/gitlab-org/gitlab-elasticsearch-indexer
     * Ruby indexer was removed and go indexer is no more experimental.
     * @deprecated removed in Gitlab 12.3. see https://gitlab.com/gitlab-org/gitlab/commit/82ba4a6a5c78501413012a9f2a918aa7353917a0?view=parallel#fbf64e6b8170f05f1b940fb05902d29f9eba3633_223_223
     */
    @Deprecated
    ELASTICSEARCH_EXPERIMENTAL_INDEXER(Boolean.class),
    
    /**
     * (PREMIUM | SILVER) Enable Elasticsearch indexing
     */
    ELASTICSEARCH_INDEXING(Boolean.class),

    /**
     * (PREMIUM | SILVER) Limit Elasticsearch to index certain namespaces and
     * projects
     */
    ELASTICSEARCH_LIMIT_INDEXING(Boolean.class),

    /**
     * (PREMIUM | SILVER) The namespaces to index via Elasticsearch if 
     * {@link #ELASTICSEARCH_LIMIT_INDEXING} is enabled.
     */
    ELASTICSEARCH_NAMESPACE_IDS(Integer[].class),

    /**
     * (PREMIUM | SILVER) The projects to index via Elasticsearch if
     * {@link #ELASTICSEARCH_LIMIT_INDEXING} is enabled.
     */
    ELASTICSEARCH_PROJECT_IDS(Integer[].class),

    /**
     * (PREMIUM | SILVER) Enable Elasticsearch search
     */
    ELASTICSEARCH_SEARCH(Boolean.class),

    /**
     * (PREMIUM | SILVER) The url to use for connecting to Elasticsearch.
     * Use a comma-separated list to support cluster (e.g., http://localhost:9200,
     * http://localhost:9201"). If your Elasticsearch instance is password
     * protected, pass the username:password in the URL (e.g., http://username:password@elastic_host:9200/).
     */
    ELASTICSEARCH_URL(String.class),

    /**
     * (PREMIUM | SILVER) Additional text added to the bottom of every email for
     * legal/auditing/compliance reasons
     */
    EMAIL_ADDITIONAL_TEXT(String.class),

    /**
     * Some email servers do not support overriding the email sender name. Enable
     * this option to include the name of the author of the issue, merge request or
     * comment in the email body instead.
     */
    EMAIL_AUTHOR_IN_BODY(Boolean.class),

    /**
     * Enabled protocols for Git access. Allowed values are: ssh, http, and nil to
     * allow both protocols.
     */
    ENABLED_GIT_ACCESS_PROTOCOL(String.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #TERMS}) Enforce application
     * ToS to all users.
     */
    ENFORCE_TERMS(Boolean.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #EXTERNAL_AUTH_CLIENT_KEY}) The
     * certificate to use to authenticate with the external authorization service
     */
    EXTERNAL_AUTH_CLIENT_CERT(String.class),

    /**
     * required by: {@link #EXTERNAL_AUTH_CLIENT_CERT} Private key for the certificate
     * when authentication is required for the external authorization service, this is
     * encrypted when stored
     */
    EXTERNAL_AUTH_CLIENT_KEY(String.class),

    /**
     * Passphrase to use for the private key when authenticating with the
     * external service this is encrypted when stored
     */
    EXTERNAL_AUTH_CLIENT_KEY_PASS(String.class),

    /**
     * required by: {@link #EXTERNAL_AUTHORIZATION_SERVICE_ENABLED} The default
     * classification label to use when requesting authorization and no
     * classification label has been specified on the project
     */
    EXTERNAL_AUTHORIZATION_SERVICE_DEFAULT_LABEL(String.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #EXTERNAL_AUTHORIZATION_SERVICE_DEFAULT_LABEL},
     * {@link #EXTERNAL_AUTHORIZATION_SERVICE_TIMEOUT} and {@link #EXTERNAL_AUTHORIZATION_SERVICE_URL})
     * Enable using an external authorization service for accessing projects
     */
    EXTERNAL_AUTHORIZATION_SERVICE_ENABLED(Boolean.class),

    /**
     * required by: {@link #EXTERNAL_AUTHORIZATION_SERVICE_ENABLED} The timeout after which an
     * authorization request is aborted, in seconds. When a request times out, access is denied
     * to the user. (min: 0.001, max: 10, step: 0.001)
     */
    EXTERNAL_AUTHORIZATION_SERVICE_TIMEOUT(Float.class),

    /**
     * required by: {@link #EXTERNAL_AUTHORIZATION_SERVICE_ENABLED} URL to which authorization
     * requests will be directed
     */
    EXTERNAL_AUTHORIZATION_SERVICE_URL(String.class),

    /**
     * (PREMIUM | SILVER) The ID of a project to load custom file templates from
     */
    FILE_TEMPLATE_PROJECT_ID(Integer.class),

    /**
     * Start day of the week for calendar views and date pickers. Valid values are 0
     * (default) for Sunday, 1 for Monday, and 6 for Saturday.
     */
    FIRST_DAY_OF_WEEK(Integer.class),

    /**
     * (PREMIUM | SILVER) Comma-separated list of IPs and CIDRs of allowed secondary nodes.
     * For example, 1.1.1.1, 2.2.2.0/24.
     */
    GEO_NODE_ALLOWED_IPS(String.class),

    /**
     * (PREMIUM | SILVER) The amount of seconds after which a request to get a secondary node
     * status will time out.
     */
    GEO_STATUS_TIMEOUT(Integer.class),

    /**
     * Default Gitaly timeout, in seconds. This timeout is not enforced for Git
     * fetch/push operations or Sidekiq jobs. Set to 0 to disable timeouts.
     */
    GITALY_TIMEOUT_DEFAULT(Integer.class),

    /**
     * Gitaly fast operation timeout, in seconds. Some Gitaly operations are
     * expected to be fast. If they exceed this threshold, there may be a problem
     * with a storage shard and ‘failing fast’ can help maintain the stability of
     * the GitLab instance. Set to 0 to disable timeouts.
     */
    GITALY_TIMEOUT_FAST(Integer.class),

    /**
     * Medium Gitaly timeout, in seconds. This should be a value between the Fast
     * and the Default timeout. Set to 0 to disable timeouts.
     */
    GITALY_TIMEOUT_MEDIUM(Integer.class),

    /**
     * Enable Grafana.
     */
    GRAFANA_ENABLED(Boolean.class),

    /**
     * Grafana URL.
     */
    GRAFANA_URL(String.class),

    /**
     * Enable Gravatar.
     */
    GRAVATAR_ENABLED(Boolean.class),

    /**
     * Create new projects using hashed storage paths: Enable immutable, hash-based paths
     * and repository names to store repositories on disk. This prevents repositories from
     * having to be moved or renamed when the Project URL changes and may improve disk I/O
     * performance. (EXPERIMENTAL)
     */
    HASHED_STORAGE_ENABLED(Boolean.class),

    /**
     * Hide marketing-related entries from help.
     */
    HELP_PAGE_HIDE_COMMERCIAL_CONTENT(Boolean.class),

    /**
     * Alternate support URL for help page and help dropdown.
     */
    HELP_PAGE_SUPPORT_URL(String.class),

    /**
     * Custom text displayed on the help page.
     */
    HELP_PAGE_TEXT(String.class),

    /**
     * (PREMIUM | SILVER) GitLab server administrator information
     */
    HELP_TEXT(String.class),

    /**
     * Do not display offers from third parties within GitLab.
     */
    HIDE_THIRD_PARTY_OFFERS(Boolean.class),

    /**
     * Redirect to this URL when not logged in.
     */
    HOME_PAGE_URL(String.class),

    /**
     * required by: {@link #HOUSEKEEPING_ENABLED} Enable Git pack file bitmap creation.
     */
    HOUSEKEEPING_BITMAPS_ENABLED(Boolean.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #HOUSEKEEPING_BITMAPS_ENABLED}, 
     * {@link #HOUSEKEEPING_FULL_REPACK_PERIOD}, {@link #HOUSEKEEPING_GC_PERIOD}, and
     * {@link #HOUSEKEEPING_INCREMENTAL_REPACK_PERIOD}) Enable or disable Git housekeeping.
     */
    HOUSEKEEPING_ENABLED(Boolean.class),

    /**
     * required by: {@link #HOUSEKEEPING_ENABLED} Number of Git pushes after which an
     * incremental git repack is run.
     */
    HOUSEKEEPING_FULL_REPACK_PERIOD(Integer.class),

    /**
     * required by: {@link #HOUSEKEEPING_ENABLED} Number of Git pushes after which git
     * gc is run.
     */
    HOUSEKEEPING_GC_PERIOD(Integer.class),

    /**
     * required by: {@link #HOUSEKEEPING_ENABLED} Number of Git pushes after which an
     * incremental git repack is run.
     */
    HOUSEKEEPING_INCREMENTAL_REPACK_PERIOD(Integer.class),

    /**
     * Enable HTML emails.
     */
    HTML_EMAILS_ENABLED(Boolean.class),

    /**
     * Sources to allow project import from, possible values: github, bitbucket,
     * bitbucket_server, gitlab, google_code, fogbugz, git, gitlab_project, gitea,
     * manifest, and phabricator.
     */
    IMPORT_SOURCES(String[].class),

    /**
     * When set to true Instance statistics will only be available to admins.
     */
    INSTANCE_STATISTICS_VISIBILITY_PRIVATE(Boolean.class),

    /**
     * Increase this value when any cached markdown should be invalidated.
     */
    LOCAL_MARKDOWN_VERSION(Integer.class),

    /**
     * NOT DOCUMENTED: but it's returned by a call to /api/v4/application/settings
     * Was added with this commit https://gitlab.com/gitlab-org/gitlab/commit/30e7f01877fd436e21efdf0974d42d8fc83f4883
     * @since 2019-07-18
     */
    LOGIN_RECAPTCHA_PROTECTION_ENABLED(Boolean.class),

    /**
     * Maximum artifacts size in MB
     */
    MAX_ARTIFACTS_SIZE(Integer.class),

    /**
     * Limit attachment size in MB
     */
    MAX_ATTACHMENT_SIZE(Integer.class),

    /**
     * Maximum import size in MB. 0 for unlimited. Default = 50
     */
    MAX_IMPORT_SIZE(Integer.class),

    /**
     * Maximum size of pages repositories in MB
     */
    MAX_PAGES_SIZE(Integer.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #METRICS_HOST}, 
     * {@link #METRICS_METHOD_CALL_THRESHOLD}, {@link #METRICS_PACKET_SIZE}, 
     * {@link #METRICS_POOL_SIZE}, {@link #METRICS_PORT}, {@link #METRICS_SAMPLE_INTERVAL} and
     * {@link #METRICS_TIMEOUT}) Enable influxDB metrics.
     */
    METRICS_ENABLED(Boolean.class),

    /**
     * required by: {@link #METRICS_ENABLED} InfluxDB host.
     */
    METRICS_HOST(String.class),

    /**
     * required by: {@link #METRICS_ENABLED} A method call is only tracked when it takes
     * longer than the given amount of milliseconds.
     */
    METRICS_METHOD_CALL_THRESHOLD(Integer.class),

    /**
     * required by: {@link #METRICS_ENABLED} The amount of datapoints to send in a single UDP
     * packet.
     */
    METRICS_PACKET_SIZE(Integer.class),

    /**
     * required by: {@link #METRICS_ENABLED} The amount of InfluxDB connections to keep open.
     */
    METRICS_POOL_SIZE(Integer.class),

    /**
     * required by: {@link #METRICS_ENABLED} The UDP port to use for connecting to InfluxDB.
     */
    METRICS_PORT(Integer.class),

    /**
     * required by: {@link #METRICS_ENABLED} The sampling interval in seconds.
     */
    METRICS_SAMPLE_INTERVAL(Integer.class),

    /**
     * required by: {@link #METRICS_ENABLED} The amount of seconds after which InfluxDB will
     * time out.
     */
    METRICS_TIMEOUT(Integer.class),

    /**
     * Allow repository mirroring to configured by project Maintainers. If disabled, only
     * Admins will be able to configure repository mirroring.
     */
    MIRROR_AVAILABLE(Boolean.class),

    /**
     * (PREMIUM | SILVER) Minimum capacity to be available before scheduling more mirrors
     * preemptively
     */
    MIRROR_CAPACITY_THRESHOLD(Integer.class),

    /**
     * (PREMIUM | SILVER) Maximum number of mirrors that can be synchronizing at the same time.
     */
    MIRROR_MAX_CAPACITY(Integer.class),

    /**
     * (PREMIUM | SILVER) Maximum time (in minutes) between updates that a mirror can have
     * when scheduled to synchronize.
     */
    MIRROR_MAX_DELAY(Integer.class),

    /**
     * Define a list of trusted domains or ip addresses to which local requests are allowed when
     * local requests for hooks and services are disabled.
     */
    OUTBOUND_LOCAL_REQUESTS_WHITELIST(String[].class),
    
    /**
     * NOT DOCUMENTED: but it's returned by a call to /api/v4/application/settings
     * Added with this commit https://gitlab.com/gitlab-org/gitlab/commit/336046254cfe69d795bc8ea454daaf5a35b60eac
     */
    OUTBOUND_LOCAL_REQUESTS_WHITELIST_RAW(String.class),

    /**
     * Require users to prove ownership of custom domains. Domain verification is an
     * essential security measure for public GitLab sites. Users are required to
     * demonstrate they control a domain before it is enabled.
     */
    PAGES_DOMAIN_VERIFICATION_ENABLED(Boolean.class),

    /**
     * NOT DOCUMENTED: but it's returned by a call to /api/v4/application/settings
     * Present for retro-compatibility purpose. See https://gitlab.com/gitlab-org/gitlab/commit/63b2082979efe182daf78e8269b252ccc73f93fc#958cb0573403da359fda7dac60baf49147a5c538_166_181
     * @deprecated Use {@link #PASSWORD_AUTHENTICATION_ENABLED_FOR_WEB} instead.
     */
    @Deprecated
    PASSWORD_AUTHENTICATION_ENABLED(Boolean.class),

    /**
     * Enable authentication for Git over HTTP(S) via a GitLab account password. Default is true.
     */
    PASSWORD_AUTHENTICATION_ENABLED_FOR_GIT(Boolean.class),

    /**
     * Enable authentication for the web interface via a GitLab account password. Default is true.
     */
    PASSWORD_AUTHENTICATION_ENABLED_FOR_WEB(Boolean.class),

    /**
     * ID of the group that is allowed to toggle the performance bar.
     * @deprecated Use {@link #PERFORMANCE_BAR_ALLOWED_GROUP_PATH} instead.
     */
    @Deprecated
    PERFORMANCE_BAR_ALLOWED_GROUP_ID(Integer.class),

    /**
     * Path of the group that is allowed to toggle the performance bar.
     */
    PERFORMANCE_BAR_ALLOWED_GROUP_PATH(String.class),
    
    /**
     * Allow enabling the performance bar.
     * @deprecated Pass performance_bar_allowed_group_path: nil instead
     */
    @Deprecated
    PERFORMANCE_BAR_ENABLED(Boolean.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #PLANTUML_URL}) Enable PlantUML integration.
     * Default is false.
     */
    PLANTUML_ENABLED(Boolean.class),

    /**
     * required by: {@link #PLANTUML_ENABLED} The PlantUML instance URL for integration.
     */
    PLANTUML_URL(String.class),

    /**
     * Interval multiplier used by endpoints that perform polling. Set to 0 to disable polling.
     * The documentation lists this as a decimal, but it is a String in the JSON.
     */
    POLLING_INTERVAL_MULTIPLIER(String.class),

    /**
     * Enable project export.
     */
    PROJECT_EXPORT_ENABLED(Boolean.class),

    /**
     * Enable Prometheus metrics.
     */
    PROMETHEUS_METRICS_ENABLED(Boolean.class),

    /**
     * Environment variables are protected by default.
     */
    PROTECTED_CI_VARIABLES(Boolean.class),

    /**
     * (PREMIUM | SILVER) When enabled, GitLab will run a background job that will produce
     * pseudonymized CSVs of the GitLab database that will be uploaded to your configured
     * object storage directory.
     */
    PSEUDONYMIZER_ENABLED(Boolean.class),
    
    /**
     * Number of changes (branches or tags) in a single push to determine whether webhooks
     * and services will be fired or not. Webhooks and services won’t be submitted if it
     * surpasses that value.
     */
    PUSH_EVENT_HOOKS_LIMIT(Integer.class),
    
    /**
     * Number of changes (branches or tags) in a single push to determine whether individual
     * push events or bulk push events will be created. 
     * <a href="https://docs.gitlab.com/ee/user/admin_area/settings/push_event_activities_limit.html">
     * Bulk push events will be created</a> if it surpasses that value.
     */
    PUSH_EVENT_ACTIVITIES_LIMIT(Integer.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #RECAPTCHA_PRIVATE_KEY} and 
     * {@link #RECAPTCHA_SITE_KEY}) Enable reCAPTCHA.
     */
    RECAPTCHA_ENABLED(Boolean.class),

    /**
     * required by: {@link #RECAPTCHA_ENABLED} Private key for reCAPTCHA.
     */
    RECAPTCHA_PRIVATE_KEY(String.class),

    /**
     * required by: {@link #RECAPTCHA_ENABLED} Site key for reCAPTCHA.
     */
    RECAPTCHA_SITE_KEY(String.class),

    /**
     * Maximum push size (MB).
     */
    RECEIVE_MAX_INPUT_SIZE(Integer.class),

    /**
     * GitLab will periodically run git fsck in all project and wiki repositories to
     * look for silent disk corruption issues.
     */
    REPOSITORY_CHECKS_ENABLED(Boolean.class),

    /**
     * (PREMIUM | SILVER) Size limit per repository (MB)
     */
    REPOSITORY_SIZE_LIMIT(Integer.class),

    /**
     * A list of names of enabled storage paths, taken from gitlab.yml. New projects
     * will be created in one of these stores, chosen at random.
     */
    REPOSITORY_STORAGES(String[].class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #TWO_FACTOR_GRACE_PERIOD}) Require all
     * users to set up Two-factor authentication.
     */
    REQUIRE_TWO_FACTOR_AUTHENTICATION(Boolean.class),

    /**
     * Selected levels cannot be used by non-admin users for groups, projects or
     * snippets. Can take private, internal and public as a parameter. Default is
     * null which means there is no restriction.
     */
    RESTRICTED_VISIBILITY_LEVELS(String[].class),

    /**
     * The minimum allowed bit length of an uploaded RSA key. Default is 0 (no
     * restriction). -1 disables RSA keys.
     */
    RSA_KEY_RESTRICTION(Integer.class),

    /**
     * Send confirmation email on sign-up.
     */
    SEND_USER_CONFIRMATION_EMAIL(Boolean.class),

    /**
     * Session duration in minutes. GitLab restart is required to apply changes
     */
    SESSION_EXPIRE_DELAY(Integer.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #SHARED_RUNNERS_TEXT} and
     * {@link #SHARED_RUNNERS_MINUTES}) Enable shared runners for new projects.
     */
    SHARED_RUNNERS_ENABLED(Boolean.class),

    /**
     * (PREMIUM | SILVER) required by: {@link #SHARED_RUNNERS_ENABLED} Set the maximum number
     * of pipeline minutes that a group can use on shared Runners per month.
     */
    SHARED_RUNNERS_MINUTES(Integer.class), 
    
    /**
     * required by: {@link #SHARED_RUNNERS_ENABLED} Shared runners text.
     */
    SHARED_RUNNERS_TEXT(String.class),
    
    /**
     * Text on the login page.
     */
    SIGN_IN_TEXT(String.class),

    /**
     * Flag indicating if password authentication is enabled for the web interface.
     * Documentation lists this as a String, but it s a Boolean.
     * @deprecated Use {@link #PASSWORD_AUTHENTICATION_ENABLED_FOR_WEB} instead
     */
    @Deprecated
    SIGNIN_ENABLED(Boolean.class),

    /**
     * Enable registration. Default is true.
     */
    SIGNUP_ENABLED(Boolean.class),

    /**
     * (PREMIUM | SILVER) (<strong>If enabled, requires:</strong> {@link #SLACK_APP_ID},
     * {@link #SLACK_APP_SECRET} and {@link #SLACK_APP_VERIFICATION_TOKEN}) Enable Slack app.
     */
    SLACK_APP_ENABLED(Boolean.class),

    /**
     * (PREMIUM | SILVER) required by: {@link #SLACK_APP_ENABLED} The app id of the Slack-app.
     */
    SLACK_APP_ID(String.class),
    
    /**
     * (PREMIUM | SILVER) required by: {@link #SLACK_APP_ENABLED} The app secret of the
     * Slack-app.
     */
    SLACK_APP_SECRET(String.class),
    
    /**
     * (PREMIUM | SILVER) required by: {@link #SLACK_APP_ENABLED}  The verification token of
     * the Slack-app.
     */
    SLACK_APP_VERIFICATION_TOKEN(String.class),

    /**
     * The Snowplow site name / application id. (e.g. gitlab)
     */
    SNOWPLOW_APP_ID(String.class),

    /**
     * required by: {@link #SNOWPLOW_ENABLED} The Snowplow collector hostname.
     * (e.g. snowplow.trx.gitlab.net)
     */
    SNOWPLOW_COLLECTOR_HOSTNAME(String.class),

    /**
     * The Snowplow cookie domain. (e.g. .gitlab.com)
     */
    SNOWPLOW_COOKIE_DOMAIN(String.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #SNOWPLOW_COLLECTOR_HOSTNAME}) 
     * Enable snowplow tracking.
     */
    SNOWPLOW_ENABLED(Boolean.class),

    /**
     * The Snowplow base Iglu Schema Registry URL to use for custom context and self describing events.
     */
    SNOWPLOW_IGLU_REGISTRY_URL(String.class),

    /**
     * The Snowplow site name / application id. (e.g. gitlab)
     */
    SNOWPLOW_SITE_ID(String.class),

    /**
     * Enables Sourcegraph integration. Default is false. If enabled, requires sourcegraph_url.
     */
    SOURCEGRAPH_ENABLED(Boolean.class),

    /**
     * Blocks Sourcegraph from being loaded on private and internal projects. Defaul is true.
     */
    SOURCEGRAPH_PUBLIC_ONLY(Boolean.class),

    /**
     * The Sourcegraph instance URL for integration.
     */
    SOURCEGRAPH_URL(String.class),

    /**
     * Enables Spam Check via external API endpoint. Default is false.
     */
    SPAM_CHECK_ENDPOINT_ENABLED(Boolean.class),

    /**
     * URL of the external Spam Check service endpoint.
     */
    SPAM_CHECK_ENDPOINT_URL(String.class),

    /**
     * required by: {@link #PENDO_ENABLED} The Pendo endpoint url with js snippet. 
     * (e.g. https://cdn.pendo.io/agent/static/your-api-key/pendo.js)
     */
    PENDO_URL(String.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #PENDO_URL}) Enable pendo tracking.
     */
    PENDO_ENABLED(Boolean.class),

    /**
     * NOT DOCUMENTED: but it's returned by a call to /api/v4/application/settings
     * https://gitlab.com/gitlab-org/gitlab/commit/85975447a2b70d1654f2f8163f55d369e130ef2b
     */
    STATIC_OBJECTS_EXTERNAL_STORAGE_AUTH_TOKEN(String.class),

    /**
     * NOT DOCUMENTED: but it's returned by a call to /api/v4/application/settings
     * https://gitlab.com/gitlab-org/gitlab/commit/85975447a2b70d1654f2f8163f55d369e130ef2b
     */
    STATIC_OBJECTS_EXTERNAL_STORAGE_URL(String.class),

    /**
     * Maximum time for web terminal websocket connection (in seconds). Set to 0 for
     * unlimited time.
     */
    TERMINAL_MAX_SESSION_TIME(Integer.class),

    /**
     * required by: {@link #ENFORCE_TERMS} Markdown content for the ToS.
     */
    TERMS(String.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #THROTTLE_AUTHENTICATED_API_PERIOD_IN_SECONDS}
     * and {@link #THROTTLE_AUTHENTICATED_API_REQUESTS_PER_PERIOD}) Enable authenticated API
     * request rate limit. Helps reduce request volume (e.g. from crawlers or abusive bots).
     */
    THROTTLE_AUTHENTICATED_API_ENABLED(Boolean.class),

    /**
     * required by: {@link #THROTTLE_AUTHENTICATED_API_ENABLED} Rate limit period in seconds.
     */
    THROTTLE_AUTHENTICATED_API_PERIOD_IN_SECONDS(Integer.class),

    /**
     * required by: {@link #THROTTLE_AUTHENTICATED_API_ENABLED} Max requests per period per user.
     */
    THROTTLE_AUTHENTICATED_API_REQUESTS_PER_PERIOD(Integer.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #THROTTLE_AUTHENTICATED_WEB_PERIOD_IN_SECONDS}
     * and {@link #THROTTLE_AUTHENTICATED_WEB_REQUESTS_PER_PERIOD}) Enable authenticated web
     * request rate limit. Helps reduce request volume (e.g. from crawlers or abusive bots).
     */
    THROTTLE_AUTHENTICATED_WEB_ENABLED(Boolean.class),

    /**
     * required by: {@link #THROTTLE_AUTHENTICATED_WEB_ENABLED}	Rate limit period in seconds.
     */
    THROTTLE_AUTHENTICATED_WEB_PERIOD_IN_SECONDS(Integer.class),

    /**
     * required by: {@link #THROTTLE_AUTHENTICATED_WEB_ENABLED} Max requests per period per user.
     */
    THROTTLE_AUTHENTICATED_WEB_REQUESTS_PER_PERIOD(Integer.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #THROTTLE_UNAUTHENTICATED_PERIOD_IN_SECONDS}
     * and {@link #THROTTLE_UNAUTHENTICATED_REQUESTS_PER_PERIOD}) Enable unauthenticated request
     * rate limit. Helps reduce request volume (e.g. from crawlers or abusive bots).
     */
    THROTTLE_UNAUTHENTICATED_ENABLED(Boolean.class),

    /**
     * required by: {@link #THROTTLE_UNAUTHENTICATED_ENABLED} Rate limit period in seconds.
     */
    THROTTLE_UNAUTHENTICATED_PERIOD_IN_SECONDS(Integer.class),

    /**
     * required by: {@link #THROTTLE_UNAUTHENTICATED_ENABLED} Max requests per period per IP.
     */
    THROTTLE_UNAUTHENTICATED_REQUESTS_PER_PERIOD(Integer.class),

    /**
     * Limit display of time tracking units to hours. Default is false.
     */
    TIME_TRACKING_LIMIT_TO_HOURS(Boolean.class),

    /**
     * required by: {@link #REQUIRE_TWO_FACTOR_AUTHENTICATION} Amount of time (in hours) that
     * users are allowed to skip forced configuration of two-factor authentication.
     */
    TWO_FACTOR_GRACE_PERIOD(Integer.class),

    /**
     * (<strong>If enabled, requires:</strong> {@link #UNIQUE_IPS_LIMIT_PER_USER} and
     * {@link #UNIQUE_IPS_LIMIT_TIME_WINDOW}) Limit sign in from multiple ips.
     */
    UNIQUE_IPS_LIMIT_ENABLED(Boolean.class),

    /**
     * required by: {@link #UNIQUE_IPS_LIMIT_ENABLED} Maximum number of ips per user.
     */
    UNIQUE_IPS_LIMIT_PER_USER(Integer.class),

    /**
     * required by: {@link #UNIQUE_IPS_LIMIT_ENABLED} How many seconds an IP will be
     * counted towards the limit.
     */
    UNIQUE_IPS_LIMIT_TIME_WINDOW(Integer.class),

    /**
     * Every week GitLab will report license usage back to GitLab, Inc.
     */
    USAGE_PING_ENABLED(Boolean.class),

    /**
     * Newly registered users will be external by default.
     */
    USER_DEFAULT_EXTERNAL(Boolean.class),

    /**
     * Specify an e-mail address regex pattern to identify default internal users.
     */
    USER_DEFAULT_INTERNAL_REGEX(String.class),

    /**
     * Allow users to register any application to use GitLab as an OAuth provider.
     */
    USER_OAUTH_APPLICATIONS(Boolean.class),

    /**
     * When set to false disable the “You won’t be able to pull or push project code
     * via SSH” warning shown to users with no uploaded SSH key.
     */
    USER_SHOW_ADD_SSH_KEY_MESSAGE(Boolean.class),

    /**
     * Let GitLab inform you when an update is available.
     */
    VERSION_CHECK_ENABLED(Boolean.class),

    /**
     * Client side evaluation (allow live previews of JavaScript projects in the Web IDE
     * using CodeSandbox client side evaluation).
     */
    WEB_IDE_CLIENTSIDE_PREVIEW_ENABLED(Boolean.class),

    /*
     * Undocumented settings as of GitLab 12.4
     * These are reported but not documented.
     */
    CUSTOM_HTTP_CLONE_URL_ROOT(String.class),
    PROTECTED_PATHS_RAW(String.class),
    THROTTLE_PROTECTED_PATHS_ENABLED(Boolean.class),
    THROTTLE_PROTECTED_PATHS_PERIOD_IN_SECONDS(Integer.class),
    THROTTLE_PROTECTED_PATHS_REQUESTS_PER_PERIOD(Integer.class),
  
    /*
     * Undocumented settings as of GitLab 12.8
     * These are reported but not documented.
     */
    FORCE_PAGES_ACCESS_CONTROL(Boolean.class),
    MINIMUM_PASSWORD_LENGTH(Integer.class),
    SNIPPET_SIZE_LIMIT(Integer.class),

    /*
     * Undocumented settings as of GitLab 12.9
     * These are reported but not documented.
     */
    EMAIL_RESTRICTIONS_ENABLED(Boolean.class),
    EMAIL_RESTRICTIONS(String.class),

    /*
     * Undocumented settings as of GitLab 13.0
     * These are reported but not documented.
     */
    CONTAINER_EXPIRATION_POLICIES_ENABLE_HISTORIC_ENTRIES(Boolean.class),
    ISSUES_CREATE_LIMIT(Integer.class),
    RAW_BLOB_REQUEST_LIMIT(Integer.class);


    private static JacksonJsonEnumHelper<Setting> enumHelper = new JacksonJsonEnumHelper<>(Setting.class);

    private Class<?> type;
    private Class<?>[] types;
    private Setting(Class<?> type) {
        this.type = type;
    }

    private Setting(Class<?>[] types) {
        this.types = types;
    }

    @JsonCreator
    public static Setting forValue(String value) {
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

    /**
     * Returns true if the provided value is of the correct type specified by this ApplicationSetting enum,
     * otherwise returns false.
     *
     * @param value the value to validate
     * @return true if the value is of the correct type or null
     */
    public final boolean isValid(Object value) {

	if (value == null) {
	    return (true);
	}

	Class<?> valueType = value.getClass();
	if (type != null) {
	    return (valueType == type);
	}
	
	for (Class<?> type : types) {
	    if (valueType == type) {
		return (true);
	    }
	}

	return (false);
    }

    /**
     * Validates the provided value against the data type of this ApplicationSetting enum.
     * Will throw a GitLabApiException if the value is not of the correct type.
     *
     * @param value the value to validate
     * @throws GitLabApiException if the provided value is not a valid type for the ApplicationSetting
     */
    public final void validate(Object value) throws GitLabApiException {

        if (isValid(value)) {
            return;
        }

        StringBuilder shouldBe;
        if (type != null) {
            shouldBe = new StringBuilder(type.getSimpleName());
        } else {
            shouldBe = new StringBuilder(types[0].getSimpleName());
            for (int i = 1; i < types.length; i++) {
                shouldBe.append(" | ").append(types[i].getSimpleName());
            }
        }

        String errorMsg = String.format("'%s' value is of incorrect type, is %s, should be %s",
                toValue(), value.getClass().getSimpleName(), shouldBe.toString());
        throw new GitLabApiException(errorMsg);
    }
}
