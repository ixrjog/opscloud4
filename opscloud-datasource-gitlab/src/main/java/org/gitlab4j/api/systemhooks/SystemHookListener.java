
package org.gitlab4j.api.systemhooks;

/**
 * This interface defines an event listener for the event fired when
 * a System Hook notification has been received from a GitLab server.
 */
public interface SystemHookListener extends java.util.EventListener {

    /**
     * This method is called when a System Hook prject event has been received.
     *
     * @param event the ProjectSystemHookEvent instance
     */
    default void onProjectEvent(ProjectSystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook team member event has been received.
     *
     * @param event the TeamMemberSystemHookEvent instance containing info on the team member event
     */
    default void onTeamMemberEvent(TeamMemberSystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook user event has been received.
     *
     * @param event the UserSystemHookEvent instance containing info on the user event
     */
    default void onUserEvent(UserSystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook key event has been received.
     *
     * @param event the KeySystemHookEvent instance containing info on the key event
     */
    default void onKeyEvent(KeySystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook group event has been received.
     *
     * @param event the GroupSystemHookEvent instance containing info on the key event
     */
    default void onGroupEvent(GroupSystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook group member event has been received.
     *
     * @param event the GroupMemberSystemHookEvent instance containing info on the key event
     */
    default void onGroupMemberEvent(GroupMemberSystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook push event has been received.
     *
     * @param event the PushSystemHookEvent instance containing info on the key event
     */
    default void onPushEvent(PushSystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook tag push event has been received.
     *
     * @param event the TagPushSystemHookEvent instance containing info on the key event
     */
    default void onTagPushEvent(TagPushSystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook repository event has been received.
     *
     * @param event the RepositorySystemHookEvent instance containing info on the key event
     */
    default void onRepositoryEvent(RepositorySystemHookEvent event) {
    }

    /**
     * This method is called when a System Hook merge_request event has been received.
     *
     * @param event the MergeRequestSystemHookEvent instance containing info on the key event
     */
    default void onMergeRequestEvent(MergeRequestSystemHookEvent event) {
    }
}
