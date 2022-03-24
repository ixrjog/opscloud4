/*
 * Copyright (c) 2018 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.model;

import java.io.IOException;
import java.util.List;
/**
 * 
 * @author Karl Heinz Marbaise, Ricardo Zanini, René Scheibe, Jakub Zacek
 */
public class MavenModule extends BaseModel {

    private List<MavenModuleRecord> moduleRecords;
    private String name;
    private String url;
    private String color;
    private String displayName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public MavenModuleWithDetails details() throws IOException {
        return client.get(url, MavenModuleWithDetails.class);
    }    

    public List<MavenModuleRecord> getModuleRecords() {
        return moduleRecords;
    }

}
