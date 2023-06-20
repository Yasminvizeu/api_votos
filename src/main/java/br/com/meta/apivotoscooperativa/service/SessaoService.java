package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraVoto;
import br.com.meta.apivotoscooperativa.dto.entrada.DadosIniciaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessaoEspecifica;
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

        var sessao = new Sessao();
        sessao.setNumeroVotosNao(0);
        sessao.setNumeroVotosSim(0);

        if(repository.existsByPautaId(dados.getIdPauta())){
            throw new br.com.meta.apivotoscooperativa.exception.PautaJaExistenteException();
        }

        if (!pautaRepository.existsById(dados.getIdPauta())) {
            throw new br.com.meta.apivotoscooperativa.exception.PautaInexistenteException();
        } else {

            var pauta = pautaRepository.findPautaById(dados.getIdPauta());

            sessao.setPauta(pauta);
        }

        sessao.setDuracao(dados.getDuracao() == null || dados.getDuracao() < 1 ?
                Long.valueOf(1) : dados.getDuracao());

        var dataFinal = LocalDateTime.now().plusMinutes(sessao.getDuracao());
        sessao.setHoraFim(dataFinal);

        repository.save(sessao);

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
        if (dados.getVoto().equals("Sim") || dados.getVoto().equals("sim") || dados.getVoto().equals("SIM")) {
            Integer votos = sessao.getNumeroVotosSim();
            votos+=1;
            sessao.setNumeroVotosSim(votos);
        } else if (dados.getVoto().equals("Não") || dados.getVoto().equals("não") || dados.getVoto().equals("NÃO")
        || dados.getVoto().equals("Nao") || dados.getVoto().equals("nao") || dados.getVoto().equals("NAO")) {
            Integer votos = sessao.getNumeroVotosNao();
            votos+=1;
            sessao.setNumeroVotosNao(votos);
        } else {
            throw new VotoInvalidoException("Voto inválido, utilize apenas Sim ou Não");
        }

        //salvar sessao no banco de dados
        repository.save(sessao);
    }

    public DadosRetornaSessaoEspecifica consulta(Long id) {
        //validando se a sessao existe no banco de dados
        if(!repository.existsById(id)){
            throw new SessaoInexistenteException();
        }
        //pegando a sessao no banco de dados pelo id
        var sessao = repository.getReferenceById(id);
        //traduzindo a sessao para o dto de saida
        var dados = new DadosRetornaSessaoEspecifica();
        dados.setIdPauta(sessao.getPauta().getId());
        dados.setIdSessao(sessao.getId());
        dados.setNumeroVotosSim(sessao.getNumeroVotosSim());
        dados.setNumeroVotosNao(sessao.getNumeroVotosNao());
        dados.setHoraFim(sessao.getHoraFim());
        dados.setDuracao(sessao.getDuracao());
        dados.setPauta(sessao.getPauta().getTitulo());

        //retornando a sessao
        return dados;
    }

    public DadosRetornaSessaoEspecifica consultaSessaoPorIdPauta(Long id) {
        //validando se a pauta existe no banco de dados
        if(!pautaRepository.existsById(id)){
            throw new PautaInexistenteException();
        }

        //validando se a pauta foi votada
        if (pautaRepository.findPautaById(id).getSessao() == null){
            throw new PautaNaoVotadaException();
        }
        //pegando a sessao no banco de dados pelo id
        var sessao = repository.findSessaoByPautaId(id);
        //traduzindo a sessao para o dto de saida
        var dados = new DadosRetornaSessaoEspecifica();
        dados.setIdPauta(sessao.getPauta().getId());
        dados.setIdSessao(sessao.getId());
        dados.setNumeroVotosSim(sessao.getNumeroVotosSim());
        dados.setNumeroVotosNao(sessao.getNumeroVotosNao());
        dados.setHoraFim(sessao.getHoraFim());
        dados.setDuracao(sessao.getDuracao());
        dados.setPauta(sessao.getPauta().getTitulo());

        //retornando a sessao
        return dados;
    }
}
