package com.iprogrammerr.riddle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.validation.Validation;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.iprogrammerr.riddle.configuration.ApplicationConfiguration;
import com.iprogrammerr.riddle.dao.UserDao;
import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.entity.UserRole;
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

    public static void main(String[] args) throws Exception {
	ApplicationConfiguration configuration = getConfiguration();

	SessionFactory factory = buildSessionFactory();
	UserDao userDao = new UserDao(factory);

	UserService userService = new UserService(userDao);
	ValidationService validationService = new ValidationService(
		Validation.buildDefaultValidatorFactory().getValidator());
	JsonService jsonService = new JsonService();
	EmailService emailService = new EmailService(configuration.getAdminEmail(),
		configuration.getAdminEmailPassword(), configuration.getSmtpHost(), configuration.getSmtpPort());
	SecurityService securityService = new SecurityService(userService);
	EncryptionService encryptionService = new EncryptionService();

	List<Route> routes = new ArrayList<>();
	UserRoute userRoute = new UserRoute(configuration.getUserActivationLinkBase(), userService, validationService,
		securityService, encryptionService, emailService, jsonService);
	routes.add(userRoute);

	JettyServer server = new JettyServer(configuration.getServerPort(), factory);
	server.start(configuration.getServerContextPath(), routes, securityService);
    }

    private static ApplicationConfiguration getConfiguration() throws IOException {
	Properties properties = new Properties();
	InputStream inputStream = RiddleApplication.class.getResourceAsStream("/application.properties");
	properties.load(inputStream);
	inputStream.close();
	return new ApplicationConfiguration(properties);
    }

    private static SessionFactory buildSessionFactory() {
	return new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class)
		.addAnnotatedClass(UserRole.class).buildSessionFactory();
    }

}
