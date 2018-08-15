package com.iprogrammerr.riddle.router;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.eclipse.jetty.http.HttpStatus;

import com.iprogrammerr.riddle.exception.crud.DuplicateEntryException;
import com.iprogrammerr.riddle.exception.request.RequestParameterException;
import com.iprogrammerr.riddle.exception.request.WrongRequestBodyException;
import com.iprogrammerr.riddle.exception.router.NotResolvedRouteException;
import com.iprogrammerr.riddle.exception.router.NotSupportedMethodException;
import com.iprogrammerr.riddle.exception.security.UnauthenticatedException;
import com.iprogrammerr.riddle.exception.security.UnauthorizedException;
import com.iprogrammerr.riddle.exception.validation.InvalidItemException;
import com.iprogrammerr.riddle.model.router.RouteWithPath;
import com.iprogrammerr.riddle.service.security.SecurityService;

public class Router extends HttpServlet {

    private String contextPath;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS_HEADER = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
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
	    setResponse(response);
	    RouteWithPath route = resolveRoute(request.getRequestURI(), response);
	    resolveRequest(request, response, route);
	} catch (Exception exception) {
	    resolveException(exception, response);
	}
    }

    private void resolveException(Exception exception, HttpServletResponse response) throws IOException {
	exception.printStackTrace();
	if (exception instanceof NotResolvedRouteException) {
	    response.setStatus(HttpStatus.NOT_FOUND_404);
	} else if (isUnprocessableEntity(exception)) {
	    response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY_422);
	} else if (isBadRequest(exception)) {
	    response.setStatus(HttpStatus.BAD_REQUEST_400);
	    setBody(exception.getMessage(), response);
	} else if (exception instanceof NoResultException) {
	    response.setStatus(HttpStatus.NO_CONTENT_204);
	} else if (exception instanceof UnauthenticatedException) {
	    response.setStatus(HttpStatus.UNAUTHORIZED_401);
	} else if (exception instanceof UnauthorizedException) {
	    response.setStatus(HttpStatus.FORBIDDEN_403);
	} else {
	    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
	}
	setBody(exception.getMessage(), response);
    }

    private boolean isBadRequest(Exception exception) {
	return exception instanceof NotSupportedMethodException || exception instanceof RequestParameterException;
    }

    private boolean isUnprocessableEntity(Exception exception) {
	return exception instanceof WrongRequestBodyException || exception instanceof InvalidItemException
		|| exception instanceof ConstraintViolationException
		|| exception instanceof org.hibernate.exception.ConstraintViolationException
		|| exception instanceof DuplicateEntryException;
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
	response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "Content-Type");
	response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    }

    protected void setBody(String message, HttpServletResponse response) {
	try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
	    writer.write(message);
	} catch (IOException exception) {
	    exception.printStackTrace();
	}
    }

}
