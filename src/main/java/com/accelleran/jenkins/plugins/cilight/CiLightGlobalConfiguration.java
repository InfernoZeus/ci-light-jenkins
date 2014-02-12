package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.List;

@Extension
public class CiLightGlobalConfiguration extends GlobalConfiguration {

    private List<Endpoint> endpoints = new ArrayList<Endpoint>();

    public CiLightGlobalConfiguration() {
        load();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        setFieldsFromJson(req, json);
        save();
        return true;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public void setFieldsFromJson(StaplerRequest req, JSONObject json) {
        Object endpointsJson = json.get("endpoints");
        if (
            (endpointsJson == null) ||
            (endpointsJson instanceof JSONObject && ((JSONObject)endpointsJson).isEmpty()) ||
            (endpointsJson instanceof JSONArray && ((JSONArray)endpointsJson).isEmpty())
        ) {
            setEndpoints(new ArrayList<Endpoint>());
        }
    }

    static CiLightGlobalConfiguration get() {
        return GlobalConfiguration.all().get(CiLightGlobalConfiguration.class);
    }

    public FormValidation doCheckUrl(@QueryParameter(value = "url", fixEmpty = true) String url, @QueryParameter(value = "protocol") String protocolParameter) {
        return FormValidation.ok();
//        Protocol protocol = Protocol.valueOf(protocolParameter);
//        try {
//            protocol.validateUrl(url);
//            return FormValidation.ok();
//        } catch (Exception e) {
//            return FormValidation.error(e.getMessage());
//        }
    }

    public FormValidation doCheckPort(@QueryParameter(value = "port", fixEmpty = true) String port, @QueryParameter(value = "protocol") String protocolParameter) {
        return FormValidation.ok();
//        Protocol protocol = Protocol.valueOf(protocolParameter);
//        try {
//            protocol.validatePort(port);
//            return FormValidation.ok();
//        } catch (Exception e) {
//            return FormValidation.error(e.getMessage());
//        }
    }

}
