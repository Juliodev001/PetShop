package arquitetura.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SenhaConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        // Usa BCrypt por padrão e grava o prefixo {bcrypt} no hash,
        // o que permite migrar para Argon2 no futuro sem trocar senhas.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}