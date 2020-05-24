Brewer = Brewer || {};

Brewer.SubmitButton = (function(){
	
	function SubmitButton() {
		this.submitBtn = $('.js-submit-btn');
		this.form = $('.js-main-form');
	}
	
	SubmitButton.prototype.start = function() {
		this.submitBtn.on('click', onSubmit.bind(this));
	}
	
	function onSubmit(event) {
		event.preventDefault();
		
		var clickedButton = $(event.target);
		var action = clickedButton.data('action');
		var actionInput = $('<input>');
		actionInput.attr('name', action);
		
		this.form.append(actionInput);
		this.form.submit();
	}
	
	return SubmitButton;
}());

$(function(){
	var SubmitButton = new Brewer.SubmitButton();
	SubmitButton.start();
	
});