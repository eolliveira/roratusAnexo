package br.ind.ajrorato.domain.repositories;

import br.ind.ajrorato.domain.model.Anexo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AnexoRepository {
    Anexo salvar(Anexo anexo, MultipartFile arquivo);
    Resource baixar(String nomeArquivo);
    Anexo apagar(Anexo arquivo);
}
