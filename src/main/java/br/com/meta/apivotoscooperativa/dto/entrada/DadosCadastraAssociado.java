package br.com.meta.apivotoscooperativa.dto.entrada;

import jakarta.validation.constraints.Pattern;
import lombok.*;



public record DadosCadastraAssociado(@Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "inválido") String cpf) {


}
