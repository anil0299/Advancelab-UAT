	
	$(document).ready(function() {
		//Pending Table
	    $('#pending-table').DataTable({
	        processing: true,
	        serverSide: true,
	        ajax: {
	            url: '/pendingStudentData',
	            method: 'GET',
	            data: function(d) {
	            	//console.log(d);
	                return $.extend({}, d, {
	                });
	            },
	            dataSrc: function(response) {
	            	//console.log(response);
	                return response.data;
	            }
	        },
	        columns: [
	        	{ 
	        		data: null,
	                className: "text-center",
	                title: "S. No",
	                render: function (data, type, row, meta) {
	                    return meta.row + meta.settings._iDisplayStart + 1;
	                }
	            },
	            { data: 'id', title: 'ID' , className: 'text-center'},
	            { data: 'firstName', title: 'First Name' , className: 'text-center'},
	            { data: 'lastName', title: 'Last Name' , className: 'text-center'},
	            { data: 'emailAddress', title: 'Email Address' , className: 'text-center'},
	            { data: 'qualification', title: 'Course' , className: 'text-center'},
	            { data: 'college', title: 'College' , className: 'text-center'},
	            { data: 'state', title: 'State' , className: 'text-center'},
	            { data: 'registrationDate', title: 'Registration Date' , className: 'text-center'},
	            { 
	                data: null,
	                title: 'Category Certificate',
	                className: 'text-center',
	                render: function(data, type, row) {
	                    return `
	                        <form action="/displayPDF/${row.id}" method="post" id="pdfform${row.id}" name="pdfform" target="_blank">
	                        	<input type="hidden" name="_csrf" id="csrfPDF${row.id}">
	                            <input type="hidden" name="hppCode" id="hppCodePDF${row.id}" />
	                            <input type="hidden" name="pdfID" value="${row.id}" id="pdfID${row.id}" />
	                            <button type="submit" class="border-0 bg-transparent h4" onclick="pdfData(${row.id})">
	                                <i class="text-success fas fa-eye"></i>
	                            </button>
	                        </form>
	                    `;
	                }
	            },
	            { 
	                data: null,
	                title: 'Last Marksheet',
	                className: 'text-center',
	                render: function(data, type, row) {
	                    return `
	                        <form action="/displayPDF1/${row.id}" method="post" id="pdfform2${row.id}" name="pdfform2" target="_blank">
	                        	<input type="hidden" name="_csrf" id="csrfPDF2${row.id}">
	                            <input type="hidden" name="hppCode" id="hppCodePDF2${row.id}" />
	                            <input type="hidden" name="pdfID2" value="${row.id}" id="pdfID2${row.id}" />
	                            <button type="submit" class="border-0 bg-transparent h4" onclick="pdfData2(${row.id})">
	                                <i class="text-success fas fa-eye"></i>
	                            </button>
	                        </form>
	                    `;
	                }
	            },
	            { 
                data: null,
                title: 'Actions',
                className: 'text-center',
                render: function(data, type, row) {
                    return `
                        <form action="/StudentApproval/approve-studentDtls/${row.id}" method="post" autocomplete="off" id="approveform${row.id}">
                        	<input type="hidden" name="_csrf" id="csrfApprove${row.id}">
                            <input type="hidden" name="hppCode" id="hppCodeApprove${row.id}" />
                            <input type="hidden" name="stuID" value="${row.id}" id="stuID${row.id}" />
                            <div class="row">
	                            <div class="col-6 my-1">
                            		<button type="submit" class="btn btn-success btn-sm" onclick="approveData(${row.id})">Approve</button>
                            	</div>
                            </div>
                        </form>
                        <form action="/rejected/${row.id}" method="post" autocomplete="off" id="rejectform${row.id}">
                        	<input type="hidden" name="_csrf" id="csrfReject${row.id}">
                            <input type="hidden" name="hppCode" id="hppCodeReject${row.id}" />
                            <input type="hidden" name="rejectID" value="${row.id}" id="rejectID${row.id}" />
                            <div class="row">
	                            <div class="col-6 my-1">
                            		<button type="submit" class="btn btn-warning btn-sm" onclick="rejectData(${row.id})">Reject</button>
                            	</div>
                            </div>
                        </form>
                        <form action="/update/${row.id}" method="post" autocomplete="off" id="editform${row.id}">
                        	<input type="hidden" name="_csrf" id="csrfEdit${row.id}">
                            <input type="hidden" name="hppCode" id="hppCodeEdit${row.id}" />
                            <input type="hidden" name="editID" value="${row.id}" id="editID${row.id}" />
                            <div class="row">
	                            <div class="col-6 my-1">
                            		<button type="submit" class="btn btn-primary btn-sm" onclick="editData(${row.id})">Edit</button>
                            	</div>
                            </div>
                        </form>
                        <form action="/rejectuser/${row.id}" method="post" autocomplete="off" id="deleteform${row.id}">
                        	<input type="hidden" name="_csrf" id="csrfDelete${row.id}">
                            <input type="hidden" name="hppCode" id="hppCodeDelete${row.id}" />
                            <input type="hidden" name="deleteID" value="${row.id}" id="deleteID${row.id}" />
                            <div class="row">
	                            <div class="col-6 my-1">
                            		<button type="submit" class="btn btn-danger btn-sm" onclick="deleteData(${row.id})">Delete</button>
                            	</div>
                            </div>
                        </form>
                    `;
                }
            }
	            
	        ],
	        lengthMenu: [
	            [10, 25, 50, 100, 500],
	            [10, 25, 50, 100, 500]
	        ],
	        searching: true,
	        ordering: true,
	        paging: true,
	        lengthChange: true,
	        pageLength: 10
	    });
	    
	    //Approved Table
	    $('#approved-table').DataTable({
	        processing: true,
	        serverSide: true,
	        ajax: {
	            url: '/approvedStudentData',
	            method: 'GET',
	            data: function(d) {
	            	//console.log(d);
	                return $.extend({}, d, {
	                });
	            },
	            dataSrc: function(response) {
	            	//console.log(response);
	                return response.data;
	            }
	        },
	        columns: [
	        	{ 
	        		data: null,
	                className: "text-center",
	                title: "S. No",
	                render: function (data, type, row, meta) {
	                    return meta.row + meta.settings._iDisplayStart + 1;
	                }
	            },
	            { data: 'id', title: 'ID' , className: 'text-center'},
	            { data: 'firstName', title: 'First Name' , className: 'text-center'},
	            { data: 'lastName', title: 'Last Name' , className: 'text-center'},
	            { data: 'emailAddress', title: 'Email Address' , className: 'text-center'},
	            { data: 'qualification', title: 'Course' , className: 'text-center'},
	            { 
	            	"data": null,
	                "title": "Batch/Class",
	                "className": "text-center",
	                "render": function(data, type, row) {
	                    var batch = row.batch;
	                    var className = row.className;
	
	                    if (batch) {
	                        return batch;
	                    } else if (className) {
	                        return className;
	                    } else {
	                        return '';
	                    }
	                }
	            },
	            { data: 'college', title: 'College' , className: 'text-center'},
	            { data: 'state', title: 'State' , className: 'text-center'},
	            { data: 'registrationDate', title: 'Registration Date' , className: 'text-center'},
	            { data: 'approvedDate', title: 'Approval Date' , className: 'text-center'},
	            { data: 'validTill', title: 'Valid Till' , className: 'text-center'},
	            { 
				    data: null,
				    title: 'Extend Validity',
				    className: 'text-center',
				    render: function(data, type, row) {
				        return `
				            <form action="/extend-validity/${row.id}" method="post" id="extendDateForm${row.id}" name="extendDateForm">
				            	<input type="hidden" name="_csrf" id="csrfExtend${row.id}">
			                    <input type="hidden" name="hppCode" id="hppCodeExtend${row.id}" />
			                    <input type="date" class="form-control form-control-sm" style="max-width: 150px;" placeholder="Enter Date" name="validTill" id="validTill${row.id}" aria-label="Enter Date" value="${row.validTill}">
		                        <button class="btn btn-outline-primary btn-sm mt-2" type="submit" onclick="extendDate(${row.id})">Extend</button>
				            </form>
				        `;
				    }
				},
	            { 
	                data: null,
	                title: 'CC',
	                className: 'text-center',
	                render: function(data, type, row) {
	                    return `
	                        <form action="/displayPDF/${row.id}" method="post" id="pdfform3${row.id}" name="pdfform3" target="_blank">
	                        	<input type="hidden" name="_csrf" id="csrfPDF3${row.id}">
	                            <input type="hidden" name="hppCode" id="hppCodePDF3${row.id}" />
	                            <input type="hidden" name="pdfID" value="${row.id}" id="pdfID3${row.id}" />
	                            <button type="submit" class="border-0 bg-transparent h4" onclick="pdfData3(${row.id})">
	                                <i class="text-success fas fa-eye"></i>
	                            </button>
	                        </form>
	                    `;
	                }
	            },
	            { 
	                data: null,
	                title: 'LM',
	                className: 'text-center',
	                render: function(data, type, row) {
	                    return `
	                        <form action="/displayPDF1/${row.id}" method="post" id="pdfform4${row.id}" name="pdfform4" target="_blank">
	                        	<input type="hidden" name="_csrf" id="csrfPDF4${row.id}">
	                            <input type="hidden" name="hppCode" id="hppCodePDF4${row.id}" />
	                            <input type="hidden" name="pdfID2" value="${row.id}" id="pdfID4${row.id}" />
	                            <button type="submit" class="border-0 bg-transparent h4" onclick="pdfData4(${row.id})">
	                                <i class="text-success fas fa-eye"></i>
	                            </button>
	                        </form>
	                    `;
	                }
	            },
	            { 
                 	data: null,
				    title: 'Actions',
				    className: 'text-center',
				    render: function(data, type, row) {
				        return `
				            <form action="/update/${row.id}" method="post" autocomplete="off" id="editform2${row.id}">
				            	<input type="hidden" name="_csrf" id="csrfEdit2${row.id}">
				                <input type="hidden" name="hppCode" id="hppCodeEdit2${row.id}" />
				                <input type="hidden" name="editID" value="${row.id}" id="editID2${row.id}" />
				                <button type="submit" class="btn btn-primary btn-sm mb-2" style="margin-top: 4px;" onclick="editData2(${row.id})">Edit</button>
				            </form>
				        `;
                	}
            	}
	        ],
	        lengthMenu: [
	            [10, 25, 50, 100, 500],
	            [10, 25, 50, 100, 500]
	        ],
	        searching: true,
	        ordering: true,
	        paging: true,
	        lengthChange: true,
	        pageLength: 10
	    });
	    
	    //Rejected Table
	    $('#rejected-table').DataTable({
	        processing: true,
	        serverSide: true,
	        ajax: {
	            url: '/rejectedStudentData',
	            method: 'GET',
	            data: function(d) {
	            	//console.log(d);
	                return $.extend({}, d, {
	                });
	            },
	            dataSrc: function(response) {
	            	//console.log(response);
	                return response.data;
	            }
	        },
	        columns: [
	        	{ 
	        		data: null,
	                className: "text-center",
	                title: "S. No",
	                render: function (data, type, row, meta) {
	                    return meta.row + meta.settings._iDisplayStart + 1;
	                }
	            },
	            { data: 'id', title: 'ID' , className: 'text-center'},
	            { data: 'firstName', title: 'First Name' , className: 'text-center'},
	            { data: 'lastName', title: 'Last Name' , className: 'text-center'},
	            { data: 'emailAddress', title: 'Email Address' , className: 'text-center'},
	            { data: 'qualification', title: 'Course' , className: 'text-center'},
	            { data: 'college', title: 'College' , className: 'text-center'},
	            { data: 'state', title: 'State' , className: 'text-center'},
	            { data: 'registrationDate', title: 'Registration Date' , className: 'text-center'},
	            { 
	                data: null,
	                title: 'Category Certificate',
	                className: 'text-center',
	                render: function(data, type, row) {
	                    return `
	                        <form action="/displayPDF2/${row.id}" method="post" id="pdfform5${row.id}" name="pdfform5" target="_blank">
	                        	<input type="hidden" name="_csrf" id="csrfPDF5${row.id}">
	                            <input type="hidden" name="hppCode" id="hppCodePDF5${row.id}" />
	                            <input type="hidden" name="pdfID" value="${row.id}" id="pdfID5${row.id}" />
	                            <button type="submit" class="border-0 bg-transparent h4" onclick="pdfData5(${row.id})">
	                                <i class="text-success fas fa-eye"></i>
	                            </button>
	                        </form>
	                    `;
	                }
	            },
	            { 
	                data: null,
	                title: 'Last Masksheet',
	                className: 'text-center',
	                render: function(data, type, row) {
	                    return `
	                        <form action="/displayPDF3/${row.id}" method="post" id="pdfform6${row.id}" name="pdfform6" target="_blank">
	                        	<input type="hidden" name="_csrf" id="csrfPDF6${row.id}">
	                            <input type="hidden" name="hppCode" id="hppCodePDF6${row.id}" />
	                            <input type="hidden" name="pdfID2" value="${row.id}" id="pdfID6${row.id}" />
	                            <button type="submit" class="border-0 bg-transparent h4" onclick="pdfData6(${row.id})">
	                                <i class="text-success fas fa-eye"></i>
	                            </button>
	                        </form>
	                    `;
	                }
	            },
	            { 
				    data: null,
				    title: 'Actions',
				    className: 'text-center',
				    render: function(data, type, row) {
				        return `
				            <form action="/RejectedApproval/${row.id}" method="post" id="reapproveform${row.id}" name="reapproveform">
				                <input type="hidden" name="_csrf" id="csrfReapprove${row.id}">
				                <input type="hidden" name="hppCode" id="hppCodeReapprove${row.id}" />
				                <input type="hidden" name="rejectedStuID" value="${row.id}" id="rejectedStuID${row.id}" />
				                <button class="btn btn-success btn-sm" type="submit" onclick="reapproveStudent(${row.id})">Reapprove</button>
				            </form>
				            <form action="/rejectedDeleted/${row.id}" method="post" id="rejecteddeleteform${row.id}" name="rejecteddeleteform">
				                <input type="hidden" name="_csrf" id="csrfRejectedDelete${row.id}">
				                <input type="hidden" name="hppCode" id="hppCodeRejectedDeleteStu${row.id}" />
				                <input type="hidden" name="rejectedDeleteStuID" value="${row.id}" id="rejectedDeleteStuID${row.id}" />
				                <button type="submit" class="btn btn-danger btn-sm my-1" onclick="rejectedDeleteStuData(${row.id})">Delete</button>
				            </form>
				        `;
				    }
				}
	        ],
	        lengthMenu: [
	            [10, 25, 50, 100, 500],
	            [10, 25, 50, 100, 500]
	        ],
	        searching: true,
	        ordering: true,
	        paging: true,
	        lengthChange: true,
	        pageLength: 10
	    });
	    
	});
	
	function pdfData(id) {
		var pdfID=document.getElementById("pdfID"+id).value;
		var inputCode = "";
		inputCode += getInputCode(pdfID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodePDF"+id).value = hppCode;
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfPDF' + id).value = csrf;
		document.getElementById('pdfform'+id).submit();
		return false; // Prevent default form submission
	}
	
	function pdfData2(id) {
		var pdfID2=document.getElementById("pdfID2"+id).value;
		var inputCode = "";
		inputCode += getInputCode(pdfID2);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodePDF2"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfPDF2' + id).value = csrf;
		
		document.getElementById('pdfform2'+id).submit();
		return false; // Prevent default form submission
	}
	
	function pdfData3(id) {
		var pdfID=document.getElementById("pdfID3"+id).value;
		var inputCode = "";
		inputCode += getInputCode(pdfID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodePDF3"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfPDF3' + id).value = csrf;
		
		document.getElementById('pdfform3'+id).submit();
		return false; // Prevent default form submission
	}
	
	function pdfData4(id) {
		var pdfID2=document.getElementById("pdfID4"+id).value;
		var inputCode = "";
		inputCode += getInputCode(pdfID2);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodePDF4"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfPDF4' + id).value = csrf;
		
		document.getElementById('pdfform4'+id).submit();
		return false; // Prevent default form submission
	}
	
	function pdfData5(id) {
		var pdfID2=document.getElementById("pdfID5"+id).value;
		var inputCode = "";
		inputCode += getInputCode(pdfID2);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodePDF5"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfPDF5' + id).value = csrf;
		
		document.getElementById('pdfform5'+id).submit();
		return false; // Prevent default form submission
	}
	
	function pdfData6(id) {
		var pdfID2=document.getElementById("pdfID6"+id).value;
		var inputCode = "";
		inputCode += getInputCode(pdfID2);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodePDF6"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfPDF6' + id).value = csrf;
		
		document.getElementById('pdfform6'+id).submit();
		return false; // Prevent default form submission
	}
	
	function approveData(id) {
		var stuID=document.getElementById("stuID"+id).value;
		var inputCode = "";
		inputCode += getInputCode(stuID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodeApprove"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfApprove' + id).value = csrf;
		
		document.getElementById('approveform'+id).submit();
		return false; // Prevent default form submission
	}
	
	function rejectData(id) {
		var rejectID=document.getElementById("rejectID"+id).value;
		var inputCode = "";
		inputCode += getInputCode(rejectID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodeReject"+id).value = hppCode;
		
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfReject' + id).value = csrf;
		
		
		document.getElementById('rejectform'+id).submit();
		return false; // Prevent default form submission
	}
	function editData(id) {
		var editID=document.getElementById("editID"+id).value;
		var inputCode = "";
		inputCode += getInputCode(editID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodeEdit"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfEdit' + id).value = csrf;
		
		document.getElementById('editform'+id).submit();
		return false; // Prevent default form submission
	}
	
	function editData2(id) {
		var editID=document.getElementById("editID2"+id).value;
		var inputCode = "";
		inputCode += getInputCode(editID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodeEdit2"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfEdit2' + id).value = csrf;
		
		document.getElementById('editform2'+id).submit();
		return false; // Prevent default form submission
	}
	
	function deleteData(id) {
		var deleteID=document.getElementById("deleteID"+id).value;
		var inputCode = "";
		inputCode += getInputCode(deleteID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodeDelete"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfDelete' + id).value = csrf;
		
		document.getElementById('deleteform'+id).submit();
		return false; // Prevent default form submission
	}
	
	function extendDate(id) {
		var dob = document.getElementById("validTill" + id).value;
		var inputCode = "";
		inputCode += getInputCode(dob);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodeExtend"+id).value = hppCode;
		
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfExtend' + id).value = csrf;		
				
		document.getElementById('extendDateForm'+id).submit();
		return false; // Prevent default form submission
	}
	
	function reapproveStudent(id) {
		var rejectedStuID=document.getElementById("rejectedStuID"+id).value;
		var inputCode = "";
		inputCode += getInputCode(rejectedStuID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodeReapprove"+id).value = hppCode;
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfReapprove' + id).value = csrf;	
		document.getElementById('reapproveform'+id).submit();
		return false; // Prevent default form submission
	}
	
	function rejectedDeleteStuData(id) {
		var rejectedDeleteStuID=document.getElementById("rejectedDeleteStuID"+id).value;
		var inputCode = "";
		inputCode += getInputCode(rejectedDeleteStuID);				
		var hppCode = getHPPCode(origHPPCode, inputCode);
		document.getElementById("hppCodeRejectedDeleteStu"+id).value = hppCode;
		var csrf = document.getElementById('csrf').value;
		document.getElementById('csrfRejectedDelete' + id).value = csrf;	
		document.getElementById('rejecteddeleteform'+id).submit();
		return false; // Prevent default form submission
	}
	
	function ToastMessage() {
		const toastLive = document.getElementById('liveToast')
	   	const toast = new bootstrap.Toast(toastLive)
	   	toast.show()
	}