Brewer.ItemsTable = (function() {
	
	function ItemsTable(autocomplete) {
		this.autocomplete = autocomplete;
		this.beerTableContainer = $('.js-beer-table-container');
		this.uuid = $('#uuid').val();
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	ItemsTable.prototype.totalValue = function() {
		return this.beerTableContainer.data('value');
	}
	
	ItemsTable.prototype.start = function() {
		this.autocomplete.on('sale.selected-item', onSelectedItem.bind(this));
		
		bindQuantity.call(this);
		bindItemTable.call(this);
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
		
		bindQuantity.call(this);
		
		var itemTable = bindItemTable.call(this);		
		this.emitter.trigger('items-table-updated', itemTable.data('total-value'));
	}
	
	function onItemQuantityChanged(event) {
		var input = $(event.target);
		var quantity = input.val();
		
		if(quantity <= 0) {
			input.val(1);
			quantity = 1;
		}
		
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
	
	function bindQuantity() {
		var itemQuantityInput = $('.js-beer-table-quantity');
		itemQuantityInput.on('change', onItemQuantityChanged.bind(this));
		itemQuantityInput.maskMoney({precision: 0, thousands: ''});
	}
	
	function bindItemTable() {
		var itemTable = $('.js-item-table');
		itemTable.on('dblclick', onDoubleClick);
		$('.js-item-exclusion-btn').on('click', onBtnExlcusionClicked.bind(this));
		
		return itemTable;
	}
	
	return ItemsTable;
}());