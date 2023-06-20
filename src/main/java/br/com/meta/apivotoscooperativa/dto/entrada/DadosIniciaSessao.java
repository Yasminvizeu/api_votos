package br.com.meta.apivotoscooperativa.dto.entrada;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosIniciaSessao {

    @NotNull
    private Long idPauta;

    private Long duracao;

}

