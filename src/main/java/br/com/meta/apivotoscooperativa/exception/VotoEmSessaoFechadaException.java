package br.com.meta.apivotoscooperativa.exception;

public class VotoEmSessaoFechadaException extends RuntimeException {

    public VotoEmSessaoFechadaException(){
        super("Sessão já está fechada e não recebe mais votos");
    }

}
