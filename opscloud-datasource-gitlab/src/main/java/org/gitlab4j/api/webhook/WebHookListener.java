
package org.gitlab4j.api.webhook;

/**
 * This interface defines an event listener for the event fired when
 * a WebHook notification has been received from a GitLab server.
 */
public interface WebHookListener extends java.util.EventListener {

    /**
     * This method is called when a WebHook build event has been received.
     *
     * @param buildEvent the BuildEvent instance
     */
    default void onBuildEvent(BuildEvent buildEvent) {
    }

    /**
     * This method is called when a WebHook issue event has been received.
     *
     * @param event the EventObject instance containing info on the issue
     */
    default void onIssueEvent(IssueEvent event) {
    }

    /**
     * This method is called when a WebHook job event has been received.
     *
     * @param jobEvent the JobEvent instance
     */
    default void onJobEvent(JobEvent jobEvent) {
    }

    /**
     * This method is called when a WebHook merge request event has been received
     *
     * @param event the EventObject instance containing info on the merge request
     */
    default void onMergeRequestEvent(MergeRequestEvent event) {
    }

    /**
     * This method is called when a WebHook note event has been received.
     *
     * @param noteEvent theNoteEvent instance
     */
    default void onNoteEvent(NoteEvent noteEvent) {
    }

    /**
     * This method is called when a WebHook pipeline event has been received.
     *
     * @param pipelineEvent the PipelineEvent instance
     */
    default void onPipelineEvent(PipelineEvent pipelineEvent) {
    }

    /**
     * This method is called when a WebHook push event has been received.
     *
     * @param pushEvent the PushEvent instance
     */
    default void onPushEvent(PushEvent pushEvent) {
    }

    /**
     * This method is called when a WebHook tag push event has been received.
     *
     * @param tagPushEvent the TagPushEvent instance
     */
    default void onTagPushEvent(TagPushEvent tagPushEvent) {
    }

    /**
     * This method is called when a WebHook wiki page event has been received.
     *
     * @param wikiEvent the WikiPageEvent instance
     */
    default void onWikiPageEvent(WikiPageEvent wikiEvent) {
    }



    /**
     * This method is called when a WebHook deployment event has been received.
     *
     * @param deploymentEvent the DeploymentEvent instance
     */
    default void onDeploymentEvent(DeploymentEvent deploymentEvent) {
    }


    /**
     * This method is called when a WebHook release event has been received.
     *
     * @param releaseEvent the ReleaseEvent instance
     */
    default void onReleaseEvent(ReleaseEvent releaseEvent) {
    }
}
