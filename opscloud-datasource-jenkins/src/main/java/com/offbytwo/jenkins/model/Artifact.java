/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

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

        if (displayPath != null ? !displayPath.equals(artifact.displayPath) : artifact.displayPath != null)
            return false;
        if (fileName != null ? !fileName.equals(artifact.fileName) : artifact.fileName != null)
            return false;
        if (relativePath != null ? !relativePath.equals(artifact.relativePath) : artifact.relativePath != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = displayPath != null ? displayPath.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (relativePath != null ? relativePath.hashCode() : 0);
        return result;
    }
}
