package br.com.rehem.rodrigo.controlepatrimonial.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Pessoa.
 */
@Entity
@Table(name = "pessoa")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SequenceGenerator(name = "pessoa_id_seq", allocationSize = 1, sequenceName = "pessoa_id_seq")
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_id_seq")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cadastro")
    private Integer cadastro;

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

    public Integer getCadastro() {
        return cadastro;
    }

    public void setCadastro(Integer cadastro) {
        this.cadastro = cadastro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pessoa pessoa = (Pessoa) o;
        if(pessoa.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, pessoa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pessoa{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", cadastro='" + cadastro + "'" +
            '}';
    }
}
