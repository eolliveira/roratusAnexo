package br.ind.ajrorato.gateway.ftp;

import br.ind.ajrorato.domain.exceptions.DirectoryNotFoundException;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class FtpService {

    protected static void criaDiretorioInexistente(String remoteDirPath, FTPClient clientFtp) {
        try {
            String[] dirs = remoteDirPath.split("/");
            String currentDir = "";
            for (String dir : dirs) {
                if (dir.isEmpty()) continue;
                currentDir += "/" + dir;
                if (!clientFtp.changeWorkingDirectory(currentDir)) {
                    clientFtp.makeDirectory(currentDir);
                    clientFtp.changeWorkingDirectory(currentDir);
                }
            }

            clientFtp.changeWorkingDirectory("/");
        } catch (IOException e) {
            throw new DirectoryNotFoundException("Falha ao criar o diretório: " + e.getMessage());
        }
    }
}



