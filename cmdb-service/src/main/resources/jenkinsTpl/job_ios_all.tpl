<?xml version='1.0' encoding='UTF-8'?>
<project>
  <actions/>
  <description></description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <jenkins.model.BuildDiscarderProperty>
      <strategy class="hudson.tasks.LogRotator">
        <daysToKeep>1</daysToKeep>
        <numToKeep>1</numToKeep>
        <artifactDaysToKeep>-1</artifactDaysToKeep>
        <artifactNumToKeep>-1</artifactNumToKeep>
      </strategy>
    </jenkins.model.BuildDiscarderProperty>
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
        <hudson.model.StringParameterDefinition>
          <name>bundleId</name>
          <description></description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>packageUrl</name>
          <description></description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>bundleVersion</name>
          <description></description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>packageName</name>
          <description></description>
          <defaultValue></defaultValue>
        </hudson.model.StringParameterDefinition>
      </parameterDefinitions>
    </hudson.model.ParametersDefinitionProperty>
  </properties>
  <scm class="hudson.plugins.git.GitSCM" plugin="git@3.7.0">
    <configVersion>2</configVersion>
    <userRemoteConfigs>
      <hudson.plugins.git.UserRemoteConfig>
        <url>git@gitlab.51xianqu.com:\$project/\$repo.git</url>
        <credentialsId>c13bfefa-b00b-4d2c-a6a3-7df7123295bf</credentialsId>
      </hudson.plugins.git.UserRemoteConfig>
    </userRemoteConfigs>
    <branches>
      <hudson.plugins.git.BranchSpec>
        <name>\$mbranch</name>
      </hudson.plugins.git.BranchSpec>
    </branches>
    <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
    <browser class="hudson.plugins.git.browser.GitLab">
      <url></url>
      <version>8.7</version>
    </browser>
    <submoduleCfg class="list"/>
    <extensions>
      <hudson.plugins.git.extensions.impl.SubmoduleOption>
        <disableSubmodules>false</disableSubmodules>
        <recursiveSubmodules>true</recursiveSubmodules>
        <trackingSubmodules>false</trackingSubmodules>
        <reference></reference>
        <parentCredentials>false</parentCredentials>
      </hudson.plugins.git.extensions.impl.SubmoduleOption>
      <hudson.plugins.git.extensions.impl.CleanBeforeCheckout/>
    </extensions>
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
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export MATCH_PASSWORD=shandiangou



./sf_build.sh</command>
    </hudson.tasks.Shell>
    <hudson.tasks.Shell>
      <command>#!/bin/bash
############# build package.plist
if [[ ! -d \${WORKSPACE}/build ]] ; then
  mkdir -p \${WORKSPACE}/build
fi

cat &lt;&lt; EOF &gt; \${WORKSPACE}/build/package.plist
&lt;? xml version = &quot;1.0&quot; encoding = &quot;UTF-8&quot;?&gt;
  &lt;plist version=&quot;1.0&quot;&gt;
  &lt;dict&gt;
  &lt;key&gt;items&lt;/key&gt;
    &lt;array&gt;
      &lt;dict&gt;
        &lt;key&gt;assets&lt;/key&gt;
          &lt;array&gt;
            &lt;dict&gt;
              &lt;key&gt;kind&lt;/key&gt;
                &lt;string&gt;software-package&lt;/string&gt;
                &lt;key&gt;url&lt;/key&gt;
                &lt;string&gt;\${packageUrl}&lt;/string&gt;
            &lt;/dict&gt;
          &lt;/array&gt;
        &lt;key&gt;metadata&lt;/key&gt;
          &lt;dict&gt;
            &lt;key&gt;bundle-identifier&lt;/key&gt;
            &lt;string&gt;\${bundleId}&lt;/string&gt;
            &lt;key&gt;bundle-version&lt;/key&gt;
            &lt;string&gt;\${bundleVersion}&lt;/string&gt;
            &lt;key&gt;kind&lt;/key&gt;
            &lt;string&gt;software&lt;/string&gt;
            &lt;key&gt;title&lt;/key&gt;
            &lt;string&gt;iOS-package&lt;/string&gt;
          &lt;/dict&gt;
          &lt;/dict&gt;
    &lt;/array&gt;
  &lt;/dict&gt;
&lt;/plist&gt;
EOF
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
cp -r \${WORKSPACE}/build \${ossPath} --config-file \
\${ossutilPath}/config

</command>
    </hudson.tasks.Shell>
  </builders>
  <publishers>
    <hudson.tasks.ArtifactArchiver>
      <artifacts>build/package.plist</artifacts>
      <allowEmptyArchive>false</allowEmptyArchive>
      <onlyIfSuccessful>true</onlyIfSuccessful>
      <fingerprint>false</fingerprint>
      <defaultExcludes>true</defaultExcludes>
      <caseSensitive>true</caseSensitive>
    </hudson.tasks.ArtifactArchiver>
  </publishers>
  <buildWrappers>
    <hudson.plugins.ansicolor.AnsiColorBuildWrapper plugin="ansicolor@0.5.2">
      <colorMapName>xterm</colorMapName>
    </hudson.plugins.ansicolor.AnsiColorBuildWrapper>
    <com.sic.plugins.kpp.KPPKeychainsBuildWrapper plugin="kpp-management-plugin@1.0.0">
      <keychainCertificatePairs>
        <com.sic.plugins.kpp.model.KPPKeychainCertificatePair>
          <keychain>login.keychain</keychain>
          <codeSigningIdentity></codeSigningIdentity>
          <varPrefix></varPrefix>
        </com.sic.plugins.kpp.model.KPPKeychainCertificatePair>
      </keychainCertificatePairs>
      <deleteKeychainsAfterBuild>false</deleteKeychainsAfterBuild>
      <overwriteExistingKeychains>true</overwriteExistingKeychains>
    </com.sic.plugins.kpp.KPPKeychainsBuildWrapper>
    <com.sic.plugins.kpp.KPPProvisioningProfilesBuildWrapper plugin="kpp-management-plugin@1.0.0">
      <provisioningProfiles>
        <com.sic.plugins.kpp.model.KPPProvisioningProfile>
          <fileName>e5a6d291-5e31-4fcb-9f81-148772ad4bd8.mobileprovision (e5a6d291-5e31-4fcb-9f81-148772ad4bd8)</fileName>
          <varPrefix></varPrefix>
        </com.sic.plugins.kpp.model.KPPProvisioningProfile>
      </provisioningProfiles>
      <deleteProfilesAfterBuild>false</deleteProfilesAfterBuild>
      <overwriteExistingProfiles>false</overwriteExistingProfiles>
    </com.sic.plugins.kpp.KPPProvisioningProfilesBuildWrapper>
  </buildWrappers>
</project>