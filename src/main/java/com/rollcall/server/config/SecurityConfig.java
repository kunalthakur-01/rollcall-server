package com.rollcall.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rollcall.server.enums.Role;
import com.rollcall.server.security.JwtAuthenticationEntryPoint;
import com.rollcall.server.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable())
                // .cors(cors -> cors.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("api/user/all/coordinators").permitAll()
                        .requestMatchers("api/user/all/attendees").permitAll()
                        .requestMatchers("api/user/group/all").permitAll()
                        .requestMatchers("api/user/notification/all/**").permitAll()
                        // .requestMatchers("api/user/attendee/signup").permitAll()
                        .requestMatchers("api/user/refresh/jwt").permitAll()
                        .requestMatchers("api/user/auth/**").permitAll()
                        .requestMatchers("api/user/group/new/**").hasAuthority(Role.TEACHER.name())
                        .requestMatchers("api/user/add/group/**").hasAuthority(Role.TEACHER.name())
                        // .requestMatchers("api/user/group/new/**").hasRole(Role.TEACHER.name())  --> do "ROLE_" + role.name() in getAuthorities in User model
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(this.daoAuthenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // @Bean
    // public UserDetailsService userDetailsService() {

    //     UserDetails normalUsers = User
    //             .withUsername("kunal")
    //             .password(passwordEncoder().encode("121212"))
    //             .build();

    //     UserDetails adminUsers = User
    //             .withUsername("kunal1")
    //             .password(passwordEncoder().encode("121212"))
    //             .roles("NORMAL")
    //             .build();

    //     return new InMemoryUserDetailsManager(normalUsers, adminUsers);
    // }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    //     httpSecurity.csrf(csrf -> csrf.disable())
    //             .authorizeHttpRequests(requests -> requests
    //                     .requestMatchers("/api/user/all/coordinators")
    //                     .hasRole("ADMIN")
    //                     .requestMatchers("/api/user/all/attendees")
    //                     .hasRole("NORMAL")
    //                     .requestMatchers("/api/user/group/all")
    //                     .permitAll()
    //                     .anyRequest()
    //                     .authenticated())
    //             .formLogin(withDefaults());

    //     return httpSecurity.build();
    //     // return null;
    // }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
