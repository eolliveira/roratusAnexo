package br.ind.ajrorato.domain.model.enuns;

public enum TipoConteudo {
    IMAGEM,
    VIDEO,
    TEXTO,
    DOCUMENTO,
    PLANILHA,
    APRESENTACAO,
    PDF,
    HTML,
    XML,
    ZIP,
    RAR,
    DAX,
    CPX,
    AD,
    PAX,
    OUTROS;

    public static TipoConteudo converterString(String tipoAnexo) {
        try {
            return TipoConteudo.valueOf(tipoAnexo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo Conteudo [" + tipoAnexo + "] inválido" + ". Tipos válidos: " + tiposValidos(), e.getCause());
        }
    }

    public static String tiposValidos() {
        StringBuilder tipos = new StringBuilder();
        for (TipoConteudo tipo : TipoConteudo.values()) {
            if (!tipos.isEmpty()) {
                tipos.append(", ");
            }
            tipos.append(tipo.name());
        }
        return tipos.toString();
    }
}