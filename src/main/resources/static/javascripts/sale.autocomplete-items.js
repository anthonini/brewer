var Brewer = Brewer || {}

Brewer.Autocomplete = (function(){

	function Autocomplete() {
		this.skuOrNameInput = $('.js-beer-sku-or-name-input')
		var htmlTemplateAutocomplete = $('#beer-autocomplete-template').html();
		this.template = Handlebars.compile(htmlTemplateAutocomplete);
	}
	
	Autocomplete.prototype.start = function(){
		var options = {
			url: function(skuOrName) {
				return '/brewer/beer?skuOrName=' + skuOrName;
			},
			getValue: 'name',
			minCharNumber: 3,
			requestDelay: 500,
			ajaxSettings: {
				contentType: 'application/json'
			},
			template: {
				type: 'custom',
				method: function(name, beer) {
					beer.formattedValue = Brewer.formatCurrency(beer.value);
					return this.template(beer);
				}.bind(this)
			}
		};
		
		this.skuOrNameInput.easyAutocomplete(options);
	}

	return Autocomplete;
}());

$(function() {
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.start();
})