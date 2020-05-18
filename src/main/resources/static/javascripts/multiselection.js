var Brewer = Brewer || {};

Brewer.MultiSelection = (function() {
	
	function MultiSelection() {
		this.statusBtn = $('.js-status-btn');
		this.selectionCheckbox = $('.js-selecao');
		this.selectAllCheckbox = $('.js-select-all');
	}
	
	MultiSelection.prototype.initiate = function(){
		this.statusBtn.on('click', onStatusBtnClicked.bind(this));
		this.selectAllCheckbox.on('click', onSelectAllCheckboxClicked.bind(this));
		this.selectionCheckbox.on('click', onSelectionCheckboxClicked.bind(this));
	}
	
	function onStatusBtnClicked(event) {
		var clickedBtn = $(event.currentTarget);
		var status = clickedBtn.data('status');
		var url = clickedBtn.data('url');
		
		var selectedCheckboxs = this.selectionCheckbox.filter(':checked');
		var ids = $.map(selectedCheckboxs, function(c) {
			return $(c).data('id')
		});
		
		if(ids.length > 0) {
			$.ajax({
				url: url,
				method: 'PUT',
				data: {
					ids: ids,
					status: status
				},
				success: function () {
					window.location.reload();
				}
			});
		}
	}
	
	function onSelectAllCheckboxClicked() {
		var status = this.selectAllCheckbox.prop('checked');
		this.selectionCheckbox.prop('checked', status);
		actionButtonStatus.call(this, status);
	}
	
	function onSelectionCheckboxClicked() {
		var checkedSelectionCheckbox = this.selectionCheckbox.filter(':checked');
		this.selectAllCheckbox.prop('checked', checkedSelectionCheckbox.length >= this.selectionCheckbox.length);
		actionButtonStatus.call(this, checkedSelectionCheckbox.length);
	}
	
	function actionButtonStatus(activate) {
		activate ? this.statusBtn.removeClass('disabled') : this.statusBtn.addClass('disabled');
	}
	
	return MultiSelection;
}());

$(function() {
	var multiSelection = new Brewer.MultiSelection();
	multiSelection.initiate();
})