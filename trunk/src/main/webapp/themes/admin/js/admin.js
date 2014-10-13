$(function(){
	("[onclick='change_picktime()']").each(function(){
	this.className = "js-datetime";
});

function getRefString(url){          
    var ref=window.location.href;
	if(url.indexOf("?")>0){
		url=url+"&ref="+escape(ref);
	}
	window.location.href=url; 
}


