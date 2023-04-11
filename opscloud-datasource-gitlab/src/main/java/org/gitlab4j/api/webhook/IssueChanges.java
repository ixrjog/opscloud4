package org.gitlab4j.api.webhook;

import java.util.Date;

public class IssueChanges extends EventChanges {

    private ChangeContainer<Date> dueDate;
    private ChangeContainer<Boolean> confidential;

    public ChangeContainer<Date> getDueDate() {
        return dueDate;
    }

    public void setDueDate(ChangeContainer<Date> dueDate) {
        this.dueDate = dueDate;
    }

    public ChangeContainer<Boolean> getConfidential() {
        return confidential;
    }

    public void setConfidential(ChangeContainer<Boolean> confidential) {
        this.confidential = confidential;
    }
}
