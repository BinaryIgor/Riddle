package com.iprogrammerr.riddle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.iprogrammerr.bright.server.Server;
import com.iprogrammerr.bright.server.configuration.ServerConfiguration;
import com.iprogrammerr.bright.server.parser.TypedUrlPatternParser;
import com.iprogrammerr.bright.server.parser.UrlPatternParser;
import com.iprogrammerr.riddle.configuration.ApplicationConfiguration;
import com.iprogrammerr.riddle.controller.UserController;
import com.iprogrammerr.riddle.database.SqlDatabase;
import com.iprogrammerr.riddle.database.SqlQueryExecutor;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.email.EmailService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.security.EncryptionService;
import com.iprogrammerr.riddle.service.security.SecurityService;
import com.iprogrammerr.riddle.service.validation.ValidationService;


public class RiddleApplication {

    // TODO log4j config?
    public static void main(String[] args) throws Exception {
	ApplicationConfiguration applicationConfiguration = getApplicationConfiguration();

	SqlDatabase connectionManager = new SqlDatabase(
		applicationConfiguration.getDatabaseUsername(), applicationConfiguration.getDatabasePassword(),
		applicationConfiguration.getJdbcUrl());
	SqlQueryExecutor executor = new SqlQueryExecutor(connectionManager);
	UrlPatternParser urlPatternParser = new TypedUrlPatternParser();
	
	UserService userService = new UserService(executor);
	ValidationService validationService = new ValidationService();
	JsonService jsonService = new JsonService();
	EmailService emailService = new EmailService(applicationConfiguration.getAdminEmail(),
		applicationConfiguration.getAdminEmailPassword(), applicationConfiguration.getSmtpHost(),
		applicationConfiguration.getSmtpPort());
	SecurityService securityService = new SecurityService(userService);
	EncryptionService encryptionService = new EncryptionService();

	UserController userController = new UserController(applicationConfiguration.getUserActivationLinkBase(),
		urlPatternParser, userService, validationService, securityService, encryptionService, emailService, jsonService);

	Server server = new Server(getServerConfiguration(), userController.createRequestResolvers(), new ArrayList<>());
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    connectionManager.close();
	    server.stop();
	}));
	server.start();
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
