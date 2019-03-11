package com.appdynamics.extensions.jmx.metrics;

import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.jmx.JMXUtil;
import com.appdynamics.extensions.jmx.commons.JMXConnectionAdapter;
import com.appdynamics.extensions.jmx.filters.IncludeFilter;
import com.appdynamics.extensions.metrics.Metric;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.appdynamics.extensions.jmx.metrics.Constants.*;

/**
 * Created by bhuvnesh.kumar on 12/19/18.
 */
public class JMXMetricsProcessor {

    private static final Logger logger = LoggerFactory.getLogger(JMXMetricsProcessor.class);
    private JMXConnectionAdapter jmxConnectionAdapter;
    private JMXConnector jmxConnector;
    private MonitorContextConfiguration monitorContextConfiguration;

    public JMXMetricsProcessor(MonitorContextConfiguration monitorContextConfiguration, JMXConnectionAdapter jmxConnectionAdapter, JMXConnector jmxConnector) {
        this.monitorContextConfiguration = monitorContextConfiguration;
        this.jmxConnectionAdapter = jmxConnectionAdapter;
        this.jmxConnector = jmxConnector;
    }

    public List<Metric> getJMXMetrics(Map mBean, Map<String, ?> metricsPropertiesMap, String metricPrefix, String displayName) throws
            MalformedObjectNameException, IOException, IntrospectionException, InstanceNotFoundException,
            ReflectionException {
        List<Metric> jmxMetrics = Lists.newArrayList();
        String configObjectName = JMXUtil.convertToString(mBean.get(OBJECT_NAME), EMPTY_STRING);

        Set<ObjectInstance> objectInstances = jmxConnectionAdapter.queryMBeans(jmxConnector, ObjectName.getInstance
                (configObjectName));
        for (ObjectInstance instance : objectInstances) {
            List<String> metricNamesDictionary = jmxConnectionAdapter.getReadableAttributeNames(jmxConnector, instance);
            List<String> metricNamesToBeExtracted = applyFilters(mBean, metricNamesDictionary);
            List<Attribute> attributes = jmxConnectionAdapter.getAttributes(jmxConnector, instance.getObjectName(),
                    metricNamesToBeExtracted.toArray(new String[metricNamesToBeExtracted.size()]));
            List<String> mBeanKeys = getMBeanKeys(mBean);
            MetricDetails metricDetails = getMetricDetails(metricsPropertiesMap, metricPrefix, displayName, jmxMetrics, instance, mBeanKeys);
            collectMetrics(metricDetails, attributes, instance);
            jmxMetrics.addAll(metricDetails.getJmxMetrics());
        }
        return jmxMetrics;
    }

    private MetricDetails getMetricDetails(Map<String, ?> metricsPropertiesMap, String metricPrefix, String displayName, List<Metric> jmxMetrics, ObjectInstance instance, List<String> mBeanKeys) {
        List<Metric> metricList = Lists.newArrayList();

        return new MetricDetails.Builder()
                .metricPrefix(metricPrefix)
                .jmxMetrics(metricList)
                .instance(instance)
                .metricPropsPerMetricName(metricsPropertiesMap)
                .mBeanKeys(mBeanKeys)
                .displayName(displayName)
                .build();
    }


    private List<String> applyFilters(Map aConfigMBean, List<String> metricNamesDictionary) {
        Set<String> filteredSet = Sets.newHashSet();
        Map configMetrics = (Map) aConfigMBean.get(METRICS);
        List includeDictionary = (List) configMetrics.get(INCLUDE);
        new IncludeFilter(includeDictionary).applyFilter(filteredSet, metricNamesDictionary);
        return Lists.newArrayList(filteredSet);
    }

    private List<String> getMBeanKeys(Map aConfigMBean) {
        List<String> mBeanKeys = (List) aConfigMBean.get(MBEANKEYS);
        return mBeanKeys;
    }

    private void collectMetrics(MetricDetails metricInfo, List<Attribute> attributes, ObjectInstance instance) {
        for (Attribute attribute : attributes) {
            try {
                metricInfo.setAttribute(attribute);
                JMXMetricsDataFilter.checkAttributeTypeAndSetDetails(metricInfo, monitorContextConfiguration);
                int a = 4 + 5;
            } catch (Exception e) {
                logger.error("Error collecting value for {} {}", instance.getObjectName(), attribute.getName(), e);
            }
        }
    }

}
