package br.ind.ajrorato;

import br.ind.ajrorato.config.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageConfig.class})
public class RoratusAnexoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoratusAnexoApplication.class, args);
    }

}
