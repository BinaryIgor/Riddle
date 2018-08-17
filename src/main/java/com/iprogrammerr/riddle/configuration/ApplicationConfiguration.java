package com.iprogrammerr.riddle.configuration;

import java.util.Properties;

public class ApplicationConfiguration {

    private String serverContextPath;
    private int serverPort;
    private String userActivationLinkBase;
    private String adminEmail;
    private String adminEmailPassword;
    private String smtpHost;
    private int smtpPort;
    private String databaseUsername;
    private String databasePassword;
    private String jdbcUrl;

    public ApplicationConfiguration(Properties properties) {
	serverContextPath = properties.getProperty("server.context-path");
	serverPort = Integer.parseInt(properties.getProperty("server.port"));
	userActivationLinkBase = properties.getProperty("user.activation-link-base");
	adminEmail = properties.getProperty("admin.email");
	adminEmailPassword = properties.getProperty("admin.email.password");
	smtpHost = properties.getProperty("smtp.host");
	smtpPort = Integer.parseInt(properties.getProperty("smtp.port"));
	databaseUsername = properties.getProperty("database.username");
	databasePassword = properties.getProperty("database.password");
	jdbcUrl = properties.getProperty("jdbc-url");
    }

    public String getServerContextPath() {
	return serverContextPath;
    }

    public int getServerPort() {
	return serverPort;
    }

    public String getUserActivationLinkBase() {
	return userActivationLinkBase;
    }

    public String getAdminEmail() {
	return adminEmail;
    }

    public String getAdminEmailPassword() {
	return adminEmailPassword;
    }

    public String getSmtpHost() {
	return smtpHost;
    }

    public int getSmtpPort() {
	return smtpPort;
    }

    public String getDatabaseUsername() {
	return databaseUsername;
    }

    public String getDatabasePassword() {
	return databasePassword;
    }

    public String getJdbcUrl() {
	return jdbcUrl;
    }

}
