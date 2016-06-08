package br.com.rehem.rodrigo.controlepatrimonial.repository;

import br.com.rehem.rodrigo.controlepatrimonial.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
