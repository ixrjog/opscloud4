package com.offbytwo.jenkins.model;

public class View extends MainView {

    //TODO: Think about the initialization of
    // the attributes? Better being done in 
    // ctor.
    private String name = "";
    private String description = "";
    private String url = "";

    public String getName() {
        return name;
    }

    public View setName(String name) {
        this.name = name;
        return this;
    }

    public View() {
    }

    public String getDescription() {
        return description;
    }

    public View setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public View setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        View other = (View) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }
}
