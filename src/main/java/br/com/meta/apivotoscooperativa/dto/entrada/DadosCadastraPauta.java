package br.com.meta.apivotoscooperativa.dto.entrada;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record DadosCadastraPauta(@NotBlank String titulo, @NotBlank String descricao) {
}

