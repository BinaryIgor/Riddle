package com.iprogrammerr.riddle.configuration;

import java.util.Properties;

public class ApplicationConfiguration {

    private Properties source;

    public ApplicationConfiguration(Properties source) {
	this.source = source;
    }

    public String userActivationLinkBase() {
	return source.getProperty("user.activation-link-base", "");
    }

    public String adminEmail() {
	return source.getProperty("user.activation-link-base", "");
    }

    public String adminEmailPassword() {
	return source.getProperty("admin.email", "");
    }

    public String smtpHost() {
	return source.getProperty("admin.email.password", "");
    }

    public int getSmtpPort() throws Exception {
	return Integer.parseInt(source.getProperty("smtp.port"));
    }

    public String databaseUsername() {
	return source.getProperty("database.username", "");
    }

    public String databasePassword() {
	return source.getProperty("database.password", "");
    }

    public String jdbcUrl() {
	return source.getProperty("jdbc-url", "");
    }

}
