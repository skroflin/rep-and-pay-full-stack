/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.utils.stripe;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author skroflin
 */

@Configuration
public class StripeConfig {
    @Value("${stripe.secret-key}")
    private String secretKey;
   
    @Value("${stripe.webhook-secret}")
    private String endpointSecret;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Bean
    public String stripeWebhookSecret() {
        return endpointSecret;
    }
    
    
}
