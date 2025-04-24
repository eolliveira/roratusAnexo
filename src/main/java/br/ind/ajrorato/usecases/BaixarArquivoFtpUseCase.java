package br.ind.ajrorato.usecases;

import br.ind.ajrorato.domain.repositories.AnexoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BaixarArquivoFtpUseCase {
    private final AnexoService anexoService;

    public Resource execute(String diretorioArquivo) {
        if (Objects.isNull(diretorioArquivo))
            throw new IllegalArgumentException("Diretório do arquivo deve ser informado");

        return anexoService.baixar(diretorioArquivo);
    }
}
