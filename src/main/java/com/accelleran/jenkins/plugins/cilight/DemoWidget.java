package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.widgets.Widget;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.io.IOException;
import java.net.*;
import java.util.List;

@Extension
public class DemoWidget extends Widget {

    private boolean demoModeEnabled = false;

    @JavaScriptMethod
    public boolean isDemoModeEnabled() {
        return demoModeEnabled;
    }

    @JavaScriptMethod
    public boolean toggleDemoMode() {
        CiLightGlobalConfiguration config = CiLightGlobalConfiguration.get();
        List<Endpoint> endpoints = config.getEndpoints();
        for (Endpoint endpoint : endpoints) {
            try {
                if (demoModeEnabled) {
                    sendDemoCommand(endpoint, DemoCommand.DISABLE_DEMO_MODE);
                } else {
                    sendDemoCommand(endpoint, DemoCommand.ENABLE_DEMO_MODE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        demoModeEnabled = !demoModeEnabled;
        return demoModeEnabled;
    }

    @JavaScriptMethod
    public void clearCache() {
        CiLightGlobalConfiguration config = CiLightGlobalConfiguration.get();
        List<Endpoint> endpoints = config.getEndpoints();
        for (Endpoint endpoint : endpoints) {
            try {
                sendClearCache(endpoint);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @JavaScriptMethod
    public void startAlarm() {
        if (isDemoModeEnabled()) {
            CiLightGlobalConfiguration config = CiLightGlobalConfiguration.get();
            List<Endpoint> endpoints = config.getEndpoints();
            for (Endpoint endpoint : endpoints) {
                try {
                    sendDemoCommand(endpoint, DemoCommand.START_ALARM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @JavaScriptMethod
    public void stopAlarm() {
        if (isDemoModeEnabled()) {
            CiLightGlobalConfiguration config = CiLightGlobalConfiguration.get();
            List<Endpoint> endpoints = config.getEndpoints();
            for (Endpoint endpoint : endpoints) {
                try {
                    sendDemoCommand(endpoint, DemoCommand.STOP_ALARM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendDemoCommand(Endpoint endpoint, DemoCommand command) throws IOException {
        String data = "{\"command\": \"demo\", \"action\": \"" + command.toJson() + "\"}";
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByName(endpoint.getUrl()), endpoint.getPort());
        socket.send(packet);
    }

    private void sendClearCache(Endpoint endpoint) throws IOException {
        String data = "{\"command\": \"clear\"}";
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), InetAddress.getByName(endpoint.getUrl()), endpoint.getPort());
        socket.send(packet);
    }

}
