
Brewer.ItemsTable = (function() {
	
	function ItemsTable(autocomplete) {
		this.autocomplete = autocomplete;
		this.beerTableContainer = $('.js-beer-table-container');
	}
	
	ItemsTable.prototype.start = function() {
		this.autocomplete.on('sale.selected-item', onSelectedItem.bind(this));
	}
	
	function onSelectedItem(event, item) {
		var response = $.ajax({
			url: 'item',
			method: 'POST',
			data: {
				beerId: item.id
			}
		});
		
		response.done(onServerItemUpdated.bind(this));
	}
	
	function onServerItemUpdated(html) {
		this.beerTableContainer.html(html);
		$('.js-beer-table-quantity').on('change', onItemQuantityChanged.bind(this));
	}
	
	function onItemQuantityChanged(event) {
		var input = $(event.target);
		var quantity = input.val();
		var beerId = input.data('beer-id');
		
		var response = $.ajax({
			url: 'item/'+beerId,
			method: 'PUT',
			data: {
				quantity: quantity
			}
		});
		
		response.done(onServerItemUpdated.bind(this));
	}
	
	return ItemsTable;
	
}());

$(function() {
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.start();
	
	var ItemsTable = new Brewer.ItemsTable(autocomplete);
	ItemsTable.start();
	
});