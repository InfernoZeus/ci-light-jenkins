package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class Endpoint extends AbstractDescribableImpl<Endpoint> {


    private Protocol protocol = Protocol.UDP;

    /**
     * json as default
     */
    private Format format = Format.JSON;

    private String url;

    private int port = 50000;

    @DataBoundConstructor
    public Endpoint(Protocol protocol, Format format, String url, int port) {
        setProtocol(protocol);
        setFormat(format);
        setUrl(url);
        setPort(port);
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        if (protocol != null) {
            this.protocol = protocol;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL can not be null or empty.");
        }
        this.url = url;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        if (format != null) {
            this.format = format;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535.");
        }
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

    @Override
    public String toString() {
        return protocol+":"+url;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<Endpoint> {
        @Override
        public String getDisplayName() {
            return "";
        }
    }

}
