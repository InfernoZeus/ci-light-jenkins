package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.model.*;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.util.List;

public class CiLightProperty extends JobProperty<AbstractProject<?, ?>> {

    private boolean notify = false;
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

        @Override
        public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            if (formData.isEmpty()) {
                return new CiLightProperty();
            } else {
                JSONObject notifyObject = formData.getJSONObject("notify");
                return new CiLightProperty(true, notifyObject.getBoolean("cache"));
            }
        }
    }

}