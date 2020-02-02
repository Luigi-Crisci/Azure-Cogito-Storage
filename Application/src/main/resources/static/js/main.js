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

function functionDelete(event,str){
	
	var divInputText = $(event).parent().parent().children().eq(1).children("input[type=text]");
    var textLabel = divInputText.parent().children().eq(1).children("label");

	console.log("File to be deleted: " + textLabel.text());

	var form=$('#createDirForm')[0];
	var data=new FormData(form);
	data.set("file",str+textLabel.text());

	$.ajax({
		type: "POST",
		url: "/account/delete",
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
			divInputText.parent().parent().remove();
        },
        error: function (e) {
        	$("#result").text(e.responseText);
            console.log("ERROR : ", e);
        }
	});
}

var oldFilenameRename;
var lastRenamedBox = null;

function showRename(event) {
	
	//setta ad hidden la label del truename
	//var divInputText = $(event).parent().parent().children().eq(1).children("input[type=text]");

	//Hide all others rename box
	if(lastRenamedBox != null && lastRenamedBox.prop("disabled") == false){
		lastRenamedBox.prop("hidden",true);
		lastRenamedBox.parent().children().eq(1).children("label").prop("hidden",false);
	}
		

	divInputText = $(event).parent().parent().children().eq(1).children("input[type=text]");
	lastRenamedBox = divInputText; //I save it for following calls

	console.log(divInputText);
	divInputText.removeAttr('hidden');
	
	//aggiunge il truename del file nella inputbox
	var textLabel = divInputText.parent().children().eq(1).children("label");
	divInputText.val(textLabel.text());

	oldFilenameRename = textLabel.text(); //Need it when rename function is called
	console.log("Old filename: " + oldFilenameRename);
	textLabel.prop("hidden", true);
}


function rename(event,dirName) {
	var code = event.which || event.keyCode;
	if(code != 13)
		return;

	var currentlyRenamingBox = lastRenamedBox;
	var currentlyOldFilename = oldFilenameRename;

	currentlyRenamingBox.prop("disabled",true);

	var form=$('#createDirForm')[0];
	var data=new FormData(form);
	data.set("oldFilename",dirName + currentlyOldFilename);
	data.set("newFilename",dirName + currentlyRenamingBox.val());
	data.set("overwrite",true);

	console.log(data);

	
	$.ajax({
		type: "POST",
		url: "/account/rename",
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
			var textLabel = currentlyRenamingBox.parent().children().eq(1).children("label");
			textLabel.text(currentlyRenamingBox.val());
			currentlyRenamingBox.prop("hidden",true);
			textLabel.prop("hidden",false);
			currentlyRenamingBox.removeAttr("disabled");

			//Set new download key
			var keyObj = jQuery.parseJSON(data);
			console.log(keyObj.key);

			console.log(textLabel.parent());
			textLabel.parent().prop("href",keyObj.key);

        },
        error: function (e) {
        	$("#result").text(e.responseText);
			console.log("ERROR : ", e);
			var textLabel = currentlyRenamingBox.parent().children().eq(1).children("label");
			currentlyRenamingBox.prop("hidden",true);
			textLabel.prop("hidden",false);
			currentlyRenamingBox.removeAttr("disabled");
        }
	});

	
}