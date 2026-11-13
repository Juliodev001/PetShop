package arquitetura.demo.auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class RevogacaoService {

    private static final String PREFIXO = "token-revogado:";

    private final StringRedisTemplate redis;

    public RevogacaoService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void revogar(Jwt jwt) {
        Duration restante = Duration.between(Instant.now(), jwt.getExpiresAt());
        if (!restante.isNegative() && !restante.isZero()) {
            redis.opsForValue().set(PREFIXO + jwt.getId(), "1", restante);
        }
    }

    public boolean estaRevogado(String jti) {
        return jti != null && Boolean.TRUE.equals(redis.hasKey(PREFIXO + jti));
    }
}