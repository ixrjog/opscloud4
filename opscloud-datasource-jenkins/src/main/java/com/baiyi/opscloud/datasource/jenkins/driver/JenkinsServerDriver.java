package com.baiyi.opscloud.datasource.jenkins.driver;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.helper.JenkinsVersion;
import com.baiyi.opscloud.datasource.jenkins.model.Computer;
import com.baiyi.opscloud.datasource.jenkins.model.FolderJob;
import com.baiyi.opscloud.datasource.jenkins.model.Job;
import com.baiyi.opscloud.datasource.jenkins.model.JobWithDetails;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/7/2 9:55 上午
 * @Version 1.0
 */
@Slf4j
public class JenkinsServerDriver {

    @Deprecated
    private static Map<String, Computer> getComputers(JenkinsConfig.Jenkins jenkins) throws URISyntaxException, IOException {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            return jenkinsServer.getComputers();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static Map<String, Job> getJobs(JenkinsConfig.Jenkins jenkins) throws URISyntaxException, IOException {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            return jenkinsServer.getJobs();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static Map<String, Job> getJobs(JenkinsConfig.Jenkins jenkins, FolderJob folder) throws URISyntaxException, IOException {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            return jenkinsServer.getJobs(folder);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private static JobWithDetails getJob(JenkinsConfig.Jenkins jenkins, String jobName) throws URISyntaxException, IOException {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            return jenkinsServer.getJob(jobName);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static String getJobXml(JenkinsConfig.Jenkins jenkins, FolderJob folder, String jobName) throws URISyntaxException, IOException {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            return jenkinsServer.getJobXml(folder, jobName);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static String getJobXml(JenkinsConfig.Jenkins jenkins, String jobName) throws URISyntaxException, IOException {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            return jenkinsServer.getJobXml(jobName);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static JenkinsVersion getVersion(JenkinsConfig.Jenkins jenkins) throws URISyntaxException, IOException {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            return jenkinsServer.getVersion();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static void createJob(JenkinsConfig.Jenkins jenkins, String jobName, String jobXml) throws URISyntaxException, IOException {
        final boolean crumbFlag = Optional.of(jenkins)
                .map(JenkinsConfig.Jenkins::getSecurity)
                .map(JenkinsConfig.Security::getCrumbFlag)
                .orElse(false);
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            jenkinsServer.createJob(jobName, jobXml, crumbFlag);
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public static void createJob(JenkinsConfig.Jenkins jenkins, FolderJob folder, String jobName, String jobXml) throws URISyntaxException, IOException {
        final boolean crumbFlag = Optional.of(jenkins)
                .map(JenkinsConfig.Jenkins::getSecurity)
                .map(JenkinsConfig.Security::getCrumbFlag)
                .orElse(false);
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            jenkinsServer.createJob(folder, jobName, jobXml, crumbFlag);
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}