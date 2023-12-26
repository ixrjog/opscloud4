package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

import java.util.List;

/**
 * @author Karl Heinz Marbaise
 */
@Getter
public class Plugin extends BaseModel {
    private boolean active;
    private String backupVersion;
    private boolean bundled;
    private boolean downgradable;
    private boolean enabled;
    private boolean hasUpdate;
    private String longName;
    private boolean pinned;
    private String shortName;
    private String supportsDynamicLoad; // YesNoMayBe
    private String url;
    private String version;

    private List<PluginDependency> dependencies;

    public Plugin setActive(boolean active) {
        this.active = active;
        return this;
    }

    public Plugin setBackupVersion(String backupVersion) {
        this.backupVersion = backupVersion;
        return this;
    }

    public Plugin setBundled(boolean bundled) {
        this.bundled = bundled;
        return this;
    }

    public Plugin setDowngradable(boolean downgradable) {
        this.downgradable = downgradable;
        return this;
    }

    public Plugin setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Plugin setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
        return this;
    }

    public Plugin setLongName(String longName) {
        this.longName = longName;
        return this;
    }

    public Plugin setPinned(boolean pinned) {
        this.pinned = pinned;
        return this;
    }

    public Plugin setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public Plugin setSupportsDynamicLoad(String supportsDynamicLoad) {
        this.supportsDynamicLoad = supportsDynamicLoad;
        return this;
    }

    public Plugin setUrl(String url) {
        this.url = url;
        return this;
    }

    public Plugin setVersion(String version) {
        this.version = version;
        return this;
    }

    public Plugin setDependencies(List<PluginDependency> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((backupVersion == null) ? 0 : backupVersion.hashCode());
        result = prime * result + (bundled ? 1231 : 1237);
        result = prime * result + ((dependencies == null) ? 0 : dependencies.hashCode());
        result = prime * result + (downgradable ? 1231 : 1237);
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + (hasUpdate ? 1231 : 1237);
        result = prime * result + ((longName == null) ? 0 : longName.hashCode());
        result = prime * result + (pinned ? 1231 : 1237);
        result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
        result = prime * result + ((supportsDynamicLoad == null) ? 0 : supportsDynamicLoad.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        Plugin other = (Plugin) obj;
        if (active != other.active)
            return false;
        if (backupVersion == null) {
            if (other.backupVersion != null)
                return false;
        } else if (!backupVersion.equals(other.backupVersion))
            return false;
        if (bundled != other.bundled)
            return false;
        if (dependencies == null) {
            if (other.dependencies != null)
                return false;
        } else if (!dependencies.equals(other.dependencies))
            return false;
        if (downgradable != other.downgradable)
            return false;
        if (enabled != other.enabled)
            return false;
        if (hasUpdate != other.hasUpdate)
            return false;
        if (longName == null) {
            if (other.longName != null)
                return false;
        } else if (!longName.equals(other.longName))
            return false;
        if (pinned != other.pinned)
            return false;
        if (shortName == null) {
            if (other.shortName != null)
                return false;
        } else if (!shortName.equals(other.shortName))
            return false;
        if (supportsDynamicLoad == null) {
            if (other.supportsDynamicLoad != null)
                return false;
        } else if (!supportsDynamicLoad.equals(other.supportsDynamicLoad))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        if (version == null) {
            return other.version == null;
        } else return version.equals(other.version);
    }

}