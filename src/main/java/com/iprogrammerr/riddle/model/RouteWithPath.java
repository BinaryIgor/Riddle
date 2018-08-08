package com.iprogrammerr.riddle.model;

import com.iprogrammerr.riddle.router.Route;

public class RouteWithPath {

    private Route route;
    private String path;

    public RouteWithPath(Route route, String path) {
	this.route = route;
	this.path = path;
    }

    public Route getRoute() {
	return route;
    }

    public String getPath() {
	return path;
    }

}
