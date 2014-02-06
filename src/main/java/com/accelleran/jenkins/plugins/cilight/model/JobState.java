package com.accelleran.jenkins.plugins.cilight.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("job")
public class JobState {

    private String name;

    private String server;

    private String url;

    private boolean cache;

    private BuildState build;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public BuildState getBuild() {
        return build;
    }

    public void setBuild(BuildState build) {
        this.build = build;
    }
}