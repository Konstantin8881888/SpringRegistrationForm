package com.example.auth.controller;

import com.example.auth.access.JwtTokenProvider;
import com.example.auth.bean.ErrorResponse;
import com.example.auth.bean.RegistrationRequest;
import com.example.auth.bean.UserResponse;
import com.example.auth.entity.AppUser;
import com.example.auth.service.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@ResponseBody
@RestController
@RequestMapping(path = "/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController
{
    private final RegistrationService registrationService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(path = "registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest registrationRequest, HttpServletResponse response)
    {
        try
        {
            AppUser appUser = registrationService.register(registrationRequest);
            setRefreshToken(appUser, response);
            setAuthToken(appUser, response);
            return buildUserResponse(appUser);
        }
        catch (Exception e)
        {
            log.error(e.getLocalizedMessage());
            clearAuthToken(response);
            return buildErrorResponse(e.getLocalizedMessage());
        }
    }

    private void setAuthToken(@NonNull AppUser appUser, HttpServletResponse httpServletResponse)
    {
        String token = jwtTokenProvider.createAuthToken(appUser.getEmail(), appUser.getRole().name());
        Cookie cookie = new Cookie(jwtTokenProvider.getAuthCookieName(), token);
        cookie.setPath(jwtTokenProvider.getCookiePath());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtTokenProvider.getAuthExpirationCookie());
        httpServletResponse.addCookie(cookie);
    }

    private void setRefreshToken(@NonNull AppUser appUser, HttpServletResponse httpServletResponse)
    {
        String token = jwtTokenProvider.createRefreshToken(appUser.getEmail(), appUser.getRole().name());
        Cookie cookie = new Cookie(jwtTokenProvider.getRefreshCookieName(), token);
        cookie.setPath(jwtTokenProvider.getCookiePath());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtTokenProvider.getRefreshExpirationCookie());
        httpServletResponse.addCookie(cookie);
    }

    private void clearAuthToken(HttpServletResponse httpServletResponse)
    {
        Cookie authCookie = new Cookie(jwtTokenProvider.getAuthCookieName(), "-");
        authCookie.setPath(jwtTokenProvider.getCookiePath());
        Cookie refreshCookie = new Cookie(jwtTokenProvider.getRefreshCookieName(), "-");
        refreshCookie.setPath(jwtTokenProvider.getCookiePath());
        httpServletResponse.addCookie(authCookie);
        httpServletResponse.addCookie(refreshCookie);
    }

    private ResponseEntity<?> buildUserResponse(AppUser appUser)
    {
        return ResponseEntity.ok(new UserResponse(appUser));
    }

    private ResponseEntity<?> buildErrorResponse(String message)
    {
        return ResponseEntity.ok(new ErrorResponse(message));
    }
}
