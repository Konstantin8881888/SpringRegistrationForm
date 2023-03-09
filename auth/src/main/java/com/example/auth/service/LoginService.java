package com.example.auth.service;

import com.example.auth.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService
{
    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService appUserDetailsService;

    public AppUser login(String email, String password)
    {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return appUserDetailsService.retrieveFromCache(email);
    }
}
