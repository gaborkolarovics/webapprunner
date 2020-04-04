function GetToken() {
	if (typeof WebAppRunner !== 'undefined')
	{
       	document.getElementById("notificationToken").value = WebAppRunner.getToken();
       	document.getElementById("deviceId").innerHTML = WebAppRunner.getDeviceID();
       	document.getElementById("configuredUrl").innerHTML = WebAppRunner.getURL();
	}
	else
	{
		alert("WebAppRunner not defined :(");
	}
}

function SendMessage() {
	if (typeof WebAppRunner !== 'undefined')
	{
	    var msg = document.getElementById("msgText").value;
       	var token = WebAppRunner.getToken();
	}
	else
	{
		alert("WebAppRunner not defined :(");
	}
}