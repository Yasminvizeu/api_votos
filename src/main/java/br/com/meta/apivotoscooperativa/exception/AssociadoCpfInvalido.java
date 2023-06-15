package br.com.meta.apivotoscooperativa.exception;

public class AssociadoCpfInvalido extends RuntimeException {

    public AssociadoCpfInvalido(){
        super("O CPF digitado é inválido.");
    }

}
