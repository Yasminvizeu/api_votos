package br.com.meta.apivotoscooperativa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

