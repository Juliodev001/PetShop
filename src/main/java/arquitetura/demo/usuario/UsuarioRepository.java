package arquitetura.demo.usuario;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UsuarioRepository extends CassandraRepository<Usuario, String> {
}