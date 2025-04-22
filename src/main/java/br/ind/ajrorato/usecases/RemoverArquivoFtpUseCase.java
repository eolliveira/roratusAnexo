package br.ind.ajrorato.usecases;

import br.ind.ajrorato.domain.repositories.AnexoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RemoverArquivoFtpUseCase {
    private final AnexoRepository anexoRepository;

    public void execute(String diretorioArquivo) {
        if (Objects.isNull(diretorioArquivo))
            throw new IllegalArgumentException("Diretório do arquivo deve ser informado");

        anexoRepository.apagar(diretorioArquivo);
    }
}
