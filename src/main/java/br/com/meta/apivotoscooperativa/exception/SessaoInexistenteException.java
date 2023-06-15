package br.com.meta.apivotoscooperativa.exception;

public class SessaoInexistenteException extends RuntimeException {

    public SessaoInexistenteException(){
        super("A sessão informada não existe.");
    }
}
