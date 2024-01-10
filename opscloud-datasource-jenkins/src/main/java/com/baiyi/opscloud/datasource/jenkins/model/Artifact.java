/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import java.util.Optional;

public class Artifact extends BaseModel {

    private String displayPath;
    private String fileName;
    private String relativePath;

    public String getDisplayPath() {
        return displayPath;
    }

    public Artifact setDisplayPath(String displayPath) {
        this.displayPath = displayPath;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public Artifact setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public Artifact setRelativePath(String relativePath) {
        this.relativePath = relativePath;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Artifact artifact = (Artifact) o;

        if (Optional.ofNullable(displayPath).map(path -> !path.equals(artifact.displayPath)).orElseGet(() -> artifact.displayPath != null))
            return false;
        if (Optional.ofNullable(fileName).map(name -> !name.equals(artifact.fileName)).orElseGet(() -> artifact.fileName != null))
            return false;
        return !Optional.ofNullable(relativePath).map(path -> !path.equals(artifact.relativePath)).orElseGet(() -> artifact.relativePath != null);
    }

    @Override
    public int hashCode() {
        int result = displayPath != null ? displayPath.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (relativePath != null ? relativePath.hashCode() : 0);
        return result;
    }

}