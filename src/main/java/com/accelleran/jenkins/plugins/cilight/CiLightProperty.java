package com.accelleran.jenkins.plugins.cilight;

import hudson.model.AbstractProject;
import hudson.model.JobProperty;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

public class CiLightProperty extends JobProperty<AbstractProject<?, ?>> {

    final public List<Endpoint> endpoints;

    @DataBoundConstructor
    public CiLightProperty(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public CiLightPropertyDescriptor getDescriptor() {
        return (CiLightPropertyDescriptor) super.getDescriptor();
    }
}