package com.patsnap.inspad.service.security.auth.model;

/**
 * Scopes
 * 
 */
public enum Scopes {
    REFRESH_TOKEN;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
}
