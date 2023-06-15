package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraVoto;
import br.com.meta.apivotoscooperativa.dto.entrada.DadosIniciaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessao;
import br.com.meta.apivotoscooperativa.exception.*;
import br.com.meta.apivotoscooperativa.model.Associado;
import br.com.meta.apivotoscooperativa.model.Sessao;
import br.com.meta.apivotoscooperativa.repository.AssociadoRepository;
import br.com.meta.apivotoscooperativa.repository.PautaRepository;
import br.com.meta.apivotoscooperativa.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository repository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    public DadosRetornaSessao iniciaSessao(DadosIniciaSessao dados) {
        //abrindo uma sessao com zero votos
        var sessao = new Sessao();
        sessao.setNumeroVotosNao(0);
        sessao.setNumeroVotosSim(0);

        //procurando se existe algum Idpauta igual ja existente
        if(repository.existsByPautaId(dados.getIdPauta())){
            throw new br.com.meta.apivotoscooperativa.exception.PautaJaExistenteException();
        }

        if (!pautaRepository.existsById(dados.getIdPauta())) {
            throw new br.com.meta.apivotoscooperativa.exception.PautaInexistenteException();
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

    public void registraVoto(DadosCadastraVoto dados) {
        //pegar associado no banco de dados pelo id do dto
        if(!associadoRepository.existsById(dados.getIdAssociado())){
            throw new AssociadoInexistenteException();
        }
        var associado = associadoRepository.findAssociadoById(dados.getIdAssociado());

        //pegar sessao no banco de dados pelo idPauta do dto
        if (!pautaRepository.existsById(dados.getIdPauta())){
            throw new PautaInexistenteException();
        }
        Sessao sessao = repository.findSessaoByPautaId(dados.getIdPauta());

        //validando se sessão está aberta
        if(LocalDateTime.now().isAfter(sessao.getHoraFim())){
            throw new VotoEmSessaoFechadaException();
        }

        //pegar lista de associados da sessão
        List<Associado> associados = sessao.getAssociados();

        //checar se o associado que está votando já votou
        if (associados.contains(associado)) {
            throw new VotoDuplicadoException();
        } else {
            //adicionar associado na lista de associados
            associados.add(associado);
        }

        //adicionar lista atualizada de associados à sessao
        sessao.setAssociados(associados);

        //contabilizar voto na sessão
        if (dados.getVoto().equals("Sim")) {
            Integer votos = sessao.getNumeroVotosSim();
            votos+=1;
            sessao.setNumeroVotosSim(votos);
        } else if (dados.getVoto().equals("Não")) {
            Integer votos = sessao.getNumeroVotosNao();
            votos+=1;
            sessao.setNumeroVotosNao(votos);
        } else {
            throw new VotoInvalidoException("Voto inválido, utilize apenas Sim ou Nao");
        }

        //salvar sessao no banco de dados
        repository.save(sessao);
    }
}
