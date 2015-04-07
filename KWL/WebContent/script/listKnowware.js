function loadKnowware(username, session, errno, filename) {
	$.ajax({
			url: "/KWL/MainServlet",
			data: {
				type : 9,
				username : username,
				session : session
			},
//			processData : false,
			type : "POST",
			async : false,
			dataType : "json",
			success : function( json) {
				if (json.res != 0) {
					alert(json.data);
				} else {
					processResult(json, errno, filename);
				}
			},
			error : function( xhr, status ) {
				alert("sorry, there was a problem");
			},
			complete : function(xhr, status) {
//				alert("The Request is complete!");
			}
	});
}

function processResult(json, errno, filename) {
	var data = json.data;
	if (errno != null) {
		$('#large').html(filename+' is not valid owl!!!');
	}
	var hashtable={"knowwareName":"知件名称：","description":"知件描述：","tag":"知件标签：","owl":"OWL文件：","file":"辅助文件："};
	$.each(data, function(i, knowware) {
		$('#body').append('<tr><td colspan=\"2\" align=\"center\" style=\"padding-top:10px;\"><table width=\"700\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#f3f3f3\" style=\"padding:10px 10px 0px 10px; display:block; \" class=\"table3\" id=\"'+i+'\"></table></td></tr>');
		$.each(knowware,function(key, value) {
			$('#'+i).append('<tr id = \"'+key+i+'\" ></tr>');
			$('#'+key+i).append('<td align=\"left\" class=\"table3left\"><p><b>'+hashtable[key]+'</b></p></td>');
			if (key == 'file') {
				$.each(value, function(j, file) {
					$('#'+key+i).append('<td align=\"left\" class=\"table3right\"><a href=\"javascript:download(\''+file+'\',\'commonfile\')">'+file+'</a></td>');
				});
			} else {
				if (key == 'owl') {
					$('#'+key+i).append('<td align=\"left\" class=\"table3right\"><a href=\"javascript:download(\''+value+'\',\'local\')">'+value+'</a></td>');
				}
				else {
					$('#'+key+i).append('<td align=\"left\" class=\"table3right\">'+value+'</td>');
				}
			}
		});
	});
}

function download(filename,pos){
	var ifr=document.createElement("iframe");
	ifr.src = "download.jsp?filename="+filename+"&pos="+pos;
	document.body.appendChild(ifr);
	ifr.hidden = true;
}