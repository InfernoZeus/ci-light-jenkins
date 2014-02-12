package com.accelleran.jenkins.plugins.cilight;

import com.accelleran.jenkins.plugins.cilight.model.BuildState;
import com.accelleran.jenkins.plugins.cilight.model.JobState;
import hudson.EnvVars;
import hudson.model.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
public enum Phase {
    STARTED, COMPLETED, FINISHED;

    public void handle(Run run, TaskListener listener) {
        Job job = run.getParent();
        CiLightProperty property = (CiLightProperty) run.getParent().getProperty(CiLightProperty.class);
        if (property != null) {
            JobState jobState = buildJobState(job, run, property);
            List<Endpoint> targets = CiLightGlobalConfiguration.get().getEndpoints();
            if (targets != null) {
                for (Endpoint target : targets) {
                    try {
                        target.getProtocol().send(target.getUrl(), target.getPort(), target.getFormat().serialize(jobState));
                    } catch (IOException e) {
                        e.printStackTrace(listener.error("Failed to notify "+target));
                    }
                }
            }
        }
    }

    private JobState buildJobState(Job job, Run run, CiLightProperty property) {
        JobState jobState = new JobState();

        jobState.setName(job.getName());
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            jobState.setServer(localhost.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            jobState.setServer("Unknown");
        }
        jobState.setUrl(job.getUrl());
        jobState.setCache(property.getCache());
        BuildState buildState = new BuildState();
        buildState.setNumber(run.number);
        buildState.setUrl(run.getUrl());
        buildState.setPhase(this);
        buildState.setStatus(getStatus(run));

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

        return jobState;
    }

    private String getStatus(Run r) {
        Result result = r.getResult();
        String status = null;
        if (result != null) {
            status = result.toString();
        }
        return status;
    }
}