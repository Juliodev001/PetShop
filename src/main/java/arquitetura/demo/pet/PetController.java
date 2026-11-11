package arquitetura.demo.pet;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tutores/{tutorId}/pets")
public class PetController {

    private final PetRepository repository;

    public PetController(PetRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet cadastrar(@PathVariable UUID tutorId,
                         @RequestBody @Valid NovoPetRequest pedido) {
        Pet pet = new Pet(tutorId, UUID.randomUUID(),
                pedido.nome(), pedido.especie(), pedido.raca(), pedido.nascimento());
        return repository.save(pet);
    }

    @GetMapping
    public List<Pet> listar(@PathVariable UUID tutorId) {
        return repository.findByTutorId(tutorId);
    }
}