package br.com.meta.apivotoscooperativa.dto.entrada;

import br.com.meta.apivotoscooperativa.model.Pauta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosIniciaSessao {

    @NotNull
    private Long idPauta;

    private Long duracao;

}

