package br.ind.ajrorato.http.controllers;


import br.ind.ajrorato.domain.model.Anexo;
import br.ind.ajrorato.http.response.SalvarAnexoResponse;
import br.ind.ajrorato.usecases.BaixarArquivoFtpUseCase;
import br.ind.ajrorato.usecases.SalvarArquivoFtpUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/anexo")
@RequiredArgsConstructor
public class AnexoController {
    private final SalvarArquivoFtpUseCase salvarArquivoFtpUseCase;
    private final BaixarArquivoFtpUseCase baixarArquivoFtpUseCase;

    @Value("${ftp.server.dir}")
    private String diretorioServidorFtp;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AnexoController.class);

    @GetMapping("/preview")
    public ResponseEntity<Resource> baixarAnexoFtp(@RequestParam("path") String path, HttpServletRequest request) {

        String nomeArquivo = path.substring(path.lastIndexOf("/") + 1);

        Resource resource = baixarArquivoFtpUseCase.execute(path);

        String contentType = request.getServletContext().getMimeType(nomeArquivo);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        //logger.info("Arquivo baixado: " + path);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                .body(resource);
    }

//    @GetMapping("/download")
//    public ResponseEntity<Resource> baixarAnexoFtp(@RequestParam("diretorioArquivo") String diretorioArquivo, HttpServletRequest request) throws IOException {
//
//        String nomeArquivo = diretorioArquivo.substring(diretorioArquivo.lastIndexOf("/") + 1);
//
//        Resource resource = baixarArquivoFtpUseCase.execute(diretorioArquivo);
//
//        String contentType = request.getServletContext().getMimeType(nomeArquivo);
//
//        if (contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        logger.info("Arquivo baixado: " + diretorioArquivo);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
//                .body(resource);
//    }


    @PostMapping("/upload")
    public SalvarAnexoResponse salvarAnexoFtp(@RequestParam("file") MultipartFile file,
                                              @RequestParam("idAnexo") Long idAnexo,
                                              @RequestParam("tipoAnexo") String tipoAnexo,
                                              @RequestParam("tipoConteudo") String tipoConteudo) {

        Anexo anexoSalvo = salvarArquivoFtpUseCase.execute(idAnexo, tipoAnexo, tipoConteudo, file);

        String anexoDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/anexo/preview")
                .queryParam("path",anexoSalvo.getDiretorioArquivoFtp())
                .toUriString();

        return new SalvarAnexoResponse(
                anexoSalvo.getIdAnexo(),
                anexoSalvo.getNomeArquivo(),
                anexoSalvo.getTipoAnexo(),
                anexoSalvo.getTipoConteudo(),
                "ftp://" + diretorioServidorFtp + "/" + anexoSalvo.getDiretorioArquivoFtp(),
                anexoDownloadUri,
                anexoSalvo.getTamanhoArquivo()
        );
    }
}
