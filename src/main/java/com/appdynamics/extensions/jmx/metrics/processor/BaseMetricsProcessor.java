package com.appdynamics.extensions.jmx.metrics.processor;

import com.appdynamics.extensions.jmx.metrics.MetricDetails;
import com.appdynamics.extensions.metrics.Metric;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.jmx.utils.Constants.EMPTY_STRING;
import static com.appdynamics.extensions.jmx.utils.Constants.METRICS_SEPARATOR;

/**
 * Created by bhuvnesh.kumar on 3/11/19.
 */
public class BaseMetricsProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BaseMetricsProcessor.class);

    public static MetricDetails setMetricDetailsForBaseMetrics(MetricDetails metricDetails) {
        String attributeName = metricDetails.getAttribute().getName();
        Map<String, ?> props = (Map) metricDetails.getMetricPropsPerMetricName().get(attributeName);
        if (props == null) {
            logger.error("Could not find metric properties for {} ", attributeName);
        }
        String instanceKey = getInstanceKey(metricDetails.getInstance(), metricDetails.getmBeanKeys());
        String metricPath = generateMetricPath(metricDetails.getMetricPrefix(), attributeName, metricDetails.getDisplayName(), instanceKey);
        String attrVal = metricDetails.getAttribute().getValue().toString();
        Metric current_metric = new Metric(attributeName, attrVal, metricPath, props);
        metricDetails.addToJmxMetrics(current_metric);
        return metricDetails;
    }

    private static String getInstanceKey(ObjectInstance instance, List<String> mBeanKeys) {
        StringBuilder metricsKey = new StringBuilder();

        for (String key : mBeanKeys) {
            String value = getKeyProperty(instance, key);
            metricsKey.append(Strings.isNullOrEmpty(value) ? EMPTY_STRING : value + METRICS_SEPARATOR);
        }
        return metricsKey.toString();
    }

    private static String generateMetricPath(String metricPrefix, String attributeName, String displayName, String instanceKey) {
        String metricPath;
        if (Strings.isNullOrEmpty(metricPrefix)) {
            if (Strings.isNullOrEmpty(displayName)) {
                metricPath = instanceKey + attributeName;
            } else {
                metricPath = displayName + METRICS_SEPARATOR + instanceKey + attributeName;
            }
        } else {
            if (Strings.isNullOrEmpty(displayName)) {
                metricPath = metricPrefix + METRICS_SEPARATOR + instanceKey + attributeName;
            } else {
                metricPath = metricPrefix + METRICS_SEPARATOR + displayName + METRICS_SEPARATOR + instanceKey + attributeName;
            }
        }
        return metricPath;
    }

    private static ObjectName getObjectName(ObjectInstance instance) {
        return instance.getObjectName();
    }

    private static String getKeyProperty(ObjectInstance instance, String property) {
        if (instance == null) {
            return "";
        }
        return getObjectName(instance).getKeyProperty(property);
    }
}