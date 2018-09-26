package com.iprogrammerr.riddle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.iprogrammerr.bright.server.Connector;
import com.iprogrammerr.bright.server.RequestResponseConnector;
import com.iprogrammerr.bright.server.Server;
import com.iprogrammerr.bright.server.application.Application;
import com.iprogrammerr.bright.server.application.HttpApplication;
import com.iprogrammerr.bright.server.cors.ConfigurableCors;
import com.iprogrammerr.bright.server.cors.Cors;
import com.iprogrammerr.bright.server.filter.ConditionalRequestFilter;
import com.iprogrammerr.bright.server.filter.HttpRequestFilter;
import com.iprogrammerr.bright.server.filter.RequestFilter;
import com.iprogrammerr.bright.server.method.GetMethod;
import com.iprogrammerr.bright.server.method.PostMethod;
import com.iprogrammerr.bright.server.method.RequestMethod;
import com.iprogrammerr.bright.server.protocol.HttpOneProtocol;
import com.iprogrammerr.bright.server.request.Request;
import com.iprogrammerr.bright.server.respondent.ConditionalRespondent;
import com.iprogrammerr.bright.server.respondent.HttpRespondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.rule.SingleRequestMethodRule;
import com.iprogrammerr.riddle.configuration.ApplicationConfiguration;
import com.iprogrammerr.riddle.database.Database;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.database.SqlDatabase;
import com.iprogrammerr.riddle.database.SqlDatabaseSession;
import com.iprogrammerr.riddle.database.SqlQueryTemplate;
import com.iprogrammerr.riddle.email.EmailServer;
import com.iprogrammerr.riddle.email.RiddleEmailServer;
import com.iprogrammerr.riddle.filter.AuthorizationFilter;
import com.iprogrammerr.riddle.respondent.user.RefreshTokenRespondent;
import com.iprogrammerr.riddle.respondent.user.SignInRespondent;
import com.iprogrammerr.riddle.respondent.user.SignUpRespondent;
import com.iprogrammerr.riddle.respondent.user.UserActivationRespondent;
import com.iprogrammerr.riddle.respondent.user.UserProfileRespondent;
import com.iprogrammerr.riddle.security.Encryption;
import com.iprogrammerr.riddle.security.ShaEncryption;
import com.iprogrammerr.riddle.security.token.JsonWebTokenTemplate;
import com.iprogrammerr.riddle.security.token.TokenTemplate;
import com.iprogrammerr.riddle.users.DatabaseUsers;
import com.iprogrammerr.riddle.users.DatabaseUsersRoles;
import com.iprogrammerr.riddle.users.Users;
import com.iprogrammerr.riddle.users.UsersRoles;

public final class RiddleApplication implements Application {

    private final Application application;

    public RiddleApplication(Application application) {
	this.application = application;
    }

    // TODO log4j config?
    public static void main(String[] args) throws Exception {
	ApplicationConfiguration applicationConfiguration = applicationConfiguration();

	Database database = new SqlDatabase(applicationConfiguration.databaseUsername(),
		applicationConfiguration.databasePassword(), applicationConfiguration.jdbcUrl());
	DatabaseSession session = new SqlDatabaseSession(database);
	QueryTemplate template = new SqlQueryTemplate();

	RequestMethod get = new GetMethod();
	RequestMethod post = new PostMethod();

	TokenTemplate accessTokenTemplate = new JsonWebTokenTemplate("access_token", 3_600_000L);
	TokenTemplate refreshTokenTemplate = new JsonWebTokenTemplate("refresh_token", 604_800_000L);

	EmailServer emailServer = new RiddleEmailServer(applicationConfiguration.adminEmail(),
		applicationConfiguration.adminEmailPassword(), applicationConfiguration.smtpHost(),
		applicationConfiguration.smtpPort());
	UsersRoles usersRoles = new DatabaseUsersRoles(session);
	Users users = new DatabaseUsers(session, usersRoles, template);
	Encryption encryption = new ShaEncryption();

	String authorizationHeaderKey = "Authorization";

	List<ConditionalRespondent> respondents = new ArrayList<>();
	ConditionalRespondent signInRespondent = new HttpRespondent("user/sign-in", post,
		new SignInRespondent(users, encryption, accessTokenTemplate, refreshTokenTemplate));
	respondents.add(signInRespondent);
	ConditionalRespondent singUpRespondent = new HttpRespondent("user/sign-up", post, new SignUpRespondent(
		applicationConfiguration.userActivationLinkBase(), users, emailServer, encryption));
	respondents.add(singUpRespondent);
	ConditionalRespondent refreshTokenRespondent = new HttpRespondent("user/token-refresh", post,
		new RefreshTokenRespondent(users, accessTokenTemplate, refreshTokenTemplate));
	respondents.add(refreshTokenRespondent);
	ConditionalRespondent userActivationRespondent = new HttpRespondent("user/activate", post,
		new UserActivationRespondent(session, template, encryption));
	respondents.add(userActivationRespondent);
	ConditionalRespondent userProfileRespondent = new HttpRespondent("user/profile", get,
		new UserProfileRespondent(users, accessTokenTemplate, authorizationHeaderKey));
	respondents.add(userProfileRespondent);

	RequestFilter authorizationFilter = new AuthorizationFilter(authorizationHeaderKey, "Bearer");
	List<ConditionalRequestFilter> filters = new ArrayList<>();
	filters.add(new HttpRequestFilter("user/profile", new SingleRequestMethodRule(post), authorizationFilter));

	Cors cors = new ConfigurableCors("http://localhost:8080", "*", "GET, POST");

	RiddleApplication application = new RiddleApplication(
		new HttpApplication("riddle", cors, respondents, filters));

	Connector connector = new RequestResponseConnector(new HttpOneProtocol(), application);
	Server server = new Server(8000, connector);
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    database.close();
	    server.stop();
	}));
	server.start();
    }

    private static ApplicationConfiguration applicationConfiguration() throws IOException {
	return new ApplicationConfiguration(properties("/application.properties"));
    }

    private static Properties properties(String path) throws IOException {
	Properties properties = new Properties();
	InputStream inputStream = RiddleApplication.class.getResourceAsStream(path);
	properties.load(inputStream);
	inputStream.close();
	return properties;
    }

    @Override
    public Optional<Response> respond(Request request) {
	return application.respond(request);
    }

}
