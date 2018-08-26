package com.iprogrammerr.riddle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.iprogrammerr.riddle.configuration.ApplicationConfiguration;
import com.iprogrammerr.riddle.controller.UserController;
import com.iprogrammerr.riddle.database.DatabaseConnectionManager;
import com.iprogrammerr.riddle.database.QueryExecutor;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.email.EmailService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.security.EncryptionService;
import com.iprogrammerr.riddle.service.security.SecurityService;
import com.iprogrammerr.riddle.service.validation.ValidationService;
import com.iprogrammerr.simple.http.server.Server;
import com.iprogrammerr.simple.http.server.configuration.ServerConfiguration;

public class RiddleApplication {

    // TODO log4j config?
    public static void main(String[] args) throws Exception {
	ApplicationConfiguration applicationConfiguration = getApplicationConfiguration();

	DatabaseConnectionManager connectionManager = new DatabaseConnectionManager(
		applicationConfiguration.getDatabaseUsername(), applicationConfiguration.getDatabasePassword(),
		applicationConfiguration.getJdbcUrl());
	QueryExecutor executor = new QueryExecutor(connectionManager);

	UserService userService = new UserService(executor);
	ValidationService validationService = new ValidationService();
	JsonService jsonService = new JsonService();
	EmailService emailService = new EmailService(applicationConfiguration.getAdminEmail(),
		applicationConfiguration.getAdminEmailPassword(), applicationConfiguration.getSmtpHost(),
		applicationConfiguration.getSmtpPort());
	SecurityService securityService = new SecurityService(userService);
	EncryptionService encryptionService = new EncryptionService();

	UserController userController = new UserController(applicationConfiguration.getUserActivationLinkBase(),
		userService, validationService, securityService, encryptionService, emailService, jsonService);

	Server server = new Server(getServerConfiguration(), userController.getRequestResolvers());
	server.start();
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    connectionManager.close();
	    server.stop();
	}));
    }

    private static ApplicationConfiguration getApplicationConfiguration() throws IOException {
	return new ApplicationConfiguration(getProperties("/application.properties"));
    }

    private static ServerConfiguration getServerConfiguration() throws IOException {
	return new ServerConfiguration(getProperties("/server.properties"));
    }

    private static Properties getProperties(String path) throws IOException {
	Properties properties = new Properties();
	InputStream inputStream = RiddleApplication.class.getResourceAsStream(path);
	properties.load(inputStream);
	inputStream.close();
	return properties;
    }

}
