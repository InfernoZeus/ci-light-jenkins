package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobPropertyDescriptor;
import hudson.util.FormValidation;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.List;

@Extension
public class CiLightPropertyDescriptor extends JobPropertyDescriptor {

    public CiLightPropertyDescriptor() {
        super(CiLightProperty.class);
        load();
    }

    private List<Endpoint> endpoints = new ArrayList<Endpoint>();

    public boolean isEnabled() {
        return !endpoints.isEmpty();
    }

    public List<Endpoint> getTargets() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
        return true;
    }

    public String getDisplayName() {
        return "Job Notification for CI Light";
    }

    @Override
    public CiLightProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {

        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        if (formData != null && !formData.isNullObject()) {
            JSON endpointsData = (JSON) formData.get("endpoints");
            if (endpointsData != null && !endpointsData.isEmpty()) {
                if (endpointsData.isArray()) {
                    JSONArray endpointsArrayData = (JSONArray) endpointsData;
                    endpoints.addAll(req.bindJSONToList(Endpoint.class, endpointsArrayData));
                } else {
                    JSONObject endpointsObjectData = (JSONObject) endpointsData;
                    endpoints.add(req.bindJSON(Endpoint.class, endpointsObjectData));
                }
            }
        }
        CiLightProperty notificationProperty = new CiLightProperty(endpoints);
        return notificationProperty;
    }

    public FormValidation doCheckUrl(@QueryParameter(value = "url", fixEmpty = true) String url, @QueryParameter(value = "protocol") String protocolParameter) {
        Protocol protocol = Protocol.valueOf(protocolParameter);
        try {
            protocol.validateUrl(url);
            return FormValidation.ok();
        } catch (Exception e) {
            return FormValidation.error(e.getMessage());
        }
    }

    public FormValidation doCheckPort(@QueryParameter(value = "port", fixEmpty = true) String port, @QueryParameter(value = "protocol") String protocolParameter) {
        Protocol protocol = Protocol.valueOf(protocolParameter);
        try {
            protocol.validatePort(port);
            return FormValidation.ok();
        } catch (Exception e) {
            return FormValidation.error(e.getMessage());
        }
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) {
        save();
        return true;
    }

}
