package arquitetura.demo.auth;

import arquitetura.demo.usuario.Perfil;
import arquitetura.demo.usuario.Usuario;
import arquitetura.demo.usuario.UsuarioRepository;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.UUID;

@Service
public class AuthService {

    private final CassandraOperations cassandra;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(CassandraOperations cassandra, UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.cassandra = cassandra;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public UsuarioResponse cadastrar(CadastroRequest pedido) {
        String email = normalizar(pedido.email());
        String senhaHash = passwordEncoder.encode(pedido.senha());

        Usuario usuario = new Usuario(email, UUID.randomUUID(),
                pedido.nome().trim(), senhaHash, Perfil.CLIENTE);

        boolean criado = cassandra.insert(usuario,
                InsertOptions.builder().withIfNotExists().build()).wasApplied();

        if (!criado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        return UsuarioResponse.de(usuario);
    }

    public TokenResponse login(LoginRequest pedido) {
        Usuario usuario = usuarioRepository.findById(normalizar(pedido.email()))
                .filter(u -> passwordEncoder.matches(pedido.senha(), u.senhaHash()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos"));

        return new TokenResponse(tokenService.gerar(usuario), "Bearer",
                tokenService.validade().toSeconds());
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}