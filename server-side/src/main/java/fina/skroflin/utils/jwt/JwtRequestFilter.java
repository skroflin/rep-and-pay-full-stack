/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.utils.jwt;

import fina.skroflin.service.user.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author skroflin
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
    private final UserDetailService detailService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(UserDetailService detailService, JwtTokenUtil jwtTokenUtil) {
        this.detailService = detailService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authorizationHeader = 
                request.getHeader("Authorization");
        String username = null;
        String jwt = null;
    }
    
    
}
