package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.LibroPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroPostgresRepository extends JpaRepository<LibroPostgres, Long> {
}
