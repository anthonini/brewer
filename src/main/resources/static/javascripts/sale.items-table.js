
Brewer.ItemsTable = (function() {
	
	function ItemsTable(autocomplete) {
		this.autocomplete = autocomplete;
		this.beerTableContainer = $('.js-beer-table-container');
		this.uuid = $('#uuid').val();
	}
	
	ItemsTable.prototype.start = function() {
		this.autocomplete.on('sale.selected-item', onSelectedItem.bind(this));
	}
	
	function onSelectedItem(event, item) {
		var response = $.ajax({
			url: 'item',
			method: 'POST',
			data: {
				beerId: item.id,
				uuid: this.uuid
			}
		});
		
		response.done(onServerItemUpdated.bind(this));
	}
	
	function onServerItemUpdated(html) {
		this.beerTableContainer.html(html);
		$('.js-beer-table-quantity').on('change', onItemQuantityChanged.bind(this));
		$('.js-item-table').on('dblclick', onDoubleClick);
		$('.js-item-exclusion-btn').on('click', onBtnExlcusionClicked.bind(this));
	}
	
	function onItemQuantityChanged(event) {
		var input = $(event.target);
		var quantity = input.val();
		var beerId = input.data('beer-id');
		
		var response = $.ajax({
			url: 'item/'+beerId,
			method: 'PUT',
			data: {
				quantity: quantity,
				uuid: this.uuid
			}
		});
		
		response.done(onServerItemUpdated.bind(this));
	}
	
	function onDoubleClick(evento) {
		$(this).toggleClass('soliciting-exclusion');
	}
	
	function onBtnExlcusionClicked() {
		var beerId = $(event.target).data('beer-id');
		
		var response = $.ajax({
			url: 'item/' + this.uuid + '/' + beerId,
			method: 'DELETE'
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