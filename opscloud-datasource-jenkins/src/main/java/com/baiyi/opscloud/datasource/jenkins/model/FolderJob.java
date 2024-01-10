package com.baiyi.opscloud.datasource.jenkins.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.datasource.jenkins.helper.FunctionalHelper.SET_CLIENT;

public class FolderJob extends Job {

    private String displayName;
    private List<Job> jobs;

    public FolderJob() {
    }

    public FolderJob(String name, String url) {
        super(name, url);
    }
    
    public FolderJob(String name, String url, String fullName) {
        super(name, url, fullName);
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Determine if this FolderJob object is a valid folder or not.
     * 
     * (internally: if jobs list exists)
     * 
     * @return true if this job is a folder.
     */
    public boolean isFolder() {
        if (jobs != null) {
            return true;
        }
        return false;
    }

    /**
     * Get a list of all the defined jobs in this folder
     *
     * @return list of defined jobs (summary level, for details {@link Job#details()}.
     */
    public Map<String, Job> getJobs() {
        //FIXME: Check for null of jobs? Can that happen?
        return jobs.stream()
                .map(SET_CLIENT(this.client))
                .collect(Collectors.toMap(Job::getName, Function.identity()));
    }

    /**
     * Get a job in this folder by name
     *
     * @param name the name of the job.
     * @return the given job
     * @throws IllegalArgumentException in case if the {@code name} does not exist.
     */
    public Job getJob(String name) {
        //FIXME: Check for null of jobs? Can that happen?
        return jobs.stream()
            .map(SET_CLIENT(this.client))
            .filter(item -> item.getName().equals(name))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Job with name " + name + " does not exist."));
    }

    /**
     * Create a folder on the server (as a subfolder of this folder)
     *
     * @param folderName name of the folder to be created.
     * @throws IOException in case of an error.
     */
    public FolderJob createFolder(String folderName) throws IOException {
        return createFolder(folderName, false);
    }

    /**
     * Create a folder on the server (as a subfolder of this folder)
     *
     * @param folderName name of the folder to be created.
     * @param crumbFlag true/false.
     * @return
     * @throws IOException in case of an error.
     */
    public FolderJob createFolder(String folderName, Boolean crumbFlag) throws IOException {
        // https://gist.github.com/stuart-warren/7786892 was slightly helpful
        // here
        //TODO: JDK9+: Map.of(...)
        Map<String, String> params = new HashMap<>();
        params.put("mode", "com.cloudbees.hudson.plugins.folder.Folder");
        params.put("name", folderName);
        params.put("from", "");
        params.put("Submit", "OK");
        client.post_form(this.getUrl() + "/createItem?", params, crumbFlag);
        return this;
    }

    /*
     * TODO public List<Job> getJobsRecursive() { return Lists.transform(jobs,
     * new Function<Job, Job>() {
     * 
     * @Override public Job apply(Job job) { // TODO: try to see if each job is
     * a folder return job; } }); }
     */

}