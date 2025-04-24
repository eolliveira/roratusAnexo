package br.ind.ajrorato.usecases;

import br.ind.ajrorato.domain.repositories.AnexoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RemoverArquivoFtpUseCase {
    private final AnexoService anexoService;

    public void execute(String diretorioArquivo) {
        if (Objects.isNull(diretorioArquivo))
            throw new IllegalArgumentException("Diretório do arquivo deve ser informado");

        anexoService.apagar(diretorioArquivo);
    }
}
