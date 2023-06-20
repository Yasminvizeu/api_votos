package br.com.meta.apivotoscooperativa.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Associado")
@Data
@AllArgsConstructor

@NoArgsConstructor
@Table(name = "associados")
@EqualsAndHashCode(of = "id")
public class Associado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;


}

