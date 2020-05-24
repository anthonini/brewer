var Brewer = Brewer || {}

Brewer.FastClientSearch = (function() {
	
	function FastClientSearch() {
		this.fastClientSearchModal = $('#fastClientSearch');
		this.clientNameModal = $('#clientNameModal');
		this.fastClientSearchButton = $('.js-fast-client-search-button');
		this.fastClientSearchErrorMessage = $('.js-client-fast-search-error-message');
		this.containerFastClientSearchTable = $('#containerFastClientSearchTable');
		this.htmlSearchTable = $('#fast-client-search-table').html();
		this.template = Handlebars.compile(this.htmlSearchTable);
	}
	
	FastClientSearch.prototype.start = function() {
		this.fastClientSearchButton.on('click', onFastClientSearchButtonClicked.bind(this));
		this.fastClientSearchModal.on('shown.bs.modal', onModalShow.bind(this));
	}
	
	function onModalShow() {
		this.clientNameModal.focus();
	}
	
	function onFastClientSearchButtonClicked(event) {
		event.preventDefault();
		$.ajax({
			url: this.fastClientSearchModal.find('form').attr('action'),
			method: 'GET',
			contentType: 'application/json',
			data: {
				name: this.clientNameModal.val()
			},
			success: onSearchComplete.bind(this),
			error: onSearchError.bind(this)
		});
	}
	
	function onSearchComplete(result) {
		var html = this.template(result);
		this.containerFastClientSearchTable.html(html);
		this.fastClientSearchErrorMessage.addClass('hidden');
		
		var fastClientSearchTable = new Brewer.FastClientSearchTable(this.fastClientSearchModal);
		fastClientSearchTable.start();
	}
	
	function onSearchError() {
		this.fastClientSearchErrorMessage.removeClass('hidden');
	}
	
	return FastClientSearch;
}());

Brewer.FastClientSearchTable = (function() {
	
	function FastClientSearchTable(modal) {
		this.client = $('.js-fast_search_client');
		this.modalClient = modal;
	}
	
	FastClientSearchTable.prototype.start = function() {
		this.client.on('click', onClientSelected.bind(this));
	}
	
	function onClientSelected(event) {
		var selectedClient = $(event.currentTarget);
		$('#clientName').val(selectedClient.data('name'));
		$('#clientId').val(selectedClient.data('id'));
		
		this.modalClient.modal('hide');		
	}
	
	return FastClientSearchTable;
}());

$(function () {
	var fastClientSearch = new Brewer.FastClientSearch();
	fastClientSearch.start();
});