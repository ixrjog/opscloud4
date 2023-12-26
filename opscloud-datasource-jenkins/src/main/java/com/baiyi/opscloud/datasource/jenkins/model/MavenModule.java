/*
 * Copyright (c) 2018 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
/**
 * 
 * @author Karl Heinz Marbaise, Ricardo Zanini, René Scheibe, Jakub Zacek
 */
@Getter
public class MavenModule extends BaseModel {

    private List<MavenModuleRecord> moduleRecords;
    @Setter
    private String name;
    @Setter
    private String url;
    @Setter
    private String color;
    @Setter
    private String displayName;

    public MavenModuleWithDetails details() throws IOException {
        return client.get(url, MavenModuleWithDetails.class);
    }

}