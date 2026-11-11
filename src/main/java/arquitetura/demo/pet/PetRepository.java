package arquitetura.demo.pet;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;
    public interface PetRepository extends CassandraRepository<Pet, MapId> {

        List<Pet> findByTutorId(UUID tutorId);
}
