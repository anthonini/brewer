package com.anthonini.brewer.dto;

import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import com.anthonini.brewer.model.Origin;

public class BeerDTO {

	private Long id;
	private String sku;
	private String name;
	private String origin;
	private BigDecimal value;
	private String photo;
	private String urlThumbnailPhoto;
	
	public BeerDTO(Long id, String sku, String name, Origin origin, BigDecimal value, String photo) {
		this.id = id;
		this.sku = sku;
		this.name = name;
		this.origin = origin.getDescription();
		this.value = value;
		this.photo = StringUtils.isEmpty(photo) ? "beer-mock.png" : photo;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSku() {
		return sku;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public String getPhoto() {
		return photo;
	}
	
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getUrlThumbnailPhoto() {
		return urlThumbnailPhoto;
	}

	public void setUrlThumbnailPhoto(String urlThumbnailPhoto) {
		this.urlThumbnailPhoto = urlThumbnailPhoto;
	}
}
