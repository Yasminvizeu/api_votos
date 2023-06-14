package br.com.meta.apivotoscooperativa.exception;

public class VotoDuplicadoException extends RuntimeException{

    public VotoDuplicadoException(String mensagem){
        super(mensagem);
    }

}
