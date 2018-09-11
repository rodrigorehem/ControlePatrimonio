package br.com.rehem.rodrigo.controlepatrimonial.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UnidadeJudiciaria.
 */
@Entity
@Table(name = "unidade_judiciaria")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnidadeJudiciaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "unidade_judiciaria_id_seq", allocationSize = 1, sequenceName = "unidade_judiciaria_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unidade_judiciaria_id_seq")
    private Long id;

    @NotNull
    @Pattern(regexp = "[0-9]+")
    @Column(name = "coj", nullable = false)
    private String coj;

    @NotNull
    @Column(name = "comarca", nullable = false)
    private String comarca;

    @NotNull
    @Column(name = "unidade", nullable = false)
    private String unidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoj() {
        return coj;
    }

    public void setCoj(String coj) {
        this.coj = coj;
    }

    public String getComarca() {
        return comarca;
    }

    public void setComarca(String comarca) {
        this.comarca = comarca;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnidadeJudiciaria unidadeJudiciaria = (UnidadeJudiciaria) o;
        if(unidadeJudiciaria.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, unidadeJudiciaria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UnidadeJudiciaria{" +
            "id=" + id +
            ", coj='" + coj + "'" +
            ", comarca='" + comarca + "'" +
            ", unidade='" + unidade + "'" +
            '}';
    }
}
