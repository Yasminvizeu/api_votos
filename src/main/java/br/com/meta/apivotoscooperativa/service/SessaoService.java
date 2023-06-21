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

import java.text.Normalizer;
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

        if (repository.existsByPautaId(dados.idPauta())) {
            throw new br.com.meta.apivotoscooperativa.exception.PautaJaExistenteException();
        }

        if (!pautaRepository.existsById(dados.idPauta())) {
            throw new br.com.meta.apivotoscooperativa.exception.PautaInexistenteException();
        } else {

            var pauta = pautaRepository.findPautaById(dados.idPauta());

            sessao.setPauta(pauta);
        }

        sessao.setDuracao(dados.duracao() == null || dados.duracao() < 1 ?
                Long.valueOf(1) : dados.duracao());

        var dataFinal = LocalDateTime.now().plusMinutes(sessao.getDuracao());
        sessao.setHoraFim(dataFinal);

        repository.save(sessao);

        var pauta = pautaRepository.findPautaById(sessao.getPauta().getId());
        pauta.setSessao(sessao);

        pautaRepository.save(pauta);

        return new DadosRetornaSessao(sessao.getId());
    }

    public void registraVoto(DadosCadastraVoto dados) {
        if (!associadoRepository.existsById(dados.idAssociado())) {
            throw new AssociadoInexistenteException();
        }
        var associado = associadoRepository.findAssociadoById(dados.idAssociado());

        if (!pautaRepository.existsById(dados.idPauta())) {
            throw new PautaInexistenteException();
        }
        Sessao sessao = repository.findSessaoByPautaId(dados.idPauta());

        if (LocalDateTime.now().isAfter(sessao.getHoraFim())) {
            throw new VotoEmSessaoFechadaException();
        }
        List<Associado> associados = sessao.getAssociados();

        if (associados.contains(associado)) {
            throw new VotoDuplicadoException();
        } else {
            associados.add(associado);
        }

        sessao.setAssociados(associados);

        contabilizarVoto(sessao, dados.voto());

        repository.save(sessao);
    }

    private void contabilizarVoto(Sessao sessao, String voto) {
        final String VOTO_SIM = "sim";
        final String VOTO_NAO = "nao";

        String votoNormalizado = Normalizer.normalize(voto.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        switch (votoNormalizado)  {
            case VOTO_SIM:
                int votosSim = sessao.getNumeroVotosSim();
                sessao.setNumeroVotosSim(votosSim + 1);
                break;
            case VOTO_NAO:
                int votosNao = sessao.getNumeroVotosNao();
                sessao.setNumeroVotosNao(votosNao + 1);
                break;
            default:
                throw new VotoInvalidoException("Voto inválido, utilize apenas Sim ou Não");
        }
    }

    public DadosRetornaSessaoEspecifica consulta(Long id) {
        //validando se a sessao existe no banco de dados
        if (!repository.existsById(id)) {
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
        if (!pautaRepository.existsById(id)) {
            throw new PautaInexistenteException();
        }

        //validando se a pauta foi votada
        if (pautaRepository.findPautaById(id).getSessao() == null) {
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
