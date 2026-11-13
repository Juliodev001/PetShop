package arquitetura.demo.auth;

import arquitetura.demo.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final Duration validade;

    public TokenService(JwtEncoder encoder,
                        @Value("${petshop.jwt.validade-minutos}") long validadeMinutos) {
        this.encoder = encoder;
        this.validade = Duration.ofMinutes(validadeMinutos);
    }

    public String gerar(Usuario usuario) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("petshop-api")
                .subject(usuario.id().toString())
                .issuedAt(agora)
                .expiresAt(agora.plus(validade))
                .id(UUID.randomUUID().toString())
                .claim("email", usuario.email())
                .claim("roles", List.of(usuario.perfil().name()))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public Duration validade() {
        return validade;
    }
}