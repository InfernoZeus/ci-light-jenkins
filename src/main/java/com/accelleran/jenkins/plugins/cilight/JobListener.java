package com.accelleran.jenkins.plugins.cilight;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

@Extension
@SuppressWarnings("rawtypes")
public class JobListener extends RunListener<Run> {

    public JobListener() {
        super(Run.class);
    }

    @Override
    public void onStarted(Run r, TaskListener listener) {
        Phase.STARTED.handle(r, listener);
    }

    @Override
    public void onCompleted(Run r, TaskListener listener) {
        Phase.COMPLETED.handle(r, listener);
    }

    @Override
    public void onFinalized(Run r) {
        Phase.FINISHED.handle(r, TaskListener.NULL);
    }

}