Brewer.Sale = (function() {
	
	function Sale(itemsTable) {
		this.itemsTable = itemsTable;
		this.totalValueBox = $('.js-total-value-box');
		this.shippingValueInput = $('#shippingValue');
		this.discountValueInput = $('#discountValue');
		
		this.totalItemsValue = this.itemsTable.totalValue();
		this.shippingValue = this.shippingValueInput.data('value');
		this.discountValue = this.discountValueInput.data('value');
	}
	
	Sale.prototype.start = function() {
		this.itemsTable.on('items-table-updated', onItemsTableUpdated.bind(this));
		this.shippingValueInput.on('keyup', onShippingValueChanged.bind(this));
		this.discountValueInput.on('keyup', onDiscountValueChanged.bind(this));
		
		this.itemsTable.on('items-table-updated', onChangedValues.bind(this));
		this.shippingValueInput.on('keyup', onChangedValues.bind(this));
		this.discountValueInput.on('keyup', onChangedValues.bind(this));
		
		onChangedValues.call(this);
	}
	
	function onItemsTableUpdated(event, totalValue) {
		this.totalItemsValue = totalValue == null ? 0 : totalValue;
	}
	
	function onShippingValueChanged(event) {
		this.shippingValue = Brewer.unFormatCurrency($(event.target).val());
	}
	
	function onDiscountValueChanged(event) {
		this.discountValue = Brewer.unFormatCurrency($(event.target).val());
	}
	
	function onChangedValues() {		
		var totalValue = numeral(this.totalItemsValue) + numeral(this.shippingValue) - numeral(this.discountValue);		
		this.totalValueBox.html(Brewer.formatCurrency(totalValue));
		
		this.totalValueBox.parent().toggleClass('negative-value', totalValue < 0);
	}
	
	return Sale;
}());

$(function() {
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.start();
	
	var itemsTable = new Brewer.ItemsTable(autocomplete);
	itemsTable.start();
	
	var sale = new Brewer.Sale(itemsTable);
	sale.start();
});
