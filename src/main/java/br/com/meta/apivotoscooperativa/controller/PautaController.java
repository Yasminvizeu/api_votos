package br.com.meta.apivotoscooperativa.controller;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraPauta;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPauta;
import br.com.meta.apivotoscooperativa.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @PostMapping
    public ResponseEntity<DadosRetornaPauta> cadastra(@RequestBody DadosCadastraPauta dados, UriComponentsBuilder uriBuilder){
        DadosRetornaPauta dadosRetornaPauta = pautaService.cadastra(dados);
        var uri = uriBuilder.path("pauta/id").buildAndExpand(dadosRetornaPauta.getId()).toUri();

        return ResponseEntity.created(uri).body(dadosRetornaPauta);

    }
}
