$(document).ready(function () {
	
	let searchParams = new URLSearchParams(window.location.search);
	var currentDir="";
	if(searchParams.has("dir"))
        currentDir=searchParams.get("dir");

    $("#btnSubmit").click(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        fire_ajax_submit(currentDir);
    });
    
    $("#createDirSubmit").click(function (event){
    	event.preventDefault();
    	create_dir_ajax(currentDir);
    });
    
 
});

function create_dir_ajax(currentDir){
	var form=$('#createDirForm')[0];
	var data=new FormData(form);
	data.set("dir",currentDir);
	  $("#createDirSubmit").prop("disabled", true);

	    $.ajax({
	        type: "POST",
	        //enctype: 'multipart/form-data',
	        url: "/account/createDir",
	        data: data,
	        //http://api.jquery.com/jQuery.ajax/
	        //https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
	        processData: false, //prevent jQuery from automatically transforming the data into a query string
	        contentType: false,
	        cache: false,
	        timeout: 600000,
	        success: function (data) {

	            $("#result").text(data);
	            console.log("SUCCESS : ", data);
	            $("#createDirSubmit").prop("disabled", false);

	        },
	        error: function (e) {

	            $("#result").text(e.responseText);
	            console.log("ERROR : ", e);
	            $("#createDirSubmit").prop("disabled", false);

	        }
	    });
	
}

function fire_ajax_submit(currentDir) {

    // Get form
    var form = $('#fileUploadForm')[0];
    var data = new FormData(form);
    data.append("CustomField", "This is some extra data, testing");
    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/account/upload?dir="+currentDir,
        data: data,
        //http://api.jquery.com/jQuery.ajax/
        //https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {

            $("#result").text(data);
            console.log("SUCCESS : ", data);
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmit").prop("disabled", false);

        }
    });
}




function myFunction(event) {
	
	//setta ad hidden la label del truename
	//var divInputText = $(event).parent().parent().children().eq(1).children("input[type=text]");
	var divInputText = $(event).parent().parent().children().eq(1).children("form").children("input[type=text]");
	console.log(divInputText);
	divInputText.removeAttr('hidden');
	
	//aggiunge il truename del file nella inputbox
	var textLabel = divInputText.parent().parent().children().eq(1).children("label");
	divInputText.val(textLabel.text());
	
	textLabel.prop("hidden", !this.checked);
}