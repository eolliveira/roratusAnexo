package br.ind.ajrorato.http.response;

import lombok.Builder;

@Builder
public record SalvarAnexoResponse(
        Long idAnexo,
        String nomeArquivo,
        String mimeType,
        String tipoAnexo,
        String tipoConteudo,
        String urlAnexoFtp,
        String uriAnexoPreview,
        String uriAnexoDownload,
        String uriAnexoDelete,
        Long tamanhoArquivo
) {
}
