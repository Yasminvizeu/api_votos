package br.com.meta.apivotoscooperativa.dto.saida;

import java.time.LocalDateTime;

public record DadosRetornaSessaoEspecifica(
        Long idPauta,
        Long idSessao,
        String pauta,
        Integer numeroVotosSim,
        Integer numerosVotosNao,
        LocalDateTime horaFim,
        Long duracao) {

}
