package br.com.rehem.rodrigo.controlepatrimonial.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Movimentacao.
 */
@Entity
@Table(name = "movimentacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Movimentacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "movimentacao_id_seq", allocationSize = 1, sequenceName = "public.movimentacao_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimentacao_id_seq")
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @ManyToOne
    private TipoMovimentacao tipoMovimentacao;

    @OneToMany(mappedBy = "movimentacao")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Documento> documentos = new HashSet<>();

    @ManyToOne
    private Pessoa pessoa;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "movimentacao_item",
               joinColumns = @JoinColumn(name="movimentacaos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="items_id", referencedColumnName="ID"))
    private Set<Item> items = new HashSet<>();

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

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public Set<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movimentacao movimentacao = (Movimentacao) o;
        if(movimentacao.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, movimentacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            ", data='" + data + "'" +
            '}';
    }
}
