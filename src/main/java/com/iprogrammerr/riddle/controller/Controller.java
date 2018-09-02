package com.iprogrammerr.riddle.controller;

import java.util.List;

import com.iprogrammerr.bright.server.resolver.RequestResolver;

public interface Controller {

    List<RequestResolver> createRequestResolvers();
}
