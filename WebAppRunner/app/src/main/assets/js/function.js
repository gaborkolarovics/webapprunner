function GetToken() {
	if (typeof WebAppRunner !== 'undefined')
	{
       	document.getElementById("notificationToken").innerHTML = WebAppRunner.getToken();
       	document.getElementById("deviceId").innerHTML = WebAppRunner.getDeviceID();
       	document.getElementById("configuredUrl").innerHTML = WebAppRunner.getURL();
	}
	else
	{
		alert("WebAppRunner not defined :(");
	}
}
