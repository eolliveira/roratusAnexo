package br.ind.ajrorato.http.controllers;


import br.ind.ajrorato.domain.model.Anexo;
import br.ind.ajrorato.http.response.SalvarAnexoResponse;
import br.ind.ajrorato.usecases.BaixarArquivoFtpUseCase;
import br.ind.ajrorato.usecases.RemoverArquivoFtpUseCase;
import br.ind.ajrorato.usecases.SalvarArquivoFtpUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@RestController
@RequestMapping("/api/anexo")
@RequiredArgsConstructor
public class AnexoController {
    private final SalvarArquivoFtpUseCase salvarArquivoFtpUseCase;
    private final BaixarArquivoFtpUseCase baixarArquivoFtpUseCase;
    private final RemoverArquivoFtpUseCase removerArquivoFtpUseCase;

    @Value("${ftp.server.dir}")
    private String diretorioServidorFtp;

    @GetMapping("/preview")
    public ResponseEntity<Resource> visualizarAnexoFtp(@RequestParam(value = "path", required = false) String path, HttpServletRequest request) {
        Resource resource = baixarArquivoFtpUseCase.execute(path);

        String nomeArquivo = path.substring(path.lastIndexOf("/") + 1);
        String contentType = request.getServletContext().getMimeType(nomeArquivo);

        if (Objects.isNull(contentType))
            contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> baixarAnexoFtp(@RequestParam(value = "path", required = false) String path, HttpServletRequest request) {
        Resource resource = baixarArquivoFtpUseCase.execute(path);

        String nomeArquivo = path.substring(path.lastIndexOf("/") + 1);
        String contentType = request.getServletContext().getMimeType(nomeArquivo);

        if (Objects.isNull(contentType))
            contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                .body(resource);
    }


    @PostMapping("/upload")
    public ResponseEntity<SalvarAnexoResponse> salvarAnexoFtp(@RequestParam(value = "arquivo", required = false) MultipartFile arquivo,
                                              @RequestParam(value = "idAnexo", required = false) Long idAnexo,
                                              @RequestParam(value = "tipoAnexo", required = false) String tipoAnexo,
                                              @RequestParam(value = "tipoConteudo", required = false) String tipoConteudo) {

        Anexo anexoSalvo = salvarArquivoFtpUseCase.execute(idAnexo, tipoAnexo, tipoConteudo, arquivo);

        String anexoPreviewUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/anexo/preview")
                .queryParam("path",anexoSalvo.getDiretorioArquivoFtp())
                .toUriString();

        String anexoDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/anexo/download")
                .queryParam("path",anexoSalvo.getDiretorioArquivoFtp())
                .toUriString();

        String anexoDeleteUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/anexo/delete")
                .queryParam("path",anexoSalvo.getDiretorioArquivoFtp())
                .toUriString();

        return ResponseEntity.status(HttpStatus.CREATED).body(SalvarAnexoResponse.builder()
                        .idAnexo(anexoSalvo.getIdAnexo())
                        .nomeArquivo(anexoSalvo.getNomeArquivo())
                        .mimeType(anexoSalvo.getMimeType())
                        .tipoAnexo(anexoSalvo.getTipoAnexo().toString())
                        .tipoConteudo(anexoSalvo.getTipoConteudo())
                        .urlAnexoFtp("ftp://" + diretorioServidorFtp + "/" + anexoSalvo.getDiretorioArquivoFtp())
                        .uriAnexoPreview(anexoPreviewUri)
                        .uriAnexoDownload(anexoDownloadUri)
                        .uriAnexoDelete(anexoDeleteUri)
                        .tamanhoArquivo(anexoSalvo.getTamanhoArquivo())
                        .build());

    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> removerAnexoFtp(@RequestParam(value = "path", required = false) String path) {
        removerArquivoFtpUseCase.execute(path);
        return ResponseEntity.noContent().build();
    }

}
