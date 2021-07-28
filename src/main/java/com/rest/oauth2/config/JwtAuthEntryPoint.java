package com.rest.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.oauth2.controller.AuthErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper mapper;

    public JwtEntryPoint(ObjectMapper mapper) {
        //
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        //
        String exceptionMessage = e.getMessage();

        String messageCode = null;
        String exceptionName = e.getClass().getSimpleName();
        if (exceptionMessage.startsWith("Access token expired:")) {
            messageCode = "common.access_token_expired";
        }
        else {
            messageCode = "common.unknown_auth_exception";
        }

        ServletServerHttpResponse res = new ServletServerHttpResponse(response);
        res.setStatusCode(HttpStatus.UNAUTHORIZED);
        res.getServletResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        AuthErrorResponse errorResponse = new AuthErrorResponse(exceptionName, messageCode);
        res.getBody().write(mapper.writeValueAsString(errorResponse).getBytes());
    }

}
