package com.anthonini.brewer.repository.helper.style;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anthonini.brewer.model.Style;
import com.anthonini.brewer.repository.filter.StyleFilter;

public interface StyleRepositoryQueries {

	public Page<Style> filter(StyleFilter filter, Pageable pageable);
}
