package br.ind.ajrorato.usecases;

import br.ind.ajrorato.domain.model.Anexo;
import br.ind.ajrorato.domain.repositories.AnexoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SalvarArquivoFtpUseCase {
    private final AnexoRepository anexoRepository;

    public Anexo execute(Long idAnexo, String tipoAnexo, String tipoConteudo, MultipartFile arquivo) {
        if (Objects.isNull(arquivo))
            throw new IllegalArgumentException("Arquivo não informado");

        var anexo = new Anexo(idAnexo, arquivo.getOriginalFilename(), tipoAnexo, tipoConteudo);
        return anexoRepository.salvar(anexo, arquivo);
    }
}
