$( 
		displayOWL		
);
function displayOWL() {
	var filename = getParameter(window.location.search, "filename");
	$('#filename').html('<h2>'+filename+'</h2>');
}
var submit = function() {
	//��ò���
	console.log("submit");
	var sparqlIn = $('#sparqlIn').val();
	var querytype = $('input[type="radio"][name="querytype"]:checked').val();
	console.log(sparqlIn);
	console.log(querytype);
	var filename = getParameter(window.location.search, "filename");
	var dirtype = getParameter(window.location.search, "dirtype");
	console.log(dirtype);
	$('#filename').html('<h2>'+filename+'</h2>');
	var username = getParameterFromCookie("username");
	var session = getParameterFromCookie("session");
	query(username, session, sparqlIn, querytype, filename, dirtype);
};


function query(username, session, sparqlIn, querytype, filename, dirtype) {
	$.ajax({
		//������շ���Ը�·��
		url: "/KWL/MainServlet",
		data: {
			type : 18,
			username : username,
			session : session,
			sparqlIn : sparqlIn,
			querytype : querytype,
			filename : filename,
			dirtype : dirtype
		},
//		processData : false,
		//POST�����ύ
		type : "POST",
		//ͬ��
		async : false,
		//�ص���ݸ�ʽ
		dataType : "json",
		//���óɹ�����
		success : function( json) {
			if (json.res != 0) {
				//��������ʧ�� ���߲���json��ʽ
				alert(json.data);
			} else {
				showResult(json);
			}
		},
		//����ʧ�ܺ���
		error : function( xhr, status ) {
			alert("对不起，SPARQL查询出现了问题!");
		},
		//������ɺ���
		complete : function(xhr, status) {
//			alert("The Request is complete!");
		}
});
};
function selectType(e) {
	querytype = e.target.value;
}
function showResult(json) {
	$('#result').val(json.data);
};
function test() {
	if($('#query')!=null) alert("success");
};

//$(document).ready(
//		$('#query').click(submit)
//);