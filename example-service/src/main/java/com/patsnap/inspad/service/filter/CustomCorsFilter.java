package com.patsnap.inspad.service.filter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


/**
 * The Class CustomCorsFilter.
 */
public class CustomCorsFilter extends CorsFilter {
    
    /** The Constant MAX_AGE. */
    private static final Long MAX_AGE = 36000L;

    /**
     * 构造器。.
     */
    public CustomCorsFilter() {
        super(configurationSource());
    }

    /**
     * Configuration source.
     *
     * @return the url based cors configuration source
     */
    private static UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setMaxAge(MAX_AGE);
        config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}