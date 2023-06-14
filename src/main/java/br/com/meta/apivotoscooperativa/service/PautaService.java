package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraPauta;
import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraVoto;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPauta;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPautaEspecifica;
import br.com.meta.apivotoscooperativa.exception.VotoDuplicadoException;
import br.com.meta.apivotoscooperativa.exception.VotoInvalidoException;
import br.com.meta.apivotoscooperativa.model.Associado;
import br.com.meta.apivotoscooperativa.model.Pauta;
import br.com.meta.apivotoscooperativa.model.Sessao;
import br.com.meta.apivotoscooperativa.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PautaService {

    @Autowired
    private PautaRepository repository;

    public DadosRetornaPauta cadastra(DadosCadastraPauta dados) {
        //Salvando pauta no banco
        var pauta = new Pauta();
        pauta.setTitulo(dados.getTitulo());
        pauta.setDescricao(dados.getDescricao());
        repository.save(pauta);

        //Criando e retornando DadosRetornaPauta
        return new DadosRetornaPauta(pauta.getId());
    }

    public DadosRetornaPautaEspecifica consulta(Long id) {
        var pauta = repository.getReferenceById(id);
        var dadosRetornaPautaEspecifica = new DadosRetornaPautaEspecifica(pauta.getId(), pauta.getTitulo(), pauta.getDescricao());
        return dadosRetornaPautaEspecifica;
    }

    public void registraVoto(DadosCadastraVoto dados) {
        //pegar associado no banco de dados pelo id do dto
        var associado = new Associado();//sessaoRepository.findById(dados.getIdAssociado());
        //pegar sessao no banco de dados pelo idPauta do dto e a lista de associados dela
        var sessao = new Sessao();//repository.findSessaoByIdPauta(dados.getIdPauta());
        List<Associado> associados = sessao.getAssociadosQueVotaram();
        //checar se o associado na lista já votou
        if (associados.contains(associado)) {
            throw new VotoDuplicadoException("Este associado já votou.");
        } else {
            //adicionar associado na lista de associados da sessao
            associados.add(associado);
        }
        //adicionar lista de associados à sessao
        sessao.setAssociadosQueVotaram(associados);
        //contabilizar voto
        if (dados.getVoto() == "Sim") {
            Integer votos = sessao.getNumeroVotosSim();
            votos+=1;
            sessao.setNumeroVotosSim(votos);
        } else if (dados.getVoto() == "Nao") {
            Integer votos = sessao.getNumeroVotosNao();
            votos+=1;
            sessao.setNumeroVotosNao(votos);
        } else {
            throw new VotoInvalidoException("Voto inválido, utilize apenas Sim ou Nao");
        }
        //salvar sessao no banco de dados
        //sessaoRepository.save(sessao);
    }
}
