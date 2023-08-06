package com.example.onlinehousingshow.security;

import com.example.onlinehousingshow.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final String jwtSecret;
    private JwtTokenProvider jwtTokenProvider;


    public JwtTokenFilter(String jwtSecret, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/**"));
        this.jwtSecret = jwtSecret;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = resolveToken(request);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return null;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String ownerUserName = claims.getSubject();

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(ownerUserName, null)
            );
        } catch (JwtException e) {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.replace("Bearer ", "");
        }
        return null;
    }
}
