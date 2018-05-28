package com.patsnap.inspad.service.security.auth.ajax;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.patsnap.inspad.core.dto.UserInfo;
import com.patsnap.inspad.core.dto.UserRole;
import com.patsnap.inspad.core.service.user.UserService;

/**
 * AjaxAuthenticationProvider.
 * @author Taydy
 * @version v1.0.0 2017年11月10日
 * @since JDK1.8
 */
@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    
    @Autowired
    public AjaxAuthenticationProvider(final UserService userService, final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userService  =userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        
        LoginRequest loginRequest = (LoginRequest) authentication.getPrincipal();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        UserInfo userInfo = userService.getByUsername(username);
        
        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }
        
        if (!UserInfo.STATUS_ACTIVE.equals(userInfo.getStatus())) {
        	throw new BadCredentialsException("Authentication Failed. Account has been disabled.");
        }
        
        if (!UserInfo.STATUS_ACTIVE.equals(userInfo.getStatus())) {
        		throw new BadCredentialsException("Authentication Failed. Account expire.");
        }
        
        List<UserRole> userRoles = Arrays.asList(UserRole.COMMON);
        
        List<GrantedAuthority> authorities = userRoles.stream()
                .map(authentity -> new SimpleGrantedAuthority(authentity.authority()))
                .collect(Collectors.toList());
        
        userInfo.setAuthorities(authorities);
        
        return new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
