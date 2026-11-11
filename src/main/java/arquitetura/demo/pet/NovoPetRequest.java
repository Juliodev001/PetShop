package arquitetura.demo.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record NovoPetRequest(
        @NotBlank String nome,
        @NotBlank String especie,
        String raca,
        @Past LocalDate nascimento
) {}