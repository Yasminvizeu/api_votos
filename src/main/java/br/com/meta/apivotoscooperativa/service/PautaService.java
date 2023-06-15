package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraPauta;
import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraVoto;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPauta;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPautaEspecifica;
import br.com.meta.apivotoscooperativa.exception.PautaInexistenteException;
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
        if(!repository.existsById(id)){
            throw new PautaInexistenteException();
        }
        var pauta = repository.getReferenceById(id);
        var dadosRetornaPautaEspecifica = new DadosRetornaPautaEspecifica(pauta.getId(), pauta.getTitulo(), pauta.getDescricao());
        return dadosRetornaPautaEspecifica;
    }
}
