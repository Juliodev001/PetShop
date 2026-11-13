package arquitetura.demo.auth;

public record TokenResponse(String token, String tipo, long expiraEmSegundos) {}