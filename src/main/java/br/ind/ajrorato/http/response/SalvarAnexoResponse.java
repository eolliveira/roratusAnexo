package br.ind.ajrorato.http.response;

import lombok.Builder;

@Builder
public record SalvarAnexoResponse(
        Long idAnexo,
        String nomeArquivo,
        String tipoAnexo,
        String tipoConteudo,
        String urlAnexoFtp,
        String uriAnexoHttp,
        Long tamanhoArquivo
) {
}
