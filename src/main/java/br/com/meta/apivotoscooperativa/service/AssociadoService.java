package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraAssociado;
import br.com.meta.apivotoscooperativa.dto.entrada.DadosCpfValidado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociadoEspecifico;
import br.com.meta.apivotoscooperativa.exception.AssociadoCpfInvalido;
import br.com.meta.apivotoscooperativa.exception.AssociadoInexistenteException;
import br.com.meta.apivotoscooperativa.exception.AssociadoJaExistenteException;
import br.com.meta.apivotoscooperativa.exception.FalhaNaValidacaoCpfException;
import br.com.meta.apivotoscooperativa.model.Associado;
import br.com.meta.apivotoscooperativa.repository.AssociadoRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    public DadosRetornaAssociado cadastraAssociado(DadosCadastraAssociado dados){
        if(associadoRepository.existsByCpf(dados.cpf())){
            throw new AssociadoJaExistenteException();
        }

        if(!validaCpf(dados.cpf())){
            throw new AssociadoCpfInvalido();
        }

        var associado = new Associado();
        associado.setCpf(dados.cpf());

        associadoRepository.save(associado);

        return new DadosRetornaAssociado(associado.getId());
    }

    public DadosRetornaAssociadoEspecifico consultaAssociadoPorId(Long id){

        if(!associadoRepository.existsById(id)){
            throw new AssociadoInexistenteException();
        }
        var associado = associadoRepository.getReferenceById(id);
        var dadosRetornaAssociadoEspecifico = new DadosRetornaAssociadoEspecifico(associado.getId(), associado.getCpf());
        return dadosRetornaAssociadoEspecifico;
    }

    public boolean validaCpf(String cpf){

        String cpfSemPontos = cpf.replace(".","");
        String cpfFormatado = cpfSemPontos.replace("-","");


        String link = String.format("https://api.nfse.io/validate/NaturalPeople/taxNumber/%s", cpfFormatado);


        var cliente = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(link))
                .build();


        HttpResponse<String> response = null;
        try {
            response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new FalhaNaValidacaoCpfException();
        } catch (InterruptedException e) {
            throw new FalhaNaValidacaoCpfException();
        }
        String json = response.body();
        Gson gson = new GsonBuilder().create();
        DadosCpfValidado cpfValidado = gson.fromJson(json, DadosCpfValidado.class);

        return cpfValidado.valid();
    }
}

