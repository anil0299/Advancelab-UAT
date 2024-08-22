 $(document).ready(function() {
            // Handle "Select All" button click
            $("#select-all-btn").click(function() {
                var isChecked = $(this).is(':checked');
                $("#approved-table tbody input[type='checkbox']").prop('checked', isChecked);
            });
            
            // Handle "Delete" button click
            $("#delete").click(function() {
                // Collect selected checkbox values
                var selectedValues = $("#approved-table tbody input[type='checkbox']:checked").map(function() {
                    return $(this).val();
                }).get();

                if (selectedValues.length === 0) {
                    alert("No VMs selected.");
                    return ;
                }

                // Confirm deletion
                if (confirm("Are you sure you want to delete the selected VMs?")) {
                    // Send the selected values to the server
                    $.ajax({
                        url: '/deleteVms',
                        type: "POST",
                        data: { selectedVms: selectedValues },
                        success: function(response) {
                            alert("Successfully deleted.");
                            location.reload(); // Reload the page to reflect changes
                        },
                        error: function(xhr, status, error) {
                            //alert("An error occurred: " + xhr.status + " " + error + " (" + status + ")");
                        }
                    });
                } else {
					alert("Deletion cancelled.")
		            event.preventDefault(); // Prevent form submission
            		return;
		        }
            });
        });
        
        let table = new DataTable('#approved-table', {
		    // config options...
		});
		
		$(document).ready(function() {
    		$("#vm-form").submit(function() {
        	$("#delete").prop('disabled', true)
               .html('<span class="spinner-border spinner-border-sm me-2"></span><span role="status">Deleting...</span>')
    		});
		});