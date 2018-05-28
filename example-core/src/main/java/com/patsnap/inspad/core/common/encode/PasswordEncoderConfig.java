package com.patsnap.inspad.core.common.encode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 加密服务。
 * @author Taydy
 * @version v1.0.0 2017年11月16日
 * @since JDK1.8
 */
@Configuration
public class PasswordEncoderConfig {
    
    /**
     * 加密服务Bean.
     * @return {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
