/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.util.List;

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

        if (actions != null ? !actions.equals(that.actions) : that.actions != null)
            return false;
        if (clouds != null ? !clouds.equals(that.clouds) : that.clouds != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (idleExecutors != null ? !idleExecutors.equals(that.idleExecutors) : that.idleExecutors != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (nodeName != null ? !nodeName.equals(that.nodeName) : that.nodeName != null)
            return false;
        if (nodes != null ? !nodes.equals(that.nodes) : that.nodes != null)
            return false;
        if (offline != null ? !offline.equals(that.offline) : that.offline != null)
            return false;
        if (propertiesList != null ? !propertiesList.equals(that.propertiesList) : that.propertiesList != null)
            return false;
        if (tiedJobs != null ? !tiedJobs.equals(that.tiedJobs) : that.tiedJobs != null)
            return false;
        if (totalExecutors != null ? !totalExecutors.equals(that.totalExecutors) : that.totalExecutors != null)
            return false;

        return true;
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