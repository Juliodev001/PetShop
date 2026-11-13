package arquitetura.demo.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final RevogacaoService revogacao;

    public AuthController(AuthService service, RevogacaoService revogacao) {
        this.service = service;
        this.revogacao = revogacao;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@RequestBody @Valid CadastroRequest pedido) {
        return service.cadastrar(pedido);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest pedido) {
        return service.login(pedido);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@AuthenticationPrincipal Jwt jwt) {
        revogacao.revogar(jwt);
    }
}