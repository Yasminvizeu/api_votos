package br.com.meta.apivotoscooperativa.exception;

public class PautaNaoVotadaException extends RuntimeException {
    public PautaNaoVotadaException(){
        super("A pauta informada ainda não foi votada.");
    }
}
