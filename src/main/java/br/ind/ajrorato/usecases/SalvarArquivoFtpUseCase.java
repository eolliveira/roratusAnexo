package br.ind.ajrorato.usecases;

import br.ind.ajrorato.config.FileStorageConfig;
import br.ind.ajrorato.domain.model.Arquivo;
import br.ind.ajrorato.domain.repositories.ArquivoRepository;
import ch.qos.logback.core.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@RequiredArgsConstructor
public class SalvarArquivoFtpUseCase {
    //private final ArquivoRepository arquivoRepository;

    @Autowired
    private FileStorageConfig fileStorageConfig;

    private final Path urlArmazenamentoArquivos = Paths.get("/temp").toAbsolutePath().normalize();

    public String execute(MultipartFile arquivo) {
        //remove espaços em branco do nome do arquivo
        String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());

        try {
            // Verifica se o nome do arquivo é seguro
            if (nomeArquivo.contains("..")) {
                throw new RuntimeException("Nome do arquivo inválido: " + nomeArquivo);
            }

            // Cria o diretório se não existir antes de salvar


            // Cria o diretório se não existir "/ACORDOANEXO/IMAGEM/2025/123_nome_arquivo"
            Path caminhoArquivo = this.urlArmazenamentoArquivos.resolve("ACORDOANEXO/IMAGEM/2025/" + nomeArquivo);

            // Cria o diretório se não existir e substitui o arquivo se já existir
            Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);


            // Salvar o arquivo no repositório
            //arquivoRepository.salvar(arquivo);

            return caminhoArquivo.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage());
        }


    }
}
