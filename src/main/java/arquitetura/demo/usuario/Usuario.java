
package arquitetura.demo.usuario;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("usuarios")
public record Usuario(
        @PrimaryKey String email,
        UUID id,
        String nome,
        String senhaHash,
        Perfil perfil
) {}