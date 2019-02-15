package coop.bancocredicoop.guv.loader.models.mongo;

import java.util.Date;

public class LoaderFlag {
    private Boolean enabled = true;
    private Date started;
    private String process;

    public LoaderFlag(String process) {
        this.enabled = true;
        this.started = new Date();
        this.process = process;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
