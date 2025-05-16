package br.ind.ajrorato.domain.model;

import br.ind.ajrorato.domain.model.enuns.TipoConteudo;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.Objects;
import java.util.regex.Pattern;

public class Anexo {
    private Long idAnexo;
    private String tipoAnexo;
    private TipoConteudo tipoConteudo;
    private String mimeType;
    private String nomeArquivo;
    private String diretorioArquivoFtp;
    private Long tamanhoArquivo;

    public Anexo(Long idAnexo, String nomeArquivo, String tipoAnexo, String tipoConteudo) {
        validarAnexo(idAnexo, tipoAnexo, tipoConteudo, nomeArquivo);
        this.idAnexo = idAnexo;
        this.nomeArquivo = StringUtils.cleanPath(removerAcentos(nomeArquivo));
        this.tipoAnexo = tipoAnexo;
        this.tipoConteudo = TipoConteudo.converterString(tipoConteudo);
    }

    private void validarAnexo(Long idAnexo, String tipoAnexo, String tipoConteudo, String nomeArquivo) {

        if (Objects.isNull(nomeArquivo) || nomeArquivo.isEmpty()) {
            throw new IllegalArgumentException("Nome do arquivo ausente ou inválido: " + nomeArquivo);
        } else if (StringUtils.cleanPath(nomeArquivo).contains("..")) {
            throw new IllegalArgumentException("Nome do arquivo inválido: " + StringUtils.cleanPath(nomeArquivo));
        } else if (StringUtils.cleanPath(nomeArquivo).length() > 100) {
            throw new IllegalArgumentException("Nome do arquivo muito longo: " + StringUtils.cleanPath(nomeArquivo) + " (máximo 100 caracteres)");
        }

        if (Objects.isNull(idAnexo) || idAnexo <= 0) {
            throw new IllegalArgumentException("ID do anexo ausente ou inválido.");
        }

        if (tipoAnexo == null || tipoAnexo.isEmpty()) {
            throw new IllegalArgumentException("Tipo de anexo ausente ou inválido.");
        }

        if (Objects.isNull(tipoConteudo) || tipoConteudo.isEmpty()) {
            throw new IllegalArgumentException("Tipo do conteúdo ausente ou inválido.");
        }
    }

    private String removerAcentos(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizado).replaceAll("").replaceAll("[´`^~¨]", "");
    }

    public Long getIdAnexo() {
        return idAnexo;
    }

    public String getTipoAnexo() {
        return tipoAnexo;
    }

    public TipoConteudo getTipoConteudo() {
        return tipoConteudo;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getDiretorioArquivoFtp() {
        return diretorioArquivoFtp;
    }

    public void setDiretorioArquivoFtp(String urlArquivoFtp) {
        this.diretorioArquivoFtp = urlArquivoFtp;
    }

    public Long getTamanhoArquivo() {
        return tamanhoArquivo;
    }

    public void setTamanhoArquivo(Long tamanhoArquivo) {
        this.tamanhoArquivo = tamanhoArquivo;
    }

}
