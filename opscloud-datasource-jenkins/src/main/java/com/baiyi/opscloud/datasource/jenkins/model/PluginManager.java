package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

import java.util.List;

/**
 * @author Karl Heinz Marbaise
 */
@Getter
public class PluginManager extends BaseModel {
    private List<Plugin> plugins;

    public PluginManager setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((plugins == null) ? 0 : plugins.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PluginManager other = (PluginManager) obj;
        if (plugins == null) {
            return other.plugins == null;
        } else return plugins.equals(other.plugins);
    }

}