package arquitetura.demo.servico;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Table("servicos")
public record Servico(
        @PrimaryKey UUID id,
        String nome,
        BigDecimal preco,
        int duracaoMinutos
) implements Serializable {}