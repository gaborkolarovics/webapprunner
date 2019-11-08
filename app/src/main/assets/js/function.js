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
	    var title = document.getElementById("msgTitle").value;
	    var url = document.getElementById("msgUrl").value;
       	var token = WebAppRunner.getToken();

       	var request = {token:token,title:title,message:msg,link:url};

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState != 4) return;
            if (this.status == 200) {
                document.getElementById("msgResponse").innerHTML = this.responseText;
            } else {
                document.getElementById("msgResponse").innerHTML = 'ERROR! [' + this.status + ']' + this.responseText;
            }
        };
        document.getElementById("msgResponse").innerHTML = 'Request sent. Wait for response ..';
       	xhttp.open("POST", "https://us-central1-webapprunner-1093.cloudfunctions.net/sendPushNotification", true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send(JSON.stringify(request));
	}
	else
	{
		alert("WebAppRunner not defined :(");
	}
}