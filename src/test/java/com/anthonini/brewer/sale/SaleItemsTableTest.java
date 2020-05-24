package com.anthonini.brewer.sale;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.anthonini.brewer.model.Beer;
import com.anthonini.brewer.session.SaleItemsTable;

public class SaleItemsTableTest {
	
	private SaleItemsTable saleItemsTable;
	
	@Before
	public void setUp() {
		saleItemsTable = new SaleItemsTable();
	}

	@Test
	public void testCalcTotalValueWithoutItems() {
		assertEquals(BigDecimal.ZERO, saleItemsTable.getTotalValue());
	}
	
	@Test
	public void testCalcTotalValueWithOneItem() {
		BigDecimal value = new BigDecimal("8.99");
		Beer beer = new Beer();
		beer.setValue(value);		
		saleItemsTable.addItem(beer, 1);
		
		assertEquals(new BigDecimal("8.99"), saleItemsTable.getTotalValue());
	}
	
	@Test
	public void testCalcTotalValueWithManyItems() {
		BigDecimal v1 = new BigDecimal("8.99");
		Beer b1 = new Beer();
		b1.setId(1L);
		b1.setValue(v1);
		
		BigDecimal v2 = new BigDecimal("4.99");
		Beer b2 = new Beer();
		b2.setId(2L);
		b2.setValue(v2);
		
		saleItemsTable.addItem(b1, 1);
		saleItemsTable.addItem(b2, 2);		
		
		assertEquals(new BigDecimal("18.97"), saleItemsTable.getTotalValue());
	}
	
	@Test
	public void testQuantityOnAddSameBeer() {
		Beer beer = new Beer();
		beer.setId(1L);
		beer.setValue(new BigDecimal("4.99"));
		
		saleItemsTable.addItem(beer, 1);
		saleItemsTable.addItem(beer, 1);
		
		assertEquals(1, saleItemsTable.total());
		assertEquals(new BigDecimal("9.98"), saleItemsTable.getTotalValue());
	}
	
	@Test
	public void testChangeQuantity() throws Exception {
		Beer beer = new Beer();
		beer.setId(1L);
		beer.setValue(new BigDecimal("4.50"));
		
		saleItemsTable.addItem(beer, 1);
		saleItemsTable.changeQuantity(beer, 3);
		
		assertEquals(1, saleItemsTable.total());
		assertEquals(new BigDecimal("13.50"), saleItemsTable.getTotalValue());
	}
	
	@Test
	public void testItemExclusion() throws Exception {
		Beer b1 = new Beer();
		b1.setId(1L);
		b1.setValue(new BigDecimal("8.50"));
		
		Beer b2 = new Beer();
		b2.setId(2L);
		b2.setValue(new BigDecimal("4.99"));
		
		Beer b3 = new Beer();
		b3.setId(3L);
		b3.setValue(new BigDecimal("5.50"));
		
		saleItemsTable.addItem(b1, 1);
		saleItemsTable.addItem(b2, 2);
		saleItemsTable.addItem(b3, 1);
		
		saleItemsTable.deleteItem(b2);
		
		assertEquals(new BigDecimal("14.00"), saleItemsTable.getTotalValue());
	}
}
