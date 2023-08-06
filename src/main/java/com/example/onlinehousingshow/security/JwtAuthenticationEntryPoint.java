package com.example.onlinehousingshow.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Set the response status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Set the content type of the response to JSON
        response.setContentType("application/json");

        // Create a JSON response body with an error message
        String jsonResponse = "{\"message\":\"Unauthorized access. Please login to access this resource.\"}";

        // Write the JSON response to the client
        PrintWriter writer = response.getWriter();
        writer.print(jsonResponse);
        writer.flush();
    }
}
