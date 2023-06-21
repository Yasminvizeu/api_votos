package br.com.meta.apivotoscooperativa.dto.entrada;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record DadosCadastraVoto(@NotNull Long idPauta, @NotNull Long idAssociado, @NotBlank String voto) {
}

