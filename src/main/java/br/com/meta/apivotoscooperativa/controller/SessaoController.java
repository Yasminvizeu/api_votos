package br.com.meta.apivotoscooperativa.controller;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosIniciaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessao;
import br.com.meta.apivotoscooperativa.service.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        var uri = uriBuilder.path("pauta/sessao/id").buildAndExpand(dadosRetornaSessao.getId()).toUri();

        return ResponseEntity.created(uri).body(dadosRetornaSessao);

    }
}
