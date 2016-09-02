package br.com.rehem.rodrigo.controlepatrimonial.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.com.rehem.rodrigo.controlepatrimonial.domain.enumeration.EstadoItem;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "item_id_seq", allocationSize = 1, sequenceName = "public.item_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    private Long id;

    @Column(name = "serial")
    private String serial;
    
    @Column(name = "tombo")
    private String tombo;
    
    @Column(name = "serial_chip")
    private String serialChip;

    @NotNull
    @Column(name = "modelo", nullable = false)
    private String modelo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoItem estado;

    @Column(name = "numero")
    private String numero;

    @ManyToOne
    private TipoItem tipoItem;

	@ManyToMany(mappedBy = "items")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Movimentacao> movimentacaos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
    
    public String getTombo() {
        return tombo;
    }

    public void setTombo(String tombo) {
        this.tombo = tombo;
    }

    public String getSerialChip() {
		return serialChip;
	}

	public void setSerialChip(String serialChip) {
		this.serialChip = serialChip;
	}
	
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public EstadoItem getEstado() {
        return estado;
    }

    public void setEstado(EstadoItem estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public Set<Movimentacao> getMovimentacaos() {
        return movimentacaos;
    }

    public void setMovimentacaos(Set<Movimentacao> movimentacaos) {
        this.movimentacaos = movimentacaos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        if(item.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + id +
            ", serial='" + serial + "'" +
            ", tombo='" + tombo + "'" +
            ", modelo='" + modelo + "'" +
            ", estado='" + estado + "'" +
            ", numero='" + numero + "'" +
            '}';
    }
}
