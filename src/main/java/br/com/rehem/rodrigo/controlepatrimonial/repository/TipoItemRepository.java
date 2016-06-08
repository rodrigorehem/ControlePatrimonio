package br.com.rehem.rodrigo.controlepatrimonial.repository;

import br.com.rehem.rodrigo.controlepatrimonial.domain.TipoItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoItem entity.
 */
public interface TipoItemRepository extends JpaRepository<TipoItem,Long> {

}
