package arquitetura.demo.pet;


import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("pets_por_tutor")
public record Pet(
        @PrimaryKeyColumn(name = "tutor_id", type = PrimaryKeyType.PARTITIONED)
        UUID tutorId,

        @PrimaryKeyColumn(name = "pet_id", type = PrimaryKeyType.CLUSTERED)
        UUID petId,

        String nome,
        String especie,
        String raca,
        LocalDate nascimento
) {}