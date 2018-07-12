package com.anthonini.brewer.model.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import com.anthonini.brewer.model.Client;

public class ClientGroupSequenceProvider implements DefaultGroupSequenceProvider<Client>  {

	@Override
	public List<Class<?>> getValidationGroups(Client client) {
		List<Class<?>> groups = new ArrayList<Class<?>>();
		groups.add(Client.class);
		
		if(isPersonTypeSelected(client)) {
			groups.add(client.getPersonType().getGroup());
		}
		
		return groups;
	}

	private boolean isPersonTypeSelected(Client client) {
		return client != null && client.getPersonType() != null;
	}

}
