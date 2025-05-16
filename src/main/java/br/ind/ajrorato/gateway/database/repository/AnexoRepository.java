package br.ind.ajrorato.gateway.database.repository;

import br.ind.ajrorato.gateway.database.entity.anexo.AnexoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnexoRepository extends CrudRepository<AnexoEntity, Long> {

    @Query("SELECT a FROM AnexoEntity a WHERE a.tipoAnexo = ?1 AND a.dirArquivoFtp IS NULL")
    List<AnexoEntity> findAllByTipoAnexoAndDirArquivoFtpIsNull(String tipoAnexo);
}