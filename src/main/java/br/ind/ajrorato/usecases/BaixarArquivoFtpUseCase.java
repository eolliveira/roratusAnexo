package br.ind.ajrorato.usecases;

import br.ind.ajrorato.domain.repositories.AnexoRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class BaixarArquivoFtpUseCase {
    private final AnexoRepository anexoRepository;

    public BaixarArquivoFtpUseCase(AnexoRepository anexoRepository) {
        this.anexoRepository = anexoRepository;
    }

    public Resource execute(String nomeArquivo) {
        try {
            return anexoRepository.baixar(nomeArquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar arquivo: " + nomeArquivo, e);
        }

    }
}
