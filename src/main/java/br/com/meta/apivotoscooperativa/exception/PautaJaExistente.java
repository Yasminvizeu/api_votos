package br.com.meta.apivotoscooperativa.exceptions;

public class PautaJaExistente extends RuntimeException{

    public PautaJaExistente(){
        super("Já existe uma sessao dessa pauta");

    }

}
