package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraPauta;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPauta;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPautaEspecifica;
import br.com.meta.apivotoscooperativa.exception.PautaInexistenteException;
import br.com.meta.apivotoscooperativa.exception.PautaRepitida;
import br.com.meta.apivotoscooperativa.model.Pauta;
import br.com.meta.apivotoscooperativa.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    @Autowired
    private PautaRepository repository;

    public DadosRetornaPauta cadastra(DadosCadastraPauta dados) {
        if (repository.existsByTituloAndDescricao(dados.titulo(), dados.descricao())) {
            throw new br.com.meta.apivotoscooperativa.exception.PautaRepitida();
        } else{

            var pauta = new Pauta();
            pauta.setTitulo(dados.titulo());
            pauta.setDescricao(dados.descricao());

            repository.save(pauta);


            return new DadosRetornaPauta(pauta.getId());
        }

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
