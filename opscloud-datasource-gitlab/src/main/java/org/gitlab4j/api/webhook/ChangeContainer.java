package org.gitlab4j.api.webhook;

import org.gitlab4j.api.utils.JacksonJson;

public class ChangeContainer<T> {
    
    private T previous;
    private T current;

    public T getPrevious() {
        return previous;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public T getCurrent() {
        return current;
    }

    public void setCurrent(T current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
