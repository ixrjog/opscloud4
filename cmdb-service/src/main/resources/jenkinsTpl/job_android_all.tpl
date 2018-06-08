<?xml version='1.0' encoding='UTF-8'?>
<project>
  <actions/>
  <description></description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin="gitlab-plugin@1.5.2">
      <gitLabConnection>gitlab</gitLabConnection>
    </com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>
    <com.tikal.hudson.plugins.notification.HudsonNotificationProperty plugin="notification@1.12">
      <endpoints>
        <com.tikal.hudson.plugins.notification.Endpoint>
          <protocol>HTTP</protocol>
          <format>JSON</format>
          <urlInfo>
            <urlOrId>https://work.52shangou.com/jenkins/jobNote</urlOrId>
            <urlType>PUBLIC</urlType>
          </urlInfo>
          <event>all</event>
          <timeout>30000</timeout>
          <loglines>0</loglines>
          <retries>3</retries>
        </com.tikal.hudson.plugins.notification.Endpoint>
      </endpoints>
    </com.tikal.hudson.plugins.notification.HudsonNotificationProperty>
    <hudson.model.ParametersDefinitionProperty>
      <parameterDefinitions>
        <hudson.model.StringParameterDefinition>
          <name>buildParam</name>
          <description>打包参数</description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>mbranch</name>
          <description>分支</description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>buildType</name>
          <description>仅展现</description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>deployPath</name>
          <description>部署路径</description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>project</name>
          <description></description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>repo</name>
          <description></description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
      </parameterDefinitions>
    </hudson.model.ParametersDefinitionProperty>
  </properties>
  <scm class="hudson.plugins.repo.RepoScm" plugin="repo@1.10.7">
      <manifestRepositoryUrl>git@gitlab.51xianqu.com:\$project/\$repo.git</manifestRepositoryUrl>
      <repoUrl>https://mirrors.tuna.tsinghua.edu.cn/git/git-repo/</repoUrl>
      <manifestBranch>\$mbranch</manifestBranch>
      <jobs>0</jobs>
      <depth>0</depth>
      <currentBranch>true</currentBranch>
      <resetFirst>false</resetFirst>
      <quiet>true</quiet>
      <forceSync>false</forceSync>
      <trace>false</trace>
      <showAllChanges>false</showAllChanges>
      <noTags>false</noTags>
      <ignoreProjects class="linked-hash-set">
        <string></string>
      </ignoreProjects>
    </scm>
    <assignedNode>macos</assignedNode>
    <canRoam>false</canRoam>
    <disabled>false</disabled>
    <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
    <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
    <triggers/>
    <concurrentBuild>false</concurrentBuild>
    <builders>
      <hudson.tasks.Shell>
        <command>#!/bin/bash
  if [[ -f &quot;./local.properties&quot; ]] ; then
    rm -f ./local.properties
  fi

  gradle_path=\${GRADLE_3_4_1}/bin/gradle
  echo \${gradle_path}

  echo &quot;JAVA_HOME=\${JAVA_HOME}&quot;
  echo &quot;ANDROID_NDK_HOME=\${ANDROID_NDK_HOME}&quot;
  echo &quot;PATH=\$PATH&quot;
  echo &quot;buildParam=\${buildParam}&quot;
  echo &quot;mbranch=\${mbranch}&quot;

  # buildParam:buildSmallPackageDaily
  \${gradle_path} clean \${buildParam} -DGRADLE_PATH=\${gradle_path}

  </command>
      </hudson.tasks.Shell>
      <hudson.tasks.Shell>
        <command>#!/bin/bash
  ############# upload to oss
  ossPath=&quot;oss://\${deployPath}&quot;
  ossutilPath=/Volumes/data/install/ossutil

  echo &quot;ossPath: \${ossPath}&quot;
  echo &quot;WORKSPACE: \${WORKSPACE}&quot;

  \${ossutilPath}/ossutil \
  cp -r \${WORKSPACE}/app/build/outputs/apk \${ossPath} --config-file \
  \${ossutilPath}/config

  </command>
      </hudson.tasks.Shell>
    </builders>
    <publishers>
      <hudson.tasks.ArtifactArchiver>
        <artifacts>app/build/outputs/apk/*.apk</artifacts>
        <allowEmptyArchive>false</allowEmptyArchive>
        <onlyIfSuccessful>false</onlyIfSuccessful>
        <fingerprint>false</fingerprint>
        <defaultExcludes>true</defaultExcludes>
        <caseSensitive>true</caseSensitive>
      </hudson.tasks.ArtifactArchiver>
    </publishers>
    <buildWrappers>
      <hudson.plugins.ansicolor.AnsiColorBuildWrapper plugin="ansicolor@0.5.2">
        <colorMapName>xterm</colorMapName>
      </hudson.plugins.ansicolor.AnsiColorBuildWrapper>
    </buildWrappers>
  </project>