package com.anthonini.brewer.dto;

public class PhotoDTO {

	private String name;
	private String contentType;
	private String url;
	
	public PhotoDTO(String name, String contentType, String url) {
		this.name = name;
		this.contentType = contentType;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
