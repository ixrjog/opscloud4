package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

public enum MembershipSourceType {

    PROJECT,

    /** Representing a group */
    NAMESPACE;

    private static JacksonJsonEnumHelper<MembershipSourceType> enumHelper = new JacksonJsonEnumHelper<>(MembershipSourceType.class, true);

    @JsonCreator
    public static MembershipSourceType forValue(String value) {
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
