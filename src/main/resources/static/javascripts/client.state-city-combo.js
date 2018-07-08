var Brewer = Brewer || {};

Brewer.StateCombo = (function() {

	function StateCombo() {
		this.combo = $('#state');
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	StateCombo.prototype.start = function() {
		this.combo.on('change', onStateChanged.bind(this));
	}
	
	function onStateChanged() {
		this.emitter.trigger('change', this.combo.val());
	}
	
	return StateCombo;
}());

Brewer.CityCombo = (function() {

	function CityCombo(stateCombo) {
		this.stateCombo = stateCombo;
		this.combo = $('#city');
		this.imgLoading = $('.js-img-loading');
	}
	
	CityCombo.prototype.start = function() {
		reset.call(this);
		this.stateCombo.on('change', onStateChanged.bind(this));
	}
	
	function onStateChanged(evento, stateId) {
		if(stateId) {
			var answer = $.ajax({
				url: this.combo.data('url'),
				method: 'GET',
				contentType: 'application/json',
				data: {'state' : stateId },
				beforeSend: startRequest.bind(this),
				complete: requestComplete.bind(this)
			});
			
			answer.done(onCitySearchComplete.bind(this));
		}else {
			reset.call(this);
		}
	}
	
	function onCitySearchComplete(cities) {
		var options = [];
		cities.forEach(function(city){
			options.push('<option value="' + city.id + '">' + city.name +'</option>');
		});
		
		this.combo.html(options.join(''));
		this.combo.removeAttr('disabled');
	}
	
	function reset() {
		this.combo.html('<option value="" label="Selecione a cidade">');
		this.combo.val('');
		this.combo.attr('disabled', 'disabled');
	}
	
	function startRequest() {
		reset.call(this);
		this.imgLoading.show();
	}
	
	function requestComplete() {
		this.imgLoading.hide();
	}
	
	return CityCombo;
}());

$(function () {
	var stateCombo = new Brewer.StateCombo();
	stateCombo.start();
	
	var cityCombo = new Brewer.CityCombo(stateCombo);
	cityCombo.start();
});