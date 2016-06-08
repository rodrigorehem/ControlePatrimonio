package br.com.rehem.rodrigo.controlepatrimonial.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Documento.
 */
@Entity
@Table(name = "documento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SequenceGenerator(name = "documentoidseq", allocationSize = 1, sequenceName = "documentoidseq")
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documentoidseq")
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Lob
    @Column(name = "anexo", nullable = false)
    private byte[] anexo;

    @Column(name = "anexo_content_type", nullable = false)    
    private String anexoContentType;

    @ManyToOne
    private TipoDocumento tipoDocumento;

    @ManyToOne
    private Movimentacao movimentacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getAnexo() {
        return anexo;
    }

    public void setAnexo(byte[] anexo) {
        this.anexo = anexo;
    }

    public String getAnexoContentType() {
        return anexoContentType;
    }

    public void setAnexoContentType(String anexoContentType) {
        this.anexoContentType = anexoContentType;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Movimentacao getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(Movimentacao movimentacao) {
        this.movimentacao = movimentacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Documento documento = (Documento) o;
        if(documento.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, documento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Documento{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            ", anexo='" + anexo + "'" +
            ", anexoContentType='" + anexoContentType + "'" +
            '}';
    }
}
