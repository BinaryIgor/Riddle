package com.iprogrammerr.riddle.security.token;

public abstract class TokenTemplateEnvelope implements TokenTemplate {

    private final TokenTemplate base;

    public TokenTemplateEnvelope(TokenTemplate base) {
	this.base = base;
    }

    @Override
    public final byte[] secret() {
	return base.secret();
    }

    @Override
    public final String typeKey() {
	return base.typeKey();
    }

    @Override
    public final String type() {
	return base.type();
    }

    @Override
    public long validity() {
	return base.validity();
    }
}
