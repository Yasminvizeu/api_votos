package br.com.meta.apivotoscooperativa.exception;

public class PautaJaExistenteException extends RuntimeException{

    public PautaJaExistenteException(){
        super("Já existe uma sessao dessa pauta");
    }

}
