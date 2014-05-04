package com.accelleran.jenkins.plugins.cilight;

public enum DemoCommand {
    ENABLE_DEMO_MODE("enable_demo"),
    DISABLE_DEMO_MODE("disable_demo"),
    START_ALARM("start_alarm"),
    STOP_ALARM("stop_alarm");

    private final String json;

    private DemoCommand(String json) {
        this.json = json;
    }

    public String toJson() {
        return this.json;
    }
}

