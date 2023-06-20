package br.com.meta.apivotoscooperativa.exception;

public class PautaRepitida extends RuntimeException {
    public PautaRepitida() {
        super("Já existe uma pauta com o mesmo título e descrição");
    }
}
