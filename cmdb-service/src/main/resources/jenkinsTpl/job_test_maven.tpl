<?xml version='1.0' encoding='UTF-8'?>
<project>
  <actions/>
  <description></description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty plugin="gitlab-plugin@1.5.5">
      <gitLabConnection>gitlab</gitLabConnection>
    </com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty>
  </properties>
  <scm class="hudson.plugins.git.GitSCM" plugin="git@3.7.0">
    <configVersion>2</configVersion>
    <userRemoteConfigs>
      <hudson.plugins.git.UserRemoteConfig>
        <url>${repositoryUrl}</url>
        <credentialsId>ba974749-86fd-49e4-bd5d-ec2cdce77917</credentialsId>
      </hudson.plugins.git.UserRemoteConfig>
    </userRemoteConfigs>
    <branches>
      <hudson.plugins.git.BranchSpec>
        <name>${mbranch}</name>
      </hudson.plugins.git.BranchSpec>
    </branches>
    <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
    <submoduleCfg class="list"/>
    <extensions/>
  </scm>
  <assignedNode>java</assignedNode>
  <canRoam>true</canRoam>
  <disabled>false</disabled>
  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
  <triggers>
    <hudson.triggers.TimerTrigger>
      <spec>${buildPeriodically}</spec>
    </hudson.triggers.TimerTrigger>
  </triggers>
  <concurrentBuild>false</concurrentBuild>
  <builders>
      <hudson.tasks.Maven>
        <targets>${buildParams}</targets>
        <mavenName>maven-3</mavenName>
        <usePrivateRepository>false</usePrivateRepository>
        <settings class="jenkins.mvn.DefaultSettingsProvider"/>
        <globalSettings class="jenkins.mvn.DefaultGlobalSettingsProvider"/>
        <injectBuildVariables>false</injectBuildVariables>
      </hudson.tasks.Maven>
  </builders>
  <publishers>
    <htmlpublisher.HtmlPublisher plugin="htmlpublisher@1.16">
      <reportTargets>
        <htmlpublisher.HtmlPublisherTarget>
          <reportName>HTML Report</reportName>
          <reportDir>${htmlReport}</reportDir>
          <reportFiles>index.html</reportFiles>
          <alwaysLinkToLastBuild>false</alwaysLinkToLastBuild>
          <reportTitles></reportTitles>
          <keepAll>false</keepAll>
          <allowMissing>false</allowMissing>
          <includes>**/*</includes>
        </htmlpublisher.HtmlPublisherTarget>
      </reportTargets>
    </htmlpublisher.HtmlPublisher>
    <hudson.tasks.junit.JUnitResultArchiver plugin="junit@1.24">
      <testResults>${junitTestResultReport}</testResults>
      <keepLongStdio>false</keepLongStdio>
      <healthScaleFactor>1.0</healthScaleFactor>
      <allowEmptyResults>false</allowEmptyResults>
    </hudson.tasks.junit.JUnitResultArchiver>
    <hudson.plugins.jacoco.JacocoPublisher plugin="jacoco@3.0.1">
          <execPattern>**/**.exec</execPattern>
          <classPattern>**/classes</classPattern>
          <sourcePattern>**/src/main/java</sourcePattern>
          <inclusionPattern></inclusionPattern>
          <exclusionPattern></exclusionPattern>
          <skipCopyOfSrcFiles>false</skipCopyOfSrcFiles>
          <minimumInstructionCoverage>0</minimumInstructionCoverage>
          <minimumBranchCoverage>0</minimumBranchCoverage>
          <minimumComplexityCoverage>0</minimumComplexityCoverage>
          <minimumLineCoverage>0</minimumLineCoverage>
          <minimumMethodCoverage>0</minimumMethodCoverage>
          <minimumClassCoverage>0</minimumClassCoverage>
          <maximumInstructionCoverage>0</maximumInstructionCoverage>
          <maximumBranchCoverage>0</maximumBranchCoverage>
          <maximumComplexityCoverage>0</maximumComplexityCoverage>
          <maximumLineCoverage>0</maximumLineCoverage>
          <maximumMethodCoverage>0</maximumMethodCoverage>
          <maximumClassCoverage>0</maximumClassCoverage>
          <changeBuildStatus>false</changeBuildStatus>
          <deltaInstructionCoverage>0</deltaInstructionCoverage>
          <deltaBranchCoverage>0</deltaBranchCoverage>
          <deltaComplexityCoverage>0</deltaComplexityCoverage>
          <deltaLineCoverage>0</deltaLineCoverage>
          <deltaMethodCoverage>0</deltaMethodCoverage>
          <deltaClassCoverage>0</deltaClassCoverage>
          <buildOverBuild>false</buildOverBuild>
    </hudson.plugins.jacoco.JacocoPublisher>
    <hudson.plugins.emailext.ExtendedEmailPublisher plugin="email-ext@2.62">
      <recipientList>${recipientList}</recipientList>
      <configuredTriggers>
        <hudson.plugins.emailext.plugins.trigger.FailureTrigger>
          <email>
            <subject>\$PROJECT_DEFAULT_SUBJECT</subject>
            <body>\$PROJECT_DEFAULT_CONTENT</body>
            <recipientProviders>
              <hudson.plugins.emailext.plugins.recipients.DevelopersRecipientProvider/>
            </recipientProviders>
            <attachmentsPattern></attachmentsPattern>
            <attachBuildLog>false</attachBuildLog>
            <compressBuildLog>false</compressBuildLog>
            <replyTo>\$PROJECT_DEFAULT_REPLYTO</replyTo>
            <contentType>project</contentType>
          </email>
        </hudson.plugins.emailext.plugins.trigger.FailureTrigger>
        <hudson.plugins.emailext.plugins.trigger.AlwaysTrigger>
          <email>
            <subject>\$PROJECT_DEFAULT_SUBJECT</subject>
            <body>\$PROJECT_DEFAULT_CONTENT</body>
            <recipientProviders>
              <hudson.plugins.emailext.plugins.recipients.DevelopersRecipientProvider/>
              <hudson.plugins.emailext.plugins.recipients.ListRecipientProvider/>
            </recipientProviders>
            <attachmentsPattern></attachmentsPattern>
            <attachBuildLog>false</attachBuildLog>
            <compressBuildLog>false</compressBuildLog>
            <replyTo>\$PROJECT_DEFAULT_REPLYTO</replyTo>
            <contentType>project</contentType>
          </email>
        </hudson.plugins.emailext.plugins.trigger.AlwaysTrigger>
      </configuredTriggers>
      <contentType>text/html</contentType>
      <defaultSubject>\$DEFAULT_SUBJECT</defaultSubject>
      <defaultContent>Hi All,
    &lt;/br&gt;
    &lt;font color=&quot;#0B610B&quot; size=4&gt;检查控制台输出&lt;/font&gt;
&lt;a href=&quot;\${BUILD_URL}console&quot;&gt;&lt;b&gt;&lt;font color=&quot;#DF0101&quot; size=5&gt; \${ENV, var=&quot;JOB_NAME&quot;}&lt;/font&gt;&lt;/b&gt;&lt;/a&gt; &lt;font color=&quot;#0B610B&quot; size=4&gt;查看结果.&lt;/font&gt;
    &lt;html&gt;
    &lt;body leftmargin=&quot;8&quot; marginwidth=&quot;0&quot; topmargin=&quot;8&quot; marginheight=&quot;4&quot; offset=&quot;0&quot;&gt;
    &lt;table width=&quot;95%&quot; cellpadding=&quot;0&quot; cellspacing=&quot;0&quot;  style=&quot;font-size:11pt; font-family:Tahoma, Arial, Helvetica, sans-serif&quot;&gt;
      &lt;tr&gt;
        &lt;td&gt;&lt;h2&gt;&lt;font color=&quot;#0000FF&quot;&gt;构建结果 - \${BUILD_STATUS}&lt;/font&gt;&lt;/h2&gt;&lt;/td&gt;
      &lt;/tr&gt;
      &lt;tr&gt;
        &lt;td&gt;&lt;br/&gt;&lt;b&gt;&lt;font color=&quot;#0B610B&quot;&gt;测试用例执行:&lt;/font&gt;&lt;/b&gt;&lt;hr size=&quot;2&quot; width=&quot;100%&quot; align=&quot;center&quot;/&gt;&lt;/td&gt;
      &lt;/tr&gt;
     &lt;tr&gt;
        &lt;td&gt;
            &lt;ul&gt;
                    &lt;li&gt;&lt;font color=&quot;#0000FF&quot;&gt;Total: \${TEST_COUNTS,var=&quot;total&quot;}&lt;/font&gt;&lt;/li&gt;
                    &lt;li&gt;&lt;font color=&quot;#00FF00&quot;&gt;Pass: \${TEST_COUNTS,var=&quot;pass&quot;}&lt;/font&gt;&lt;/li&gt;
                    &lt;li&gt;&lt;font color=&quot;#DF0101&quot;&gt;Fail: \${TEST_COUNTS,var=&quot;fail&quot;}&lt;/font&gt;&lt;/li&gt;
                    &lt;li&gt;&lt;font color=&quot;#9F9F5F&quot;&gt;Skip: \${TEST_COUNTS,var=&quot;skip&quot;}&lt;/font&gt;&lt;/li&gt;
            &lt;/ul&gt;
        &lt;/td&gt;
      &lt;/tr&gt;
      &lt;tr&gt;
        &lt;td&gt;&lt;br/&gt;&lt;b&gt;&lt;font color=&quot;#0B610B&quot;&gt;构建信息:&lt;/font&gt;&lt;/b&gt;&lt;hr size=&quot;2&quot; width=&quot;100%&quot; align=&quot;center&quot;/&gt;&lt;/td&gt;
      &lt;/tr&gt;
       &lt;tr&gt;
        &lt;td&gt;
            &lt;ul&gt;
                    &lt;li&gt;项目名称 - \${PROJECT_NAME}&lt;/li&gt;
                    &lt;li&gt;构建结果 - &lt;a href=&quot;\${PROJECT_URL}ws&quot;&gt;\${PROJECT_URL}ws&lt;/a&gt;&lt;/li&gt;
                    &lt;li&gt;项目 Url - &lt;a href=&quot;\${PROJECT_URL}&quot;&gt;\${PROJECT_URL}&lt;/a&gt;&lt;/li&gt;
                    &lt;li&gt;构建 Url - &lt;a href=&quot;\${BUILD_URL}&quot;&gt;\${BUILD_URL}&lt;/a&gt;&lt;/li&gt;
            &lt;/ul&gt;
        &lt;/td&gt;
      &lt;/tr&gt;
      &lt;tr&gt;
        &lt;td&gt;&lt;b&gt;&lt;font color=&quot;#0B610B&quot;&gt;自最后一次构建成功后的变化:&lt;/font&gt;&lt;/b&gt;&lt;hr size=&quot;2&quot; width=&quot;100%&quot; align=&quot;center&quot;/&gt;&lt;/td&gt;
       &lt;/tr&gt;
       &lt;tr&gt;
        &lt;td&gt;
            &lt;ul&gt;
                    &lt;li&gt;在此查看历史变化记录: - &lt;a href=&quot;\${PROJECT_URL}changes&quot;&gt;\${PROJECT_URL}changes&lt;/a&gt;&lt;/li&gt;
            &lt;/ul&gt;
        &lt;/td&gt;
       &lt;/tr&gt;
        &lt;tr&gt;
        &lt;td &gt;
            &lt;ul&gt;
                    &lt;li&gt;详细测试结果： &lt;a href=&quot;\${PROJECT_URL}lastCompletedBuild/testReport&quot;&gt;\${PROJECT_URL}lastCompletedBuild/testReport/&lt;/a&gt;&lt;/li&gt;
            &lt;/ul&gt;
        &lt;/td&gt;
       &lt;/tr&gt;
       &lt;tr&gt;
       &lt;tr&gt;
        &lt;td &gt;&lt;b&gt;失败的测试结果：&lt;/b&gt;&lt;hr size=&quot;2&quot; width=&quot;100%&quot; align=&quot;center&quot;/&gt;&lt;/td&gt;
       &lt;/tr&gt;
       &lt;tr&gt;
        &lt;td&gt;
            &lt;pre style=&quot;font-size:11pt; font-family:Tahoma, Arial, Helvetica, sans-serif&quot;&gt;\${FAILED_TESTS}&lt;/pre&gt;
                    &lt;br/&gt;
        &lt;/td&gt;
      &lt;/tr&gt;
       &lt;tr&gt;
        &lt;td &gt;&lt;b&gt;&lt;font color=&quot;#0B610B&quot;&gt;构建日志 (最后 100 行):&lt;/font&gt;&lt;/b&gt;&lt;hr size=&quot;2&quot; width=&quot;100%&quot; align=&quot;center&quot;/&gt;&lt;/td&gt;
       &lt;/tr&gt;
       &lt;tr&gt;
        &lt;td&gt;
          &lt;textarea cols=&quot;80&quot; rows=&quot;30&quot; readonly=&quot;readonly&quot;  style=&quot;font-family: Courier New&quot;&gt;\${BUILD_LOG, maxLines=100}&lt;/textarea&gt;
        &lt;/td&gt;
      &lt;/tr&gt;
    &lt;/table&gt;
    &lt;/body&gt;
    &lt;/html&gt;</defaultContent>
      <attachmentsPattern></attachmentsPattern>
      <presendScript>\$DEFAULT_PRESEND_SCRIPT</presendScript>
      <postsendScript>\$DEFAULT_POSTSEND_SCRIPT</postsendScript>
      <attachBuildLog>false</attachBuildLog>
      <compressBuildLog>false</compressBuildLog>
      <replyTo>\$DEFAULT_REPLYTO</replyTo>
      <from></from>
      <saveOutput>false</saveOutput>
      <disabled>false</disabled>
    </hudson.plugins.emailext.ExtendedEmailPublisher>
  </publishers>
  <buildWrappers>
    <hudson.plugins.ansicolor.AnsiColorBuildWrapper plugin="ansicolor@0.5.2">
      <colorMapName>xterm</colorMapName>
    </hudson.plugins.ansicolor.AnsiColorBuildWrapper>
  </buildWrappers>
</project>