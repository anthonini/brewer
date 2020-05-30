Brewer = Brewer || {};

Brewer.ExclusionDialog = (function(){
	
	function ExclusionDialog() {
		this.exclusionBtn = $('.js-exclusion-btn');
		
		if(window.location.search.indexOf('excluded') > -1){
			swal('Pronto!', 'Excluído com sucesso!', 'success');
		}
	}
	
	ExclusionDialog.prototype.start = function() {
		this.exclusionBtn.on('click', onExclusionBtnClicked.bind(this));
	}
	
	function onExclusionBtnClicked(event) {
		event.preventDefault();
		var clickedBtn = $(event.currentTarget);
		var url = clickedBtn.data('url');
		var object = clickedBtn.data('object');
		
		swal({
			title: 'Tem certeza?',
			text: 'Excluir "' + object + '"? Você não poderá recuperar depois.',
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: 'Sim, exclua agora!',
			closeOnConfirm: false
		}, onExlusionConfirmed.bind(this, url));
	}
	
	function onExlusionConfirmed(url) {
		$.ajax({
			url: url,
			method: 'DELETE',
			success: onExclusionSuccess.bind(this),
			error: onExclusionError.bind(this)
		});
	}
	
	function onExclusionSuccess() {
		var actualUrl = window.location.href;
		var separator = actualUrl.indexOf('?') > -1 ? '&' : '?';
		var newUrl = actualUrl.indexOf('excluded') > -1 ? actualUrl : actualUrl + separator + 'excluded';
		
		window.location = newUrl;
	}
	function onExclusionError(e) {
		swal('Oops!', e.responseText, 'error');
	}
	
	return ExclusionDialog;
}());

$(function(){
	var exclusionDialog = new Brewer.ExclusionDialog();
	exclusionDialog.start();
});