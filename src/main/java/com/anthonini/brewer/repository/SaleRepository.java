package com.anthonini.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthonini.brewer.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long>{

}
