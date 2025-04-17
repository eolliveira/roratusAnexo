package br.ind.ajrorato.usecases;

import br.ind.ajrorato.domain.model.Anexo;
import br.ind.ajrorato.domain.repositories.AnexoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SalvarArquivoFtpUseCase {
    private final AnexoRepository anexoRepository;

    public SalvarArquivoFtpUseCase(AnexoRepository anexoRepository) {
        this.anexoRepository = anexoRepository;
    }

    public Anexo execute(Long idAnexo, String tipoAnexo, String tipoConteudo, MultipartFile arquivo) {
        var anexo = new Anexo(idAnexo, arquivo.getOriginalFilename(), tipoAnexo, tipoConteudo);
        return anexoRepository.salvar(anexo, arquivo);
    }
}
