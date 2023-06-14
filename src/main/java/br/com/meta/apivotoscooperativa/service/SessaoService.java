package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosIniciaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessao;
import br.com.meta.apivotoscooperativa.exceptions.PautaInexistente;
import br.com.meta.apivotoscooperativa.exceptions.PautaJaExistente;
import br.com.meta.apivotoscooperativa.model.Sessao;
import br.com.meta.apivotoscooperativa.repository.PautaRepository;
import br.com.meta.apivotoscooperativa.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository repository;

    @Autowired
    private PautaRepository pautaRepository;


    public DadosRetornaSessao iniciaSessao(DadosIniciaSessao dados) {
        //abrindo uma sessao com zero votos
        var sessao = new Sessao();
        sessao.setNumeroVotosNao(0);
        sessao.setNumeroVotosSim(0);

        //procurando se existe algum Idpauta igual ja existente
        if(repository.existsByPautaId(dados.getIdPauta())){
            throw new PautaJaExistente();
        }

        if (!pautaRepository.existsById(dados.getIdPauta())) {
            throw new PautaInexistente();
        } else {
            //bucando pauta no banco de dados
            var pauta = pautaRepository.findPautaById(dados.getIdPauta());
            //salvando pauta dentro da sessao
            sessao.setPauta(pauta);
        }

        if (dados.getDuracao() == null) {
            //duracao default
            sessao.setDuracao(Long.valueOf(1));
        } else {
            //buscando duracao em minutos
            Long duracao = dados.getDuracao();
            sessao.setDuracao(duracao);
        }
        //criando data final
        var dataFinal = LocalDateTime.now().plusMinutes(sessao.getDuracao());
        sessao.setHoraFim(dataFinal);

        repository.save(sessao);

        //atualzando tabela pautas com id da Sessao
        var pauta = pautaRepository.findPautaById(sessao.getPauta().getId());
        pauta.setSessao(sessao);

        pautaRepository.save(pauta);


        return new DadosRetornaSessao(sessao.getId());
    }


}
