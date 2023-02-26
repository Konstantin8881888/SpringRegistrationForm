package com.example.auth.access;

import lombok.AllArgsConstructor;
import org.apache.catalina.filters.RequestFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtTokenConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>
{
    private final JwtTokenFilter jwtTokenFilter;

    @Override
    public void configure(HttpSecurity builder) throws Exception
    {
        builder.addFilterAfter(jwtTokenFilter, ExceptionTranslationFilter.class);
    }
}
