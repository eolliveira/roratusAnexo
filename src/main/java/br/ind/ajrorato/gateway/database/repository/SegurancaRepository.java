package br.ind.ajrorato.gateway.database.repository;

import br.ind.ajrorato.gateway.database.entity.SegurancaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SegurancaRepository extends CrudRepository<SegurancaEntity, Long> {
    Optional<SegurancaEntity> findByLogin(String login);
}
