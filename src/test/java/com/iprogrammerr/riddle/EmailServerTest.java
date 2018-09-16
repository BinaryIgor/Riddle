package com.iprogrammerr.riddle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import com.iprogrammerr.riddle.configuration.ApplicationConfiguration;
import com.iprogrammerr.riddle.email.EmailServer;
import com.iprogrammerr.riddle.email.RiddleEmailServer;

public class EmailServerTest {

    @Test
    public void sedingFromPropertis() throws Exception {
	ApplicationConfiguration configuration = new ApplicationConfiguration(properties("/application.properties"));
	EmailServer emailServer = new RiddleEmailServer(configuration.adminEmail(), configuration.adminEmailPassword(),
		configuration.smtpHost(), configuration.smtpPort());
	emailServer.sendSigningUp("ceigor94@gmail.com", configuration.userActivationLinkBase() + "ajsdsad");
    }

    private static Properties properties(String path) throws IOException {
	Properties properties = new Properties();
	InputStream inputStream = RiddleApplication.class.getResourceAsStream(path);
	properties.load(inputStream);
	inputStream.close();
	return properties;
    }
}
