/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.utils.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 *
 * @author skroflin
 */
@Component
public class JwtAuthenticationEntryPoint 
        implements AuthenticationEntryPoint{
    
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        System.out.println("Unauthorized access:" 
                + " " + exception.getMessage());
        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED, 
                "Unauthorized:" + " " 
                        + exception.getMessage()
        );
    }
    
}
