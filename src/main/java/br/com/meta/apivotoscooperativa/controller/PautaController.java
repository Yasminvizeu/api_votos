package br.com.meta.apivotoscooperativa.controller;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraPauta;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPauta;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaPautaEspecifica;
import br.com.meta.apivotoscooperativa.service.PautaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @PostMapping("/v1")
    @Transactional
    public ResponseEntity<DadosRetornaPauta> cadastra(@RequestBody @Valid DadosCadastraPauta dados, UriComponentsBuilder uriBuilder){
        DadosRetornaPauta dadosRetornaPauta = pautaService.cadastra(dados);
        var uri = uriBuilder.path("pautas/{id}").buildAndExpand(dadosRetornaPauta.id()).toUri();
        return ResponseEntity.created(uri).body(dadosRetornaPauta);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<DadosRetornaPautaEspecifica> consulta(@PathVariable Long id){
        var dadosRetornaPautaEspecifica = pautaService.consulta(id);
        return ResponseEntity.ok().body(dadosRetornaPautaEspecifica);
    }

}
