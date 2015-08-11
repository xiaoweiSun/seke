$(
		initModify
);

function initModify() {
	var modify = getParameter(window.location.search,"modify");
	if (modify != null && modify == "yes") {
		console.log(modify);
		var fieldName = getParameterFromCookie("fieldName");
		var tag = getParameterFromCookie("tag");
		var description = getParameterFromCookie("description");
		console.log(fieldName+" "+tag+" "+description);
		$('#title').val(fieldName);
		$('#tag').val(tag);
		$('#description').val(description);
	}
};
function submitField() {
	if ($('#title').val()==null ||$('#title').val()=="") {
		alert("领域知识库名称必填");
		return ;
	}
	var fieldName = $('#title').val();
	var tag = $('#tag').val();
	var description = $('#description').val();
	var isFinished = 'building';
	var username = getParameterFromCookie('username');
	var session = getParameterFromCookie('session');
	if ( username == null || session == null) {
		alert("请先登录!");
		return;
	}
	doSubmit(username, session, fieldName, tag, description, isFinished);
};


function doSubmit(username, session, fieldName, tag, description, isFinished) {
	$.ajax({
		url: "/KWL/MainServlet",
		data: {
			type : 22,
			username : username,
			session : session,
			fieldName : fieldName,
			tag : tag,
			description : description,
			isFinished : isFinished
		},
//		processData : false,
		type : "POST",
		async : false,
		dataType : "json",
		contentType:'application/x-www-form-urlencoded; charset=utf-8',
		success : function( json) {
			if (json.res != 0) {
				alert(json.data);
			} else {
				alert(json.data);
				showtable('2');
			}
		},
		error : function( xhr, status ) {
			alert("对不起，提交领域库出现问题!");
		},
		complete : function(xhr, status) {
//			alert("The Request is complete!");
		}
});
};