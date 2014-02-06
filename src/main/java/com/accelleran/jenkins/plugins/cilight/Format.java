package com.accelleran.jenkins.plugins.cilight;

import com.accelleran.jenkins.plugins.cilight.model.JobState;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;

public enum Format {
    XML {
        private XStream xstream = new XStream();

        @Override
        protected byte[] serialize(JobState jobState) throws IOException {
            xstream.processAnnotations(JobState.class);
            return xstream.toXML(jobState).getBytes();
        }
    },
    JSON {
        private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        @Override
        protected byte[] serialize(JobState jobState) throws IOException {
            return gson.toJson(jobState).getBytes();
        }
    };

    abstract protected byte[] serialize(JobState jobState) throws IOException;
}
