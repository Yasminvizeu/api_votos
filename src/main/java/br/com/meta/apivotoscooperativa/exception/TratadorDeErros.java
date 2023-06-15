package br.com.meta.apivotoscooperativa.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex){
        var erros = ex.getFieldErrors();
        var listaDeErros =  new ArrayList<DadosErrosValidacao>();
        //criando lista de erros
        for (FieldError error : erros){
            var erroValidado = new DadosErrosValidacao(error.getField(), error.getDefaultMessage());
            listaDeErros.add(erroValidado);
        }

        return ResponseEntity.badRequest().body(erros.stream().map(le -> "Campo " + le.getField() + " " + le.getDefaultMessage()));//convertendo a lista de erros apra DadosErrosValidacao
    }

    @ExceptionHandler(PautaJaExistenteException.class)
    public ResponseEntity tratarErroPautaJaExistente(PautaJaExistenteException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PautaInexistenteException.class)
    public ResponseEntity tratarErroPautaInexistente(PautaInexistenteException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(VotoDuplicadoException.class)
    public ResponseEntity tratarVotoDuplicado(VotoDuplicadoException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(VotoInvalidoException.class)
    public ResponseEntity tratarVotoInvalido(VotoInvalidoException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AssociadoInexistenteException.class)
    public ResponseEntity tratarAssociadoInexistente(AssociadoInexistenteException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(VotoEmSessaoFechadaException.class)
    public ResponseEntity tratarVotoEmSessaoFechada(VotoEmSessaoFechadaException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(SessaoInexistenteException.class)
    public ResponseEntity tratarSessaoInexistente(SessaoInexistenteException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PautaNaoVotadaException.class)
    public ResponseEntity tratarPautaNaoVotada(PautaNaoVotadaException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AssociadoCpfInvalido.class)
    public ResponseEntity tratarAssociadoCpfInvalido(AssociadoCpfInvalido ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(FalhaNaValidacaoCpfException.class)
    public ResponseEntity tratarFalhaNaValidacaoCpf(FalhaNaValidacaoCpfException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AssociadoJaExistenteException.class)
    public ResponseEntity tratarAssociadoJaExistente(AssociadoJaExistenteException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    public class DadosErrosValidacao {
        private String campo;
        private String mensagem;
        public DadosErrosValidacao(String campo, String defautMessage){
            this.campo = campo;
            this.mensagem = defautMessage;
        }

    }


}
