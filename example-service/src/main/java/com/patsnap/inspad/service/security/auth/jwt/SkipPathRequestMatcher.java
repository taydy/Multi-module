package com.patsnap.inspad.service.security.auth.jwt;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * SkipPathRequestMatcher
 */
public class SkipPathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher matchers;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        Assert.notNull(pathsToSkip, "pathsToSkip can't be null");
        if (!pathsToSkip.isEmpty()) {
        		List<RequestMatcher> requestMatchers = pathsToSkip.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
            matchers = new OrRequestMatcher(requestMatchers);
        }
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
    		if (matchers != null) {
    			if (matchers.matches(request)) {
    	            return false;
    	        }
    		}
        
        return processingMatcher.matches(request);
    }
}
