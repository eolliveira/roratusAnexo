package br.ind.ajrorato.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    private final Environment env;

    @Value("${url.service.anexoftp}")
    private String urlServidorFtp;

    @Bean
    public OpenAPI defineOpenApi() {
        Server serverProd = new Server();
        serverProd.setUrl(urlServidorFtp);

        var profileProd = Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.contains("prod"));
        serverProd.setDescription(profileProd ? "Servidor de Produção" : "Servidor de Desenvolvimento");

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