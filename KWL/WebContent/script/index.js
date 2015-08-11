	function submit(){
		var form = document.getElementById("form");
		form.submit();
	}
	function displaysearch(){
		var searchblock = document.getElementById("search");
		var display = document.getElementById("display");
		var publish = document.getElementById("publish");
		searchblock.style.display= "";
		display.style.display = "none";
		publish.style.display = "none";
	}
	function displaydisplay(){
		var searchblock = document.getElementById("search");
		var display = document.getElementById("display");
		var publish = document.getElementById("publish");
		display.style.display= "";
		searchblock.style.display = "none";
		publish.style.display = "none";
	}
	function displaypublish(){
		var searchblock = document.getElementById("search");
		var display = document.getElementById("display");
		var publish = document.getElementById("publish");
		publish.style.display ="";
		searchblock.style.display="none";
		display.style.display = "none";
		
	}
	