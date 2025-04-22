package br.ind.ajrorato.domain.model.enuns;

public enum TipoAnexo {
    ACORDO, MENSAGEM_EMAIL;

    public static TipoAnexo converterString(String tipoAnexo) {
        try {
            return TipoAnexo.valueOf(tipoAnexo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo [" + tipoAnexo + "] inválido" + ". Tipos válidos: " + tiposValidos(), e.getCause());
        }
    }

    private static String tiposValidos() {
        StringBuilder tipos = new StringBuilder();
        for (TipoAnexo tipo : TipoAnexo.values()) {
            if (!tipos.isEmpty()) {
                tipos.append(", ");
            }
            tipos.append(tipo.name());
        }
        return tipos.toString();
    }
}