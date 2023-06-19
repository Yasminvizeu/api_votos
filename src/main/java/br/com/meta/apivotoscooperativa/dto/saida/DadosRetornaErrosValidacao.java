package br.com.meta.apivotoscooperativa.dto.saida;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DadosRetornaErrosValidacao {

    private String campo;
    private String mensagem;

}
