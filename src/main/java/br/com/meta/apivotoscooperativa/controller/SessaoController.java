package br.com.meta.apivotoscooperativa.controller;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraVoto;
import br.com.meta.apivotoscooperativa.dto.entrada.DadosIniciaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessaoEspecifica;
import br.com.meta.apivotoscooperativa.service.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosRetornaSessao> iniciaSessao(@RequestBody @Valid DadosIniciaSessao dados, UriComponentsBuilder uriBuilder){
        DadosRetornaSessao dadosRetornaSessao = sessaoService.iniciaSessao(dados);
        var uri = uriBuilder.path("sessao/{id}").buildAndExpand(dadosRetornaSessao.getId()).toUri();
        return ResponseEntity.created(uri).body(dadosRetornaSessao);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosRetornaSessaoEspecifica> consulta(@PathVariable Long id){
        var dadosRetornaSessaoEspecifica = sessaoService.consulta(id);
        return ResponseEntity.status(HttpStatus.OK).body(dadosRetornaSessaoEspecifica);
    }

    @GetMapping("/pautas/{id}")
    public ResponseEntity<DadosRetornaSessaoEspecifica> consultaSessaoPorIdPauta(@PathVariable Long id){
        var dadosRetornaSessaoEspecifica = sessaoService.consultaSessaoPorIdPauta(id);
        return ResponseEntity.status(HttpStatus.OK).body(dadosRetornaSessaoEspecifica);
    }

    @PostMapping("/votar")
    @Transactional
    public ResponseEntity vota(@RequestBody @Valid DadosCadastraVoto dadosCadastraVoto){
        sessaoService.registraVoto(dadosCadastraVoto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
