package br.ind.ajrorato.jobs;

import br.ind.ajrorato.config.ClientFtpConfig;
import br.ind.ajrorato.gateway.database.entity.anexo.AnexoEntity;
import br.ind.ajrorato.gateway.database.repository.AnexoRepository;
import br.ind.ajrorato.gateway.ftp.FtpService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferirAnexosParaServidorFtp {
    private static final String TP_ANEXO = "SOLICITACAO_HELPDESK";

    private Long QTD_ANEXOS_TRANSFERIDOS = 0L;
    private final List<Long> IDS_ANEXOS_NAO_TRANSFERIDOS = new ArrayList<>();
    private final AnexoRepository anexoRepository;
    private final ClientFtpConfig clientFtpConfig;

    @Value("${url.service.anexoftp}")
    private String urlServidorFtp;

    @Transactional
    //@Scheduled(initialDelay = 1000 * 5)
    public void execute() {
        var ftpClient = clientFtpConfig.executar();
        try {
            log.info("Iniciando transferência de anexos tipo [" + TP_ANEXO + "] para o servidor FTP");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            List<AnexoEntity> anexosTransferidos = new ArrayList<>();

            List<AnexoEntity> anexos = anexoRepository.findAllByTipoAnexoAndDirArquivoFtpIsNull(TP_ANEXO);

            if (!anexos.isEmpty()) {
                anexos.forEach(anexo -> {
                    if (Objects.isNull(anexo.getConteudo()) || anexo.getConteudo().length == 0) {
                        IDS_ANEXOS_NAO_TRANSFERIDOS.add(anexo.getId());
                        log.warn("Anexo id: {" + anexo.getId() + "} não possui conteudo para ser transferido");
                    } else {
                        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(anexo.getConteudo())) {

                            String id_nomeAnexo = anexo.getId().toString() + "_" + removerAcentos(anexo.getNomeArquivo());
                            String diretorioPastas = anexo.getTipoAnexo()
                                                     + "/" + anexo.getTipoConteudo()
                                                     + "/" + LocalDateTime.now().getYear()
                                                     + "/" + LocalDateTime.now().getMonthValue();

                            String diretorioAnexo = diretorioPastas + "/" + id_nomeAnexo;
                            FtpService.criaDiretorioInexistente(diretorioPastas, ftpClient);

                            boolean arquivoSalvo = ftpClient.storeFile(diretorioAnexo, inputStream);

                            if (!arquivoSalvo) {
                                IDS_ANEXOS_NAO_TRANSFERIDOS.add(anexo.getId());
                                log.error("Anexo id: {" + anexo.getId() + "} não foi salvo no servidor FTP");
                                throw new RuntimeException("Erro ao salvar o arquivo no servidor FTP");
                            }

                            anexo.setDirArquivoFtp(diretorioAnexo);
                            anexo.setUrlConteudo(ServletUriComponentsBuilder.fromUri(URI.create(urlServidorFtp))
                                    .path("/api/anexo/recovery")
                                    .queryParam("path", diretorioAnexo)
                                    .toUriString());

                            anexosTransferidos.add(anexo);
                        } catch (IOException e) {
                            log.error("Erro ao transferir anexo id: {" + anexo.getId() + "} para o servidor FTP: {}", e.getMessage());
                        }

                        QTD_ANEXOS_TRANSFERIDOS++;
                        log.info("Conteudo anexo id: {" + anexo.getId() + "} foi transferido para o servidor FTP com sucesso");
                    }
                });
            }

            anexoRepository.saveAll(anexosTransferidos);
            log.info("Transferência de anexos finalizada. Total de anexos transferidos: " + QTD_ANEXOS_TRANSFERIDOS);

            if (!IDS_ANEXOS_NAO_TRANSFERIDOS.isEmpty()) {
                System.out.println("----------------------------------------------------------------------------------");
                System.out.println("Ids anexos não transferidos: " + IDS_ANEXOS_NAO_TRANSFERIDOS.size());
                IDS_ANEXOS_NAO_TRANSFERIDOS.forEach(id -> System.out.println("Id anexo não transferido: " + id));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao transferir anexos para o servidor FTP: " + e.getMessage());
        } finally {
            clientFtpConfig.desconectar(ftpClient);
        }
    }

    private static String removerAcentos(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizado).replaceAll("").replaceAll("[´`^~¨]", "");
    }
}
