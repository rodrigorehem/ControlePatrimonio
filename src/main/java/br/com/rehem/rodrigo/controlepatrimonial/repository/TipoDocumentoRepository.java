package br.com.rehem.rodrigo.controlepatrimonial.repository;

import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoDocumento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoDocumento entity.
 */
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento,Long> {

}
