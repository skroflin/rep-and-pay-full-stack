/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.utils.security;

import fina.skroflin.model.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author skroflin
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/skroflin/fina/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/skroflin/fina/user/registraion").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/skroflin/fina/user/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/user/get").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/user/getById").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.POST, "/api/skroflin/fina/user/post").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.PUT, "/api/skroflin/fina/user/put").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/skroflin/fina/user/delete").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/user/getMyProfile").hasAnyAuthority(Role.user.name(), Role.coach.name())
                        .requestMatchers(HttpMethod.PUT, "/api/skroflin/fina/user/updateMyProfile").hasAnyAuthority(Role.user.name(), Role.coach.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/skroflin/fina/user/deleteMyProfile").hasAnyAuthority(Role.superuser.name(), Role.coach.name())
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/trainingSession/get").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/trainingSession/getById").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.POST, "/api/skroflin/fina/trainingSession/post").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.PUT, "/api/skroflin/fina/trainingSession/put").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/skroflin/fina/trainingSession/delete").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/trainingSession/getMyTrainingSessions").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.POST, "/api/skroflin/fina/trainingSession/createMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.PUT, "/api/skroflin/fina/trainingSession/updateMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.POST, "/api/skroflin/fina/trainingSession/createMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.PUT, "/api/skroflin/fina/trainingSession/updateMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/skroflin/fina/trainingSession/deleteMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/booking/get").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/booking/getById").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.POST, "/api/skroflin/fina/booking/post").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.PUT, "/api/skroflin/fina/booking/put").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/skroflin/fina/booking/delete").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/skroflin/fina/booking/getMyBookings").hasAnyAuthority(Role.user.name())
                        .requestMatchers(HttpMethod.POST, "/api/skroflin/fina/booking/createMyBookings").hasAnyAuthority(Role.user.name())
                        .requestMatchers(HttpMethod.PUT, "/api/skroflin/fina/booking/updateMyBooking").hasAnyAuthority(Role.user.name())
                        .requestMatchers(HttpMethod.PUT, "/api/skroflin/fina/booking/deleteMyBookings").hasAnyAuthority(Role.user.name())
                        .requestMatchers("/api/skroflin/fina/user").hasAuthority(Role.superuser.toString()).anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }
}
