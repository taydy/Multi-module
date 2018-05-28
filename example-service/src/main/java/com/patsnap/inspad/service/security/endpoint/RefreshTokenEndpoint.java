package com.patsnap.inspad.service.security.endpoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.patsnap.inspad.core.dto.UserInfo;
import com.patsnap.inspad.core.dto.UserRole;
import com.patsnap.inspad.core.service.user.UserService;
import com.patsnap.inspad.service.security.auth.jwt.extractor.TokenExtractor;
import com.patsnap.inspad.service.security.auth.jwt.verifier.TokenVerifier;
import com.patsnap.inspad.service.security.auth.model.JwtToken;
import com.patsnap.inspad.service.security.auth.model.JwtTokenFactory;
import com.patsnap.inspad.service.security.auth.model.RawAccessJwtToken;
import com.patsnap.inspad.service.security.auth.model.RefreshToken;
import com.patsnap.inspad.service.security.config.JwtSettings;
import com.patsnap.inspad.service.security.config.WebSecurityConfig;
import com.patsnap.inspad.service.security.exceptions.InvalidJwtToken;


/**
 * RefreshTokenEndpoint
 *
 */
@RestController
public class RefreshTokenEndpoint {
    @Autowired
    private JwtTokenFactory tokenFactory;
    @Autowired
    private JwtSettings jwtSettings;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenVerifier tokenVerifier;
    @Autowired
    @Qualifier("jwtHeaderTokenExtractor")
    private TokenExtractor tokenExtractor;

    @RequestMapping(value = "/api/auth/token", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String username = rawToken.parseClaims(jwtSettings.getTokenSigningKey()).getBody().get("user", String.class);
        UserInfo userInfo = userService.getByUsername(username);

        List<UserRole> userRoles = Arrays.asList(UserRole.COMMON);
        List<GrantedAuthority> authorities = userRoles.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.authority()))
                .collect(Collectors.toList());

        UserInfo jwtInfo = UserInfo.of(userInfo.getId(), userInfo.getUsername(), authorities);

        return tokenFactory.createAccessJwtToken(jwtInfo);
    }
}
