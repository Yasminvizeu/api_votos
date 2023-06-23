package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraVoto;
import br.com.meta.apivotoscooperativa.dto.entrada.DadosIniciaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessao;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaSessaoEspecifica;
import br.com.meta.apivotoscooperativa.exception.*;
import br.com.meta.apivotoscooperativa.model.Associado;
import br.com.meta.apivotoscooperativa.model.Pauta;
import br.com.meta.apivotoscooperativa.model.Sessao;
import br.com.meta.apivotoscooperativa.repository.AssociadoRepository;
import br.com.meta.apivotoscooperativa.repository.PautaRepository;
import br.com.meta.apivotoscooperativa.repository.SessaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.lang.reflect.Executable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Test
    @DisplayName("Deveria retornar dto de saida da sessao iniciada")
    void iniciaSessao() {
        var pauta = new Pauta(Long.valueOf(1), null, null, null);
        var dadosIniciaSessao = new DadosIniciaSessao(pauta.getId(), Long.valueOf(10));
        var dadosRetornaSessaoEsperado = new DadosRetornaSessao(null);
        var sessaoService = geraRepositorios();
        Mockito.when(sessaoRepository.existsByPautaId(pauta.getId())).thenReturn(false);
        Mockito.when(pautaRepository.existsById(pauta.getId())).thenReturn(true);
        Mockito.when(pautaRepository.findPautaById(pauta.getId())).thenReturn(pauta);
        var dadosRetornaSessao = sessaoService.iniciaSessao(dadosIniciaSessao);

        Assertions.assertEquals(dadosRetornaSessaoEsperado, dadosRetornaSessao);
    }

    @Test
    @DisplayName("Deveria retornar exceção de pauta já votada")
    void iniciaSessaoDePautaJaVotada(){
        var pauta = new Pauta(Long.valueOf(1), null, null, null);
        var sessaoService = geraRepositorios();
        var dadosIniciaSessao = new DadosIniciaSessao(pauta.getId(), Long.valueOf(10));
        Mockito.when(sessaoRepository.existsByPautaId(pauta.getId())).thenReturn(true);

        Assertions.assertThrows(PautaJaExistenteException.class, () -> sessaoService.iniciaSessao(dadosIniciaSessao));
    }

    @Test
    @DisplayName("Deveria retornar exceção de pauta inexistente")
    void iniciaSessaoDePautaInexistente(){
        var pauta = new Pauta(Long.valueOf(1), null, null, null);
        var dadosIniciaSessao = new DadosIniciaSessao(pauta.getId(), Long.valueOf(10));
        var sessaoService = geraRepositorios();
        Mockito.when(sessaoRepository.existsByPautaId(pauta.getId())).thenReturn(false);
        Mockito.when(pautaRepository.existsById(pauta.getId())).thenReturn(false);

        Assertions.assertThrows(PautaInexistenteException.class, () -> sessaoService.iniciaSessao(dadosIniciaSessao));
    }

    @Test
    @DisplayName("Deveria retornar exceção de associado inexistente")
    void registraVotoDeAssociadoInexistente() {
        var dadosCadastraVoto = new DadosCadastraVoto(Long.valueOf(1), Long.valueOf(1), "sim");
        var sessaoService = geraRepositorios();
        Mockito.when(associadoRepository.existsById(dadosCadastraVoto.idAssociado())).thenReturn(false);

        Assertions.assertThrows(AssociadoInexistenteException.class, () -> sessaoService.registraVoto(dadosCadastraVoto));
    }

    @Test
    @DisplayName("Deveria retornar exceção de pauta inexistente")
    void registraVotoEmPautaInexistente() {
        var dadosCadastraVoto = new DadosCadastraVoto(Long.valueOf(1), Long.valueOf(1), "sim");
        var sessaoService = geraRepositorios();
        Mockito.when(associadoRepository.existsById(dadosCadastraVoto.idAssociado())).thenReturn(true);
        Mockito.when(pautaRepository.existsById(dadosCadastraVoto.idPauta())).thenReturn(false);

        Assertions.assertThrows(PautaInexistenteException.class, () -> sessaoService.registraVoto(dadosCadastraVoto));
    }

    @Test
    @DisplayName("Deveria retornar exceção de voto em sessão fechada")
    void registraVotoEmSessaoJaEncerrada() {
        var horaAtras = LocalDateTime.now().minusHours(1);
        var sessaoEncerrada = new Sessao(Long.valueOf(1),0,0,horaAtras,Long.valueOf(10),null,null);
        var dadosCadastraVoto = new DadosCadastraVoto(Long.valueOf(1), Long.valueOf(1), "sim");
        var sessaoService = geraRepositorios();
        Mockito.when(associadoRepository.existsById(dadosCadastraVoto.idAssociado())).thenReturn(true);
        Mockito.when(pautaRepository.existsById(dadosCadastraVoto.idPauta())).thenReturn(true);
        Mockito.when(sessaoRepository.findSessaoByPautaId(Long.valueOf(1))).thenReturn(sessaoEncerrada);

        Assertions.assertThrows(VotoEmSessaoFechadaException.class, () -> sessaoService.registraVoto(dadosCadastraVoto));
    }

    @Test
    @DisplayName("Deveria retornar exceção de voto duplicado")
    void registraVotoDuplicado() {
        var hora = LocalDateTime.now().plusHours(1);
        var associado = new Associado(Long.valueOf(1),"125.030.196-33");
        List<Associado> listaAssociados = new ArrayList<>();
        listaAssociados.add(associado);
        var sessaoVotada = new Sessao(Long.valueOf(1),0,0,hora,Long.valueOf(10),null,listaAssociados);
        var dadosCadastraVoto = new DadosCadastraVoto(Long.valueOf(1), Long.valueOf(1), "sim");
        var sessaoService = geraRepositorios();
        Mockito.when(associadoRepository.existsById(dadosCadastraVoto.idAssociado())).thenReturn(true);
        Mockito.when(pautaRepository.existsById(dadosCadastraVoto.idPauta())).thenReturn(true);
        Mockito.when(sessaoRepository.findSessaoByPautaId(Long.valueOf(1))).thenReturn(sessaoVotada);

        Assertions.assertThrows(VotoDuplicadoException.class, () -> sessaoService.registraVoto(dadosCadastraVoto));
    }

    @Test
    @DisplayName("Deveria retornar dto de saída da sessao consultada")
    void consultaDevolveExiste() {
        var dataAgora = LocalDateTime.now();
        var sessao = new Sessao(Long.valueOf(1),0,0, dataAgora, Long.valueOf(10), new Pauta(), new ArrayList<>());
        var sessaoService = geraRepositorios();
        Mockito.when(sessaoRepository.existsById(Long.valueOf(1))).thenReturn(true);
        Mockito.when(sessaoRepository.getReferenceById(Long.valueOf(1))).thenReturn(sessao);
        var dadosRetornaSessaoEspecificaValidar = new DadosRetornaSessaoEspecifica(
                null,
                Long.valueOf(1),
                null,
                0,
                0,
                dataAgora,
                Long.valueOf(10)
                );
        DadosRetornaSessaoEspecifica dadosRetornaSessaoEspecifica = sessaoService.consulta(Long.valueOf(1));

        Assertions.assertEquals(dadosRetornaSessaoEspecificaValidar, dadosRetornaSessaoEspecifica);
    }

    @Test
    @DisplayName("Deveria retornar exceção de sessao inexistente")
    void consultaDevolveInexistente() {
        var sessaoService = geraRepositorios();
        Mockito.when(sessaoRepository.existsById(Long.valueOf(1))).thenReturn(false);

        Assertions.assertThrows(SessaoInexistenteException.class, () -> sessaoService.consulta(Long.valueOf(1)));
    }

    @Test
    @DisplayName("Deveria retornar dto de saida da sessao especifica")
    void consultaSessaoPorIdPauta() {
        var dataAgora = LocalDateTime.now();
        var sessao = new Sessao(Long.valueOf(1),0,0,dataAgora,Long.valueOf(10), new Pauta(), new ArrayList<>());
        var pauta = new Pauta(Long.valueOf(1), null, null, sessao);
        var sessaoService = geraRepositorios();
        Mockito.when(pautaRepository.existsById(Long.valueOf(1))).thenReturn(true);
        Mockito.when(pautaRepository.findPautaById(Long.valueOf(1))).thenReturn(pauta);
        Mockito.when(sessaoRepository.findSessaoByPautaId(Long.valueOf(1))).thenReturn(sessao);
        var dadosRetornaSessaoEspecificaValidar = new DadosRetornaSessaoEspecifica(
                null,
                Long.valueOf(1),
                null,
                0,
                0,
                dataAgora,
                Long.valueOf(10)
        );
        DadosRetornaSessaoEspecifica dadosRetornaSessaoEspecifica = sessaoService.consultaSessaoPorIdPauta(Long.valueOf(1));

        Assertions.assertEquals(dadosRetornaSessaoEspecificaValidar, dadosRetornaSessaoEspecifica);
    }

    @Test
    @DisplayName("Deveria retornar exceção de pauta inexistente")
    void consultaSessaoPorIdPautaDevolveInexistente() {
        var sessaoService = geraRepositorios();
        Mockito.when(pautaRepository.existsById(Long.valueOf(1))).thenReturn(false);

        Assertions.assertThrows(PautaInexistenteException.class, () -> sessaoService.consultaSessaoPorIdPauta(Long.valueOf(1)));
    }

    @Test
    @DisplayName("Deveria retornar exceção de pauta não votada")
    void consultaSessaoPorIdPautaDevolvePautaNaoVotada() {
        var pauta = new Pauta(Long.valueOf(1), null, null, null);
        var sessaoService = geraRepositorios();
        Mockito.when(pautaRepository.existsById(Long.valueOf(1))).thenReturn(true);
        Mockito.when(pautaRepository.findPautaById(Long.valueOf(1))).thenReturn(pauta);
        Assertions.assertThrows(PautaNaoVotadaException.class, () -> sessaoService.consultaSessaoPorIdPauta(Long.valueOf(1)));
    }

    SessaoService geraRepositorios(){
        return new SessaoService(this.sessaoRepository,this.pautaRepository,this.associadoRepository);
    }
}