package br.ind.ajrorato.gateway.database.entity.anexo;

import br.ind.ajrorato.domain.model.enuns.TipoConteudo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SequenceGenerator(name = "SEQ_ANEXO", sequenceName = "SEQ_ANEXO", allocationSize = 1)
@Table(name = "ANEXO", uniqueConstraints = {@UniqueConstraint(columnNames = {"ID_ANEXO"})})
@EqualsAndHashCode(of = {"id"})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TP_ANEXO", length = 20)
public abstract class AnexoEntity {

    @Id
    @Column(name = "ID_ANEXO", length = 8)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_ANEXO")
    private Long id;

    @Column(name = "DS_ANEXO", length = 100)
    private String dsAnexo;

    @Lob
    @Column(name = "CONTEUDO")
    private byte[] conteudo;

    @Enumerated(EnumType.STRING)
    @Column(name = "TP_CONTEUDO", length = 20, nullable = false)
    private TipoConteudo tipoConteudo;

    @Column(name = "TP_ANEXO", length = 50, insertable = false, updatable = false)
    private String tipoAnexo;

    @Column(name = "NOME_ARQUIVO", length = 200)
    private String nomeArquivo;

    @Column(name = "TAMANHO_ARQUIVO")
    private Long tamanhoArquivo;

    @Column(name = "USUARIO_INCLUIU", length = 15)
    private String usuarioIncluiu;

    @Column(name = "URL_CONTEUDO")
    private String urlConteudo;

    @Column(name = "DIR_ARQUIVO_FTP")
    private String dirArquivoFtp;

}