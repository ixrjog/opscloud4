package org.gitlab4j.api.systemhooks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
    visible = true,
    property="event_name")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateProjectSystemHookEvent.class, name = ProjectSystemHookEvent.PROJECT_CREATE_EVENT),
    @JsonSubTypes.Type(value = DestroyProjectSystemHookEvent.class, name = ProjectSystemHookEvent.PROJECT_DESTROY_EVENT),
    @JsonSubTypes.Type(value = RenameProjectSystemHookEvent.class, name = ProjectSystemHookEvent.PROJECT_RENAME_EVENT),
    @JsonSubTypes.Type(value = TransferProjectSystemHookEvent.class, name = ProjectSystemHookEvent.PROJECT_TRANSFER_EVENT),
    @JsonSubTypes.Type(value = UpdateProjectSystemHookEvent.class, name = ProjectSystemHookEvent.PROJECT_UPDATE_EVENT),
    @JsonSubTypes.Type(value = NewTeamMemberSystemHookEvent.class, name = TeamMemberSystemHookEvent.NEW_TEAM_MEMBER_EVENT),
    @JsonSubTypes.Type(value = RemoveTeamMemberSystemHookEvent.class, name = TeamMemberSystemHookEvent.TEAM_MEMBER_REMOVED_EVENT),
    @JsonSubTypes.Type(value = CreateUserSystemHookEvent.class, name = UserSystemHookEvent.USER_CREATE_EVENT),
    @JsonSubTypes.Type(value = DestroyUserSystemHookEvent.class, name = UserSystemHookEvent.USER_DESTROY_EVENT),
    @JsonSubTypes.Type(value = UserFailedLoginSystemHookEvent.class, name = UserSystemHookEvent.USER_FAILED_LOGIN_EVENT),
    @JsonSubTypes.Type(value = RenameUserSystemHookEvent.class, name = UserSystemHookEvent.USER_RENAME_EVENT),
    @JsonSubTypes.Type(value = CreateKeySystemHookEvent.class, name = KeySystemHookEvent.KEY_CREATE_EVENT),
    @JsonSubTypes.Type(value = DestroyKeySystemHookEvent.class, name = KeySystemHookEvent.KEY_DESTROY_EVENT),
    @JsonSubTypes.Type(value = CreateGroupSystemHookEvent.class, name = GroupSystemHookEvent.GROUP_CREATE_EVENT),
    @JsonSubTypes.Type(value = DestroyGroupSystemHookEvent.class, name = GroupSystemHookEvent.GROUP_DESTROY_EVENT),
    @JsonSubTypes.Type(value = RenameGroupSystemHookEvent.class, name = GroupSystemHookEvent.GROUP_RENAME_EVENT),
    @JsonSubTypes.Type(value = NewGroupMemberSystemHookEvent.class, name = GroupMemberSystemHookEvent.NEW_GROUP_MEMBER_EVENT),
    @JsonSubTypes.Type(value = RemoveGroupMemberSystemHookEvent.class, name = GroupMemberSystemHookEvent.GROUP_MEMBER_REMOVED_EVENT),
    @JsonSubTypes.Type(value = PushSystemHookEvent.class, name = PushSystemHookEvent.PUSH_EVENT),
    @JsonSubTypes.Type(value = TagPushSystemHookEvent.class, name = TagPushSystemHookEvent.TAG_PUSH_EVENT),
    @JsonSubTypes.Type(value = RepositorySystemHookEvent.class, name = RepositorySystemHookEvent.REPOSITORY_UPDATE_EVENT),
    @JsonSubTypes.Type(value = MergeRequestSystemHookEvent.class, name = MergeRequestSystemHookEvent.MERGE_REQUEST_EVENT)
})
public interface SystemHookEvent {

    String getEventName();

    void setRequestUrl(String requestUrl);
    @JsonIgnore String getRequestUrl();

    void setRequestQueryString(String requestQueryString);
    @JsonIgnore String getRequestQueryString();

    void setRequestSecretToken(String requestSecretToken);
    @JsonIgnore String getRequestSecretToken();
}

// All of the following class definitions are needed to make the above work.
// Jackson has a tough time mapping the same class to multiple IDs
class CreateProjectSystemHookEvent extends ProjectSystemHookEvent {}
class DestroyProjectSystemHookEvent extends ProjectSystemHookEvent {}
class RenameProjectSystemHookEvent extends ProjectSystemHookEvent {}
class TransferProjectSystemHookEvent extends ProjectSystemHookEvent {}
class UpdateProjectSystemHookEvent extends ProjectSystemHookEvent {}

class NewTeamMemberSystemHookEvent extends TeamMemberSystemHookEvent {}
class RemoveTeamMemberSystemHookEvent extends TeamMemberSystemHookEvent {}

class CreateUserSystemHookEvent extends UserSystemHookEvent {}
class DestroyUserSystemHookEvent extends UserSystemHookEvent {}
class RenameUserSystemHookEvent extends UserSystemHookEvent {}
class UserFailedLoginSystemHookEvent extends UserSystemHookEvent {}

class CreateKeySystemHookEvent extends KeySystemHookEvent {}
class DestroyKeySystemHookEvent extends KeySystemHookEvent {}

class CreateGroupSystemHookEvent extends GroupSystemHookEvent {}
class DestroyGroupSystemHookEvent extends GroupSystemHookEvent {}
class RenameGroupSystemHookEvent extends GroupSystemHookEvent {}

class NewGroupMemberSystemHookEvent extends GroupMemberSystemHookEvent {}
class RemoveGroupMemberSystemHookEvent extends GroupMemberSystemHookEvent {}
