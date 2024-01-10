/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import com.baiyi.opscloud.datasource.jenkins.util.ComputerNameUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.util.List;

/**
 * @author Kelly Plummer
 */
public class Computer extends BaseModel {

    private String displayName;

    private List<Computer> computers;

    public Computer() {
    }

    @JsonProperty("computer")
    public List<Computer> getComputers() {
        return computers;
    }

    @JsonProperty("computer")
    public Computer setComputers(List<Computer> computers) {
        this.computers = computers;
        return this;
    }

    public Computer(String displayName) {
        this();
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public ComputerWithDetails details() throws IOException {
        String name = ComputerNameUtil.toName(displayName);
        // TODO: Check if depth=2 is a good idea or if it could be solved
        // better.
        ComputerWithDetails computerWithDetails = client.get("/computer/" + name + "/?depth=2",
                ComputerWithDetails.class);
        computerWithDetails.setClient(client);
        return computerWithDetails;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Computer other = (Computer) obj;
        if (computers == null) {
            if (other.computers != null)
                return false;
        } else if (!computers.equals(other.computers))
            return false;
        if (displayName == null) {
            return other.displayName == null;
        } else return displayName.equals(other.displayName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((computers == null) ? 0 : computers.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        return result;
    }

}