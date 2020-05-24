package com.anthonini.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anthonini.brewer.model.Sale;
import com.anthonini.brewer.repository.helper.sale.SaleRepositoryQueries;

public interface SaleRepository extends JpaRepository<Sale, Long>, SaleRepositoryQueries {

}
