package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociadoEspecifico;
import br.com.meta.apivotoscooperativa.exception.AssociadoInexistenteException;
import br.com.meta.apivotoscooperativa.model.Associado;
import br.com.meta.apivotoscooperativa.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    public DadosRetornaAssociado cadastraAssociado(DadosCadastraAssociado dados){
        //salvando associado no banco
        var associado = new Associado();
        associado.setCpf(dados.getCpf());
        associadoRepository.save(associado);

        return new DadosRetornaAssociado(associado.getId());
    }

    public DadosRetornaAssociadoEspecifico consulta(@PathVariable Long id){
       //ESSA VALIDAÇÃO NAO DEVERIA ESTAR NO METOD DE CADASTRA?
        if(!associadoRepository.existsById(id)){
            throw new AssociadoInexistenteException();
        }
        var associado = associadoRepository.getReferenceById(id);
        var dadosRetornaAssociadoEspecifico = new DadosRetornaAssociadoEspecifico(associado.getId(), associado.getCpf());
        return dadosRetornaAssociadoEspecifico;
    }

}
