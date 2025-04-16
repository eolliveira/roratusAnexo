package br.ind.ajrorato.http.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@RequiredArgsConstructor
public record UploadFileResponse(
        Long idAnexo,
        String tipoAnexo,
        String nomeArquivo,
        String anexoDownloadUri,
        String tipoConteudo,
        Long tamanhoArquivo
) {
    /// construtor com todos os campos
    public UploadFileResponse(
            Long idAnexo,
            String tipoAnexo,
            String nomeArquivo,
            String anexoDownloadUri,
            String tipoConteudo,
            Long tamanhoArquivo
    ) {
        this.idAnexo = idAnexo;
        this.tipoAnexo = tipoAnexo;
        this.nomeArquivo = nomeArquivo;
        this.anexoDownloadUri = anexoDownloadUri;
        this.tipoConteudo = tipoConteudo;
        this.tamanhoArquivo = tamanhoArquivo;
    }
}
