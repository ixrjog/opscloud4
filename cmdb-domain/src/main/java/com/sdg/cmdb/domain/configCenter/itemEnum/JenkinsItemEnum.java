package com.sdg.cmdb.domain.configCenter.itemEnum;

public enum JenkinsItemEnum {
    
    // private static String OSS_BUCKET_FT_ONLINE="img0-cdn";
    JENKINS_HOST("JENKINS_HOST", "jenkins服务器url"),
    JENKINS_USER("JENKINS_USER", "jenkins登陆账户"),
    JENKINS_PWD("JENKINS_PWD", "jenkins登陆密码"),
    JENKINS_OSS_BUCKET_FT_ONLINE("JENKINS_OSS_BUCKET_FT_ONLINE", "前端构建OSSBucket"),
    JENKINS_FT_BUILD_BRANCH("JENKINS_FT_BUILD_BRANCH", "触发前端构建的branch"),
    REPO_LOCAL_PATH("REPO_LOCAL_PATH", "代码仓库本地临时目录"),
    GITLAB_USER("GITLAB_USER", "gitlab代码仓库管理员账户"),
    GITLAB_PWD("GITLAB_PWD", "gitlab代码仓库管理员密码"),
    GITLAB_HOST("GITLAB_HOST", "gitlab代码仓库服务器url"),
    STASH_USER("STASH_USER", "stash代码仓库管理员账户"),
    STASH_PWD("STASH_PWD", "stash代码仓库管理员账户"),
    STASH_HOST("STASH_HOST", "stash代码仓库服务器url"),
    ANDROID_DEBUG_URL("ANDROID_DEBUG_URL", "stash代码仓库服务器url")
    ;

    private String itemKey;
    private String itemDesc;

    JenkinsItemEnum(String itemKey, String itemDesc) {
        this.itemKey = itemKey;
        this.itemDesc = itemDesc;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemDescByKey(String itemKey) {
        for (JenkinsItemEnum itemEnum : JenkinsItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }

}
