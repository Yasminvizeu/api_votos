package br.com.meta.apivotoscooperativa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="Sessao")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sessoes")
@EqualsAndHashCode(of = "id")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numeroVotosSim;
    private Integer numeroVotosNao;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    //private Pauta pauta;
    //private Associado associadosQueVotaram;

}
