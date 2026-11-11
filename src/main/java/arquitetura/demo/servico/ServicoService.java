package arquitetura.demo.servico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServicoService {

    private static final Logger log = LoggerFactory.getLogger(ServicoService.class);

    private final ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    @Cacheable("catalogo")
    public List<Servico> listarCatalogo() {
        log.info(">>> Buscando o catálogo no CASSANDRA");
        return repository.findAll();
    }

    @CacheEvict(value = "catalogo", allEntries = true)
    public Servico cadastrar(NovoServicoRequest pedido) {
        Servico servico = new Servico(UUID.randomUUID(),
                pedido.nome(), pedido.preco(), pedido.duracaoMinutos());
        return repository.save(servico);
    }
}