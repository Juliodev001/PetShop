package arquitetura.demo.pet;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meus-pets")
public class PetController {

    private final PetRepository repository;

    public PetController(PetRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet cadastrar(@AuthenticationPrincipal Jwt jwt,
                         @RequestBody @Valid NovoPetRequest pedido) {
        UUID tutorId = tutorDoToken(jwt);
        Pet pet = new Pet(tutorId, UUID.randomUUID(),
                pedido.nome(), pedido.especie(), pedido.raca(), pedido.nascimento());
        return repository.save(pet);
    }

    @GetMapping
    public List<Pet> listar(@AuthenticationPrincipal Jwt jwt) {
        return repository.findByTutorId(tutorDoToken(jwt));
    }

    private UUID tutorDoToken(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}