package com.anthonini.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sale")
public class Sale implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_sale")
	private Long id;
	
	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	
	@Column(name = "shipping_value")
	private BigDecimal shippingValue;
	
	@Column(name = "discount_value")
	private BigDecimal discountValue;
	
	@Column(name = "total_value")
	private BigDecimal totalValue = BigDecimal.ZERO;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private SaleStatus status = SaleStatus.ORCAMENTO;
	
	private String observation;
	
	@Column(name = "delivery_date_time")
	private LocalDateTime deliveryDateTime;
	
	@ManyToOne
	@JoinColumn(name = "id_client")
	private Client client;
	
	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;
	
	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
	private List<SaleItem> items = new ArrayList<>();
	
	@Transient
	private String uuid;
	
	@Transient
	private LocalDate deliveryDate;
	
	@Transient
	private LocalTime deliveryTime;
	
	public boolean isNew() {
		return id == null;
	}
	
	public void addItems(List<SaleItem> items) {
		this.items = items;
		this.items.forEach(i -> i.setSale(this));
	}
	
	public void calculateTotalValue() {
		BigDecimal itemsTotalValue = getItems().stream()
		.map(SaleItem::getTotalValue)
		.reduce(BigDecimal::add)
		.orElse(BigDecimal.ZERO);
		
		this.totalValue = calculateTotalValue(itemsTotalValue, getShippingValue(), getDiscountValue());
	}
	
	private BigDecimal calculateTotalValue(BigDecimal ItemsTotalValue, BigDecimal shippingValue, BigDecimal discountValue) {
		BigDecimal valorTotal = ItemsTotalValue
				.add(Optional.ofNullable(shippingValue).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(discountValue).orElse(BigDecimal.ZERO));
		return valorTotal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public BigDecimal getShippingValue() {
		return shippingValue;
	}

	public void setShippingValue(BigDecimal shippingValue) {
		this.shippingValue = shippingValue;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public SaleStatus getStatus() {
		return status;
	}

	public void setStatus(SaleStatus status) {
		this.status = status;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public LocalDateTime getDeliveryDateTime() {
		return deliveryDateTime;
	}

	public void setDeliveryDateTime(LocalDateTime deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}

	public List<SaleItem> getItems() {
		return items;
	}

	public void setItems(List<SaleItem> items) {
		this.items = items;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public LocalTime getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(LocalTime deliveryTime) {
		this.deliveryTime = deliveryTime;
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
		Sale other = (Sale) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
