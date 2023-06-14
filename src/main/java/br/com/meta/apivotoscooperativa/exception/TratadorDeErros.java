package br.com.meta.apivotoscooperativa.exception;

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

    public class DadosErrosValidacao {
        private String campo;
        private String mensagem;
        public DadosErrosValidacao(String campo, String defautMessage){
            this.campo = campo;
            this.mensagem = defautMessage;
        }

    }
}
