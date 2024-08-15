package com.iw.nems_test_subscriber_top.adapter.in.event_subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.solace.messaging.config.SolaceProperties;

public class GlobalProperties {

    private static Properties properties = new Properties();
    public static String propertyFile = "application.properties";

    static {
        try {
            // Load the properties file
            properties.load(GlobalProperties.class.getClassLoader().getResourceAsStream(propertyFile));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);

        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static Properties loadProperties() throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = GlobalProperties.class
                .getClassLoader()
                .getResourceAsStream(propertyFile);
        configuration.load(inputStream);
        inputStream.close();
        return configuration;
    }

    public static Properties setSolaceProperties()  {

        return setSolaceProperties("OAUTH");
    }
    public static Properties setSolaceProperties(String authType) {

        switch (authType.toUpperCase()) {
            case "BASIC":
                return setSolaceBasicProperties();  
            default:
                return setSolaceOAuthProperties();
        }

    }

    private static Properties setSolaceOAuthProperties() {

        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, getProperty("nems.broker.host")); // host:port
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, getProperty("nems.broker.vpn")); // message-vpn
        properties.setProperty(SolaceProperties.TransportLayerProperties.RECONNECTION_ATTEMPTS, "20"); // recommended
        properties.setProperty(SolaceProperties.TransportLayerProperties.CONNECTION_RETRIES_PER_HOST, "5");

        return properties;
    }

    private static Properties setSolaceBasicProperties() {

        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, getProperty("nems.broker.host")); // host:port
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, getProperty("nems.broker.vpn")); // message-vpn
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, getProperty("nems.broker.username")); // client-username
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, getProperty("nems.broker.password"));
        properties.setProperty(SolaceProperties.TransportLayerProperties.RECONNECTION_ATTEMPTS, "20"); // recommended
        properties.setProperty(SolaceProperties.TransportLayerProperties.CONNECTION_RETRIES_PER_HOST, "5");

        return properties;
    }
}