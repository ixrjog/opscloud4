<?xml version='1.0' encoding='UTF-8'?>
<matrix-project plugin="matrix-project@1.12">
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
          <timeout>10000</timeout>
          <loglines>0</loglines>
          <retries>3</retries>
        </com.tikal.hudson.plugins.notification.Endpoint>
      </endpoints>
    </com.tikal.hudson.plugins.notification.HudsonNotificationProperty>
    <hudson.model.ParametersDefinitionProperty>
      <parameterDefinitions>
        <net.uaznia.lukanus.hudson.plugins.gitparameter.GitParameterDefinition plugin="git-parameter@0.9.0">
          <name>mbranch</name>
          <description>多分枝选择</description>
          <uuid>244fdcda-f76f-4ad0-927b-b62f72589c07</uuid>
          <type>PT_BRANCH_TAG</type>
          <branch></branch>
          <tagFilter>*</tagFilter>
          <branchFilter>.*</branchFilter>
          <sortMode>NONE</sortMode>
          <defaultValue></defaultValue>
          <selectedValue>NONE</selectedValue>
          <quickFilterEnabled>false</quickFilterEnabled>
        </net.uaznia.lukanus.hudson.plugins.gitparameter.GitParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>deployPath</name>
          <description></description>
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
          <scm class="hudson.plugins.git.GitSCM" plugin="git@3.6.4">
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
            <submoduleCfg class="list"/>
            <extensions/>
          </scm>
          <canRoam>true</canRoam>
          <disabled>false</disabled>
          <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
          <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
          <triggers/>
          <concurrentBuild>false</concurrentBuild>
          <axes/>
          <builders>
            <hudson.tasks.Shell>
                  <command>#!/bin/bash
/usr/local/prometheus/tools/ft_build.sh -x</command>
                </hudson.tasks.Shell>
                <hudson.tasks.Shell>
                  <command>#!/bin/bash
############# upload to oss
ossPath=&quot;oss://\${deployPath}/build&quot;

echo &quot;ossPath: \${ossPath}&quot;
echo &quot;WORKSPACE: \${WORKSPACE}&quot;

/data/www/install/ossutil/ossutil64 \
cp -r -f \${WORKSPACE}/build \${ossPath} --config-file \
/data/www/install/ossutil/config
# &gt;&gt;  /dev/null
#echo &quot; server path : &quot; \${ossPath}
#onlinePrefix=&quot;http://cdn.52shangou.com/goup/name/&quot;</command>
                </hudson.tasks.Shell>
              </builders>
              <publishers/>
              <buildWrappers>
                <hudson.plugins.ansicolor.AnsiColorBuildWrapper plugin="ansicolor@0.5.2">
                  <colorMapName>xterm</colorMapName>
                </hudson.plugins.ansicolor.AnsiColorBuildWrapper>
              </buildWrappers>
              <executionStrategy class="hudson.matrix.DefaultMatrixExecutionStrategyImpl">
                <runSequentially>false</runSequentially>
              </executionStrategy>
</matrix-project>