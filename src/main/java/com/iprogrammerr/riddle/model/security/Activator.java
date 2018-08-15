package com.iprogrammerr.riddle.model.security;

import javax.validation.constraints.NotNull;

public class Activator {

    public long id;
    @NotNull
    public String hash;

}
