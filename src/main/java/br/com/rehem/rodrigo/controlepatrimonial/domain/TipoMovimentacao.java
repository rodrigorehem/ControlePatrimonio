package br.com.rehem.rodrigo.controlepatrimonial.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TipoMovimentacao.
 */
@Entity
@Table(name = "tipo_movimentacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoMovimentacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "tipo_movimentacao_id_seq", allocationSize = 1, sequenceName = "public.tipo_movimentacao_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_movimentacao_id_seq")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;
    
    @NotNull
    @Min(value=1L)
    @Max(value=2L)
    @Column(name="categoria", nullable=false)
    private Integer categoria;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Integer getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoMovimentacao tipoMovimentacao = (TipoMovimentacao) o;
        if(tipoMovimentacao.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tipoMovimentacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TipoMovimentacao{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", descricao='" + descricao + "'" +
            ", categoria='" + this.categoria + "'" + 
            '}';
    }
}
