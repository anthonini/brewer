package com.anthonini.brewer.repository.helper.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anthonini.brewer.model.Client;
import com.anthonini.brewer.repository.filter.ClientFilter;

public interface ClientRepositoryQueries {

	public Page<Client> filter(ClientFilter filter, Pageable pageable);
}
