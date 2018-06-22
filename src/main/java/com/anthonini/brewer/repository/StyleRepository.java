package com.anthonini.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anthonini.brewer.model.Style;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {

	public Optional<Style> findByNameIgnoreCase(String name);
}
