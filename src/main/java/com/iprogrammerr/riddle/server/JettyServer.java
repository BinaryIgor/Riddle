package com.iprogrammerr.riddle.server;

import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.hibernate.SessionFactory;

import com.iprogrammerr.riddle.router.Route;
import com.iprogrammerr.riddle.router.Router;

public class JettyServer {

    private final Server server = new Server();
    private int port;
    private SessionFactory sessionFactory;

    public JettyServer(int port) {
	this.port = port;
    }

    public JettyServer(int port, SessionFactory sessionFactory) {
	this.port = port;
	this.sessionFactory = sessionFactory;
    }

    public void start(String contextPath, List<Route> routes) throws Exception {
	ServerConnector connector = new ServerConnector(server);
	connector.setPort(port);
	server.setConnectors(new Connector[] { connector });
	ServletContextHandler contextHandler = new ServletContextHandler();
	contextHandler.setContextPath("/" + contextPath + "/");
	Router router = (Router) contextHandler.addServlet(Router.class, "/*").getServlet();
	router.init(contextPath, routes);
	server.setHandler(contextHandler);
	server.start();
    }

    public void stop() throws Exception {
	if (sessionFactory != null) {
	    sessionFactory.close();
	}
	server.stop();
    }
}
