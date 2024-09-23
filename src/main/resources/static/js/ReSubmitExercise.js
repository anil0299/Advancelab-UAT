setTimeout(function() {
	var message = document.getElementById("message");
	message.parentNode.removeChild(message);
}, 3000);

document.getElementById('tick').addEventListener('change', function () {
    document.getElementById('terms').disabled = !this.checked;
});
	
const idProofInput = document.getElementById('submissionPdf');

idProofInput.addEventListener('change', function() {
    const files = this.files;
    if (files.length !== 1) {
        alert('Please select only one PDF file.');
        this.value = ''; // Clear the input field
    } else {
        const file = files[0];
        const fileSizeKB = file.size / 1024;
        
        if (file.type !== 'application/pdf' || fileSizeKB < 10 || fileSizeKB > 1024) {
            alert('Please select a PDF file between 10KB and 1MB in size.');
            this.value = ''; // Clear the input field
        }
        
        const fileName = file.name;
        const validPdfNameRegex = /^[a-zA-Z0-9_-]+\.[pP][dD][fF]$/; // Valid PDF filename pattern
        
        if (!validPdfNameRegex.test(fileName)) {
            alert('Please select a PDF file with a valid filename.');
            this.value = ''; // Clear the input field
        }
    }
});
		
document.getElementById('ReSubmit').addEventListener('submit', function (event) {
    var result = confirm('Are you sure you want to proceed? Your previous exercise submission will be deleted.');
    if (!result) {
        event.preventDefault(); // Prevent form submission if the user cancels
    } else {
        checkData(); // Call your function if confirmed
    }
});
	
		
function ToastMessage() {
	const toastLive = document.getElementById('liveToast')
   	const toast = new bootstrap.Toast(toastLive)
   	toast.show()
}