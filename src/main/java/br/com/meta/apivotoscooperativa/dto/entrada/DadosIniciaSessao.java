package br.com.meta.apivotoscooperativa.dto.entrada;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record DadosIniciaSessao(@NotNull Long idPauta, Long duracao) {

}

