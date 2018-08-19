package com.iprogrammerr.riddle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.iprogrammerr.riddle.configuration.ApplicationConfiguration;
import com.iprogrammerr.riddle.database.DatabaseConnectionManager;
import com.iprogrammerr.riddle.database.QueryExecutor;
import com.iprogrammerr.riddle.router.Route;
import com.iprogrammerr.riddle.router.UserRoute;
import com.iprogrammerr.riddle.server.JettyServer;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.email.EmailService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.security.EncryptionService;
import com.iprogrammerr.riddle.service.security.SecurityService;
import com.iprogrammerr.riddle.service.validation.ValidationService;

public class RiddleApplication {

    // TODO log4j config?
    public static void main(String[] args) throws Exception {
	ApplicationConfiguration configuration = getConfiguration();

	DatabaseConnectionManager connectionManager = new DatabaseConnectionManager(configuration.getDatabaseUsername(),
		configuration.getDatabasePassword(), configuration.getJdbcUrl());
	QueryExecutor executor = new QueryExecutor(connectionManager);

	UserService userService = new UserService(executor);
	ValidationService validationService = new ValidationService();
	JsonService jsonService = new JsonService();
	EmailService emailService = new EmailService(configuration.getAdminEmail(),
		configuration.getAdminEmailPassword(), configuration.getSmtpHost(), configuration.getSmtpPort());
	SecurityService securityService = new SecurityService(userService);
	EncryptionService encryptionService = new EncryptionService();

	List<Route> routes = new ArrayList<>();
	UserRoute userRoute = new UserRoute(configuration.getUserActivationLinkBase(), userService, validationService,
		securityService, encryptionService, emailService, jsonService);
	routes.add(userRoute);

	JettyServer server = new JettyServer(configuration.getServerPort(), connectionManager);
	server.start(configuration.getServerContextPath(), routes, securityService);
    }

    private static ApplicationConfiguration getConfiguration() throws IOException {
	Properties properties = new Properties();
	InputStream inputStream = RiddleApplication.class.getResourceAsStream("/application.properties");
	properties.load(inputStream);
	inputStream.close();
	return new ApplicationConfiguration(properties);
    }

}
