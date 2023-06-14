package br.com.meta.apivotoscooperativa.exception;

public class VotoDuplicadoException extends RuntimeException{

    public VotoDuplicadoException(){
        super("Este associado já votou.");
    }

}
