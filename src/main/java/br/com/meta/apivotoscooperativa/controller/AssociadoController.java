package br.com.meta.apivotoscooperativa.controller;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociadoEspecifico;
import br.com.meta.apivotoscooperativa.service.AssociadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("associados")
public class AssociadoController {

    @Autowired
    private AssociadoService associadoService;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosRetornaAssociado> cadastraAssociado(@RequestBody @Valid DadosCadastraAssociado dados, UriComponentsBuilder uriBuilder){
        DadosRetornaAssociado dadosRetornaAssociado = associadoService.cadastraAssociado(dados);
        var uri = uriBuilder.path("associados/{id}").buildAndExpand(dadosRetornaAssociado.id()).toUri();

        return ResponseEntity.created(uri).body(dadosRetornaAssociado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosRetornaAssociadoEspecifico> consultaAssociadoPorId(@PathVariable long id) {
        var dadosRetornaAssociadoEspecifico = associadoService.consultaAssociadoPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(dadosRetornaAssociadoEspecifico);
    }
}
