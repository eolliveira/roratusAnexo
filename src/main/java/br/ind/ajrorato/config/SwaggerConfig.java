package br.ind.ajrorato.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${url.service.anexoftp}")
    private String urlServidorFtp;

    @Bean
    public OpenAPI defineOpenApi() {
        Server serverProd = new Server();
        serverProd.setUrl(urlServidorFtp);

        serverProd.setDescription("Servidor de Produção");

        Contact myContact = new Contact();
        myContact.setName("Equipe de Suporte");
        myContact.setEmail("suporte@ajrorato.ind.br");

        Info information = new Info()
                .title("Roratus Anexo")
                .version("1.0")
                .description("API para manipulação de anexos no servidor FTP")
                .contact(myContact);

        return new OpenAPI().info(information).servers(List.of(serverProd));
    }
}