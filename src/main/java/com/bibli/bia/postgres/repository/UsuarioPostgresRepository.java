package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.UsuarioPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioPostgresRepository extends JpaRepository<UsuarioPostgres, Long> {
    Optional<UsuarioPostgres> findByUsername(String username);
}