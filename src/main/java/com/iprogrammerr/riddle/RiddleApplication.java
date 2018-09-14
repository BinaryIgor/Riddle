package com.iprogrammerr.riddle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.iprogrammerr.bright.server.Server;
import com.iprogrammerr.bright.server.configuration.BrightServerConfiguration;
import com.iprogrammerr.bright.server.filter.ConditionalRequestFilter;
import com.iprogrammerr.bright.server.method.GetMethod;
import com.iprogrammerr.bright.server.method.PostMethod;
import com.iprogrammerr.bright.server.method.RequestMethod;
import com.iprogrammerr.bright.server.respondent.ConditionalRespondent;
import com.iprogrammerr.bright.server.respondent.HttpRespondent;
import com.iprogrammerr.riddle.configuration.ApplicationConfiguration;
import com.iprogrammerr.riddle.database.Database;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.database.SqlDatabase;
import com.iprogrammerr.riddle.database.SqlDatabaseSession;
import com.iprogrammerr.riddle.database.SqlQueryTemplate;
import com.iprogrammerr.riddle.respondent.user.SignInRespondent;
import com.iprogrammerr.riddle.security.Encryption;
import com.iprogrammerr.riddle.security.ShaEncryption;
import com.iprogrammerr.riddle.user.DatabaseUsers;
import com.iprogrammerr.riddle.user.Users;

public class RiddleApplication {

    // TODO log4j config?
    public static void main(String[] args) throws Exception {
	ApplicationConfiguration applicationConfiguration = applicationConfiguration();

	Database database = new SqlDatabase(applicationConfiguration.getDatabaseUsername(),
		applicationConfiguration.getDatabasePassword(), applicationConfiguration.getJdbcUrl());
	DatabaseSession session = new SqlDatabaseSession(database);
	QueryTemplate queryTemplate = new SqlQueryTemplate();

	RequestMethod get = new GetMethod();
	RequestMethod post = new PostMethod();

	Users users = new DatabaseUsers(session, queryTemplate);
	Encryption encryption = new ShaEncryption();
	List<ConditionalRespondent> respondents = new ArrayList<>();
	ConditionalRespondent signInRespondent = new HttpRespondent("user/sign-in", post,
		new SignInRespondent(users, encryption));
	respondents.add(signInRespondent);

	List<ConditionalRequestFilter> filters = new ArrayList<>();

	Server server = new Server(serverConfiguration(), respondents, filters);
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    database.close();
	    server.stop();
	}));
	server.start();
    }

    private static ApplicationConfiguration applicationConfiguration() throws IOException {
	return new ApplicationConfiguration(properties("/application.properties"));
    }

    private static BrightServerConfiguration serverConfiguration() throws IOException {
	return new BrightServerConfiguration(properties("/server.properties"));
    }

    private static Properties properties(String path) throws IOException {
	Properties properties = new Properties();
	InputStream inputStream = RiddleApplication.class.getResourceAsStream(path);
	properties.load(inputStream);
	inputStream.close();
	return properties;
    }

}
