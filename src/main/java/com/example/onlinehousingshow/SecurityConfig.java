package com.example.onlinehousingshow;

import com.example.onlinehousingshow.security.JwtTokenProvider;
import com.example.onlinehousingshow.service.JpaUserDetailsService;
import com.example.onlinehousingshow.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OwnerService ownerService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/housing/**").permitAll();

        // Configuring security for private APIs (owner-specific endpoints)
        http.antMatcher("/api/housings/owner/**")
                .authorizeRequests()
                .antMatchers("/api/housings/owner").authenticated()
                .and()
                .httpBasic(); // Use HTTP Basic Authentication for owner-specific endpoints

        // Configuring security for public APIs (open endpoints)
        http.antMatcher("/api/housings/public/**")
                .authorizeRequests()
                .antMatchers("/api/housings/public").permitAll() // Allow access to all public endpoints
                .and()
                .csrf().disable(); // Disable CSRF (Cross-Site Request Forgery) protection
    }



    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
