package com.accelleran.jenkins.plugins.cilight;

import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class Endpoint {


    private Protocol protocol = Protocol.UDP;

    /**
     * json as default
     */
    private Format format = Format.JSON;

    private String url;

    private int port = 50000;

    private boolean cache = true;

    @DataBoundConstructor
    public Endpoint(Protocol protocol, Format format, String url, int port, boolean cache) {
        this.protocol = protocol;
        this.format = format;
        this.url = url;
        this.port = port;
        this.cache = cache;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Format getFormat() {
        if (this.format==null){
            this.format = Format.JSON;
        }
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPort(String port) {
        try {
            int portInt = Integer.valueOf(port);
            this.port = portInt;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid port: 1-65535");
        }
    }

    public boolean getCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    @Override
    public String toString() {
        return protocol+":"+url;
    }

}
