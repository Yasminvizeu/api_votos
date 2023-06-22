package br.com.meta.apivotoscooperativa.service;

import br.com.meta.apivotoscooperativa.dto.entrada.DadosCadastraAssociado;
import br.com.meta.apivotoscooperativa.dto.saida.DadosRetornaAssociado;
import br.com.meta.apivotoscooperativa.exception.AssociadoCpfInvalido;
import br.com.meta.apivotoscooperativa.exception.AssociadoInexistenteException;
import br.com.meta.apivotoscooperativa.exception.AssociadoJaExistenteException;
import br.com.meta.apivotoscooperativa.model.Associado;
import br.com.meta.apivotoscooperativa.repository.AssociadoRepository;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("associadoServiceTest")
public class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private AssociadoService associadoService;


    @Test
    @DisplayName("sucesso: entra com cpf único e valido retornando o id do novo associado cadastrado")
    void cadastraAssociado_cenario1() {
        String cpfValidoEUnico = "700.708.801-58";

        DadosCadastraAssociado dados = new DadosCadastraAssociado(cpfValidoEUnico);

        when(associadoRepository.existsByCpf(dados.cpf())).thenReturn(false);
        when(associadoRepository.save(any(Associado.class))).thenAnswer(invocation -> {
            Associado associado = invocation.getArgument(0);
            associado.setId(1L);
            return associado;
        });

        DadosRetornaAssociado resultado = associadoService.cadastraAssociado(dados);

        assertNotNull(resultado);
        assertNotNull(resultado.id());

        Mockito.verify(associadoRepository, Mockito.times(1)).save(any(Associado.class));

    }

    @Test
    @DisplayName("Deve receber um cpf repitido e valido e retornar AssociadoJaExistenteException")
    void cadastraAssociado_cenario2() {
        String cpfRepetido = "733.581.507-04";

        when(associadoRepository.existsByCpf(cpfRepetido)).thenReturn(true);

        assertThrows(AssociadoJaExistenteException.class, () -> {
            DadosCadastraAssociado dados = new DadosCadastraAssociado(cpfRepetido);
            associadoService.cadastraAssociado(dados);
        });

        Mockito.verify(associadoRepository, Mockito.never()).save(Mockito.any(Associado.class));

    }

    @Test
    @DisplayName("deve receber um CPF unico e inválido e retornar a exceção AssociadoCpfInvalido")
    void cadastraAssociado_cenario3() {
        String cpfInvalido = "123.456.789-00";

        assertThrows(AssociadoCpfInvalido.class, () -> {
            DadosCadastraAssociado dados = new DadosCadastraAssociado(cpfInvalido);
            associadoService.cadastraAssociado(dados);
        });

        Mockito.verify(associadoRepository, Mockito.never()).save(Mockito.any(Associado.class));

    }

    @Test
    @DisplayName("sucesso:Deve receber o associado com o ID fornecido existente no repositório e retornar um objeto DadosRetornaAssociadoEspecifico preenchido corretamente com os dados do associado.")
    void consultaAssociadoPorId_Cenario1() {
        Long id = 4L;
        String cpf = "733.581.507-04";

        when(associadoRepository.existsById(id)).thenReturn(true);

        when(associadoRepository.getReferenceById(id))
                .thenReturn(new Associado(id, cpf));

        var dadosRetornaAssociadoEspecifico = associadoService.consultaAssociadoPorId(id);

        assertEquals(id, dadosRetornaAssociadoEspecifico.id());
        assertEquals(cpf, dadosRetornaAssociadoEspecifico.cpf());
    }

    @Test
    @DisplayName("Deve receber o associado com o ID inválido ou inexistente e retornar uma exceção do tipo AssociadoInexistenteException")
    void consultaAssociadoPorId_Cenario2() {

        Long idInvalido = 100L;

        when(associadoRepository.existsById(idInvalido)).thenReturn(false);

        assertThrows(AssociadoInexistenteException.class, () -> {
            associadoService.consultaAssociadoPorId(idInvalido);
        });

    }


}