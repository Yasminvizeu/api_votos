package br.com.meta.apivotoscooperativa.exception;

public class PautaRepetidaException extends RuntimeException {
    public PautaRepetidaException() {
        super("Já existe uma pauta com o mesmo título e descrição");
    }
}
