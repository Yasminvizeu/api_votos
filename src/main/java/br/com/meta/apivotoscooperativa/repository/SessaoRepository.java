package br.com.meta.apivotoscooperativa.repository;

import br.com.meta.apivotoscooperativa.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SessaoRepository  extends JpaRepository<Sessao, Long> {


    boolean existsByPautaId(Long idPauta);

}
