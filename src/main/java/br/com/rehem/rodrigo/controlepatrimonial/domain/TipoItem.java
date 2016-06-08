package br.com.rehem.rodrigo.controlepatrimonial.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TipoItem.
 */
@Entity
@Table(name = "tipo_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SequenceGenerator(name = "tipo_item_id_seq", allocationSize = 1, sequenceName = "tipo_item_id_seq")
public class TipoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_item_id_seq")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "sigla", length = 3, nullable = false)
    private String sigla;

    @Column(name = "descricao")
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoItem tipoItem = (TipoItem) o;
        if(tipoItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tipoItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TipoItem{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", sigla='" + sigla + "'" +
            ", descricao='" + descricao + "'" +
            '}';
    }
}
