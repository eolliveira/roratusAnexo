package br.ind.ajrorato.domain.repositories;

import br.ind.ajrorato.domain.model.Arquivo;
import org.springframework.stereotype.Component;

@Component
public interface ArquivoRepository {
    Arquivo salvar(Arquivo arquivo);
    Arquivo obter(Arquivo arquivo);
    Arquivo apagar(Arquivo arquivo);
}
