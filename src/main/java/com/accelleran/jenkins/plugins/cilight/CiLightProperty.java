package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

public class CiLightProperty extends JobProperty<AbstractProject<?, ?>> {

    private boolean notify = true;
    private boolean cache = true;

    CiLightProperty() {
    }

    @DataBoundConstructor
    public CiLightProperty(boolean notify, boolean cache) {
        setNotify(notify);
        setCache(cache);
    }

    public boolean getCache() {
        return this.cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean getNotify() {
        return this.notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @Extension
    public static class DescriptorImpl extends JobPropertyDescriptor
    {
        @Override
        public String getDisplayName() {
            return "ci-light";
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return true;
        }


    }

}