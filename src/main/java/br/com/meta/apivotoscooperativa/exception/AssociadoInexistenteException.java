package br.com.meta.apivotoscooperativa.exception;

public class AssociadoInexistenteException extends RuntimeException {

    public AssociadoInexistenteException(){
        super("Associado inexistente.");
    }

}
