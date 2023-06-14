package br.com.meta.apivotoscooperativa.exception;

public class PautaInexistenteException extends RuntimeException{

    public PautaInexistenteException(){
        super("Pauta inexistente.");
    }
}
