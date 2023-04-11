package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

public class Artifact {

    public enum FileType {
        /**
         *
         */
        ARCHIVE, METADATA, TRACE, JUNIT;

        private static JacksonJsonEnumHelper<FileType> enumHelper = new JacksonJsonEnumHelper<>(FileType.class);

        @JsonCreator
        public static FileType forValue(String value) {
            return enumHelper.forValue(value);
        }

        @JsonValue
        public String toValue() {
            return (enumHelper.toString(this));
        }

        @Override
        public String toString() {
            return (enumHelper.toString(this));
        }
    }

    private FileType fileType;
    private Long size;
    private String filename;
    private String fileFormat;

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
