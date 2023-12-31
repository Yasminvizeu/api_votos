package br.com.meta.apivotoscooperativa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "associados_id")
    private List<Associado> associados;


}
