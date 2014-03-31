package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

public class CiLightProperty extends JobProperty<AbstractProject<?, ?>> {

    private boolean cache = true;

    CiLightProperty() {
    }

    @DataBoundConstructor
    public CiLightProperty(boolean cache) {
        setCache(cache);
    }

    public boolean getCache() {
        return this.cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
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