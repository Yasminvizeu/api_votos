package br.com.meta.apivotoscooperativa.repository;

import br.com.meta.apivotoscooperativa.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
