package br.com.meta.apivotoscooperativa.exception;

public class VotoInvalidoException extends RuntimeException{

    public VotoInvalidoException(String mensagem){
        super(mensagem);
    }

}
