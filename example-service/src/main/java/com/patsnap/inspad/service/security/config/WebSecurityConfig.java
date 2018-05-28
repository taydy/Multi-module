package com.patsnap.inspad.service.security.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patsnap.inspad.core.dto.UserRole;
import com.patsnap.inspad.service.filter.CustomCorsFilter;
import com.patsnap.inspad.service.security.auth.ajax.AjaxAuthenticationProvider;
import com.patsnap.inspad.service.security.auth.ajax.AjaxAwareAuthenticationFailureHandler;
import com.patsnap.inspad.service.security.auth.ajax.AjaxAwareAuthenticationSuccessHandler;
import com.patsnap.inspad.service.security.auth.ajax.AjaxLoginProcessingFilter;
import com.patsnap.inspad.service.security.auth.jwt.JwtAuthenticationProvider;
import com.patsnap.inspad.service.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.patsnap.inspad.service.security.auth.jwt.SkipPathRequestMatcher;
import com.patsnap.inspad.service.security.auth.jwt.extractor.TokenExtractor;
import com.patsnap.inspad.service.security.endpoint.RestAuthenticationEntryPoint;

/**
 * WebSecurityConfig。
 * @author Taydy
 * @version v1.0.0 2017年11月10日
 * @since JDK1.8
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
    public static final String FORM_BASED_REGISTER_ENTRY_POINT = "/api/auth/register";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/common/**";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    
    @Autowired
    private AjaxAwareAuthenticationSuccessHandler ajaxAwareAuthenticationSuccessHandler;
    @Autowired
    private AjaxAwareAuthenticationFailureHandler ajaxAwareAuthenticationFailureHandler;
    @Autowired
    private AjaxAuthenticationProvider ajaxAuthenticationProvider;
    
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, ajaxAwareAuthenticationSuccessHandler,
        		ajaxAwareAuthenticationFailureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
    
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(new ArrayList<>(), TOKEN_BASED_AUTH_ENTRY_POINT);
        JwtTokenAuthenticationProcessingFilter filter =
                new JwtTokenAuthenticationProcessingFilter(ajaxAwareAuthenticationFailureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // We don't need CSRF for JWT based authentication
            
            .exceptionHandling()
            .authenticationEntryPoint(this.authenticationEntryPoint)

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
            .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()
            .antMatchers(FORM_BASED_REGISTER_ENTRY_POINT).permitAll()
            .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()

            .and()
            .authorizeRequests()
            .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).hasAnyAuthority(UserRole.COMMON.authority())

            .and()
            .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

}
