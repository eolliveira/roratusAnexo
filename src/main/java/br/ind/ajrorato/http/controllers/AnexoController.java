package br.ind.ajrorato.http.controllers;


import br.ind.ajrorato.domain.model.Anexo;
import br.ind.ajrorato.http.response.SalvarAnexoResponse;
import br.ind.ajrorato.usecases.BaixarArquivoFtpUseCase;
import br.ind.ajrorato.usecases.SalvarArquivoFtpUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/api/anexo")
@RequiredArgsConstructor
public class AnexoController {
    private final SalvarArquivoFtpUseCase salvarArquivoFtpUseCase;
    private final BaixarArquivoFtpUseCase baixarArquivoFtpUseCase;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AnexoController.class);

    @GetMapping("/download/{nomeArquivo:.+}")
    public ResponseEntity<Resource> baixarAnexoFtp(@PathVariable("nomeArquivo") String nomeArquivo, HttpServletRequest request) throws IOException {

        Resource resource = baixarArquivoFtpUseCase.execute(nomeArquivo);

        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        logger.info("Arquivo baixado: " + nomeArquivo);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @PostMapping("/upload")
    public SalvarAnexoResponse salvarAnexoFtp(@RequestParam("file") MultipartFile file,
                                              @RequestParam("idAnexo") Long idAnexo,
                                              @RequestParam("tipoAnexo") String tipoAnexo,
                                              @RequestParam("tipoConteudo") String tipoConteudo) {

        Anexo anexoSalvo = salvarArquivoFtpUseCase.execute(idAnexo, tipoAnexo, tipoConteudo, file);

        String anexoDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/anexo/download/")
                .path(anexoSalvo.getNomeArquivo())
                .toUriString();

        return new SalvarAnexoResponse(
                anexoSalvo.getIdAnexo(),
                anexoSalvo.getNomeArquivo(),
                anexoSalvo.getTipoAnexo(),
                anexoSalvo.getTipoConteudo(),
                anexoSalvo.getUrlArquivoFtp(),
                anexoDownloadUri,
                anexoSalvo.getTamanhoArquivo()
        );
    }
}
