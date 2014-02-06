package com.accelleran.jenkins.plugins.cilight;


import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import com.accelleran.jenkins.plugins.cilight.model.BuildState;
import com.accelleran.jenkins.plugins.cilight.model.JobState;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Run;

@SuppressWarnings("rawtypes")
public enum Protocol {

    UDP {
        @Override
        protected void send(String url, int port, byte[] data) throws IOException {
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(url), port);
            socket.send(packet);
        }

    },
    TCP {
        @Override
        protected void send(String url, int port, byte[] data) throws IOException {
            SocketAddress endpoint = new InetSocketAddress(InetAddress.getByName(url), port);
            Socket socket = new Socket();
            socket.connect(endpoint);
            OutputStream output = socket.getOutputStream();
            output.write(data);
            output.flush();
            output.close();
        }
    },
    HTTP {
        @Override
        protected void send(String url, int port, byte[] data) throws IOException {
            URL targetUrl = new URL(url);
            if (!targetUrl.getProtocol().startsWith("http")) {
                throw new IllegalArgumentException("Not an http(s) url: " + url);
            }

            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            String userInfo = targetUrl.getUserInfo();
            if (null != userInfo) {
                String b64UserInfo = DatatypeConverter.printBase64Binary(userInfo.getBytes());
                String authorizationHeader = "Basic " + b64UserInfo;
                connection.setRequestProperty("Authorization", authorizationHeader);
            }
            connection.setFixedLengthStreamingMode(data.length);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();
            try {
                OutputStream output = connection.getOutputStream();
                try {
                    output.write(data);
                    output.flush();
                } finally {
                    output.close();
                }
            } finally {
                // Follow an HTTP Temporary Redirect if we get one,
                //
                // NB: Normally using the HttpURLConnection interface, we'd call
                // connection.setInstanceFollowRedirects(true) to enable 307 redirect following but
                // since we have the connection in streaming mode this does not work and we instead
                // re-direct manually.
                if (307 == connection.getResponseCode()) {
                    String location = connection.getHeaderField("Location");
                    connection.disconnect();
                    send(location, port, data);
                } else {
                    connection.disconnect();
                }
            }
        }

        public void validateUrl(String url) {
            try {
                new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Url: http://hostname/path");
            }
        }

        @Override
        public int validatePort(String port) {
            int portInt = super.validatePort(port);
            if (!(portInt == 80 || portInt == 443)) {
                throw new RuntimeException("Invalid port: 80 or 443 with protocol HTTP");
            }
            return portInt;
        }
    };


    private Gson gson = new GsonBuilder().setFieldNamingPolicy(
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private byte[] buildMessage(Job job, Run run, Phase phase, String status) {
        JobState jobState = new JobState();
        jobState.setName(job.getName());
        jobState.setUrl(job.getUrl());
        BuildState buildState = new BuildState();
        buildState.setNumber(run.number);
        buildState.setUrl(run.getUrl());
        buildState.setPhase(phase);
        buildState.setStatus(status);
        buildState.setDisplayName(run.getDisplayName());

        String rootUrl = Hudson.getInstance().getRootUrl();
        if (rootUrl != null) {
            buildState.setFullUrl(rootUrl + run.getUrl());
        }

        jobState.setBuild(buildState);

        ParametersAction paramsAction = run.getAction(ParametersAction.class);
        if (paramsAction != null && run instanceof AbstractBuild) {
            AbstractBuild build = (AbstractBuild) run;
            EnvVars env = new EnvVars();
            for (ParameterValue value : paramsAction.getParameters())
                if (!value.isSensitive())
                    value.buildEnvVars(build, env);
            buildState.setParameters(env);
        }

        return gson.toJson(jobState).getBytes();
    }

    abstract protected void send(String url, int port, byte[] data) throws IOException;

    public void validateUrl(String url) {
        if (url.trim().isEmpty()) {
            throw new RuntimeException("Invalid Url: hostname or IP");
        }
    }

    public int validatePort(String port) {
        try {
            int portInt = Integer.valueOf(port);
            if (portInt < 1 || portInt > 65535) {
                throw new RuntimeException("Invalid port: 1-65535");
            }
            return portInt;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid port: 1-65535");
        }
    }
}