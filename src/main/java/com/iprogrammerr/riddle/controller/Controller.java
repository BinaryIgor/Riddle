package com.iprogrammerr.riddle.controller;

import java.util.List;

import com.iprogrammerr.bright.server.respondent.HttpRespondent;

public interface Controller {

    List<HttpRespondent> createRequestResolvers();
}
