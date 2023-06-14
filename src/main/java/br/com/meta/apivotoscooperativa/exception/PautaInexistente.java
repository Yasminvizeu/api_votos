package br.com.meta.apivotoscooperativa.exceptions;

public class PautaInexistente extends RuntimeException{

    public PautaInexistente(){
        super("Pauta Inexistente!");
    }
}
