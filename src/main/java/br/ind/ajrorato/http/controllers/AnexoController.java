package br.ind.ajrorato.http.controllers;


import br.ind.ajrorato.http.response.UploadFileResponse;
import br.ind.ajrorato.usecases.SalvarArquivoFtpUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/anexo")
public class AnexoController {

    @Autowired
    private SalvarArquivoFtpUseCase salvarArquivoFtpUseCase;

    @PostMapping("/upload")
    public UploadFileResponse salvarArquivoFtp(@RequestParam("file") MultipartFile file,
                                               @RequestParam("tipoAnexo") String tipoAnexo) {
        String fileName = salvarArquivoFtpUseCase.execute(file);

        String anexoDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/anexo/download/")
                .path(fileName)
                .toUriString();


        return new UploadFileResponse(
                1L,
                "tipoAnexo",
                fileName,
                anexoDownloadUri,
                file.getContentType(),
                file.getSize()
        );

    }
}
