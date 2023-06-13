package br.com.meta.apivotoscooperativa.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.sql.Array;
import java.util.ArrayList;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex){
        var erros = ex.getFieldErrors();
        var listaDeErros =  new ArrayList<DadosErrosValidacao>();
        //criando lista de erros
        for (FieldError error:erros){
            var erroValidado = new DadosErrosValidacao(error);
            listaDeErros.add(erroValidado);
        }
        return ResponseEntity.badRequest().body(listaDeErros);//convertendo a lista de erros apra DadosErrosValidacao
    }
    private class DadosErrosValidacao {
        private String campo;
        private String mensagem;
        public DadosErrosValidacao(FieldError erro){
            this.campo = erro.getField();

            this.mensagem = erro.getDefaultMessage();

        }

    }
}
