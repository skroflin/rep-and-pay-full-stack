/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos;

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
    devServer.setDescription("Razvoj (DEV)");

     Contact contact = new Contact();
    contact.setEmail("kolokvij@ffos.hr");
    contact.setName("2. kolokvij");
    contact.setUrl("https://www.ffos.unios.hr");

    License mitLicense = new License().name("Edukacijska licenca").url("https://unevoc.unesco.org/home/Open+Licensing+of+Educational+Resources");

    Info info = new Info()
        .title("IT@FFOS Programiranje 2 Ispitni rok")
        .version("1.0")
        .contact(contact)
        .description("Ovo je predložak za 2. kolokvij iz kolegija Programiranje 2 u sklopu Dvopredmetnog diplomskog studija Informacijske tehnologije koji se izvodi na Odsjeku za informacijske znanosti pri Filozofskom fakultetu u Osijeku")
            .termsOfService("https://sokrat.ffos.hr/ff-info/kolegiji.php?action=show&id=1900")
        .license(mitLicense);
    
    return new OpenAPI().info(info).servers(List.of(devServer));
  }
}