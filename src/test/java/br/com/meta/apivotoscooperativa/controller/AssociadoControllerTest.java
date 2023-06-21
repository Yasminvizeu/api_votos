package br.com.meta.apivotoscooperativa.controller;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociado;
import br.com.meta.apivotoscooperativa.service.AssociadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AssociadoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastraAssociado> dadosCadastraAssociadoJson;

    @Autowired
    private JacksonTester<DadosRetornaAssociado> dadosRetornaAssociadoJson;

    @MockBean
    private AssociadoService associadoService;


    @Test
    @DisplayName("Deveria devolver codigo http 400 quando cpf esta invalido")
    void cadastraAssociado_cenario1() throws Exception {
       var response =  mvc.perform(MockMvcRequestBuilders.post("/associados/v1"))
                .andReturn().getResponse();

       assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 201 quando cpf esta valido")
    void cadastraAssociado_cenario2() throws Exception {

        var dadosDetalhamento = new DadosRetornaAssociado(3L);
        when(associadoService.cadastraAssociado(any())).thenReturn(dadosDetalhamento);

        var response =  mvc
                .perform(
                        MockMvcRequestBuilders.post("/associados/v1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosCadastraAssociadoJson.write(
                                        new DadosCadastraAssociado("423.314.098-46")
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jasonEsperado = dadosRetornaAssociadoJson.write(
                dadosDetalhamento
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jasonEsperado);

    }
}











