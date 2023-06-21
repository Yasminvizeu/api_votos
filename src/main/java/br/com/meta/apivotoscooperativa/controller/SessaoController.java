package br.com.meta.apivotoscooperativa.controller;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraVoto;
import br.com.meta.apivotoscooperativa.dto.entrada.DadosIniciaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessaoEspecifica;
import br.com.meta.apivotoscooperativa.service.SessaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    @PostMapping("/v1")
    @Transactional
    public ResponseEntity<DadosRetornaSessao> iniciaSessao(@RequestBody @Valid DadosIniciaSessao dados, UriComponentsBuilder uriBuilder){
        DadosRetornaSessao dadosRetornaSessao = sessaoService.iniciaSessao(dados);
        var uri = uriBuilder.path("sessao/v1/{id}").buildAndExpand(dadosRetornaSessao.id()).toUri();
        return ResponseEntity.created(uri).body(dadosRetornaSessao);

    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<DadosRetornaSessaoEspecifica> consulta(@PathVariable Long id){
        var dadosRetornaSessaoEspecifica = sessaoService.consulta(id);
        return ResponseEntity.status(HttpStatus.OK).body(dadosRetornaSessaoEspecifica);
    }

    @GetMapping("/v1/pautas/{id}")
    public ResponseEntity<DadosRetornaSessaoEspecifica> consultaSessaoPorIdPauta(@PathVariable Long id){
        var dadosRetornaSessaoEspecifica = sessaoService.consultaSessaoPorIdPauta(id);
        return ResponseEntity.status(HttpStatus.OK).body(dadosRetornaSessaoEspecifica);
    }

    @PostMapping("/v1/votar")
    @Transactional
    public ResponseEntity vota(@RequestBody @Valid DadosCadastraVoto dadosCadastraVoto){
        sessaoService.registraVoto(dadosCadastraVoto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
