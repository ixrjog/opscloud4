package com.sdg.cmdb.service.jms.model;

import java.util.Date;

public class Usergroup {
    private String id;

    private Boolean is_discard;

    private Date discard_time;

    private String name;

    private Date date_created;

    private String created_by;

    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIs_discard() {
        return is_discard;
    }

    public void setIs_discard(Boolean is_discard) {
        this.is_discard = is_discard;
    }

    public Date getDiscard_time() {
        return discard_time;
    }

    public void setDiscard_time(Date discard_time) {
        this.discard_time = discard_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}