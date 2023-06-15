package br.com.meta.apivotoscooperativa.repository;

import br.com.meta.apivotoscooperativa.model.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    public Associado findAssociadoById(Long id);

    boolean existsByCpf(String cpf);
}
