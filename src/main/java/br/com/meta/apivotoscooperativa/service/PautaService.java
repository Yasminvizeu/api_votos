package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraPauta;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPauta;
import br.com.meta.apivotoscooperativa.model.Pauta;
import br.com.meta.apivotoscooperativa.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
