package br.com.meta.apivotoscooperativa.exception;

public class FalhaNaValidacaoCpfException extends RuntimeException {

    public FalhaNaValidacaoCpfException(){
        super("Falha ao tentar realizar a validação do CPF.");
    }

}
