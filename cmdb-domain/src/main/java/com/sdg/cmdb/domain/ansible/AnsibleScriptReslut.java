package com.sdg.cmdb.domain.ansible;

import java.io.Serializable;

public class AnsibleScriptReslut implements Serializable {
    private static final long serialVersionUID = 1195519523998659838L;

    private boolean changed;

    private int rc;

    private String stderr;

    private String stdout;

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }
}
