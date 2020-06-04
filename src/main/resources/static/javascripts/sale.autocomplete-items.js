var Brewer = Brewer || {}

Brewer.Autocomplete = (function(){

	function Autocomplete() {
		this.skuOrNameInput = $('.js-beer-sku-or-name-input')
		var htmlTemplateAutocomplete = $('#beer-autocomplete-template').html();
		this.template = Handlebars.compile(htmlTemplateAutocomplete);
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	Autocomplete.prototype.start = function(){
		var options = {
			url: function(skuOrName) {
				return this.skuOrNameInput.data('url') + '?skuOrName=' + skuOrName;
			}.bind(this),
			getValue: 'name',
			minCharNumber: 3,
			requestDelay: 500,
			ajaxSettings: {
				contentType: 'application/json'
			},
			template: {
				type: 'custom',
				method: template.bind(this)
			},
			list: {
				onChooseEvent: onSelectedItem.bind(this)
			}
		};
		
		this.skuOrNameInput.easyAutocomplete(options);
	}
	
	function template(name, beer) {
		beer.formattedValue = Brewer.formatCurrency(beer.value);
		return this.template(beer);
	}
	
	function onSelectedItem() {
		this.emitter.trigger('sale.selected-item', this.skuOrNameInput.getSelectedItemData());
	}

	return Autocomplete;
}());

$(function() {
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.start();
})