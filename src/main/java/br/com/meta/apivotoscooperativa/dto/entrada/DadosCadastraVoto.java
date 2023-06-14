package br.com.meta.apivotoscooperativa.dto.entrada;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosCadastraVoto {

    @NotNull
    private Long idPauta;
    @NotNull
    private Long idAssociado;
    @NotBlank
    private String voto;

}
