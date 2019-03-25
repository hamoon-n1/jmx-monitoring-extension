/*
 *   Copyright 2019 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.jmx.metrics;

import javax.management.ObjectInstance;
import java.util.List;
import java.util.Map;

/**
 * Created by bhuvnesh.kumar on 3/7/19.
 */
public class MetricDetails {
    // TODO metricPrefix is not something unique to each metric, this can be avoided in this class
    private String metricPrefix;
    private ObjectInstance instance;
    private Map<String, ?> metricPropsPerMetricName;
    // TODO why fully qualified name, there are no conflicts here
    private java.util.List<String> mBeanKeys;
    private String displayName;
    // TODO not required in 2.2.0
    private List<Map<String, String>> metricCharacterReplacer;
    private String separator;

    public List<Map<String, String>> getMetricCharacterReplacer() {
        return metricCharacterReplacer;
    }

    public void setMetricCharacterReplacer(List<Map<String, String>> metricCharacterReplacer) {
        this.metricCharacterReplacer = metricCharacterReplacer;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getMetricPrefix() {
        return metricPrefix;
    }

    public void setMetricPrefix(String metricPrefix) {
        this.metricPrefix = metricPrefix;
    }

    public ObjectInstance getInstance() {
        return instance;
    }

    public void setInstance(ObjectInstance instance) {
        this.instance = instance;
    }

    public Map<String, ?> getMetricPropsPerMetricName() {
        return metricPropsPerMetricName;
    }

    public void setMetricPropsPerMetricName(Map<String, ?> metricPropsPerMetricName) {
        this.metricPropsPerMetricName = metricPropsPerMetricName;
    }

    public List<String> getmBeanKeys() {
        return mBeanKeys;
    }

    public void setmBeanKeys(List<String> mBeanKeys) {
        this.mBeanKeys = mBeanKeys;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public static class Builder {
        // TODO same comments as in JMXMonitorTask.Builder
        private MetricDetails task = new MetricDetails();

        Builder metricPrefix(String metricPrefix) {
            task.metricPrefix = metricPrefix;
            return this;
        }

        Builder instance(ObjectInstance instance) {
            task.instance = instance;
            return this;
        }

        Builder metricPropsPerMetricName(Map<String, ?> metricPropsPerMetricName) {
            task.metricPropsPerMetricName = metricPropsPerMetricName;
            return this;
        }

        Builder mBeanKeys(List<String> mBeanKeys) {
            task.mBeanKeys = mBeanKeys;
            return this;
        }

        Builder displayName(String displayName) {
            task.displayName = displayName;
            return this;
        }

        Builder metricCharacterReplacer(List<Map<String, String>> metricCharacterReplacer) {
            task.metricCharacterReplacer = metricCharacterReplacer;
            return this;
        }

        Builder separator(String separator) {
            task.separator = separator;
            return this;
        }

        MetricDetails build() {
            return task;
        }
    }

}
