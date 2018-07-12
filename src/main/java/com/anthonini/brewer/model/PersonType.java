package com.anthonini.brewer.model;

import com.anthonini.brewer.model.validation.group.CnpjGroup;
import com.anthonini.brewer.model.validation.group.CpfGroup;

public enum PersonType {

	FISICA("Física", "CPF", "000.000.000-00", CpfGroup.class),
	JURIDICA("Jurídica", "CNPJ", "00.000.000/0000-00", CnpjGroup.class);
	
	private String description;
	private String document;
	private String mask;
	private Class<?> group;
	
	PersonType(String description, String document, String mask, Class<?> group) {
		this.description = description;
		this.document = document;
		this.mask = mask;
		this.group = group;
	}

	public String getDescription() {
		return description;
	}

	public String getDocument() {
		return document;
	}

	public String getMask() {
		return mask;
	}

	public Class<?> getGroup() {
		return group;
	}
	
	public static String removeFormatting(String cpfCnpj) {
		return cpfCnpj.replaceAll("\\.|-|/", "");
	}
}
