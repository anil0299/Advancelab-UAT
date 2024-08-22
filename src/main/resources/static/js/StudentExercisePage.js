$(document).ready(function() {
    $("#createLabBtn").click(function() {
        $(this).prop('disabled', true)
               .html('<span class="spinner-border spinner-border-sm me-2"></span><span role="status">Creating Lab...</span>')
    });
});

setTimeout(function() {
	var message = document.getElementById("message");
	message.parentNode.removeChild(message);
}, 3000);

document.getElementById('tick').addEventListener('change', function () {
    document.getElementById('terms').disabled = !this.checked;
});


function checkDataCreateLab() {
	var topicId=document.getElementById("createLabexerciseId").value;
	var inputCode = "";
	inputCode += getInputCode(topicId);				
	var hppCode = getHPPCode(origHPPCode, inputCode);
	document.getElementById("hppCodeCreateLab").value = hppCode;
	document.getElementById('createlabform').submit();
	return false; 
}
	
document.getElementById('deleteAdvanceLabVM').addEventListener('submit', function (event) {
    var result = confirm('Are you sure? All your VM will be Deleted');
    if (!result) {
        event.preventDefault();
    } else {
        checkData();
    }
});	
	  
function checkDataDeleteLabVm() {
	var topicId=document.getElementById("deleteLabexerciseId").value;
	var pdf=document.getElementById("submissionPdf").files[0].name;
	var inputCode = "";
	inputCode += getInputCode(topicId);
	inputCode += getInputCode(pdf);					
	var hppCode = getHPPCode(origHPPCode, inputCode);
	document.getElementById("hppCodeDeleteLabVm").value = hppCode;
	document.getElementById('deleteAdvanceLabVM').submit();
	return false; 
}
	
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
		
		
		
	