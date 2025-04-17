package br.ind.ajrorato.gateway.ftp;

import br.ind.ajrorato.config.ClientFtpConfig;
import br.ind.ajrorato.domain.exceptions.FileUploadException;
import br.ind.ajrorato.domain.model.Anexo;
import br.ind.ajrorato.domain.repositories.AnexoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AnexoRepositoryImpl implements AnexoRepository {

    private final ClientFtpConfig clientFtpConfig;

    @Override
    public Anexo salvar(Anexo anexo, MultipartFile arquivo) {
        FTPClient clientFtp = clientFtpConfig.executar();

        try (InputStream inputStream = arquivo.getInputStream()) {
            clientFtp.setFileType(FTP.BINARY_FILE_TYPE);

            String id_nomeAnexo = anexo.getIdAnexo().toString() + "_" + anexo.getNomeArquivo();
            String diretorioPastas = anexo.getTipoAnexo()
                                     + "/" + anexo.getTipoConteudo()
                                     + "/" + LocalDateTime.now().getYear()
                                     + "/" + LocalDateTime.now().getMonthValue();

            String diretorioAnexo = diretorioPastas + "/" + id_nomeAnexo;

            FtpService.criaDiretorioInexistente(diretorioPastas, clientFtp);

            boolean arquivoSalvo = clientFtp.storeFile(diretorioAnexo, inputStream);

            if (!arquivoSalvo)
                throw new FileUploadException("conteudo não foi salvo no servidor ftp");

            anexo.setNomeArquivo((id_nomeAnexo));
            anexo.setDiretorioArquivoFtp(diretorioAnexo);
            anexo.setTamanhoArquivo(arquivo.getSize());

            return anexo;
        } catch (IOException | FileUploadException e) {
            throw new FileUploadException("Erro ao salvar o arquivo " + anexo.getNomeArquivo() + " no servidor FTP: " + e.getMessage());
        } finally {
            clientFtpConfig.desconectar(clientFtp);
        }
    }

    @Override
    public Resource baixar(String diretorioArquivo) {
        FTPClient clientFtp = clientFtpConfig.executar();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            boolean arquivoBaixado = clientFtp.retrieveFile(diretorioArquivo, outputStream);

            if (!arquivoBaixado) {
                throw new FileUploadException("arquivo não encontrado.");
            }

            String nomeArquivo = diretorioArquivo.substring(diretorioArquivo.lastIndexOf("/") + 1);

            return new NamedByteArrayResource(outputStream.toByteArray(), nomeArquivo);
            //return new ByteArrayResource(outputStream.toByteArray());

            //TODO(verificar essa excecao)
        } catch (Exception e) {
            throw new FileUploadException("Erro ao baixar o arquivo " + diretorioArquivo + " do servidor FTP: " + e.getMessage());
        } finally {
            clientFtpConfig.desconectar(clientFtp);
        }
    }

    @Override
    public Anexo apagar(Anexo arquivo) {
        return null;
    }
}
