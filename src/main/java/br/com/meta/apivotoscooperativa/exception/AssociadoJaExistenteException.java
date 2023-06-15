package br.com.meta.apivotoscooperativa.exception;

public class AssociadoJaExistenteException extends RuntimeException {

    public AssociadoJaExistenteException(){
        super("Este CPF já está cadastrado.");
    }

}
