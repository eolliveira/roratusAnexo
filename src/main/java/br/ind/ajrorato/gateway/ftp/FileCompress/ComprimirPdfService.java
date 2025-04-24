package br.ind.ajrorato.gateway.ftp.FileCompress;

import br.ind.ajrorato.domain.exceptions.FileCompressionException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

@Component("comprimirPdf")
@RequiredArgsConstructor
public class ComprimirPdfService implements ComprimirArquivoService {
    private final Logger log = Logger.getLogger(ComprimirArquivoService.class.getName());

    @Override
    public byte[] execute(byte[] arquivo) {
        try {
            ByteArrayOutputStream baos = null;
            PdfStamper stamper = null;
            try {
                PdfReader reader = new PdfReader(arquivo);
                baos = new ByteArrayOutputStream();
                stamper = new PdfStamper(reader, baos);
                stamper.setFullCompression();

            } catch (IOException | DocumentException ee) {
                log.warning(ee.getMessage());
            } finally {
                try {
                    stamper.close();
                } catch (DocumentException | IOException ee) {
                    log.warning(ee.getMessage());
                }
            }
            return Objects.isNull(baos) ? arquivo : baos.toByteArray();
        } catch (Exception e) {
            throw new FileCompressionException("Erro ao comprimir pdf: " + e.getMessage());
        }
    }
}
