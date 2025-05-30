package br.ind.ajrorato.gateway.ftp;

import br.ind.ajrorato.config.ClientFtpConfig;
import br.ind.ajrorato.domain.exceptions.FileDownloadException;
import br.ind.ajrorato.domain.exceptions.FileRemoveException;
import br.ind.ajrorato.domain.exceptions.FileUploadException;
import br.ind.ajrorato.domain.model.Anexo;
import br.ind.ajrorato.domain.repositories.AnexoService;
import br.ind.ajrorato.gateway.ftp.FileCompress.ComprimirServiceProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AnexoServiceImpl implements AnexoService {
    private static final Logger logger = LoggerFactory.getLogger(AnexoServiceImpl.class);
    private final ClientFtpConfig clientFtpConfig;
    private final ComprimirServiceProvider comprimirServiceProvider;

    @Override
    public Anexo salvar(Anexo anexo, MultipartFile arquivo) throws FileUploadException {
        FTPClient clientFtp = clientFtpConfig.executar();

        try {
            InputStream inputStream = arquivo.getInputStream();
            clientFtp.setFileType(FTP.BINARY_FILE_TYPE);

            String id_nomeAnexo = anexo.getIdAnexo().toString() + "_" + anexo.getNomeArquivo();
            String diretorioPastas = anexo.getTipoAnexo()
                                     + "/" + anexo.getTipoConteudo()
                                     + "/" + LocalDateTime.now().getYear()
                                     + "/" + LocalDateTime.now().getMonthValue();

            String diretorioAnexo = diretorioPastas + "/" + id_nomeAnexo;

            FtpService.criaDiretorioInexistente(diretorioPastas, clientFtp);

            if (anexo.getTipoConteudo().getCompress()) {
                var servicoCompressao = comprimirServiceProvider.execute(anexo.getTipoConteudo());
                byte[] arquivoComprimido = servicoCompressao.execute(arquivo.getBytes());
                inputStream = new ByteArrayInputStream(arquivoComprimido);
            }

            boolean arquivoSalvo = clientFtp.storeFile(diretorioAnexo, inputStream);

            if (!arquivoSalvo)
                throw new FileUploadException("arquivo não foi salvo no servidor ftp");

            var mimeType = arquivo.getContentType().isBlank()
                    ? "application/octet-stream" : arquivo.getContentType();

            anexo.setNomeArquivo((id_nomeAnexo));
            anexo.setMimeType(mimeType);
            anexo.setDiretorioArquivoFtp(diretorioAnexo);
            anexo.setTamanhoArquivo(arquivo.getSize());

            logger.info("Upload realizado: " + anexo.getNomeArquivo() + " -> " + FtpService.converteByteParaUnidades(arquivo.getSize()));
            return anexo;
        } catch (IOException | FileUploadException e) {
            throw new FileUploadException("Falha ao salvar o arquivo [ " + anexo.getNomeArquivo() + " ] no servidor FTP: " + e.getMessage());
        } finally {
            clientFtpConfig.desconectar(clientFtp);
        }
    }

    @Override
    public Resource baixar(String diretorioArquivo) throws FileDownloadException {
        FTPClient clientFtp = clientFtpConfig.executar();

        String diretorioArquivoFtp = diretorioArquivo
                .replaceAll("%20", " ")
                .replaceAll("\\+", " ");

        String nomeArquivoFtp = diretorioArquivo
                .substring(diretorioArquivo.lastIndexOf("/") + 1)
                .replaceAll("%20", " ")
                .replaceAll("\\+", " ");

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            boolean recuperouArquivo = clientFtp.retrieveFile(diretorioArquivoFtp, outputStream);

            if (!recuperouArquivo)
                throw new FileUploadException("arquivo não encontrado.");

            logger.info("Download realizado: " + nomeArquivoFtp + " -> " + FtpService.converteByteParaUnidades(outputStream.size()));

            return new NamedByteArrayResource(outputStream.toByteArray(), nomeArquivoFtp);
        } catch (IOException | FileUploadException e) {
            throw new FileDownloadException("Falha ao baixar o arquivo [ " + nomeArquivoFtp + " ] do servidor FTP: " + e.getMessage());
        } finally {
            clientFtpConfig.desconectar(clientFtp);
        }
    }

    @Override
    public void apagar(String diretorioArquivo) throws FileRemoveException {
        FTPClient clientFtp = clientFtpConfig.executar();

        var diretorioArquivoFtp = diretorioArquivo
                .replaceAll("%20", " ")
                .replaceAll("\\+", " ");

        var nomeArquivo = diretorioArquivoFtp
                .substring(diretorioArquivoFtp.lastIndexOf("/") + 1)
                .replaceAll("%20", " ")
                .replaceAll("\\+", " ");

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            boolean recuperouArquivo = clientFtp.retrieveFile(diretorioArquivoFtp, outputStream);

            if (!recuperouArquivo)
                throw new FileRemoveException("arquivo não encontrado.");

            boolean removeuArquivo = clientFtp.deleteFile(diretorioArquivoFtp);

            if (!removeuArquivo)
                throw new FileRemoveException("arquivo não foi removido do servidor ftp.");

            logger.info("Arquivo removido: " + nomeArquivo + " -> " + FtpService.converteByteParaUnidades(outputStream.size()));

        } catch (IOException | FileRemoveException e) {
            throw new FileRemoveException("Falha ao remover o arquivo [ " + nomeArquivo + " ] do servidor FTP: " + e.getMessage());
        } finally {
            clientFtpConfig.desconectar(clientFtp);
        }
    }
}
