/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import com.baiyi.opscloud.datasource.jenkins.util.ComputerNameUtil;
import com.baiyi.opscloud.datasource.jenkins.client.util.EncodingUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputerWithDetails extends Computer {

    private String displayName;
    private List actions; //TODO: What kind of List?
    private List<Executor> executors;
    private List<ComputerLabel> assignedLabels;
    private Boolean idle;
    private Boolean jnlp;
    private Boolean launchSupported;
    private Boolean manualLaunchAllowed;
    private Map monitorData; //TODO: What kind of map?
    private Integer numExecutors;
    private Boolean offline;
    private OfflineCause offlineCause;
    private String offlineCauseReason;
    private List oneOffExecutors; //TODO: What kind of List?
    private Boolean temporarilyOffline;

    public ComputerWithDetails()
    {
    }
    @Override
    public String getDisplayName() {
        return displayName;
    }

    public List<Map> getActions() {
        return actions;
    }

    public List<Executor> getExecutors() {
        return executors;
    }

    public List<ComputerLabel> getAssignedLabels() {
        return assignedLabels;
    }

    public Boolean getIdle() {
        return idle;
    }

    public Boolean getJnlp() {
        return jnlp;
    }

    public Boolean getLaunchSupported() {
        return launchSupported;
    }

    /**
     * This will explicitly get the whole statistics for the given computer
     * (node) name.
     * 
     * @return {@link LoadStatistics}
     * @throws IOException in case of an error.
     */
    public LoadStatistics getLoadStatistics() throws IOException {
        // TODO: Think about the following handling, cause that has also being
        // done in Computer#details().
        String name = ComputerNameUtil.toName(displayName);
        // TODO: ?depth=2 good idea or could this being done better?
        return client.get("/computer/" + name + "/" + "loadStatistics/?depth=2", LoadStatistics.class);
    }

    public ComputerWithDetails toggleOffline(boolean crumbFlag) throws IOException {
        // curl --data "json=init" -X POST "http://192.168.99.100:8080/computer/(master)/toggleOffline"
        String name;
        if ("master".equals(displayName)) {
            name = "(master)";
        } else {
            name = EncodingUtils.encode(displayName);
        }
        
        client.post( "/computer/" + name + "/toggleOffline", crumbFlag);
        return this;
    }

    public ComputerWithDetails toggleOffline() throws IOException {
        return toggleOffline(false);
    }

    public ComputerWithDetails changeOfflineCause(String cause, boolean crumbFlag) throws IOException {
      String name;
      if ("master".equals(displayName)) {
        name = "(master)";
      } else {
        name = EncodingUtils.encode(displayName);
      }
        Map<String, String> data = new HashMap<String, String>();
        data.put("offlineMessage", cause);
        client.post_form("/computer/" + name + "/changeOfflineCause?", data, crumbFlag);
        return this;
    }

    public ComputerWithDetails changeOfflineCause(String cause) throws IOException {
        return changeOfflineCause(cause, false);
    }

    public Boolean getManualLaunchAllowed() {
        return manualLaunchAllowed;
    }

    public Map<String, Map> getMonitorData() {
        return monitorData;
    }

    public Integer getNumExecutors() {
        return numExecutors;
    }

    public Boolean getOffline() {
        return offline;
    }

    public OfflineCause getOfflineCause() throws IOException {
        return offlineCause;
    }

    public String getOfflineCauseReason() {
        return offlineCauseReason;
    }

    public List<Map> getOneOffExecutors() {
        return oneOffExecutors;
    }

    public Boolean getTemporarilyOffline() {
        return temporarilyOffline;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ComputerWithDetails other = (ComputerWithDetails) obj;
        if (actions == null) {
            if (other.actions != null)
                return false;
        } else if (!actions.equals(other.actions))
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (executors == null) {
            if (other.executors != null)
                return false;
        } else if (!executors.equals(other.executors))
            return false;
        if (assignedLabels == null) {
            if (other.assignedLabels != null)
                return false;
        } else if (!assignedLabels.equals(other.assignedLabels))
            return false;
        if (idle == null) {
            if (other.idle != null)
                return false;
        } else if (!idle.equals(other.idle))
            return false;
        if (jnlp == null) {
            if (other.jnlp != null)
                return false;
        } else if (!jnlp.equals(other.jnlp))
            return false;
        if (launchSupported == null) {
            if (other.launchSupported != null)
                return false;
        } else if (!launchSupported.equals(other.launchSupported))
            return false;
        if (manualLaunchAllowed == null) {
            if (other.manualLaunchAllowed != null)
                return false;
        } else if (!manualLaunchAllowed.equals(other.manualLaunchAllowed))
            return false;
        if (monitorData == null) {
            if (other.monitorData != null)
                return false;
        } else if (!monitorData.equals(other.monitorData))
            return false;
        if (numExecutors == null) {
            if (other.numExecutors != null)
                return false;
        } else if (!numExecutors.equals(other.numExecutors))
            return false;
        if (offline == null) {
            if (other.offline != null)
                return false;
        } else if (!offline.equals(other.offline))
            return false;
        if (offlineCause == null) {
            if (other.offlineCause != null)
                return false;
        } else if (!offlineCause.equals(other.offlineCause))
            return false;
        if (offlineCauseReason == null) {
            if (other.offlineCauseReason != null)
                return false;
        } else if (!offlineCauseReason.equals(other.offlineCauseReason))
            return false;
        if (oneOffExecutors == null) {
            if (other.oneOffExecutors != null)
                return false;
        } else if (!oneOffExecutors.equals(other.oneOffExecutors))
            return false;
        if (temporarilyOffline == null) {
            return other.temporarilyOffline == null;
        } else return temporarilyOffline.equals(other.temporarilyOffline);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actions == null) ? 0 : actions.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((executors == null) ? 0 : executors.hashCode());
        result = prime * result + ((idle == null) ? 0 : idle.hashCode());
        result = prime * result + ((jnlp == null) ? 0 : jnlp.hashCode());
        result = prime * result + ((launchSupported == null) ? 0 : launchSupported.hashCode());
        result = prime * result + ((manualLaunchAllowed == null) ? 0 : manualLaunchAllowed.hashCode());
        result = prime * result + ((monitorData == null) ? 0 : monitorData.hashCode());
        result = prime * result + ((numExecutors == null) ? 0 : numExecutors.hashCode());
        result = prime * result + ((offline == null) ? 0 : offline.hashCode());
        result = prime * result + ((offlineCause == null) ? 0 : offlineCause.hashCode());
        result = prime * result + ((offlineCauseReason == null) ? 0 : offlineCauseReason.hashCode());
        result = prime * result + ((oneOffExecutors == null) ? 0 : oneOffExecutors.hashCode());
        result = prime * result + ((temporarilyOffline == null) ? 0 : temporarilyOffline.hashCode());
        return result;
    }

}