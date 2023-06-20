package br.com.meta.apivotoscooperativa.dto.entrada;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
