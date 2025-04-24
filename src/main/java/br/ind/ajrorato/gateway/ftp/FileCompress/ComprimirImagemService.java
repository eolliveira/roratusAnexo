package br.ind.ajrorato.gateway.ftp.FileCompress;

import br.ind.ajrorato.domain.exceptions.FileCompressionException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

@Component("comprimirImagem")
@RequiredArgsConstructor
public class ComprimirImagemService implements ComprimirArquivoService {
    private static final Logger logger = LoggerFactory.getLogger(ComprimirImagemService.class);
    private final static String SIZE_IMAGE = "150";

    @Override
    public byte[] execute(byte[] arquivo) {
        try {
            var tamanhoArquivo = arquivo.length;

            if (tamanhoArquivo > Integer.parseInt(SIZE_IMAGE))
                tamanhoArquivo = tamanhoArquivo / Integer.parseInt(SIZE_IMAGE);

            if (tamanhoArquivo < Integer.parseInt(SIZE_IMAGE))
                return arquivo;

            BufferedImage imagem = ImageIO.read(new ByteArrayInputStream(arquivo));
            ByteArrayOutputStream byteArrayOs = new ByteArrayOutputStream();

            ImageInputStream imageInputStream = ImageIO.createImageInputStream(new ByteArrayInputStream(arquivo));
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);

            var formatoImagem = "";
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                formatoImagem = reader.getFormatName();
            }

            if ((!formatoImagem.equalsIgnoreCase("PNG") &&
                 (!formatoImagem.equalsIgnoreCase("JPG") &&
                  (!formatoImagem.equalsIgnoreCase("JPEG"))))) {
                throw new FileCompressionException("formato não é suportado. Formatos suportados: PNG, JPG e JPEG.");
            }

            BufferedImage result;
            if (formatoImagem.equalsIgnoreCase("PNG")) {
                result = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_RGB);
                result.createGraphics().drawImage(imagem, 0, 0, Color.WHITE, null);
                imagem = result;
            }

            ImageWriter writer = ImageIO.getImageWritersByFormatName("JPG").next();
            ImageOutputStream imageOs = ImageIO.createImageOutputStream(byteArrayOs);
            writer.setOutput(imageOs);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.25f);

            writer.write(null, new IIOImage(imagem, null, null), param);

            var conteudoComprimido = byteArrayOs.toByteArray();
            var tamanhoComprimido = conteudoComprimido.length / Integer.parseInt(SIZE_IMAGE);
            logger.info("--> Imagem comprimida. Tamanho original: " + tamanhoArquivo + " / Tamanho comprimido: " + tamanhoComprimido);

            byteArrayOs.close();
            imageOs.close();
            writer.dispose();
            return conteudoComprimido;
        } catch (Exception e) {
            throw new FileCompressionException("Erro ao comprimir imagem: " + e.getMessage());
        }
    }
}
