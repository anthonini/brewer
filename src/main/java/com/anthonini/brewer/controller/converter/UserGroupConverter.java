package com.anthonini.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.anthonini.brewer.model.UserGroup;

@Component
public class UserGroupConverter implements Converter<String, UserGroup> {

	@Override
	public UserGroup convert(String id) {
		if (!StringUtils.isEmpty(id)) {
			UserGroup userGroup = new UserGroup();
			userGroup.setId(Long.valueOf(id));
			
			return userGroup;
		}
		
		return null;
	}
}
