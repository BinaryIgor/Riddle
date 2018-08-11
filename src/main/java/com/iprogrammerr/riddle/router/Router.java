package com.iprogrammerr.riddle.router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.iprogrammerr.riddle.exception.InvalidItemException;
import com.iprogrammerr.riddle.exception.NotResolvedRouteException;
import com.iprogrammerr.riddle.exception.NotSupportedMethodException;
import com.iprogrammerr.riddle.exception.RequestParameterException;
import com.iprogrammerr.riddle.exception.UnauthenticatedException;
import com.iprogrammerr.riddle.exception.UnauthorizedException;
import com.iprogrammerr.riddle.exception.WrongRequestBodyException;
import com.iprogrammerr.riddle.model.RouteWithPath;
import com.iprogrammerr.riddle.service.security.SecurityService;

public class Router extends HttpServlet {

    private String contextPath;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private final List<Route> routes = new ArrayList<>();
    private SecurityService securityService;

    public void init(String contextPath, List<Route> routes, SecurityService securityService) {
	this.contextPath = contextPath;
	this.routes.addAll(routes);
	this.securityService = securityService;
    }

    // TODO filtering, headers?
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	try {
	    securityService.check(request);
	    RouteWithPath route = resolveRoute(request.getRequestURI(), response);
	    resolveRequest(request, response, route);
	} catch (Exception exception) {
	    resolveException(exception, response);
	} finally {
	    setResponse(response);
	}
    }

    private void resolveException(Exception exception, HttpServletResponse response) throws IOException {
	exception.printStackTrace();
	if (exception instanceof NotResolvedRouteException) {
	    response.sendError(HttpStatus.NOT_FOUND_404, exception.getMessage());
	} else if (exception instanceof WrongRequestBodyException || exception instanceof InvalidItemException) {
	    response.sendError(HttpStatus.UNPROCESSABLE_ENTITY_422, exception.getMessage());
	} else if (exception instanceof NotSupportedMethodException || exception instanceof RequestParameterException) {
	    response.sendError(HttpStatus.BAD_REQUEST_400, exception.getMessage());
	} else if (exception instanceof NoResultException) {
	    response.setStatus(HttpStatus.NO_CONTENT_204);
	} else if (exception instanceof UnauthenticatedException) {
	    response.sendError(HttpStatus.UNAUTHORIZED_401, exception.getMessage());
	} else if (exception instanceof UnauthorizedException) {
	    response.sendError(HttpStatus.FORBIDDEN_403, exception.getMessage());
	}
    }

    private RouteWithPath resolveRoute(String requestUrl, HttpServletResponse response) {
	String[] urlParts = requestUrl.split("/");
	if (urlParts.length < 2) {
	    throw new NotResolvedRouteException();
	}
	Route route = getRoute(urlParts);
	if (route == null) {
	    throw new NotResolvedRouteException();
	}
	String path = getUrlRoute(route.getMainPath(), urlParts);
	if (path == null) {
	    throw new NotResolvedRouteException();
	}
	return new RouteWithPath(route, path);

    }

    private Route getRoute(String[] urlParts) {
	for (String urlPart : urlParts) {
	    if (urlPart == null || urlPart.isEmpty()) {
		continue;
	    }
	    for (Route route : routes) {
		if (route.getMainPath().equals(urlPart)) {
		    return route;
		}
	    }
	}
	return null;
    }

    private String getUrlRoute(String routePath, String[] urlParts) {
	int routeIndex = -1;
	for (int i = 0; i < urlParts.length - 1; i++) {
	    if (urlParts[i].equals(routePath)) {
		routeIndex = i + 1;
	    }
	}
	if (routeIndex == -1) {
	    return null;
	}
	return urlParts[routeIndex];
    }

    private void resolveRequest(HttpServletRequest request, HttpServletResponse response, RouteWithPath routeWithPath) {
	Route route = routeWithPath.getRoute();
	String method = request.getMethod();
	if (method.equals(GET)) {
	    route.resolveGetRequest(routeWithPath.getPath(), request, response);
	} else if (method.equals(POST)) {
	    route.resolvePostRequest(routeWithPath.getPath(), request, response);
	} else if (method.equals(PUT)) {
	    route.resolvePutRequest(routeWithPath.getPath(), request, response);
	} else if (method.equals(DELETE)) {
	    route.resolveDeleteRequest(routeWithPath.getPath(), request, response);
	} else {
	    throw new NotSupportedMethodException(method);
	}
    }

    private void setResponse(HttpServletResponse response) {
	response.setContentType("application/json");
	response.setHeader("Access-Control-Allow-Origin", "*");
    }

}
