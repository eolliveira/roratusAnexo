package br.ind.ajrorato.config;

import br.ind.ajrorato.domain.exceptions.FtpClientConfigurationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ClientFtpConfig {

    @Value("${ftp.server.dir}")
    private String diretorioServidorFtp;

    @Value("${ftp.server.port}")
    private Integer portaServidorFtp;

    @Value("${ftp.server.user}")
    private String usuarioServidorFtp;

    @Value("${ftp.server.password}")
    private String senhaServidorFtp;


    public FTPClient executar() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(diretorioServidorFtp, portaServidorFtp);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                throw new FtpClientConfigurationException(("Não foi possível conectar com o servidor ftp. code: " +
                                                           ftpClient.getReplyCode()));
            }
            boolean success = ftpClient.login(usuarioServidorFtp, senhaServidorFtp);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode()) && !success) {
                throw new FtpClientConfigurationException(("Não foi possível conectar com o servidor ftp. code: " +
                                                           ftpClient.getReplyCode()));
            }
            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                throw new FtpClientConfigurationException(ex);
            }
            throw new FtpClientConfigurationException(e);
        }
        return ftpClient;
    }

    public void desconectar(FTPClient ftpClient) {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            throw new FtpClientConfigurationException(ex);
        }
    }


}
