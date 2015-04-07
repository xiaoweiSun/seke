upload = function() {
	checkLoginState(uploadDo);
};
uploadDo = function(){
	var form = document.getElementById("publish");
	var username = getParameterFromCookie("username");
	var session = getParameterFromCookie("session");
	form.action="MainServlet?username="+username+"&session="+session+"&type=8";
	form.submit();
};
reset = function(){
	form.reset();
};
//选中本地上传 只要本地上传可见   服务器上传不可见就可以
function selectClient() {
	$('#fromserver').hide();
	$('#fromclient').show();
};

function selectServer() {
	$('#fromserver').show();
	$('#fromclient').hide();
	getFiles();
};

function getFiles() {
	$.ajax({
		//������շ���Ը�·��
		url: "/KWL/MainServlet",
		data: {
			type : 21,
		},
//		processData : false,
		type : "POST",
		async : false,
		dataType : "json",
		success : function( json) {
			showOwl(json);
		},
		error : function( xhr, status ) {
			alert("sorry, there was a problem");
		},
		complete : function(xhr, status) {
//			alert("The Request is complete!");
		}
	});
};
function showOwl(json) {
	$('#serverfile').html("");
	$.each(json, function(j, file) {
		if (j==0) {
			$('#serverfile').append('<option selected ="selected" value = "'+file+'">'+file+'</option>');
		} else {
			$('#serverfile').append('<option value = "'+file+'">'+file+'</option>');
		}
	});
	
};

function select() {
	var from = $('input[type="radio"][name="from"]:checked').val();
	if (from == 'server') {
		selectServer();
	} else {
		selectClient();
	}
};

