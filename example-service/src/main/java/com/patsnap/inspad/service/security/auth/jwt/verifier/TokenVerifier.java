package com.patsnap.inspad.service.security.auth.jwt.verifier;

/**
 *
 */
public interface TokenVerifier {
    boolean verify(String jti);
}
