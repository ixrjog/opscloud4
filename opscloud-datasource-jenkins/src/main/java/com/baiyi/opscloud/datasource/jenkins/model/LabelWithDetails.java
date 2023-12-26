/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import java.util.List;
import java.util.Objects;

public class LabelWithDetails extends Job {

    String name;

    List actions;
    List clouds;
    String description;
    Integer idleExecutors;
    List nodes;
    String nodeName;
    Boolean offline;
    List tiedJobs;
    Integer totalExecutors;
    List propertiesList;

    public String getName() {
        return name;
    }

    public List getActions() {
        return actions;
    }

    public List getClouds() {
        return clouds;
    }

    public String getDescription() {
        return description;
    }

    public Integer getIdleExecutors() {
        return idleExecutors;
    }

    public List getNodes() {
        return nodes;
    }

    public String getNodeName() {
        return nodeName;
    }

    public Boolean getOffline() {
        return offline;
    }

    public List getTiedJobs() {
        return tiedJobs;
    }

    public Integer getTotalExecutors() {
        return totalExecutors;
    }

    public List getPropertiesList() {
        return propertiesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        LabelWithDetails that = (LabelWithDetails) o;

        if (!Objects.equals(actions, that.actions))
            return false;
        if (!Objects.equals(clouds, that.clouds))
            return false;
        if (!Objects.equals(description, that.description))
            return false;
        if (!Objects.equals(idleExecutors, that.idleExecutors))
            return false;
        if (!Objects.equals(name, that.name))
            return false;
        if (!Objects.equals(nodeName, that.nodeName))
            return false;
        if (!Objects.equals(nodes, that.nodes))
            return false;
        if (!Objects.equals(offline, that.offline))
            return false;
        if (!Objects.equals(propertiesList, that.propertiesList))
            return false;
        if (!Objects.equals(tiedJobs, that.tiedJobs))
            return false;
        return Objects.equals(totalExecutors, that.totalExecutors);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (actions != null ? actions.hashCode() : 0);
        result = 31 * result + (clouds != null ? clouds.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (idleExecutors != null ? idleExecutors.hashCode() : 0);
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        result = 31 * result + (nodeName != null ? nodeName.hashCode() : 0);
        result = 31 * result + (offline != null ? offline.hashCode() : 0);
        result = 31 * result + (tiedJobs != null ? tiedJobs.hashCode() : 0);
        result = 31 * result + (totalExecutors != null ? totalExecutors.hashCode() : 0);
        result = 31 * result + (propertiesList != null ? propertiesList.hashCode() : 0);
        return result;
    }

}