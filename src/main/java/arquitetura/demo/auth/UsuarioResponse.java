package arquitetura.demo.auth;

import arquitetura.demo.usuario.Perfil;
import arquitetura.demo.usuario.Usuario;

import java.util.UUID;

public record UsuarioResponse(UUID id, String nome, String email, Perfil perfil) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(usuario.id(), usuario.nome(), usuario.email(), usuario.perfil());
    }
}