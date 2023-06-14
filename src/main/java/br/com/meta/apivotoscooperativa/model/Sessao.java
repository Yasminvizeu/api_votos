package br.com.meta.apivotoscooperativa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    //private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private Long duracao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id")
    private List<Associado> associadosQueVotaram;

}
