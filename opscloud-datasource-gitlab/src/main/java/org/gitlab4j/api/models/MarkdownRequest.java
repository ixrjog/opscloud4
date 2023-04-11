package org.gitlab4j.api.models;

public class MarkdownRequest {
    private String text;
    private boolean gfm;

    public MarkdownRequest(String text, boolean gfm) {
        this.text = text;
        this.gfm = gfm;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGfm() {
        return gfm;
    }

    public void setGfm(boolean gfm) {
        this.gfm = gfm;
    }
}
