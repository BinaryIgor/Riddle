package com.iprogrammerr.riddle.email;

public interface EmailServer {

    void send(Email email) throws Exception;

    void sendSigningUp(String recipent, String activatingLink) throws Exception;
}
