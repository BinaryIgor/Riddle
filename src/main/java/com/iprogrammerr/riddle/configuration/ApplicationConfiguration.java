package com.iprogrammerr.riddle.configuration;

import java.util.Properties;

public class ApplicationConfiguration {

    private String serverContextPath;
    private int serverPort;
    private String serverDomain;

    public ApplicationConfiguration(Properties properties) {
	serverContextPath = properties.getProperty("server.context-path");
	serverPort = Integer.parseInt(properties.getProperty("server.port"));
	serverDomain = properties.getProperty("server.domain");
    }

    public String getServerContextPath() {
	return serverContextPath;
    }

    public int getServerPort() {
	return serverPort;
    }

    public String getServerDomain() {
	return serverDomain;
    }

}
