package br.com.meta.apivotoscooperativa.controller;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociadoEspecifico;
import br.com.meta.apivotoscooperativa.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("associados")
public class AssociadoCotnroller {


    @Autowired
    private AssociadoService associadoService;



    @PostMapping
    @Transactional
    public ResponseEntity<DadosRetornaAssociado> cadastraAssociado(@RequestBody @Valid DadosCadastraAssociado dados, UriComponentsBuilder uriBuilder){
        DadosRetornaAssociado dadosRetornaAssociado = associadoService.cadastraAssociado(dados);
        var uri = uriBuilder.path("asssociados/{id}").buildAndExpand(dadosRetornaAssociado.getId()).toUri();

        return ResponseEntity.created(uri).body(dadosRetornaAssociado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosRetornaAssociadoEspecifico> consultaAssociado(@PathVariable long id) {
        var dadosRetornaAssociadoEspecifico = associadoService.consulta(id);
        return ResponseEntity.ok().body(dadosRetornaAssociadoEspecifico);
    }
}
