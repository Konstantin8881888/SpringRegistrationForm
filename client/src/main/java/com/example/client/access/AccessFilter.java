package com.example.client.access;

import com.example.client.bean.CurrentUser;
import com.example.client.provider.JwtSettingsProvider;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Order(1)
@Component
@Slf4j
@AllArgsConstructor
public class AccessFilter implements Filter {

    private final JwtSettingsProvider jwtSettingsProvider;
    private final CurrentUserProvider currentUserProvider;
    private final Gson gson;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Cookie[] cookies = httpServletRequest.getCookies();
        CurrentUser currentUser = null;
        String authToken = null;
        if (cookies != null)
        {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(jwtSettingsProvider.getCookieAuthTokenName()))
                {
                    authToken = cookie.getName();
                }
            }
        }
        if (authToken != null && !authToken.isEmpty())
        {
            currentUser = fetchRemoteUser(httpServletRequest);
        }
    }

    private CurrentUser fetchRemoteUser(HttpServletRequest httpServletRequest)
    {
        try
        {
            URL url = new URL("http://localhost:8088/api/v1/auth/current");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(1000);
            connection.setRequestProperty("Cookie", httpServletRequest.getHeader("Cookie"));
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return gson.fromJson(content.toString(), CurrentUser.class);
        }
        catch (IOException e)
        {
            log.error(e.getLocalizedMessage());
        }
        return new CurrentUser();
    }
}
