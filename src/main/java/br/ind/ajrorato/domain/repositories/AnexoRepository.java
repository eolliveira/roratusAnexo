package br.ind.ajrorato.domain.repositories;

import br.ind.ajrorato.domain.exceptions.FileDownloadException;
import br.ind.ajrorato.domain.exceptions.FileRemoveException;
import br.ind.ajrorato.domain.exceptions.FileUploadException;
import br.ind.ajrorato.domain.model.Anexo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AnexoRepository {
    Anexo salvar(Anexo anexo, MultipartFile arquivo) throws FileUploadException;
    Resource baixar(String diretorioArquivo) throws FileDownloadException;
    void apagar(String diretorioArquivo) throws FileRemoveException;
}