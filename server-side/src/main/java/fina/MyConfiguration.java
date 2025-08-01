/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {
    
    @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl("http://localhost:8080");
    devServer.setDescription("Development (DEV)");

    License mitLicense = new License().name("Edukacijska licenca").url("https://unevoc.unesco.org/home/Open+Licensing+of+Educational+Resources");

    Info info = new Info()
        .title("R&P REST API")
        .version("1.0")
        .description("Simple REST API made with Spring Boot, Hibernate and MariaDB.")
        .license(mitLicense);
    
    return new OpenAPI().info(info).servers(List.of(devServer));
  }
}