package arquitetura.demo.servico;

import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface ServicoRepository extends CassandraRepository<Servico, UUID> {
}