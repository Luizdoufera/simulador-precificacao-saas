package br.senai.chavecerta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação. Sobe o servidor web em http://localhost:8080
 * e serve os arquivos de src/main/resources/static/.
 */
@SpringBootApplication
public class ChaveCertaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaveCertaApplication.class, args);
    }
}
