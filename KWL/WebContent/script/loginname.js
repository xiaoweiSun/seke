displayName = function() {
	console.log(search);
	var username = getParameterFromCookie("username");
	if (username != null) {
		console.log(username);
		$('#loginname').append('<p>a</p>');
	}
};

$().ready(
		displayName()
);