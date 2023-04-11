package org.gitlab4j.api.utils;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public class JacksonJsonEnumHelper<E extends Enum<E>> {

    private Map<String, E> valuesMap;
    private Map<E, String> namesMap;

    public JacksonJsonEnumHelper(Class<E> enumType) {
        this(enumType, false);
    }

    public JacksonJsonEnumHelper(Class<E> enumType, boolean firstLetterCapitalized) {

        valuesMap = new HashMap<>();
        namesMap = new HashMap<>();

        for (E e : enumType.getEnumConstants()) {

            String name = e.name().toLowerCase();
            if (firstLetterCapitalized) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            }

            valuesMap.put(name, e);
            namesMap.put(e, name);
        }
    }

    public JacksonJsonEnumHelper(Class<E> enumType, boolean firstLetterCapitalized, boolean camelCased) {
        this(enumType, firstLetterCapitalized, camelCased, false);
    }

    public JacksonJsonEnumHelper(Class<E> enumType, boolean firstLetterCapitalized, boolean camelCased, boolean preserveUnderscores) {

        valuesMap = new HashMap<>();
        namesMap = new HashMap<>();

        for (E e : enumType.getEnumConstants()) {

            char[] chars = e.name().toLowerCase().toCharArray();
            StringBuilder nameBuf = new StringBuilder(chars.length);
            boolean nextCharIsCapitalized = firstLetterCapitalized;
            for (char ch : chars) {
                if (ch == '_') {
                    if (preserveUnderscores) {
                        nameBuf.append(ch);
                    } else {
                        if (camelCased) {
                            nextCharIsCapitalized = true;
                        } else {
                            nameBuf.append(' ');
                        }
                    }
                } else if (nextCharIsCapitalized) {
                    nextCharIsCapitalized = false;
                    nameBuf.append(Character.toUpperCase(ch));
                } else {
                    nameBuf.append(ch);
                }
            }

            String name = nameBuf.toString();
            valuesMap.put(name, e);
            namesMap.put(e, name);
        }
    }

    /**
     * Add an enum that has a specialized name that does not fit the standard naming conventions.
     *
     * @param e the enum to add
     * @param name the name for the enum
     */
    public void addEnum(E e, String name) {
        valuesMap.put(name, e);
        namesMap.put(e, name);
    }

    @JsonCreator
    public E forValue(String value) {
        return valuesMap.get(value);
    }

    /**
     * Get the string used by the API for this enum.
     *
     * @param e the enum value to get the API string for
     * @return the string used by the API for this enum
     */
    public String toString(E e) {
        return (namesMap.get(e));
    }
}