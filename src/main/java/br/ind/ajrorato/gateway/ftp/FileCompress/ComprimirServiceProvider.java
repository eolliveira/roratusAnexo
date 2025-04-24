package br.ind.ajrorato.gateway.ftp.FileCompress;

import br.ind.ajrorato.domain.model.enuns.TipoConteudo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComprimirServiceProvider {
    private final ApplicationContext applicationContext;

    public ComprimirArquivoService execute(TipoConteudo tipoConteudo) {
        return (ComprimirArquivoService) applicationContext.getBean(tipoConteudo.getServiceName());
    }
}