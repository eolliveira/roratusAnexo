package br.ind.ajrorato.gateway.ftp;

import br.ind.ajrorato.domain.exceptions.FtpDirectoryCreationException;
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
            throw new FtpDirectoryCreationException("Erro ao criar diretório: " + remoteDirPath, e);
        }
    }

    protected static String converteByteParaUnidades(long size) {
        String[] unidades = {"B", "KB", "MB", "GB", "TB"};
        int indiceUnidade = 0;
        double tamanhoArquivo = size;

        while (tamanhoArquivo >= 1024 && indiceUnidade < unidades.length - 1) {
            tamanhoArquivo /= 1024;
            indiceUnidade++;
        }

        return String.format("%.1f %s", tamanhoArquivo, unidades[indiceUnidade]);
    }
}



