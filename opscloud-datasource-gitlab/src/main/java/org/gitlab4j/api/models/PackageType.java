package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

public enum PackageType {

    MAVEN, NPM, CONAN, PYPI, COMPOSER, NUGET, HELM, GOLANG, GENERIC;

    private static JacksonJsonEnumHelper<PackageType> enumHelper = new JacksonJsonEnumHelper<>(PackageType.class);

    @JsonCreator
    public static PackageType forValue(String value) {
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
