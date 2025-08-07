/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.utils.security;

import fina.skroflin.model.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173"));
        config.addAllowedHeader("*");
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        //  .requestMatchers("/api/fina/skroflin/user/register", "/api/fina/skroflin/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/fina/skroflin/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/fina/skroflin/user/login").permitAll()
                        //  .requestMatchers(HttpMethod.GET,"/api/fina/skroflin/user/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/fina/skroflin/user").hasAnyAuthority(Role.superuser.name(), Role.coach.name(), Role.user.name())
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/user/get").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/user/getById").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.POST, "/api/fina/skroflin/user/post").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.PUT, "/api/fina/skroflin/user/put").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/fina/skroflin/user/delete").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/user/getMyProfile").hasAnyAuthority(Role.user.name(), Role.coach.name())
                        .requestMatchers(HttpMethod.PUT, "/api/fina/skroflin/user/updateMyProfile").hasAnyAuthority(Role.user.name(), Role.coach.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/fina/skroflin/user/deleteMyProfile").hasAnyAuthority(Role.superuser.name(), Role.coach.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/fina/skroflin/user/changePassword").hasAnyAuthority(Role.superuser.name(), Role.coach.name())
                        //  .requestMatchers(HttpMethod.GET,"/api/fina/skroflin/trainingSession/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/trainingSession/get").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/trainingSession/getById").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.POST, "/api/fina/skroflin/trainingSession/post").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.PUT, "/api/fina/skroflin/trainingSession/put").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/fina/skroflin/trainingSession/delete").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/trainingSession/getMyTrainingSessions").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.POST, "/api/fina/skroflin/trainingSession/createMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.PUT, "/api/fina/skroflin/trainingSession/updateMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.POST, "/api/fina/skroflin/trainingSession/createMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.PUT, "/api/fina/skroflin/trainingSession/updateMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/fina/skroflin/trainingSession/deleteMyTrainingSession").hasAnyAuthority(Role.coach.name())
                        //  .requestMatchers(HttpMethod.GET,"/api/fina/skroflin/booking/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/booking/get").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/booking/getById").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.POST, "/api/fina/skroflin/booking/post").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.PUT, "/api/fina/skroflin/booking/put").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/fina/skroflin/booking/delete").hasAnyAuthority(Role.superuser.name())
                        .requestMatchers(HttpMethod.GET, "/api/fina/skroflin/booking/getMyBookings").hasAnyAuthority(Role.user.name())
                        .requestMatchers(HttpMethod.POST, "/api/fina/skroflin/booking/createMyBookings").hasAnyAuthority(Role.user.name())
                        .requestMatchers(HttpMethod.PUT, "/api/fina/skroflin/booking/updateMyBooking").hasAnyAuthority(Role.user.name())
                        .requestMatchers(HttpMethod.PUT, "/api/fina/skroflin/booking/deleteMyBookings").hasAnyAuthority(Role.user.name())
                        .requestMatchers("/api/fina/skroflin/user").hasAuthority(Role.superuser.toString()).anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }
}
