package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraPauta;
import br.com.meta.apivotoscooperativa.exception.PautaInexistenteException;
import br.com.meta.apivotoscooperativa.exception.PautaRepetidaException;
import br.com.meta.apivotoscooperativa.model.Pauta;
import br.com.meta.apivotoscooperativa.model.Sessao;
import br.com.meta.apivotoscooperativa.repository.PautaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class PautaServiceTest {


    @Test
    @DisplayName("Deveria retornar id da pauta cadastrada")
    void cadastraCenario1(){
        PautaRepository pautaRepository = Mockito.mock(PautaRepository.class);
        PautaService pautaService = new PautaService(pautaRepository);
        var dadosRetornaPauta = pautaService.cadastra(new DadosCadastraPauta("teste", "teste"));

        Assertions.assertNull(dadosRetornaPauta.id());
    }

    @Test
    @DisplayName("Deveria retornar pauta existente exception no caso de cadastro com o mesmo título" +
            " e descrição")
    void cadastraCenario2() {
        PautaRepository pautaRepository = Mockito.mock(PautaRepository.class);
        Mockito.when(pautaRepository.existsByTituloAndDescricao("teste", "teste"))
                .thenReturn(true);
        PautaService pautaService = new PautaService(pautaRepository);

        Assertions.assertThrows(PautaRepetidaException.class,
                () -> pautaService.cadastra(new DadosCadastraPauta("teste", "teste")));
    }

    @Test
    @DisplayName("Deveria retornar pauta especifica.")
    void consultaCenario1() {
        PautaRepository pautaRepository = Mockito.mock(PautaRepository.class);
        Mockito.when(pautaRepository.existsById(Long.valueOf(1)))
                .thenReturn(true);
        Mockito.when(pautaRepository.getReferenceById(Long.valueOf(1)))
                .thenReturn(new Pauta(Long.valueOf(1), "teste", "teste", new Sessao()));
        PautaService pautaService = new PautaService(pautaRepository);
        var dadosRetornaPautaEspecifica = pautaService.consulta(Long.valueOf(1));

        Assertions.assertEquals(Long.valueOf(1), dadosRetornaPautaEspecifica.id());
    }

    @Test
    @DisplayName("Deveria retornar exception de pauta inexistente.")
    void consultaCenario2() {
        PautaRepository pautaRepository = Mockito.mock(PautaRepository.class);
        Mockito.when(pautaRepository.existsById(Long.valueOf(1)))
                .thenReturn(false);
        PautaService pautaService = new PautaService(pautaRepository);

        Assertions.assertThrows(PautaInexistenteException.class,
                () -> pautaService.consulta(Long.valueOf(1)));
    }
}