package br.com.meta.apivotoscooperativa.dto.saida;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosRetornaSessaoEspecifica {

    private Long idPauta;
    private Long idSessao;
    private String pauta;
    private Integer numeroVotosSim;
    private Integer numeroVotosNao;
    private LocalDateTime horaFim;
    private Long duracao;

}
