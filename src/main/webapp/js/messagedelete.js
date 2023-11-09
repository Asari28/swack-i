var deleteModal = document.getElementById('deleteModal')
deleteModal.addEventListener('show.bs.modal', function (event) {
  // Button that triggered the modal
  var button = event.relatedTarget
  // Extract info from data-bs-* attributes
  var recipient = button.getAttribute('data-bs-whatever')
  // If necessary, you could initiate an AJAX request here
  // and then do the updating in a callback.
  //
  // Update the modal's content.
  var modalTitle = deleteModal.querySelector('.modal-title')
  var modalBodyInput = deleteModal.querySelector('.modal-body input')

  modalTitle.textContent = 'メッセージを削除しますか？'
  modalBodyInput.value = recipient
})

document.querySelectorAll('.deleteicon').forEach(function (button) {
    button.addEventListener('click', function () {
        var chatLogId = button.getAttribute('data-bs-whatever');
        
        document.getElementById('chatLogIdInput').value = chatLogId;
    });
});