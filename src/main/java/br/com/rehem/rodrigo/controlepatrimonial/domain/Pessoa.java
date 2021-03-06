package br.com.rehem.rodrigo.controlepatrimonial.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.rehem.rodrigo.controlepatrimonial.domain.enumeration.CategoriaFuncional;

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
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "pessoa_id_seq", allocationSize = 1, sequenceName = "public.pessoa_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_id_seq")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cadastro")
    private Integer cadastro;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_funcional", nullable = false)
    private CategoriaFuncional categoriaFuncional;

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

    public CategoriaFuncional getCategoriaFuncional() {
		return categoriaFuncional;
	}

	public void setCategoriaFuncional(CategoriaFuncional categoriaFuncional) {
		this.categoriaFuncional = categoriaFuncional;
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
            ", categoriaFuncional='" + categoriaFuncional + "'" +
            '}';
    }
}
