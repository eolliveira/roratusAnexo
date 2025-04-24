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
        @Schema(example = "ftp://erp-repo.ajrorato.ind.br/ACORDO/IMAGEM/2025/4/1_teste.jpeg")
        String urlAnexoFtp,
        @Schema(example = "http://localhost:8080/api/anexo/preview?path=ACORDO/IMAGEM/2025/4/1_teste.jpeg")
        String uriAnexoPreview,
        @Schema(example = "http://localhost:8080/api/anexo/download?path=ACORDO/IMAGEM/2025/4/1_teste.jpeg")
        String uriAnexoDownload,
        @Schema(example = "http://localhost:8080/api/anexo/delete?path=ACORDO/IMAGEM/2025/4/1_teste.jpeg")
        String uriAnexoDelete,
        Long tamanhoArquivo
) {
}
