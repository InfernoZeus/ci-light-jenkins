package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.Functions;
import hudson.Plugin;
import hudson.model.Hudson;
import hudson.util.FormApply;
import hudson.widgets.Widget;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.io.IOException;
import java.net.*;
import java.util.List;

@Extension
public class DemoWidget extends Widget {

    private boolean active = false;

    @JavaScriptMethod
    public boolean isActive() {
        return active;
    }

    @JavaScriptMethod
    public boolean toggle() {
        CiLightGlobalConfiguration config = CiLightGlobalConfiguration.get();
        List<Endpoint> endpoints = config.getEndpoints();
        for (Endpoint endpoint : endpoints) {
            try {
                setDemoMode(endpoint, !active);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        active = !active;
        return active;
    }

    private void setDemoMode(Endpoint endpoint, boolean newMode) throws IOException {
        String data = "{\"command\": \"demo\", \"mode\": " + newMode + "}";
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByName(endpoint.getUrl()), endpoint.getPort());
        socket.send(packet);
    }

}
