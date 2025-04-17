package br.ind.ajrorato.gateway.ftp;

public class NamedByteArrayResource extends org.springframework.core.io.ByteArrayResource {

    private final String name;

    public NamedByteArrayResource(byte[] byteArray, String name) {
        super(byteArray);
        this.name = name;
    }

    @Override
    public String getFilename() {
        return name;
    }
}
