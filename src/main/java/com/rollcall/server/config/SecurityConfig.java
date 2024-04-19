package com.rollcall.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails normalUsers = User
                    .withUsername("kunal")
                    .password(passwordEncoder().encode("password"))
                    .roles("ADMIN")
                    .build();
        
        UserDetails adminUsers = User
                    .withUsername("kunal1")
                    .password(passwordEncoder().encode("password"))
                    .roles("NORMAL")
                    .build();            
        
        return new InMemoryUserDetailsManager(normalUsers, adminUsers);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/user/all/coordinators")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/user/all/attendees")
                        .hasRole("NORMAL")
                        .requestMatchers("/api/user/group/all")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(withDefaults());

        return httpSecurity.build();
        // return null;
    }
}
