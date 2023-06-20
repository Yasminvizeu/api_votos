package br.com.meta.apivotoscooperativa.dto.entrada;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosCadastraPauta {

    @NotBlank
    private String titulo;
    @NotBlank
    private String descricao;



}
