package br.ind.ajrorato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RoratusAnexoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoratusAnexoApplication.class, args);
    }
}
