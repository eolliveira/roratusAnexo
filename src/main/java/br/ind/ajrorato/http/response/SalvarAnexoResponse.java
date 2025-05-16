package br.ind.ajrorato.http.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SalvarAnexoResponse(
        Long idAnexo,
        String nomeArquivo,
        @Schema(example = "application/pdf")
        String mimeType,
        String tipoAnexo,
        String tipoConteudo,
        @Schema(example = "TESTESWAGGER/IMAGEM/2025/4/1_teste.jpeg")
        String diretorioAnexoFtp,
        @Schema(example = "ftp://erp-repo.ajrorato.ind.br/TESTESWAGGER/IMAGEM/2025/4/1_teste.jpeg")
        String urlAnexoFtp,
        @Schema(example = "http://localhost:8080/api/anexo/recovery?path=TESTESWAGGER/IMAGEM/2025/4/1_teste.jpeg")
        String uriAnexoRecovery,
        @Schema(example = "http://localhost:8080/api/anexo/download?path=TESTESWAGGER/IMAGEM/2025/4/1_teste.jpeg")
        String uriAnexoDownload,
        Long tamanhoArquivo
) {
}
