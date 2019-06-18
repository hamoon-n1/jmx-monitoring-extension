/*
 *   Copyright 2019 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */


package com.appdynamics.extensions.jmx.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public  class SSLUtilsTest {

    @Before
    public void setUpConnectionWithoutSSL(){

        Properties props = new Properties();
        props.setProperty("com.sun.management.jmxremote.authenticate", "false");
        props.setProperty("com.sun.management.jmxremote.ssl", "false");
        props.setProperty("com.sun.management.jmxremote.registry.ssl", "false");
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");

        JMXConnectorServer server = sun.management.jmxremote.ConnectorBootstrap
                .startRemoteConnectorServer("9993", props);
    }

    @Test
    public void whenNotUsingSslThenTestServerConnection() throws Exception {

        JMXServiceURL serviceUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:9993/jmxrmi");
        Map env = new HashMap();
        JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceUrl, env);
        Assert.assertNotNull(jmxConnector);

    }

//    @Before
//    public void setUpConnectionWithSslAndCorrectKeys(){
//        System.setProperty("javax.net.ssl.keyStore", "src/test/resources/keystore/kafka.server.keystore.jks");
//        System.setProperty("javax.net.ssl.keyStorePassword", "test1234");
//        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
//        System.setProperty("com.sun.management.jmxremote.port", "6789");
//        MonitorContextConfiguration contextConfiguration = new MonitorContextConfiguration
//                ("JMX Monitor",
//                        "Custom Metrics|JMX Monitor|", PathResolver.resolveDirectory(AManagedMonitor.class),
//                        Mockito.mock(AMonitorJob.class));
//        contextConfiguration.setConfigYml("src/test/resources/conf/config_ssl_correct_keys.yml");
//        Map configMap = contextConfiguration.getConfigYml();
//        SslUtils sslUtils = new SslUtils();
//        sslUtils.setSslProperties(configMap);
//        Properties connectionProperties = new Properties();
//        connectionProperties.setProperty("com.sun.management.jmxremote.authenticate", "false");
//        connectionProperties.setProperty("com.sun.management.jmxremote.ssl", "true");
//        connectionProperties.setProperty("com.sun.management.jmxremote.registry.ssl", "false");
//
//        JMXConnectorServer server = sun.management.jmxremote.ConnectorBootstrap
//              .startRemoteConnectorServer("6789", connectionProperties);
//    }
//
//    @Test
//    public void whenUsingSslAndCorrectKeysThenTestServerConnection() throws Exception {
//        JMXServiceURL serviceUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:6789/jmxrmi");
//        Map env = new HashMap();
//        env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, new SslRMIClientSocketFactory());
//        env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, new SslRMIServerSocketFactory());
//        JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceUrl, env);
//        Assert.assertNotNull(jmxConnector);
//    }

//    @Before
//    public void setUpConnectionWithIncorrectKeys(){
//        System.setProperty("javax.net.ssl.keyStore", "src/test/resources/keystore/kafka.server.keystore.jks");
//        System.setProperty("javax.net.ssl.keyStorePassword", "test1234");
//        MonitorContextConfiguration contextConfiguration = new MonitorContextConfiguration
//                ("Kafka Monitor",
//                        "Custom Metrics|Kafka|", PathResolver.resolveDirectory(AManagedMonitor.class),
//                        Mockito.mock(AMonitorJob.class));
//        contextConfiguration.setConfigYml("src/test/resources/conf/config_ssl_incorrect_keys.yml");
//        Map configMap = contextConfiguration.getConfigYml();
//        SslUtils sslUtils = new SslUtils();
//        sslUtils.setSslProperties(configMap);
//        Properties props = new Properties();
//        props.setProperty("com.sun.management.jmxremote.authenticate", "false");
//        props.setProperty("com.sun.management.jmxremote.ssl", "true");
//        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
//        JMXConnectorServer server = sun.management.jmxremote.ConnectorBootstrap
//              .startRemoteConnectorServer("6789", props);
//    }
//
//
//    @Test
//    public void testSSLServerConnectionWithIncorrectTrustStore() {
//        try {
//            JMXServiceURL serviceUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:6789/jmxrmi");
//            Map env = new HashMap();
//            env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, new SslRMIClientSocketFactory());
//            env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, new SslRMIServerSocketFactory());
//            JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceUrl, env);
//        } catch (MalformedURLException e) {
//
//        } catch (IOException e) {
//            Assert.assertEquals( e.getCause().toString(),
//                    "javax.net.ssl.SSLException: java.lang.RuntimeException: " +
//                            "Unexpected error: java.security.InvalidAlgorithmParameterException: " +
//                            "the trustAnchors parameter must be non-empty");
//        }
//    }



}