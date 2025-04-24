package br.ind.ajrorato.http.controllers;

import br.ind.ajrorato.config.SecurityConfig;
import br.ind.ajrorato.domain.model.Anexo;
import br.ind.ajrorato.domain.model.enuns.TipoConteudo;
import br.ind.ajrorato.http.response.SalvarAnexoResponse;
import br.ind.ajrorato.usecases.BaixarArquivoFtpUseCase;
import br.ind.ajrorato.usecases.RemoverArquivoFtpUseCase;
import br.ind.ajrorato.usecases.SalvarArquivoFtpUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/api/anexo")
@RequiredArgsConstructor
@Tag(name = "Controlador de arquivos")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AnexoController {
    private static final String EXAMPLE_PATH = "ACORDO/IMAGEM/2025/4/1_baixados.jpeg";

    private final SalvarArquivoFtpUseCase salvarArquivoFtpUseCase;
    private final BaixarArquivoFtpUseCase baixarArquivoFtpUseCase;
    private final RemoverArquivoFtpUseCase removerArquivoFtpUseCase;

    @Value("${url.service.anexoftp}")
    private String urlServidorFtp;

    @Value("${ftp.server.dir}")
    private String diretorioServidorFtp;

    @Operation(description = "Retorna o arquivo do servidor FTP")
    @ApiResponse(responseCode = "200", description = "Arquivo recuperado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "422", description = "Erro ao processar a requisição")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/preview")
    public ResponseEntity<Resource> visualizarAnexoFtp(
            @Parameter(description = "Diretório do arquivo no servidor FTP", example = EXAMPLE_PATH, required = true)
            @RequestParam(value = "path", required = false) String path, HttpServletRequest request) {
        Resource resource = baixarArquivoFtpUseCase.execute(path);

        String nomeArquivo = path.substring(path.lastIndexOf("/") + 1);
        String contentType = request.getServletContext().getMimeType(nomeArquivo);

        if (Objects.isNull(contentType))
            contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @Operation(description = "Realiza o download do arquivo.")
    @ApiResponse(responseCode = "200", description = "Arquivo recuperado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "422", description = "Erro ao processar a requisição")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/download")
    public ResponseEntity<Resource> baixarAnexoFtp(
            @Parameter(description = "Diretório do arquivo no servidor FTP", example = EXAMPLE_PATH, required = true)
            @RequestParam(value = "path", required = false) String path, HttpServletRequest request) {
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

    @Operation(description = "Salva o arquivo no servidor FTP")
    @ApiResponse(responseCode = "201", description = "Arquivo salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "413", description = "Arquivo muito grande")
    @ApiResponse(responseCode = "422", description = "Erro ao processar a requisição")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SalvarAnexoResponse> salvarAnexoFtp(
            @RequestPart(value = "arquivo")
            @Schema(type = "string", format = "binary")
            @RequestParam(value = "arquivo", required = false) MultipartFile arquivo,

            @Parameter(required = true, description = "ID do anexo associado ao arquivo", example = "0001")
            @RequestParam(value = "idAnexo", required = false) Long idAnexo,

            @Parameter(required = true, example = "ACORDO")
            @RequestParam(value = "tipoAnexo", required = false) String tipoAnexo,

            @Parameter(required = true)
            @RequestParam(value = "tipoConteudo", required = false) TipoConteudo tipoConteudo) {
        Anexo anexoSalvo = salvarArquivoFtpUseCase.execute(idAnexo, tipoAnexo, tipoConteudo.toString(), arquivo);

        String anexoPreviewUri = ServletUriComponentsBuilder.fromUri(URI.create(urlServidorFtp))
                .path("/api/anexo/preview")
                .queryParam("path", anexoSalvo.getDiretorioArquivoFtp())
                .toUriString();

        String anexoDownloadUri = ServletUriComponentsBuilder.fromUri(URI.create(urlServidorFtp))
                .path("/api/anexo/download")
                .queryParam("path", anexoSalvo.getDiretorioArquivoFtp())
                .toUriString();

        return ResponseEntity.status(HttpStatus.CREATED).body(SalvarAnexoResponse.builder()
                .idAnexo(anexoSalvo.getIdAnexo())
                .nomeArquivo(anexoSalvo.getNomeArquivo())
                .mimeType(anexoSalvo.getMimeType())
                .tipoAnexo(anexoSalvo.getTipoAnexo())
                .tipoConteudo(anexoSalvo.getTipoConteudo().toString())
                .diretorioAnexoFtp(anexoSalvo.getDiretorioArquivoFtp())
                .urlAnexoFtp("ftp://" + diretorioServidorFtp + "/" + anexoSalvo.getDiretorioArquivoFtp())
                .uriAnexoPreview(anexoPreviewUri)
                .uriAnexoDownload(anexoDownloadUri)
                .tamanhoArquivo(anexoSalvo.getTamanhoArquivo())
                .build());
    }

    @Operation(description = "Remove o arquivo do servidor FTP")
    @ApiResponse(responseCode = "204", description = "Arquivo removido com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "422", description = "Erro ao processar a requisição")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> removerAnexoFtp(
            @Parameter(description = "Diretório do arquivo no servidor FTP", example = EXAMPLE_PATH, required = true)
            @RequestParam(value = "path", required = false) String path) {
        removerArquivoFtpUseCase.execute(path);
        return ResponseEntity.noContent().build();
    }

}