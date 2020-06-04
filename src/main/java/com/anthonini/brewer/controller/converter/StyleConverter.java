package com.anthonini.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.model.Style;

@Component
public class StyleConverter implements Converter<String, Style> {

	@Override
	public Style convert(String id) {
		if (!StringUtils.isEmpty(id)) {
			Style style = new Style();
			style.setId(Long.valueOf(id));
			
			return style;
		}
		
		return null;
	}

}
