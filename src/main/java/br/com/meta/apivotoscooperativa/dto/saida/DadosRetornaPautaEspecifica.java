package br.com.meta.apivotoscooperativa.dto.saida;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DadosRetornaPautaEspecifica {

    private Long id;
    private String titulo;
    private String descricao;

}
