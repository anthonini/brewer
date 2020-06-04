package com.anthonini.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;

import com.anthonini.brewer.repository.listener.BeerEntityListener;
import com.anthonini.brewer.validation.SKU;

@EntityListeners(BeerEntityListener.class)
@Entity
@Table(name = "beer")
public class Beer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_beer")
	private Long id;
	
	@SKU
	@NotBlank(message = "SKU é obrigatório")
	private String sku;
	
	@NotBlank(message = "Nome é obrigatório")
	private String name;
	
	@NotBlank(message = "Descrição é obrigatório")
	@Size(max = 50, message = "O tamanho da descrição deve estar entre 1 e {max}")
	private String description;
	
	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.50", message = "O valor da cerveja deve ser maior que R$0,50")
	@DecimalMax(value = "9999999.99", message = "Valor deve ser menor ou igual a R$9.999.999,99")
	private BigDecimal value;
	
	@NotNull(message = "Teor alcóolico é obrigatório")
	@DecimalMax(value = "100.00", message = "Teor alcóolico deve ser menor ou igual a 100%")
	@Column(name = "alcoholic_strength")
	private BigDecimal alcoholicStrength; 
	
	@NotNull(message = "A comissão é obrigatória")
	@DecimalMax(value = "100.00", message = "Comissão deve ser menor ou igual a 100%")
	private BigDecimal comission;
	
	@NotNull(message = "A quantidade em estoque é obrigatória")
	@Max(value = 9999, message = "A quantidade em estoque  deve ser menor ou igual a 9.999")
	@Min(value = 1, message = "A quantidade em estoque  deve ser maior ou igual a 1")
	@Column(name = "stock_quantity")
	private Integer stockQuantity;
	
	@NotNull(message = "Origem é obrigatório")
	@Enumerated(EnumType.STRING)
	private Origin origin;
	
	@NotNull(message = "Sabor é obrigatório")
	@Enumerated(EnumType.STRING)
	private Flavor flavor;
	
	@NotNull(message = "Estilo é obrigatório")
	@ManyToOne
	@JoinColumn(name = "id_style")
	private Style style;
	
	private String photo;
	
	@Column(name = "content_type")
	private String contentType;
	
	@Transient
	private boolean newPhoto;
	
	@Transient
	private String urlPhoto;
	
	@Transient
	private String urlThumbnailPhoto;
	
	@PrePersist @PreUpdate
	private void prePersistUpdate() {
		sku = sku.toUpperCase();
	}
	
	public String getPhotoOrMock() {
		return !StringUtils.isEmpty(photo) ? photo : "beer-mock.png";
	}
	
	public boolean hasPhoto() {
		return !StringUtils.isEmpty(this.getPhoto());
	}
	
	public boolean isNew() {
		return id == null;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getAlcoholicStrength() {
		return alcoholicStrength;
	}

	public void setAlcoholicStrength(BigDecimal alcoholicStrength) {
		this.alcoholicStrength = alcoholicStrength;
	}

	public BigDecimal getComission() {
		return comission;
	}

	public void setComission(BigDecimal comission) {
		this.comission = comission;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	public Flavor getFlavor() {
		return flavor;
	}

	public void setFlavor(Flavor flavor) {
		this.flavor = flavor;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isNewPhoto() {
		return newPhoto;
	}

	public void setNewPhoto(boolean newPhoto) {
		this.newPhoto = newPhoto;
	}

	public String getUrlPhoto() {
		return urlPhoto;
	}

	public void setUrlPhoto(String urlPhoto) {
		this.urlPhoto = urlPhoto;
	}

	public String getUrlThumbnailPhoto() {
		return urlThumbnailPhoto;
	}

	public void setUrlThumbnailPhoto(String urlThumbnailPhoto) {
		this.urlThumbnailPhoto = urlThumbnailPhoto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Beer other = (Beer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
