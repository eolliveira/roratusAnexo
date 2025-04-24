package br.ind.ajrorato.domain.model.enuns;

public enum TipoConteudo {
    IMAGEM("comprimirImagem", true),
    VIDEO("comprimirVideo", false),
    TEXTO("comprimirTexto", false),
    DOCUMENTO("comprimirDocumento", false),
    PLANILHA("comprimirPlanilha", false),
    APRESENTACAO("comprimirApresentacao", false),
    PDF("comprimirPdf", true),
    HTML("comprimirHtml", false),
    XML("comprimirXml", false),
    ZIP("comprimirZip", false),
    RAR("comprimirRar", false),
    DAX("comprimirDax", false),
    CPX("comprimirCpx", false),
    ADX("comprimirAdx", false),
    PAX("comprimirPax", false),
    OUTROS("comprimirOutros", false);

    private final String serviceName;
    private final boolean compress;

    TipoConteudo(String serviceName, boolean compress) {
        this.serviceName = serviceName;
        this.compress = compress;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean getCompress() {
        return compress;
    }

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