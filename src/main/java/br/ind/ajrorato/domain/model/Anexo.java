package br.ind.ajrorato.domain.model;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class Anexo {
    private Long idAnexo;
    private String tipoAnexo;
    private String tipoConteudo;
    private String nomeArquivo;
    private String urlArquivoFtp;
    private String uriArquivoHttp;
    private Long tamanhoArquivo;

    public Anexo(Long idAnexo, String nomeArquivo, String tipoAnexo, String tipoConteudo) {
        validarAnexo(idAnexo, tipoAnexo, tipoConteudo, nomeArquivo);
        this.idAnexo = idAnexo;
        this.nomeArquivo = StringUtils.cleanPath(nomeArquivo);
        this.tipoAnexo = tipoAnexo;
        this.tipoConteudo = tipoConteudo;
    }

    private void validarAnexo(Long idAnexo, String tipoAnexo, String tipoConteudo, String nomeArquivo) {

        if (Objects.isNull(nomeArquivo) || nomeArquivo.isEmpty()) {
            throw new IllegalArgumentException("Nome do arquivo inválido: " + nomeArquivo);
        } else if (StringUtils.cleanPath(nomeArquivo).contains("..")) {
            throw new IllegalArgumentException("Nome do arquivo inválido: " + StringUtils.cleanPath(nomeArquivo));
            //TODO(Criar uma variavel de ambiente para definir o tamanho maximo do nome do arquivo)
        } else if (StringUtils.cleanPath(nomeArquivo).length() > 100) {
            throw new IllegalArgumentException("Nome do arquivo muito longo: " + StringUtils.cleanPath(nomeArquivo) + " (máximo 100 caracteres)");
        }

        if (Objects.isNull(idAnexo) || idAnexo <= 0) {
            throw new IllegalArgumentException("ID do anexo inválido.");
        }
        if (tipoAnexo == null || tipoAnexo.isEmpty()) {
            throw new IllegalArgumentException("Tipo de anexo inválido.");
        }
        if (Objects.isNull(tipoConteudo) || tipoConteudo.isEmpty()) {
            throw new IllegalArgumentException("Tipo de conteúdo inválido.");
        }
    }

    public Long getIdAnexo() {
        return idAnexo;
    }

    public String getTipoAnexo() {
        return tipoAnexo;
    }

    public String getTipoConteudo() {
        return tipoConteudo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getUrlArquivoFtp() {
        return urlArquivoFtp;
    }

    public void setUrlArquivoFtp(String urlArquivoFtp) {
        this.urlArquivoFtp = urlArquivoFtp;
    }

    public String getUriArquivoHttp() {
        return uriArquivoHttp;
    }

    public void setUriArquivoHttp(String uriArquivoHttp) {
        this.uriArquivoHttp = uriArquivoHttp;
    }

    public Long getTamanhoArquivo() {
        return tamanhoArquivo;
    }

    public void setTamanhoArquivo(Long tamanhoArquivo) {
        this.tamanhoArquivo = tamanhoArquivo;
    }

}
