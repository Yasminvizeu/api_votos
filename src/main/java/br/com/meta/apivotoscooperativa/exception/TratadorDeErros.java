package br.com.meta.apivotoscooperativa.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


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
    @ExceptionHandler(PautaJaExistente.class)
    public ResponseEntity tratarErroPautaJaExistente(PautaJaExistente ex){

        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(PautaInexistente.class)
    public ResponseEntity tratarErroPautaInexistente(PautaInexistente ex){

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
